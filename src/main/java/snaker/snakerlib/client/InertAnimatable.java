package snaker.snakerlib.client;

import snaker.snakerlib.SnakerLib;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Created by SnakerBone on 18/03/2023
 * <p>
 * Used for utilising Geckolib's rendering instead of animations
 **/
public interface InertAnimatable extends GeoAnimatable
{
    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar registrar)
    {
        registrar.add(new AnimationController<>(this, "controller", 0, this::none));
    }

    @Override
    default AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    default double getTick(Object o)
    {
        return SnakerLib.getVMTickCount();
    }

    default <T extends GeoAnimatable> PlayState none(AnimationState<T> state)
    {
        return PlayState.CONTINUE;
    }
}