package snaker.snakerlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Created by SnakerBone on 29/05/2023
 **/
public class CommonConfig
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG_SPEC;
    public static final ForgeConfigSpec.BooleanValue PLAYER_VULNERABLE_IN_CREATIVE;
    public static final ForgeConfigSpec.BooleanValue FORCE_CRASH_JVM_KEY_BINDINGS;
    public static final ForgeConfigSpec.BooleanValue REMOVE_JVM_CRASH_FILES_ON_STARTUP;
    public static final ForgeConfigSpec.BooleanValue REMOVE_MINECRAFT_CRASH_FILES_ON_STARTUP;

    static {
        BUILDER.push("common");
        PLAYER_VULNERABLE_IN_CREATIVE = BUILDER.comment("Should mobs target the player when in creative mode (default: true)").define("PlayerVulnerableInCreative", true);
        FORCE_CRASH_JVM_KEY_BINDINGS = BUILDER.comment("Should Left Shift + F4 force crash the JVM for debugging purposes (default: true)").define("ForceCrashJVMKeyBindings", true);
        REMOVE_JVM_CRASH_FILES_ON_STARTUP = BUILDER.comment("Should JVM crash files be deleted on startup for optimization purposes (default: true)").define("DeleteJVMCrashFilesOnStartup", true);
        REMOVE_MINECRAFT_CRASH_FILES_ON_STARTUP = BUILDER.comment("Should Minecraft crash files be deleted on startup for optimization purposes (default: false)").define("DeleteMinecraftCrashFilesOnStartup", false);
        BUILDER.pop();
        CONFIG_SPEC = BUILDER.build();
    }
}
