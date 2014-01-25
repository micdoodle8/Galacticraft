package gregtechmod.api;

import gregtechmod.api.enums.Materials;
import gregtechmod.api.interfaces.IGT_Mod;
import gregtechmod.api.interfaces.IGT_RecipeAdder;
import gregtechmod.api.interfaces.IMachineBlockUpdateable;
import gregtechmod.api.interfaces.IMetaTileEntity;
import gregtechmod.api.items.GT_Tool_Item;
import gregtechmod.api.metatileentity.BaseMetaTileEntity;
import gregtechmod.api.util.*;
import gregtechmod.api.world.GT_Worldgen;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This File contains the functions used to get Items and add Recipes. Please do not include this File in your Moddownload as it maybe ruins compatiblity, like with the IC2-API
 * You may just copy those Functions into your Code, or better call them via reflection.
 * 
 * The whole API is the basic construct of my Mod. Everything is dependent on it.
 * I change things quite often so please don't include any File inside your Mod, unless it's an Interface. Interfaces are not going to be changed during a MC-Version
 * Since some Authors were stupid enough to break this simple Rule, I added Version checks to enforce it.
 * 
 * In these Folders are many useful Functions. You can use them via reflection if you want.
 * I know not everything is compilable due to API's of other Mods, but these are easy to fix in your Setup.
 * 
 * You can use this to learn about Modding, but I would recommend simpler Mods.
 * You may even copypaste Code from these API-Files into your Mod, as I have nothing against that, but you should look exactly at what you are copying.
 * 
 * @author Gregorius Techneticies
 */
public class GregTech_API {
	/** For the API Version check */
	public static volatile int VERSION = 407;
	
	/** The Mod Object itself. That is the GT_Mod-Object. It's needed to open GUI's and similar. */
	public static IGT_Mod gregtechmod;
	
	/**
	 * Use this Object to add Recipes.
	 * 
	 * All Recipe adding Functions have been moved to here!
	 */
	public static IGT_RecipeAdder sRecipeAdder;
	
	/** These Lists are getting executed at their respective timings. Useful if you have to do things right before/after I do them, without having to control the load order. Add your "Commands" in the Constructor or in a static Code Block of your Mods Main Class. These are not Threaded, I just use a native Java Interface for their execution. Implement just the Method run() and everything should work */
	public static List<Runnable> sBeforeGTPreload = new ArrayList<Runnable>(), sAfterGTPreload = new ArrayList<Runnable>(), sBeforeGTLoad = new ArrayList<Runnable>(), sAfterGTLoad = new ArrayList<Runnable>(), sBeforeGTPostload = new ArrayList<Runnable>(), sAfterGTPostload = new ArrayList<Runnable>(), sBeforeGTServerstart = new ArrayList<Runnable>(), sAfterGTServerstart = new ArrayList<Runnable>(), sBeforeGTServerstop = new ArrayList<Runnable>(), sAfterGTServerstop = new ArrayList<Runnable>(), sGTCoverload = new ArrayList<Runnable>(), sGTBlockIconload = new ArrayList<Runnable>(), sGTItemIconload = new ArrayList<Runnable>();
	
	/** The Icon Registers from Blocks and Items. They will get set right before the corresponding Icon Load Phase as executed in the Runnable List above. */
	public static Object sBlockIcons, sItemIcons;
	
	/** Configured Booleans */
	public static boolean DEBUG_MODE = false, SECONDARY_DEBUG_MODE = false, IC_ENERGY_COMPATIBILITY = true, UE_ENERGY_COMPATIBILITY = true, BC_ENERGY_COMPATIBILITY = true;
	
	/** The Configuration Objects */
	public static GT_Config sRecipeFile = null, sMachineFile = null, sWorldgenFile = null, sMaterialProperties = null, sUnification = null, sSpecialFile = null;
	
	/** Because Minecraft changed it from -1 to that Value */
	public static final short ITEM_WILDCARD_DAMAGE = OreDictionary.WILDCARD_VALUE;
	
	/** The MetaTileEntity-ID-List-Length */
	public static final short MAXIMUM_METATILE_IDS = Short.MAX_VALUE - 1;
	
	/** Icon which represents failed rendering */
	public static Icon FAIL_ICON = null;
	
	/** My Creative Tab */
	public static final CreativeTabs TAB_GREGTECH = new GT_CreativeTab(), TAB_GREGTECH_MATERIALS = new GT_CreativeTab();
	
	public static int TICKS_FOR_LAG_AVERAGING = 25, MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = 100;
	
	/**
	 * A List of all registered MetaTileEntities
	 * 
	 *      0 -  2047 are reserved for GregTech.
	 *   2048 -  2559 are reserved for OvermindDL.
	 *   2560 -  3071 are reserved for Immibis.
	 *   IDs >= 3072 are currently free.
	 *  
	 * Contact me if you need a free ID-Range, which doesn't conflict with other Addons.
	 * You could make an ID-Config, but we all know, what "stupid" customers think about conflicting ID's
	 */
	public static final IMetaTileEntity[] mMetaTileList = new IMetaTileEntity[MAXIMUM_METATILE_IDS];
	
