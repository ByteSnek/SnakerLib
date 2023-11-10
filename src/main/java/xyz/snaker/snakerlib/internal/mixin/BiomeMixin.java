package xyz.snaker.snakerlib.internal.mixin;

import xyz.snaker.snakerlib.codec.Codecs;
import xyz.snaker.snakerlib.codec.MixinHooks;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import com.mojang.serialization.Codec;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Biome.class)
public abstract class BiomeMixin
{
    @Shadow
    @Final
    @Mutable
    public static Codec<Biome> DIRECT_CODEC;
    @Shadow
    @Final
    @Mutable
    public static Codec<Holder<Biome>> CODEC;

    static {
        DIRECT_CODEC = MixinHooks.makeBiomeCodec();
        CODEC = Codecs.registryEntryCodec(Registries.BIOME, DIRECT_CODEC);
    }
}
