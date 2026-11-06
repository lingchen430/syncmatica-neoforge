package ch.endte.syncmatica.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;

public class StringTools
{
    public static String getHexString(byte[] bytes) {
        List<String> list = new ArrayList<>();
        for (byte b : bytes) {
            list.add(String.format("%02x", b));
        }
        return String.join(" ", list);
    }

    public static String getModVersion(String modid)
    {
        final var CONTAINER = ModList.get().getModContainerById(modid);
        if (CONTAINER.isPresent())
        {
            return CONTAINER.get().getModInfo().getVersion().toString();
        }
        else return "?";
    }
}
