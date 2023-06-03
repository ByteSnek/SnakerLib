package snaker.snakerlib.client.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import snaker.snakerlib.SnakerUtil;

import javax.annotation.Nullable;

import static net.minecraft.client.renderer.RenderStateShard.*;

/**
 * Created by SnakerBone on 29/04/2023
 **/
public class Shader
{
    private final RenderType type;

    public Shader(@Nullable String name, VertexFormat format, VertexFormat.Mode mode, @Nullable Integer bufferSize, RenderType.CompositeState shard)
    {
        type = create(name, format, mode, bufferSize, shard);
    }

    public Shader(@Nullable String name, RenderType.CompositeState shard)
    {
        type = create(name, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, shard);
    }

    public Shader(VertexFormat format, RenderType.CompositeState shard)
    {
        type = create(null, format, VertexFormat.Mode.QUADS, 256, shard);
    }

    public Shader(RenderType.CompositeState shard)
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
        RenderType.CompositeState type;
        RenderType.CompositeState itemAndBlockShard = RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(() -> instance)).setLightmapState(LIGHTMAP).setCullState(NO_CULL).setOverlayState(OVERLAY).createCompositeState(outline);
        RenderType.CompositeState entityShard = RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(() -> instance)).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setLightmapState(LIGHTMAP).createCompositeState(outline);
        switch (category) {
            case OBJECT -> type = itemAndBlockShard;
            case ENTITY -> type = entityShard;
            default -> throw new RuntimeException("Category must be either OBJECT or ENTITY");
        }
        String name = shaderName == null ? SnakerUtil.PLACEHOLDER : shaderName;
        int size = bufferSize == null ? 256 : bufferSize;
        return RenderType.create(name, format, mode, size, type);
    }

    public RenderType get()
    {
        return type;
    }

    public enum Category
    {
        OBJECT,
        ENTITY
    }
}
