package xyz.snaker.snakerlib.internal.mixin;

import java.util.function.Consumer;

import xyz.snaker.snakerlib.utility.unsafe.TheUnsafe;
import xyz.snaker.snakerlib.utility.Worlds;

import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;

import com.mojang.serialization.DataResult;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin
{
    @Dynamic
    @Redirect(method = "*(Lnet/minecraft/server/WorldLoader$DataLoadContext;)Lnet/minecraft/server/WorldLoader$DataLoadOutput;", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow(ZLjava/util/function/Consumer;)Ljava/lang/Object;", remap = false))
    private <E> E resolveErrorMessages(DataResult<E> result, boolean allowPartial, Consumer<String> onError)
    {
        return TheUnsafe.cast(Worlds.printWorldGenSettingsError(TheUnsafe.cast(result)));
    }
}
