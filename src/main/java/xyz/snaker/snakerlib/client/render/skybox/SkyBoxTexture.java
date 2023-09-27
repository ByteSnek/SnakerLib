package xyz.snaker.snakerlib.client.render.skybox;

import java.util.HashMap;
import java.util.Map;

import xyz.snaker.snakerlib.utility.ResourcePath;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

/**
 * Created by SnakerBone on 27/09/2023
 **/
public class SkyBoxTexture
{
    public static final String SKYBOX_FOLDER = "textures/skybox/";

    private final ResourceLocation[] textures = new ResourceLocation[6];

    private final Map<Integer, String> side = Util.make(new HashMap<>(), map -> {
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

    private void setTextures(@Nullable String textureName)
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

    private ResourcePath texturePath(String path)
    {
        return new ResourcePath(SKYBOX_FOLDER + path + ".png");
    }

    public ResourceLocation getTexture(Side side)
    {
        return textures[side.getIndex()];
    }

    public enum Side
    {
        FRONT(0),
        RIGHT(1),
        BACK(2),
        LEFT(3),
        UP(4),
        DOWN(5);

        private final int index;

        Side(int index)
        {
            this.index = index;
        }

        public int getIndex()
        {
            return index;
        }
    }
}
