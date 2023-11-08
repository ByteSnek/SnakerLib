package xyz.snaker.snakerlib.utility.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by SnakerBone on 21/08/2023
 **/
@FunctionalInterface
public interface BlockConditions
{
    BlockConditions ALWAYS = (state, level, pos) -> true;
    BlockConditions NEVER = (state, level, pos) -> false;

    boolean apply(BlockState state, BlockGetter level, BlockPos pos);
}
