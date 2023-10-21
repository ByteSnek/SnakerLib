package xyz.snaker.snakerlib.codec;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 * <p>
 * An either codec record
 *
 * @param <F> The first element of this codec
 * @param <S> The second element of this codec
 * @see Either
 * @see Codecs#newEitherCodec(Codec, ShapedCodec)
 * @see Codecs#newEitherCodec(Codec, Codec)
 * @see Codecs#newEitherCodec(ShapedCodec, Codec)
 **/
record EitherCodec<F, S>(ShapedCodec<F> first, ShapedCodec<S> second) implements Codec<Either<F, S>>
{
    @Override
    public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> ops, T input)
    {
        DataResult<?> firstShape = first.decodeShape(ops, input), secondShape = second.decodeShape(ops, input);

        if (firstShape.result().isPresent() && secondShape.error().isPresent()) {
            return first.decode(ops, input).map(vo -> vo.mapFirst(Either::left));
        } else if (firstShape.error().isPresent() && secondShape.result().isPresent()) {
            return second.decode(ops, input).map(vo -> vo.mapFirst(Either::right));
        } else {
            DataResult<Pair<Either<F, S>, T>> firstRead = first.decode(ops, input).map(vo -> vo.mapFirst(Either::left));

            if (firstRead.result().isPresent()) {
                return firstRead;
            }

            DataResult<Pair<Either<F, S>, T>> secondRead = second.decode(ops, input).map(vo -> vo.mapFirst(Either::right));

            if (secondRead.result().isPresent()) {
                return secondRead;
            }

            return firstRead.mapError(err -> secondRead.error().map(pr -> "Either [" + err + "; " + pr.message() + "]").orElse(err));
        }
    }

    @Override
    public <T> DataResult<T> encode(Either<F, S> input, DynamicOps<T> ops, T prefix)
    {
        return input.map(value1 -> first.encode(value1, ops, prefix), value2 -> second.encode(value2, ops, prefix));
    }
}
