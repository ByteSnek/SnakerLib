package snaker.snakerlib.math;

import codechicken.lib.util.Copyable;
import codechicken.lib.vec.Matrix4;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

/**
 * Created by SnakerBone on 30/03/2023
 **/
@SuppressWarnings("unused")
public class Tensor extends PoseStack implements Copyable<Tensor>
{
    private PoseStack stack;

    public Tensor(PoseStack stack)
    {
        this.stack = stack;
    }

    public Tensor()
    {
        setIdentity();
    }

    public void scale(float vec)
    {
        stack.scale(vec, vec, vec);
    }

    public void translate(float vec)
    {
        stack.translate(vec, vec, vec);
    }

    public void rotVec3(float x, float y, float z)
    {
        stack.mulPose(Quaternion.fromXYZ(x, y, z));
    }

    public void rotQuat(Quaternion quaternion)
    {
        stack.mulPose(quaternion);
    }

    public void startTransposition()
    {
        stack.pushPose();
    }

    public void endTransposition()
    {
        stack.popPose();
    }

    public Matrix4 toMatrix4()
    {
        return new Matrix4(stack);
    }

    public Matrix4f toMatrix4f()
    {
        return stack.last().pose();
    }

    @Override
    public Tensor copy()
    {
        return stack == null ? new Tensor() : new Tensor(stack);
    }
}
