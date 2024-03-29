package xyz.snaker.snakerlib.client.render;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Created by SnakerBone on 13/06/2023
 **/
public abstract class CyclicalIconRenderer extends PreppedRenderer
{
    /**
     * Logs an error only once if anything does occur
     **/
    protected static volatile boolean notifyError;

    public CyclicalIconRenderer(BlockEntityRenderDispatcher renderDispatcher, EntityModelSet modelSet)
    {
        super(renderDispatcher, modelSet);
        notifyError = true;
    }

    @Override
    public abstract void renderByItem(@NotNull ItemStack itemStack, @NotNull ItemDisplayContext context, @NotNull PoseStack stack, @NotNull MultiBufferSource source, int packedLight, int packedOverlay);
}
