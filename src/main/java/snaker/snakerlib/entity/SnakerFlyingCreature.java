package snaker.snakerlib.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.NotNull;
import snaker.snakerlib.entity.ai.LookAroundGoal;
import snaker.snakerlib.entity.ai.RandomWanderGoal;

import javax.annotation.Nullable;

/**
 * Created by SnakerBone on 2/01/2023
 **/
@SuppressWarnings("unused")
public class SnakerFlyingCreature extends Animal implements FlyingAnimal
{
    public SnakerFlyingCreature(EntityType<? extends Animal> type, Level world)
    {
        super(type, world);
        moveControl = new FlyingMoveControl(this, 20, true);
        setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16);
        setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1);
        setPathfindingMalus(BlockPathTypes.COCOA, -1);
        setPathfindingMalus(BlockPathTypes.WATER, -1);
        setPathfindingMalus(BlockPathTypes.FENCE, -1);
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(0, new RandomWanderGoal(this));
        goalSelector.addGoal(0, new LookAroundGoal(this));
        goalSelector.addGoal(7, new WaterAvoidingRandomFlyingGoal(this, 1));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state)
    {
        
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level world)
    {
        return new FlyingPathNavigation(this, world);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource source)
    {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos)
    {
        fallDistance = 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel server, @NotNull AgeableMob mate)
    {
        return this;
    }

    @Override
    public boolean isFlying()
    {
        return true;
    }
}
