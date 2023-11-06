package xyz.snaker.snakerlib.utility;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by SnakerBone on 16/08/2023
 **/
public class Once
{
    private final AtomicBoolean value = new AtomicBoolean();

    public boolean execute()
    {
        return !value.getAndSet(true);
    }

    public void reset()
    {
        value.getAndSet(false);
    }
}
