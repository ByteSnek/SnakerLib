package xyz.snaker.snakerlib.internal;

import java.util.Arrays;
import java.util.List;

import xyz.snaker.snakerlib.utility.Keyboards;
import xyz.snaker.snakerlib.utility.Reflection;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.mojang.datafixers.util.Pair;

import org.lwjgl.glfw.GLFW;

/**
 * Created by SnakerBone on 17/10/2023
 **/
public class KeyPair
{
    /**
     * Super Keys
     **/
    public static final KeyPair SUPER = new KeyPair(GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER);

    /**
     * Shift Keys
     **/
    public static final KeyPair SHIFT = new KeyPair(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);

    /**
     * Control Keys
     **/
    public static final KeyPair CONTROL = new KeyPair(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL);

    /**
     * Alternate Keys
     **/
    public static final KeyPair ALTERNATE = new KeyPair(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT);

    /**
     * Internal pair holding the keys
     **/
    private final Pair<Integer, Integer> keyPair;

    /**
     * The handle of the current window
     **/
    private long handle;

    public KeyPair(int left, int right)
    {
        validateKeys(left, right);
        findOrSetWindowHandle();

        this.keyPair = new Pair<>(left, right);
    }

    /**
     * A KeyPair with left shift and a custom defined key
     *
     * @param key The key
     * @return A new shifted KeyPair
     **/
    public static KeyPair shifted(int key)
    {
        return new KeyPair(GLFW.GLFW_KEY_LEFT_SHIFT, key);
    }

    /**
     * Checks if a key is invalid. Valid keys should be one of the field values present in {@link GLFW}
     *
     * @param key The key to check
     * @return True if the key is invalid or does not exist
     **/
    private static boolean isInvalidKey(int key)
    {
        Integer[] rawValues = Reflection.getFieldsInClass(GLFW.class, o -> o instanceof Integer, Integer[]::new);
        List<Integer> values = Arrays.stream(rawValues).toList();

        return !values.contains(key);
    }

    /**
     * Checks if a key is invalid. Valid keys should be one of the field values present in {@link GLFW}
     *
     * @param keys The keys to check
     * @throws IllegalArgumentException If a key is invalid
     **/
    private static void validateKeys(int... keys)
    {
        for (int key : keys) {
            if (isInvalidKey(key)) {
                throw new IllegalArgumentException("Key '%s' not found. Key should be a value of one of the following fields present in org.lwjgl.glfw.GLFW or similar".formatted(key));
            }
        }
    }

    /**
     * Attempts to find and/or set the current window handle
     **/
    private void findOrSetWindowHandle()
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

    /**
     * Gets the 'left' variant of this KeyPair
     *
     * @return The 'left' key
     **/
    public int left()
    {
        return keyPair.getFirst();
    }

    /**
     * Gets the 'right' variant of this KeyPair
     *
     * @return The 'right' key
     **/
    public int right()
    {
        return keyPair.getSecond();
    }

    /**
     * Base method for checking if a key is being pressed
     *
     * @param key The key to check
     * @return True if the key is currently being pressed
     **/
    private boolean isDown(int key)
    {
        return Keyboards.isKeyDown(handle, key);
    }

    /**
     * Base method for checking if a key is not being pressed
     *
     * @param key The key to check
     * @return True if the key is not currently being pressed
     **/
    private boolean isUp(int key)
    {
        return Keyboards.isKeyUp(handle, key);
    }

    /**
     * Checks if the 'left' key is being pressed
     *
     * @return True if the key is currently being pressed
     **/
    public boolean leftDown()
    {
        return isDown(left());
    }

    /**
     * Checks if the 'right' key is being pressed
     *
     * @return True if the key is currently being pressed
     **/
    public boolean rightDown()
    {
        return isDown(right());
    }

    /**
     * Checks if the 'left' key is not being pressed
     *
     * @return True if the key is currently not being pressed
     **/
    public boolean leftUp()
    {
        return isUp(left());
    }

    /**
     * Checks if the 'right' key is not being pressed
     *
     * @return True if the key is currently not being pressed
     **/
    public boolean rightUp()
    {
        return isUp(right());
    }

    /**
     * Checks if both keys in this KeyPair are being pressed
     *
     * @return True if both keys in this KeyPair are currently being pressed
     **/
    public boolean allDown()
    {
        return leftDown() & rightDown();
    }

    /**
     * Checks if both keys in this KeyPair are not being pressed
     *
     * @return True if both keys in this KeyPair are currently not being pressed
     **/
    public boolean allUp()
    {
        return leftUp() & rightUp();
    }

    /**
     * Checks if any keys in this KeyPair are being pressed
     *
     * @return True if any keys in this KeyPair are currently being pressed
     **/
    public boolean anyDown()
    {
        return leftDown() | rightDown();
    }

    /**
     * Checks if any keys in this KeyPair are not being pressed
     *
     * @return True if any keys in this KeyPair are not currently being pressed
     **/
    public boolean anyUp()
    {
        return leftUp() | rightUp();
    }

    /**
     * Checks if any singular key in this KeyPair is being pressed
     *
     * @return True if a single key in this KeyPair is currently being pressed
     **/
    public boolean sequentialDown()
    {
        return leftDown() ^ rightDown();
    }

    /**
     * Checks if any singular key in this KeyPair is not being pressed
     *
     * @return True if a single key in this KeyPair is not currently being pressed
     **/
    public boolean sequentialUp()
    {
        return leftUp() ^ rightUp();
    }
}
