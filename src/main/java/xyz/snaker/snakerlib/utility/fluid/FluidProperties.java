package xyz.snaker.snakerlib.utility.fluid;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Created by SnakerBone on 22/11/2023
 **/
@FunctionalInterface
public interface FluidProperties
{
    FluidProperties LAVA = (fluids, blockAndBucket) -> new BaseFlowingFluid.Properties(fluids.getLeft(), fluids.getMiddle(), fluids.getRight())
            .block(blockAndBucket.getLeft())
            .bucket(blockAndBucket.getRight())
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .tickRate(10);

    FluidProperties WATER = (fluids, blockAndBucket) -> new BaseFlowingFluid.Properties(fluids.getLeft(), fluids.getMiddle(), fluids.getRight())
            .block(blockAndBucket.getLeft())
            .bucket(blockAndBucket.getRight())
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .tickRate(5);

    BaseFlowingFluid.Properties apply(Triple<Supplier<? extends FluidType>, Supplier<? extends Fluid>, Supplier<? extends Fluid>> fluids, Pair<Supplier<? extends LiquidBlock>, Supplier<? extends Item>> blockAndBucket);
}
