package snaker.snakerlib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snaker.snakerlib.network.Watchdog;
import software.bernie.geckolib3.GeckoLib;

@Mod(SnakerLib.MODID)
public class SnakerLib
{
    public static final String MODID = "snakerlib";
    public static final String[] DEPENDANTS = {"forge", "snakerbone", "azcray"};
    public static final Logger LOGGER = LogManager.getLogger();

    public SnakerLib()
    {
        MinecraftForge.EVENT_BUS.register(this);
        GeckoLib.initialize();
        Watchdog.initialize();
    }
}
