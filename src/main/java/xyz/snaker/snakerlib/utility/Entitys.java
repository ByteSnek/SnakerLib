package xyz.snaker.snakerlib.utility;

import xyz.snaker.hiss.math.Maths;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by SnakerBone on 15/08/2023
 * <p>
 * Deliberately spelt incorrectly to avoid confliction
 **/
public class Entitys
{
    /**
     * Adds an effect to an entity
     *
     * @param entity     The entity to add the effect to
     * @param effect     The effect to add
     * @param duration   The duration of the effect in ticks
     * @param mul        The amplifier of the effect
     * @param isAmbient  Is the effect ambient
     * @param showBubble Should the effect show the bubble particles when active
     * @param showIcon   Should the icon be rendered on the screen (if present)
     * @return True if the effect was added
     **/
    public static <T extends LivingEntity, E extends MobEffect> boolean addEffectDirect(T entity, E effect, @Nullable Integer duration, @Nullable Integer mul, boolean isAmbient, boolean showBubble, boolean showIcon)
    {
        return entity.addEffect(new MobEffectInstance(effect, duration == null ? Maths.secondsToTicks(10) : duration, mul == null ? 0 : mul, isAmbient, showBubble, showIcon));
    }

    /**
     * Adds an effect to an entity
     *
     * @param entity   The entity to add the effect to
     * @param effect   The effect to add
     * @param duration The duration of the effect in ticks
     * @param mul      The amplifier of the effect
     * @return True if the effect was added
     **/
    public static <T extends LivingEntity, E extends MobEffect> boolean addEffectDirect(T entity, E effect, @Nullable Integer duration, @Nullable Integer mul)
    {
        return addEffectDirect(entity, effect, duration, mul, false, false, false);
    }

    /**
     * Adds an effect to an entity
     *
     * @param entity The entity to add the effect to
     * @param effect The effect to add
     * @return True if the effect was added
     **/
    public static <T extends LivingEntity, E extends MobEffect> boolean addEffectDirect(T entity, E effect)
    {
        return addEffectDirect(entity, effect, null, null);
    }

    /**
     * Checks if an entity is rotating
     *
     * @param entity The entity
     * @return True if the entity is rotating on any axis
     **/
    public static <T extends LivingEntity> boolean isEntityRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO || entity.yBodyRot != entity.yBodyRotO || entity.yHeadRot != entity.yHeadRotO;
    }

    /**
     * Checks if an entity is rotating
     *
     * @param entity The entity
     * @return True if the entity is rotating on the Y axis
     **/
    public static <T extends LivingEntity> boolean isEntityYRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO;
    }

    /**
     * Checks if an entity is rotating
     *
     * @param entity The entity
     * @return True if the entity is rotating on the Y axis
     **/
    public static <T extends LivingEntity> boolean isEntityYBodyRotating(@NotNull T entity)
    {
        return entity.yBodyRot != entity.yBodyRotO;
    }

    /**
     * Checks if an entity is rotating
     *
     * @param entity The entity
     * @return True if the entity is rotating on the Y axis
     **/
    public static <T extends LivingEntity> boolean isEntityYHeadRotating(@NotNull T entity)
    {
        return entity.yHeadRot != entity.yHeadRotO;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in any direction
     **/
    public static <T extends Entity> boolean isEntityMoving(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the X direction
     **/
    public static <T extends Entity> boolean isEntityMovingX(@NotNull T entity)
    {
        return entity.getX() != entity.xo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the Y direction
     **/
    public static <T extends Entity> boolean isEntityMovingY(@NotNull T entity)
    {
        return entity.getY() != entity.yo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the Z direction
     **/
    public static <T extends Entity> boolean isEntityMovingZ(@NotNull T entity)
    {
        return entity.getZ() != entity.zo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the X and Z direction
     **/
    public static <T extends Entity> boolean isEntityMovingXZ(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getZ() != entity.zo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the X and Y direction
     **/
    public static <T extends Entity> boolean isEntityMovingXY(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo;
    }

    /**
     * Checks if an entity is moving
     *
     * @param entity The entity
     * @return True if the entity is moving in the Y and Z direction
     **/
    public static <T extends Entity> boolean isEntityMovingYZ(@NotNull T entity)
    {
        return entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    /**
     * Checks if an entity has a specific goal or not regardless of whether it is currently executing or not
     *
     * @param entity    The entity
     * @param goalClass The class of the goal to check
     * @return True if the entity has the specified goal
     **/
    public static <T extends Mob> boolean hasGoal(@NotNull T entity, Class<? extends Goal> goalClass)
    {
        return entity.goalSelector.getAvailableGoals()
                .stream()
                .map(WrappedGoal::getGoal)
                .map(Object::getClass)
                .toList()
                .contains(goalClass);
    }

    /**
     * Checks if an entity is currently executing a specific goal
     *
     * @param entity    The entity
     * @param goalClass The class of the goal to check
     * @return True if the entity is currently executing the specified goal
     **/
    public static <T extends Mob> boolean hasActiveGoal(@NotNull T entity, Class<? extends Goal> goalClass)
    {
        return entity.goalSelector.getRunningGoals()
                .map(WrappedGoal::getGoal)
                .map(Object::getClass)
                .toList()
                .contains(goalClass);
    }
}
