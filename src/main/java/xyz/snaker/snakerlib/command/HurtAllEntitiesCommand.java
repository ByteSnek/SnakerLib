package xyz.snaker.snakerlib.command;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.tools.WorldStuff;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;

/**
 * Created by SnakerBone on 31/08/2023
 **/
public class HurtAllEntitiesCommand
{
    HurtAllEntitiesCommand(CommandDispatcher<CommandSourceStack> dispatcher, String name)
    {
        dispatcher.register(Commands.literal(name)
                .then(Commands.literal("hurtAllEntities")
                        .then(Commands.argument("DamageAmount", FloatArgumentType.floatArg())
                                .executes(context -> execute(context,
                                        FloatArgumentType.getFloat(context, "DamageAmount"))
                                )
                        )
                )
        );
    }

    public static HurtAllEntitiesCommand register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        return new HurtAllEntitiesCommand(dispatcher, SnakerLib.MODID);
    }

    /**
     * Hurts all entities excluding players
     *
     * @param context The command context
     * @return The execution result
     * <ul>
     *     <li><strong>1</strong> for <strong>SUCCESS</strong></li>
     *     <li><strong>0</strong> for <strong>FAILURE</strong></li>
     *     <li><strong>-1</strong> for <strong>ERROR</strong></li>
     * </ul>
     **/
    private int execute(CommandContext<CommandSourceStack> context, float amount)
    {
        Predicate<Entity> predicate = entity -> !(entity instanceof ServerPlayer);
        ServerPlayer player = context.getSource().getPlayer();

        if (player != null) {
            Level level = player.level();
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, WorldStuff.getWorldBoundingBox(player), predicate);

            boolean immune = false;

            for (Entity entity : entities) {
                if (!entity.isInvulnerable()) {
                    entity.hurt(level.damageSources().generic(), amount);
                } else {
                    immune = true;
                }
            }

            if (!entities.isEmpty() && immune) {
                context.getSource().sendSuccess(success0(entities.size(), amount), true);
                return 1;
            } else if (!entities.isEmpty()) {
                context.getSource().sendSuccess(success(entities.size(), amount), true);
                return 1;
            } else {
                context.getSource().sendFailure(failure());
                return 0;
            }
        }

        return 0;
    }

    private Supplier<Component> success0(int size, float amount)
    {
        return () -> Component.translatable("commands.snakerlib.hurt_entity_success_0", size, amount);
    }

    private Supplier<Component> success(int size, float amount)
    {
        return () -> Component.translatable("commands.snakerlib.hurt_entity_success", size, amount);
    }

    private Component failure()
    {
        return Component.translatable("commands.snakerlib.hurt_entity_failure");
    }
}
