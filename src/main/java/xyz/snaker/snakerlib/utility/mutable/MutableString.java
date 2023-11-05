package xyz.snaker.snakerlib.utility.mutable;

/**
 * Created by SnakerBone on 5/11/2023
 **/
public class MutableString implements Mutable<String>
{
    private String value;

    public MutableString() {}

    public MutableString(String value)
    {
        this.value = value;
    }

    @Override
    public String get()
    {
        return value;
    }

    @Override
    public boolean set(String value)
    {
        this.value = value;
        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj instanceof MutableString other) {
            return value.equals(other.value);
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return value == null ? 0 : value.hashCode();
    }

    @Override
    public String toString()
    {
        return value == null ? "null" : value;
    }
}
