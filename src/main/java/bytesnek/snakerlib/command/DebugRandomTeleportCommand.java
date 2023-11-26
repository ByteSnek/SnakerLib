package bytesnek.snakerlib.command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.Tags;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import bytesnek.snakerlib.SnakerLib;
import bytesnek.snakerlib.utility.BlockPositions;
import oshi.util.Memoizer;

/**
 * Created by SnakerBone on 24/11/2023
 **/
public class DebugRandomTeleportCommand
{
    private static final RandomSource RANDOM = RandomSource.create();

    DebugRandomTeleportCommand(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(Commands.literal("debugRtp")
                .requires(CommandConstants::dev)
                .executes(context ->
                        execute(context, RANDOM.nextInt(100_000))
                )
                .then(Commands.argument("searchExtent", IntegerArgumentType.integer())
                        .requires(CommandConstants::dev)
                        .executes(context ->
                                execute(context,
                                        IntegerArgumentType.getInteger(context, "searchExtent")
                                )
                        )
                )
        );
    }

    public static DebugRandomTeleportCommand register(CommandDispatcher<CommandSourceStack> context)
    {
        return new DebugRandomTeleportCommand(context);
    }

    private int execute(CommandContext<CommandSourceStack> context, int searchExtent)
    {
        CommandSourceStack stack = context.getSource();
        CommandSource source = stack.source;

        ServerPlayer player = stack.getPlayer();
        ServerLevel level = stack.getLevel();

        if (player != null) {
            ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            service.submit(() ->
                    {
                        Supplier<BlockPos> memoizedPos = Memoizer.memoize(() ->
                        {
                            SnakerLib.DEVLOGGER.info("Starting RTP search");
                            System.gc();

                            return findPos(level, player, searchExtent, 0);
                        });

                        BlockPos pos = memoizedPos.get();

                        if (pos != player.getOnPos()) {
                            player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                        }
                    }
            );

            service.shutdown();
        }

        return CommandConstants.getExecutionResult(source);
    }

    private synchronized BlockPos findPos(ServerLevel level, ServerPlayer player, int searchExtent, int depth)
    {
        depth++;

        SnakerLib.DEVLOGGER.infof("Attempt []/75...", depth);

        if (depth >= 75) {
            SnakerLib.DEVLOGGER.warnf("Cancelling search. Method recurred too deeply");
            return player.getOnPos();
        }

        int x = RANDOM.nextInt(searchExtent);
        int y = depth >= 5 ? 63 : player.getOnPos().getY();
        int z = RANDOM.nextInt(searchExtent);

        BlockPos pos = BlockPositions.of(x, y, z);

        if (isNotAir(level, pos) || isFluid(level, pos) || isWaterLogged(level, pos)) {
            return findPos(level, player, searchExtent, depth);
        }

        if (isWetBiome(level, pos)) {
            return findPos(level, player, searchExtent, depth);
        }

        if (isNotWithinWorldBorder(level, pos)) {
            return findPos(level, player, searchExtent, depth);
        }

        SnakerLib.DEVLOGGER.infof("Found spot at [ [], [], [] ] took [] tries", x, y, z, depth);

        return BlockPositions.of(x, y, z);
    }

    private synchronized boolean isNotWithinWorldBorder(ServerLevel level, BlockPos pos)
    {
        return !level.getWorldBorder().isWithinBounds(pos);
    }

    private synchronized boolean isWetBiome(ServerLevel level, BlockPos pos)
    {
        return level.getBiome(pos).is(Tags.Biomes.IS_WET) ||
                level.getBiome(pos).is(Tags.Biomes.IS_WATER);
    }

    private synchronized boolean isFluid(ServerLevel level, BlockPos pos)
    {
        return (level.getBlockState(pos).is(Blocks.WATER) || level.getBlockState(pos.below()).is(Blocks.WATER)) ||
                (level.getBlockState(pos).is(Blocks.LAVA) || level.getBlockState(pos.below()).is(Blocks.LAVA));
    }

    private synchronized boolean isWaterLogged(ServerLevel level, BlockPos pos)
    {
        return level.getBlockState(pos).hasProperty(BlockStateProperties.WATERLOGGED) ||
                level.getBlockState(pos.below()).hasProperty(BlockStateProperties.WATERLOGGED);
    }

    private synchronized boolean isNotAir(ServerLevel level, BlockPos pos)
    {
        return !level.getBlockState(pos).isAir() &&
                !level.getBlockState(pos.below()).isAir();
    }
}
