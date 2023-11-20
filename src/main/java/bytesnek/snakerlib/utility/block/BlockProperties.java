package bytesnek.snakerlib.utility.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Created by SnakerBone on 16/08/2023
 **/
@FunctionalInterface
public interface BlockProperties
{
    /**
     * Empty block properties instance
     **/
    BlockProperties EMPTY = colour -> BlockBehaviour.Properties.of();

    /**
     * Default properties for normal blocks
     **/
    BlockProperties NORMAL = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .sound(SoundType.STONE)
            .pushReaction(PushReaction.NORMAL);

    /**
     * Default properties for nether blocks
     **/
    BlockProperties NETHER = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .strength(0.5F)
            .sound(SoundType.NETHER_ORE);

    /**
     * Default properties for plant blocks
     **/
    BlockProperties PLANT = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .instabreak()
            .noCollission()
            .ignitedByLava();

    /**
     * Default properties for grass blocks
     **/
    BlockProperties GRASS = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .instabreak()
            .noCollission()
            .ignitedByLava()
            .replaceable();

    /**
     * Default properties for wood
     **/
    BlockProperties WOOD = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .sound(SoundType.WOOD)
            .pushReaction(PushReaction.NORMAL)
            .strength(2, 3)
            .ignitedByLava();

    /**
     * Default properties for blocks using special rendering
     **/
    BlockProperties PERSPECTIVE = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .sound(SoundType.STONE)
            .pushReaction(PushReaction.IGNORE)
            .strength(5, 8)
            .noOcclusion()
            .dynamicShape();

    /**
     * Default properties for blocks using cutout rendering
     **/
    BlockProperties CUTOUT = colour -> BlockBehaviour.Properties.of()
            .mapColor(colour)
            .strength(0.5F)
            .sound(SoundType.NETHER_ORE)
            .pushReaction(PushReaction.DESTROY)
            .isViewBlocking(BlockConditions.NEVER::apply)
            .noOcclusion();

    BlockProperties LEAVES = colour -> BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .strength(0.2F)
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .isSuffocating(BlockConditions.NEVER::apply)
            .isViewBlocking(BlockConditions.NEVER::apply)
            .isRedstoneConductor(BlockConditions.NEVER::apply)
            .randomTicks()
            .noOcclusion();

    BlockBehaviour.Properties apply(MapColor colour);
}
