package snaker.snakerlib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import snaker.snakerlib.network.Network;

import java.util.HashMap;

@Mod(SnakerLib.MODID)
public class SnakerLib
{
    public static final String MODID = "snakerlib";
    public static final HashMap<Integer, String> DEFAULT_DEPENDANTS = new HashMap<>();
    public static final HashMap<Integer, String> EXTERNAL_DEPENDANTS = new HashMap<>();
    public static final Logger LOGGER = LogManager.getLogger();

    public SnakerLib()
    {
        MinecraftForge.EVENT_BUS.register(this);
        Network.initialize();
    }
}
