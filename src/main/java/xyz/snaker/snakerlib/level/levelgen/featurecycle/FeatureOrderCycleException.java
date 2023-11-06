package xyz.snaker.snakerlib.level.levelgen.featurecycle;

import java.io.Serial;

/**
 * Created by SnakerBone on 10/13/2023
 * <p>
 * Throws when a feature order cycle is found while generating the world
 **/
public class FeatureOrderCycleException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = -1L;

    public FeatureOrderCycleException(String message)
    {
        super(message);
    }

    public FeatureOrderCycleException()
    {
        super("Feature order cycle found");
    }
}
