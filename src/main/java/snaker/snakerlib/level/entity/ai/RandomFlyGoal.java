package snaker.snakerlib.level.entity.ai;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Created by SnakerBone on 2/01/2023
 **/
@SuppressWarnings("unused")
public class RandomFlyGoal extends Goal
{
    private final Mob owner;

    public RandomFlyGoal(Mob owner)
    {
        this.owner = owner;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse()
    {
        MoveControl controller = owner.getMoveControl();
        if (!controller.hasWanted())
        {
            return true;
        } else
        {
            double x = controller.getWantedX() - owner.getX();
            double y = controller.getWantedY() - owner.getY();
            double z = controller.getWantedZ() - owner.getZ();

            double xyz = x * x + y * y + z * z;

            return xyz < 1 || xyz > 3600;
        }
    }

    public boolean canContinueToUse()
    {
        return false;
    }

    public void start()
    {
        RandomSource random = owner.getRandom();

        double x = owner.getX() + (double) ((random.nextFloat() * 2 - 1) * 16);
        double y = owner.getY() + (double) ((random.nextFloat() * 2 - 1) * 16);
        double z = owner.getZ() + (double) ((random.nextFloat() * 2 - 1) * 16);

        owner.getMoveControl().setWantedPosition(x, y, z, 1);
    }
}
