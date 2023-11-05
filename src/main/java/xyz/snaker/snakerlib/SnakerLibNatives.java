package xyz.snaker.snakerlib;

import java.io.File;
import java.net.URL;

/**
 * Created by SnakerBone on 4/11/2023
 **/
public class SnakerLibNatives
{
    SnakerLibNatives()
    {
        if (SnakerLib.STACK_WALKER.getCallerClass() != SnakerLib.class) {
            throw new IllegalCallerException();
        }
    }

    static {
        initialize();
    }

    private static void initialize()
    {
        URL url = SnakerLibNatives.class.getClassLoader().getResource("snkr.dll");

        if (url == null) {
            SnakerLib.LOGGER.error("Could not load natives: snkr.dll does not exist");
            return;
        }

        try {
            File file = new File(url.getFile()).getAbsoluteFile();
            String path = file.getAbsolutePath().replaceAll("%\\d+!\\\\", "");
            System.load(path);
        } catch (Exception e) {
            SnakerLib.LOGGER.errorf("Could not load natives: []", e.getMessage());
            return;
        }

        SnakerLib.LOGGER.infof("Loaded natives for SnakerLib");
    }

    /**
     * Goodbye, World!
     **/
    public native void goodbyeWorld();
}
