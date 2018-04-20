package craftedMods.recipes;

import java.util.*;

import cpw.mods.fml.client.config.*;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.*;

public class DefaultConfigurationGui extends GuiConfig {

	public DefaultConfigurationGui(GuiScreen parent) {
		super(parent, DefaultConfigurationGui.getConfigElements(), NEIExtensions.MODID, true, false,
				StatCollector.translateToLocal("neiLotr.config.gui.title"));
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> ret = new ArrayList();
		Configuration config = NEIExtensions.mod.getConfig().getConfigFile();

		Set<String> registeredCategories = new HashSet<>();
		for (String categoryName : config.getCategoryNames()) {
			boolean add = true;
			for (String cat : categoryName.split("\\.")) {
				if (registeredCategories.contains(cat)) {
					add = false;
					break;
				}
			}
			if (add) {
				ConfigCategory category = config.getCategory(categoryName);
				registeredCategories.add(category.getName());
				addChildren(category, registeredCategories);
				ret.add(new ConfigElement(category));
			}
		}

		return ret;
	}

	private static void addChildren(ConfigCategory category, Set<String> toAdd) {
		for (ConfigCategory child : category.getChildren()) {
			toAdd.add(child.getName());
			addChildren(child, toAdd);
		}
	}
}
