package bytesnek.snakerlib.internal.mixin;

import net.minecraft.client.renderer.EffectInstance;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 23/09/2023
 **/
@Mixin(EffectInstance.class)
public class EffectInstanceMixin
{
    @Redirect(method = "updateLocations", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", ordinal = 1))
    void suppressUnusedUniformsInShaderProgram(Logger instance, String format, Object arg1, Object arg2) {}
}