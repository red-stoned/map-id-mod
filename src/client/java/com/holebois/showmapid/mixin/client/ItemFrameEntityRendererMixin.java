package com.holebois.showmapid.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.ItemFrame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameEntityRendererMixin<T extends ItemFrame>
extends EntityRenderer<T, ItemFrameRenderState> {
	protected ItemFrameEntityRendererMixin(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

    @Unique
    private boolean showmapid$shouldShowMapId(T entity) {
        return entity.getItem().has(DataComponents.MAP_ID) && Minecraft.getInstance().player.isShiftKeyDown();
    }

    @Definition(id = "entity", local = @Local(type = ItemFrame.class))
    @Definition(id = "getItem", method = "Lnet/minecraft/world/entity/decoration/ItemFrame;getItem()Lnet/minecraft/world/item/ItemStack;")
    @Definition(id = "getCustomName", method = "Lnet/minecraft/world/item/ItemStack;getCustomName()Lnet/minecraft/network/chat/Component;")
    @Expression("entity.getItem().getCustomName() != null")
    @ModifyExpressionValue(method = "shouldShowName(Lnet/minecraft/world/entity/decoration/ItemFrame;D)Z", at = @At("MIXINEXTRAS:EXPRESSION"))
    public boolean showIfSneakingMap(boolean original, @Local(argsOnly = true) T entity) {
        return original || showmapid$shouldShowMapId(entity);
    }

    @ModifyReturnValue(method = "getNameTag(Lnet/minecraft/world/entity/decoration/ItemFrame;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"))
    public Component addMapIdToNametag(Component original, @Local(argsOnly = true) T entity) {
        if (showmapid$shouldShowMapId(entity)) {
            return original.copy().append(" <ID:" + entity.getItem().get(DataComponents.MAP_ID).id() + ">");
        } else {
            return original;
        }
    }
}
