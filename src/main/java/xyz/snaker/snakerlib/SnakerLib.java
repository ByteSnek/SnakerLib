package xyz.snaker.snakerlib;

import java.io.File;
import java.util.Arrays;

import xyz.snaker.snakerlib.command.*;
import xyz.snaker.snakerlib.concurrent.lock.LockedValue;
import xyz.snaker.snakerlib.config.SnakerConfig;
import xyz.snaker.snakerlib.utility.tools.KeyboardStuff;
import xyz.snaker.snakerlib.utility.tools.UnsafeStuff;
import xyz.snaker.snkr4j.SimpleLogger;
import xyz.snaker.snkr4j.SnakerLogger;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import com.mojang.brigadier.CommandDispatcher;

import org.lwjgl.glfw.GLFW;

/**
 * Created by SnakerBone on 5/05/2023
 **/
@Mod(SnakerLib.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SnakerLib
{
    public static final String NAME = "SnakerLib";
    public static final String MODID = "snakerlib";

    /**
     * A client side tick counter
     *
     * @see #getClientTickCount()
     **/
    private static long clientTickCount = 0;

    /**
     * A server side tick counter
     *
     * @see #getServerTickCount()
     **/
    private static long serverTickCount = 0;

    /**
     * A printable debug key for debugging. By default this key is set to keypad enter
     *
     * @see KeyboardStuff#isDebugKeyDown()
     **/
    private static int debugKey;

    /**
     * Checks if SnakerLib is initialized
     **/
    private static boolean isInitialized;

    /**
     * Checks if SnakerLib is registered to Forge
     **/
    private static boolean isRegistered;

    /**
     * Checks if SnakerLib is running in developer environment
     **/
    private static boolean inDeveloperEnvironment;

    /**
     * A locked value holding the modid of the mod that initialized SnakerLib
     *
     * @see SnakerLib#initialize()
     **/
    public static final LockedValue<String> MOD = new LockedValue<>();

    /**
     * SnakerLib's logger instance
     **/
    public static final SnakerLogger LOGGER = new SimpleLogger(SnakerLib.class, true);

    /**
     * SnakerLib's dev logger instance
     **/
    public static final SnakerLogger DEVLOGGER = new SimpleLogger(SnakerLib.class, !FMLEnvironment.production);

    /**
     * Stack walker with class reference retention
     **/
    public static StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * SnakerLib's native functions
     **/
    public static final SnakerLibNatives NATIVES = new SnakerLibNatives();

    public SnakerLib()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SnakerConfig.COMMON_SPEC, "snakerlib-common.toml");
        SnakerLib.debugKey = GLFW.GLFW_KEY_KP_ENTER;
    }

    /**
     * Call this to initialize your mod to SnakerLib.
     * <p>
     * <strong>The class you're calling it from must be annotated with {@link Mod} and cannot be a static or nested class</strong>
     **/
    public static void initialize()
    {
        Class<?> clazz = STACK_WALKER.getCallerClass();
        if (isInitialized) {
            throw new RuntimeException("SnakerLib has already been initialized");
        } else {
            if (!clazz.isAnnotationPresent(Mod.class)) {
                throw new RuntimeException(String.format("Could not initialize mod to SnakerLib: Class '%s' is not annotated with @Mod", clazz.getSimpleName()));
            }
            String modId = clazz.getAnnotation(Mod.class).value();
            if (ResourceLocation.isValidNamespace(modId)) {
                if (MOD.set(modId)) {
                    String name = MOD.get();
                    SnakerLib.LOGGER.infof("Successfully initialized mod to SnakerLib: []", name);
                    isInitialized = true;
                }
            } else {
                throw new RuntimeException(String.format("Could not initialize mod '%s' to SnakerLib: modid is invalid", modId));
            }
            if (isRegistered) {
                throw new RuntimeException("SnakerLib has already been registered");
            } else {
                isRegistered = true;
            }
        }
    }

    /**
     * Deletes any JVM hotspot crash files that were caused by {@link UnsafeStuff#forceCrashJVM()}
     **/
    public static void deleteJVMHSFiles()
    {
        if (FMLPaths.GAMEDIR.get() != null) {
            File file = new File(FMLPaths.GAMEDIR.get().toUri());
            File[] files = file.listFiles();
            if (files != null) {
                for (File jvmFile : files) {
                    if (jvmFile.getName().startsWith("hs")) {
                        String fileName = jvmFile.getName();
                        if (jvmFile.delete()) {
                            SnakerLib.LOGGER.infof("Successfully deleted JVM crash file: []'", fileName);
                        } else {
                            SnakerLib.LOGGER.infof("Could not delete JVM crash file: []", fileName);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END || !Minecraft.getInstance().isPaused()) {
            if (KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_RIGHT_ALT)) {
                if (KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
                    if (KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_F4)) {
                        if (SnakerConfig.COMMON.forceCrashJvmKeyBindings.get()) {
                            UnsafeStuff.forceCrashJVM();
                        }
                    }
                }
            }
            clientTickCount++;
        }
    }

    @SubscribeEvent
    static void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END) {
            serverTickCount++;
        }
    }

    @SubscribeEvent
    static void onCommandRego(RegisterCommandsEvent event)
    {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        DiscardAllEntitiesCommand.register(dispatcher);
        HurtAllEntitiesCommand.register(dispatcher);
        KillAllEntitiesCommand.register(dispatcher);
        PlaygroundModeCommand.register(dispatcher);
        ConfigCommand.register(dispatcher);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        File runDir = new File(FMLPaths.GAMEDIR.get().toUri());
        File crashDir = new File(FMLPaths.GAMEDIR.get().resolve("crash-reports").toUri());
        File[] runFiles = runDir.listFiles();
        File[] crashFiles = crashDir.listFiles();
        if (SnakerConfig.COMMON.removeJvmCrashFilesOnStartup.get()) {
            if (runFiles != null) {
                if (Arrays.stream(runFiles).anyMatch(f -> f.getName().startsWith("hs"))) {
                    deleteJVMHSFiles();
                }
            }
        }
        if (SnakerConfig.COMMON.removeMinecraftCrashFilesOnStartup.get()) {
            if (crashFiles != null) {
                for (File crashFile : crashFiles) {
                    String fileName = crashFile.getName();
                    if (crashFile.delete()) {
                        SnakerLib.LOGGER.infof("Successfully deleted crash file: []", fileName);
                    } else {
                        SnakerLib.LOGGER.infof("Could not delete crash file: []", fileName);
                    }
                }
            }
        }
    }

    public static void setDebugKey(int key)
    {
        debugKey = key;
    }

    public static int getDebugKey()
    {
        return debugKey;
    }

    public static boolean isInDeveloperEnvironment()
    {
        return inDeveloperEnvironment;
    }

    public static long getClientTickCount()
    {
        return clientTickCount;
    }

    public static long getServerTickCount()
    {
        return serverTickCount;
    }
}