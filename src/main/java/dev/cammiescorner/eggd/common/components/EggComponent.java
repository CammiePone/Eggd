package dev.cammiescorner.eggd.common.components;

import dev.cammiescorner.eggd.common.EggPlacement;
import dev.cammiescorner.eggd.common.registry.EggdComponents;
import dev.cammiescorner.eggd.common.registry.EggdSounds;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EggComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity player;
	private final Map<EggPlacement, Integer> eggMap = new HashMap<>();
	private int lastHitByEgg = 0;

	public EggComponent(PlayerEntity player) {
		this.player = player;
		eggMap.put(EggPlacement.LEFT, 0);
		eggMap.put(EggPlacement.MIDDLE, 0);
		eggMap.put(EggPlacement.RIGHT, 0);
	}

	@Override
	public void serverTick() {
		for(EggPlacement placement : eggMap.keySet()) {
			int timer = eggMap.get(placement);

			if(timer > 0)
				decrementTimer(placement);
		}

		if(lastHitByEgg > 0)
			lastHitByEgg--;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList list = tag.getList("EggPlacementMap", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < list.size(); i++) {
			NbtCompound nbt = list.getCompound(i);
			eggMap.put(EggPlacement.values()[nbt.getInt("EggPlacementIndex")], nbt.getInt("EggTimer"));
		}

		lastHitByEgg = tag.getInt("LastHitByEgg");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();

		for(EggPlacement placement : eggMap.keySet()) {
			NbtCompound nbt = new NbtCompound();

			nbt.putInt("EggPlacementIndex", placement.ordinal());
			nbt.putInt("EggTimer", eggMap.get(placement));
			list.add(nbt);
		}

		tag.put("EggPlacementMap", list);
		tag.putInt("LastHitByEgg", lastHitByEgg);
	}

	public Set<EggPlacement> getEggs() {
		return eggMap.keySet();
	}

	public int getTimer(EggPlacement placement) {
		return eggMap.get(placement);
	}

	public void hitWithEgg(EggEntity egg) {
		double eggY = egg.getY();

		if(!eggMap.values().stream().allMatch(integer -> integer > 0) && lastHitByEgg <= 0 && eggY >= player.getEyeY() - 0.1875) {
			EggPlacement placement = EggPlacement.values()[egg.world.random.nextInt(3)];

			while(eggMap.get(placement) > 0)
				placement = EggPlacement.values()[egg.world.random.nextInt(3)];

			eggMap.put(placement, 100);
			lastHitByEgg = 10;
			egg.playSound(EggdSounds.EGG_SPLAT, 1F, 1.2F / (egg.world.random.nextFloat() * 0.2F + 0.9F));
			EggdComponents.EGG.sync(player);
		}
	}

	private void decrementTimer(EggPlacement placement) {
		int timer = eggMap.get(placement) - 1;
		eggMap.put(placement, timer);

		if(timer < 20)
			EggdComponents.EGG.sync(player);
	}
}
