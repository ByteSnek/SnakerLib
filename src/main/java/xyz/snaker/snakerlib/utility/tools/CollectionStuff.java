package xyz.snaker.snakerlib.utility.tools;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Stream;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Created by SnakerBone on 18/09/2023
 **/
public class CollectionStuff
{
    public static <T> Stream<T> mapDeferredRegistries(DeferredRegister<T> registrar, IntFunction<T[]> function)
    {
        return Arrays.stream(registrar.getEntries().stream().map(RegistryObject::get).toArray(function));
    }

    public static <T> Stream<T> getDeferredRegistries(DeferredRegister<T> registrar, IntFunction<T[]> function)
    {
        return Arrays.stream(registrar.getEntries().toArray(function));
    }
}
