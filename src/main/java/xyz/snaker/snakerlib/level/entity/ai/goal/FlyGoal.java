package xyz.snaker.snakerlib.level.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Created by SnakerBone on 2/01/2023
 **/
public class FlyGoal extends Goal
{
    /**
     * The mob using this goal
     **/
    private final Mob owner;

    /**
     * Constructs a new FlyGoal instance
     *
     * @param owner The owner of this goal
     **/
    public FlyGoal(Mob owner)
    {
        this.owner = owner;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    /**
     * Checks if the owner can currently use this goal
     *
     * @return True if the owner can currently use this goal
     **/
    @Override
    public boolean canUse()
    {
        MoveControl control = owner.getMoveControl();

        if (!control.hasWanted()) {
            return true;
        } else {
            double x = control.getWantedX() - owner.getX();
            double y = control.getWantedY() - owner.getY();
            double z = control.getWantedZ() - owner.getZ();

            double xyz = x * x + y * y + z * z;

            return xyz < 1 || xyz > 3600;
        }
    }

    /**
     * Checks if the owner can currently use this goal while it is being used
     *
     * @return True if the owner can currently use this goal while it is being used
     **/
    @Override
    public boolean canContinueToUse()
    {
        return false;
    }

    /**
     * Tasks to do when this goal starts being used
     **/
    @Override
    public void start()
    {
        RandomSource random = owner.getRandom();

        double x = owner.getX() + ((random.nextFloat() * 2 - 1) * 16);
        double y = owner.getY() + ((random.nextFloat() * 2 - 1) * 16);
        double z = owner.getZ() + ((random.nextFloat() * 2 - 1) * 16);

        owner.getMoveControl().setWantedPosition(x, y, z, 1);
    }
}
