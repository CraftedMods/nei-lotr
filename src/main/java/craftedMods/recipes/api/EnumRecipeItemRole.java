package craftedMods.recipes.api;

import craftedMods.recipes.utils.NEIExtensionsUtils;

public enum EnumRecipeItemRole {

	RESULT, INGREDIENT, OTHER;
	
	public static EnumRecipeItemRole createRecipeItemRole(String name) {
		return NEIExtensionsUtils.createRecipeItemRole(name);
	}

}
