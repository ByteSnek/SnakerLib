package bytesnek.snakerlib.resources;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;

import com.mojang.serialization.Codec;

/**
 * Created by SnakerBone on 19/05/2023
 * <p>
 * Used for getting the ResourceLocation / namespace / path / registry name of most game objects
 **/
public class ResourceLocations
{
    public static final ResourceEntry<GameEvent> GAME_EVENT = of(BuiltInRegistries.GAME_EVENT);
    public static final ResourceEntry<SoundEvent> SOUND_EVENT = of(BuiltInRegistries.SOUND_EVENT);
    public static final ResourceEntry<Fluid> FLUID = of(BuiltInRegistries.FLUID);
    public static final ResourceEntry<MobEffect> MOB_EFFECT = of(BuiltInRegistries.MOB_EFFECT);
    public static final ResourceEntry<Block> BLOCK = of(BuiltInRegistries.BLOCK);
    public static final ResourceEntry<Enchantment> ENCHANTMENT = of(BuiltInRegistries.ENCHANTMENT);
    public static final ResourceEntry<EntityType<?>> ENTITY_TYPE = of(BuiltInRegistries.ENTITY_TYPE);
    public static final ResourceEntry<Item> ITEM = of(BuiltInRegistries.ITEM);
    public static final ResourceEntry<Potion> POTION = of(BuiltInRegistries.POTION);
    public static final ResourceEntry<ParticleType<?>> PARTICLE_TYPE = of(BuiltInRegistries.PARTICLE_TYPE);
    public static final ResourceEntry<BlockEntityType<?>> BLOCK_ENTITY_TYPE = of(BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final ResourceEntry<PaintingVariant> PAINTING_VARIANT = of(BuiltInRegistries.PAINTING_VARIANT);
    public static final ResourceEntry<ResourceLocation> CUSTOM_STAT = of(BuiltInRegistries.CUSTOM_STAT);
    public static final ResourceEntry<ChunkStatus> CHUNK_STATUS = of(BuiltInRegistries.CHUNK_STATUS);
    public static final ResourceEntry<RuleTestType<?>> RULE_TEST = of(BuiltInRegistries.RULE_TEST);
    public static final ResourceEntry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = of(BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
    public static final ResourceEntry<PosRuleTestType<?>> POS_RULE_TEST = of(BuiltInRegistries.POS_RULE_TEST);
    public static final ResourceEntry<MenuType<?>> MENU = of(BuiltInRegistries.MENU);
    public static final ResourceEntry<RecipeType<?>> RECIPE_TYPE = of(BuiltInRegistries.RECIPE_TYPE);
    public static final ResourceEntry<RecipeSerializer<?>> RECIPE_SERIALIZER = of(BuiltInRegistries.RECIPE_SERIALIZER);
    public static final ResourceEntry<Attribute> ATTRIBUTE = of(BuiltInRegistries.ATTRIBUTE);
    public static final ResourceEntry<PositionSourceType<?>> POSITION_SOURCE_TYPE = of(BuiltInRegistries.POSITION_SOURCE_TYPE);
    public static final ResourceEntry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPE = of(BuiltInRegistries.COMMAND_ARGUMENT_TYPE);
    public static final ResourceEntry<StatType<?>> STAT_TYPE = of(BuiltInRegistries.STAT_TYPE);
    public static final ResourceEntry<VillagerType> VILLAGER_TYPE = of(BuiltInRegistries.VILLAGER_TYPE);
    public static final ResourceEntry<VillagerProfession> VILLAGER_PROFESSION = of(BuiltInRegistries.VILLAGER_PROFESSION);
    public static final ResourceEntry<PoiType> POINT_OF_INTEREST_TYPE = of(BuiltInRegistries.POINT_OF_INTEREST_TYPE);
    public static final ResourceEntry<MemoryModuleType<?>> MEMORY_MODULE_TYPE = of(BuiltInRegistries.MEMORY_MODULE_TYPE);
    public static final ResourceEntry<SensorType<?>> SENSOR_TYPE = of(BuiltInRegistries.SENSOR_TYPE);
    public static final ResourceEntry<Schedule> SCHEDULE = of(BuiltInRegistries.SCHEDULE);
    public static final ResourceEntry<Activity> ACTIVITY = of(BuiltInRegistries.ACTIVITY);
    public static final ResourceEntry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = of(BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
    public static final ResourceEntry<LootItemFunctionType> LOOT_FUNCTION_TYPE = of(BuiltInRegistries.LOOT_FUNCTION_TYPE);
    public static final ResourceEntry<LootItemConditionType> LOOT_CONDITION_TYPE = of(BuiltInRegistries.LOOT_CONDITION_TYPE);
    public static final ResourceEntry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = of(BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
    public static final ResourceEntry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = of(BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
    public static final ResourceEntry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = of(BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
    public static final ResourceEntry<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = of(BuiltInRegistries.FLOAT_PROVIDER_TYPE);
    public static final ResourceEntry<IntProviderType<?>> INT_PROVIDER_TYPE = of(BuiltInRegistries.INT_PROVIDER_TYPE);
    public static final ResourceEntry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = of(BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
    public static final ResourceEntry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = of(BuiltInRegistries.BLOCK_PREDICATE_TYPE);
    public static final ResourceEntry<WorldCarver<?>> CARVER = of(BuiltInRegistries.CARVER);
    public static final ResourceEntry<Feature<?>> FEATURE = of(BuiltInRegistries.FEATURE);
    public static final ResourceEntry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = of(BuiltInRegistries.STRUCTURE_PLACEMENT);
    public static final ResourceEntry<StructurePieceType> STRUCTURE_PIECE = of(BuiltInRegistries.STRUCTURE_PIECE);
    public static final ResourceEntry<StructureType<?>> STRUCTURE_TYPE = of(BuiltInRegistries.STRUCTURE_TYPE);
    public static final ResourceEntry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = of(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
    public static final ResourceEntry<BlockStateProviderType<?>> BLOCKSTATE_PROVIDER_TYPE = of(BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE);
    public static final ResourceEntry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = of(BuiltInRegistries.FOLIAGE_PLACER_TYPE);
    public static final ResourceEntry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = of(BuiltInRegistries.TRUNK_PLACER_TYPE);
    public static final ResourceEntry<RootPlacerType<?>> ROOT_PLACER_TYPE = of(BuiltInRegistries.ROOT_PLACER_TYPE);
    public static final ResourceEntry<TreeDecoratorType<?>> TREE_DECORATOR_TYPE = of(BuiltInRegistries.TREE_DECORATOR_TYPE);
    public static final ResourceEntry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = of(BuiltInRegistries.FEATURE_SIZE_TYPE);
    public static final ResourceEntry<Codec<? extends BiomeSource>> BIOME_SOURCE = of(BuiltInRegistries.BIOME_SOURCE);
    public static final ResourceEntry<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = of(BuiltInRegistries.CHUNK_GENERATOR);
    public static final ResourceEntry<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = of(BuiltInRegistries.MATERIAL_CONDITION);
    public static final ResourceEntry<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = of(BuiltInRegistries.MATERIAL_RULE);
    public static final ResourceEntry<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = of(BuiltInRegistries.DENSITY_FUNCTION_TYPE);
    public static final ResourceEntry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = of(BuiltInRegistries.STRUCTURE_PROCESSOR);
    public static final ResourceEntry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = of(BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
    public static final ResourceEntry<CatVariant> CAT_VARIANT = of(BuiltInRegistries.CAT_VARIANT);
    public static final ResourceEntry<FrogVariant> FROG_VARIANT = of(BuiltInRegistries.FROG_VARIANT);
    public static final ResourceEntry<BannerPattern> BANNER_PATTERN = of(BuiltInRegistries.BANNER_PATTERN);
    public static final ResourceEntry<Instrument> INSTRUMENT = of(BuiltInRegistries.INSTRUMENT);
    public static final ResourceEntry<String> DECORATED_POT_PATTERNS = of(BuiltInRegistries.DECORATED_POT_PATTERNS);
    public static final ResourceEntry<CreativeModeTab> CREATIVE_MODE_TAB = of(BuiltInRegistries.CREATIVE_MODE_TAB);

    static <T> ResourceEntry<T> of(Registry<T> registry)
    {
        return new ResourceEntry<>()
        {
            @Override
            public ResourceLocation getResourceLocation(T key)
            {
                return registry.getKey(key);
            }

            @Override
            public String getPath(T key)
            {
                return getResourceLocation(key).getPath();
            }

            @Override
            public String getNamespace(T key)
            {
                return getResourceLocation(key).getNamespace();
            }

            @Override
            public String getRegistryName(T key)
            {
                return getResourceLocation(key).toString();
            }

            @Override
            public T get(ResourceLocation value)
            {
                return registry.get(value);
            }
        };
    }
}