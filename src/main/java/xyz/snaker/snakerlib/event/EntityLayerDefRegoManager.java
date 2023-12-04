package xyz.snaker.snakerlib.event;

import java.util.function.Supplier;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public class EntityLayerDefRegoManager extends AbstractEventManager<EntityRenderersEvent.RegisterLayerDefinitions>
{
    public EntityLayerDefRegoManager(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        super(event);
    }

    public void register(ModelLayerLocation location, Supplier<LayerDefinition> supplier)
    {
        addTask(() -> event.registerLayerDefinition(location, supplier));
    }
}
