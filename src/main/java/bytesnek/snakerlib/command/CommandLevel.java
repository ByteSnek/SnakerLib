package bytesnek.snakerlib.command;

/**
 * Created by SnakerBone on 26/11/2023
 **/
public enum CommandLevel
{
    NONE(0),
    MODERATOR(1),
    MASTER(2),
    ADMIN(3),
    OWNER(4);

    private final int value;

    CommandLevel(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
