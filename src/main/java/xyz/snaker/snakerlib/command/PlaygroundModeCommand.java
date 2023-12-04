package xyz.snaker.snakerlib.command;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import xyz.snaker.snakerlib.chat.ChatComponents;
import xyz.snaker.snakerlib.utility.Worlds;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WorldData;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 22/08/2023
 **/
public class PlaygroundModeCommand
{
    PlaygroundModeCommand(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("playgroundMode")
                .requires(CommandConstants.require(CommandLevel.ADMIN))
                .then(Commands.argument("playgroundMode", BoolArgumentType.bool())
                        .executes(context -> execute(context, BoolArgumentType.getBool(context, "playgroundMode")))
                )
        );
    }

    public static PlaygroundModeCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new PlaygroundModeCommand(dispatcher);
    }

    private int execute(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        Predicate<Entity> predicate = entity -> !(entity instanceof ServerPlayer);
        ServerPlayer player = context.getSource().getPlayer();

        if (player != null) {
            MinecraftServer server = player.getServer();
            Level level = player.level();

            if (server != null) {
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, Worlds.getWorldBoundingBox(player), predicate);

                for (Entity entity : entities) {
                    entity.discard();
                }

                setPlaygroundMode(server, value);

                if (!player.getPersistentData().contains("PlaygroundMode")) {
                    player.getPersistentData().putBoolean("PlaygroundMode", value);
                }

                context.getSource().sendSuccess(success(value), true);

                return CommandConstants.getExecutionResult(source);
            }
        }

        return 0;
    }

    private void setPlaygroundMode(MinecraftServer server, boolean value)
    {
        WorldData worldData = server.getWorldData();
        ServerLevelData overworldData = worldData.overworldData();

        worldData.setDifficulty(value ? Difficulty.PEACEFUL : Difficulty.NORMAL);
        worldData.setDifficultyLocked(value);

        overworldData.setRaining(!value);
        overworldData.setThundering(!value);
        overworldData.setClearWeatherTime(value ? 1000000000 : overworldData.getClearWeatherTime());
        overworldData.setDayTime(value ? 6000 : overworldData.getDayTime());
        overworldData.setGameType(value ? GameType.CREATIVE : overworldData.getGameType());
    }

    private Supplier<Component> success(boolean value)
    {
        return () -> ChatComponents.info("command.snakerlib.playground_mode_status", value ? ChatComponents.ENABLED.getString() : ChatComponents.DISABLED.getString());
    }
}
