package snaker.snakerlib.utility;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snaker.snakerlib.SnakerLib;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static snaker.snakerlib.SnakerLib.MODID;

/**
 * Created by SnakerBone on 20/02/2023
 **/
public class SnakerUtil
{
    public static final String PLACEHOLDER = MODID + ":" + PlaceHolders.PH8;
    public static final String PLACEHOLDER_NO_MODID = PlaceHolders.PH8;

    public static <T extends LivingEntity> boolean isEntityRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO || entity.yBodyRot != entity.yBodyRotO || entity.yHeadRot != entity.yHeadRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYBodyRotating(@NotNull T entity)
    {
        return entity.yBodyRot != entity.yBodyRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYHeadRotating(@NotNull T entity)
    {
        return entity.yHeadRot != entity.yHeadRotO;
    }

    public static <T extends Entity> boolean isEntityMoving(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingX(@NotNull T entity)
    {
        return entity.getX() != entity.xo;
    }

    public static <T extends Entity> boolean isEntityMovingY(@NotNull T entity)
    {
        return entity.getY() != entity.yo;
    }

    public static <T extends Entity> boolean isEntityMovingZ(@NotNull T entity)
    {
        return entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingXZ(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingXY(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo;
    }

    public static <T extends Entity> boolean isEntityMovingYZ(@NotNull T entity)
    {
        return entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    public static String untranslate(String text)
    {
        return !text.isEmpty() ? text.replaceAll("\\s+", "_").toLowerCase() : text;
    }

    public static String translate(String text)
    {
        if (!text.isEmpty()) {
            return Stream.of(text.trim().split("\\s|\\p{Pc}")).filter(word -> word.length() > 0).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
        } else {
            return text;
        }
    }

    public static String translate(String text, Rarity rarity)
    {
        switch (rarity) {
            case UNCOMMON -> {
                return "§e" + translate(text);
            }
            case RARE -> {
                return "§b" + translate(text);
            }
            case EPIC -> {
                return "§d" + translate(text);
            }
            default -> {
                return translate(text);
            }
        }
    }

    public static String untranslateComponent(MutableComponent component, boolean leaveCaps)
    {
        String string = component.getString();
        if (!string.isEmpty()) {
            string = string.replaceAll("\\p{P}", "");
        }
        return leaveCaps ? string : string.toLowerCase();
    }

    public static String untranslateComponent(MutableComponent component)
    {
        return untranslateComponent(component, false);
    }

    public static String appendId(String name)
    {
        return name + "_snkr";
    }

    public static int randomHex()
    {
        Random random = new Random();

        return random.nextInt(0xffffff + 1);
    }

    public static int hexToInt(String hexCode)
    {
        hexCode = hexCode.replace("#", "");

        return Integer.parseInt(hexCode, 16);
    }

    public static float hexToFloat(String hexCode)
    {
        hexCode = hexCode.replace("#", "");

        return Float.parseFloat(hexCode);
    }

    public static ResourceLocation noModel(int key)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), "geo/nil.geo.json");
    }

    public static ResourceLocation noAnimation(int key)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), "animations/nil.animation.json");
    }

    public static ResourceLocation noTexture(int key)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), "textures/clear.png");
    }

    public static ResourceLocation soildTexture(int key)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), "textures/solid.png");
    }

    public static ResourceLocation blockModel(int key)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), "geo/block.geo.json");
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Contract("null->null;!null->!null")
    public static <Anything> Anything unsafeCast(@Nullable Object object) // todo use until quack gets updated
    {
        return (Anything) object;
    }

    private static String generatePlaceholder(int limit)
    {
        return RandomStringUtils.randomAlphanumeric(limit).toUpperCase();
    }

    static class PlaceHolders
    {
        static String PH2 = generatePlaceholder(2);
        static String PH4 = generatePlaceholder(4);
        static String PH8 = generatePlaceholder(8);
        static String PH16 = generatePlaceholder(16);
        static String PH32 = generatePlaceholder(32);
        static String PH64 = generatePlaceholder(64);
    }
}