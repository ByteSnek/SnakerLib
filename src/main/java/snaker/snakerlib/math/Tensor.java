package snaker.snakerlib.math;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * Created by SnakerBone on 30/03/2023
 **/
@SuppressWarnings("unused")
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

    public void rotQuat(Quaternionf quaternion)
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
