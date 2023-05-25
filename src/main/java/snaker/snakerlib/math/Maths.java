package snaker.snakerlib.math;

import java.math.BigInteger;

/**
 * Created by SnakerBone on 14/02/2023
 **/
@SuppressWarnings("unused")
public class Maths
{
    public static final int LEVEL_AABB_RADIUS = 0x989680;
    public static final float RADIANS_TO_DEGREES = 57.29577951308232F;
    public static final float DEGREES_TO_RADIANS = 0.017453292519943F;
    public static final float PI_HALF = (float) Math.PI / 2;
    public static final float PI = (float) Math.PI / 2;

    public static final float PIE_ADD = (float) (Math.PI + Math.E);
    public static final float PIE_SUB = (float) (Math.PI - Math.E);
    public static final float PIE_MUL = (float) (Math.PI * Math.E);
    public static final float PIE_DIV = (float) (Math.PI / Math.E);

    // distance

    public static float dist(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return (float) Math.sqrt((x * x + y * y + z * z));
    }

    public static float dist(Vec3A a, Vec3A b)
    {
        return dist(a.x, a.y, a.z, b.x, b.y, b.z);
    }

    public static float distXZ(double x1, double z1, double x2, double z2)
    {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return (float) Math.sqrt((dx * dx + dz * dz));
    }

    public static float distSq(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        return (float) (x * x + y * y + z * z);
    }

    public static float distSqXZ(double x1, double z1, double x2, double z2)
    {
        double x = x1 - x2;
        double z = z1 - z2;
        return (float) (x * x + z * z);
    }

    // min, max

    public static float min(double a, double b)
    {
        return (float) Math.min(a, b);
    }

    public static float max(double a, double b)
    {
        return (float) Math.max(a, b);
    }

    // clamping

    public static int clamp(int value, int lower, int upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    public static long clamp(long value, long lower, long upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    public static float clamp(float value, float lower, float upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    public static double clamp(double value, double lower, double upper)
    {
        return Math.max(Math.min(value, upper), lower);
    }

    public static double round(double value, double multiplier)
    {
        return Math.round(value * multiplier) / multiplier;
    }

    // pow2

    public static int pow2(int exponent)
    {
        return (int) Math.pow(2, exponent);
    }

    // sine, cosine, tangent, etc

    public static float sin(double a)
    {
        return (float) Math.sin(a);
    }

    public static float cos(double a)
    {
        return (float) Math.cos(a);
    }

    public static float tan(double a)
    {
        return (float) Math.tan(a);
    }

    public static float asin(double a)
    {
        return (float) Math.asin(a);
    }

    public static float acos(double a)
    {
        return (float) Math.acos(a);
    }

    public static float atan(double a)
    {
        return (float) Math.atan(a);
    }

    public static float atan2(double y, double x)
    {
        return (float) Math.atan2(y, x);
    }

    // rotation towards point

    public static float sinRotNeg(double a)
    {
        float angle = Maths.sin(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float cosRotNeg(double a)
    {
        float angle = Maths.cos(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float tanRotNeg(double a)
    {
        float angle = Maths.tan(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float asinRotNeg(double a)
    {
        float angle = Maths.asin(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float acosRotNeg(double a)
    {
        float angle = Maths.acos(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float atanRotNeg(double a)
    {
        float angle = Maths.atan(a);
        return -(angle * (180 / Maths.PI));
    }

    public static float atan2RotNeg(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return -(angle * (180 / Maths.PI));
    }

    // rotation away point

    public static float sinRotPos(double a)
    {
        float angle = Maths.sin(a);
        return (angle * (180 / Maths.PI));
    }

    public static float cosRotPos(double a)
    {
        float angle = Maths.cos(a);
        return (angle * (180 / Maths.PI));
    }

    public static float tanRotPos(double a)
    {
        float angle = Maths.tan(a);
        return (angle * (180 / Maths.PI));
    }

    public static float asinRotPos(double a)
    {
        float angle = Maths.asin(a);
        return (angle * (180 / Maths.PI));
    }

    public static float acosRotPos(double a)
    {
        float angle = Maths.acos(a);
        return (angle * (180 / Maths.PI));
    }

    public static float atanRotPos(double a)
    {
        float angle = Maths.atan(a);
        return (angle * (180 / Maths.PI));
    }

    public static float atan2RotPos(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return (angle * (180 / Maths.PI));
    }

    // rotation towards radians to degrees

    public static float sinRotNegRadToDeg(double a)
    {
        float angle = Maths.sin(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float cosRotNegRadToDeg(double a)
    {
        float angle = Maths.cos(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float tanRotNegRadToDeg(double a)
    {
        float angle = Maths.tan(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float asinRotNegRadToDeg(double a)
    {
        float angle = Maths.asin(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float acosRotNegRadToDeg(double a)
    {
        float angle = Maths.acos(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float atanRotNegRadToDeg(double a)
    {
        float angle = Maths.atan(a);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float atan2RotNegRadToDeg(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return -(angle * Maths.RADIANS_TO_DEGREES);
    }

    // rotation towards degrees to radians

    public static float sinRotNegDegToRad(double a)
    {
        float angle = Maths.sin(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float cosRotNegDegToRad(double a)
    {
        float angle = Maths.cos(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float tanRotNegDegToRad(double a)
    {
        float angle = Maths.tan(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float asinRotNegDegToRad(double a)
    {
        float angle = Maths.asin(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float acosRotNegDegToRad(double a)
    {
        float angle = Maths.acos(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float atanRotNegDegToRad(double a)
    {
        float angle = Maths.atan(a);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float atan2RotNegDegToRad(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return -(angle * Maths.DEGREES_TO_RADIANS);
    }

    // rotation away radians to degrees

    public static float sinRotPosRadToDeg(double a)
    {
        float angle = Maths.sin(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float cosRotPosRadToDeg(double a)
    {
        float angle = Maths.cos(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float tanRotPosRadToDeg(double a)
    {
        float angle = Maths.tan(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float asinRotPosRadToDeg(double a)
    {
        float angle = Maths.asin(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float acosRotPosRadToDeg(double a)
    {
        float angle = Maths.acos(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float atanRotPosRadToDeg(double a)
    {
        float angle = Maths.atan(a);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    public static float atan2RotPosRadToDeg(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return (angle * Maths.RADIANS_TO_DEGREES);
    }

    // rotation away degrees to radians

    public static float sinRotPosDegToRad(double a)
    {
        float angle = Maths.sin(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float cosRotPosDegToRad(double a)
    {
        float angle = Maths.cos(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float tanRotPosDegToRad(double a)
    {
        float angle = Maths.tan(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float asinRotPosDegToRad(double a)
    {
        float angle = Maths.asin(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float acosRotPosDegToRad(double a)
    {
        float angle = Maths.acos(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float atanRotPosDegToRad(double a)
    {
        float angle = Maths.atan(a);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    public static float atan2RotPosDegToRad(double a, double b)
    {
        float angle = Maths.atan2(a, b);
        return (angle * Maths.DEGREES_TO_RADIANS);
    }

    // wrapping to degrees

    public static float wrap(double a)
    {
        float value = (float) a % 360;

        if (value >= 180)
        {
            value -= 360;
        }

        if (value < -180)
        {
            value += 360;
        }

        return value;
    }

    // factorials, additorials, etc

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