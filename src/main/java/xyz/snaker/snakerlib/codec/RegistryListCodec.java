package xyz.snaker.snakerlib.codec;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

/**
 * Created by SnakerBone on 13/10/2023
 * <p>
 * A registry list codec
 *
 * @param <E> The element of this codec
 **/
class RegistryListCodec<E> implements Codec<HolderSet<E>>
{
    /**
     * The registry key for this codec
     **/
    private final ResourceKey<? extends Registry<E>> registryKey;

    /**
     * The internal list codec
     **/
    private final Codec<List<Holder<E>>> listCodec;

    /**
     * The direct list codec
     **/
    private final Codec<List<Holder.Direct<E>>> directListCodec;

    /**
     * The registry aware tag or list codec
     **/
    private final Codec<Either<TagKey<E>, List<Holder<E>>>> registryAwareTagOrListCodec;

    /**
     * Creates a new registry list codec
     **/
    public RegistryListCodec(ResourceKey<? extends Registry<E>> registryKey, Codec<Holder<E>> elementCodec, boolean onlyLists)
    {
        this.registryKey = registryKey;

        Function<List<Holder<E>>, DataResult<List<Holder<E>>>> check = ExtraCodecs.ensureHomogenous(Holder::kind);
        Codec<List<Holder<E>>> listCodec = Codecs.newListCodec(elementCodec).flatXmap(check, check);

        this.listCodec = onlyLists ? listCodec : Codec.either(listCodec, elementCodec).xmap(listOrSingleton -> listOrSingleton.map(list -> list, List::of), list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list));
        this.registryAwareTagOrListCodec = Codecs.newShapedEitherCodec(ShapedCodec.ofString(TagKey.hashedCodec(registryKey)), ShapedCodec.ofList(listCodec));
        this.directListCodec = Codecs.newListCodec(
                elementCodec.comapFlatMap(
                        holder ->
                        {
                            if (holder instanceof Holder.Direct<E> direct) {
                                return DataResult.success(direct);
                            }

                            return DataResult.error(() -> "Can't decode element " + holder + " without registry present");
                        },
                        holder -> holder
                ));
    }

    @Override
    public <T> DataResult<Pair<HolderSet<E>, T>> decode(DynamicOps<T> ops, T input)
    {
        if (ops instanceof RegistryOps<T> registryOps) {
            Optional<HolderGetter<E>> optionalGetter = registryOps.getter(registryKey);

            if (optionalGetter.isPresent()) {
                HolderGetter<E> getter = optionalGetter.get();

                return registryAwareTagOrListCodec.decode(ops, input).map(pair -> pair.mapFirst(either -> either.map(getter::getOrThrow, HolderSet::direct)));
            }
        }

        return decodeWithoutRegistry(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(HolderSet<E> input, DynamicOps<T> ops, T prefix)
    {
        if (ops instanceof RegistryOps<T> registryOps) {
            Optional<HolderOwner<E>> optionalOwner = registryOps.owner(registryKey);

            if (optionalOwner.isPresent()) {
                if (!input.canSerializeIn(optionalOwner.get())) {
                    return DataResult.error(() -> "HolderSet " + input + " is not valid in current registry set");
                }

                return registryAwareTagOrListCodec.encode(input.unwrap().mapRight(List::copyOf), ops, prefix);
            }
        }

        return encodeWithoutRegistry(input, ops, prefix);
    }

    /**
     * Decodes a codec without registries
     **/
    private <T> DataResult<Pair<HolderSet<E>, T>> decodeWithoutRegistry(DynamicOps<T> ops, T input)
    {
        return directListCodec.decode(ops, input).map(pair -> pair.mapFirst(HolderSet::direct));
    }

    /**
     * Encodes a codec without registries
     **/
    private <T> DataResult<T> encodeWithoutRegistry(HolderSet<E> input, DynamicOps<T> ops, T prefix)
    {
        return listCodec.encode(input.stream().toList(), ops, prefix);
    }
}
