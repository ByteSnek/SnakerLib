package xyz.snaker.snakerlib.codec;

import java.util.function.UnaryOperator;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 * <p>
 * A delegated error reporting codec
 *
 * @param <E>           The element of this codec
 * @param delegate      The delegate of this codec
 * @param errorReporter The error reporting function
 * @see Codecs#newErrorReportingCodec(Codec, String)
 * @see Codecs#newErrorReportingCodec(Codec, UnaryOperator)
 **/
record ErrorReportingCodec<E>(Codec<E> delegate, UnaryOperator<String> errorReporter) implements DelegateCodec<E>
{
    @Override
    public <T> DataResult<Pair<E, T>> decode(DynamicOps<T> ops, T input)
    {
        return delegate().decode(ops, input).mapError(errorReporter);
    }
}
