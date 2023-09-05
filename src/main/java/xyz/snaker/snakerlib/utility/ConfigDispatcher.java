package xyz.snaker.snakerlib.utility;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Created by SnakerBone on 5/09/2023
 **/
public class ConfigDispatcher
{
    private final ModConfig.Type type;
    private final ForgeConfigSpec spec;
    private final String fileName;

    private boolean isRegistered;

    public ConfigDispatcher(ModConfig.Type type, ForgeConfigSpec spec, String fileName)
    {
        this.type = type;
        this.spec = spec;
        this.fileName = fileName;
    }

    public static void direct(ModConfig.Type type, ForgeConfigSpec spec, String fileName)
    {
        ConfigDispatcher dispatcher = new ConfigDispatcher(type, spec, fileName);
        dispatcher.register();
    }

    public void register()
    {
        if (isRegistered) {
            return;
        }
        ModLoadingContext.get().registerConfig(type, spec, fileName);
        isRegistered = true;
    }

    public boolean isRegistered()
    {
        return isRegistered;
    }
}
