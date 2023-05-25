package snaker.snakerlib.level.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import snaker.snakerlib.math.Maths;

/**
 * Created by SnakerBone on 2/01/2023
 **/
@SuppressWarnings("unused")
public class FlyControl extends MoveControl
{
    private final Mob owner;
    private int duration;

    public FlyControl(Mob owner)
    {
        super(owner);
        this.owner = owner;
    }

    public void tick()
    {
        if (operation == Operation.MOVE_TO)
        {
            mob.setNoGravity(true);

            double x = wantedX - mob.getX();
            double y = wantedY - mob.getY();
            double z = wantedZ - mob.getZ();

            double xyz = x * x + y * y + z * z;

            if (xyz < 2.5000003E-7)
            {
                mob.setYya(0);
                mob.setZza(0);
                return;
            }

            float zx = Maths.atan2RotPos(z, x) - 90;
            float newSpeed;

            mob.setYRot(rotlerp(mob.getYRot(), zx, 90));

            if (mob.isOnGround())
            {
                newSpeed = (float) (speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            } else
            {
                newSpeed = (float) (speedModifier * mob.getAttributeValue(Attributes.FLYING_SPEED));
            }

            mob.setSpeed(newSpeed);

            double xz = Mth.sqrt((float) (x * x + z * z));
            float yxz = Maths.atan2RotNeg(y, xz);

            mob.setXRot(rotlerp(mob.getXRot(), yxz, (float) 20));
            mob.setYya(y > 0 ? newSpeed : -newSpeed);

            if (duration-- <= 0)
            {
                duration += owner.getRandom().nextInt(5) + 2;

                Vec3 wanted = new Vec3(wantedX - owner.getX(), wantedY - owner.getY(), wantedZ - owner.getZ());

                double length = wanted.length();

                wanted = wanted.normalize();

                if (canReach(wanted, Mth.ceil(length)))
                {
                    owner.setDeltaMovement(owner.getDeltaMovement().add(wanted.scale(0.1)));
                } else
                {
                    operation = Operation.WAIT;
                }
            }
        }
    }

    private boolean canReach(Vec3 position, int length)
    {
        AABB aabb = owner.getBoundingBox();

        for (int i = 1; i < length; ++i)
        {
            aabb = aabb.move(position);

            if (!owner.level.noCollision(owner, aabb))
            {
                return false;
            }
        }

        return true;
    }
}
