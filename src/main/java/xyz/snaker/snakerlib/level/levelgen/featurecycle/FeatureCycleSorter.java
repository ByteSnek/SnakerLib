package xyz.snaker.snakerlib.level.levelgen.featurecycle;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import xyz.snaker.snakerlib.internal.ImpossibleExecutionException;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.apache.commons.lang3.mutable.MutableInt;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

/**
 * Created by SnakerBone on 13/10/2023
 **/
public class FeatureCycleSorter
{
    /**
     * Creates features in the correct order
     *
     * @param allBiomes     The biomes to generate
     * @param biomeFeatures The biome feature setter for each biome
     * @return A sorted list of biome feature steps
     **/
    public static List<FeatureSorter.StepFeatureData> createFeatures(List<Holder<Biome>> allBiomes, Function<Holder<Biome>, List<HolderSet<PlacedFeature>>> biomeFeatures)
    {
        boolean calledFromTopLevel = true;

        Reference2IntMap<PlacedFeature> featureToIntIdMap = new Reference2IntOpenHashMap<>();
        Reference2IntMap<Biome> biomeToIntIdMap = new Reference2IntOpenHashMap<>();

        MutableInt nextFeatureId = new MutableInt(0);
        MutableInt nextBiomeId = new MutableInt(0);

        Comparator<FeatureData> compareByStepThenByIndex = Comparator.comparingInt(FeatureData::step).thenComparingInt(FeatureData::featureId);
        Map<FeatureData, Set<FeatureData>> nodesToChildren = new TreeMap<>(compareByStepThenByIndex);

        int maxSteps = 0;

        Map<FeatureData, Map<BiomeData, IntSet>> nodesToTracebacks = new HashMap<>();

        for (Holder<Biome> biomeHolder : allBiomes) {
            Biome biome = biomeHolder.value();
            List<FeatureData> flatDataList = new ArrayList<>();
            List<HolderSet<PlacedFeature>> features = biomeFeatures.apply(biomeHolder);

            maxSteps = Math.max(maxSteps, features.size());

            for (int stepIndex = 0; stepIndex < features.size(); ++stepIndex) {
                int biomeIndex = 0;

                for (Holder<PlacedFeature> featureHolder : features.get(stepIndex)) {
                    PlacedFeature feature = featureHolder.value();
                    FeatureData featureIdentity = new FeatureData(idFor(feature, featureToIntIdMap, nextFeatureId), stepIndex, feature, featureHolder);

                    flatDataList.add(featureIdentity);

                    BiomeData biomeIdentity = new BiomeData(idFor(biome, biomeToIntIdMap, nextBiomeId), biome, biomeHolder);

                    nodesToTracebacks.computeIfAbsent(featureIdentity, key -> new HashMap<>(1)).computeIfAbsent(biomeIdentity, key -> new IntOpenHashSet()).add(biomeIndex);
                    biomeIndex++;
                }
            }

            for (int featureIndex = 0; featureIndex < flatDataList.size(); ++featureIndex) {
                Set<FeatureData> children = nodesToChildren.computeIfAbsent(flatDataList.get(featureIndex), key -> new TreeSet<>(compareByStepThenByIndex));

                if (featureIndex < flatDataList.size() - 1) {
                    children.add(flatDataList.get(featureIndex + 1));
                }
            }
        }

        Set<FeatureData> nonCyclicalNodes = new TreeSet<>(compareByStepThenByIndex);
        Set<FeatureData> inProgress = new TreeSet<>(compareByStepThenByIndex);
        List<FeatureData> sortedFeatureData = new ArrayList<>();
        List<FeatureData> featureCycle = new ArrayList<>();

        for (FeatureData node : nodesToChildren.keySet()) {
            if (!inProgress.isEmpty()) {
                throw new ImpossibleExecutionException();
            }

            if (!nonCyclicalNodes.contains(node) && depthFirstSearch(nodesToChildren, nonCyclicalNodes, inProgress, sortedFeatureData::add, featureCycle::add, node)) {
                if (featureCycle.size() <= 1) {
                    throw new ImpossibleExecutionException("Found empty cycle node(s)");
                }

                FeatureData loop = featureCycle.get(0);

                for (int i = 1; i < featureCycle.size(); i++) {
                    if (featureCycle.get(i).equals(loop)) {
                        featureCycle = featureCycle.subList(0, i + 1);
                        break;
                    }
                }

                Collections.reverse(featureCycle);

                if (true) {
                    throw new FeatureCycleSorterException(nodesToTracebacks, featureCycle);
                }

                if (!calledFromTopLevel) {
                    throw new FeatureOrderCycleException();
                }

                List<Holder<Biome>> biomeSubset = new ArrayList<>(allBiomes);

                int biomesLeft;

                do {
                    biomesLeft = biomeSubset.size();

                    ListIterator<Holder<Biome>> biomeSubsetIterator = biomeSubset.listIterator();

                    while (biomeSubsetIterator.hasNext()) {
                        Holder<Biome> biome = biomeSubsetIterator.next();

                        biomeSubsetIterator.remove();

                        try {
                            createFeatures(biomeSubset, biomeFeatures);
                        } catch (IllegalStateException e) {
                            continue;
                        }

                        biomeSubsetIterator.add(biome);
                    }

                } while (biomesLeft != biomeSubset.size());

                throw new FeatureOrderCycleException("Feature order cycle found, involved biomes: " + biomeSubset);
            }
        }

        Collections.reverse(sortedFeatureData);

        ImmutableList.Builder<FeatureSorter.StepFeatureData> featuresPerStepData = ImmutableList.builder();

        for (int stepIndex = 0; stepIndex < maxSteps; ++stepIndex) {
            int finalStepIndex = stepIndex;

            List<PlacedFeature> featuresAtStep = sortedFeatureData.stream().filter(arg -> arg.step() == finalStepIndex).map(FeatureData::feature).collect(Collectors.toList());

            int totalIndex = featuresAtStep.size();

            Object2IntMap<PlacedFeature> featureToIndexMapping = new Object2IntOpenCustomHashMap<>(totalIndex, Util.identityStrategy());

            for (int index = 0; index < totalIndex; ++index) {
                featureToIndexMapping.put(featuresAtStep.get(index), index);
            }

            featuresPerStepData.add(new FeatureSorter.StepFeatureData(featuresAtStep, featureToIndexMapping));
        }

        return featuresPerStepData.build();
    }

