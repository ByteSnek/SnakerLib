package xyz.snaker.snakerlib.internal.mixin;

import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by SnakerBone on 13/10/2023
 **/
@Mixin(BiomeSpecialEffects.class)
public interface BiomeSpecialEffectsAccessor
{
    @Invoker("<init>")
    static BiomeSpecialEffects snakerlib$new(int fogColor, int waterColor, int waterFogColor, int skyColor, Optional<Integer> foliageColorOverride_, Optional<Integer> grassColorOverride, BiomeSpecialEffects.GrassColorModifier grassColorModifier, Optional<AmbientParticleSettings> ambientParticleSettings, Optional<Holder<SoundEvent>> ambientLoopSoundEvent, Optional<AmbientMoodSettings> ambientMoodSettings, Optional<AmbientAdditionsSettings> ambientAdditionsSettings, Optional<Music> backgroundMusic)
    {
        throw new AssertionError();
    }
}
