package xyz.snaker.snakerlib.client.render.skybox;

import java.util.HashMap;
import java.util.Map;

import xyz.snaker.snakerlib.resources.ResourceReference;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

/**
 * Created by SnakerBone on 27/09/2023
 * <p>
 * A texture consisting of 6 textures and 6 sides used to draw a skybox
 **/
public class SkyBoxTexture
{
    private static final String FILE_EXTENSION = "[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)?";

    /**
     * The skybox folder to look in for the textures
     **/
    public static final String SKYBOX_FOLDER = "textures/skybox/";

    /**
     * 6 empty ResourceLocation's for the textures
     **/
    private final ResourceLocation[] textures = new ResourceLocation[6];

    /**
     * A map that maps the texture index to the side
     **/
    private final Map<Integer, String> side = Util.make(new HashMap<>(), map ->
    {
        map.put(0, "front");
        map.put(1, "right");
        map.put(2, "back");
        map.put(3, "left");
        map.put(4, "up");
        map.put(5, "down");
    });

    public SkyBoxTexture(@Nullable String textureName)
    {
        setTextures(textureName);
    }

    public SkyBoxTexture()
    {
        this(null);
    }

    /**
     * Sets the textures and creates a new skybox texture
     *
     * @param textureName An optional texture name
     **/
    public void setTextures(@Nullable String textureName)
    {
        if (textureName != null) {
            for (int i = 0; i < 6; i++) {
                textures[i] = texturePath(textureName + "_" + side.get(i));
            }
        } else {
            for (int i = 0; i < 6; i++) {
                textures[i] = texturePath(side.get(i));
            }
        }
    }

    /**
     * Sets a texture path
     *
     * @param path The path to the texture
     * @return A new ResourcePath
     * @throws IllegalArgumentException If the path contains a file extension
     **/
    public ResourceReference texturePath(String path)
    {
        if (path.matches(FILE_EXTENSION)) {
            throw new IllegalArgumentException("Texture path cannot contain file extensions");
        }
        return new ResourceReference(SKYBOX_FOLDER + path + ".png");
    }

    /**
     * Sets a texture path
     *
     * @param path The path to the texture
     * @return A new ResourceLocation
     * @throws IllegalArgumentException If the path contains a file extension
     **/
    public ResourceLocation texturePath(String namesapce, String path)
    {
        if (path.matches(FILE_EXTENSION)) {
            throw new IllegalArgumentException("Texture path cannot contain file extensions");
        }
        return new ResourceLocation(namesapce, SKYBOX_FOLDER + path + ".png");
    }

    /**
     * Gets a single side of a texture
     *
     * @param side The side to get
     * @return The texture as a ResourceLocation
     **/
    public ResourceLocation getTexture(Side side)
    {
        return textures[side.getIndex()];
    }

    /**
     * A skybox texture side enum
     **/
    public enum Side
    {
        FRONT(0),
        RIGHT(1),
        BACK(2),
        LEFT(3),
        UP(4),
        DOWN(5);

        /**
         * The index of the side
         **/
        private final int index;

        Side(int index)
        {
            this.index = index;
        }

        /**
         * Gets the side index
         *
         * @return The side index
         **/
        public int getIndex()
        {
            return index;
        }
    }
}
