package ch.endte.syncmatica;

import ch.endte.syncmatica.network.PacketType;
import ch.endte.syncmatica.network.SyncmaticaPacket;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(EphemeralTestServerProvider.class)
class PortIntegrationTest {
    @Test
    void placementMetadataRetainsPositionRotationAndOwner(MinecraftServer server) {
        var context = Syncmatica.getContext(Syncmatica.SERVER_CONTEXT);
        var id = java.util.UUID.randomUUID();
        var hash = java.util.UUID.randomUUID();
        var original = new ch.endte.syncmatica.data.ServerPlacement(id, "example.litematic",
            "测试投影", hash, ch.endte.syncmatica.extended_core.PlayerIdentifier.MISSING_PLAYER);
        original.move("minecraft:overworld", new net.minecraft.core.BlockPos(-123, 75, 456),
            net.minecraft.world.level.block.Rotation.CLOCKWISE_90,
            net.minecraft.world.level.block.Mirror.FRONT_BACK);
        var restored = ch.endte.syncmatica.data.ServerPlacement.fromJson(original.toJson(), context);
        assertNotNull(restored);
        assertEquals(id, restored.getId());
        assertEquals(hash, restored.getHash());
        assertEquals(original.getPosition(), restored.getPosition());
        assertEquals(original.getDimension(), restored.getDimension());
        assertEquals(original.getRotation(), restored.getRotation());
        assertEquals(original.getMirror(), restored.getMirror());
        assertEquals(original.getOwner().uuid, restored.getOwner().uuid);
        assertEquals(original.getName(), restored.getName());
    }

    @Test
    void dedicatedServerStartsWithoutClientMods(MinecraftServer server) {
        assertTrue(server.isRunning());
        assertNotNull(Syncmatica.getContext(Syncmatica.SERVER_CONTEXT));
        assertTrue(Syncmatica.getContext(Syncmatica.SERVER_CONTEXT).isStarted());
        assertNotNull(server.getCommands().getDispatcher().getRoot().getChild("syncmatica"));
    }

    @Test
    void payloadRoundTripKeepsFabricWireFormatAndSupportsRepeatedEncoding(MinecraftServer server) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf first = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf second = new FriendlyByteBuf(Unpooled.buffer());
        try {
            data.writeUtf("0.3.20");
            var payload = new SyncmaticaPacket.Payload(new SyncmaticaPacket(PacketType.REGISTER_VERSION.getId(), data));
            SyncmaticaPacket.Payload.CODEC.encode(first, payload);
            SyncmaticaPacket.Payload.CODEC.encode(second, payload);
            assertEquals(0, data.readerIndex());
            assertEquals(first, second);
            assertEquals(PacketType.REGISTER_VERSION.getId(), second.readIdentifier());
            assertEquals("0.3.20", second.readUtf());
            var decoded = SyncmaticaPacket.Payload.CODEC.decode(first);
            try {
                assertEquals(PacketType.REGISTER_VERSION, decoded.data().getType());
                assertEquals("0.3.20", decoded.data().getPacket().readUtf());
                assertEquals(0, first.readableBytes());
            } finally { decoded.data().getPacket().release(); }
        } finally { data.release(); first.release(); second.release(); }
    }
}
