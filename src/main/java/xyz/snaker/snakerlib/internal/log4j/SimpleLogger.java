package xyz.snaker.snakerlib.internal.log4j;

import java.util.Formatter;
import java.util.function.Function;

import xyz.snaker.snakerlib.utility.tools.StringStuff;

/**
 * Created by SnakerBone on 9/07/2023
 **/
public class SimpleLogger implements SnakerLogger
{
    /**
     * The instance to create. Pass in an identifier for the logger. The logger would output something like this:
     * <p>
     * <code>
     * [{YOUR_LOGGER_IDENTIFIER_NAME_HERE}/INFO]: Message content goes here......
     * </code>
     * </p>
     **/
    public static Function<String, SimpleLogger> INSTANCE = SimpleLogger::new;

    /**
     * This logger's identifier
     **/
    protected final String name;

    protected SimpleLogger(String name)
    {
        this.name = StringStuff.i18nt(name);
    }

    @Override
    public <MSG> void info(MSG message)
    {
        print(message, ConsoleLogLevel.INFO);
    }

    @Override
    public <MSG> void warn(MSG message)
    {
        print(message, ConsoleLogLevel.WARN);
    }

    @Override
    public <MSG> void error(MSG message)
    {
        print(message, ConsoleLogLevel.ERROR);
    }

    @Override
    public <MSG> void print(MSG message, ConsoleLogLevel level)
    {
        System.out.println(level.colourValue() + "[" + name + "/" + level.markerValue() + "]: " + message + ConsoleTextColour.WHITE.value());
    }

    @Override
    public void infof(String format, Object... args)
    {
        printf(format, ConsoleLogLevel.INFO, args);
    }

    @Override
    public void warnf(String format, Object... args)
    {
        printf(format, ConsoleLogLevel.WARN, args);
    }

    @Override
    public void errorf(String format, Object... args)
    {
        printf(format, ConsoleLogLevel.ERROR, args);
    }

    @Override
    public void printf(String format, ConsoleLogLevel level, Object... args)
    {
        System.out.println(level.colourValue() + "[" + name + "/" + level.markerValue() + "]: " + new Formatter().format(format, args) + ConsoleTextColour.WHITE.value());
    }
}
