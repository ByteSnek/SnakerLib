package bytesnek.snakerlib.resources;

import java.io.Serial;

import net.minecraft.ResourceLocationException;

/**
 * Created by SnakerBone on 15/11/2023
 **/
public class ResourceReferenceException extends ResourceLocationException
{
    @Serial
    private static final long serialVersionUID = -1L;

    private static final Exception CAUSE = new Exception();
    private static final String MESSAGE = CAUSE.getStackTrace()[1].toString();

    public ResourceReferenceException()
    {
        super(MESSAGE, CAUSE);
    }

    public ResourceReferenceException(String message)
    {
        super(message);
    }

    public ResourceReferenceException(Throwable cause)
    {
        super(MESSAGE, cause);
    }

    public ResourceReferenceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
