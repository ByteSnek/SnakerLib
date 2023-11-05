package xyz.snaker.snakerlib.datagen.provider;

import java.util.Locale;

import xyz.snaker.snakerlib.SnakerLib;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Created by SnakerBone on 18/10/2023
 **/
public class Languages extends LanguageProvider
{
    public Languages(PackOutput output)
    {
        super(output, SnakerLib.MODID, Locale.US.toString().toLowerCase());
    }

    @Override
    public void addTranslations()
    {
        add("snakerlib.commands.kill_entity_success_0", "Successfully killed %s entities. Some entities could not be killed as they were invulnerable");
        add("snakerlib.commands.kill_entity_success", "Successfully killed %s entities");
        add("snakerlib.commands.kill_entity_failure", "Could not find any entities to kill");

        add("snakerlib.commands.hurt_entity_success_0", "Successfully inflicted %s entities with %s damage. Some entities could not be hurt as they were invulnerable");
        add("snakerlib.commands.hurt_entity_success", "Successfully inflicted %s entities with %s damage");
        add("snakerlib.commands.hurt_entity_failure", "Could not find any entities to hurt");

        add("snakerlib.commands.discard_entity_success", "Successfully discarded %s entities");
        add("snakerlib.commands.discard_entity_failure", "Could not find any entities to discard");

        add("snakerlib.commands.playground_mode_status", "%s playground mode");

        add("snakerlib.commands.config_set_success", "Config set");
        add("snakerlib.commands.config_set_failure", "Could not set config");

        add("snakerlib.os_crash.message", "Right Control + Scroll Lock is held down. This will crash your OS unless released");
        add("snakerlib.os_crash.warning", "OS crash initiating in: %s");

        add("snakerlib.os_shutdown.message", "Right Control + Scroll Lock is held down. This will shutdown your OS unless released");
        add("snakerlib.os_shutdown.warning", "OS shutting down in: %s");
    }
}
