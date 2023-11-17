package xyz.snaker.snakerlib.command;

import xyz.snaker.snakerlib.SnakerLib;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 17/10/2023
 **/
public class ForceRemoveCommand
{
    private final Component success = Component.translatable("command.snakerlib.force_removal_success");

    ForceRemoveCommand(CommandDispatcher<CommandSourceStack> dispatcher, String name)
    {
        dispatcher.register(Commands.literal(name)
                .then(Commands.literal("forceRemove")
                        .executes(this::execute)
                )
        );
    }

    public static ForceRemoveCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new ForceRemoveCommand(dispatcher, SnakerLib.MODID);
    }

    private int execute(CommandContext<CommandSourceStack> context)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        ServerPlayer player = stack.getPlayer();

        if (player != null) {
            player.discard();
            player.connection.disconnect(success);
        }

        return source.acceptsSuccess() ? 1 : 0;
    }
}
