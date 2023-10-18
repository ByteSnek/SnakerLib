package xyz.snaker.snakerlib.command;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.config.SnakerConfig;

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
                        .then(Commands.literal("playerVulnerableInCreative")
                                .then(Commands.argument("playerVulnerableInCreative", BoolArgumentType.bool())
                                        .executes(context -> setPlayerVulnerableInCreative(context,
                                                BoolArgumentType.getBool(context, "playerVulnerableInCreative"))
                                        )
                                )
                        )
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
                )
        );
    }

    public static ConfigCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new ConfigCommand(dispatcher, SnakerLib.MODID);
    }

    /**
     * Discards all entities excluding players
     *
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int setPlayerVulnerableInCreative(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.playerVulnerableInCreative.set(value);
        SnakerConfig.COMMON.playerVulnerableInCreative.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    /**
     * Discards all entities excluding players
     *
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int setForceCrashJvmKeyBindings(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.forceCrashJvmKeyBindings.set(value);
        SnakerConfig.COMMON.forceCrashJvmKeyBindings.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    /**
     * Discards all entities excluding players
     *
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int setRemoveJvmCrashFilesOnStartup(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.removeJvmCrashFilesOnStartup.set(value);
        SnakerConfig.COMMON.removeJvmCrashFilesOnStartup.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    /**
     * Discards all entities excluding players
     *
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int setRemoveMinecraftCrashFilesOnStartup(CommandContext<CommandSourceStack> context, boolean value)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        SnakerConfig.COMMON.removeMinecraftCrashFilesOnStartup.set(value);
        SnakerConfig.COMMON.removeMinecraftCrashFilesOnStartup.save();

        stack.sendSuccess(this::success, true);

        return source.acceptsSuccess() ? 1 : 0;
    }

    private Component success()
    {
        return Component.translatable("commands.snakerlib.config_set_success");
    }
}
