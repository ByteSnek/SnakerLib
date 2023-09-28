package xyz.snaker.snakerlib.concurrent.event.management;

import xyz.snaker.snakerlib.utility.tools.ReflectiveStuff;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;

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
        return ReflectiveStuff.getFieldDirect(ModLifecycleEvent.class, "container", true, null);
    }

    public ModLoadingStage getModLoadingStage()
    {
        return ReflectiveStuff.getFieldDirect(ParallelDispatchEvent.class, "modLoadingStage", true, null);
    }
}
