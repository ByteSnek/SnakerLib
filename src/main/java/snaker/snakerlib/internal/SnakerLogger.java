package snaker.snakerlib.internal;

/**
 * Created by SnakerBone on 29/05/2023
 * <p>
 * Log helper
 **/
public class SnakerLogger
{
    public static <Exception extends Throwable> void logError(Exception exception)
    {
        error(exception);
        exception.printStackTrace();
    }

    public static <Message> void msg(Message message)
    {
        msg(message, ColourCode.WHITE, MarkerType.MESSAGE);
    }

    public static <Message> void msg(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void info(Message message)
    {
        info(message, ColourCode.CYAN, MarkerType.INFO);
    }

    public static <Message> void info(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void debug(Message message)
    {
        debug(message, ColourCode.CYAN, MarkerType.DEBUG);
    }

    public static <Message> void debug(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void catching(Message message)
    {
        catching(message, ColourCode.YELLOW, MarkerType.CATCHING);
    }

    public static <Message> void catching(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void system(Message message)
    {
        system(message, ColourCode.GREEN, MarkerType.SYSTEM);
    }

    public static <Message> void system(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void thread(Message message)
    {
        thread(message, ColourCode.GREEN, MarkerType.THREAD);
    }

    public static <Message> void thread(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void worker(Message message)
    {
        worker(message, ColourCode.CYAN, MarkerType.WORKER);
    }

    public static <Message> void worker(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void trace(Message message)
    {
        trace(message, ColourCode.RED, MarkerType.TRACE);
    }

    public static <Message> void trace(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void dev(Message message)
    {
        dev(message, ColourCode.PURPLE, MarkerType.DEV);
    }

    public static <Message> void dev(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void warn(Message message)
    {
        warn(message, ColourCode.YELLOW, MarkerType.WARN);
    }

    public static <Message> void warn(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void error(Message message)
    {
        error(message, ColourCode.RED, MarkerType.ERROR);
    }

    public static <Message> void error(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    public static <Message> void fatal(Message message)
    {
        fatal(message, ColourCode.RED, MarkerType.FATAL);
    }

    public static <Message> void fatal(Message message, ColourCode colour, MarkerType type)
    {
        print(colour.get() + type.get() + message);
    }

    private static <Message> void print(Message message)
    {
        System.out.println(message + ColourCode.WHITE.get());
    }
}