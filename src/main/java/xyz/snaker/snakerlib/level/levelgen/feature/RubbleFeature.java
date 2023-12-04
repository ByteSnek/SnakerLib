package xyz.snaker.snakerlib.level.levelgen.feature;

import java.util.List;

import xyz.snaker.snakerlib.utility.Worlds;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.BlockBlobFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

import com.mojang.serialization.Codec;

import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.Blocks.AIR;

/**
 * Created by SnakerBone on 25/08/2023
 **/
public abstract class RubbleFeature extends BlockBlobFeature
{
    private WorldGenLevel level;
    private final int frequency;
    private final List<Block> blocksToPlaceOn;
    private boolean isLevelSet;
    private final int minHeightRange;
    private final int maxHeightRange;

    public RubbleFeature(Codec<BlockStateConfiguration> codec, int frequency, List<Block> blocksToPlaceOn, int minHeightRange, int maxHeightRange)
    {
        super(codec);
        this.frequency = frequency;
        this.blocksToPlaceOn = blocksToPlaceOn;
        this.minHeightRange = minHeightRange;
        this.maxHeightRange = maxHeightRange;
    }

    public final boolean isReplaceableAt(BlockPos pos)
    {
        return level.getBlockState(pos).is(AIR) ||
                level.getBlockState(pos).is(BlockTags.FLOWERS) ||
                level.getBlockState(pos).is(BlockTags.LEAVES) ||
                level.getBlockState(pos).is(BlockTags.CAVE_VINES) ||
                level.getBlockState(pos).getBlock() instanceof BushBlock ||
                level.getBlockState(pos).canBeReplaced();
    }

    public abstract void placeStud(BlockPos pos, BlockState state);

    public abstract void placeBase(RandomSource random, BlockPos pos, BlockState state);

    public int getFrequency()
    {
        return frequency;
    }

    public void setLevel(WorldGenLevel level)
    {
        this.level = level;
        this.isLevelSet = level != null;
    }

    public WorldGenLevel getLevel()
    {
        if (isLevelSet) {
            return level;
        } else {
            throw new RuntimeException("Level Not Set");
        }
    }

    public boolean placeBlock(BlockPos pos, BlockState state)
    {
        return level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<BlockStateConfiguration> context)
    {
        setLevel(context.level());

        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockStateConfiguration config = context.config();
        BlockState state = config.state;

        if (random.nextInt(frequency) == 0) {
            for (Block block : blocksToPlaceOn) {
                if (level.getBlockState(origin.below()).is(block)) {
                    if (Worlds.isFreeAroundPos(level, origin, false)) {
                        for (int i = 0; i < random.nextInt(minHeightRange, maxHeightRange); i++) {
                            placeBlock(origin.above(i), state);
                            if (i == 0) {
                                placeBase(random, origin, state);
                            }
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
