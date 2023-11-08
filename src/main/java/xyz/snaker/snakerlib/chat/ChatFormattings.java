package xyz.snaker.snakerlib.chat;

import java.util.Arrays;
import java.util.function.Predicate;

import xyz.snaker.snakerlib.SnakerLib;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Created by SnakerBone on 8/11/2023
 **/
public class ChatFormattings
{
    /**
     * All the Minecraft chat format values
     **/
    public static final ChatFormatting[] ALL_FORMATTINGS = ChatFormatting.values();

    /**
     * All the Minecraft chat colours
     **/
    public static final ChatFormatting[] ALL_COLOURS = filterEnumValues(ChatFormatting.values(), ChatFormatting::isColor);

    /**
     * All the Minecraft chat formats
     **/
    public static final ChatFormatting[] ALL_FORMATS = filterEnumValues(ChatFormatting.values(), ChatFormatting::isFormat);

    /**
     * All the Minecraft chat colour shades
     **/
    public static final ChatFormatting[] ALL_SHADES = {ChatFormatting.BLACK, ChatFormatting.DARK_GRAY, ChatFormatting.GRAY, ChatFormatting.WHITE};

    /**
     * All the Minecraft chat prime colours
     **/
    public static final ChatFormatting[] PRIME_COLOURS = {ChatFormatting.RED, ChatFormatting.GREEN, ChatFormatting.BLUE, ChatFormatting.YELLOW};

    /**
     * All the Minecraft chat rainbow colours
     **/
    public static final ChatFormatting[] RAINBOW_COLOURS = {ChatFormatting.RED, ChatFormatting.GREEN, ChatFormatting.BLUE, ChatFormatting.YELLOW, ChatFormatting.LIGHT_PURPLE, ChatFormatting.AQUA};

    /**
     * All the Minecraft chat dark colours
     **/
    public static final ChatFormatting[] DARK_COLOURS = {ChatFormatting.DARK_RED, ChatFormatting.DARK_GREEN, ChatFormatting.DARK_BLUE, ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_AQUA};

    /**
     * All the Minecraft chat light colours
     **/
    public static final ChatFormatting[] LIGHT_COLOURS = {ChatFormatting.LIGHT_PURPLE};

    /**
     * All the Minecraft chat warm colours
     **/
    public static final ChatFormatting[] WARM_COLOURS = {ChatFormatting.DARK_RED, ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW};

    /**
     * All the Minecraft chat cool colours
     **/
    public static final ChatFormatting[] COOL_COLOURS = {ChatFormatting.GREEN, ChatFormatting.DARK_GREEN, ChatFormatting.BLUE, ChatFormatting.DARK_BLUE, ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_AQUA};

    /**
     * All the Minecraft chat dark colour shades
     **/
    public static final ChatFormatting[] DARK_SHADES = {ChatFormatting.BLACK, ChatFormatting.DARK_GRAY};

    /**
     * All the Minecraft chat light colour shades
     **/
    public static final ChatFormatting[] LIGHT_SHADES = {ChatFormatting.GRAY, ChatFormatting.WHITE};

    /**
     * Yellow bold formatting commonly used by Minecraft
     **/
    public static final ChatFormatting[] YELLOW_BOLD = {ChatFormatting.YELLOW, ChatFormatting.BOLD};

    /**
     * Cycles between chat formats in an array
     *
     * @param string The string to format
     * @param values The chat format values
     * @param delay  The cycle delay
     * @param step   The cycle step
     * @return The value with the formatting
     * @author cnlimiter
     **/
    public static String cycle(String string, ChatFormatting[] values, double delay, int step)
    {
        StringBuilder builder = new StringBuilder(string.length() * 3);

        if (delay <= 0) {
            delay = 0.001;
        }

        int offset = (int) Math.floor(Util.getMillis() / delay) % values.length;

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);

            int colour = ((i * step) + values.length - offset) % values.length;

            builder.append(values[colour].toString());
            builder.append(character);
        }

        return builder.toString();
    }

    /**
     * Cycles between chat formats in an array
     *
     * @param string The string to format
     * @param values The chat format values
     * @param delay  The cycle delay
     * @param step   The cycle step
     * @return A literal component with the formatting
     * @author cnlimiter
     **/
    public static MutableComponent cycleComponent(String string, ChatFormatting[] values, double delay, int step)
    {
        return Component.literal(ChatFormattings.cycle(string, values, delay, step));
    }

    /**
     * Filters enum values
     *
     * @param values The enum values
     * @param filter The filter to use
     * @return The filtered enum values as an array
     **/
    static ChatFormatting[] filterEnumValues(ChatFormatting[] values, Predicate<? super ChatFormatting> filter)
    {
        if (values != null && values.length > 0) {
            return Arrays.stream(values).filter(filter).toArray(ChatFormatting[]::new);
        } else {
            SnakerLib.LOGGER.error("Invalid chat formattings. Ignoring and returning fallback");
            return new ChatFormatting[]{ChatFormatting.RESET};
        }
    }
}
