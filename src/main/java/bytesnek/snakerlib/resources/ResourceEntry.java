package bytesnek.snakerlib.resources;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 26/11/2023
 **/
public interface ResourceEntry<T>
{
    ResourceLocation getResourceLocation(T key);

    String getPath(T key);

    String getNamespace(T key);

    String getRegistryName(T key);

    T get(ResourceLocation value);
}
