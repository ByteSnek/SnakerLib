package xyz.snaker.snakerlib.utility;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Created by SnakerBone on 18/09/2023
 **/
public class RegistryStreams
{
    /**
     * Gets registry objects that are registered to a deferred register
     *
     * @param registrar The deferred register
     * @param function  The collector function
     * @return A new stream of {@link T}
     * @throws RuntimeException If the deferred register contains no entries
     **/
    public static <T> Stream<T> getDeferredRegistries(DeferredRegister<T> registrar, IntFunction<T[]> function)
    {
        var entries = registrar.getEntries();
        if (entries.isEmpty()) {
            throw new RuntimeException("No registry objects found in registrar");
        }
        return Arrays.stream(entries.toArray(function));
    }

    /**
     * Gets registry objects that are registered to a deferred register
     *
     * @param registrar The deferred register
     * @param mapper    The collection mapper
     * @param function  The collector function
     * @return A new stream of {@link T}
     * @throws RuntimeException If the deferred register contains no entries
     **/
    public static <T> Stream<T> getDeferredRegistries(DeferredRegister<T> registrar, Function<DeferredHolder<T, ?>, ?> mapper, IntFunction<T[]> function)
    {
        var entries = registrar.getEntries();
        if (entries.isEmpty()) {
            throw new RuntimeException("No registry objects found in registrar");
        }
        return Arrays.stream(entries.stream().map(mapper).toArray(function));
    }

    /**
     * Gets and maps registry objects that are registered to a deferred register
     *
     * @param registrar The deferred register
     * @param function  The collector function
     * @return A new stream of {@link T}
     * @throws RuntimeException If the deferred register contains no entries or if any registry objects are not present
     **/
    public static <T> Stream<T> mapDeferredRegistries(DeferredRegister<T> registrar, IntFunction<T[]> function)
    {
        var entries = registrar.getEntries();
        if (entries.isEmpty()) {
            throw new RuntimeException("No registry objects found in registrar");
        }
        try {
            return Arrays.stream(entries.stream().map(DeferredHolder::get).toArray(function));
        } catch (NullPointerException e) {
            throw new RuntimeException("Could not get object(s) from registry object(s). Make sure you're calling CollectionStuff.mapDeferredRegistries at the correct time", e);
        }
    }
}
