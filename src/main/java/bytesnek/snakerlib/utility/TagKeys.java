package bytesnek.snakerlib.utility;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import bytesnek.snakerlib.resources.ResourceReference;

/**
 * Created by SnakerBone on 25/11/2023
 **/
public class TagKeys
{
    public static final TagKey<Block> IS_FLUID = key(Registries.BLOCK, "is_fluid");

    public static <T> TagKey<T> key(ResourceKey<Registry<T>> registry, String name)
    {
        return TagKey.create(registry, new ResourceReference(name));
    }
}