	/** FilePaths and similar Strings */
    public static final String GENERIC_CHANNEL = "gregtech", SOUND_PACKET_CHANNEL = "GTSound", TILEENTITY_PACKET_CHANNEL = "GTTile", IC2_MOD_ID = "ic2", IC2_TEXTURE_PATH = IC2_MOD_ID + ":", MOD_ID = "gregtech_addon", TEXTURE_FOLDER = "textures/", TEXTURE_PATH_ITEM = MOD_ID + ":", TEXTURE_PATH_BLOCK = MOD_ID + ":", GUI_PATH = MOD_ID + ":" + TEXTURE_FOLDER + "gui/";
    
	/** Initialized by the Block creation. */
	public static final Block[] sBlockList = new Block[16];
	
	/** Initialized by the Item creation. */
    public static final Item[] sItemList = new Item[256];
    
	public static long sServerTickCounter = 0, sClientTickCounter = 0, sWorldTickCounter = 0;
	
	/** Getting assigned by the Config */
	public static boolean sColoredGUI = true, sConstantEnergy = true, sMachineExplosions = true, sMachineFlammable = true, sMachineNonWrenchExplosions = true, sMachineRainExplosions = true, sMachineThunderExplosions = true, sMachineFireExplosions = true, sMachineWireFire = true;
	
	/** Getting assigned by the Mod loading */
	public static boolean sPreloadStarted = false, sPreloadFinished = false, sLoadStarted = false, sLoadFinished = false, sPostloadStarted = false, sPostloadFinished = false;
	
	/** The Icon List for Covers */
	public static final Map<Integer, Icon> sCovers = new HashMap<Integer, Icon>();
	
	/** The List of Circuit Behaviors for the Redstone Circuit Block */
	public static final Map<Integer, GT_CircuitryBehavior> sCircuitryBehaviors = new HashMap<Integer, GT_CircuitryBehavior>();
	
	/** The List of Cover Behaviors for the Covers */
	public static final Map<Integer, GT_CoverBehavior> sCoverBehaviors = new HashMap<Integer, GT_CoverBehavior>();
	
	/** The List of Blocks, which can conduct Machine Block Updates */
    public static final Map<Block, Integer> sMachineIDs = new HashMap<Block, Integer>();
    
	/** The Redstone Frequencies */
    public static final Map<Integer, Byte> sWirelessRedstone = new HashMap<Integer, Byte>();
    
	/** The IDSU Frequencies */
	public static final Map<Integer, Integer> sIDSUList = new HashMap<Integer, Integer>();
	
	/** A List of all Books, which were created using @GT_Utility.getWrittenBook the original Title is the Key Value */
	public static final Map<String, ItemStack> sBookList = new HashMap<String, ItemStack>();
	
	/** The List of all Sounds used in GT, indices are in the static Block at the bottom */
	public static final Map<Integer, String> sSoundList = new HashMap<Integer, String>();
	
	/** The List of Tools, which can be used. Accepts regular damageable Items and Electric Items */
	public static final List<Integer> sToolList = new ArrayList<Integer>(), sCrowbarList = new ArrayList<Integer>(), sScrewdriverList = new ArrayList<Integer>(), sWrenchList = new ArrayList<Integer>(), sSoftHammerList = new ArrayList<Integer>(), sHardHammerList = new ArrayList<Integer>(), sSolderingToolList = new ArrayList<Integer>(), sSolderingMetalList = new ArrayList<Integer>();
	
	/** 
	 * The List of Dimensions, which are Whitelisted for the Teleporter. This list should not contain other Planets.
	 * Mystcraft Dimensions and other Dimensional Things should be allowed.
	 * Mystcraft and Twilight Forest are automatically considered a Dimension, without being in this List.
	 */
	public static final List<Integer> sDimensionalList = new ArrayList<Integer>();
	
	/**
	 * Lists of Biome Names, which can spawn these Materials usually
	 * Add your Biomes to these Lists before postload!
	 */
	public static final List<String> sContinentalWaterList = new ArrayList<String>(), sTetrahedriteList = new ArrayList<String>(), sCassiteriteList = new ArrayList<String>(), sNickelList = new ArrayList<String>(), sRubyList = new ArrayList<String>(), sSapphireList = new ArrayList<String>(), sBauxiteList = new ArrayList<String>();
	
	/** Lists of all the active World generation Features, these are getting Initialized in Postload! */
	public static final List<GT_Worldgen> sWorldgenList = new ArrayList<GT_Worldgen>();
	
	/** Gets true in the PreInitPhase of the GregTech-Addon */
	public static boolean isGregTechLoaded() {return gregtechmod != null;}
	
	/** Energy Values for Voltage Classifications */
	public static final int VOLTAGE_ULTRALOW = 8, VOLTAGE_LOW = 32, VOLTAGE_MEDIUM = 128, VOLTAGE_HIGH = 512, VOLTAGE_EXTREME = 2048, VOLTAGE_QUADEXTREME = 8192, VOLTAGE_INSANE = 8192, VOLTAGE_MEGA = 32768, VOLTAGE_ULTIMATE = 1000000;
	public static final int[] VOLTAGES = new int[] {VOLTAGE_ULTRALOW, VOLTAGE_LOW, VOLTAGE_MEDIUM, VOLTAGE_HIGH, VOLTAGE_EXTREME, VOLTAGE_INSANE, VOLTAGE_MEGA, VOLTAGE_ULTIMATE};
	
