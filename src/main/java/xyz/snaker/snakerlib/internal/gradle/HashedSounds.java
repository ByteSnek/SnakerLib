package xyz.snaker.snakerlib.internal.gradle;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.utility.Link;

import net.minecraft.Util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * Created by SnakerBone on 21/10/2023
 * <p>
 * A gradle task for renaming hashed sound files after running <a href="https://minecraft.fandom.com/wiki/Tutorials/Sound_directory#Extracting_Minecraft_sounds_using_Node.js">this script</a>
 * <p>
 * Currently only works on Windows
 **/
class HashedSounds
{
    public static void main(String[] args)
    {
        File jsonFile = getJsonFile();
        File objsFolder = getObjectsFolder();
        File[] objFiles = objsFolder.listFiles();
        Map<String, String> map = new HashMap<>();

        long startTime = System.currentTimeMillis();

        SnakerLib.LOGGER.info("Renaming hashed sounds...");

        try {
            JsonElement root = JsonParser.parseReader(new FileReader(jsonFile));
            JsonObject objects = root.getAsJsonObject().getAsJsonObject("objects");

            for (String key : objects.keySet()) {
                String keyNameWithExt = key.substring(key.lastIndexOf("/") + 1);
                String keyName = keyNameWithExt.substring(0, keyNameWithExt.indexOf('.'));

                JsonObject objPath = objects.getAsJsonObject(key);
                JsonPrimitive objHash = objPath.getAsJsonPrimitive("hash");

                String hash = objHash.getAsString();

                map.put(hash, keyName);
            }

            if (objFiles == null) {
                throw new NullPointerException("Objects in directory is empty or does not exist");
            }

            for (File file : objFiles) {
                if (file.isDirectory()) {
                    File[] subFiles = file.listFiles();

                    if (subFiles == null) {
                        throw new RuntimeException("Objects in sub directory is empty or does not exist");
                    }

                    for (File subFile : subFiles) {
                        String hash = subFile.getName().substring(0, subFile.getName().lastIndexOf('.'));

                        if (map.containsKey(hash)) {
                            String path = subFile.getAbsolutePath().replace(hash, map.get(hash));

                            if (subFile.renameTo(new File(path))) {
                                SnakerLib.LOGGER.info("Renamed file to " + path);
                            }
                        }
                    }
                }
            }

            SnakerLib.LOGGER.info("Successfully renamed all hashed sounds");
            SnakerLib.LOGGER.infof("Done. Elapsed time: %sms", System.currentTimeMillis() - startTime);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(objsFolder);
            }

            System.exit(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static File getRoamingFolder()
    {
        try {
            return switch (Util.getPlatform()) {
                case WINDOWS -> Paths.get(System.getenv("AppData"), ".minecraft").toFile();
                case OSX -> Paths.get(System.getProperty("user.home"), "Library", "Application Support", "minecraft").toFile();
                default -> throw new UnsupportedOperationException("Unsupported operating system: %s".formatted(Util.getPlatform()));
            };
        } catch (Exception e) {
            throw new RuntimeException("Could not locate the minecraft roaming folder on the current system", e);
        }
    }

    static File getJsonFile()
    {
        File folder = Paths.get(getRoamingFolder().getAbsolutePath(), "assets", "indexes").toFile();
        File[] files = folder.listFiles();

        if (files != null) {
            File jsonFile;

            files = Arrays.stream(files).filter(file -> file.getName().endsWith(".json")).toArray(File[]::new);

            if (Arrays.stream(files).anyMatch(file -> file.getName().startsWith("snkr") || file.getName().contains("snkr"))) {
                jsonFile = Arrays.stream(files).findFirst().orElseThrow();

                SnakerLib.LOGGER.info("Found prioritized JSON file starting with or containing 'snkr'. Using file: %s".formatted(jsonFile.getAbsolutePath()));

                return jsonFile;
            }

            if (files.length == 1) {
                jsonFile = files[0];

                SnakerLib.LOGGER.info("Found and using JSON file: %s".formatted(jsonFile.getAbsolutePath()));

            } else {
                jsonFile = Arrays.stream(files).findFirst().orElseThrow();

                SnakerLib.LOGGER.info("Found multiple JSON files. Using first one thats found by file system: %s".formatted(jsonFile.getAbsolutePath()));
                SnakerLib.LOGGER.info("Note: JSON files can be prioritized to be found easier by starting it's name with 'snkr'");
            }

            return jsonFile;
        }

        throw new RuntimeException("Directory %s could not be read or indexed".formatted(folder.getAbsolutePath()));
    }

    static File getObjectsFolder()
    {
        File file = Paths.get(getRoamingFolder().getAbsolutePath(), "assets", "objects").toFile();

        if (!file.exists()) {
            throw new RuntimeException("Could not find objects folder for sound extraction. Please vist %s and execute the provided Node.js script to generate the objects folder and try again".formatted(Link.of("https://minecraft.fandom.com/wiki/Tutorials/Sound_directory#Extracting_Minecraft_sounds_using_Node.js")));
        }

        return file;
    }
}
