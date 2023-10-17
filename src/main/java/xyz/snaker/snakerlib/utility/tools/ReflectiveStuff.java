package xyz.snaker.snakerlib.utility.tools;

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
public class ReflectiveStuff
{
    public static Field getField(Class<?> clazz, String name, boolean isPrivateField)
    {
        try {
            Field field = clazz.getDeclaredField(name);
            if (isPrivateField) {
                field.setAccessible(true);
            }
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getFieldDirect(Class<?> clazz, String name, boolean isPrivate, @Nullable Object obj)
    {
        try {
            Field field = clazz.getDeclaredField(name);
            if (isPrivate) {
                field.setAccessible(true);
            }
            return UnsafeStuff.cast(field.get(obj));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T[] getFieldsInClass(Class<?> targetClass, Predicate<? super Object> filter, IntFunction<T[]> generator)
    {
        return UnsafeStuff.cast(Arrays.stream(targetClass.getDeclaredFields())
                .map(field -> getFieldDirect(targetClass, field.getName(), Modifier.isPrivate(field.getModifiers()), null))
                .filter(filter).toArray(generator));
    }

    public static <T> T getRandomFieldInClass(Class<?> targetClass, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator)
    {
        RandomSource random = RandomSource.create();
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field ->
        {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't get field: %s".formatted(field.getName()), e);
            }
        }).filter(filter == null ? o -> true : filter).toArray(generator), random);
    }

    public static <T> T getRandomFieldInClass(Class<?> targetClass, @Nullable Predicate<? super Object> filter, IntFunction<T[]> generator, RandomSource random)
    {
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field ->
        {
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
        return Util.getRandom(Arrays.stream(targetClass.getDeclaredFields()).map(field ->
        {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Couldn't get field: %s".formatted(field.getName()), e);
            }
        }).filter(filter == null ? o -> true : filter).toArray(generator), random);
    }
}
