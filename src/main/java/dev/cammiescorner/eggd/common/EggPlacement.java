package dev.cammiescorner.eggd.common;

import dev.cammiescorner.eggd.Eggd;
import net.minecraft.util.Identifier;

public enum EggPlacement {
	LEFT(Eggd.id("textures/gui/hud/egg_overlay_left.png")), MIDDLE(Eggd.id("textures/gui/hud/egg_overlay_middle.png")), RIGHT(Eggd.id("textures/gui/hud/egg_overlay_right.png"));

	public final Identifier texture;

	EggPlacement(Identifier texture) {
		this.texture = texture;
	}
}
