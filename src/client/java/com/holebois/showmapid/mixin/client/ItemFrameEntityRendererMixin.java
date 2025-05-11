package com.holebois.showmapid.mixin.client;

import net.minecraft.client.MinecraftClient;

import net.minecraft.component.DataComponentTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.render.entity.state.ItemFrameEntityRenderState;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.text.Text;


@SuppressWarnings({"ReassignedVariable", "OverwriteAuthorRequired"})
@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrameEntity>
extends EntityRenderer<T, ItemFrameEntityRenderState> {
	protected ItemFrameEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

    @Inject(at = @At("HEAD"), method = "hasLabel", cancellable = true)
    public void label(T itemFrameEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        Boolean show =  MinecraftClient.isHudEnabled() && 
            !itemFrameEntity.getHeldItemStack().isEmpty() && 
            this.dispatcher.targetedEntity == itemFrameEntity &&
            (
                itemFrameEntity.getHeldItemStack().contains(DataComponentTypes.CUSTOM_NAME) ||
                (
                    itemFrameEntity.getHeldItemStack().contains(DataComponentTypes.MAP_ID) &&
                    MinecraftClient.getInstance().player.isSneaking()
                )
            ); 
        cir.setReturnValue(show);
    }

    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    public void name(T itemFrameEntity, CallbackInfoReturnable<Text> cir) {
        if (itemFrameEntity.getHeldItemStack().contains(DataComponentTypes.MAP_ID) && MinecraftClient.getInstance().player.isSneaking()) {
            cir.setReturnValue(itemFrameEntity.getHeldItemStack().getName().copy().append(" <ID:" + itemFrameEntity.getHeldItemStack().get(DataComponentTypes.MAP_ID).id() + ">"));
        }
     }
}
