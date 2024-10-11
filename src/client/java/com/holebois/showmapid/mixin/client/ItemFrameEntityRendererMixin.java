package com.holebois.showmapid.mixin.client;

import net.minecraft.client.MinecraftClient;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	@Inject(at = @At("TAIL"), method = "updateRenderState")
	public void updateRenderState(T itemFrameEntity, ItemFrameEntityRenderState itemFrameEntityRenderState, float f, CallbackInfo ci) {
        ItemStack itemstack = itemFrameEntityRenderState.contents;
        
		if (this.dispatcher.targetedEntity == itemFrameEntity && itemstack.contains(DataComponentTypes.MAP_ID) && MinecraftClient.getInstance().player.isSneaking()) {
            Text text = itemstack.getName();
			int mapid = itemFrameEntityRenderState.mapId.id();
			itemFrameEntityRenderState.displayName = text.copy().append(" <ID:" + mapid + ">");
		}
	}
}
