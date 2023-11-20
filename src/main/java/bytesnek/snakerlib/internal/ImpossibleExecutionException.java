package bytesnek.snakerlib.internal;

import java.io.Serial;

/**
 * Created by SnakerBone on 10/13/2023
 * <p>
 * Throws when code that is deemed unreachable executes:
 * <pre> {@code
 *    static final int alwaysOne = 1; // Is always 1. It will (should) never change
 *
 *    // Example
 *    if (alwaysOne == 2) {
 *        throw new ImpossibleExecutionException("How did this execute...");
 *    }
 *
 *    // Another (impossible) example
 *    if (true) {
 *        return;
 *        throw new ImpossibleExecutionException("Unreachable code reached");
 *    }
 * }</pre>
 **/
public class ImpossibleExecutionException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = -1L;

    public ImpossibleExecutionException(String message)
    {
        super(message);
    }

    public ImpossibleExecutionException()
    {
        super();
    }
}
