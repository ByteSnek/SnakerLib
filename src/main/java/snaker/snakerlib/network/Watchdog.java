package snaker.snakerlib.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import snaker.snakerlib.SnakerLib;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SnakerBone on 5/05/2023
 **/
@SuppressWarnings("unused")
public class Watchdog
{
    private static long tickCount = 0;

    public static synchronized void testThread(int a)
    {
        for (int x = 0; x < a; x++)
        {
            for (int y = 0; y < a + 1; y++)
            {
                for (int z = 0; z < a + 2; z++)
                {
                    SnakerLib.LOGGER.info("Running!\n" + a + " : " + x + "\n" + a + " : " + y + "\n" + a + " : " + z);
                }
            }
        }
    }

    public static long getTickCount()
    {
        return tickCount;
    }

    public static synchronized void scheduleTask(Runnable task, int bound, int add, int delay)
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        AtomicInteger integer = new AtomicInteger();
        service.scheduleAtFixedRate(() ->
        {
            int amount = integer.getAndAdd(add);
            if (amount > bound)
            {
                service.shutdownNow();
                return;
            }
            task.run();
        }, 0, delay, TimeUnit.MILLISECONDS);
    }
    
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        if (Minecraft.getInstance().isPaused()) return;
        tickCount++;
    }

    public static void initialize()
    {
        MinecraftForge.EVENT_BUS.register(new Watchdog());
    }
}