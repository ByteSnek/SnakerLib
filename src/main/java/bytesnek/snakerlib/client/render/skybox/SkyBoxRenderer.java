package bytesnek.snakerlib.client.render.skybox;

import java.util.Objects;
import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;

/**
 * Created by SnakerBone on 27/09/2023
 * <p>
 * An implemention of a basic skybox renderer using a skybox texture
 *
 * @see SkyBoxTexture
 **/
public class SkyBoxRenderer
{
    /**
     * The skybox texture to use
     **/
    private final SkyBoxTexture skyBoxTexture;

    /**
     * The dimension for the skybox to be rendered in
     **/
    private final ResourceKey<Level> dimension;

    /**
     * Creates and renders the skybox directly for the specified dimension. Make sure this is done client side only (e.g. using {@link Dist#CLIENT} via {@link FMLEnvironment#dist} or similar)
     *
     * @param dimension The dimension
     * @param texture   The skybox texture to use
     * @return A new skybox renderer
     **/
    public static SkyBoxRenderer createForDimension(ResourceKey<Level> dimension, Supplier<SkyBoxTexture> texture)
    {
        return new SkyBoxRenderer(dimension, texture.get());
    }

    private SkyBoxRenderer(ResourceKey<Level> dimension, SkyBoxTexture texture)
    {
        this.skyBoxTexture = texture;
        this.dimension = dimension;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderLevelStage(RenderLevelStageEvent event)
    {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = Objects.requireNonNull(minecraft.getCameraEntity());
            ClientLevel level = Objects.requireNonNull(minecraft.level);
            if (level.dimension() == dimension) {
                RenderSystem.clear(GL11C.GL_COLOR_BUFFER_BIT | GL11C.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
                drawSkyBox(event, entity);
            }
        }
    }

    /**
     * Draws a single texture of the skybox as a quad
     *
     * @param event The parent event
     * @param side  The side of the texture to render
     * @param yaw   The yaw of the texture
     * @param pitch The pitch of the texture
     * @param roll  The roll of the texture
     * @param size  The size of the quad to draw
     **/
    public void drawQuad(RenderLevelStageEvent event, SkyBoxTexture.Side side, double yaw, double pitch, double roll, double size)
    {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = Objects.requireNonNull(minecraft.level);
        PoseStack stack = event.getPoseStack();

        float partialTick = event.getPartialTick();
        float position = (float) size;

        float UVO = 1;
        float UV1 = 1;
        float UV2 = 0;
        float UV3 = 0;

        float alpha = 1 - level.getRainLevel(partialTick);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.bindTexture(GL11.GL_ONE);
        RenderSystem.setShaderColor(1, 1, 1, alpha);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(GL11.GL_ZERO, skyBoxTexture.getTexture(side));

        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.mulPose(Axis.ZP.rotationDegrees((float) yaw));
        stack.mulPose(Axis.XP.rotationDegrees((float) pitch));
        stack.mulPose(Axis.YP.rotationDegrees((float) roll));

        Matrix4f pose = stack.last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.vertex(pose, -position, 100, -position).uv(UV2, UV3).endVertex();
        builder.vertex(pose, position, 100, -position).uv(UVO, UV3).endVertex();
        builder.vertex(pose, position, 100, position).uv(UVO, UV1).endVertex();
        builder.vertex(pose, -position, 100, position).uv(UV2, UV1).endVertex();

        BufferUploader.drawWithShader(builder.end());

        builder.discard();
        stack.popPose();

        RenderSystem.deleteTexture(GL11.GL_ZERO);
        RenderSystem.disableBlend();
    }

    /**
     * Draws the entire skybox
     *
     * @param event  The parent event
     * @param entity The camera entity
     **/
    public void drawSkyBox(RenderLevelStageEvent event, Entity entity)
    {
        if (entity.level().dimension() == dimension) {
            drawQuad(event, SkyBoxTexture.Side.FRONT, 0, 0, 0, 100);
            drawQuad(event, SkyBoxTexture.Side.RIGHT, -90, 0, 0, 100);
            drawQuad(event, SkyBoxTexture.Side.BACK, 180, 0, 0, 100);
            drawQuad(event, SkyBoxTexture.Side.LEFT, 90, 0, 0, 100);
            drawQuad(event, SkyBoxTexture.Side.UP, 0, -90, 0, 100);
            drawQuad(event, SkyBoxTexture.Side.DOWN, 0, 90, 0, 100);
        }
    }
}
