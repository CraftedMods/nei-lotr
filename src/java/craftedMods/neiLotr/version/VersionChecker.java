package craftedMods.neiLotr.version;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.util.NeiLotrUtil;
import net.minecraftforge.common.MinecraftForge;

public class VersionChecker {

	private final URL versionFile;
	private final ModVersion currentVersion = new ModVersion(EnumVersionState.ALPHA, NeiLotr.MODID, NeiLotr.MODNAME,
			NeiLotr.VERSION, MinecraftForge.MC_VERSION);
	private ModVersion newestAvVersion = null;
	private boolean checked = false;
	private boolean newVersionAvaible = false;

	public VersionChecker() {
		URL tmpUrl = null;
		try {
			tmpUrl = new URL("https://dl.dropboxusercontent.com/s/gyz1oq7vyz753y5/version.txt");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			NeiLotr.mod.getLogger().error("URL of version file isn't valid!");
		}
		versionFile = tmpUrl;
	}

	private String downloadVersionFile() throws IOException {
		InputStream stream = versionFile.openStream();

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

		if (versionString == null) {
			return null;
		}

		EnumVersionState state = null;
		String version = null;
		String mcVersion = null;
		String url = null;

		StringTokenizer tok = new StringTokenizer(versionString, "|");
		while (tok.hasMoreTokens()) {
			if (tok.countTokens() == 4) {
				state = EnumVersionState.valueOf(tok.nextToken().toUpperCase());
			} else if (tok.countTokens() == 3) {
				version = tok.nextToken();
			} else if (tok.countTokens() == 2) {
				mcVersion = tok.nextToken();
			} else if (tok.countTokens() == 1) {
				url = tok.nextToken().trim();
			}
		}

		NeiLotr.mod.getLogger().info("Found version: " + version);

		return new ModVersion(state, NeiLotr.MODID, NeiLotr.mod.MODNAME, version, mcVersion, url);
	}

	public boolean ping() {
		try {
			if (versionFile == null)
				return false;
			URLConnection conn = versionFile.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
		} catch (Exception e) {
			NeiLotr.mod.getLogger().error("Cannot connect to version file. Do you have an internet connection?", e);
			return false;
		}

		return true;
	}

	public boolean checkVersion() throws Exception {
		if (newestAvVersion == null) {
			if (!ping()) {
				return false;
			}
			newestAvVersion = parseVersionString(downloadVersionFile());
			checked = true;
		}
		newVersionAvaible = currentVersion.compareTo(newestAvVersion) < 0;
		return newVersionAvaible;
	}

	public ModVersion getCurrentVersion() {
		return currentVersion;
	}

	public ModVersion getNewestVersion() {
		return newestAvVersion;
	}

	public boolean isNewVersionAvaible() {
		return newVersionAvaible;
	}

	public boolean hasChecked() {
		return checked;
	}
}
