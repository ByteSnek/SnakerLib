package bytesnek.snakerlib.command;

import java.util.function.Predicate;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * Created by SnakerBone on 25/11/2023
 **/
public class CommandConstants
{
    public static Predicate<CommandSourceStack> require(CommandLevel level)
    {
        return stack -> stack.hasPermission(level.getValue());
    }

    public static boolean dev(CommandSourceStack stack)
    {
        return stack.getPlayer() != null && stack.hasPermission(Commands.LEVEL_ADMINS) && (stack.getPlayer().getName().getString().equals("Dev") || !FMLEnvironment.production);
    }

    public static int getExecutionResult(CommandSource source)
    {
        return source.acceptsSuccess() ? 1 : 0;
    }
}
