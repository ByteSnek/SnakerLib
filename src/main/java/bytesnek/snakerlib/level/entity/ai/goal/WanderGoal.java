package bytesnek.snakerlib.level.entity.ai.goal;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;

import bytesnek.hiss.math.Maths;

/**
 * Created by SnakerBone on 21/02/2023
 **/
public class WanderGoal extends Goal
{
    /**
     * The animal using this goal
     **/
    private final Animal owner;

    /**
     * Constructs a new WanderGoal instance
     *
     * @param owner The owner of this goal
     **/
    public WanderGoal(Animal owner)
    {
        this.owner = owner;
    }

    @Override
    public boolean canUse()
    {
        return owner.getNavigation().isDone() && owner.getRandom().nextInt(10) == 0;
    }

    /**
     * Checks if the owner can currently use this goal while it is being used
     *
     * @return True if the owner can currently use this goal while it is being used
     **/
    @Override
    public boolean canContinueToUse()
    {
        return owner.getNavigation().isInProgress();
    }

    /**
     * Tasks to do when this goal starts being used
     **/
    @Override
    public void start()
    {
        Vec3 pos = this.findPos();

        if (pos != null) {
            owner.getNavigation().moveTo(owner.getNavigation().createPath(BlockPos.containing(pos), 1), 1.0D);
        }
    }

    /**
     * Finds a random position around this animal
     *
     * @return The position as a Vec3 or null if there is none
     **/
    @Nullable
    private Vec3 findPos()
    {
        Vec3 view = owner.getViewVector(0);
        Vec3 pos = HoverRandomPos.getPos(owner, 8, 7, view.x, view.z, Maths.HALF_PI, 3, 1);

        return pos != null ? pos : AirAndWaterRandomPos.getPos(owner, 8, 4, -2, view.x, view.z, Maths.HALF_PI);
    }
}
