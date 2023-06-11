package snaker.snakerlib.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import snaker.snakerlib.utility.SnakerUtil;

import javax.annotation.Nullable;

import static net.minecraft.client.renderer.RenderStateShard.*;

/**
 * Created by SnakerBone on 29/04/2023
 **/
public class RTB
{
    private final RenderType type;

    public RTB(@Nullable String name, VertexFormat format, VertexFormat.Mode mode, @Nullable Integer bufferSize, RenderType.CompositeState shard)
    {
        type = create(name, format, mode, bufferSize, shard);
    }

    public RTB(@Nullable String name, RenderType.CompositeState shard)
    {
        type = create(name, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, shard);
    }

    public RTB(VertexFormat format, RenderType.CompositeState shard)
    {
        type = create(null, format, VertexFormat.Mode.QUADS, 256, shard);
    }

    public RTB(RenderType.CompositeState shard)
    {
        type = create(null, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, shard);
    }

    protected RenderType create(@Nullable String shaderName, VertexFormat format, VertexFormat.Mode mode, @Nullable Integer bufferSize, RenderType.CompositeState shard)
    {
        String name = shaderName == null ? SnakerUtil.PLACEHOLDER : shaderName;
        int size = bufferSize == null ? 256 : bufferSize;
        return RenderType.create(name, format, mode, size, shard);
    }

    protected RenderType create(Category category, @Nullable String shaderName, VertexFormat format, VertexFormat.Mode mode, @Nullable Integer bufferSize, ShaderInstance instance, boolean outline)
    {
        String name = shaderName == null ? SnakerUtil.PLACEHOLDER : shaderName;
        int size = bufferSize == null ? 256 : bufferSize;
        switch (category) {
            case GAME_OBJECT -> {
                return RenderType.create(name, format, mode, size, RenderType.CompositeState.builder()
                        .setShaderState(new ShaderStateShard(() -> instance))
                        .setLightmapState(LIGHTMAP).setCullState(NO_CULL)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(outline));
            }
            case ENTITY_NORMAL -> {
                return RenderType.create(name, format, mode, size, RenderType.CompositeState.builder()
                        .setShaderState(new ShaderStateShard(() -> instance))
                        .setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST)
                        .setLightmapState(LIGHTMAP)
                        .createCompositeState(outline));
            }
            case ENTITY_TRANSLUCENT -> {
                return RenderType.create(name, format, mode, size, RenderType.CompositeState.builder()
                        .setShaderState(new ShaderStateShard(() -> instance))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(LIGHTMAP)
                        .setCullState(NO_CULL)
                        .setDepthTestState(EQUAL_DEPTH_TEST)
                        .createCompositeState(false));
            }
            case GUI_NORMAL -> {
                return RenderType.create(name, format, mode, size, RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(() -> instance))
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                        .createCompositeState(false));
            }
            case GUI_TRANSLUCENT -> {
                return RenderType.create(name, format, mode, size, RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(() -> instance))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                        .createCompositeState(false));
            }
            default -> throw new TypeNotPresentException(null, null);
        }
    }

    public RenderType get()
    {
        return type;
    }

    public enum Category
    {
        GAME_OBJECT,
        ENTITY_NORMAL,
        ENTITY_TRANSLUCENT,
        GUI_NORMAL,
        GUI_TRANSLUCENT,
    }
}
