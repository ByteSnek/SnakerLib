package xyz.snaker.snakerlib.utility;

/**
 * Created by SnakerBone on 26/09/2023
 **/
public enum TriBool
{
    YES(1),
    NO(0),
    MAYBE(-1);

    private final Boolean value;

    TriBool(int value)
    {
        this.value = setValue(value);
    }

    public Boolean getValue()
    {
        return value;
    }

    private Boolean setValue(int value)
    {
        return switch (value) {
            case 1 -> true;
            case 0 -> false;
            case -1 -> null;
            default -> throw new RuntimeException("Enum accessed or instantiated illegally");
        };
    }

    @Override
    public String toString()
    {
        if (value == null) {
            return "maybe";
        }
        return value ? "true" : "false";
    }
}
