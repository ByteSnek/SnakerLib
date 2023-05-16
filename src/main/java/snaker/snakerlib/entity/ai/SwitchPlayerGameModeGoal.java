package snaker.snakerlib.entity.ai;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Created by SnakerBone on 28/02/2023
 **/
@SuppressWarnings("unused")
public class SwitchPlayerGameModeGoal extends Goal
{
    private final Mob mob;

    public SwitchPlayerGameModeGoal(Mob owner)
    {
        mob = owner;
    }

    @Override
    public void tick()
    {
        Level world = mob.level;
        List<ServerPlayer> players = world.getEntitiesOfClass(ServerPlayer.class, mob.getBoundingBox().inflate(8));
        if (players.stream().anyMatch(ServerPlayer::isCreative))
        {
            for (ServerPlayer player : players)
            {
                player.setGameMode(GameType.SURVIVAL);
                mob.setTarget(player);
            }
        }
    }

    @Override
    public boolean canUse()
    {
        return mob.getLastHurtByMob() instanceof ServerPlayer;
    }
}
