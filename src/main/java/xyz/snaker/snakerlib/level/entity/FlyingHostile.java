package xyz.snaker.snakerlib.level.entity;

import xyz.snaker.snakerlib.level.entity.ai.control.FlyController;
import xyz.snaker.snakerlib.level.entity.ai.goal.FlyGoal;
import xyz.snaker.snakerlib.level.entity.ai.goal.LookAroundGoal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 2/01/2023
 **/
public abstract class FlyingHostile extends FlyingMob implements Enemy
{
    public FlyingHostile(EntityType<? extends FlyingHostile> type, Level level, int xpReward)
    {
        super(type, level);
        this.xpReward = xpReward;
        this.moveControl = new FlyController(this);
    }

    public FlyingHostile(EntityType<? extends FlyingHostile> type, Level level)
    {
        this(type, level, 15);
    }

    /**
     * Checks if this mob is actually aggressive
     *
     * @return True if this mob is actually aggressive
     **/
    public boolean isCranky()
    {
        return isAlive() && isEffectiveAi() && isAggressive() && getTarget() != null;
    }

    @Override
    public boolean shouldDespawnInPeaceful()
    {
        return true;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource source)
    {
        return false;
    }

    @Override
    public void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos)
    {
        fallDistance = 0;
    }

    public void registerGoals()
    {
        goalSelector.addGoal(4, new FlyGoal(this));
        goalSelector.addGoal(6, new LookAroundGoal(this));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
