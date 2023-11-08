package xyz.snaker.snakerlib.level.levelgen.featurecycle;

import java.io.Serial;
import java.util.List;
import java.util.Map;

import xyz.snaker.snakerlib.utility.Strings;

import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * Created by SnakerBone on 10/13/2023
 * <p>
 * Throws when something occurs while sorting feature cycles in world gen
 *
 * @see FeatureCycleSorter
 **/
public class FeatureCycleSorterException extends RuntimeException
{
    @Serial
    private static final long serialVersionUID = -1L;

    public FeatureCycleSorterException(Map<FeatureCycleSorter.FeatureData, Map<FeatureCycleSorter.BiomeData, IntSet>> tracebacks, List<FeatureCycleSorter.FeatureData> cycle)
    {
        super(Strings.formatFeatureCycleErrorMessage(tracebacks, cycle));
    }
}