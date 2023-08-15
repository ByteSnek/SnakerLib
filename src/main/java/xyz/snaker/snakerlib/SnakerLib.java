package xyz.snaker.snakerlib;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import xyz.snaker.snakerlib.config.SnakerConfig;
import xyz.snaker.snakerlib.internal.LevelSavingEvent;
import xyz.snaker.snakerlib.internal.Single;
import xyz.snaker.snakerlib.internal.log4j.SnakerLogger;
import xyz.snaker.snakerlib.internal.log4j.SnakerLoggerManager;
import xyz.snaker.snakerlib.level.entity.SnakerBoss;
import xyz.snaker.snakerlib.utility.tools.KeyboardStuff;
import xyz.snaker.snakerlib.utility.tools.StringStuff;
import xyz.snaker.snakerlib.utility.tools.UnsafeStuff;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import org.lwjgl.glfw.GLFW;

/**
 * Created by SnakerBone on 5/05/2023
 **/
@Mod(SnakerLib.MODID)
public class SnakerLib
{
    public static final String MODID = "snakerlib";
    public static final String NAME = SnakerLib.class.getSimpleName();

    private static long clientTickCount = 0;
    private static long serverTickCount = 0;

    private static boolean isInitialized = false;
    private static boolean isRegistered = false;

    public static final Single<String> MOD = new Single<>();

    public static final SnakerLogger LOGGER = SnakerLoggerManager.INSTANCE.apply(SnakerLib.NAME);
    public static StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public SnakerLib()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modSetupEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SnakerConfig.COMMON_SPEC, "snakerlib-common.toml");
    }

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
            if (StringStuff.isValidString(modId)) {
                MOD.set(modId);
                String name = MOD.get();
                SnakerLib.LOGGER.infof("Successfully initialized mod '%s' to SnakerLib", name);
                isInitialized = true;
            } else {
                throw new RuntimeException(String.format("Could not initialize mod '%s' to SnakerLib: modid is invalid", modId));
            }
            if (isRegistered) {
                throw new RuntimeException("SnakerLib has already been registered");
            } else {
                MinecraftForge.EVENT_BUS.register(new SnakerLib());
                isRegistered = true;
            }
        }
    }

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
                            SnakerLib.LOGGER.infof("Successfully deleted JVM crash file '%s'", fileName);
                        } else {
                            SnakerLib.LOGGER.infof("Could not delete JVM crash file '%s'", fileName);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    protected void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END || !Minecraft.getInstance().isPaused()) {
            if (KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                if (KeyboardStuff.isKeyDown(GLFW.GLFW_KEY_F4)) {
                    if (SnakerConfig.COMMON.forceCrashJvmKeyBindings.get()) {
                        UnsafeStuff.forceCrashJVM(Component.literal("Left shift and F4 pressed.").getString() + " " + Component.literal("You can disable this in the config (snakerlib-common.toml) if you wish").getString());
                    }
                }
            }
            clientTickCount++;
        }
    }

    @SubscribeEvent
    protected void serverTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END) {
            serverTickCount++;
        }
    }

    public void modSetupEvent(FMLCommonSetupEvent event)
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
                        SnakerLib.LOGGER.infof("Successfully deleted crash file '%s'", fileName);
                    } else {
                        SnakerLib.LOGGER.infof("Could not delete crash file '%s'", fileName);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    protected void serverStopped(LevelSavingEvent event) throws InterruptedException
    {
        AtomicBoolean hasDiscarded = new AtomicBoolean(true);
        var bosses = SnakerBoss.BOSS_INSTANCES;
        if (!bosses.isEmpty()) {
            for (SnakerBoss boss : new CopyOnWriteArrayList<>(bosses)) {
                int bossesSize = bosses.size();
                boss.discard();
                if (hasDiscarded.get()) {
                    SnakerLib.LOGGER.infof("Successfully discarded %s bosses", bossesSize);
                    hasDiscarded.compareAndSet(true, false);
                }
            }
            Thread.sleep(500);
            bosses.clear();
            hasDiscarded.compareAndSet(false, true);
        }
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