	/** A List containing all the Materials, which are somehow in use by GT and therefor receive a specific Set of Items. */
	public static final Materials[] sGeneratedMaterials = new Materials[1000];
	
	/** 
	 * This is worth exactly one Item.
	 * This Constant can be divided by many commonly used Numbers such as 
	 * 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 24, ... 64 or 81
	 * without loosing precision and is for that reason used as Unit of Amount.
	 * But it is also small enough to be multiplied with larger Numbers.
	 * 
	 * This is used to determine the amount of Material contained inside a prefixed Ore.
	 * For example Nugget = MATERIAL_UNIT / 9 as it contains out of 1/9 of an Ingot.
	 */
	public static final long MATERIAL_UNIT = 3628800;
	
	/** If you have to give something a World Parameter but there is no World... */
	public static World sDummyWorld;
	
	/**
	 * Gets a Block from my Addon.
	 * @param aIndex Index of my Item:
	 * 0 Standardblock,
	 * 1 Machineblock,
	 * 2 Oreblock,
	 * 3 That glowing thing from my Lighthelmet.
	 * @param aAmount Amount of the Item in the returned Stack
	 * @param aMeta The Metavalue of the Block
	 * @return The ItemStack you ordered, if not then look at the Log.
	 */
	public static ItemStack getGregTechBlock(int aIndex, int aAmount, int aMeta) {
    	if (aIndex < GregTech_API.sBlockList.length && aIndex >= 0) {
	    	if (GregTech_API.sBlockList[aIndex] != null) return new ItemStack(GregTech_API.sBlockList[aIndex], aAmount, aMeta);
	    	GT_Log.err.println("GregTech_API ERROR: This Block Index " + aIndex + " wasn't initialized, you either called it before the Init-Phase (Only Post-Init or later will work), or this Item doesn't exist now");
    	} else GT_Log.err.println("GregTech_API ERROR: The Index " + aIndex + " is not part of my Block Range");
		return null;
	}
	
	/**
	 * Ever wondered out of how many Tin Cells an Item consists? Find it out.
	 * You could also check if the targetItem implements ICapsuleCellContainer, to not be relent on the Code in my Addon, but this also outputs values for IC2-Items
	 * @param aStack the Stack of the Item
	 * @return The amount of Tin Cells in ONE of the Items from the Stack
	 */
	public static int getCapsuleCellContainerCount(ItemStack aStack) {
		return GT_ModHandler.getCapsuleCellContainerCount(aStack);
	}
	
	/**
	 * Gets an Item from my Addon.
	 * @param aIndex Index of the Item.
	 * ID's of MetaItems:
	 * 0 = GT_MetaItem_Material use "getGregTechMaterial" instead!
	 * 3 = GT_MetaItem_Component use "getGregTechComponent" instead!
	 * @Deprecated Please use gregtechmod.enums.GT_Items instead of calling this Function.
	 * @param aAmount Amount of the Item in the returned Stack
	 * @return The ItemStack you ordered, if not then look at the Log.
	 */
	@Deprecated
	public static ItemStack getGregTechItem(int aIndex, int aAmount, int aMeta) {
    	if (aIndex < GregTech_API.sItemList.length && aIndex >= 0) {
	    	if (GregTech_API.sItemList[aIndex] != null) return GT_OreDictUnificator.get(true, new ItemStack(GregTech_API.sItemList[aIndex], aAmount, aMeta));
	    	GT_Log.err.println("GregTech_API ERROR: This Item Index " + aIndex + " wasn't initialized, you either called it before the Init-Phase (Only Post-Init or later will work), or this Item doesn't exist now");
    	} else GT_Log.err.println("GregTech_API ERROR: The Index " + aIndex + " is not part of my Item Range");
		return null;
	}
	
	/**
	 * For getting special GregTech Component Items.
	 * 
	 * This is more of an internal, I just change all Item IDs with the 1.7 Update, and this is a preparation for it.
	 */
	public static ItemStack getGregTechComponent(int aComponentIndex, int aAmount) {
    	if (aComponentIndex >= 0) {
	    	return GT_OreDictUnificator.get(true, new ItemStack(GregTech_API.sItemList[3], aAmount, aComponentIndex));
    	}
		return null;
	}
	
	/**
	 * For getting special GregTech Material Items.
	 * 
	 * This is more of an internal, I just change all Item IDs with the 1.7 Update, and this is a preparation for it.
	 */
	public static ItemStack getGregTechMaterial(int aComponentIndex, int aAmount) {
    	if (aComponentIndex >= 0) {
	    	return GT_OreDictUnificator.get(true, new ItemStack(GregTech_API.sItemList[0], aAmount, aComponentIndex));
    	}
		return null;
	}
	
