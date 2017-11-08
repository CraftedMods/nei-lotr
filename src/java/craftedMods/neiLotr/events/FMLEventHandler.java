package craftedMods.neiLotr.events;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import craftedMods.neiLotr.NeiLotr;

public class FMLEventHandler {
	
	private NeiLotr mod;
	public FMLEventHandler() {
		mod = NeiLotr.mod;
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(NeiLotr.MODID)) {
			NeiLotr.mod.getConfig().update();
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if (mod.getConfig().isUseVersionChecker() && mod.getVersionChecker().isNewVersionAvaible()) {
			if (mod.getVersionChecker().getNewestVersion() != null) {
				event.player.addChatComponentMessage(mod.getVersionChecker().getNewestVersion().getFormattedChatText());
			}
		}

	}

}
