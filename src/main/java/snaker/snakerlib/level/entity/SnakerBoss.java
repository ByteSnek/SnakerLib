package snaker.snakerlib.level.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import snaker.snakerlib.data.SnakerConstants;
import snaker.snakerlib.level.entity.ai.SnakerFlyControl;
import snaker.snakerlib.level.entity.ai.SnakerSwitchGameModeGoal;

import java.util.Objects;
import java.util.UUID;

/**
 * Created by SnakerBone on 4/01/2023
 **/
@SuppressWarnings("unused")
public abstract class SnakerBoss extends PathfinderMob
{
    public SnakerBoss(EntityType<? extends PathfinderMob> type, Level level, int xpReward)
    {
        super(type, level);
        this.xpReward = xpReward;
        this.moveControl = new SnakerFlyControl(this);
    }

    public SnakerBoss(EntityType<? extends PathfinderMob> type, Level level)
    {
        this(type, level, SnakerConstants.BOSS_XP_REWARD.asInt());
    }

    public abstract ServerBossEvent getBossInfo();

    public abstract UUID getBossUUID();

    public abstract BossEvent.BossBarColor getBarColour();

    public void extraHealth(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addTransientModifier(new AttributeModifier("ExtraHealth", amount, operation));
    }

    @Override
    protected void registerGoals()
    {
        goalSelector.addGoal(1, new SnakerSwitchGameModeGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distance)
    {
        return false;
    }

    @Override
    public boolean shouldDespawnInPeaceful()
    {
        return true;
    }
}
