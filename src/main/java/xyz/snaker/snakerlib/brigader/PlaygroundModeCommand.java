package xyz.snaker.snakerlib.brigader;

import java.util.List;
import java.util.function.Predicate;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.tools.WorldStuff;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 22/08/2023
 **/
@SuppressWarnings("JavadocReference")
public class PlaygroundModeCommand
{
    private boolean enabled;

    PlaygroundModeCommand(CommandDispatcher<CommandSourceStack> dispatcher, String name, String arg)
    {
        dispatcher.register(Commands.literal(name)
                .then(Commands.literal(arg)
                        .executes(this::execute)));
    }

    public static PlaygroundModeCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new PlaygroundModeCommand(dispatcher, SnakerLib.MODID, "PlaygroundMode");
    }

    /**
     * Discard all entities excluding players, toggles daylight cycle, toggles mob spawning and toggles weather cycle
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int execute(CommandContext<CommandSourceStack> context)
    {
        Predicate<Entity> predicate = entity -> !(entity instanceof ServerPlayer);
        ServerPlayer player = context.getSource().getPlayer();

        if (player != null) {
            MinecraftServer server = player.getServer();
            Level level = player.level();

            if (server != null) {
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, WorldStuff.getWorldBoundingBox(player), predicate);

                for (Entity entity : entities) {
                    entity.discard();
                }

                enabled = !enabled;
                boolean active = !enabled;

                setupPlaygroundMode(server);

                if (!player.getPersistentData().contains("PlaygroundMode")) {
                    player.getPersistentData().putBoolean("PlaygroundMode", active);
                }

                context.getSource().sendSuccess(() -> Component.literal(String.format("%s Playground Mode", enabled ? "Enabled" : "Disabled")), true);

                return 1;
            }
        }

        return 0;
    }

    /**
     * Modified level data for debugging found in the MinecraftServer class
     * @param server The Minecraft Server
     * @see MinecraftServer#setupDebugLevel(WorldData)
     **/
    private void setupPlaygroundMode(MinecraftServer server)
    {
        WorldData worldData = server.getWorldData();
        ServerLevelData overworldData = worldData.overworldData();

        worldData.setDifficulty(Difficulty.PEACEFUL);
        worldData.setDifficultyLocked(true);

        overworldData.setRaining(false);
        overworldData.setThundering(false);
        overworldData.setClearWeatherTime(1000000000);
        overworldData.setDayTime(6000);
        overworldData.setGameType(GameType.CREATIVE);
    }
}
