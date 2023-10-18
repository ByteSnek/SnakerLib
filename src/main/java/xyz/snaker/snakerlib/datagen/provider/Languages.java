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
        add("commands.snakerlib.kill_entity_success_0", "Successfully killed %s entities. Some entities could not be killed as they were invulnerable");
        add("commands.snakerlib.kill_entity_success", "Successfully killed %s entities");
        add("commands.snakerlib.kill_entity_failure", "Could not find any entities to kill");

        add("commands.snakerlib.hurt_entity_success_0", "Successfully inflicted %s entities with %s damage. Some entities could not be hurt as they were invulnerable");
        add("commands.snakerlib.hurt_entity_success", "Successfully inflicted %s entities with %s damage");
        add("commands.snakerlib.hurt_entity_failure", "Could not find any entities to hurt");

        add("commands.snakerlib.discard_entity_success", "Successfully discarded %s entities");
        add("commands.snakerlib.discard_entity_failure", "Could not find any entities to discard");

        add("commands.snakerlib.playground_mode_status", "%s playground mode");

        add("commands.snakerlib.config_set_success", "Config set");
        add("commands.snakerlib.config_set_failure", "Could not set config");
    }
}
