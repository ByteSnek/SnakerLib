package xyz.snaker.snakerlib.internal.mixin;

import java.util.List;

import xyz.snaker.snakerlib.codec.Codecs;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(StructureTemplatePool.class)
public abstract class StructureTemplatePoolMixin
{
    @Shadow
    @Final
    @Mutable
    public static Codec<StructureTemplatePool> DIRECT_CODEC;

    @Shadow
    @Final
    @Mutable
    public static Codec<Holder<StructureTemplatePool>> CODEC;

    static {
        CODEC = Codecs.newRegistryEntryCodec(Registries.TEMPLATE_POOL, DIRECT_CODEC);
    }

    @Dynamic
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;listOf()Lcom/mojang/serialization/Codec;", remap = false))
    private static Codec<List<Pair<StructurePoolElement, Integer>>> addReportingToCodec(Codec<Pair<StructurePoolElement, Integer>> codec)
    {
        return Codecs.newListCodec(codec);
    }
}
