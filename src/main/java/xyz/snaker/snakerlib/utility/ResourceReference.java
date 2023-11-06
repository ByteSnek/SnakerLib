package xyz.snaker.snakerlib.utility;

import xyz.snaker.snakerlib.SnakerLib;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 15/02/2023
 **/
public class ResourceReference extends ResourceLocation
{
    /**
     * An example resource path of a solid texture
     **/
    public static final ResourceReference SOLID_TEXTURE = new ResourceReference("textures/solid.png");

    /**
     * An example resource path of a transparent texture
     **/
    public static final ResourceReference NO_TEXTURE = new ResourceReference("textures/clear.png");

    /**
     * Creates a resource path with the class name that called this method as the path
     *
     * @return A new resource path
     **/
    public static ResourceReference currentClass()
    {
        Class<?> clazz = SnakerLib.STACK_WALKER.getCallerClass();

        return of(clazz);
    }

    /**
     * Creates a resource path using a class name
     *
     * @param clazz The class to get the name from
     * @return A new resource path with the class name
     **/
    public static <T> ResourceReference of(Class<T> clazz)
    {
        return new ResourceReference(clazz);
    }

    public ResourceReference(String path)
    {
        super(SnakerLib.MOD.get(), path);
    }

    private ResourceReference(Class<?> clazz)
    {
        super(SnakerLib.MOD.get(), Strings.i18nf(clazz.getSimpleName()));
    }
}
