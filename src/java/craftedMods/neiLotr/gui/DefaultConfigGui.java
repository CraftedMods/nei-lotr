package craftedMods.neiLotr.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import craftedMods.neiLotr.NeiLotr;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class DefaultConfigGui extends GuiConfig {

	public DefaultConfigGui(GuiScreen parent) {
		super(parent, getConfigElements(), NeiLotr.MODID, true, false,
				StatCollector.translateToLocal("neiLotr.config.gui.title"));
	}

	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> ret = new ArrayList();
		Configuration config = NeiLotr.mod.getConfig().getConfigFile();
		for (String category : config.getCategoryNames()) {
			ret.addAll(new ConfigElement(config.getCategory(category)).getChildElements());
		}
		return ret;
	}
}
