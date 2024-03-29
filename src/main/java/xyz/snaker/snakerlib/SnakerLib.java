package xyz.snaker.snakerlib;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import xyz.snaker.hiss.keyboard.KeyPair;
import xyz.snaker.hiss.keyboard.Keyboard;
import xyz.snaker.hiss.logger.Logger;
import xyz.snaker.hiss.logger.Loggers;
import xyz.snaker.hiss.sneaky.Sneaky;
import xyz.snaker.hiss.utility.MutableString;
import xyz.snaker.snakerlib.chat.ChatComponents;
import xyz.snaker.snakerlib.command.*;
import xyz.snaker.snakerlib.config.SnakerConfig;
import xyz.snaker.snakerlib.internal.GoodbyeWorldThread;
import xyz.snaker.snakerlib.resources.ResourceReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TickEvent;

import com.mojang.blaze3d.platform.Window;
import com.mojang.brigadier.CommandDispatcher;

import org.apache.commons.lang3.RandomStringUtils;
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
     * Stack walker with class reference retention
     **/
    public static StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * SnakerLib's logger instance
     **/
    public static final Logger LOGGER = Loggers.getLogger(SnakerLib.STACK_WALKER.getCallerClass());

    /**
     * SnakerLib's dev logger instance
     **/
    public static final Logger DEVLOGGER = Loggers.newSimpleLogger(SnakerLib.STACK_WALKER.getCallerClass(), !FMLEnvironment.production);

    /**
     * The goodbye world task pending status
     **/
    private static boolean pending;

    /**
     * The goodbye world task time counter
     **/
    private static final Vector2i time = new Vector2i(10000);

    /**
     * A resource location for a custom skin
     **/
    private static ResourceReference customSkin;

    public SnakerLib()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SnakerConfig.COMMON_SPEC, "snakerlib-common.toml");
        SnakerLib.MOD.set(SnakerLib.MODID);
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
        Window window = minecraft.getWindow();
        long handle = window.getWindow();

        if (event.phase == TickEvent.Phase.END || !minecraft.isPaused()) {
            if (SnakerConfig.COMMON.forceCrashJvmKeyBindings.get()) {
                if (KeyPair.ALTERNATE.apply(handle).sequentialDown() && KeyPair.SHIFT.apply(handle).sequentialDown() && Keyboard.isKeyDown(handle, GLFW.GLFW_KEY_F4)) {
                    Sneaky.forceCrashJVM();
                }
            }

            if (level != null && player != null) {
                if (SnakerConfig.COMMON.goodbyeWorldKeyBindings.get()) {
                    KeyPair pair = new KeyPair(handle, GLFW.GLFW_KEY_RIGHT_CONTROL, GLFW.GLFW_KEY_F12);

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
        DebugRandomTeleportCommand.register(dispatcher);
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
        return ChatComponents.debug("goodbye_world.snakerlib." + (warning ? "warning" : "message"), args);
    }

    public static long getClientTickCount()
    {
        return clientTickCount;
    }

    public static long getServerTickCount()
    {
        return serverTickCount;
    }

    public static ResourceReference getCustomSkin()
    {
        return customSkin;
    }

    public static void setCustomSkin(ResourceReference customSkin)
    {
        SnakerLib.customSkin = customSkin;
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