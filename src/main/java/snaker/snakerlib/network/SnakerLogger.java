package snaker.snakerlib.network;

import snaker.snakerlib.data.ColourCode;
import snaker.snakerlib.data.MarkerType;

/**
 * Created by SnakerBone on 29/05/2023
 **/
public class SnakerLogger
{
    public static <V> void msg(V message)
    {
        msg(message, ColourCode.WHITE, MarkerType.MESSAGE);
    }

    public static <V> void msg(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void info(V message)
    {
        info(message, ColourCode.CYAN, MarkerType.INFO);
    }

    public static <V> void info(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void debug(V message)
    {
        debug(message, ColourCode.CYAN, MarkerType.DEBUG);
    }

    public static <V> void debug(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void catching(V message)
    {
        catching(message, ColourCode.YELLOW, MarkerType.CATCHING);
    }

    public static <V> void catching(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void system(V message)
    {
        system(message, ColourCode.GREEN, MarkerType.SYSTEM);
    }

    public static <V> void system(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void thread(V message)
    {
        thread(message, ColourCode.GREEN, MarkerType.THREAD);
    }

    public static <V> void thread(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void worker(V message)
    {
        worker(message, ColourCode.CYAN, MarkerType.WORKER);
    }

    public static <V> void worker(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void trace(V message)
    {
        trace(message, ColourCode.RED, MarkerType.TRACE);
    }

    public static <V> void trace(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void dev(V message)
    {
        dev(message, ColourCode.PURPLE, MarkerType.DEV);
    }

    public static <V> void dev(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void warn(V message)
    {
        warn(message, ColourCode.YELLOW, MarkerType.WARN);
    }

    public static <V> void warn(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void error(V message)
    {
        error(message, ColourCode.RED, MarkerType.ERROR);
    }

    public static <V> void error(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }

    public static <V> void fatal(V message)
    {
        fatal(message, ColourCode.RED, MarkerType.FATAL);
    }

    public static <V> void fatal(V message, ColourCode colour, MarkerType type)
    {
        System.out.println(colour.get() + type.get() + message);
    }
}