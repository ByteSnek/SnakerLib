package bytesnek.snakerlib.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 19/08/2023
 **/
public class FlowerBlock extends PlantBlock
{
    /**
     * The voxel shape of this block
     **/
    private static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 10, 11);

    public FlowerBlock(Properties properties, TagKey<Block> allowedBlocks, boolean allowDirt)
    {
        super(properties, allowedBlocks, SHAPE, allowDirt);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context)
    {
        Vec3 offset = state.getOffset(level, pos);
        return shape.move(offset.x, offset.y, offset.z);
    }
}
