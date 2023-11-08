package xyz.snaker.snakerlib.chat;

import java.util.function.Function;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.Strings;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;

/**
 * Created by SnakerBone on 17/06/2023
 **/
public class ChatComponents
{
    public static final MutableComponent MOD = Component.literal(Strings.i18nt(SnakerLib.MOD.get()));
    public static final MutableComponent COLON = Component.literal(":");
    public static final MutableComponent ENABLED = Component.translatable("addServer.resourcePack.enabled");
    public static final MutableComponent DISABLED = Component.translatable("addServer.resourcePack.disabled");
    public static final MutableComponent DEBUG = Component.translatable("snakerlib.debug");
    public static final MutableComponent INFO = Component.translatable("snakerlib.info");

    public static final Function<String, MutableComponent> HELLO = punctuation -> Component.translatable("snakerlib.hello", punctuation);
    public static final Function<String, MutableComponent> GOODBYE = punctuation -> Component.translatable("snakerlib.goodbye", punctuation);

    public static MutableComponent info(String key, Object... args)
    {
        return notification(ChatComponents.INFO, key, args);
    }

    public static MutableComponent debug(String key, Object... args)
    {
        return notification(ChatComponents.DEBUG, key, args);
    }

    public static MutableComponent mod(String key, Object... args)
    {
        return notification(ChatComponents.MOD, key, args);
    }

    public static MutableComponent notification(MutableComponent toWrap, String key, Object... args)
    {
        return notification(toWrap, key, ChatFormattings.YELLOW_BOLD, args);
    }

    public static MutableComponent notification(MutableComponent toWrap, String key, ChatFormatting[] formattings, Object... args)
    {
        return Component.empty().append(ComponentUtils
                        .wrapInSquareBrackets(toWrap)
                        .withStyle(formattings)
                        .append(ChatComponents.COLON)
                        .append(CommonComponents.SPACE))
                .append(Component.translatable(key, args));
    }
}
