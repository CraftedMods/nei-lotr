package craftedMods.recipes.utils;

import java.net.MalformedURLException;
import java.net.URL;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class ModVersion implements Comparable<ModVersion> {
	private EnumVersionState state;
	private String modid;
	private String modname;
	private String version;
	private String mcVersion;
	private String url;

	public ModVersion(EnumVersionState state, String modid, String modname, String version, String mcVersion,
			String url) {
		this.state = state;
		this.modid = modid;
		this.modname = modname;
		this.version = version;
		this.mcVersion = mcVersion;
		this.url = url;
	}

	public ModVersion(EnumVersionState state, String modid, String modname, String version, String mcVersion) {
		this(state, modid, modname, version, mcVersion, null);
	}

	public EnumVersionState getState() {
		return state;
	}

	public String getModid() {
		return modid;
	}

	public String getModname() {
		return modname;
	}

	public String getVersion() {
		return version;
	}

	public String getMcVersion() {
		return mcVersion;
	}

	public String getUrl() {
		return url;
	}

	public URL getURL() throws MalformedURLException {
		return new URL(url);
	}

	public IChatComponent getFormattedChatText() {
		String text = StatCollector.translateToLocal("neiLotr.versionChecker.notification.chat");
		String[] parts = text.split("7");
		String text1 = parts[0];
		String text2 = parts[1];
		String text3 = parts[2];
		String text4 = parts[3];

		ChatComponentText chat1 = (ChatComponentText) new ChatComponentText("[NEI LOTR]: ")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN));
		ChatComponentText chat2 = (ChatComponentText) new ChatComponentText(text1 + " ")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		ChatComponentText chat3 = (ChatComponentText) new ChatComponentText(text2)
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true)
						.setChatClickEvent(
								new ClickEvent(Action.OPEN_URL, "https://goo.gl/EkxFlC"))
						.setChatHoverEvent(
								new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
										new ChatComponentText(StatCollector
												.translateToLocal("neiLotr.versionChecker.notification.changelog")
												.replace("/", "\n")))));
		ChatComponentText chat4 = (ChatComponentText) new ChatComponentText(" " + text3 + " ")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		ChatComponentText chat5 = (ChatComponentText) new ChatComponentText(state.toString() + " " + version)
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.YELLOW).setUnderlined(true)
						.setChatClickEvent(
								new ClickEvent(Action.OPEN_URL, url))
						.setChatHoverEvent(
								new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT,
										new ChatComponentText(StatCollector
												.translateToLocal("neiLotr.versionChecker.notification.newVersion")
												.replace("/", "\n")))));
		ChatComponentText chat6 = (ChatComponentText) new ChatComponentText(" " + text4 + " ")
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));
		ChatComponentText chat7 = (ChatComponentText) new ChatComponentText(mcVersion)
				.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.WHITE));

		return chat1.appendSibling(chat2).appendSibling(chat3).appendSibling(chat4).appendSibling(chat5)
				.appendSibling(chat6).appendSibling(chat7);
	}

	@Override
	public int compareTo(ModVersion comp) throws IllegalArgumentException {
		if (comp.modid.equals(this.modid)) {
			int stateComp = this.state.compareTo(comp.state);
			int mcVersionComp = this.mcVersion.compareTo(comp.mcVersion);
			int versionComp = this.version.compareTo(comp.version);

			if (mcVersionComp != 0) {
				return mcVersionComp;
			} else {
				if (stateComp != 0) {
					return stateComp;
				} else {
					return versionComp;
				}
			}

		} else {
			throw new IllegalArgumentException("Modid of comp != modid of this object!");
		}
	}
}