package craftedMods.recipes.provider;

import codechicken.nei.api.IConfigureNEI;
import craftedMods.recipes.NEIExtensions;
import net.minecraft.util.StatCollector;

public class NEIExtensionsAddonConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return StatCollector.translateToLocal("neiLotr.integration.name");
	}

	@Override
	public String getVersion() {
		return NEIExtensions.mod.getVersionChecker().getCurrentVersion().getState().toString() + " " + NEIExtensions.VERSION;
	}

	@Override
	public void loadConfig() {
		NEIExtensions.mod.getNeiConfig().load();
	}

}
