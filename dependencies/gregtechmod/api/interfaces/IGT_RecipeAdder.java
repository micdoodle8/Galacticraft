package gregtechmod.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IGT_RecipeAdder {
	/**
	 * Adds a FusionreactorRecipe
	 * @param aInput1 = first Input (not null, and respects StackSize)
	 * @param aInput2 = second Input (not null, and respects StackSize)
	 * @param aOutput = Output of the Fusion (can be null, and respects StackSize)
	 * @param aFusionDurationInTicks = How many ticks the Fusion lasts (must be > 0)
	 * @param aFusionEnergyPerTick = The EU generated per Tick (can even be negative!)
	 * @param aEnergyNeededForStartingFusion = EU needed for heating the Reactor up (must be >= 0)
	 * @return true if the Recipe got added, otherwise false.
	 */
	public boolean addFusionReactorRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aFusionDurationInTicks, int aFusionEnergyPerTick, int aEnergyNeededForStartingFusion);
	
	/**
	 * Adds a Centrifuge Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 * @param aDuration must be > 0
	 */
	public boolean addCentrifugeRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration);
	
	/**
	 * Adds a Electrolyzer Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincellcount, < 0 for negative Fuelcancount, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addElectrolyzerRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);
	
	/**
	 * Adds a Chemical Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 */
	public boolean addChemicalRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration);
	
	/**
	 * Adds a Blast Furnace Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 can be null
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 * @param aLevel should be > 0 is the minimum Heat Level needed for this Recipe
	 */
	public boolean addBlastRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt, int aLevel);
	
	/**
	 * Adds a Canning Machine Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0, 100 ticks is standard.
	 * @param aEUt should be > 0, 1 EU/t is standard.
	 */
	public boolean addCannerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
	
	/**
	 * Adds an Alloy Smelter Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addAlloySmelterRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds a CNC-Machine Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addCNCRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds a Circuit Assembler Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addAssemblerRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds a Wiremill Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addWiremillRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds a Plate Bending Machine Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 * @param aEUt should be > 0
	 */
	public boolean addBenderRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds an Implosion Compressor Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 amount of ITNT, should be > 0
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 */
	public boolean addImplosionRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2);
	
	/**
	 * Adds a Grinder Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincell count, < 0 for negative Water Cell count, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 */
	public boolean addGrinderRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4);
	
	/**
	 * Adds a Grinder Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 id for the Cell needed for this Recipe
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 * @param aOutput4 can be null
	 */
	public boolean addGrinderRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4);
	
	/**
	 * Adds a Distillation Tower Recipe
	 */
	public boolean addDistillationRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aDuration, int aEUt);

	/**
	 * Adds a Lathe Machine Recipe
	 */
	public boolean addLatheRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, int aDuration, int aEUt);
	
	/**
	 * Adds a Plate Cutter Recipe
	 */
	public boolean addCutterRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration, int aEUt);
	
	/**
	 * Adds a Vacuum Freezer Recipe
	 * @param aInput1 must be != null
	 * @param aOutput1 must be != null
	 * @param aDuration must be > 0
	 */
	public boolean addVacuumFreezerRecipe(ItemStack aInput1, ItemStack aOutput1, int aDuration);
	
	/**
	 * Adds a Sawmill Recipe
	 * @param aInput1 must be != null
	 * @param aCellInput this is for the needed Cells, > 0 for Tincell count, < 0 for negative Water Cell count, == 0 for nothing
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 */
	public boolean addSawmillRecipe(ItemStack aInput1, int aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3);
	
	/**
	 * Adds a Sawmill Recipe
	 * @param aInput1 must be != null
	 * @param aInput2 this is for the needed Cells
	 * @param aOutput1 must be != null
	 * @param aOutput2 can be null
	 * @param aOutput3 can be null
	 */
	public boolean addSawmillRecipe(ItemStack aInput1, ItemStack aInput2, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3);
	
	/**
	 * Adds a Fuel for My Generators
	 * @param aInput1 must be != null
	 * @param aOutput1 can be null
	 * @param aEU EU per MilliBucket. If no Liquid Form of this Container is available, then it will give you EU*1000 per Item.
	 * @param aType 0 = Diesel; 1 = Gas Turbine; 2 = Thermal; 3 = Dense Fluid; 4 = Plasma; 5 = Magic; And if something is unclear or missing, then look at the GT_Recipe-Class
	 */
	public boolean addFuel(ItemStack aInput1, ItemStack aOutput1, int aEU, int aType);
	
	/**
	 * Adds a BlockID to the List of the Minable Blocks by the Jackhammer. Call this in postload!
	 */
	public boolean addJackHammerMinableBlock(Block aBlock, boolean aDiamondOnly);
	
	/**
	 * Adds a Sound to the Sonictron9001
	 * you should NOT call this in the preInit-Phase!
	 * @param aItemStack = The Item you want to display for this Sound
	 * @param aSoundName = The Name of the Sound in the resources/newsound-folder like Vanillasounds
	 * @return true if the Sound got added, otherwise false.
	 */
	public boolean addSonictronSound(ItemStack aItemStack, String aSoundName);
	
	/**
	 * Adds a Description to the Computercube
	 * you should call this in the postInit-Phase!
	 * @param aItemStack = An Array of 14 ItemStacks not more, not less. The Stacks themself can be null if needed. The last 9 Stacks are showing a Craftingrecipe in the top right Corner if one of them is != null.
	 * @param aText = An Array of exactly 17 Lines of Text. 0 = First Line; 16 = Last displayable Line.
	 * @return true if the Descripion got added, otherwise false.
	 */
	public boolean addComputercubeDescriptionSet(ItemStack[] aItemStack, String[] aText);
}
