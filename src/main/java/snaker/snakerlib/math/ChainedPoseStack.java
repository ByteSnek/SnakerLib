package snaker.snakerlib.math;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by SnakerBone on 30/03/2023
 * <p>
 * A simple {@link PoseStack} builder that adds extra functions and paramater overloads for optimization and such
 * <p>
 * All functions are called directly to the parent stack while also returning the current ChainedPoseStack instance. Similar to that of a builder
 **/
public class ChainedPoseStack extends PoseStack
{
    private volatile boolean funny;
    private final PoseStack parent;

    public ChainedPoseStack(PoseStack parent)
    {
        this.parent = parent;
        this.funny = true;
    }

    public static ChainedPoseStack set(PoseStack other)
    {
        return new ChainedPoseStack(other);
    }

    /**
     * Rotates the specified entity based on their {@link Entity#tickCount} like tumbleweed
     *
     * @param entity The entity to rotate
     **/
    public <T extends Entity> ChainedPoseStack funny(T entity)
    {
        double value = (entity.tickCount + Minecraft.getInstance().getFrameTime()) / 8;
        Quaternionf rotation = rotateXYZ(value);
        if (funny) {
            rotate(rotation);
        }
        return this;
    }

    /**
     * Specifies whether the Entity passed into {@link ChainedPoseStack#funny} should rotate or not
     *
     * @param funny Should {@link ChainedPoseStack#funny} be active
     **/
    public boolean setFunnyStatus(boolean funny)
    {
        this.funny = funny;
        return funny;
    }

    public ChainedPoseStack scale(double x, double y, double z)
    {
        parent.scale((float) x, (float) y, (float) z);
        return this;
    }

    public ChainedPoseStack scale(double value)
    {
        parent.scale((float) value, (float) value, (float) value);
        return this;
    }

    public ChainedPoseStack scale(Double[] scale)
    {
        assertCorrectLength(scale, 3);
        parent.scale(scale[0].floatValue(), scale[1].floatValue(), scale[2].floatValue());
        return this;
    }

    public ChainedPoseStack scale(Vector3f scale)
    {
        parent.scale(scale.x, scale.y, scale.z);
        return this;
    }

    public ChainedPoseStack translate(double translation)
    {
        parent.translate(translation, translation, translation);
        return this;
    }

    public ChainedPoseStack translate(double[] translation)
    {
        parent.translate(translation[0], translation[1], translation[2]);
        return this;
    }

    public ChainedPoseStack translate(Vector3f translation)
    {
        parent.translate(translation.x, translation.y, translation.z);
        return this;
    }

    public ChainedPoseStack rotate(Quaternionf rotation)
    {
        parent.mulPose(rotation);
        return this;
    }

    public ChainedPoseStack rotate(double x, double y, double z)
    {
        rotate(new Quaternionf(x, y, z, 1));
        return this;
    }

    public ChainedPoseStack rotate(double x, double y, double z, double w)
    {
        rotate(new Quaternionf(x, y, z, w));
        return this;
    }

    public ChainedPoseStack rotate(Double[] rotation)
    {
        assertCorrectLength(rotation, 4);
        rotate(new Quaternionf(rotation[0], rotation[1], rotation[2], rotation[3]));
        return this;
    }

    public ChainedPoseStack rotateAround(Quaternionf rotation, Vector3f pos)
    {
        parent.rotateAround(rotation, pos.x, pos.y, pos.z);
        return this;
    }

    public ChainedPoseStack rotateAround(Vector4f rotation, Vector3f pos)
    {
        parent.rotateAround(new Quaternionf(rotation.x, rotation.y, rotation.z, rotation.w), pos.x, pos.y, pos.z);
        return this;
    }

    public ChainedPoseStack rotateAround(Quaternionf rotation, double x, double y, double z)
    {
        parent.rotateAround(rotation, (float) x, (float) y, (float) z);
        return this;
    }

    public ChainedPoseStack rotateAround(double x1, double y1, double z1, double w, double x2, double y2, double z2)
    {
        parent.rotateAround(new Quaternionf(x1, y1, z1, w), (float) x2, (float) y2, (float) z2);
        return this;
    }

    public ChainedPoseStack rotateAround(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        parent.rotateAround(new Quaternionf(x1, y1, z1, 1), (float) x2, (float) y2, (float) z2);
        return this;
    }

    public ChainedPoseStack rotateAround(Double[] rotation, Double[] pos)
    {
        assertCorrectLength(rotation, 4);
        assertCorrectLength(pos, 3);
        parent.rotateAround(new Quaternionf(rotation[0], rotation[1], rotation[2], rotation[3]), pos[0].floatValue(), pos[1].floatValue(), pos[2].floatValue());
        return this;
    }

    /**
     * Rotates a {@link BlockEntity} to always be facing the main camera.
     * <p>
     * Useful for BlockEntities that have been built with a {@link VertexConsumer} using {@link BasicCube}
     *
     * @param blockEntity The BlockEntity
     * @param around      The pivot point of the BlockEntity
     * @param sub         Optional subtraction qualifier
     **/
    public <B extends BlockEntity> ChainedPoseStack rotateTowardsFrustum(B blockEntity, Vector3f around, double sub)
    {
        Vec3 camera = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        BlockPos pos = blockEntity.getBlockPos();

        double camX = camera.x;
        double camY = camera.y;
        double camZ = camera.z;

        double x = camX - pos.getX();
        double y = camY - pos.getY();
        double z = camZ - pos.getZ();

        double angleXZ = Mh.atan2(z, x);
        double angleY = Mh.atan2(y, Mh.sqrt(x * x + z * z));

        float rotXZ = Mh.toDeg(angleXZ);
        float rotY = Mh.toDeg(angleY - sub);

        Quaternionf degreesYXZ = Axis.YN.rotationDegrees(rotXZ);
        Quaternionf degreesZY = Axis.ZP.rotationDegrees(rotY);

        rotateAround(degreesYXZ, around.x, around.y, around.z);
        rotateAround(degreesZY, around.x, around.y, around.z);

        return this;
    }

    public <B extends BlockEntity> ChainedPoseStack rotateTowardsFrustum(B blockEntity, double sub)
    {
        Vec3 camera = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
        BlockPos pos = blockEntity.getBlockPos();

        double camX = camera.x;
        double camY = camera.y;
        double camZ = camera.z;

        double x = camX - pos.getX();
        double y = camY - pos.getY();
        double z = camZ - pos.getZ();

        double angleXZ = Mh.atan2(z, x);
        double angleY = Mh.atan2(y, Mh.sqrt(x * x + z * z));

        float rotXZ = Mh.toDeg(angleXZ);
        float rotY = Mh.toDeg(angleY - sub);

        Quaternionf degreesYXZ = Axis.YN.rotationDegrees(rotXZ);
        Quaternionf degreesZY = Axis.ZP.rotationDegrees(rotY);

        rotateAround(degreesYXZ, 0.5F, 0, 0.5F);
        rotateAround(degreesZY, 0.5F, 0, 0.5F);

        return this;
    }

    private Quaternionf rotateXYZ(double value)
    {
        return new Quaternionf().rotateXYZ((float) value, (float) value, (float) value);
    }

    private <A> void assertCorrectLength(A[] array, int wantedLength)
    {
        if (array.length != wantedLength) {
            throw new IndexOutOfBoundsException(array.length);
        }
    }
}
