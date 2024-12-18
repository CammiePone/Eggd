package dev.cammiescorner.eggd.mixin;

import dev.cammiescorner.eggd.common.registry.EggdComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EggEntity.class)
public abstract class EggEntityMixin extends ThrownEntity {
	@Unique private boolean spawnedChicken = false;

	protected EggEntityMixin(EntityType<? extends ThrownEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "onCollision", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
	))
	private void eggd$spawnChicken(HitResult hitResult, CallbackInfo info) {
		spawnedChicken = true;
	}

	@Inject(method = "onCollision", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"
	))
	private void eggd$eggEntity(HitResult hitResult, CallbackInfo info) {
		if(hitResult.getType() == HitResult.Type.ENTITY && !spawnedChicken && ((EntityHitResult) hitResult).getEntity() instanceof PlayerEntity player)
			EggdComponents.EGG.get(player).hitWithEgg((EggEntity) (Object) this);
	}
}
