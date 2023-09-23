package xyz.snaker.snakerlib.internal.mixin;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 10/08/2023
 **/
@Mixin(ForgeMod.class)
public class ForgeModMixin
{
    @Redirect(method = "preInit", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/VersionChecker;startVersionCheck()V"))
    public void supressVersionCheckerError(FMLCommonSetupEvent event) {}
}
