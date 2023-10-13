package xyz.snaker.snakerlib.codec;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import xyz.snaker.snakerlib.utility.tools.RegistryStuff;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public class Codecs
{
    public static <F, S> Codec<Either<F, S>> newEitherCodec(Codec<F> first, Codec<S> second)
    {
        return new EitherCodec<>(ShapedCodec.of(first), ShapedCodec.of(second));
    }

    public static <F, S> Codec<Either<F, S>> newEitherCodec(ShapedCodec<F> first, Codec<S> second)
    {
        return new EitherCodec<>(first, ShapedCodec.of(second));
    }

    public static <F, S> Codec<Either<F, S>> newEitherCodec(Codec<F> first, ShapedCodec<S> second)
    {
        return new EitherCodec<>(ShapedCodec.of(first), second);
    }

    public static <F, S> Codec<Either<F, S>> newShapedEitherCodec(ShapedCodec<F> first, ShapedCodec<S> second)
    {
        return new EitherCodec<>(first, second);
    }

    public static <E> Codec<List<E>> newListCodec(Codec<E> elementCodec)
    {
        return new ListCodec<>(elementCodec, (e, i) -> e + " at index " + i);
    }

    public static <E> Codec<List<E>> newListCodec(Codec<E> elementCodec, ListCodec.Reporter indexReporter)
    {
        return new ListCodec<>(elementCodec, indexReporter);
    }

    public static <E> Codec<E> newErrorReportingCodec(Codec<E> codec, String at)
    {
        return new ErrorReportingCodec<>(codec, e -> RegistryStuff.appendErrorLocation(e, '"' + at + '"'));
    }

    public static <E> Codec<E> newErrorReportingCodec(Codec<E> codec, UnaryOperator<String> errorReporter)
    {
        return new ErrorReportingCodec<>(codec, errorReporter);
    }

    public static <E> MapCodec<E> newErrorReportingCodec(MapCodec<E> codec, String at)
    {
        return new ErrorReportingMapCodec<>(codec, e -> RegistryStuff.appendErrorLocation(e, '"' + at + '"'));
    }

    public static <E> MapCodec<E> newErrorReportingCodec(MapCodec<E> codec, UnaryOperator<String> errorReporter)
    {
        return new ErrorReportingMapCodec<>(codec, errorReporter);
    }

    public static <E> MapCodec<E> newOptionalFieldOfCodec(Codec<E> codec, String name, E defaultValue)
    {
        return newOptionalFieldOfCodec(codec, name).xmap(o -> o.orElse(defaultValue), a -> Objects.equals(a, defaultValue) ? Optional.empty() : Optional.of(a));
    }

    public static <F> MapCodec<Optional<F>> newOptionalFieldOfCodec(Codec<F> elementCodec, String name)
    {
        return new OptionalCodec<>(name, elementCodec);
    }

    public static <E extends Enum<E> & StringRepresentable> Codec<E> newEnumCodec(String id, Supplier<E[]> enumValues)
    {
        Supplier<E[]> values = Suppliers.memoize(enumValues::get);
        Supplier<Function<String, E>> nameResolver = Suppliers.memoize(() ->
        {
            Map<String, E> map = Arrays.stream(values.get()).collect(Collectors.toMap(StringRepresentable::getSerializedName, Function.identity()));
            return map::get;
        });

        return ExtraCodecs.orCompressed(
                Codec.STRING.flatXmap(
                        name -> Optional.ofNullable(nameResolver.get().apply(name))
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown " + id + " name: " + name + ", expected one of [" + Arrays.stream(values.get()).map(StringRepresentable::getSerializedName).collect(Collectors.joining(", ")) + "]")),
                        value -> Optional.of(value.getSerializedName())
                                .map(DataResult::success)
                                .orElseGet(() -> DataResult.error(() -> "Unknown name for " + id + ": " + value))),
                ExtraCodecs.idResolverCodec(Enum::ordinal, index -> index >= 0 && index < values.get().length ? values.get()[index] : null, -1)
        );
    }

    public static <E> Codec<Holder<E>> newRegistryEntryCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec)
    {
        return new RegistryEntryCodec<>(registryKey, elementCodec);
    }

    public static <E> Codec<HolderSet<E>> newRegistryEntryListCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec)
    {
        return new RegistryListCodec<>(registryKey, newRegistryEntryCodec(registryKey, elementCodec), false);
    }
}
