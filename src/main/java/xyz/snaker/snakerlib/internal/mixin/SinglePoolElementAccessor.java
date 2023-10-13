package xyz.snaker.snakerlib.internal.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor
{
    @Accessor("processors")
    Holder<StructureProcessorList> snakerlib$getProcessors();
}
