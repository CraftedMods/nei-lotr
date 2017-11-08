package craftedMods.neiLotr.handlers;

import java.util.Iterator;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import craftedMods.neiLotr.util.*;
import lotr.client.gui.LOTRGuiUnsmeltery;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class UnsmelteryRecipeHandler extends FurnaceRecipeHandler {

	@Override
	public String getGuiTexture() {
		return "lotr:gui/unsmelter.png";
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiUnsmeltery.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("container.lotr.unsmeltery");
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) {
			loadCraftingRecipes((ItemStack) results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		FMLControlledNamespacedRegistry<Item> items = GameData.getItemRegistry();
		Iterator<Item> it = items.iterator();
		while (it.hasNext()) {
			ItemStack stack = new ItemStack(it.next(), 1);
			ItemStack equipmentMaterial = NeiLotrReflection.getEquipmentMaterial(stack);
			if (equipmentMaterial != null) {
				if (NEIClientUtils.areStacksSameTypeCrafting(equipmentMaterial, result)) {
					ItemStack randomResult = NeiLotrReflection.getRandomUnsmelteryResult(stack);
					UnsmeltingPair pair = new UnsmeltingPair(stack,
							randomResult != null ? randomResult : equipmentMaterial);
					arecipes.add(pair);
				}
			}

		}

		FMLControlledNamespacedRegistry<Block> blocks = GameData.getBlockRegistry();
		Iterator<Block> it2 = blocks.iterator();
		while (it2.hasNext()) {
			ItemStack stack = new ItemStack(it2.next(), 1);
			ItemStack equipmentMaterial = NeiLotrReflection.getEquipmentMaterial(stack);
			if (equipmentMaterial != null) {
				if (NEIClientUtils.areStacksSameTypeCrafting(equipmentMaterial, result)) {
					ItemStack randomResult = NeiLotrReflection.getRandomUnsmelteryResult(stack);
					UnsmeltingPair pair = new UnsmeltingPair(stack,
							randomResult != null ? randomResult : equipmentMaterial);
					arecipes.add(pair);
				}
			}

		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item")) {
			loadUsageRecipes((ItemStack) ingredients[0]);
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		ItemStack resultStack = NeiLotrReflection.getRandomUnsmelteryResult(ingredient);
		if (resultStack != null) {
			UnsmeltingPair pair = new UnsmeltingPair(ingredient, resultStack);
			arecipes.add(pair);
		}
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new UnsmelteryRecipeHandler();
	}

	private class UnsmeltingPair extends SmeltingPair {

		private ItemStack ingredient;
		private PositionedStack lastIngredient;
		private int lastCycle = cycleticks / 48;

		public UnsmeltingPair(ItemStack ingred, ItemStack result) {
			super(ingred, result);
			ingredient = ingred;
		}

		@Override
		public PositionedStack getResult() {
			return getCycledResult(cycleticks / 48, super.getResult());
		}

		public PositionedStack getCycledResult(int cycle, PositionedStack result) {
			if (cycle != lastCycle) {
				lastCycle = cycle;
				ItemStack stack = NeiLotrReflection.getRandomUnsmelteryResult(ingredient);
				if (stack != null) {
					lastIngredient = new PositionedStack(stack, result.relx, result.rely);
					return lastIngredient;
				} else {
					return lastIngredient == null ? result : lastIngredient;
				}
			}
			return lastIngredient == null ? result : lastIngredient;
		}
	}
}
