package xyz.snaker.snakerlib.internal.mixin;

import java.util.List;

import xyz.snaker.snakerlib.utility.tools.RegistryStuff;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.packs.resources.ResourceManager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(RegistryDataLoader.class)
public abstract class RegistryDataLoaderMixin
{
    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private static void loadAndReportErrors(ResourceManager resourceManager, RegistryAccess registryAccess, List<RegistryDataLoader.RegistryData<?>> registryData, CallbackInfoReturnable<RegistryAccess.Frozen> cir)
    {
        cir.setReturnValue(RegistryStuff.loadAllRegistryData(resourceManager, registryAccess, registryData));
    }
}
