package xyz.snaker.snakerlib.utility.tools;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Function;

import xyz.snaker.snakerlib.SnakerLib;
import xyz.snaker.snakerlib.codec.Codecs;
import xyz.snaker.snakerlib.codec.ShapedCodec;
import xyz.snaker.snakerlib.internal.mixin.BiomeGenerationSettingsAccessor;
import xyz.snaker.snakerlib.internal.mixin.BiomeSpecialEffectsAccessor;
import xyz.snaker.snakerlib.internal.mixin.SinglePoolElementAccessor;
import xyz.snaker.snakerlib.math.Maths;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.RegistryObject;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Created by SnakerBone on 4/06/2023
 **/
public class WorldStuff
{
    static final GenerationStep.Decoration[] DECORATION_STEPS = GenerationStep.Decoration.values();
    static Codec<Holder<StructureProcessorList>> STRUCTURE_PROCESSOR_LIST_CODEC;

    /**
     * Adds a biome spawn
     *
     * @param builder  The spawn settings builder
     * @param category The mob category
     * @param entity   The entity
     * @param weight   The likeliness of the spawn to happen
     * @param minCount The minimum mob count of each spawn
     * @param maxCount The maximum mob count of each spawn
     * @return The builder
     **/
    public static <T extends LivingEntity> MobSpawnSettings.Builder addBiomeSpawn(MobSpawnSettings.Builder builder, MobCategory category, RegistryObject<EntityType<T>> entity, int weight, int minCount, int maxCount)
    {
        return builder.addSpawn(category, new MobSpawnSettings.SpawnerData(entity.get(), weight, minCount, maxCount));
    }

    /**
     * Checks the current dimension
     *
     * @param level  The current level accessor
     * @param wanted The resource key of the wanted dimension
     * @return True if the current dimension is the wanted dimension
     **/
    public static boolean isDimension(ServerLevelAccessor level, ResourceKey<Level> wanted)
    {
        return level.getLevel().dimension().equals(wanted);
    }

    public static boolean isBiome(ServerLevelAccessor level, BlockPos pos, ResourceKey<Biome> wanted)
    {
        return level.getLevel().getBiome(pos).is(wanted);
    }

    public static boolean isOverworld(ServerLevelAccessor level)
    {
        return isDimension(level, Level.OVERWORLD);
    }

    public static boolean isNether(ServerLevelAccessor level)
    {
        return isDimension(level, Level.NETHER);
    }

    public static boolean isEnd(ServerLevelAccessor level)
    {
        return isDimension(level, Level.END);
    }

    public static boolean canSeeSky(ServerLevelAccessor level, BlockPos pos)
    {
        return isOverworld(level) && level.getLevel().canSeeSky(pos);
    }

    public static boolean isDay(ServerLevelAccessor level)
    {
        return level.getLevel().isDay();
    }

    public static boolean isNight(ServerLevelAccessor level)
    {
        return level.getLevel().isNight();
    }

    public static boolean hasDaylightCycle(ServerLevelAccessor level)
    {
        return level.getLevel().dimensionType().bedWorks() && !level.getLevel().dimensionType().hasFixedTime();
    }

    public static boolean random(@Nullable RandomSource random, int bound)
    {
        RandomSource source = random == null ? RandomSource.create() : random;
        return source.nextInt(bound) == 0;
    }

    /**
     * Checks the current dimension
     *
     * @param level  The current level
     * @param wanted The resource key of the wanted dimension
     * @return True if the current dimension is the wanted dimension
     **/
    public static boolean isDimension(Level level, ResourceKey<Level> wanted)
    {
        return level.dimension().equals(wanted);
    }

    public static <T extends Entity> AABB getWorldBoundingBox(T entity)
    {
        return new AABB(entity.blockPosition()).inflate(Maths.LEVEL_AABB_RADIUS);
    }

    public static Direction getRandomHorizontalDirection(RandomSource random)
    {
        Direction[] directions = Direction.stream().filter(dir -> !(dir == Direction.UP || dir == Direction.DOWN)).toArray(Direction[]::new);
        return directions[random.nextInt(directions.length)];
    }

    public static Direction getRandomVerticalDirection(RandomSource random)
    {
        Direction[] directions = Direction.stream().filter(dir -> !(dir == Direction.NORTH || dir == Direction.SOUTH || dir == Direction.EAST || dir == Direction.WEST)).toArray(Direction[]::new);
        return directions[random.nextInt(directions.length)];
    }

