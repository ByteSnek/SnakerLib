package xyz.snaker.snakerlib.utility;

import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ibm.icu.util.SimpleHoliday;

/**
 * Created by SnakerBone on 15/08/2023
 **/
public class DatesAndTimes
{
    /**
     * The time right now
     **/
    public static final Date NOW = Date.from(Instant.now(Clock.systemDefaultZone()));

    /**
     * A single second in milliseconds
     **/
    public static final long SECOND = 1000;

    /**
     * A single millisecond in nanoseconds
     **/
    public static final long MILLISECOND = 1000000;

    /**
     * A single second in nanoseconds
     **/
    public static final long SECONDS = MILLISECOND * SECOND;

    /**
     * Converts nanoseconds to seconds
     *
     * @param nanos The time to convert in nanoseconds
     * @return The nanoseconds in seconds
     **/
    public static double nanosToSeconds(double nanos)
    {
        return toSeconds(nanos / SECONDS);
    }

    /**
     * Converts nanoseconds to seconds
     *
     * @param nanos The time to convert in nanoseconds
     * @return The nanoseconds in seconds
     **/
    public static long nanosToSeconds(long nanos)
    {
        return toSeconds(nanos / SECONDS);
    }

    /**
     * Converts nanoseconds to milliseconds
     *
     * @param nanos The time to convert in nanoseconds
     * @return The nanoseconds in milliseconds
     **/
    public static double nanosToMillis(double nanos)
    {
        return toSeconds(nanos / MILLISECOND);
    }

    /**
     * Converts nanoseconds to milliseconds
     *
     * @param nanos The time to convert in nanoseconds
     * @return The nanoseconds in milliseconds
     **/
    public static long nanosToMillis(long nanos)
    {
        return toSeconds(nanos / MILLISECOND);
    }

    /**
     * Base method for converting time to seconds
     *
     * @param time The time to convert
     * @return The time in seconds
     **/
    public static double toSeconds(double time)
    {
        return (double) Math.round(time * SECOND) / SECOND;
    }

    /**
     * Base method for converting time to seconds
     *
     * @param time The time to convert
     * @return The time in seconds
     **/
    public static long toSeconds(long time)
    {
        return (long) Math.round(time * SECOND) / SECOND;
    }

    /**
     * Checks if today is a holiday
     *
     * @return True if today is currently a holiday
     **/
    public static boolean isHoliday()
    {
        SimpleHoliday[] holidays = Reflection.getFieldsInClass(SimpleHoliday.class, o -> o instanceof SimpleHoliday, SimpleHoliday[]::new);
        List<Boolean> activeHolidays = Arrays.stream(holidays).map(holiday -> holiday.isOn(NOW)).toList();

        return activeHolidays.contains(true);
    }
}
