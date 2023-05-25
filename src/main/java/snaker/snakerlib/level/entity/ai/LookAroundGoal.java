package snaker.snakerlib.level.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import snaker.snakerlib.math.Maths;

import java.util.EnumSet;

/**
 * Created by SnakerBone on 2/01/2023
 **/
@SuppressWarnings("unused")
public class LookAroundGoal extends Goal
{
    private final Mob owner;

    public LookAroundGoal(Mob owner)
    {
        this.owner = owner;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse()
    {
        return true;
    }

    public void tick()
    {
        if (owner.getTarget() == null)
        {
            Vec3 movement = owner.getDeltaMovement();

            owner.setXRot(Maths.atan2RotNeg(movement.x, movement.z));
            owner.xRotO = owner.getXRot();

        } else
        {
            LivingEntity target = owner.getTarget();

            if (target.distanceToSqr(owner) < 4096)
            {
                double x = target.getX() - owner.getX();
                double z = target.getZ() - owner.getZ();

                owner.setXRot(Maths.atan2RotNeg(x, z));
                owner.xRotO = owner.getXRot();
            }
        }
    }
}