	/**
	 * You want OreDict-Unification for YOUR Mod/Addon, when GregTech is installed? This Function is especially for YOU.
	 * Call this Function after the load-Phase, as I register the the most of the Unification at that Phase (Redpowers Storageblocks are registered at postload).
	 * A recommended use of this Function is inside your Recipe-System itself (if you have one), as the unification then makes 100% sure, that every added non-unificated Output gets automatically unificated.
	 * 
	 * I will personally make sure, that only common prefixes of Ores get registered at the Unificator, as of now there are:
	 * pulp, dust, dustSmall, ingot, nugget, gem, ore and block
	 * If another Mod-Author messes these up, then it's not my fault and it's especially not your fault. As these are commonly used prefixes.
	 * 
	 * This Unificator-API-Function uses the same Functions I use, for unificating Items. So if there is something messed up (very unlikely), then everything is messed up.
	 * 
	 * You shouldn't use this to unificate the Inputs of your Recipes, this is only meant for the Outputs.
	 * 
	 * @param aOreStack the Stack you want to get unificated. It is stackSize Sensitive.
	 * @return Either an unificated Stack or the stack you toss in, but it should never be null, unless you throw a Nullpointer into it.
	 */
	public static ItemStack getUnificatedOreDictStack(ItemStack aOreStack) {
		if (!GregTech_API.sPreloadFinished) GT_Log.err.println("GregTech_API ERROR: " + aOreStack.getItem() + "." + aOreStack.getItemDamage() + " - OreDict Unification Entries are not registered now, please call it in the postload phase.");
		return GT_OreDictUnificator.get(true, aOreStack);
	}
	
	/**
	 * Causes a Machineblock Update
	 * This update will cause surrounding Multi-Block Machines to update their Configuration.
	 * You should call this Function in @Block.breakBlock and in @Block.onBlockAdded of your Machine.
	 * @param aWorld is being the World
	 * @param aX is the X-Coord of the update causing Block
	 * @param aY is the Y-Coord of the update causing Block
	 * @param aZ is the Z-Coord of the update causing Block
	 */
	public static boolean causeMachineUpdate(World aWorld, int aX, int aY, int aZ) {
		stepToUpdateMachine(aWorld, aX, aY, aZ, new ArrayList<ChunkPosition>());
		return true;
	}
	
	private static void stepToUpdateMachine(World aWorld, int aX, int aY, int aZ, ArrayList<ChunkPosition> aList) {
		aList.add(new ChunkPosition(aX, aY, aZ));
		TileEntity tTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
		if (tTileEntity != null && tTileEntity instanceof IMachineBlockUpdateable) {
			((IMachineBlockUpdateable)tTileEntity).onMachineBlockUpdate();
		}
		if (aList.size() < 5 || (tTileEntity != null && tTileEntity instanceof IMachineBlockUpdateable) || GregTech_API.isMachineBlock(Block.blocksList[aWorld.getBlockId(aX, aY, aZ)], aWorld.getBlockMetadata(aX, aY, aZ))) {
			if (!aList.contains(new ChunkPosition(aX + 1, aY, aZ))) stepToUpdateMachine(aWorld, aX + 1, aY, aZ, aList);
			if (!aList.contains(new ChunkPosition(aX - 1, aY, aZ))) stepToUpdateMachine(aWorld, aX - 1, aY, aZ, aList);
			if (!aList.contains(new ChunkPosition(aX, aY + 1, aZ))) stepToUpdateMachine(aWorld, aX, aY + 1, aZ, aList);
			if (!aList.contains(new ChunkPosition(aX, aY - 1, aZ))) stepToUpdateMachine(aWorld, aX, aY - 1, aZ, aList);
			if (!aList.contains(new ChunkPosition(aX, aY, aZ + 1))) stepToUpdateMachine(aWorld, aX, aY, aZ + 1, aList);
			if (!aList.contains(new ChunkPosition(aX, aY, aZ - 1))) stepToUpdateMachine(aWorld, aX, aY, aZ - 1, aList);
		}
	}
	
	/**
	 * Adds a Multi-Machine Block, like my Machine Casings for example.
	 * You should call @causeMachineUpdate in @Block.breakBlock and in @Block.onBlockAdded of your registered Block.
	 * You don't need to register TileEntities which implement @IMachineBlockUpdateable
	 * @param aID the ID of your Block
	 * @param aMeta the Metadata of the Blocks as Bitmask! -1 for all Metavalues
	 */
	public static boolean registerMachineBlock(Block aBlock, int aMeta) {
		if (GT_Utility.isBlockInvalid(aBlock)) return false;
		sMachineIDs.put(aBlock, aMeta);
		return true;
	}
	
	/**
	 * Like above but with boolean Parameters instead of a Bitmask
	 */
	public static boolean registerMachineBlock(Block aBlock, boolean... aMeta) {
		if (GT_Utility.isBlockInvalid(aBlock) || aMeta == null || aMeta.length == 0) return false;
		int rMeta = 0;
		for (byte i = 0; i < aMeta.length && i < 16; i++) if (aMeta[i]) rMeta |= (1 << i);
		sMachineIDs.put(aBlock, rMeta);
		return true;
	}
	
	/**
	 * if this Block is a Machine Update Conducting Block
	 */
	public static boolean isMachineBlock(Block aBlock, int aMeta) {
		if (GT_Utility.isBlockInvalid(aBlock)) return false;
		return (sMachineIDs.containsKey(aBlock) && (sMachineIDs.get(aBlock) & (1 << aMeta)) != 0);
	}
	
