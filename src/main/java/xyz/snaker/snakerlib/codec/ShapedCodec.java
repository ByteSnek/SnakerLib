package xyz.snaker.snakerlib.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public interface ShapedCodec<A> extends Codec<A>
{
    /**
     * Creates a new shaped codec with a number element
     **/
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

    /**
     * Creates a new shaped codec with a string element
     **/
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

    /**
     * Creates a new shaped codec with a map element
     **/
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

    /**
     * Creates a new shaped codec with a list element
     **/
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

    /**
     * Creates a new shaped codec
     **/
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

    /**
     * Decodes this codec
     **/
    <T> DataResult<?> decodeShape(DynamicOps<T> ops, T input);

    /**
     * A codec shape implementation
     **/
    abstract class CodecShape<A> implements DelegateCodec<A>, ShapedCodec<A>
    {
        /**
         * The internal codec
         **/
        private final Codec<A> codec;

        /**
         * Creates a new codec shape
         **/
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
