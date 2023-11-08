package xyz.snaker.snakerlib.config;

import net.minecraftforge.common.ForgeConfigSpec;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by SnakerBone on 29/05/2023
 **/
public class SnakerConfig
{
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common
    {
        public final ForgeConfigSpec.BooleanValue forceCrashJvmKeyBindings;
        public final ForgeConfigSpec.BooleanValue removeJvmCrashFilesOnStartup;
        public final ForgeConfigSpec.BooleanValue removeMinecraftCrashFilesOnStartup;
        public final ForgeConfigSpec.BooleanValue goodbyeWorldKeyBindings;

        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("common");
            forceCrashJvmKeyBindings = builder.comment("Should ALT + SHIFT + F4 force crash the JVM for debugging purposes (default: false)").define("forceCrashJvmKeyBindings", false);
            removeJvmCrashFilesOnStartup = builder.comment("Should JVM crash files be deleted on startup for optimization purposes (default: true)").define("removeJvmCrashFilesOnStartup", true);
            removeMinecraftCrashFilesOnStartup = builder.comment("Should Minecraft crash files be deleted on startup for optimization purposes (default: false)").define("removeMinecraftCrashFilesOnStartup", false);
            goodbyeWorldKeyBindings = builder.comment("Should Right CTRL + Scroll Lock say goodbye world (default: false)").define("goodbyeWorldKeyBindings", false);
            builder.pop();
        }
    }
}
