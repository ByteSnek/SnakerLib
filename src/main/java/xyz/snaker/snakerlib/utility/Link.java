package xyz.snaker.snakerlib.utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by SnakerBone on 21/10/2023
 * <p>
 * An immutable URL with extra functionality
 **/
public class Link implements Cloneable
{
    /**
     * The URL of this link
     **/
    private final URL url;

    /**
     * Creates a new link
     *
     * @param address The address of this link
     **/
    public Link(String address)
    {
        this.url = setUrl(address);
    }

    /**
     * Creates a new link
     *
     * @param address The address of this link
     * @return A new link
     **/
    public static Link of(String address)
    {
        return new Link(address);
    }

    /**
     * Sets the URL of this link
     *
     * @param address The address of this link
     * @return A URL with the address
     * @throws RuntimeException If the URL is invalid or is the address is null or empty
     **/
    private URL setUrl(String address)
    {
        if (address == null || address.isEmpty()) {
            throw new RuntimeException("URL address cannot be null or empty");
        }

        try {
            return new URL(address);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid or malformed URL address: %s".formatted(address), e);
        }
    }

    /**
     * Opens this link in a browser
     **/
    public void open()
    {
        Platform platform = Platform.identify();

        Runtime runtime = Runtime.getRuntime();
        String address = url.toString();

        switch (platform) {
            case WINDOWS, MAC -> openUrl(runtime, platform.getFileProtocolHandler(), address);
            case LINUX -> openUrl(runtime, platform, address);
        }
    }

    /**
     * Opens a simple URL
     *
     * @param runtime The current runtime instance
     * @param command The command to execute
     * @param address The URL address to open
     **/
    private void openUrl(Runtime runtime, String command, String address)
    {
        try {
            runtime.exec(command + " " + address);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a URL on unix systems
     *
     * @param runtime  The current runtime instance
     * @param platform The platform of the current unix OS
     * @param address  The URL address to open
     **/
    private void openUrl(Runtime runtime, Platform platform, String address)
    {
        String[] browsers = {"google-chrome", "firefox", "mozilla", "epiphany", "konqueror", "netscape", "opera", "links", "lynx"};
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < browsers.length; i++) {
            if (i == 0) {
                buffer.append(String.format("%s \"%s\"", browsers[i], address));
            } else {
                buffer.append(String.format(" || %s \"%s\"", browsers[i], address));
            }
        }

        try {
            runtime.exec(new String[]{platform.getFileProtocolHandler(), buffer.toString()});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets this link's URL
     *
     * @return This link's URL
     **/
    public URL getUrl()
    {
        return url;
    }

    /**
     * Gets this link's URL as a String
     *
     * @return This link's URL as a String
     **/
    public String getAddress()
    {
        return url.toString();
    }

    @Override
    public String toString()
    {
        return getAddress();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Link link) {
            return url == link.url || Objects.equals(getAddress(), link.getAddress());
        }

        return false;
    }

    @Override
    public Link clone()
    {
        try {
            return (Link) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
