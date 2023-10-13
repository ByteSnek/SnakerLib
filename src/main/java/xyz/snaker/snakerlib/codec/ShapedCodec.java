package xyz.snaker.snakerlib.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public interface ShapedCodec<A> extends Codec<A>
{
    static <A> ShapedCodec<A> ofNumber(Codec<A> codec)
    {
        return new CodecShape<>(codec)
        {
            @Override
            public <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input)
            {
                return ops.getNumberValue(input);
            }
        };
    }

    static <A> ShapedCodec<A> ofString(Codec<A> codec)
    {
        return new CodecShape<>(codec)
        {
            @Override
            public <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input)
            {
                return ops.getStringValue(input);
            }
        };
    }

    static <A> ShapedCodec<A> ofMap(Codec<A> codec)
    {
        return new CodecShape<>(codec)
        {
            @Override
            public <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input)
            {
                return ops.getMap(input);
            }
        };
    }

    static <A> ShapedCodec<A> ofList(Codec<A> codec)
    {
        return new CodecShape<>(codec)
        {
            @Override
            public <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input)
            {
                return ops.getList(input);
            }
        };
    }

    static <A> ShapedCodec<A> of(Codec<A> codec)
    {
        return new CodecShape<>(codec)
        {
            @Override
            public <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input)
            {
                return DataResult.success(input);
            }
        };
    }

    <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input);

    abstract class CodecShape<A> implements DelegateCodec<A>, ShapedCodec<A>
    {
        private final Codec<A> codec;

        CodecShape(Codec<A> codec)
        {
            this.codec = codec;
        }

        @Override
        public Codec<A> delegate()
        {
            return codec;
        }
    }
}
