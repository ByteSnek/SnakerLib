package xyz.snaker.snakerlib.utility;

import java.lang.annotation.Annotation;

import xyz.snaker.snakerlib.SnakerLib;

/**
 * Created by SnakerBone on 22/08/2023
 **/
public class Annotations
{
    public static <V> boolean isPresent(V obj, Class<? extends Annotation> annotationClass)
    {
        return obj.getClass().isAnnotationPresent(annotationClass);
    }

    public static boolean isPresent(Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        return clazz.isAnnotationPresent(annotationClass);
    }

    public static boolean isPresent(Class<? extends Annotation> annotationClass)
    {
        return SnakerLib.STACK_WALKER.getCallerClass().isAnnotationPresent(annotationClass);
    }
}
