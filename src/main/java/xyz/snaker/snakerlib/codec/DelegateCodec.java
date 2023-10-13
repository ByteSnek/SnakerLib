package xyz.snaker.snakerlib.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public interface DelegateCodec<A> extends Codec<A>
{
    Codec<A> delegate();

    @Override
    default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input)
    {
        return delegate().decode(ops, input);
    }

    @Override
    default <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix)
    {
        return delegate().encode(input, ops, prefix);
    }
}
