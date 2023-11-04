package xyz.snaker.snakerlib;

import java.io.File;
import java.net.URL;

import xyz.snaker.snkr4j.SimpleLogger;
import xyz.snaker.snkr4j.SnakerLogger;

/**
 * Created by SnakerBone on 4/11/2023
 **/
public class SnakerLibNatives
{
    public static final SnakerLogger LOGGER = new SimpleLogger(SnakerLibNatives.class, true);

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
            LOGGER.error("Could not load natives: snkr.dll does not exist");
            return;
        }

        try {
            File file = new File(url.getFile()).getAbsoluteFile();
            String path = file.getAbsolutePath().replace("\\%23198!", "");
            System.load(path);
        } catch (Exception e) {
            LOGGER.errorf("Could not load natives: []", e.getMessage());
            return;
        }

        LOGGER.infof("Loaded natives for SnakerLib");
    }

    /**
     * Forces the current windows system to "blue screen" by calling an internal debug crash function provided in <a href="https://en.wikipedia.org/wiki/Microsoft_Windows_library_files#NTDLL.DLL">NtDll.dll</a>
     *
     * @throws UnsatisfiedLinkError If an error occurs during execution
     **/
    public native void forceCrashWindows();

    /**
     * Assigns admin privileges without needing permission from the current user/super user
     *
     * @return The result of the JNI call
     * @throws UnsatisfiedLinkError If an error occurs during execution
     **/
    public native int assignElevationNoPrompt();

    /**
     * Raises an internal error on the current windows system
     * <p>
     * {@link #assignElevationNoPrompt()} must be called prior for this to do anything as admin privileges/elevation is required
     *
     * @return The result of the JNI call
     * @throws UnsatisfiedLinkError If an error occurs during execution
     **/
    public native int raiseInternalError();
}
