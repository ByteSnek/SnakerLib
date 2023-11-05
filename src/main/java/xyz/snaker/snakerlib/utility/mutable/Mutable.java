package xyz.snaker.snakerlib.utility.mutable;

/**
 * Created by SnakerBone on 5/11/2023
 **/
public interface Mutable<V>
{
    V get();

    boolean set(V value);
}
