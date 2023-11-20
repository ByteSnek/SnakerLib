package bytesnek.snakerlib.command;

import bytesnek.snakerlib.SnakerLib;
import bytesnek.snakerlib.config.SnakerConfig;
import bytesnek.snakerlib.chat.ChatComponents;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 18/10/2023
 **/
public class ConfigCommand
{
    ConfigCommand(CommandDispatcher<CommandSourceStack> dispatcher, String name)
    {
        dispatcher.register(Commands.literal(name)
                .then(Commands.literal("config")
                        .then(Commands.literal("forceCrashJvmKeyBindings")
                                .then(Commands.argument("forceCrashJvmKeyBindings", BoolArgumentType.bool())
                                        .executes(context -> setForceCrashJvmKeyBindings(context,
                                                BoolArgumentType.getBool(context, "forceCrashJvmKeyBindings"))
                                        )
                                )
                        )
                        .then(Commands.literal("removeJvmCrashFilesOnStartup")
                                .then(Commands.argument("removeJvmCrashFilesOnStartup", BoolArgumentType.bool())
                                        .executes(context -> setRemoveJvmCrashFilesOnStartup(context,
                                                BoolArgumentType.getBool(context, "removeJvmCrashFilesOnStartup"))
                                        )
                                )
                        )
                        .then(Commands.literal("removeMinecraftCrashFilesOnStartup")
                                .then(Commands.argument("removeMinecraftCrashFilesOnStartup", BoolArgumentType.bool())
                                        .executes(context -> setRemoveMinecraftCrashFilesOnStartup(context,
                                                BoolArgumentType.getBool(context, "removeMinecraftCrashFilesOnStartup"))
                                        )
                                )
                        )
                        .then(Commands.literal("goodbyeWorldKeyBindings")
                                .then(Commands.argument("goodbyeWorldKeyBindings", BoolArgumentType.bool())
                                        .executes(context -> setForceCrashOperatingSystemBindings(context,
                                                BoolArgumentType.getBool(context, "goodbyeWorldKeyBindings"))
                                        )
                                )
                        )
                )
        );
    }

    public static ConfigCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new ConfigCommand(dispatcher, SnakerLib.MODID);
    }

    private int setForceCrashJvmKeyBindings(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.forceCrashJvmKeyBindings.set(value);
        SnakerConfig.COMMON.forceCrashJvmKeyBindings.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    private int setRemoveJvmCrashFilesOnStartup(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.removeJvmCrashFilesOnStartup.set(value);
        SnakerConfig.COMMON.removeJvmCrashFilesOnStartup.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    private int setRemoveMinecraftCrashFilesOnStartup(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.removeMinecraftCrashFilesOnStartup.set(value);
        SnakerConfig.COMMON.removeMinecraftCrashFilesOnStartup.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    private int setForceCrashOperatingSystemBindings(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.goodbyeWorldKeyBindings.set(value);
        SnakerConfig.COMMON.goodbyeWorldKeyBindings.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    private Component success()
    {
        return ChatComponents.info("snakerlib.commands.config_set_success");
    }
}
