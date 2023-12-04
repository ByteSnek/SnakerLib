package xyz.snaker.snakerlib.event;

import java.util.Arrays;
import java.util.LinkedList;

import xyz.snaker.hiss.utility.Streams;

import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Created by SnakerBone on 28/09/2023
 * <p>
 * The base forge event manager
 **/
public abstract class AbstractEventManager<E extends Event>
{
    /**
     * The task execution queue for the current event
     **/
    private final LinkedList<Runnable> taskQueue = new LinkedList<>();

    /**
     * The current event
     **/
    public final E event;

    /**
     * The event result for the current event
     **/
    private Event.Result result;

    /**
     * The event cancellation flag for the current event
     **/
    private boolean cancel;

    /**
     * Checks if the current event manager has finished it's task(s). If true; no more task(s) can be added to this event manager
     **/
    private boolean done;

    public AbstractEventManager(E event)
    {
        this.event = event;
    }

    /**
     * Adds a task to the current event manager
     *
     * @param task The task to execute (as a runnable)
     **/
    public void addTask(Runnable task)
    {
        taskQueue.add(task);
    }

    /**
     * Registers task(s) to NeoForge
     *
     * @param targets The task(s) to execute
     **/
    public void registerToForge(Object... targets)
    {
        Arrays.stream(targets).forEach(NeoForge.EVENT_BUS::register);
    }

    /**
     * Sets the result of the current event
     *
     * @param result The result
     * @throws RuntimeException If the current event does not have a result
     **/
    public void setResult(Event.Result result)
    {
        if (!event.hasResult()) {
            throw new RuntimeException("Current event does not have a result");
        }

        this.result = result;
    }

    /**
     * Closes the current event manager and runs each task
     *
     * @throws RuntimeException If the event manager has already finished executing it's tasks
     **/
    public final void close()
    {
        if (done) {
            throw new RuntimeException("Task(s) for event '%s' have already been executed".formatted(event.getClass().getSimpleName()));
        } else {
            Streams.newCollectionStream(taskQueue, Runnable[]::new).forEach(Runnable::run);

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
