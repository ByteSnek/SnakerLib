package xyz.snaker.snakerlib.utility;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.internal.UncaughtExceptionThread;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.lwjgl.glfw.GLFW;

/**
 * Created by SnakerBone on 15/08/2023
 **/
public class Keyboard
{
    /**
     * The current window handle
     **/
    private static long handle;

    static {
        initialize();
    }

    /**
     * Checks if a key is being pressed
     *
     * @param key A {@link GLFW} printable key
     * @return True if the key is currently being pressed
     **/
    public static boolean isKeyDown(int key)
    {
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
    }

    /**
     * Checks if a key is being pressed
     *
     * @param handle The window of the current {@link GLFW} window
     * @param key    A {@link GLFW} printable key
     * @return True if the key is currently being pressed
     **/
    public static boolean isKeyDown(long handle, int key)
    {
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
    }

    /**
     * Checks if a key is not being pressed
     *
     * @param key A {@link GLFW} printable key
     * @return True if the key is currently not being pressed
     **/
    public static boolean isKeyUp(int key)
    {
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_RELEASE;
    }

    /**
     * Checks if a key is not being pressed
     *
     * @param handle The window of the current {@link GLFW} window
     * @param key    A {@link GLFW} printable key
     * @return True if the key is currently not being pressed
     **/
    public static boolean isKeyUp(long handle, int key)
    {
        return GLFW.glfwGetKey(handle, key) == GLFW.GLFW_RELEASE;
    }

    /**
     * Checks if the debug key is down
     *
     * @return True if the debug key is down is developer environment
     **/
    public static boolean isDebugKeyDown()
    {
        return isKeyDown(SnakerLib.getDebugKey()) && !FMLEnvironment.production;
    }

    /**
     * Attempts to set the current window handle
     **/
    static void initialize()
    {
        long glfwHandle = GLFW.glfwGetCurrentContext();
        long mcHandle = 0;

        if (FMLEnvironment.dist.isClient()) {
            mcHandle = Minecraft.getInstance().getWindow().getWindow();
        }

        if (glfwHandle == 0 || glfwHandle != mcHandle) {
            if (mcHandle == 0) {
                IllegalStateException exception = new IllegalStateException("No valid window could be found. Key state checking will be unavailable");
                UncaughtExceptionThread.createAndRun(exception);

                return;
            }

            handle = mcHandle;

            return;
        }

        handle = glfwHandle;
    }
}
