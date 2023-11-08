package xyz.snaker.snakerlib.internal.mixin;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.Logging;

import net.minecraft.world.level.storage.loot.LootDataManager;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(LootDataManager.class)
public abstract class LootTablesMixin
{
    @Dynamic
    @Redirect(method = "*(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonElement;)V", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), require = 0)
    private static void simplifyLootTableErrors(Logger logger, String message, Object p0, Object p1)
    {
        Logging.cleanLootTableError(SnakerLib.LOGGER, message, p0, p1);
    }
}
