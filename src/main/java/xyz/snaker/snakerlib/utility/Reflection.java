package xyz.snaker.snakerlib.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

/**
 * Created by SnakerBone on 26/09/2023
 **/
public class Reflection
{
    public static <T> T getFieldDirect(Class<?> clazz, String name, boolean isPrivate, @Nullable Object obj)
    {
        try {
            Field field = clazz.getDeclaredField(name);
            if (isPrivate) {
                field.setAccessible(true);
            }
            return TheUnsafe.cast(field.get(obj));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T[] getFieldsInClass(Class<?> clazz, Predicate<? super Object> filter, IntFunction<T[]> generator)
    {
        return TheUnsafe.cast(Arrays.stream(clazz.getDeclaredFields())
                .map(field -> getFieldDirect(clazz, field.getName(), Modifier.isPrivate(field.getModifiers()), null))
                .filter(filter)
                .toArray(generator)
        );
    }

    public static <T> T getRandomFieldInClass(Class<?> clazz, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator)
    {
        return Util.getRandom(Arrays.stream(clazz.getDeclaredFields())
                .map(field -> getFieldDirect(clazz, field.getName(), Modifier.isPrivate(field.getModifiers()), null))
                .filter(filter == null ? o -> true : filter)
                .toArray(generator), RandomSource.create()
        );
    }
}
