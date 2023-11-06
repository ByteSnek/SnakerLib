package xyz.snaker.snakerlib.utility;

import net.minecraft.world.item.Item;

/**
 * Created by SnakerBone on 16/08/2023
 **/
@FunctionalInterface
public interface ItemProperties
{
    /**
     * Empty item properties instance
     **/
    ItemProperties EMPTY = (durability) -> new Item.Properties().defaultDurability(durability);

    /**
     * Default properties for a tool item
     **/
    ItemProperties TOOL = (durability) -> new Item.Properties().defaultDurability(durability).stacksTo(1);

    /**
     * Default properties for a special tool item (e.g. ender pearl)
     **/
    ItemProperties SPECIAL = (durability) -> new Item.Properties().defaultDurability(durability).stacksTo(1).setNoRepair();

    Item.Properties apply(int durability);
}
