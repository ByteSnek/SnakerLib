package xyz.snaker.snakerlib.utility.tools;

import xyz.snaker.snkr4j.SnakerLogger;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 10/13/2023
 **/
public class Log4jStuff
{
    public static void cleanLootTableError(SnakerLogger logger, String message, Object p0, Object p1)
    {
        cleanError(logger, "Error parsing loot table %s.json : %s", message, p0, p1);
    }

    public static void cleanRecipeError(SnakerLogger logger, String message, Object p0, Object p1)
    {
        cleanError(logger, "Error parsing recipe %s.json : %s", message, p0, p1);
    }

    private static void cleanError(SnakerLogger logger, String message, String fallbackMessage, Object possibleId, Object possibleError)
    {
        if (possibleId instanceof ResourceLocation id && possibleError instanceof Exception e) {
            logger.errorf(message, id, e.getMessage());
            return;
        }
        logger.errorf(fallbackMessage, possibleId, possibleError);
    }
}
