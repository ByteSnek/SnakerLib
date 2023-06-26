package snaker.snakerlib.utility;

import net.minecraft.resources.ResourceLocation;
import snaker.snakerlib.SnakerLib;

/**
 * Created by SnakerBone on 15/02/2023
 **/
public class SnakerBoneResourceLocation extends ResourceLocation
{
    public SnakerBoneResourceLocation(ResourceDirectoryBuilder builder)
    {
        this(builder.getPath());
    }

    public SnakerBoneResourceLocation(String path)
    {
        super(SnakerLib.MODID, path);
    }

    public <T> SnakerBoneResourceLocation(Class<T> clazz)
    {
        super(SnakerLib.MODID, clazz.getSimpleName().toLowerCase());
    }
}
