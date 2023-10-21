package xyz.snaker.snakerlib.codec;

import java.util.stream.Stream;

import com.mojang.serialization.*;

/**
 * Created by SnakerBone on 13/10/2023
 * <p>
 * A delegated map codec
 *
 * @see ErrorReportingCodec
 **/
class DelegateMapCodec<A> extends MapCodec<A>
{
    /**
     * The delegate for this map codec
     **/
    private final MapCodec<A> delegate;

    /**
     * Creates a new delegated map codec
     **/
    public DelegateMapCodec(MapCodec<A> delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Gets the delegate of this map codec
     *
     * @return The delegate of this map codec
     **/
    public MapCodec<A> delegate()
    {
        return delegate;
    }

    @Override
    public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input)
    {
        return delegate().decode(ops, input);
    }

    @Override
    public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix)
    {
        return delegate().encode(input, ops, prefix);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops)
    {
        return delegate().keys(ops);
    }
}
