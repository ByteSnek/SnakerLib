package snaker.snakerlib.internal;

/**
 * Created by SnakerBone on 29/05/2023
 **/
public enum MarkerType
{
    NONE(null),
    MESSAGE("[MESSAGE]: "),
    INFO("[INFO]: "),
    DEBUG("[DEBUG]: "),
    CATCHING("[CATCHING]: "),
    SYSTEM("[SYSTEM]: "),
    THREAD("[THREAD]: "),
    WORKER("[WORKER]: "),
    TRACE("[TRACE]: "),
    DEV("[DEV]: "),
    WARN("[WARN]: "),
    ERROR("[ERROR]: "),
    FATAL("[FATAL]: ");

    final String value;

    MarkerType(String value)
    {
        this.value = value;
    }

    public String get()
    {
        return value == null ? "" : value;
    }
}
