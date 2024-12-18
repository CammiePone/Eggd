package dev.cammiescorner.eggd.mixin.client;

import dev.cammiescorner.eggd.common.EggPlacement;
import dev.cammiescorner.eggd.common.components.EggComponent;
import dev.cammiescorner.eggd.common.registry.EggdComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
	@Shadow @Final private MinecraftClient client;
	@Shadow protected abstract void renderOverlay(Identifier texture, float opacity);

	@Inject(method = "render", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
	), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void eggd$renderEggs(MatrixStack matrices, float tickDelta, CallbackInfo info, TextRenderer textRenderer, float f, ItemStack itemStack) {
		if(client.player != null) {
			EggComponent component = EggdComponents.EGG.get(client.player);

			for(EggPlacement placement : component.getEggs())
				renderOverlay(placement.texture, Math.min(1F, component.getTimer(placement) / 20F));
		}
	}
}
