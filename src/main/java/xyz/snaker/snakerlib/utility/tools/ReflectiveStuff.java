package xyz.snaker.snakerlib.utility.tools;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

/**
 * Created by SnakerBone on 26/09/2023
 **/
public class ReflectiveStuff
{
    public static <T> T[] getFieldsInClass(Class<?> targetClass)
    {
        return UnsafeStuff.cast(Arrays.stream(targetClass.getDeclaredFields()).map(field -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toArray());
    }

    public static <T> T getRandomFieldInClass(Class<?> targetClass, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator)
    {
        RandomSource random = RandomSource.create();
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't get field: %s".formatted(field.getName()), e);
            }
        }).filter(filter == null ? o -> true : filter).toArray(generator), random);
    }

    public static <T> T getRandomFieldInClass(Class<?> targetClass, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator, RandomSource random)
    {
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't get field: %s".formatted(field.getName()), e);
            }
        }).filter(filter == null ? o -> true : filter).toArray(generator), random);
    }

    public static <T> T getRandomFieldInClass(Class<?> targetClass, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator, long seed)
    {
        RandomSource random = RandomSource.create(seed);
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't get field: %s".formatted(field.getName()), e);
            }
        }).filter(filter == null ? o -> true : filter).toArray(generator), random);
    }
}
