package snaker.snakerlib.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by SnakerBone on 4/01/2023
 **/
@SuppressWarnings("unused")
public class BossMob extends PathfinderMob
{
    private final BossEvent.BossBarColor colour;
    private final BossEvent.BossBarOverlay overlay;
    private final ServerBossEvent event = new ServerBossEvent(getDisplayName(), getColour(), getOverlay());

    public BossMob(EntityType<? extends PathfinderMob> type, Level level, BossEvent.BossBarOverlay overlay, BossEvent.BossBarColor colour, int xpReward)
    {
        super(type, level);
        this.xpReward = xpReward;
        this.colour = colour;
        this.overlay = overlay;
    }

    public BossMob(EntityType<? extends PathfinderMob> type, Level level, int xpReward)
    {
        this(type, level, BossEvent.BossBarOverlay.PROGRESS, BossEvent.BossBarColor.BLUE, xpReward);
    }

    public BossMob(EntityType<? extends PathfinderMob> type, Level level)
    {
        this(type, level, BossEvent.BossBarOverlay.PROGRESS, BossEvent.BossBarColor.BLUE, 8000);
    }

    public BossEvent.BossBarColor getColour()
    {
        return colour;
    }

    public BossEvent.BossBarOverlay getOverlay()
    {
        return overlay;
    }

    public void extraHealth(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addTransientModifier(new AttributeModifier("ExtraHealth", amount, operation));
    }

    public void extraAttackDamage(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).addTransientModifier(new AttributeModifier("ExtraAttackDamage", amount, operation));
    }

    public void extraAttackSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.ATTACK_SPEED)).addTransientModifier(new AttributeModifier("ExtraAttackSpeed", amount, operation));
    }

    public void extraMovementSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).addTransientModifier(new AttributeModifier("ExtraMovementSpeed", amount, operation));
    }

    public void extraFlyingSpeed(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.FLYING_SPEED)).addTransientModifier(new AttributeModifier("ExtraFlyingSpeed", amount, operation));
    }

    public void extraFollowRange(int amount, AttributeModifier.Operation operation)
    {
        Objects.requireNonNull(getAttribute(Attributes.FOLLOW_RANGE)).addTransientModifier(new AttributeModifier("ExtraFollowRange", amount, operation));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player)
    {
        super.startSeenByPlayer(player);
        event.setColor(getColour());
        event.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player)
    {
        super.stopSeenByPlayer(player);
        event.removePlayer(player);
    }

    @Override
    public void tick()
    {
        super.tick();
        event.setProgress(getHealth() / getMaxHealth());
    }

    @Override
    protected boolean shouldDespawnInPeaceful()
    {
        return true;
    }
}
