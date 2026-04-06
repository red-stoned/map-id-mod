package com.holebois.showmapid.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrame>
extends EntityRenderer<T, ItemFrameRenderState> {
	protected ItemFrameEntityRendererMixin(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

    @Inject(at = @At("HEAD"), method = "shouldShowName", cancellable = true)
    public void label(T itemFrameEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        Boolean show =  Minecraft.renderNames() && 
            !itemFrameEntity.getItem().isEmpty() && 
            this.entityRenderDispatcher.crosshairPickEntity == itemFrameEntity &&
            (
                itemFrameEntity.getItem().has(DataComponents.CUSTOM_NAME) ||
                (
                    itemFrameEntity.getItem().has(DataComponents.MAP_ID) &&
                    Minecraft.getInstance().player.isShiftKeyDown()
                )
            ); 
        cir.setReturnValue(show);
    }

    @Inject(at = @At("HEAD"), method = "getNameTag", cancellable = true)
    public void name(T itemFrameEntity, CallbackInfoReturnable<Component> cir) {
        if (itemFrameEntity.getItem().has(DataComponents.MAP_ID) && Minecraft.getInstance().player.isShiftKeyDown()) {
            cir.setReturnValue(itemFrameEntity.getItem().getHoverName().copy().append(" <ID:" + itemFrameEntity.getItem().get(DataComponents.MAP_ID).id() + ">"));
        }
     }
}
