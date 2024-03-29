package xyz.snaker.snakerlib.internal.mixin;

import xyz.snaker.snakerlib.codec.MixinHooks;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public abstract class SinglePoolElementMixin
{
    @Inject(method = "processorsCodec", at = @At("HEAD"), cancellable = true)
    private static <E extends SinglePoolElement> void improvedProcessorsCodec(CallbackInfoReturnable<RecordCodecBuilder<E, Holder<StructureProcessorList>>> cir)
    {
        cir.setReturnValue(MixinHooks.makeSinglePoolElementProcessorsCodec());
    }
}
