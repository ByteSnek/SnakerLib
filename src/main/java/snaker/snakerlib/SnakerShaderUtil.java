package snaker.snakerlib;

import codechicken.lib.render.shader.CCShaderInstance;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import snaker.snakerlib.client.shader.Shader;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.client.renderer.RenderStateShard.*;

/**
 * Created by SnakerBone on 28/04/2023
 **/
public class SnakerShaderUtil
{
    public static <X extends BlockEntity> BlockEntity createBlockEntity(RegistryObject<BlockEntityType<X>> type, @NotNull BlockPos pos, BlockState state)
    {
        if (type != null && type.isPresent()) {
            return type.get().create(pos, state);
        } else {
            SnakerLib.LOGGER.error("The shader block placed at [ " + pos.toShortString() + " ] is null");
            return null;
        }
    }

    public static <T extends LivingEntity> int packOverlay(T entity, int u)
    {
        return LivingEntityRenderer.getOverlayCoords(entity, u);
    }

    public static <T extends LivingEntity> int packOverlay(T entity)
    {
        return packOverlay(entity, 0);
    }

    public static float[] hexToVec3(String hexCode)
    {
        if (!hexCode.startsWith("#")) {
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
        if (!hexCode.startsWith("#")) {
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

    public static void accept(RegisterShadersEvent event, Integer key, String name, Consumer<ShaderInstance> shader)
    {
        event.registerShader(CCShaderInstance.create(event.getResourceManager(), new ResourceLocation(SnakerLib.DEFAULT_DEPENDANTS.get(key), name), DefaultVertexFormat.POSITION_TEX), shader);
    }

    public static Shader createObjectShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader)).setLightmapState(LIGHTMAP).setCullState(NO_CULL).setOverlayState(OVERLAY).createCompositeState(false));
    }

    public static Shader createEntityShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader)).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setLightmapState(LIGHTMAP).createCompositeState(false));
    }

    public static Shader createTranslucentEntityShader(Supplier<ShaderInstance> shader)
    {
        return new Shader(DefaultVertexFormat.POSITION_TEX, RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).createCompositeState(false));
    }
}