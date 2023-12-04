package xyz.snaker.snakerlib.event;

import java.util.function.Supplier;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public class ClientSetupManager extends AbstractInternalEventManager<FMLClientSetupEvent>
{
    public ClientSetupManager(FMLClientSetupEvent event)
    {
        super(event);
    }

    public void setFluidRenderLayer(Supplier<FlowingFluid> fluid, RenderType type)
    {
        addTask(() -> ItemBlockRenderTypes.setRenderLayer(fluid.get(), type));
    }
}
