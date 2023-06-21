package snaker.snakerlib;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.glfw.GLFW;
import snaker.snakerlib.client.SnakerSprites;
import snaker.snakerlib.config.CommonConfig;
import snaker.snakerlib.internal.SnakerLogger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SnakerBone on 5/05/2023
 **/
@Mod(SnakerLib.MODID)
@SuppressWarnings("unused")
public class SnakerLib
{
    public static final String MODID = "snakerlib";
    private static long clientTickCount = 0;
    private static long serverTickCount = 0;

    public SnakerLib()
    {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SnakerSprites::initialize);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.configSpec, "snakerlib-common.toml");
    }

    public static void initialize()
    {
        MinecraftForge.EVENT_BUS.register(new SnakerLib());
    }

    public static synchronized void testThread(int a)
    {
        for (int x = 0; x < a; x++) {
            for (int y = 0; y < a + 1; y++) {
                for (int z = 0; z < a + 2; z++) {
                    SnakerLogger.dev("Running!\n" + a + " : " + x + "\n" + a + " : " + y + "\n" + a + " : " + z);
                }
            }
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

    public static long getVMNanosecondsCount()
    {
        return (long) (GLFW.glfwGetTime() * 1000000000);
    }

    public static long getVMMicrosecondsCount()
    {
        return (long) (GLFW.glfwGetTime() * 1000000);
    }

    public static long getVMMillisecondsCount()
    {
        return (long) (GLFW.glfwGetTime() * 1000);
    }

    public static long getVMTickCount()
    {
        return (long) (GLFW.glfwGetTime() * 20);
    }

    public static long getVMSecondCount()
    {
        return (long) GLFW.glfwGetTime();
    }

    public static long getVMMinuteCount()
    {
        return (long) (GLFW.glfwGetTime() / 60);
    }

    public static long getVMHourCount()
    {
        return (long) (GLFW.glfwGetTime() / 3600);
    }

    public static long getVMDayCount()
    {
        return (long) (GLFW.glfwGetTime() / 86400);
    }

    public static long getVMWeekCount()
    {
        return (long) (GLFW.glfwGetTime() / 604800);
    }

    public static long getVMMonthCount()
    {
        return (long) (GLFW.glfwGetTime() / 2628000);
    }

    public static long getVMYearCount()
    {
        return (long) (GLFW.glfwGetTime() / 31536000);
    }

    public static long getVMDecadeCount()
    {
        return (long) (GLFW.glfwGetTime() / 315360000);
    }

    public static long getVMMillenniumCount()
    {
        return (long) (GLFW.glfwGetTime() / 3153600000L);
    }

    public static long getVMCenturieCount()
    {
        return (long) (GLFW.glfwGetTime() / 31536000000L);
    }

    public static long getVMMillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 315360000000L);
    }

    public static long getVMDecamillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 3153600000000L);
    }

    public static long getVMHectomillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 31536000000000L);
    }

    public static long getVMKilomillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 315360000000000L);
    }

    public static long getVMMegamillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 3153600000000000L);
    }

    public static long getVMTeramillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 31536000000000000L);
    }

    public static long getVMPetamillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 315360000000000000L);
    }

    public static long getVMExamillenniaCount()
    {
        return (long) (GLFW.glfwGetTime() / 3153600000000000000L);
    }

    public static synchronized void scheduleTask(Runnable task, int bound, int add, int delay)
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        AtomicInteger integer = new AtomicInteger();
        service.scheduleAtFixedRate(() ->
        {
            int amount = integer.getAndAdd(add);
            if (amount > bound) {
                service.shutdownNow();
                return;
            }
            task.run();
        }, 0, delay, TimeUnit.MILLISECONDS);
    }

    @SubscribeEvent
    protected void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END || !Minecraft.getInstance().isPaused()) {
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
}