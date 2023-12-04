package xyz.snaker.snakerlib.utility.item;

import net.minecraft.world.item.Item;

/**
 * Created by SnakerBone on 16/08/2023
 **/
public interface ItemProperties
{
    /**
     * Empty item properties instance
     **/
    Item.Properties EMPTY = new Item.Properties();

    /**
     * Default properties for a tool item
     **/
    Item.Properties TOOL = new Item.Properties().stacksTo(1);

    /**
     * Default properties for a special tool item (e.g. ender pearl)
     **/
    Item.Properties SPECIAL = new Item.Properties().stacksTo(1).setNoRepair();
}
