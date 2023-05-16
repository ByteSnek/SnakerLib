package snaker.snakerlib.math;

import codechicken.lib.vec.Vector3;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;

import java.math.BigInteger;

/**
 * Created by SnakerBone on 14/02/2023
 **/
@SuppressWarnings("unused")
public class Maths
{
    public static final int LEVEL_AABB_RADIUS = 0x989680;
    public static final double RADIANS_TO_DEGREES = 57.29577951308232;
    public static final double DEGREES_TO_RADIANS = 0.017453292519943;

    public static final float PI_HALF = (float) Math.PI / 2;
    public static final double PIE_ADD = Math.PI + Math.E;
    public static final double PIE_SUB = Math.PI - Math.E;
    public static final double PIE_MUL = Math.PI * Math.E;
    public static final double PIE_DIV = Math.PI / Math.E;

    public static double dist(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return Math.sqrt((x * x + y * y + z * z));
    }

    public static double distXZ(double x1, double z1, double x2, double z2)
    {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }

    public static double dist(Vector3 pos1, Vector3 pos2)
    {
        return dist(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static double distSq(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return x * x + y * y + z * z;
    }

    public static double distSqXZ(double x1, double z1, double x2, double z2)
    {
        double x = x1 - x2;
        double z = z1 - z2;
        return x * x + z * z;
    }

    public static Vector3f rotateVec(Vector3f vec, double angle, double x, double y, double z)
    {
        return rotateVec(vec, (float) angle, (float) x, (float) y, (float) z);
    }

    public static Vector3f rotateVec(Vector3f vec, float angle, float x, float y, float z)
    {
        float l = Mth.sqrt(x * x + y * y + z * z), r = (float) Math.toRadians(angle);
        float s = Mth.sin(r), c = Mth.cos(r);

        if (l != 0)
        {
            x /= l;
            y /= l;
            z /= l;
        }

        float[][] m = new float[3][3];
        float[] v = new float[3];

        m[0][0] = c + x * x * (1 - c);
        m[0][1] = x * y * (1 - c) - z * s;
        m[0][2] = x * z * (1 - c) + y * s;
        m[1][0] = y * x * (1 - c) + z * s;
        m[1][1] = c + y * y * (1 - c);
        m[1][2] = y * z * (1 - c) - x * s;
        m[2][0] = z * x * (1 - c) - y * s;
        m[2][1] = z * y * (1 - c) + x * s;
        m[2][2] = c + z * z * (1 - c);

        v[0] = m[0][0] * vec.x() + m[0][1] * vec.y() + m[0][2] * vec.z();
        v[1] = m[1][0] * vec.x() + m[1][1] * vec.y() + m[1][2] * vec.z();
        v[2] = m[2][0] * vec.x() + m[2][1] * vec.y() + m[2][2] * vec.z();

        return new Vector3f(v[0], v[1], v[2]);
    }

    public static int clamp(int number, int lower, int upper)
    {
        return Math.max(Math.min(number, upper), lower);
    }

    public static long clamp(long number, long lower, long upper)
    {
        return Math.max(Math.min(number, upper), lower);
    }

    public static float clamp(float number, float lower, float upper)
    {
        return Math.max(Math.min(number, upper), lower);
    }

    public static double clamp(double number, double lower, double upper)
    {
        return Math.max(Math.min(number, upper), lower);
    }

    public static double round(double number, double multiplier)
    {
        return Math.round(number * multiplier) / multiplier;
    }

    public static int pow2(int exponent)
    {
        return (int) Math.pow(2, exponent);
    }

    public static float rotateTowards(float pos1, float pos2)
    {
        float angle = (float) Mth.atan2(pos1, pos2);
        return -(angle * (180 / (float) Math.PI));
    }

    public static float rotateTowards(double pos1, double pos2)
    {
        double angle = Mth.atan2(pos1, pos2);
        return -((float) angle * (180 / (float) Math.PI));
    }

    public static float rotateAway(float pos1, float pos2)
    {
        float angle = (float) Mth.atan2(pos1, pos2);
        return (angle * (180 / (float) Math.PI));
    }

    public static float rotateAway(double pos1, double pos2)
    {
        double angle = Mth.atan2(pos1, pos2);
        return ((float) angle * (180 / (float) Math.PI));
    }

    public static float rotateTowardsDegrees(double pos1, double pos2)
    {
        float angle = (float) Mth.atan2(pos1, pos2);
        return (angle * (float) Maths.RADIANS_TO_DEGREES);
    }

    public static BigInteger factorial(int value)
    {
        BigInteger factorial = BigInteger.ONE;
        for (int i = value; i > 0; i--)
        {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    public static BigInteger factorial(long value)
    {
        BigInteger factorial = BigInteger.ONE;
        for (long i = value; i > 0; i--)
        {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    public static BigInteger additorial(int value)
    {
        BigInteger additorial = BigInteger.ONE;
        for (int i = value; i > 0; i--)
        {
            additorial = additorial.add(BigInteger.valueOf(i));
        }
        return additorial;
    }

    public static BigInteger additorial(long value)
    {
        BigInteger additorial = BigInteger.ONE;
        for (long i = value; i > 0; i--)
        {
            additorial = additorial.add(BigInteger.valueOf(i));
        }
        return additorial;
    }
}
