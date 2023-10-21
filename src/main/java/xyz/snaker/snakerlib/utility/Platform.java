package xyz.snaker.snakerlib.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SnakerBone on 21/10/2023
 * <p>
 * An enum consisting of the commonly used operating systems
 **/
public enum Platform
{
    /**
     * Windows platform
     **/
    WINDOWS("os.name", "AppData", "rundll32 url.dll,FileProtocolHandler", true),

    /**
     * OSX platform
     **/
    MAC("os.name", "user.home", "open", false),

    /**
     * Linux platform
     **/
    LINUX("os.name", "user.home", "sh -c", false);

    /**
     * The system property of the name of this platform
     **/
    private final String nameProp;

    /**
     * The local application data variable
     **/
    private final String appData;

    /**
     * The default file protocol handler for this platform
     **/
    private final String fileProtocolHandler;

    /**
     * Checks whether this operating system uses {@link System#getenv()} instead of {@link System#getProperty(String)} to get the local application data
     **/
    private final boolean useEnv;

    /**
     * Default constructor for this platform
     *
     * @param nameProp            The system property of the name of this platform
     * @param appData             The local application data variable
     * @param fileProtocolHandler The default file protocol handler for this platform
     * @param useEnv              Checks whether this operating system uses {@link System#getenv()} instead of {@link System#getProperty(String)} to get the local application data
     **/
    Platform(String nameProp, String appData, String fileProtocolHandler, boolean useEnv)
    {
        this.nameProp = nameProp;
        this.appData = appData;
        this.fileProtocolHandler = fileProtocolHandler;
        this.useEnv = useEnv;
    }

    /**
     * Identifies the current platform
     *
     * @return The current platform or null if the current platform isn't specified in this enum
     **/
    public static Platform identify()
    {
        Map<String, Platform> platform = Suppliers.make(new HashMap<>(), map ->
        {
            map.put(Platform.WINDOWS.getName(), Platform.WINDOWS);
            map.put(Platform.MAC.getName(), Platform.MAC);
            map.put(Platform.LINUX.getName(), Platform.LINUX);
        });

        return platform.get(getProp("os.name"));
    }

    /**
     * Gets the local application data folder of this platform
     *
     * @return The local application data folder of this platform
     **/
    public Path getLocalAppData()
    {
        if (useEnv) {
            if (this == WINDOWS) {
                return getPathEnv(appData);
            }
        }

        return this == LINUX ? getPathProp(appData) : getPathProp(appData, "Library", "Application Support");
    }

    /**
     * Gets the name of this platform
     *
     * @return The name of this platform
     **/
    public String getName()
    {
        return getProp(nameProp);
    }

    /**
     * Gets the file protocol handler for this platform
     *
     * @return The file protocol handler for this platform
     **/
    public String getFileProtocolHandler()
    {
        return fileProtocolHandler;
    }

    /**
     * Gets a system property as a path
     *
     * @param key  The system property key
     * @param more An optional path extending the value of the property
     * @return The system property as a path
     **/
    public static Path getPathProp(String key, String... more)
    {
        return Paths.get(getProp(key), more);
    }

    /**
     * Gets an environment variable as a path
     *
     * @param key  The environment variable key
     * @param more An optional path extending the value of the environment variable
     * @return The environment variable as a path
     **/
    public static Path getPathEnv(String key, String... more)
    {
        return Paths.get(getEnv(key), more);
    }

    /**
     * Gets a system property as a path
     *
     * @param key The system property key
     * @return The system property as a path
     **/
    public static String getProp(String key)
    {
        return System.getProperty(key);
    }

    /**
     * Gets an environment variable as a path
     *
     * @param key The environment variable key
     * @return The environment variable as a path
     **/
    public static String getEnv(String key)
    {
        return System.getenv(key);
    }
}
