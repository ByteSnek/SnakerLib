package xyz.snaker.snakerlib.level.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

/**
 * Created by SnakerBone on 10/11/2023
 **/
public class FollowSpecificMobGoal extends Goal
{
    /**
     * The mob using this goal
     **/
    private final Mob owner;

    /**
     * The filter to prevent the owner from following the same entities as them
     **/
    private final Predicate<Mob> followFilter;

    /**
     * The class of the mob to follow
     **/
    private final Class<? extends Mob> toFollowClass;

    /**
     * The mob the owner is following
     **/
    private Mob followingMob;

    /**
     * The owners path navigation
     **/
    private final PathNavigation navigation;

    /**
     * The owners movement speed modifier
     **/
    private final double speedModifier;

    /**
     * The time for the owner to recalculate its path finding when following the mob
     **/
    private int timeToRecalculatePath;

    /**
     * The space for the owner to give in between itself and the mob while following them
     **/
    private final double stopDistance;

    /**
     * The owners path finding malus value for water
     **/
    private float oldWaterCost;

    /**
     * The aabb range for the owner to search for mobs to follow
     **/
    private final float areaSize;

    /**
     * Constructs a new FollowSpecificMobGoal instance
     *
     * @param owner         The owner of this goal
     * @param toFollowClass The class of the mob to follow
     * @param speedModifier The speed multiplier to give to the owner while using this goal
     * @param stopDistance  The space for the owner to give in between itself and the mob while following them
     * @param areaSize      The aabb range for the owner to search for mobs to follow
     * @throws IllegalArgumentException If the mob does not have a valid {@link PathNavigation}
     **/
    public FollowSpecificMobGoal(Mob owner, Class<? extends Mob> toFollowClass, double speedModifier, double stopDistance, float areaSize)
    {
        this.owner = owner;
        this.toFollowClass = toFollowClass;
        this.followFilter = mob -> mob != null && owner.getClass() != mob.getClass();
        this.speedModifier = speedModifier;
        this.navigation = owner.getNavigation();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.checkIfNavigationValid(owner);
    }

    /**
     * Checks if the navigation for this mob is valid
     *
     * @param mob The mob to check
     **/
    private void checkIfNavigationValid(Mob mob)
    {
        PathNavigation navigation = mob.getNavigation();

        boolean isGroundNavigation = navigation instanceof GroundPathNavigation;
        boolean isFlyingNavigation = navigation instanceof FlyingPathNavigation;

        if (!isGroundNavigation && !isFlyingNavigation) {
            throw new IllegalArgumentException("Unsupported mob type");
        }
    }

    /**
     * Checks if the owner can currently use this goal
     *
     * @return True if the owner can currently use this goal
     **/
    @Override
    public boolean canUse()
    {
        List<? extends Mob> mobs = owner.level().getEntitiesOfClass(toFollowClass, owner.getBoundingBox().inflate(areaSize), followFilter);

        if (!mobs.isEmpty()) {
            for (Mob mob : mobs) {
                if (!mob.isInvisible()) {
                    followingMob = mob;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the owner can currently use this goal while it is being used
     *
     * @return True if the owner can currently use this goal while it is being used
     **/
    @Override
    public boolean canContinueToUse()
    {
        return followingMob != null && !navigation.isDone() && owner.distanceToSqr(followingMob) > (stopDistance * stopDistance);
    }

    /**
     * Tasks to do when this goal starts being used
     **/
    @Override
    public void start()
    {
        timeToRecalculatePath = 0;
        oldWaterCost = owner.getPathfindingMalus(BlockPathTypes.WATER);
        owner.setPathfindingMalus(BlockPathTypes.WATER, 0);
    }

    /**
     * Tasks to do when this goal stops being used
     **/
    @Override
    public void stop()
    {
        followingMob = null;
        navigation.stop();
        owner.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
    }

    /**
     * Tasks to do every tick while this goal is being used
     **/
    @Override
    public void tick()
    {
        if (followingMob != null && !owner.isLeashed()) {
            owner.getLookControl().setLookAt(followingMob, 10, (float) owner.getMaxHeadXRot());

            if (timeToRecalculatePath-- <= 0) {
                timeToRecalculatePath = adjustedTickDelay(10);

                double distX = owner.getX() - followingMob.getX();
                double distY = owner.getY() - followingMob.getY();
                double distZ = owner.getZ() - followingMob.getZ();

                double distXYZ = distX * distX + distY * distY + distZ * distZ;

                if (!(distXYZ <= (stopDistance * stopDistance))) {
                    navigation.moveTo(followingMob, speedModifier);
                } else {
                    navigation.stop();
                    LookControl lookcontrol = followingMob.getLookControl();

                    if (distXYZ <= stopDistance || lookcontrol.getWantedX() == owner.getX() && lookcontrol.getWantedY() == owner.getY() && lookcontrol.getWantedZ() == owner.getZ()) {
                        double followerDistX = followingMob.getX() - owner.getX();
                        double followerDistZ = followingMob.getZ() - owner.getZ();

                        navigation.moveTo(owner.getX() - followerDistX, owner.getY(), owner.getZ() - followerDistZ, speedModifier);
                    }
                }
            }
        }
    }
}
