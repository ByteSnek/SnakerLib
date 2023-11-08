package xyz.snaker.snakerlib.internal.mixin;

import java.util.List;
import java.util.function.Function;

import xyz.snaker.snakerlib.level.levelgen.featurecycle.FeatureCycleSorter;
import xyz.snaker.snakerlib.utility.unsafe.TheUnsafe;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(FeatureSorter.class)
public abstract class FeatureSorterMixin
{
    @Inject(method = "buildFeaturesPerStep", at = @At("HEAD"), cancellable = true)
    private static <T> void sortFeatureCycles(List<T> biomes, Function<T, List<HolderSet<PlacedFeature>>> biomeFeatures, boolean topLevel, CallbackInfoReturnable<List<FeatureSorter.StepFeatureData>> cir)
    {
        cir.setReturnValue(FeatureCycleSorter.createFeatures(TheUnsafe.cast(biomes), TheUnsafe.cast(biomeFeatures)));
    }
}
