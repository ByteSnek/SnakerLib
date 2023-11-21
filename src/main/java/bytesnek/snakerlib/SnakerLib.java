package bytesnek.snakerlib;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
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

import org.apache.commons.lang3.RandomStringUtils;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import bytesnek.hiss.keyboard.KeyPair;
import bytesnek.hiss.keyboard.Keyboard;
import bytesnek.hiss.logger.LogColour;
import bytesnek.hiss.logger.Logger;
import bytesnek.hiss.logger.SimpleLogger;
import bytesnek.hiss.printstream.ColourfulPrintStream;
import bytesnek.hiss.sneaky.Sneaky;
import bytesnek.hiss.utility.DatesAndTimes;
import bytesnek.hiss.utility.MutableString;
import bytesnek.snakerlib.chat.ChatComponents;
import bytesnek.snakerlib.command.*;
import bytesnek.snakerlib.config.SnakerConfig;
import bytesnek.snakerlib.internal.GoodbyeWorldThread;

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
     * Checks if SnakerLib is initialized
     **/
    private static boolean isInitialized;

    /**
     * A locked value holding the modid of the mod that initialized SnakerLib. Defaults to SnakerLib's mod id
     *
     * @see SnakerLib#initialize()
     **/
    public static final MutableString MOD = new MutableString();

    /**
     * SnakerLib's logger instance
     **/
    public static final Logger LOGGER = new SimpleLogger(SnakerLib.class, true);

    /**
     * SnakerLib's dev logger instance
     **/
    public static final Logger DEVLOGGER = new SimpleLogger(SnakerLib.class, !FMLEnvironment.production);

    /**
     * Stack walker with class reference retention
     **/
    public static StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * A colourful print stream
     **/
    public static ColourfulPrintStream out = new ColourfulPrintStream(LogColour.Style.BOLD, LogColour.Style.ITALIC);

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
        SnakerLib.MOD.set(SnakerLib.MODID);

        if (DatesAndTimes.isHoliday()) {
            System.setOut(out);
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
     * Deletes any JVM hotspot crash files that were caused by {@link Sneaky#forceCrashJVM()}
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

        if (event.phase == TickEvent.Phase.END || !minecraft.isPaused()) {
            if (SnakerConfig.COMMON.forceCrashJvmKeyBindings.get()) {
                if (KeyPair.ALTERNATE.sequentialDown() && KeyPair.SHIFT.sequentialDown() && Keyboard.isKeyDown(GLFW.GLFW_KEY_F4)) {
                    Sneaky.forceCrashJVM();
                }
            }

            if (level != null && player != null) {
                if (SnakerConfig.COMMON.goodbyeWorldKeyBindings.get()) {
                    KeyPair pair = new KeyPair(GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_F12);

                    MutableComponent message = goodbyeWorldDebug(false);
                    MutableComponent warning = goodbyeWorldDebug(true, time.y / 1000);

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

                            if (time.y >= 0) {
                                player.sendSystemMessage(warning);
                            } else {
                                new GoodbyeWorldThread(time, minecraft).start();
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
        ForceRemoveCommand.register(dispatcher);
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

    private static MutableComponent goodbyeWorldDebug(boolean warning, Object... args)
    {
        return ChatComponents.debug("snakerlib.goodbye_world." + (warning ? "warning" : "message"), args);
    }

    public static long getClientTickCount()
    {
        return clientTickCount;
    }

    public static long getServerTickCount()
    {
        return serverTickCount;
    }

    /**
     * Generates a random alpha numeric string
     *
     * @param locale The language to use
     * @param limit  The maximum amount of characters to use in the string
     * @param modid  Should the string include a modid
     * @return A random alphanumeric string
     **/
    public static String placeholder(Locale locale, int limit, boolean modid)
    {
        return modid ? SnakerLib.MOD.get() + ":" + RandomStringUtils.randomAlphanumeric(limit).toLowerCase(locale) : RandomStringUtils.randomAlphanumeric(limit).toLowerCase(locale);
    }

    /**
     * Generates a random alpha numeric string without a modid
     *
     * @param locale The language to use
     * @param limit  The maximum amount of characters to use in the string
     * @return The random alphanumeric string
     **/
    public static String placeholder(Locale locale, int limit)
    {
        return placeholder(locale, limit, false);
    }

    /**
     * Generates a random alpha numeric string with a limit of 8
     *
     * @param locale The language to use
     * @param modid  Should the string include a modid
     * @return A random alphanumeric string
     **/
    public static String placeholder(Locale locale, boolean modid)
    {
        return placeholder(locale, 8, modid);
    }

    /**
     * Generates a random alpha numeric string with a limit of 8 and without a modid
     *
     * @param locale The language to use
     * @return A random alphanumeric string
     **/
    public static String placeholder(Locale locale)
    {
        return placeholder(locale, false);
    }

    /**
     * Generates a random alpha numeric string with a limit of 8, without a modid and a root language
     *
     * @return A random alphanumeric string
     **/
    public static String placeholder()
    {
        return placeholder(Locale.ROOT);
    }

    /**
     * Generates a random alpha numeric string with a limit of 8, with a modid and a root language
     *
     * @return A random alphanumeric string
     **/
    public static String placeholderWithId()
    {
        return placeholder(Locale.ROOT, true);
    }
}