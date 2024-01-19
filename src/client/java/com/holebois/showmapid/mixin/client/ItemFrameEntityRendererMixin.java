package com.holebois.showmapid.mixin.client;

import net.minecraft.client.MinecraftClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.text.Text;


@Mixin(ItemFrameEntityRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrameEntity>
extends EntityRenderer<T> {
	protected ItemFrameEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Overwrite
    public boolean hasLabel(T itemFrameEntity) {
        if (!MinecraftClient.isHudEnabled() || itemFrameEntity.getHeldItemStack().isEmpty() || this.dispatcher.targetedEntity != itemFrameEntity) {
            return false;
        }
		if (!itemFrameEntity.getHeldItemStack().hasCustomName()) {
			if (itemFrameEntity.containsMap()) {
				return true;
			}
		}

        double d = this.dispatcher.getSquaredDistanceToCamera((Entity)itemFrameEntity);
        float f = itemFrameEntity.isSneaky() ? 32.0f : 64.0f;
        return d < (double)(f * f);
    }

	@Overwrite
	public void renderLabelIfPresent(T itemFrameEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		text = itemFrameEntity.getHeldItemStack().getName();
		if (itemFrameEntity.containsMap()) {
			int mapid = itemFrameEntity.getMapId().getAsInt();
			text = text.copy().append(" <ID:" + mapid + ">");
		}
		super.renderLabelIfPresent(itemFrameEntity, text, matrixStack, vertexConsumerProvider, i);
	}
}
