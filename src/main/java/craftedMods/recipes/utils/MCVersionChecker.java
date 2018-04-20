package craftedMods.recipes.utils;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

import craftedMods.recipes.NEIExtensions;
import net.minecraftforge.common.MinecraftForge;

public class MCVersionChecker {

	private final URL versionFile;
	private final ModVersion currentVersion = new ModVersion(EnumVersionState.ALPHA, NEIExtensions.MODID, NEIExtensions.MODNAME, NEIExtensions.VERSION,
			MinecraftForge.MC_VERSION);
	private ModVersion newestAvVersion = null;
	private boolean checked = false;
	private boolean newVersionAvaible = false;

	public MCVersionChecker(String versionFileURL) throws MalformedURLException {
		URL tmpUrl = null;
		try {
			tmpUrl = new URL(versionFileURL);
		} catch (MalformedURLException e) {
			NEIExtensions.mod.getLogger().error("The URL of the version file (\"" + versionFileURL + "\") isn't valid!");
			throw e;
		}
		this.versionFile = tmpUrl;
	}

	private String downloadVersionFile() throws IOException {
		InputStream stream = this.versionFile.openStream();

		String tmp = new String();
		String tmp2 = new String();
		byte[] buffer = new byte[1024];

		while (stream.read(buffer) != -1) {
			tmp = new String(buffer);
			buffer = new byte[1024];
			tmp2 = tmp2.concat(tmp);
		}
		tmp2.trim();
		return tmp2;
	}

	private ModVersion parseVersionString(String versionString) throws MalformedURLException {

		if (versionString == null) return null;

		EnumVersionState state = null;
		String version = null;
		String mcVersion = null;
		String url = null;

		StringTokenizer tok = new StringTokenizer(versionString, "|");
		while (tok.hasMoreTokens())
			if (tok.countTokens() == 4) state = EnumVersionState.valueOf(tok.nextToken().toUpperCase());
			else if (tok.countTokens() == 3) version = tok.nextToken();
			else if (tok.countTokens() == 2) mcVersion = tok.nextToken();
			else if (tok.countTokens() == 1) url = tok.nextToken().trim();

		NEIExtensions.mod.getLogger().info("Found version: " + version);

		return new ModVersion(state, NEIExtensions.MODID, NEIExtensions.MODNAME, version, mcVersion, url);
	}

	public boolean ping() throws Exception {
		try {
			if (this.versionFile == null) return false;
			URLConnection conn = this.versionFile.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
		} catch (Exception e) {
			NEIExtensions.mod.getLogger().error("Cannot connect to the version file. Do you have an internet connection?");
			throw e;
		}

		return true;
	}

	public boolean checkVersion() throws Exception {
		if (this.newestAvVersion == null) {
			if (!this.ping()) return false;
			this.newestAvVersion = this.parseVersionString(this.downloadVersionFile());
			this.checked = true;
		}
		this.newVersionAvaible = this.currentVersion.compareTo(this.newestAvVersion) < 0;
		return this.newVersionAvaible;
	}

	public ModVersion getCurrentVersion() {
		return this.currentVersion;
	}

	public ModVersion getNewestVersion() {
		return this.newestAvVersion;
	}

	public boolean isNewVersionAvaible() {
		return this.newVersionAvaible;
	}

	public boolean hasChecked() {
		return this.checked;
	}
}
