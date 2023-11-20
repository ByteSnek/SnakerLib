package bytesnek.snakerlib.level.levelgen.flat;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;

/**
 * Created by SnakerBone on 18/10/2023
 * <p>
 * A simplified {@link FlatLayerInfo} alternative
 *
 * @see FlatLayerList
 **/
public class FlatLayer
{
    /**
     * The amount of layers to add
     **/
    private final int height;

    /**
     * The block to use for the layer
     **/
    private final Block block;

    /**
     * Creates a new flat layer
     *
     * @param height The amount of layers this layer will contain
     * @param block  The block to use for the layer
     **/
    FlatLayer(int height, Block block)
    {
        this.height = height;
        this.block = block;
    }

    /**
     * Creates a new flat layer
     *
     * @param height The amount of layers this layer will contain
     * @param block  The block to use for the layer
     * @return A new flat layer
     **/
    public static FlatLayer of(int height, Block block)
    {
        return new FlatLayer(height, block);
    }

    /**
     * Creates a new flat layer with the height of 1 block
     *
     * @param block The block to use for the layer
     * @return A new flat layer
     **/
    public static FlatLayer of(Block block)
    {
        return new FlatLayer(1, block);
    }

    /**
     * Creates a new flat layer info directly
     *
     * @param height The amount of layers this layer will contain
     * @param block  The block to use for the layer
     * @return A new flat layer info
     **/
    public static FlatLayerInfo direct(int height, Block block)
    {
        return new FlatLayer(height, block).getInfo();
    }

    /**
     * Creates a new flat layer info directly with the height of 1
     *
     * @param block The block to use for the layer
     * @return A new flat layer info
     **/
    public static FlatLayerInfo direct(Block block)
    {
        return new FlatLayer(1, block).getInfo();
    }

    /**
     * Gets the flat layer info for this flat layer
     *
     * @return The flat layer info for this flat layer
     **/
    public FlatLayerInfo getInfo()
    {
        return new FlatLayerInfo(height, block);
    }
}
