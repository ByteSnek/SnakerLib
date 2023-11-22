package bytesnek.snakerlib.utility.fluid;

import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * Created by SnakerBone on 22/11/2023
 **/
@FunctionalInterface
public interface FluidProperties
{
    FluidProperties LAVA = (fluids, blockAndBucket) -> new ForgeFlowingFluid.Properties(fluids.getLeft(), fluids.getMiddle(), fluids.getRight())
            .block(blockAndBucket.getLeft())
            .bucket(blockAndBucket.getRight())
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .tickRate(10);

    FluidProperties WATER = (fluids, blockAndBucket) -> new ForgeFlowingFluid.Properties(fluids.getLeft(), fluids.getMiddle(), fluids.getRight())
            .block(blockAndBucket.getLeft())
            .bucket(blockAndBucket.getRight())
            .slopeFindDistance(4)
            .levelDecreasePerBlock(1)
            .tickRate(5);

    ForgeFlowingFluid.Properties apply(Triple<Supplier<? extends FluidType>, Supplier<? extends Fluid>, Supplier<? extends Fluid>> fluids, Pair<Supplier<? extends LiquidBlock>, Supplier<? extends Item>> blockAndBucket);
}
