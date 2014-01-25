package gregtechmod.api.enums;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IItemContainer;
import gregtechmod.api.util.GT_OreDictUnificator;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Class containing all non-OreDict Items of GregTech.
 */
public enum GT_Items implements IItemContainer {
	TE_Slag,
	TE_Slag_Rich,
	TE_Rockwool,
	TE_Hardened_Glass,
	IC2_Scrap,
	IC2_Scrapbox,
	IC2_Fertilizer,
	IC2_Mixed_Metal_Ingot,
	IC2_Rubber,
	IC2_Resin,
	IC2_Crop_Seeds,
	IC2_Grin_Powder,
	IC2_Energium_Dust,
	IC2_Compressed_Coal_Ball,
	IC2_Compressed_Coal_Chunk,
	IC2_Fuel_Can_Empty,
	IC2_Fuel_Can_Filled,
	IC2_Food_Can_Empty,
	IC2_Food_Can_Filled,
	IC2_Food_Can_Spoiled,
	IC2_Industrial_Diamond,
	Shape_Empty,
	Shape_Mold_Credit,
	Shape_Extruder_Plate,
	Shape_Extruder_Cell,
	Shape_Extruder_Ring,
	Shape_Extruder_Rod,
	Shape_Extruder_Bolt,
	Shape_Extruder_Ingot,
	Shape_Extruder_Wire,
	Shape_Extruder_Casing,
	Shape_Extruder_Pipe_Small,
	Shape_Extruder_Pipe_Normal,
	Shape_Extruder_Pipe_Large,
	Shape_Extruder_Block,
	Credit_Copper,
	Credit_Iron,
	Credit_Silver,
	Credit_Gold,
	Credit_Platinum,
	Credit_Osmium,
	Cell_Empty,
	Cell_Water,
	Cell_Lava,
	Cell_Air,
	Circuit_Board_Basic,
	Circuit_Board_Advanced,
	Circuit_Board_Elite,
	Circuit_Parts_Advanced,
	Circuit_Basic,
	Circuit_Advanced,
	Circuit_Data,
	Circuit_Elite,
	Circuit_Master,
	Circuit_Ultimate,
	Upgrade_Overclocker,
	Upgrade_Transformer,
	Upgrade_Battery,
	Display_Fluid,
	NC_SensorCard,
	NC_SensorKit,
	Tool_Mortar_Iron,
	Tool_Mortar_Wood,
	Tool_Cheat,
	Tool_Scanner,
	Tool_Crowbar_Iron,
	Tool_Screwdriver_Iron,
	Tool_Screwdriver_TungstenSteel,
	Tool_Screwdriver_Electric,
	Tool_Wrench_Iron,
	Tool_Wrench_Bronze,
	Tool_Wrench_Steel,
	Tool_Wrench_TungstenSteel,
	Tool_Wrench_Electric,
	Tool_Wrench_Advanced,
	Tool_Hammer_Forge,
	Tool_Hammer_Rubber,
	Tool_Hammer_Iron,
	Tool_Hammer_Bronze,
	Tool_Hammer_Steel,
	Tool_Hammer_TungstenSteel,
	Tool_File_Iron,
	Tool_File_Bronze,
	Tool_File_Steel,
	Tool_File_TungstenSteel,
	Tool_Saw_Iron,
	Tool_Saw_Bronze,
	Tool_Saw_Steel,
	Tool_Saw_TungstenSteel,
	Tool_Saw_Electric,
	Tool_Saw_Advanced,
	Tool_Drill_Advanced,
	Tool_SolderingIron_Electric,
	Tool_SolderingMaterial_Tin,
	Tool_SolderingMaterial_Lead,
	Tool_Rockcutter,
	Tool_Teslastaff,
	Tool_DataOrb,
	Tool_Sonictron,
	Tool_Destructopack,
	Tool_Flint_Sword,
	Tool_Flint_Pickaxe,
	Tool_Flint_Shovel,
	Tool_Flint_Axe,
	Tool_Flint_Hoe,
	Tool_Steel_Sword,
	Tool_Steel_Pickaxe,
	Tool_Steel_Shovel,
	Tool_Steel_Axe,
	Tool_Steel_Hoe,
	Tool_TungstenSteel_Sword,
	Tool_TungstenSteel_Pickaxe,
	Tool_TungstenSteel_Shovel,
	Tool_TungstenSteel_Axe,
	Tool_TungstenSteel_Hoe,
	Tool_Jackhammer_Bronze,
	Tool_Jackhammer_Steel,
	Tool_Jackhammer_Diamond,
	Spray_Bug,
	Spray_Ice,
	Spray_Hardener,
	Spray_CFoam,
	Spray_Pepper,
	Spray_Hydration,
	Spray_Color_00,
	Spray_Color_01,
	Spray_Color_02,
	Spray_Color_03,
	Spray_Color_04,
	Spray_Color_05,
	Spray_Color_06,
	Spray_Color_07,
	Spray_Color_08,
	Spray_Color_09,
	Spray_Color_10,
	Spray_Color_11,
	Spray_Color_12,
	Spray_Color_13,
	Spray_Color_14,
	Spray_Color_15,
	Armor_Cheat,
	Armor_Cloaking,
	Armor_Lamp,
	Armor_LithiumPack,
	Armor_LapotronicPack,
	Armor_ForceField,
	Energy_LapotronicOrb,
	Energy_Lithium,
	Energy_Lithium_Empty,
	Reactor_Coolant_He_1,
	Reactor_Coolant_He_3,
	Reactor_Coolant_He_6,
	Reactor_Coolant_NaK_1,
	Reactor_Coolant_NaK_3,
	Reactor_Coolant_NaK_6,
	Reactor_NeutronReflector,
	Component_Turbine_Bronze,
	Component_Turbine_Steel,
	Component_Turbine_Magnalium,
	Component_Turbine_TungstenSteel,
	Component_Turbine_Carbon,
	Component_LavaFilter,
	
