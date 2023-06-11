package snaker.snakerlib.utility;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.joml.Vector4f;
import snaker.snakerlib.client.shader.RTB;

import java.awt.*;
import java.util.function.Supplier;

import static net.minecraft.client.renderer.RenderStateShard.*;

/**
 * Created by SnakerBone on 28/04/2023
 **/
public class ShaderUtil
{
    public static <T extends LivingEntity> int packOverlay(T entity, int u)
    {
        return LivingEntityRenderer.getOverlayCoords(entity, u);
    }

    public static <T extends LivingEntity> int packOverlay(T entity)
    {
        return packOverlay(entity, 0);
    }

    public static Vector3f hexToVec3f(String hexCode)
    {
        if (!hexCode.startsWith("#")) {
            hexCode = "#" + hexCode;
        }
        Color colour = Color.decode(hexCode);
        float x = colour.getRed() / 255F;
        float y = colour.getGreen() / 255F;
        float z = colour.getBlue() / 255F;
        return new Vector3f(x, y, z);
    }

    public static Vector4f hexToVec4f(String hexCode)
    {
        if (!hexCode.startsWith("#")) {
            hexCode = "#" + hexCode;
        }
        Color colour = Color.decode(hexCode);
        float x = colour.getRed() / 255F;
        float y = colour.getGreen() / 255F;
        float z = colour.getBlue() / 255F;
        float w = colour.getAlpha();
        return new Vector4f(x, y, z, w);
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

    public static class RenderTypeBuffers
    {
        public static RTB gameObject(Supplier<ShaderInstance> shader)
        {
            return new RTB(RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(shader))
                    .setLightmapState(LIGHTMAP).setCullState(NO_CULL)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(false));
        }

        public static RTB entityNormal(Supplier<ShaderInstance> shader)
        {
            return new RTB(RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(shader))
                    .setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .setLightmapState(LIGHTMAP)
                    .createCompositeState(false));
        }

        public static RTB entityTranslucent(Supplier<ShaderInstance> shader)
        {
            return new RTB(DefaultVertexFormat.POSITION_TEX, RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(shader))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(LIGHTMAP).setCullState(NO_CULL)
                    .setDepthTestState(EQUAL_DEPTH_TEST)
                    .createCompositeState(false));
        }

        public static RTB guiNormal(Supplier<ShaderInstance> shader)
        {
            return new RTB(DefaultVertexFormat.POSITION_TEX, RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(shader))
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                    .createCompositeState(false));
        }

        public static RTB guiTranslucent(Supplier<ShaderInstance> shader)
        {
            return new RTB(DefaultVertexFormat.POSITION_TEX, RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(shader))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                    .createCompositeState(false));
        }
    }
}