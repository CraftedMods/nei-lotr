package craftedMods.neiLotr.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import codechicken.core.ReflectionManager;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.neiLotr.NeiLotr;
import lotr.common.entity.npc.LOTRTradeEntries;
import lotr.common.item.LOTRItemMug;
import lotr.common.tileentity.LOTRTileEntityAlloyForgeBase;
import lotr.common.tileentity.LOTRTileEntityUnsmeltery;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class NeiLotrReflection {

	private static LOTRTileEntityUnsmeltery unsmelteryTileEntity = new LOTRTileEntityUnsmeltery();
	private static Method alloyForgeMethod;

	static {
		unsmelteryTileEntity.setWorldObj(Minecraft.getMinecraft().theWorld);
		try {
			alloyForgeMethod = LOTRTileEntityAlloyForgeBase.class.getDeclaredMethod("getAlloySmeltingResult",
					ItemStack.class, ItemStack.class);
			alloyForgeMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ItemStack getRandomUnsmelteryResult(ItemStack stack) {
		ItemStack ret = null;
		try {
			ret = ReflectionManager.callMethod(LOTRTileEntityUnsmeltery.class, ItemStack.class, unsmelteryTileEntity,
					"getRandomUnsmeltingResult");
		} catch (Exception e) {
			NeiLotr.mod.getLogger().error("Could not invoke getRandomUnsmeltingResult via reflection", e);
		}
		return ret;
	}

	public static ItemStack getEquipmentMaterial(ItemStack stack) {
		ItemStack ret = null;
		try {
			ret = ReflectionManager.callMethod(LOTRTileEntityUnsmeltery.class, ItemStack.class, "getEquipmentMaterial",
					stack);
		} catch (Exception e) {
			NeiLotr.mod.getLogger().error("Could not invoke static method getEquipmentMaterial via reflection", e);
		}
		return ret;
	}

	public static ItemStack getAlloySmeltingResult(LOTRTileEntityAlloyForgeBase instance, ItemStack stack,
			ItemStack alloyItem) {
		ItemStack ret = null;
		try {
			ret = (ItemStack) alloyForgeMethod.invoke(instance, stack, alloyItem);
		} catch (Exception e) {
			NeiLotr.mod.getLogger().error("Could not invoke method getAlloySmeltingResult via reflection", e);
		}
		return ret;
	}

	public static LOTRItemMug.Vessel[] getDrinkVessels(LOTRTradeEntries instance) {
		LOTRItemMug.Vessel[] ret = null;
		try {
			ret = ReflectionHelper.getPrivateValue(LOTRTradeEntries.class, instance, "drinkVessels");
		} catch (Exception e) {
			NeiLotr.mod.getLogger()
					.error("Could not get private field value drinkVessels from LOTRTradeEntries via reflection", e);
		}
		return ret;
	}

	public static float[] getStrenghts() {
		float[] ret = null;
		try {
			ret = ReflectionHelper.getPrivateValue(LOTRItemMug.class, null, "strengths");
		} catch (Exception e) {
			NeiLotr.mod.getLogger()
					.error("Could not get private field strengths drinkVessels from LOTRItemMug via reflection", e);
		}
		return ret;
	}
}
