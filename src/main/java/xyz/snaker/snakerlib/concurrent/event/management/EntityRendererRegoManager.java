package xyz.snaker.snakerlib.concurrent.event.management;

import java.util.function.Supplier;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public class EntityRendererRegoManager extends AbstractEventManager<EntityRenderersEvent.RegisterRenderers>
{
    public EntityRendererRegoManager(EntityRenderersEvent.RegisterRenderers event)
    {
        super(event);
    }

    public <T extends BlockEntity> void registerBlockEntity(Supplier<BlockEntityType<T>> type, BlockEntityRendererProvider<T> provider)
    {
        addTask(() -> event.registerBlockEntityRenderer(type.get(), provider));
    }

    public <T extends Entity> void registerEntity(Supplier<EntityType<T>> type, EntityRendererProvider<T> provider)
    {
        addTask(() -> event.registerEntityRenderer(type.get(), provider));
    }
}
