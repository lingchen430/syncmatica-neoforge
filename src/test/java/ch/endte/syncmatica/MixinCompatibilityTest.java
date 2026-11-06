package ch.endte.syncmatica;

import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import static org.junit.jupiter.api.Assertions.*;

/** Checks client injection signatures against the actual released dependency jars without loading graphics. */
class MixinCompatibilityTest {
    private static final Path ROOT = Path.of(System.getProperty("port.projectDir"));
    private final List<Path> jars = List.of(
        Path.of("libs/forgematica-0.5.1+mc26.1.2.jar"),
        Path.of(System.getProperty("port.mafglibJar")),
        Path.of(System.getProperty("port.minecraftJar")));

    private ClassNode read(String name) throws Exception {
        var local = ROOT.resolve(Path.of("build/classes/java/main", name + ".class"));
        if (Files.exists(local)) return parse(Files.readAllBytes(local));
        for (var path : jars) {
            try (var jar = new JarFile(ROOT.resolve(path).toFile())) {
                var entry = jar.getJarEntry(name + ".class");
                if (entry != null) return parse(jar.getInputStream(entry).readAllBytes());
            }
        }
        throw new AssertionError("Missing target class: " + name);
    }

    private static ClassNode parse(byte[] bytes) {
        var node = new ClassNode();
        new ClassReader(bytes).accept(node, 0);
        return node;
    }

    private static Object value(AnnotationNode node, String key) {
        if (node.values == null) return null;
        for (int i = 0; i < node.values.size(); i += 2)
            if (node.values.get(i).equals(key)) return node.values.get(i + 1);
        return null;
    }

    @Test
    @SuppressWarnings("unchecked")
    void selectedMaFgLibTooltipHooksMatchNeoForgeItemStack() throws Exception {
        var mixin = read("fi/dy/masa/malilib/mixin/item/MixinItemStack");
        var target = read("net/minecraft/world/item/ItemStack");
        int checkedHooks = 0;
        for (var callback : mixin.methods) {
            var annotations = new ArrayList<AnnotationNode>();
            if (callback.visibleAnnotations != null) annotations.addAll(callback.visibleAnnotations);
            if (callback.invisibleAnnotations != null) annotations.addAll(callback.invisibleAnnotations);
            for (var inject : annotations) {
                if (!inject.desc.endsWith("/Inject;")) continue;
                for (String selector : (List<String>) value(inject, "method")) {
                    int descriptor = selector.indexOf('(');
                    String methodName = descriptor < 0 ? selector : selector.substring(0, descriptor);
                    var method = target.methods.stream().filter(m -> m.name.equals(methodName)
                        && (descriptor < 0 || m.desc.equals(selector.substring(descriptor))))
                        .findFirst().orElseThrow(() -> new AssertionError("MaFgLib tooltip method missing: " + selector));
                    for (var at : (List<AnnotationNode>) value(inject, "at")) {
                        if (!"INVOKE".equals(value(at, "value"))) continue;
                        String invocation = (String) value(at, "target");
                        int split = invocation.indexOf(';');
                        int args = invocation.indexOf('(', split);
                        String owner = invocation.substring(1, split);
                        String name = invocation.substring(split + 1, args);
                        String desc = invocation.substring(args);
                        int count = 0;
                        for (var instruction : method.instructions) {
                            if (instruction instanceof MethodInsnNode call && call.owner.equals(owner)
                                    && call.name.equals(name) && call.desc.equals(desc)) count++;
                        }
                        Integer ordinal = (Integer) value(at, "ordinal");
                        assertTrue(count > (ordinal == null ? -1 : ordinal) && count > 0,
                            "MaFgLib tooltip invocation missing: " + selector + " -> " + invocation);
                        checkedHooks++;
                    }
                }
            }
        }
        assertTrue(checkedHooks > 0);
    }

    @Test
    @SuppressWarnings("unchecked")
    void allActiveMixinTargetsAndCallbackSignaturesMatch() throws Exception {
        int verified = 0;
        for (var config : List.of("syncmatica.mixin.json", "syncmatica.litematica_mixin.json")) {
            var json = JsonParser.parseString(Files.readString(ROOT.resolve(Path.of("src/main/resources", config)))).getAsJsonObject();
            for (var side : List.of("mixins", "client", "server")) {
                for (var entry : json.getAsJsonArray(side)) {
                    var name = (json.get("package").getAsString() + "." + entry.getAsString()).replace('.', '/');
                    var mixin = read(name);
                    var annotation = mixin.invisibleAnnotations.stream()
                        .filter(a -> a.desc.endsWith("/Mixin;")).findFirst().orElseThrow();
                    var target = read(((List<Type>) value(annotation, "value")).getFirst().getInternalName());
                    for (var field : mixin.fields) {
                        if (field.visibleAnnotations != null && field.visibleAnnotations.stream().anyMatch(a -> a.desc.endsWith("/Shadow;")))
                            assertTrue(target.fields.stream().anyMatch(f -> f.name.equals(field.name) && f.desc.equals(field.desc)), name + " shadow " + field.name);
                    }
                    for (var method : mixin.methods) {
                        var annotations = new ArrayList<AnnotationNode>();
                        if (method.visibleAnnotations != null) annotations.addAll(method.visibleAnnotations);
                        if (method.invisibleAnnotations != null) annotations.addAll(method.invisibleAnnotations);
                        for (var inject : annotations) {
                            if (!inject.desc.endsWith("/Inject;")) continue;
                            for (var selector : (List<String>) value(inject, "method")) {
                                int descriptor = selector.indexOf('(');
                                String methodName = descriptor < 0 ? selector : selector.substring(0, descriptor);
                                var candidates = target.methods.stream().filter(m -> m.name.equals(methodName)
                                    && (descriptor < 0 || m.desc.equals(selector.substring(descriptor)))).toList();
                                assertFalse(candidates.isEmpty(), name + " missing method " + selector);
                                var args = Type.getArgumentTypes(method.desc);
                                if (args.length > 1) {
                                    assertTrue(candidates.stream().anyMatch(m -> {
                                        var targetArgs = Type.getArgumentTypes(m.desc);
                                        if (targetArgs.length != args.length - 1) return false;
                                        for (int i = 0; i < targetArgs.length; i++) if (!targetArgs[i].equals(args[i])) return false;
                                        return true;
                                    }), name + " incompatible callback " + method.name + method.desc);
                                }
                                verified++;
                            }
                        }
                    }
                }
            }
        }
        assertTrue(verified >= 10);
        System.out.println("Validated " + verified + " active mixin injection signatures");
    }
}
