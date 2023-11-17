package xyz.snaker.snakerlib.datagen;

import java.util.Locale;

import xyz.snaker.snakerlib.SnakerLib;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.jetbrains.annotations.NotNull;

import bytesnek.hiss.utility.Translations;

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

    static class Languages extends LanguageProvider
    {
        public Languages(PackOutput output)
        {
            super(output, SnakerLib.MODID, Locale.US.toString().toLowerCase());
        }

        @Override
        public void addTranslations()
        {
            add("command.force_removal_success", "Successfully force removed you from the world");

            add("command.kill_entity_success_0", "Successfully killed %s entities. Some entities could not be killed as they were invulnerable");
            add("command.kill_entity_success", "Successfully killed %s entities");
            add("command.kill_entity_failure", "Could not find any entities to kill");

            add("command.hurt_entity_success_0", "Successfully inflicted %s entities with %s damage. Some entities could not be hurt as they were invulnerable");
            add("command.hurt_entity_success", "Successfully inflicted %s entities with %s damage");
            add("command.hurt_entity_failure", "Could not find any entities to hurt");

            add("command.discard_entity_success", "Successfully discarded %s entities");
            add("command.discard_entity_failure", "Could not find any entities to discard");

            add("command.playground_mode_status", "%s playground mode");

            add("command.config_set_success", "Config set");
            add("command.config_set_failure", "Could not set config");

            add("goodbye_world.message", "Right Control + Scroll Lock is held down. If you're not familiar with this key combination or you're not a developer that knows what their doing then you should probably release these keys right now");
            add("goodbye_world.warning", "Saying goodbye to the current context in: %s");

            add("chat.info", "Info");
            add("chat.debug", "Debug");
            add("chat.hello", "Hello%s");
            add("chat.goodbye", "Goodbye%s");
        }

        @Override
        public void add(@NotNull String key, @NotNull String value)
        {
            super.add(Translations.joinKey(key, SnakerLib.MODID, "itemGroup"), value);
        }
    }
}