package dev.cammiescorner.eggd.common.registry;

import dev.cammiescorner.eggd.Eggd;
import dev.cammiescorner.eggd.common.components.EggComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;

public class EggdComponents implements EntityComponentInitializer {
	public static final ComponentKey<EggComponent> EGG = createComponent("egg", EggComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(PlayerEntity.class, EGG).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(EggComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Eggd.id(name), component);
	}
}
