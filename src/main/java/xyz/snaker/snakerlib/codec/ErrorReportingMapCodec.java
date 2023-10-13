package xyz.snaker.snakerlib.codec;

import java.util.function.UnaryOperator;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public class ErrorReportingMapCodec<E> extends DelegateMapCodec<E>
{
    private final UnaryOperator<String> errorReporter;

    public ErrorReportingMapCodec(MapCodec<E> delegate, UnaryOperator<String> errorReporter)
    {
        super(delegate);
        this.errorReporter = errorReporter;
    }

    @Override
    public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input)
    {
        return super.decode(ops, input).mapError(errorReporter);
    }
}
