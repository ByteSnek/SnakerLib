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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 31/08/2023
 **/
public class KillAllEntitiesCommand
{
    KillAllEntitiesCommand(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("killAllEntities")
                .requires(CommandConstants.require(CommandLevel.ADMIN))
                .executes(this::execute)
        );
    }

    public static KillAllEntitiesCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new KillAllEntitiesCommand(dispatcher);
    }

    private int execute(CommandContext<CommandSourceStack> context)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        Predicate<Entity> predicate = entity -> !(entity instanceof ServerPlayer);
        ServerPlayer player = context.getSource().getPlayer();

        if (player != null) {
            Level level = player.level();
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, Worlds.getWorldBoundingBox(player), predicate);

            boolean immune = false;

            for (Entity entity : entities) {
                if (!entity.isInvulnerable()) {
                    entity.kill();
                } else {
                    immune = true;
                }
            }

            if (!entities.isEmpty() && immune) {
                context.getSource().sendSuccess(success0(entities.size()), player.isCreative());
                return CommandConstants.getExecutionResult(source);
            } else if (!entities.isEmpty()) {
                context.getSource().sendSuccess(success(entities.size()), player.isCreative());
                return CommandConstants.getExecutionResult(source);
            } else {
                context.getSource().sendFailure(failure());
                return 0;
            }
        }

        return 0;
    }

    private Supplier<Component> success0(int size)
    {
        return () -> ChatComponents.info("command.snakerlib.kill_entity_success_0", size);
    }

    private Supplier<Component> success(int size)
    {
        return () -> ChatComponents.info("command.snakerlib.kill_entity_success", size);
    }

    private Component failure()
    {
        return ChatComponents.info("command.snakerlib.kill_entity_failure");
    }
}