    public static <L extends LevelReader> boolean isFreeAroundPos(L level, BlockPos pos, boolean vertical)
    {
        boolean checkVertical = !vertical || (state(level, pos.above()).is(Blocks.AIR) && state(level, pos.below()).is(Blocks.AIR));
        return state(level, pos.north()).is(Blocks.AIR) && state(level, pos.south()).is(Blocks.AIR) && state(level, pos.east()).is(Blocks.AIR) && state(level, pos.west()).is(Blocks.AIR) && checkVertical;
    }

    private static <L extends LevelReader> BlockState state(L level, BlockPos pos)
    {
        return level.getBlockState(pos);
    }

    public static Codec<Biome> makeBiomeCodec()
    {
        Codec<BiomeSpecialEffects> specialEffectsCodec =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                Codec.INT.fieldOf("fog_color").forGetter(BiomeSpecialEffects::getFogColor),
                                Codec.INT.fieldOf("water_color").forGetter(BiomeSpecialEffects::getWaterColor),
                                Codec.INT.fieldOf("water_fog_color").forGetter(BiomeSpecialEffects::getWaterFogColor),
                                Codec.INT.fieldOf("sky_color").forGetter(BiomeSpecialEffects::getSkyColor),
                                Codecs.newOptionalFieldOfCodec(Codec.INT, "foliage_color").forGetter(BiomeSpecialEffects::getFoliageColorOverride),
                                Codecs.newOptionalFieldOfCodec(Codec.INT, "grass_color").forGetter(BiomeSpecialEffects::getGrassColorOverride),
                                Codecs.newOptionalFieldOfCodec(Codecs.newEnumCodec("grass color modifier", BiomeSpecialEffects.GrassColorModifier::values), "grass_color_modifier", BiomeSpecialEffects.GrassColorModifier.NONE).forGetter(BiomeSpecialEffects::getGrassColorModifier),
                                Codecs.newOptionalFieldOfCodec(AmbientParticleSettings.CODEC, "particle").forGetter(BiomeSpecialEffects::getAmbientParticleSettings),
                                Codecs.newOptionalFieldOfCodec(SoundEvent.CODEC, "ambient_sound").forGetter(BiomeSpecialEffects::getAmbientLoopSoundEvent),
                                Codecs.newOptionalFieldOfCodec(AmbientMoodSettings.CODEC, "mood_sound").forGetter(BiomeSpecialEffects::getAmbientMoodSettings),
                                Codecs.newOptionalFieldOfCodec(AmbientAdditionsSettings.CODEC, "additions_sound").forGetter(BiomeSpecialEffects::getAmbientAdditionsSettings),
                                Codecs.newOptionalFieldOfCodec(Music.CODEC, "music").forGetter(BiomeSpecialEffects::getBackgroundMusic)
                        ).apply(instance, BiomeSpecialEffectsAccessor::snakerlib$new)
                );

        Codec<PlacedFeature> placedFeatureCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codecs.newErrorReportingCodec(ConfiguredFeature.CODEC.fieldOf("feature"), "feature").forGetter(PlacedFeature::feature),
                        Codecs.newErrorReportingCodec(Codecs.newListCodec(PlacementModifier.CODEC).fieldOf("placement"), "placement").forGetter(PlacedFeature::placement)
                ).apply(instance, PlacedFeature::new));

        Codec<HolderSet<PlacedFeature>> placedFeatureListCodec = Codecs.newRegistryEntryListCodec(Registries.PLACED_FEATURE, placedFeatureCodec);

        MapCodec<BiomeGenerationSettings> biomeGenerationSettingsCodec = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codecs.newErrorReportingCodec(Codec.simpleMap(GenerationStep.Carving.CODEC, ConfiguredWorldCarver.LIST_CODEC, StringRepresentable.keys(GenerationStep.Carving.values())).fieldOf("carvers"), "carvers").forGetter(c -> ((BiomeGenerationSettingsAccessor) c).snakerlib$getCarvers()),
                        Codecs.newListCodec(placedFeatureListCodec, (e, i) ->
                                {
                                    if (i >= 0 && i < DECORATION_STEPS.length) {
                                        return RegistryStuff.appendErrorLocation(e, "\"features\", step " + DECORATION_STEPS[i].name().toLowerCase(Locale.ROOT) + ", index " + i);
                                    }
                                    return RegistryStuff.appendErrorLocation(e, "\"features\", unknown step, index " + i);
                                }
                        ).fieldOf("features").forGetter(BiomeGenerationSettings::features)
                ).apply(instance, BiomeGenerationSettingsAccessor::snakerlib$new));

        return WorldStuff.makeBiomeCodec(specialEffectsCodec, placedFeatureCodec, biomeGenerationSettingsCodec);
    }

    public static Codec<Biome> makeBiomeCodec(Codec<BiomeSpecialEffects> specialEffectsCodec, Codec<PlacedFeature> placedFeatureCodec, MapCodec<BiomeGenerationSettings> biomeGenerationSettingsCodec)
    {
        final MapCodec<Biome.ClimateSettings> climateSettingsCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codecs.newErrorReportingCodec(Codec.BOOL.fieldOf("has_precipitation"), "has_precipitation").forGetter(Biome.ClimateSettings::hasPrecipitation),
                Codecs.newErrorReportingCodec(Codec.FLOAT.fieldOf("temperature"), "temperature").forGetter(Biome.ClimateSettings::temperature),
                Codecs.newOptionalFieldOfCodec(Codecs.newEnumCodec("temperature modifier", Biome.TemperatureModifier::values), "temperature_modifier", Biome.TemperatureModifier.NONE).forGetter(Biome.ClimateSettings::temperatureModifier),
                Codecs.newErrorReportingCodec(Codec.FLOAT.fieldOf("downfall"), "downfall").forGetter(Biome.ClimateSettings::downfall)
        ).apply(instance, Biome.ClimateSettings::new));

        return RecordCodecBuilder.create(instance -> instance.group(
                climateSettingsCodec.forGetter(b -> b.modifiableBiomeInfo().getOriginalBiomeInfo().climateSettings()),
                Codecs.newErrorReportingCodec(specialEffectsCodec.fieldOf("effects"), "effects").forGetter(b -> b.modifiableBiomeInfo().getOriginalBiomeInfo().effects()),
                biomeGenerationSettingsCodec.forGetter(Biome::getGenerationSettings),
                MobSpawnSettings.CODEC.forGetter(Biome::getMobSettings)
        ).apply(instance, WorldStuff::makeBiome));
    }

    private static Biome makeBiome(Biome.ClimateSettings climateSettings, BiomeSpecialEffects specialEffects, BiomeGenerationSettings generationSettings, MobSpawnSettings mobSpawnSettings)
    {
        return new Biome.BiomeBuilder()
                .downfall(climateSettings.downfall())
                .temperature(climateSettings.temperature())
                .temperatureAdjustment(climateSettings.temperatureModifier())
                .hasPrecipitation(climateSettings.hasPrecipitation())
                .specialEffects(specialEffects)
                .generationSettings(generationSettings)
                .mobSpawnSettings(mobSpawnSettings)
                .build();
    }

    public static WorldGenSettings printWorldGenSettingsError(DataResult<WorldGenSettings> result)
    {
        return result.getOrThrow(false, err -> SnakerLib.LOGGER.error("Error parsing worldgen settings after loading data packs\n" + "(This is usually an error due to invalid dimensions.)\n\n" + err.replaceAll("; ", "\n") + "\n"));
    }

    public static <E extends SinglePoolElement> RecordCodecBuilder<E, Holder<StructureProcessorList>> makeSinglePoolElementProcessorsCodec()
    {
        return Codecs.newErrorReportingCodec(structureProcessorListCodec().fieldOf("processors"), "processors").forGetter(e -> ((SinglePoolElementAccessor) e).snakerlib$getProcessors());
    }

    private static Codec<Holder<StructureProcessorList>> structureProcessorListCodec()
    {
        if (STRUCTURE_PROCESSOR_LIST_CODEC == null) {
            final Codec<StructureProcessorList> listObjectCodec = Codecs.newListCodec(StructureProcessorType.SINGLE_CODEC)
                    .xmap(StructureProcessorList::new, StructureProcessorList::list);
            Codec<StructureProcessorList> directCodec = Codecs.newShapedEitherCodec(
                    ShapedCodec.ofMap(listObjectCodec.fieldOf("processors").codec()),
                    ShapedCodec.ofList(listObjectCodec)
            ).xmap(e -> e.map(Function.identity(), Function.identity()), Either::left);
            STRUCTURE_PROCESSOR_LIST_CODEC = Codecs.newRegistryEntryCodec(Registries.PROCESSOR_LIST, directCodec);
        }
        return STRUCTURE_PROCESSOR_LIST_CODEC;
    }
}
