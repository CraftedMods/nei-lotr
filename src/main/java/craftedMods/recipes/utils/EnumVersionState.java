package craftedMods.recipes.utils;

import net.minecraft.util.StatCollector;

public enum EnumVersionState {
	ALPHA, BETA, FULL;

	@Override
	public String toString() {
		return StatCollector.translateToLocal("neiLotr.versionChecker.version." + this.name().toLowerCase());
	}
}
