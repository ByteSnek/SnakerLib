package snaker.snakerlib.math;

import codechicken.lib.util.Copyable;
import codechicken.lib.vec.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Created by SnakerBone on 12/05/2023
 **/
public class Vec3A implements Copyable<Vec3A>
{
    public double x;
    public double y;
    public double z;

    public Vec3A()
    {

    }

    public Vec3A(Entity entity)
    {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    public Vec3A(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3A(Vec3A vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3A(Vector3 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3A(BlockPos pos)
    {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vec3A set(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3A set(Vec3A vec)
    {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public Vec3A set(BlockPos pos)
    {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        return this;
    }

    public Vec3A add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3A add(Vec3A vec)
    {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vec3A add(BlockPos pos)
    {
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    public Vec3A sub(BlockPos pos)
    {
        this.x -= pos.getX();
        this.y -= pos.getY();
        this.z -= pos.getZ();
        return this;
    }

    public Vec3A sub(Vec3A vec)
    {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vec3A sub(double x, double y, double z)
    {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3A mul(Vec3A vec)
    {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Vec3A mul(double x, double y, double z)
    {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    @Override
    public Vec3A copy()
    {
        return new Vec3A(this);
    }

    public BlockPos getPos()
    {
        return new BlockPos(x, y, z);
    }

    public Vector3 toVector3()
    {
        return new Vector3(x, y, z);
    }

    public Vec3A offset(Direction direction, double offset)
    {
        this.x += direction.getStepX() * offset;
        this.y += direction.getStepY() * offset;
        this.z += direction.getStepZ() * offset;
        return this;
    }

    public Vec3A offset(Vec3A direction, double offset)
    {
        this.x += direction.x * offset;
        this.y += direction.y * offset;
        this.z += direction.z * offset;
        return this;
    }

    public Vec3A rad(Direction.Axis axis, double sine, double cosine, double offset)
    {
        x += ((axis == Direction.Axis.X ? 0 : sine) * offset);
        y += ((axis == Direction.Axis.X ? sine : axis == Direction.Axis.Y ? 0 : cosine) * offset);
        z += ((axis == Direction.Axis.X ? cosine : axis == Direction.Axis.Y ? cosine : 0) * offset);
        return this;
    }

    public static Vec3A dir(Vec3A vec1, Vec3A vec2)
    {
        double distance = SnakerMth.dist(vec1, vec2);
        if (distance == 0) {
            distance = 0.1;
        }
        Vec3A offset = vec2.copy();
        offset.sub(vec1);
        return new Vec3A((offset.x / distance), (offset.y / distance), (offset.z / distance));
    }

    public static Vector3 dir(Vector3 vec1, Vector3 vec2)
    {
        double distance = SnakerMth.dist(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z);
        if (distance == 0) {
            distance = 0.1;
        }
        Vector3 offset = vec2.copy();
        offset.subtract(vec1);
        return new Vector3(offset.x / distance, offset.y / distance, offset.z / distance);
    }

    public static Vec3A centre(BlockPos pos)
    {
        return new Vec3A(pos).add((0.5), (0.5), (0.5));
    }

    public static Vec3A centre(BlockEntity entity)
    {
        return centre(entity.getBlockPos());
    }

    public double distXZ(Vec3A vec)
    {
        return SnakerMth.distXZ(x, z, vec.x, vec.z);
    }

    public double dist(Vec3A vec)
    {
        return SnakerMth.dist(this, vec);
    }

    public double dist(Entity entity)
    {
        return SnakerMth.dist(this, new Vec3A(entity));
    }

    public double distSq(Vec3A vec)
    {
        return SnakerMth.distSq(x, y, z, vec.x, vec.y, vec.z);
    }

    public int floorX()
    {
        return Mth.floor(x);
    }

    public int floorY()
    {
        return Mth.floor(y);
    }

    public int floorZ()
    {
        return Mth.floor(z);
    }
}