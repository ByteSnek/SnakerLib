package bytesnek.snakerlib.resources;

import bytesnek.snakerlib.SnakerLib;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 15/02/2023
 **/
public class ResourceReference extends ResourceLocation
{
    public ResourceReference(String path)
    {
        super(SnakerLib.MOD.get(), path);
    }

    public static ResourceReference forClass(Class<?> clazz)
    {
        return new ResourceReference(clazz.getSimpleName().toLowerCase());
    }

    public static ResourceReference forCurrentClass()
    {
        return forClass(SnakerLib.STACK_WALKER.getCallerClass());
    }
}
