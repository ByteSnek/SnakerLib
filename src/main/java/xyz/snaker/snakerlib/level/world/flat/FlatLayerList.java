package xyz.snaker.snakerlib.level.world.flat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;

import com.google.common.collect.Lists;

/**
 * Created by SnakerBone on 18/10/2023
 * <p>
 * An immutable list of flat layers
 *
 * @see FlatLayer
 **/
public class FlatLayerList
{
    /**
     * The internal flat layer list
     **/
    private final List<FlatLayer> list;

    /**
     * Creates a new flat layer list
     *
     * @param listType The list type to use
     **/
    FlatLayerList(List<FlatLayer> listType)
    {
        this.list = listType;
    }

    /**
     * Creates a new flat layer list as an array list
     *
     * @return A new flat layer list as an array list
     **/
    public static FlatLayerList of()
    {
        return new FlatLayerList(Lists.newArrayList());
    }

    /**
     * Creates a new flat layer list with a definable type
     *
     * @param listType The type of list to use
     * @return A new flat layer list
     **/
    public static FlatLayerList of(List<FlatLayer> listType)
    {
        return new FlatLayerList(listType);
    }

    /**
     * Adds a flat layer to this list
     *
     * @param layer The flat layer to add to this list
     * @return True if the layer was added to this list
     **/
    public boolean add(FlatLayer layer)
    {
        return list.add(layer);
    }

    /**
     * Bulk adds flat layers to this list
     *
     * @param layers The flat layers to add to this list
     * @return True if all the layers were added to this list
     **/
    public boolean addAll(FlatLayer... layers)
    {
        return list.addAll(Arrays.asList(layers));
    }

    /**
     * Gets a flat layer from this list
     *
     * @param index The index of the flat layer to get
     * @return The flat layer
     **/
    public FlatLayer get(int index)
    {
        return list.get(index);
    }

    /**
     * Gets a flat layer's info from this list
     *
     * @param index The index of the flat layer to get
     * @return The flat layer's info
     **/
    public FlatLayerInfo getInfo(int index)
    {
        return list.get(index).getInfo();
    }

    /**
     * Gets the size of this list
     *
     * @return The size of this list
     **/
    public int size()
    {
        return list.size();
    }

    /**
     * Checks whether this list is empty
     *
     * @return True if this list is empty
     **/
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    /**
     * Gets the internal list as an {@link Collections#unmodifiableList(List)}
     *
     * @return This list as an unmodifiable list
     **/
    public List<FlatLayer> getList()
    {
        return Collections.unmodifiableList(list);
    }
}