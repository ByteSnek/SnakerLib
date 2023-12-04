package xyz.snaker.snakerlib.event;

import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public class EntityAttrCreationManager extends AbstractEventManager<EntityAttributeCreationEvent>
{
    public EntityAttrCreationManager(EntityAttributeCreationEvent event)
    {
        super(event);
    }

    public <T extends LivingEntity> void put(Supplier<EntityType<T>> entity, AttributeSupplier supplier)
    {
        addTask(() -> event.put(entity.get(), supplier));
    }
}
