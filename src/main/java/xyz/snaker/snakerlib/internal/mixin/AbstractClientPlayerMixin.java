package xyz.snaker.snakerlib.internal.mixin;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.resources.ResourceReference;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by SnakerBone on 29/11/2023
 **/
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin
{
    @Inject(method = "getSkin", at = @At("TAIL"), cancellable = true)
    public void setCustomSkin(CallbackInfoReturnable<PlayerSkin> cir)
    {
        ResourceReference skin = SnakerLib.getCustomSkin();

        if (skin != null) {
            cir.setReturnValue(new PlayerSkin(skin, null, null, null, PlayerSkin.Model.WIDE, true));
        }
    }
}
