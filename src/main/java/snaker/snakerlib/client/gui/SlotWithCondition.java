package snaker.snakerlib.client.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * Created by SnakerBone on 26/04/2023
 **/
@SuppressWarnings("unused")
public class SlotWithCondition extends SlotItemHandler
{
    private final Predicate<ItemStack> condition;

    public SlotWithCondition(IItemHandler handler, int index, int xPosition, int yPosition, Predicate<ItemStack> condition)
    {
        super(handler, index, xPosition, yPosition);
        this.condition = condition;
    }

    public SlotWithCondition(IItemHandler handler, int index, int xPosition, int yPosition)
    {
        this(handler, index, xPosition, yPosition, stack -> false);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        return condition.test(stack);
    }
}