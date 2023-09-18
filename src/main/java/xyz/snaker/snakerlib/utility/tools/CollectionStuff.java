package xyz.snaker.snakerlib.utility.tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Created by SnakerBone on 18/09/2023
 **/
public class CollectionStuff
{
    /**
     * Creates a new stream from a collection
     *
     * @param collection The collection to stream
     * @param function   The collector function
     * @return A new stream of {@link T}
     **/
    public static <T> Stream<T> newCollectionStream(Collection<T> collection, IntFunction<T[]> function)
    {
        return Arrays.stream(collection.toArray(function));
    }

    /**
     * Creates a new stream from a collection
     *
     * @param collection The collection to stream
     * @param mapper     The collection mapper
     * @param function   The collector function
     * @return A new stream of {@link T}
     **/
    public static <T> Stream<T> newCollectionStream(Collection<T> collection, Function<T, ?> mapper, IntFunction<T[]> function)
    {
        return Arrays.stream(collection.stream().map(mapper).toArray(function));
    }

    /**
     * Creates a new concurrent friendly stream from a collection
     *
     * @param collection The collection to stream
     * @param function   The collector function
     * @return A new stream of {@link T}
     **/
    public static <T> Stream<T> newConcurrentCollectionStream(Collection<T> collection, IntFunction<T[]> function)
    {
        return Arrays.stream(new CopyOnWriteArrayList<>(collection).toArray(function));
    }

    /**
     * Creates a new concurrent friendly stream from a collection
     *
     * @param collection The collection to stream
     * @param mapper     The collection mapper
     * @param function   The collector function
     * @return A new stream of {@link T}
     **/
    public static <T> Stream<T> newConcurrentCollectionStream(Collection<T> collection, Function<T, ?> mapper, IntFunction<T[]> function)
    {
        return Arrays.stream(new CopyOnWriteArrayList<>(collection).stream().map(mapper).toArray(function));
    }

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
    public static <T> Stream<T> getDeferredRegistries(DeferredRegister<T> registrar, Function<RegistryObject<T>, ?> mapper, IntFunction<T[]> function)
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
            return Arrays.stream(entries.stream().map(RegistryObject::get).toArray(function));
        } catch (NullPointerException e) {
            throw new RuntimeException("Could not get object(s) from registry object(s). Make sure you're calling CollectionStuff.mapDeferredRegistries at the correct time", e);
        }
    }
}