	Frame_Iron,
	Frame_Aluminium,
	Frame_Steel,
	Frame_StainlessSteel,
	Frame_TungstenSteel,
	
	Pipe_Bronze_Small,
	Pipe_Bronze_Medium,
	Pipe_Bronze_Large,
	Pipe_Steel_Small,
	Pipe_Steel_Medium,
	Pipe_Steel_Large,
	Pipe_StainlessSteel_Small,
	Pipe_StainlessSteel_Medium,
	Pipe_StainlessSteel_Large,
	Pipe_TungstenSteel_Small,
	Pipe_TungstenSteel_Medium,
	Pipe_TungstenSteel_Large,
	Pipe_Brass_Medium,
	Pipe_Brass_Large,
	Pipe_Electrum_Medium,
	Pipe_Electrum_Large,
	Pipe_Platinum_Medium,
	Pipe_Platinum_Large,
	
	NULL;
	
	public static final GT_Items[] SPRAY_CAN_DYES = {Spray_Color_00, Spray_Color_01, Spray_Color_02, Spray_Color_03, Spray_Color_04, Spray_Color_05, Spray_Color_06, Spray_Color_07, Spray_Color_08, Spray_Color_09, Spray_Color_10, Spray_Color_11, Spray_Color_12, Spray_Color_13, Spray_Color_14, Spray_Color_15};
	private ItemStack mStack;
	private boolean mHasNotBeenSet = true;
	
	public IItemContainer set(Item aItem) {
		mHasNotBeenSet = false;
		if (aItem == null) return this;
		ItemStack aStack = new ItemStack(aItem, 1, 0);
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}
	
	public IItemContainer set(ItemStack aStack) {
		mHasNotBeenSet = false;
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}
	
	@Override
	public Item getItem() {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return null;
		return mStack.getItem();
	}
	
	@Override
	public Block getBlock() {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		return GT_Utility.getBlockFromStack(getItem());
	}
	
	@Override
	public boolean isStackEqual(Object aStack) {
		return isStackEqual(aStack, false, false);
	}
	
	@Override
	public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
		if (GT_Utility.isStackInvalid(aStack)) return false;
		return GT_Utility.areStacksEqual((ItemStack)aStack, aWildcard?getWildcard(1):get(1), aIgnoreNBT);
	}
	
	@Override
	public ItemStack get(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmount(aAmount, GT_OreDictUnificator.get(mStack));
	}
	
	@Override
	public ItemStack getWildcard(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, GregTech_API.ITEM_WILDCARD_DAMAGE, GT_OreDictUnificator.get(mStack));
	}
	
	@Override
	public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, 0, GT_OreDictUnificator.get(mStack));
	}
	
	@Override
	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, mStack.getMaxDamage()-1, GT_OreDictUnificator.get(mStack));
	}
	
	@Override
	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		if (GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount(aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData(aAmount, aMetaValue, GT_OreDictUnificator.get(mStack));
	}
	
	@Override
	public IItemContainer registerOre(Object... aOreNames) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, get(1));
		return this;
	}
	
	@Override
	public IItemContainer registerWildcardAsOre(Object... aOreNames) {
		if (mHasNotBeenSet) throw new IllegalAccessError("The Enum '" + toString() + "' has not been set to an Item at this time!");
		for (Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, getWildcard(1));
		return this;
	}
}