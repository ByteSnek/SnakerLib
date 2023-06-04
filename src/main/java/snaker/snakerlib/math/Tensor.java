package snaker.snakerlib.math;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

/**
 * Created by SnakerBone on 30/03/2023
 **/
public class Tensor extends PoseStack
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

    public Matrix4f toMatrix4f()
    {
        return stack.last().pose();
    }
}
