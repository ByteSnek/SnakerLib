package xyz.snaker.snakerlib.data;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Created by SnakerBone on 21/09/2023
 **/
@FunctionalInterface
public interface DefaultEntityBlockConditions
{
    DefaultEntityBlockConditions OCELOT_OR_PARROT = (state, level, pos, type) -> type == EntityType.OCELOT || type == EntityType.PARROT;

    boolean apply(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> type);
}