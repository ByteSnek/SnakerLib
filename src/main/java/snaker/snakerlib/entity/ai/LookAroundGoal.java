package snaker.snakerlib.entity.ai;

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
    private final Mob mob;

    public LookAroundGoal(Mob owner)
    {
        mob = owner;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    public boolean canUse()
    {
        return true;
    }

    public void tick()
    {
        if (mob.getTarget() == null)
        {
            Vec3 movement = mob.getDeltaMovement();
            mob.setXRot(Maths.rotateTowards(movement.x, movement.z));
            mob.xRotO = mob.getXRot();
        } else
        {
            LivingEntity target = mob.getTarget();
            if (target.distanceToSqr(mob) < 4096)
            {
                double x = target.getX() - mob.getX();
                double z = target.getZ() - mob.getZ();
                mob.setXRot(Maths.rotateTowards(x, z));
                mob.xRotO = mob.getXRot();
            }
        }
    }
}
