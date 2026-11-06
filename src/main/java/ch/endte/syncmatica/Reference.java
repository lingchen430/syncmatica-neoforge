package ch.endte.syncmatica;

import java.nio.file.Path;
import ch.endte.syncmatica.util.StringTools;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.minecraft.SharedConstants;

/**
 * Main (Generic) Reference Calls --
 * Should use the "Context" versions of some of these for their respective context.
 * These are used to help control what contexts get registered and when (Server versus Client)
 */
public class Reference
{
    public static final String MOD_ID = "syncmatica";
    public static final String MOD_NAME = "Syncmatica";
    public static final String MOD_VERSION = StringTools.getModVersion(MOD_ID); // No more manually typing in the version # :)
    public static final String MC_VERSION = SharedConstants.getCurrentVersion().id();
    private static final Dist MOD_ENV = FMLEnvironment.getDist();
    public static final boolean MOD_DEBUG = false;

    // Fixes various file saving problems by using the FAPI GameDir/ConfigDir instead of "."
    public static final Path GAME_ROOT = FMLPaths.GAMEDIR.get();
    public static final Path CONFIG_ROOT = FMLPaths.CONFIGDIR.get();

    private static boolean DEDICATED_SERVER = false;
    private static boolean INTEGRATED_SERVER = false;
    private static boolean OPEN_TO_LAN = false;

    public static boolean isClient()
    {
        return MOD_ENV == Dist.CLIENT;
    }
    public static boolean isServer()
    {
        return MOD_ENV == Dist.DEDICATED_SERVER;
    }
    public static boolean isDedicatedServer()
    {
        return DEDICATED_SERVER;
    }
    public static boolean isIntegratedServer()
    {
        return INTEGRATED_SERVER;
    }
    public static boolean isOpenToLan()
    {
        return OPEN_TO_LAN;
    }

    public static void setDedicatedServer(boolean toggle)
    {
        DEDICATED_SERVER = toggle;
    }
    public static void setIntegratedServer(boolean toggle)
    {
        INTEGRATED_SERVER = toggle;
    }
    public static void setOpenToLan(boolean toggle)
    {
        OPEN_TO_LAN = toggle;
    }
}
