package bytesnek.snakerlib.event;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

import bytesnek.hiss.sneaky.Reflection;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public abstract class AbstractInternalEventManager<E extends ParallelDispatchEvent> extends AbstractEventManager<E>
{
    public AbstractInternalEventManager(E event)
    {
        super(event);
    }

    public ModContainer getModContainer()
    {
        return Reflection.getFieldDirect(ModLifecycleEvent.class, "container", true, null);
    }

    public ModLoadingStage getModLoadingStage()
    {
        return Reflection.getFieldDirect(ParallelDispatchEvent.class, "modLoadingStage", true, null);
    }
}
