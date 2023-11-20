package bytesnek.snakerlib.client.render;

import javax.annotation.Nullable;
import java.util.Objects;

import bytesnek.snakerlib.SnakerLib;

import net.minecraft.client.renderer.RenderType;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;

/**
 * Created by SnakerBone on 12/08/2023
 **/
public interface SRTP extends RTP
{
    /**
     * A custom render type with the defaults applied
     *
     * @param name The name of the render type
     * @param pair A pair holding the vertex format and render type composite state
     * @return The render type
     **/
    @Override
    default RenderType create(@Nullable String name, Pair<VertexFormat, RenderType.CompositeState> pair)
    {
        return RenderType.create(Objects.requireNonNullElse(name, SnakerLib.placeholderWithId()), pair.getFirst(), VertexFormat.Mode.QUADS, RenderType.TRANSIENT_BUFFER_SIZE, pair.getSecond());
    }
}
