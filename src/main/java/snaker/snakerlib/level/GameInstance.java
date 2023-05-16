package snaker.snakerlib.level;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Created by SnakerBone on 30/03/2023
 **/
@SuppressWarnings("unused")
public interface GameInstance
{
    Minecraft MINECRAFT = Minecraft.getInstance();
    Level LEVEL = safeGetMinecraftLevel();
    Player PLAYER = safeGetPlayer();

    static Player safeGetPlayer()
    {
        return assertNonNull(MINECRAFT.player);
    }

    static Level safeGetMinecraftLevel()
    {
        return assertNonNull(MINECRAFT.level);
    }

    static <X> X assertNonNull(X other)
    {
        assert other != null;
        return other;
    }
}
