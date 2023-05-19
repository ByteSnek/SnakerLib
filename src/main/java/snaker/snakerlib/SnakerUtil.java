package snaker.snakerlib;

import codechicken.lib.render.shader.CCShaderInstance;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import snaker.snakerlib.shader.Shader;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.client.renderer.RenderStateShard.*;
import static snaker.snakerlib.SnakerLib.MODID;

/**
 * Created by SnakerBone on 20/02/2023
 **/
@SuppressWarnings("unused")
public class SnakerUtil
{
    public static final String PLACEHOLDER = MODID + ":" + PlaceHolders.PH8;
    public static final String PLACEHOLDER_NO_MODID = PlaceHolders.PH8;

    public static <X extends BlockEntity> BlockEntity createBlockEntity(RegistryObject<BlockEntityType<X>> type, @NotNull BlockPos pos, BlockState state)
    {
        if (type != null && type.isPresent())
        {
            return type.get().create(pos, state);
        } else
        {
            SnakerLib.LOGGER.error("The shader block placed at [ " + pos.toShortString() + " ] is null");
            return null;
        }
    }

    public static float[] hexToVec3(String hexCode)
    {
        if (!hexCode.startsWith("#"))
        {
            hexCode = "#" + hexCode;
        }

        Color colour = Color.decode(hexCode);

        float[] vec = new float[3];

        vec[0] = colour.getRed() / 255F;
        vec[1] = colour.getGreen() / 255F;
        vec[2] = colour.getBlue() / 255F;

        return vec;
    }

    public static float[] hexToVec4(String hexCode)
    {
        if (!hexCode.startsWith("#"))
        {
            hexCode = "#" + hexCode;
        }

        Color colour = Color.decode(hexCode);

        float[] vec = new float[4];

        vec[0] = colour.getRed() / 255F;
        vec[1] = colour.getGreen() / 255F;
        vec[2] = colour.getBlue() / 255F;
        vec[3] = colour.getAlpha() / 255F;

        return vec;
    }

    public static void accept(RegisterShadersEvent event, String name, Consumer<ShaderInstance> shader, int i)
    {
        event.registerShader(CCShaderInstance.create(event.getResourceManager(), new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), name), DefaultVertexFormat.POSITION_TEX), shader);
    }

    public static Shader createObjectShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(shader)).setLightmapState(LIGHTMAP).setCullState(NO_CULL).setOverlayState(OVERLAY).createCompositeState(false));
    }

    public static Shader createEntityShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader)).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setLightmapState(LIGHTMAP).createCompositeState(false));
    }

    public static Shader createTranslucentEntityShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(DefaultVertexFormat.POSITION_TEX, RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).createCompositeState(false));
    }

    public static <X> DeferredRegister<X> createDeferredRegistry(IForgeRegistry<X> type, int i)
    {
        return DeferredRegister.create(type, SnakerLib.DEFAULT_DEPENDANTS.get(i));
    }

    public static <X> DeferredRegister<X> createDeferredRegistry(ResourceKey<? extends Registry<X>> key, int i)
    {
        return DeferredRegister.create(key, SnakerLib.DEFAULT_DEPENDANTS.get(i));
    }

    public static boolean generateFlag(int fract)
    {
        RandomSource random = RandomSource.create();
        return random.nextInt(100) < fract;
    }

    public static boolean generateFlag(int bound, int fract)
    {
        RandomSource random = RandomSource.create();
        return random.nextInt(bound) < fract;
    }

    public static boolean getRandom()
    {
        RandomSource random = RandomSource.create();
        return random.nextBoolean();
    }

    public static String untranslate(String text)
    {
        return !text.isEmpty() ? text.replaceAll("\\s+", "_").toLowerCase() : text;
    }

    public static String translate(String text)
    {
        if (!text.isEmpty())
        {
            return Stream.of(text.trim().split("\\s|\\p{Pc}")).filter(word -> word.length() > 0).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
        } else
        {
            return text;
        }
    }

    public static String translate(String text, Rarity rarity)
    {
        switch (rarity)
        {
            case UNCOMMON ->
            {
                return "§e" + translate(text);
            }
            case RARE ->
            {
                return "§b" + translate(text);
            }
            case EPIC ->
            {
                return "§d" + translate(text);
            }
            default ->
            {
                return translate(text);
            }
        }
    }

    public static String untranslateComponent(MutableComponent component, boolean leaveCaps)
    {
        String string = component.getString();
        if (!string.isEmpty())
        {
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

    public static ResourceLocation noModel(int i)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), "geo/nil.geo.json");
    }

    public static ResourceLocation noAnimation(int i)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), "animations/nil.animation.json");
    }

    public static ResourceLocation noTexture(int i)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), "textures/clear.png");
    }

    public static ResourceLocation soildTexture(int i)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), "textures/solid.png");
    }

    public static ResourceLocation blockModel(int i)
    {
        return new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(i), "geo/block.geo.json");
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