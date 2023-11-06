package xyz.snaker.snakerlib;

/**
 * Created by SnakerBone on 4/11/2023
 **/
public class SnakerLibNatives
{
    SnakerLibNatives()
    {
        if (SnakerLib.STACK_WALKER.getCallerClass() != SnakerLib.class) {
            throw new IllegalCallerException();
        }
    }

    static {
        SnakerLib.loadNatives();
    }

    /**
     * Goodbye, World!
     **/
    public native void goodbyeWorld();
}
