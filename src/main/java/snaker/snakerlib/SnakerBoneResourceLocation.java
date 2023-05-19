package snaker.snakerlib;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 14/02/2023
 **/
@SuppressWarnings("unused")
public class SnakerBoneResourceLocation extends ResourceLocation
{
    public SnakerBoneResourceLocation(String path)
    {
        super(SnakerLib.DEFAULT_DEPENDANTS.get(2), path);
    }
}