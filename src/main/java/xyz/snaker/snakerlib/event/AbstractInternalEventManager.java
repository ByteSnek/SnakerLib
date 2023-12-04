package xyz.snaker.snakerlib.event;

import net.neoforged.fml.event.lifecycle.ParallelDispatchEvent;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public abstract class AbstractInternalEventManager<E extends ParallelDispatchEvent> extends AbstractEventManager<E>
{
    public AbstractInternalEventManager(E event)
    {
        super(event);
    }
}
