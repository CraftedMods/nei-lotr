package craftedMods.lotr.recipes;

import craftedMods.recipes.api.*;
import craftedMods.utils.*;

@RegisteredHandler
public class NeiLotrVersionCheckerHandler implements VersionCheckerHandler {

	public static final SemanticVersion NEI_LOTR_VERSION = new SemanticVersion(EnumVersionState.ALPHA, 0, 6, 0);

	@Override
	public SemanticVersion getCurrentVersion() {
		return NEI_LOTR_VERSION;
	}

	@Override
	public String getLocalizedHandlerName() {
		return "NEI LOTR";
	}

	@Override
	public String getVersionFileURL() {
		return "https://raw.githubusercontent.com/CraftedMods/nei-lotr/master/version.txt";//FIXME change to master
	}

}
