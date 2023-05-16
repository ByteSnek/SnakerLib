package snaker.snakerlib;

import net.minecraft.resources.ResourceLocation;

/**
 * Created by SnakerBone on 14/02/2023
 **/
@SuppressWarnings("unused")
public class ForgeResourceLocation extends ResourceLocation
{
    public ForgeResourceLocation(String path)
    {
        super(SnakerLib.DEPENDANTS[0], path);
    }
}