	/**
	 * Creates a new Coolant Cell Item for your Nuclear Reactor
	 */
	public static Item constructCoolantCellItem(int aID, String aUnlocalized, String aEnglish, int aMaxStore) {
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_CoolantCellIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxStore);
		} catch(Throwable e) {/*Do nothing*/}
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_CoolantCell_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxStore);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Generic_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
	}
	
	/**
	 * Creates a new Energy Armor Item
	 */
	public static Item constructElectricArmorItem(int aID, String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aDamageEnergyCost, int aSpecials, double aArmorAbsorbtionPercentage, boolean aChargeProvider, int aType, int aArmorIndex) {
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_EnergyArmorIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
		} catch(Throwable e) {/*Do nothing*/}
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_EnergyArmor_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aDamageEnergyCost, aSpecials, aArmorAbsorbtionPercentage, aChargeProvider, aType, aArmorIndex);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Generic_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
	}
	
	/**
	 * Creates a new Energy Battery Item
	 */
	public static Item constructElectricEnergyStorageItem(int aID, String aUnlocalized, String aEnglish, int aCharge, int aTransfer, int aTier, int aEmptyID, int aFullID) {
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_EnergyStoreIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
		} catch(Throwable e) {/*Do nothing*/}
		try {
			return (Item)Class.forName("gregtechmod.api.items.GT_EnergyStore_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aCharge, aTransfer, aTier, aEmptyID, aFullID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Generic_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", false);
	}
	
	/**
	 * Creates a new Hard Hammer Item
	 */
	public static GT_Tool_Item constructHardHammerItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_HardHammer_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new Crowbar Item
	 */
	public static GT_Tool_Item constructCrowbarItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_CrowbarRC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
		} catch(Throwable e) {/*Do nothing*/}
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_Crowbar_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new Wrench Item
	 */
	public static GT_Tool_Item constructWrenchItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_Wrench_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new electric Screwdriver Item
	 */
	public static GT_Tool_Item constructElectricScrewdriverItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_ScrewdriverIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new electric Wrench Item
	 */
	public static GT_Tool_Item constructElectricWrenchItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_WrenchIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}

	/**
	 * Creates a new electric Saw Item
	 */
	public static GT_Tool_Item constructElectricSawItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_SawIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new electric Drill Item
	 */
	public static GT_Tool_Item constructElectricDrillItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aToolQuality, float aToolStrength, int aEnergyConsumptionPerBlockBreak, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_DrillIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aToolQuality, aToolStrength, aEnergyConsumptionPerBlockBreak, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new electric Soldering Tool
	 */
	public static GT_Tool_Item constructElectricSolderingToolItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aEntityDamage, int aDisChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_SolderingToolIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aEntityDamage, aDisChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, aEntityDamage, false);
	}
	
	/**
	 * Creates a new empty electric Tool
	 */
	public static GT_Tool_Item constructEmptyElectricToolItem(int aID, String aUnlocalized, String aEnglish, int aMaxDamage, int aChargedGTID) {
		try {
			return (GT_Tool_Item)Class.forName("gregtechmod.api.items.GT_EmptyToolIC_Item").getConstructors()[0].newInstance(aID, aUnlocalized, aEnglish, aMaxDamage, aChargedGTID);
		} catch(Throwable e) {/*Do nothing*/}
		return new gregtechmod.api.items.GT_Tool_Item(aID, aUnlocalized, aEnglish, "Doesn't work as intended, this is a Bug", aMaxDamage, 0, false);
	}
	
	private static Class sBaseMetaTileEntityClass = null;
	
	/**
	 * This gives you a new BaseMetaTileEntity. As some Interfaces are not always loaded (Buildcraft, Univeral Electricity) I have to use Invocation at the Constructor of the BaseMetaTileEntity
	 */
	public static BaseMetaTileEntity constructBaseMetaTileEntity() {
		if (sBaseMetaTileEntityClass == null) {
			try {
				if (UE_ENERGY_COMPATIBILITY && BC_ENERGY_COMPATIBILITY && IC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityICUEMJ")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (BC_ENERGY_COMPATIBILITY && IC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityICMJ")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (UE_ENERGY_COMPATIBILITY && IC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityICUE")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (IC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityIC")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (UE_ENERGY_COMPATIBILITY && BC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityUEMJ")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (UE_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityUE")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				if (BC_ENERGY_COMPATIBILITY) return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = Class.forName("gregtechmod.api.metatileentity.BaseMetaTileEntityMJ")).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
			try {
				return (BaseMetaTileEntity)(sBaseMetaTileEntityClass = BaseMetaTileEntity.class).newInstance();
			} catch(Throwable e) {/*Do nothing*/}
		}
		
		try {
			return (BaseMetaTileEntity)(sBaseMetaTileEntityClass.newInstance());
		} catch(Throwable e) {
			GT_Log.err.println("GT_Mod: Fatal Error ocurred while initializing TileEntities, crashing Minecraft.");
			e.printStackTrace(GT_Log.err);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Registers a Cover Item, must be done Server Side too, but you have to give aCover a null-pointer there.
	 * 
	 * Best is you make a Runnable with all Cover Registrations, and add it to the Cover Registration ArrayList ontop of this File.
	 */
	public static void registerCover(ItemStack aStack, Icon aCover) {
		int tStack = GT_Utility.stackToInt(aStack);
		if (tStack != 0 && sCovers.get(tStack) == null) sCovers.put(tStack, aCover);
	}
	
	/**
	 * Registers a Cover Item with an Icon Filepath.
	 */
	public static void registerCover(ItemStack aStack, String aCover) {
		if (aCover != null && !aCover.equals("") && sBlockIcons != null) {try {
			registerCover(aStack, ((net.minecraft.client.renderer.texture.IconRegister)sBlockIcons).registerIcon(aCover));
			return;
		} catch(Throwable e) {/*Do nothing*/}}
		registerCover(aStack, (Icon)null);
	}
	
	/**
	 * Registers multiple Cover Items. I use that for the OreDict Functionality.
	 */
	public static void registerCover(Collection<ItemStack> aStackList, Icon aCover) {
		for (ItemStack tStack : aStackList) {
			registerCover(tStack, aCover);
		}
	}
	
	/**
	 * Registers multiple Cover Items. I use that for the OreDict Functionality.
	 */
	public static void registerCover(Collection<ItemStack> aStackList, String aCover) {
		for (ItemStack tStack : aStackList) {
			registerCover(tStack, aCover);
		}
	}
	
	/**
	 * This is the generic Cover behavior. Used for the default Covers, which have no Behavior.
	 */
	public static GT_CoverBehavior sGenericBehavior;
	
	/**
	 * returns a Cover behavior, guaranteed to not return null after preload
	 */
	public static GT_CoverBehavior getCoverBehavior(ItemStack aStack) {
		return getCoverBehavior(GT_Utility.stackToInt(aStack));
	}
	
	/**
	 * returns a Cover behavior, guaranteed to not return null after preload
	 */
	public static GT_CoverBehavior getCoverBehavior(int aStack) {
		GT_CoverBehavior rCover = sCoverBehaviors.get(aStack);
		if (rCover == null) return sGenericBehavior;
		return rCover;
	}
	
	/**
	 * Register a Wrench to be usable on GregTech Machines.
	 * The Wrench MUST have some kind of Durability unlike certain Buildcraft Wrenches.
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 * 
	 * -----
	 * 
	 * Returning true at isDamagable was a great Idea, KingLemming. Well played.
	 * Since the OmniWrench is just a Single-Item-Mod, people can choose if they want your infinite durability or not. So that's not really a Problem.
	 * I even have a new Config to autodisable most infinite BC Wrenches (but that one is turned off).
	 * 
	 * One last Bug for you to fix:
	 * My Autoregistration detects Railcrafts Crowbars, Buildcrafts Wrenches and alike, due to their Interfaces.
	 * Guess what now became a Crowbar by accident. Try registering the Wrench at the load phase to prevent things like that from happening.
	 * Yes, I know that "You need to register Tools in the Load Phase"-Part wasn't there before this. Sorry about that.
	 */
	public static boolean registerWrench(ItemStack aTool) {
		return registerTool(aTool, sWrenchList);
	}
	
	/**
	 * Register a Crowbar to extract Covers from Machines
	 * Crowbars are NOT Wrenches btw.
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerCrowbar(ItemStack aTool) {
		return registerTool(aTool, sCrowbarList);
	}
	
	/**
	 * Register a Screwdriver to interact directly with Machines and Covers
	 * Did I mention, that it is intentionally not possible to make a Multitool, which doesn't switch ItemID (like a Mode) all the time?
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerScrewdriver(ItemStack aTool) {
		return registerTool(aTool, sScrewdriverList);
	}
	
	/**
	 * Register a Soft Hammer to interact with Machines
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerSoftHammer(ItemStack aTool) {
		return registerTool(aTool, sSoftHammerList);
	}
	
	/**
	 * Register a Hard Hammer to interact with Machines
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerHardHammer(ItemStack aTool) {
		return registerTool(aTool, sHardHammerList);
	}
	
	/**
	 * Register a Soldering Tool to interact with Machines
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerSolderingTool(ItemStack aTool) {
		return registerTool(aTool, sSolderingToolList);
	}
	
	/**
	 * Register a Soldering Tin to interact with Soldering Tools
	 * 
	 * You need to register Tools in the Load Phase, because otherwise the Autodetection will assign a Tool Type in certain Cases during postload (When IToolWrench or similar Interfaces are implemented).
	 */
	public static boolean registerSolderingMetal(ItemStack aTool) {
		return registerTool(aTool, sSolderingMetalList);
	}
	
	/**
	 * Generic Function to add Tools to the Lists.
	 * Contains all sanity Checks for Tools, like preventing one Tool from being registered for multiple purposes as controls would override each other.
	 */
	public static boolean registerTool(ItemStack aTool, Collection<Integer> aToolList) {
		if (aTool == null || GT_Utility.isItemStackInList(aTool, sToolList) || (!aTool.getItem().isDamageable() && !GT_ModHandler.isElectricItem(aTool))) return false;
		aToolList.add(GT_Utility.stackToInt(GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aTool)));
		sToolList.add(GT_Utility.stackToInt(GT_Utility.copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aTool)));
		return true;
	}
	
	/**
	 * Adds Biomes to the Biome Lists for World Generation
	 */
	static {
		sContinentalWaterList.add(BiomeGenBase.river.biomeName);
		sContinentalWaterList.add(BiomeGenBase.frozenRiver.biomeName);
		sContinentalWaterList.add("Lake");
		sCassiteriteList.add(BiomeGenBase.taiga.biomeName);
		sCassiteriteList.add(BiomeGenBase.taigaHills.biomeName);
		sCassiteriteList.add(BiomeGenBase.icePlains.biomeName);
		sCassiteriteList.add(BiomeGenBase.iceMountains.biomeName);
		sCassiteriteList.add(BiomeGenBase.mushroomIsland.biomeName);
		sCassiteriteList.add(BiomeGenBase.mushroomIslandShore.biomeName);
		sCassiteriteList.add(BiomeGenBase.extremeHills.biomeName);
		sCassiteriteList.add(BiomeGenBase.extremeHillsEdge.biomeName);
		sCassiteriteList.add("Alps");
		sCassiteriteList.add("Glacier");
		sCassiteriteList.add("Ice Wasteland");
		sCassiteriteList.add("Flying Mountains");
		sCassiteriteList.add("Rock Mountains");
		sCassiteriteList.add("Snow Mountains");
		sCassiteriteList.add("Snow Forest");
		sCassiteriteList.add("Tall Pine Forest");
		sCassiteriteList.add("Tundra");
		sCassiteriteList.add("Snow Island");
		sCassiteriteList.add("Rock Island");
		sCassiteriteList.add("Mountain Ridge");
		sCassiteriteList.add("Volcano Island");
		sTetrahedriteList.add(BiomeGenBase.jungle.biomeName);
		sTetrahedriteList.add(BiomeGenBase.jungleHills.biomeName);
		sTetrahedriteList.add(BiomeGenBase.swampland.biomeName);
		sTetrahedriteList.add(BiomeGenBase.mushroomIsland.biomeName);
		sTetrahedriteList.add(BiomeGenBase.mushroomIslandShore.biomeName);
		sTetrahedriteList.add(BiomeGenBase.extremeHills.biomeName);
		sTetrahedriteList.add(BiomeGenBase.extremeHillsEdge.biomeName);
		sTetrahedriteList.add("Bog");
		sTetrahedriteList.add("Cliffs");
		sTetrahedriteList.add("Valley");
		sTetrahedriteList.add("Canyon");
		sTetrahedriteList.add("Flying Mountains");
		sTetrahedriteList.add("Rock Mountains");
		sTetrahedriteList.add("Jungle Island");
		sTetrahedriteList.add("Rock Island");
		sTetrahedriteList.add("Mountain Ridge");
		sTetrahedriteList.add("Volcano Island");
		sRubyList.add(BiomeGenBase.desert.biomeName);
		sRubyList.add(BiomeGenBase.desertHills.biomeName);
		sRubyList.add("Sahel");
		sRubyList.add("Dunes");
		sRubyList.add("Desert Mountains");
		sRubyList.add("Mountainous Desert");
		sRubyList.add("Mountain Ridge");
		sRubyList.add("Savanna");
		sRubyList.add("Savannah");
		sRubyList.add("Shrubland");
		sRubyList.add("Wasteland");
		sRubyList.add("Fire Swamp");
		sRubyList.add("Highlands");
		sRubyList.add("Volcano");
		sRubyList.add("Steppe");
		sRubyList.add("Scrubland");
		sRubyList.add("Prairie");
		sRubyList.add("Lush Desert");
		sRubyList.add("Deadlands");
		sRubyList.add("Badlands");
		sRubyList.add("Dunes");
		sRubyList.add("Mesa");
		sRubyList.add("Outback");
		sRubyList.add("Canyon");
		sRubyList.add("Desert Island");
		sRubyList.add("Volcano Island");
		sRubyList.add("Desert Oil Field");
		sSapphireList.add(BiomeGenBase.ocean.biomeName);
		sSapphireList.add(BiomeGenBase.beach.biomeName);
		sSapphireList.add(BiomeGenBase.frozenOcean.biomeName);
		sSapphireList.add("Twilight Lake");
		sSapphireList.add("Twilight Stream");
		sSapphireList.add("Lake Border");
		sSapphireList.add("Glacier");
		sSapphireList.add("Mangrove");
		sSapphireList.add("Oasis");
		sSapphireList.add("Origin Valley");
		sSapphireList.add("Sacred Springs");
		sSapphireList.add("Tropics");
		sSapphireList.add("Tropical Islands");
		sSapphireList.add("Estuary");
		sSapphireList.add("Ocean Oil Field");
		sBauxiteList.add(BiomeGenBase.plains.biomeName);
		sBauxiteList.add(BiomeGenBase.forest.biomeName);
		sBauxiteList.add(BiomeGenBase.forestHills.biomeName);
		sBauxiteList.add("Bald Hill");
		sBauxiteList.add("Forest Island");
		sBauxiteList.add("Pinelands");
		sBauxiteList.add("Highlands");
		sBauxiteList.add("Lowlands");
		sBauxiteList.add("Rainforest");
		sBauxiteList.add("Autumn Woods");
		sBauxiteList.add("Autumn Forest");
		sBauxiteList.add("Birch Forest");
		sBauxiteList.add("Birch Hills");
		sBauxiteList.add("Forested Hills");
		sBauxiteList.add("Forested Island");
		sBauxiteList.add("Green Hills");
		sBauxiteList.add("Meadow");
		sBauxiteList.add("Redwood Forest");
		sBauxiteList.add("Redwood Lush");
		sBauxiteList.add("Woodlands");
		sBauxiteList.add("Twilight Forest");
		sBauxiteList.add("Dense Twilight Forest");
		sBauxiteList.add("Dark Forest");
		sBauxiteList.add("Enchanted Forest");
		sBauxiteList.add("Woodland");
		sBauxiteList.add("Woodlands");
		sBauxiteList.add("Woodland Mountains");
		sBauxiteList.add("Thicket");
		sBauxiteList.add("Seasonal Forest");
		sBauxiteList.add("Redwood Forest");
		sBauxiteList.add("Quagmire");
		sBauxiteList.add("Orchard");
		sBauxiteList.add("Pasture");
		sBauxiteList.add("Mystic Grove");
		sBauxiteList.add("Meadow");
		sBauxiteList.add("Marsh");
		sBauxiteList.add("Maple Woods");
		sBauxiteList.add("Grove");
		sBauxiteList.add("Grassland");
		sBauxiteList.add("Garden");
		sBauxiteList.add("Fungi Forest");
		sBauxiteList.add("Deciduous Forest");
		sBauxiteList.add("Coniferous Forest");
		sBauxiteList.add("Cherry Blossom Grove");
		sNickelList.add(BiomeGenBase.extremeHills.biomeName);
		sNickelList.add(BiomeGenBase.extremeHillsEdge.biomeName);
		sNickelList.add("Alps");
		sNickelList.add("Flying Mountains");
		sNickelList.add("Rock Mountains");
		sNickelList.add("Snow Mountains");
		sNickelList.add("Rock Island");
		sNickelList.add("Mountain Ridge");
		sNickelList.add("Volcano Island");
		sNickelList.add("Dark Forest");
		sNickelList.add("Mountainous Desert");
		sNickelList.add("Volcano");
		
		sDimensionalList.add(-1);
		sDimensionalList.add( 0);
		sDimensionalList.add( 1);
		
		sSoundList.put(  0, "random.break");
		sSoundList.put(  1, "random.anvil_use");
		sSoundList.put(  2, "random.anvil_break");
		sSoundList.put(  3, "random.click");
		sSoundList.put(  4, "random.fizz");
		sSoundList.put(  5, "random.explode");
		
		sSoundList.put(100, GregTech_API.IC2_MOD_ID + ":" + "tools.Wrench");
		sSoundList.put(101, GregTech_API.IC2_MOD_ID + ":" + "tools.RubberTrampoline");
		sSoundList.put(102, GregTech_API.IC2_MOD_ID + ":" + "tools.Painter");
		sSoundList.put(103, GregTech_API.IC2_MOD_ID + ":" + "tools.BatteryUse");
		sSoundList.put(104, GregTech_API.IC2_MOD_ID + ":" + "tools.chainsaw.ChainsawUseOne");
		sSoundList.put(105, GregTech_API.IC2_MOD_ID + ":" + "tools.chainsaw.ChainsawUseTwo");
		sSoundList.put(106, GregTech_API.IC2_MOD_ID + ":" + "tools.drill.DrillSoft");
		sSoundList.put(107, GregTech_API.IC2_MOD_ID + ":" + "tools.drill.DrillHard");
		sSoundList.put(108, GregTech_API.IC2_MOD_ID + ":" + "tools.ODScanner");
		
		sSoundList.put(200, GregTech_API.IC2_MOD_ID + ":" + "machines.ExtractorOp");
		sSoundList.put(201, GregTech_API.IC2_MOD_ID + ":" + "machines.MaceratorOp");
		sSoundList.put(202, GregTech_API.IC2_MOD_ID + ":" + "machines.InductionLoop");
		sSoundList.put(203, GregTech_API.IC2_MOD_ID + ":" + "machines.CompressorOp");
		sSoundList.put(204, GregTech_API.IC2_MOD_ID + ":" + "machines.RecyclerOp");
		sSoundList.put(205, GregTech_API.IC2_MOD_ID + ":" + "machines.MinerOp");
		sSoundList.put(206, GregTech_API.IC2_MOD_ID + ":" + "machines.PumpOp");
		sSoundList.put(207, GregTech_API.IC2_MOD_ID + ":" + "machines.ElectroFurnaceLoop");
		sSoundList.put(208, GregTech_API.IC2_MOD_ID + ":" + "machines.InductionLoop");
		sSoundList.put(209, GregTech_API.IC2_MOD_ID + ":" + "machines.MachineOverload");
		sSoundList.put(210, GregTech_API.IC2_MOD_ID + ":" + "machines.InterruptOne");
		sSoundList.put(211, GregTech_API.IC2_MOD_ID + ":" + "machines.KaChing");
		sSoundList.put(212, GregTech_API.IC2_MOD_ID + ":" + "machines.MagnetizerLoop");
	}
}