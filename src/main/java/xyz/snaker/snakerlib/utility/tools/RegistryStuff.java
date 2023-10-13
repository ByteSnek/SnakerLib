package xyz.snaker.snakerlib.utility.tools;

import java.io.Reader;
import java.util.*;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;

import org.jetbrains.annotations.NotNull;

/**
 * Created by SnakerBone on 10/13/2023
 **/
public class RegistryStuff
{
    public record RegistryDataPair<R>(RegistryDataLoader.RegistryData<R> data, MappedRegistry<R> registry)
    {
        public static <R> RegistryDataPair<R> create(RegistryDataLoader.RegistryData<R> data)
        {
            return new RegistryDataPair<>(data, new MappedRegistry<>(data.key(), Lifecycle.stable()));
        }
    }

    public static RegistryAccess.Frozen loadAllRegistryData(ResourceManager resourceManager, RegistryAccess registryAccess, List<RegistryDataLoader.RegistryData<?>> registryData)
    {
        List<? extends RegistryDataPair<?>> registryPairs = registryData.stream().map(RegistryDataPair::create).toList();
        Map<ResourceKey<? extends Registry<?>>, RegistryOps.RegistryInfo<?>> registryInfo = new HashMap<>();

        registryAccess.registries().forEach(registryEntry -> registryInfo.put(registryEntry.key(), createImmutableRegistryInfo(registryEntry.value())));
        registryPairs.forEach(pair -> registryInfo.put(pair.registry().key(), createMutableRegistryInfo(pair.registry())));

        RegistryOps.RegistryInfoLookup registryInfoLookup = new RegistryOps.RegistryInfoLookup()
        {
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public <T> @NotNull Optional<RegistryOps.RegistryInfo<T>> lookup(@NotNull ResourceKey<? extends Registry<? extends T>> resourceKey)
            {
                return Optional.ofNullable((RegistryOps.RegistryInfo) registryInfo.get(resourceKey));
            }
        };

        List<String> errors = new ArrayList<>();

        for (RegistryDataPair<?> pair : registryPairs) {
            loadRegistryData(resourceManager, registryInfoLookup, pair, errors);
        }

        boolean anyFreezeErrors = false;

        for (RegistryDataPair<?> pair : registryPairs) {
            try {
                pair.registry().freeze();
            } catch (IllegalStateException e) {
                if (!anyFreezeErrors) {
                    anyFreezeErrors = true;
                    errors.add("\n\nErrors occurred freezing registries.\nThese were elements that were referenced, but never defined (or their definition had an error above).\n\n");
                }

                String unboundElementPrefix = "Unbound values in registry " + pair.registry.key() + ": [";

                if (e.getMessage().startsWith(unboundElementPrefix)) {
                    errors.add("Missing references from the " + pair.registry.key().location().getPath() + " registry: [\n\t'" + e.getMessage().substring(unboundElementPrefix.length(), e.getMessage().length() - 1).replaceAll(", ", "',\n\t'") + "'\n]\n\n");
                } else {
                    errors.add(e.getMessage());
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalStateException("Error(s) loading registries:\n" + String.join("", errors));
        }

        return new RegistryAccess.ImmutableRegistryAccess(registryPairs.stream().map(RegistryDataPair::registry).toList()).freeze();
    }

    public static <R> void loadRegistryData(ResourceManager resourceManager, RegistryOps.RegistryInfoLookup registryInfoLookup, RegistryDataPair<R> pair, List<String> errors)
    {
        ResourceKey<? extends Registry<R>> registryKey = pair.data.key();

        String registryName = registryKey.location().getPath();
        FileToIdConverter fileToIdConverter = FileToIdConverter.json(registryName);
        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryInfoLookup);

        List<String> errorsInRegistry = new ArrayList<>();

        for (Map.Entry<ResourceLocation, Resource> entry : fileToIdConverter.listMatchingResources(resourceManager).entrySet()) {
            ResourceLocation entryId = entry.getKey();
            ResourceKey<R> entryKey = ResourceKey.create(registryKey, fileToIdConverter.fileToId(entryId));
            Resource entryResource = entry.getValue();

            try {
                Reader reader = entryResource.openAsReader();
                JsonElement json = JsonParser.parseReader(reader);
                DataResult<R> dataResult = pair.data().elementCodec().parse(registryOps, json);

                dataResult.result().ifPresent(result -> pair.registry().register(entryKey, result, entryResource.isBuiltin() ? Lifecycle.stable() : dataResult.lifecycle()));
                dataResult.error().ifPresent(error -> errorsInRegistry.add(appendErrorLocation(appendErrorLocation(error.message(), registryName + " '" + entryId + "'"), "pack '" + entryResource.sourcePackId() + "'")));
            } catch (Exception e) {
                errorsInRegistry.add(appendErrorLocation(appendErrorLocation("External error occurred: " + e.getMessage(), registryName + " '" + entryId + "'"), "pack '" + entryResource.sourcePackId() + "'"));
            }
        }

        if (!errorsInRegistry.isEmpty()) {
            final StringBuilder builder = new StringBuilder("\n\nErrors(s) loading registry ")
                    .append(registryKey.location())
                    .append(":\n\n");
            for (String error : errorsInRegistry) {
                builder.append(ensureNewLineSuffix(error.replaceAll("; ", "\n")));
            }
            errors.add(builder.toString());
        }
    }

    private static <T> RegistryOps.RegistryInfo<T> createMutableRegistryInfo(WritableRegistry<T> registry)
    {
        return new RegistryOps.RegistryInfo<>(registry.asLookup(), registry.createRegistrationLookup(), registry.registryLifecycle());
    }

    private static <T> RegistryOps.RegistryInfo<T> createImmutableRegistryInfo(Registry<T> registry)
    {
        return new RegistryOps.RegistryInfo<>(registry.asLookup(), registry.asTagAddingLookup(), registry.registryLifecycle());
    }

    public static <E> DataResult<E> appendRegistryError(DataResult<E> result, ResourceKey<? extends Registry<?>> registry)
    {
        return result.mapError(e -> appendErrorLocation(e, "registry " + registry.location()));
    }

    public static <E> DataResult<E> appendRegistryReferenceError(DataResult<E> result, ResourceLocation id, ResourceKey<? extends Registry<?>> registry)
    {
        return result.mapError(e -> appendErrorLocation(e, "reference to \"" + id + "\" from " + registry.location()));
    }

    public static String appendErrorLocation(String error, String at)
    {
        return ensureNewLineSuffix(error) + "\tat: " + at;
    }

    private static String ensureNewLineSuffix(String s)
    {
        return s.endsWith("\n") ? s : s + "\n";
    }
}
