package xyz.snaker.snakerlib.internal;

import java.util.function.Function;

/**
 * Created by SnakerBone on 23/09/2023
 **/
public class PackedPrimitives
{
    public static final Function<Boolean, boolean[]> BOOLEAN = bool -> new boolean[]{bool};
    public static final Function<Byte, byte[]> BYTE = b -> new byte[]{b};
    public static final Function<Short, short[]> SHORT = s -> new short[]{s};
    public static final Function<Integer, int[]> INTEGER = i -> new int[]{i};
    public static final Function<Long, long[]> LONG = l -> new long[]{l};
    public static final Function<Float, float[]> FLOAT = f -> new float[]{f};
    public static final Function<Double, double[]> DOUBLE = d -> new double[]{d};

    public static final boolean[] TRUE = BOOLEAN.apply(true);
    public static final boolean[] FALSE = BOOLEAN.apply(false);
}
