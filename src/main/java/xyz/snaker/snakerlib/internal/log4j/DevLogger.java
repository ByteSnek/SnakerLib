package xyz.snaker.snakerlib.internal.log4j;

import java.util.Formatter;
import java.util.function.Function;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.tools.StringStuff;
import xyz.snaker.snakerlib.utility.tools.TimeStuff;

/**
 * Created by SnakerBone on 10/13/2023
 **/
public class DevLogger implements SnakerLogger
{
    /**
     * The instance to create. Pass in an identifier for the logger. The logger would output something like this:
     * <p>
     * <code>
     * [{YOUR_LOGGER_IDENTIFIER_NAME_HERE}/INFO]: Message content goes here......
     * </code>
     * </p>
     **/
    public static final Function<String, DevLogger> INSTANCE = name -> new DevLogger(name, SnakerLib.isInDeveloperEnvironment());

    /**
     * This logger's identifier
     **/
    private final String name;

    /**
     * Checks if the logger is in developer environment
     **/
    private final boolean inDeveloperEnvironment;

    /**
     * The timers name
     *
     * @see #startTimer(String)
     * @see #stopTimer()
     **/
    private String timerName;

    /**
     * The timers start time
     *
     * @see #startTimer(String)
     * @see #stopTimer()
     **/
    private long startTime;

    /**
     * Checks if the timer is currently running
     *
     * @see #startTimer(String)
     * @see #stopTimer()
     **/
    private boolean timerRunning;

    protected DevLogger(String name, boolean inDeveloperEnvironment)
    {
        this.name = "DEV/" + StringStuff.i18nt(name);
        this.inDeveloperEnvironment = inDeveloperEnvironment;
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
        if (inDeveloperEnvironment) {
            System.out.println(level.colourValue() + "[" + name + "/" + level.markerValue() + "]: " + message + ConsoleTextColour.WHITE.value());
        }
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
        if (inDeveloperEnvironment) {
            System.out.println(level.colourValue() + "[" + name + "/" + level.markerValue() + "]: " + new Formatter().format(format, args) + ConsoleTextColour.WHITE.value());
        }
    }

    /**
     * Starts a timer if one isn't currently running
     *
     * @param name The timer name
     **/
    public void startTimer(String name)
    {
        if (inDeveloperEnvironment) {
            if (!timerRunning) {
                timerName = name;
                startTime = System.nanoTime();
                timerRunning = true;
            }
        }
    }

    /**
     * Stops the current running timer if one is running
     **/
    public void stopTimer()
    {
        if (inDeveloperEnvironment) {
            if (timerRunning) {
                long nanos = System.nanoTime() - startTime;
                long millis = TimeStuff.MILLISECOND;
                long seconds = millis * TimeStuff.SECOND;

                String elapsedTime = nanos + "ns";

                if (nanos > seconds) {
                    elapsedTime = TimeStuff.nanosToSeconds(nanos) + "s";
                } else if (nanos > TimeStuff.SECOND) {
                    elapsedTime = TimeStuff.nanosToMillis(nanos) + "ms";
                }

                infof("Timer '%s' stopped. Elapsed time: %s", timerName, elapsedTime);
                timerRunning = false;
            }
        }
    }
}
