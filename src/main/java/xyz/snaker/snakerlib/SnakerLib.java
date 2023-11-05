package xyz.snaker.snakerlib;

import java.io.File;
import java.util.Arrays;

import xyz.snaker.snakerlib.command.*;
import xyz.snaker.snakerlib.concurrent.thread.GoodbyeWorld;
import xyz.snaker.snakerlib.config.SnakerConfig;
import xyz.snaker.snakerlib.internal.PrettyPrettyPrintStream;
import xyz.snaker.snakerlib.internal.glfw.KeyPair;
import xyz.snaker.snakerlib.utility.mutable.MutableString;
import xyz.snaker.snakerlib.utility.tools.KeyboardStuff;
import xyz.snaker.snakerlib.utility.tools.TimeStuff;
import xyz.snaker.snakerlib.utility.tools.UnsafeStuff;
import xyz.snaker.snkr4j.LogColour;
import xyz.snaker.snkr4j.SimpleLogger;
import xyz.snaker.snkr4j.SnakerLogger;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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

import org.joml.Vector2i;
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
     * A locked value holding the modid of the mod that initialized SnakerLib
     *
     * @see SnakerLib#initialize()
     **/
    public static final MutableString MOD = new MutableString();

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

    /**
     * A colourful print stream
     **/
    public static final PrettyPrettyPrintStream pretty = new PrettyPrettyPrintStream(LogColour.Style.BOLD, LogColour.Style.ITALIC);

    /**
     * The goodbye world task pending status
     **/
    private static boolean pending;

    /**
     * The goodbye world task time counter
     **/
    private static final Vector2i time = new Vector2i(10000);

    public SnakerLib()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SnakerConfig.COMMON_SPEC, "snakerlib-common.toml");
        SnakerLib.debugKey = GLFW.GLFW_KEY_KP_ENTER;
        SnakerLib.MOD.set(SnakerLib.MODID);

        if (TimeStuff.isHoliday()) {
            System.setOut(pretty);
        }
    }

    /**
     * Call this to initialize your mod to SnakerLib.
     * <p>
     * <strong>The class you're calling it from must be annotated with {@link Mod} and cannot be a static or nested class</strong>
     **/
    public static void initialize()
    {
        Class<?> clazz = STACK_WALKER.getCallerClass();

        if (!isInitialized) {
            if (!clazz.isAnnotationPresent(Mod.class)) {
                SnakerLib.LOGGER.errorf("Could not initialize mod to SnakerLib: No [] annotation present in caller class []", Mod.class.getName(), clazz.getName());
                isInitialized = true;
                return;
            }

            String modId = clazz.getAnnotation(Mod.class).value();

            if (ResourceLocation.isValidNamespace(modId)) {
                if (MOD.set(modId)) {
                    String name = MOD.get();
                    SnakerLib.LOGGER.infof("Successfully initialized mod to SnakerLib: []", name);
                    isInitialized = true;
                    return;
                }
            } else {
                SnakerLib.LOGGER.errorf("Could not initialize mod to SnakerLib: Invalid mod id []", modId);
                isInitialized = true;
                return;
            }
        }

        throw new RuntimeException("SnakerLib already initialized");
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
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        boolean windows = Util.getPlatform() == Util.OS.WINDOWS;

        if (event.phase == TickEvent.Phase.END || !minecraft.isPaused()) {
            if (SnakerConfig.COMMON.forceCrashJvmKeyBindings.get()) {
                if (KeyPair.ALTERNATE.sequentialDown() && KeyPair.SHIFT.sequentialDown() && KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_F4)) {
                    UnsafeStuff.forceCrashJVM();
                }
            }

            if (level != null && player != null) {
                if (SnakerConfig.COMMON.forceCrashOperatingSystemBindings.get()) {
                    KeyPair pair = new KeyPair(GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_F12);

                    Component message = Component.empty().append(Component.translatable("debug.prefix").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)).append(CommonComponents.SPACE).append(Component.translatable(windows ? "snakerlib.os_crash.message" : "snakerlib.os_shutdown.message"));
                    Component warning = Component.empty().append(Component.translatable("debug.prefix").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD)).append(CommonComponents.SPACE).append(Component.translatable(windows ? "snakerlib.os_crash.warning" : "snakerlib.os_shutdown.warning", time.y / 1000));

                    if (!pending) {
                        if (pair.allDown()) {
                            player.sendSystemMessage(message);
                            pending = true;
                        } else {
                            time.y = 10000;
                        }
                    }

                    if (pending && pair.allDown()) {
                        if (clientTickCount % 40 == 0) {
                            time.y -= 1000;
                            player.sendSystemMessage(warning);
                            SnakerLib.LOGGER.warnf("Debug crash for player %s initiating in: []".formatted(player.getDisplayName().getString()), time.y / 1000);
                            if (time.y <= 0) {
                                new GoodbyeWorld(time).start();
                                time.y = 10000;
                                pending = false;
                            }
                        }
                    }

                    if (pair.anyUp()) {
                        time.y = 10000;
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

    public static long getClientTickCount()
    {
        return clientTickCount;
    }

    public static long getServerTickCount()
    {
        return serverTickCount;
    }
}