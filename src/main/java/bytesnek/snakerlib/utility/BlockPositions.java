package bytesnek.snakerlib.utility;

import net.minecraft.core.BlockPos;

/**
 * Created by SnakerBone on 24/11/2023
 **/
public class BlockPositions
{
    public static BlockPos of(int x, int y, int z)
    {
        return new BlockPos(x, y, z);
    }

    public static BlockPos.MutableBlockPos ofMutable(double x, double y, double z)
    {
        return new BlockPos.MutableBlockPos(x, y, z);
    }

    public static BlockPos distBetween(BlockPos a, BlockPos b)
    {
        int aX = a.getX();
        int aY = a.getY();
        int aZ = a.getZ();

        int bX = b.getX();
        int bY = b.getY();
        int bZ = b.getZ();

        int distX = aX - bX;
        int distY = aY - bY;
        int distZ = aZ - bZ;

        return new BlockPos(distX, distY, distZ);
    }
}
