package xyz.snaker.snakerlib.datagen;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.datagen.provider.Languages;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by SnakerBone on 18/10/2023
 **/
@Mod.EventBusSubscriber(modid = SnakerLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(true, new Languages(output));
    }
}