    /**
     * A recursively called function for discovering generated feature data
     *
     * @param edges                  The feature data map for getting discoverable features
     * @param nonCyclicalNodes       Discoverable nodes that do not need re-visiting
     * @param pathSet                A discoverable set to track the current feature data being recursively processed
     * @param onNonCyclicalNodeFound A non-cyclical node task
     * @param onCyclicalNodeFound    A cyclical node task
     * @param start                  The feature data to start at
     * @return True if data was added to currently active feature data set
     **/
    public static boolean depthFirstSearch(Map<FeatureData, Set<FeatureData>> edges, Set<FeatureData> nonCyclicalNodes, Set<FeatureData> pathSet, Consumer<FeatureData> onNonCyclicalNodeFound, Consumer<FeatureData> onCyclicalNodeFound, FeatureData start)
    {
        if (nonCyclicalNodes.contains(start)) {
            return false;
        } else if (pathSet.contains(start)) {
            onCyclicalNodeFound.accept(start);
            return true;
        } else {
            pathSet.add(start);

            for (FeatureData next : edges.getOrDefault(start, ImmutableSet.of())) {
                if (depthFirstSearch(edges, nonCyclicalNodes, pathSet, onNonCyclicalNodeFound, onCyclicalNodeFound, next)) {
                    onCyclicalNodeFound.accept(start);
                    return true;
                }
            }

            pathSet.remove(start);
            nonCyclicalNodes.add(start);
            onNonCyclicalNodeFound.accept(start);

            return false;
        }
    }

    /**
     * Gets the id for an object or increments it
     *
     * @return The id of the object
     **/
    private static <T> int idFor(T object, Reference2IntMap<T> objectToIntIdMap, MutableInt nextId)
    {
        return objectToIntIdMap.computeIfAbsent(object, key -> nextId.getAndIncrement());
    }

    /**
     * A feature data record container
     *
     * @param featureId The feature id
     * @param step      The feature step
     * @param feature   A reference of a placed feature
     * @param source    The placed features in this feature data
     **/
    public record FeatureData(int featureId, int step, PlacedFeature feature, Holder<PlacedFeature> source)
    {
        /**
         * Gets the mapped name of this feature data
         *
         * @return The mapped name of this feature data
         **/
        public String name()
        {
            return source.unwrap().map(e -> e.location().toString(), e -> "[Inline feature: " + feature + "]");
        }
    }

    /**
     * A biome data record container
     *
     * @param biomeId The biome id
     * @param biome   A reference of a biome
     * @param source  The biomes in this biome data
     **/
    public record BiomeData(int biomeId, Biome biome, Holder<Biome> source)
    {
        /**
         * Gets the mapped name of this biome data
         *
         * @return The mapped name of this biome data
         **/
        public String name()
        {
            return source.unwrap().map(e -> e.location().toString(), e -> "[Inline biome: " + biome + "]");
        }
    }
}
