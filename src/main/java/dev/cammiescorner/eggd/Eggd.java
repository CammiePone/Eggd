package dev.cammiescorner.eggd;

import dev.cammiescorner.eggd.common.registry.EggdSounds;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eggd implements ModInitializer {
	public static final String MOD_ID = "eggd";
	public static final Logger LOGGER = LoggerFactory.getLogger("Egg'd!");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());

		Registry.register(Registry.SOUND_EVENT, Eggd.id("egg_splat"), EggdSounds.EGG_SPLAT);
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}
