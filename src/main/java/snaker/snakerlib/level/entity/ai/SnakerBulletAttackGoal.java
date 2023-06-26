package snaker.snakerlib.level.entity.ai;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import snaker.snakerlib.math.Mh;

/**
 * Created by SnakerBone on 2/01/2023
 **/
public class SnakerBulletAttackGoal extends Goal
{
    private final Mob owner;
    private int delay;
    private float velocity;
    private float inaccuracy;

    public SnakerBulletAttackGoal(Mob owner, int delay, float velocity, float inaccuracy)
    {
        this.owner = owner;
        this.delay = delay;
        this.velocity = velocity;
        this.inaccuracy = inaccuracy;
    }

    public boolean canUse()
    {
        return owner.getTarget() != null;
    }

    public boolean requiresUpdateEveryTick()
    {
        return true;
    }

    public void tick()
    {
        LivingEntity target = this.owner.getTarget();
        RandomSource random = RandomSource.create();

        if (target != null) {
            owner.getLookControl().setLookAt(target, 10, owner.getMaxHeadXRot());

            if (target.distanceToSqr(owner) < 4096 && owner.hasLineOfSight(target)) {
                Level level = owner.level();

                double x = target.getX() - owner.getX();
                double y = target.getY() - owner.getY();
                double z = target.getZ() - owner.getZ();

                owner.setXRot(Mh.atan2RotNeg(y, (x * x + z * z)));
                owner.xRotO = owner.getXRot();

                //if (owner.tickCount % delay == 0) {
                //Bullet bullet = new Bullet(ContentRegistry.ENTITY_BULLET.get(), owner, level);
                //Vec3D xyz = new Vec3D(x, y, z);

                //bullet.shoot(xyz.x, xyz.y, xyz.z, velocity, inaccuracy);
                //level.addFreshEntity(bullet);
                //level.playSound(null, target.getX(), target.getY(), target.getZ(), ContentRegistry.SOUND_PEW.get(), SoundSource.BLOCKS, 0.5F, (random.nextFloat() - random.nextFloat()) * 0.5F + 1);
                //}
            }
        }
    }
}