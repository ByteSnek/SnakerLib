package xyz.snaker.snakerlib;

import xyz.snaker.snakerlib.utility.unsafe.TheUnsafe;

/**
 * Created by SnakerBone on 4/11/2023
 * <p>
 * <strong>WARNING:</strong> Calling methods directly from this class <strong>is not recommended</strong>. They are usually called via {@link TheUnsafe} (or similar) with extensive checks and exception handling
 **/
public final class SnakerLibNatives
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

    /**
     * Gets uninitialized memory that is present on the current stack with the current index. Potentially can be unsafe due to UB and unwanted code execution before security checks are done. Can be useful as Java does not permit reading uninitialized memory addresses directly
     *
     * @param alloc The amount of memory to allocate to the uninitialized array (clamped between 0 and 1024)
     * @param i     The index of the memory array
     * @return The memory address at the specified index
     * @see TheUnsafe#uMemoryArray(int)
     **/
    public native long uMemory(int alloc, int i);
}
