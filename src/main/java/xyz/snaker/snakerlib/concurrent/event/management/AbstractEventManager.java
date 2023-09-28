package xyz.snaker.snakerlib.concurrent.event.management;

import java.util.Arrays;
import java.util.LinkedList;

import xyz.snaker.snakerlib.utility.tools.CollectionStuff;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * Created by SnakerBone on 28/09/2023
 **/
public abstract class AbstractEventManager<E extends Event>
{
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();

    public final E event;

    private Event.Result result;
    private boolean cancel;

    private boolean done;

    public AbstractEventManager(E event)
    {
        this.event = event;
    }

    public void addTask(Runnable task)
    {
        taskQueue.add(task);
    }

    public void registerToForge(Object... targets)
    {
        Arrays.stream(targets).forEach(MinecraftForge.EVENT_BUS::register);
    }

    public void setCanceled(boolean cancel)
    {
        this.cancel = cancel;
    }

    public void setResult(Event.Result result)
    {
        this.result = result;
    }

    public final void close()
    {
        if (done) {
            throw new RuntimeException("Task(s) for event '%s' have already been executed".formatted(event.getClass().getSimpleName()));
        } else {
            CollectionStuff.newCollectionStream(taskQueue, Runnable[]::new).forEach(Runnable::run);

            if (event.isCancelable()) {
                event.setCanceled(cancel);
            }

            if (event.hasResult()) {
                if (result != null) {
                    if (event.getResult() != result) {
                        if (event.getResult() != Event.Result.DEFAULT) {
                            event.setResult(result);
                        }
                    }
                }
            }

            done = true;
        }
    }
}
