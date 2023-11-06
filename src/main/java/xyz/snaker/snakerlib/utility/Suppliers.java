package xyz.snaker.snakerlib.utility;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by SnakerBone on 20/10/2023
 **/
public class Suppliers
{
    /**
     * An empty supplier instance
     **/
    public static final Supplier<?> EMPTY = () -> null;

    /**
     * Retrieves an object in a supplier
     *
     * @param supplier The supplier to retrieve the object from
     * @return The object in the supplier
     **/
    public static <T> T make(Supplier<T> supplier)
    {
        return supplier.get();
    }

    /**
     * Puts an object into a consumer and returns it
     *
     * @param obj      The object to put into the consumer
     * @param consumer The action to execute with said object in the consumer
     * @return The object put into the consumer
     **/
    public static <T> T make(T obj, Consumer<T> consumer)
    {
        consumer.accept(obj);
        return obj;
    }

    /**
     * Retrieves an object in a supplier
     *
     * @param supplier The supplier to retrieve the object from
     * @return The object in the supplier
     * @throws RuntimeException If something occurs or if the supplier was called at the incorrect time
     **/
    public static <T> T makeOrThrow(Supplier<T> supplier)
    {
        final T obj;

        try {
            obj = make(supplier);
        } catch (Throwable e) {
            throw new RuntimeException("Could not retrieve contents from supplier", e);
        }

        return obj;
    }

    /**
     * Puts an object into a consumer and returns it
     *
     * @param obj      The object to put into the consumer
     * @param consumer The action to execute with said object in the consumer
     * @return The object put into the consumer
     * @throws RuntimeException If something occurs or if the consumer was called at the incorrect time
     **/
    public static <T> T makeOrThrow(T obj, Consumer<T> consumer)
    {
        try {
            make(obj, consumer);
        } catch (Throwable e) {
            throw new RuntimeException("Could not put contents into consumer", e);
        }

        return obj;
    }
}
