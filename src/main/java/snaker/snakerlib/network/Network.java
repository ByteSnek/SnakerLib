package snaker.snakerlib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import snaker.snakerlib.SnakerLib;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SnakerBone on 5/05/2023
 **/
public class Network
{
    private static final HashMap<Integer, String> defaultDependants = SnakerLib.DEFAULT_DEPENDANTS;
    private static final HashMap<Integer, String> externalDependants = SnakerLib.EXTERNAL_DEPENDANTS;

    private static int size = externalDependants.size();

    private static long clientTickCount = 0;
    private static long serverTickCount = 0;

    public static BlockPos explosionPos = null;
    public static double explosionAnimation = 0;
    public static int explosionTime = 0;
    public static boolean explosionRetreating = false;

    protected static void onAddedExternalDependants(FMLCommonSetupEvent event)
    {
        putDefaultDependants();

        for (int i = 0; i < size; i++) {
            SnakerLib.LOGGER.info("Added " + externalDependants.get(i) + " to external dependants");
        }
    }

    private static void putDefaultDependants()
    {
        defaultDependants.putIfAbsent(0, "azcray");
        defaultDependants.putIfAbsent(1, "snakerbone");
        defaultDependants.putIfAbsent(2, "forge");
    }

    public static void addExternalDependant(String modId)
    {
        if (ResourceLocation.isValidNamespace(modId)) {
            externalDependants.putIfAbsent(size, modId);
            size++;

        } else {
            SnakerLib.LOGGER.error("Dependant: [ " + modId + " ] could not not be added because it is not a valid namespace");
        }
    }

    public static synchronized void testThread(int a)
    {
        for (int x = 0; x < a; x++) {
            for (int y = 0; y < a + 1; y++) {
                for (int z = 0; z < a + 2; z++) {
                    SnakerLib.LOGGER.info("Running!\n" + a + " : " + x + "\n" + a + " : " + y + "\n" + a + " : " + z);
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

    public static void initialize()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new Network());
        bus.addListener(Network::onAddedExternalDependants);
    }
}