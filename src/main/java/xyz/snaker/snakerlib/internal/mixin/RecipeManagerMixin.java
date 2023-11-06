package xyz.snaker.snakerlib.internal.mixin;

import java.util.Map;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.Loggers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin
{
    @Shadow
    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;

    @Redirect(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private void simplifyRecipeErrors(Logger logger, String message, Object p0, Object p1)
    {
        Loggers.cleanRecipeError(SnakerLib.LOGGER, message, p0, p1);
    }

    @Redirect(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;size()I", remap = false))
    private int correctRecipeCount(Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> map)
    {
        return recipes.values().stream().mapToInt(Map::size).sum();
    }
}
