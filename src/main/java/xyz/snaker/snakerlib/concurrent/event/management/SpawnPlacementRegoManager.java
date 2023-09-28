package xyz.snaker.snakerlib.concurrent.event.management;

import java.util.function.Supplier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public class SpawnPlacementRegoManager extends AbstractEventManager<SpawnPlacementRegisterEvent>
{
    public SpawnPlacementRegoManager(SpawnPlacementRegisterEvent event)
    {
        super(event);
    }

    public <T extends Entity> void register(Supplier<EntityType<T>> type, SpawnPlacements.Type placement, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> predicate, SpawnPlacementRegisterEvent.Operation op)
    {
        event.register(type.get(), placement, heightmap, predicate, op);
    }

    public <T extends Entity> void register(Supplier<EntityType<T>> type, SpawnPlacements.SpawnPredicate<T> predicate)
    {
        event.register(type.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, predicate, SpawnPlacementRegisterEvent.Operation.AND);
    }
}
