package micdoodle8.mods.galacticraft;

import java.io.File;
import java.util.logging.Level;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCConfigManager 
{
	public static boolean loaded;
	
	static Configuration configuration;
	
	public GCConfigManager(File file)
	{
		if (!loaded)
		{
			configuration = new Configuration(file);
			setDefaultValues();
		}
	}
	
	public static int dimensionIDMars;

	public static int idBlockBreatheableAir;
	public static int idBlockPowerCrystal;
	//
	public static int idBlockMarsPortalFrame;
	public static int idBlockMarsPortalCenter;
	public static int idBlockMarsStone;
	public static int idBlockMarsGrass;
	public static int idBlockMarsDirt;
	public static int idBlockOreDesh;
	public static int idBlockOreQuandrium;
	public static int idBlockOreRhodium;
	public static int idBlockOreElectrum;
	public static int idBlockOreGreenstone;
	public static int idBlockOreGreenstoneGlowing;
	public static int idBlockMarsCobblestone;
	public static int idBlockCreeperEgg;
	public static int idBlockCreeperDungeonWall;
	public static int idBlockTreasureChest;
	public static int idBlockOreAluminum;
	public static int idBlockOreCopper;
	public static int idBlockOreTitanium;
	public static int idBlockLandingPad;
	public static int idBlockBacterialSludgeMoving;
	public static int idBlockBacterialSludgeStill;
	public static int idBlockUnlitTorch;
	public static int idBlockUnlitTorchLit;
	public static int idBlockAirDistributor;
	public static int idBlockAirDistributorActive;
	public static int idBlockAirPipe;

	public static int idItemLightOxygenTank;
	public static int idItemLightOxygenTankEmpty;
	public static int idItemMedOxygenTank;
	public static int idItemMedOxygenTankEmpty;
	public static int idItemHeavyOxygenTank;
	public static int idItemHeavyOxygenTankEmpty;
	public static int idItemGalacticOrb;
	public static int idItemSpaceship;
	
	public static int idArmorOxygenMask;
	public static int idBlueCrystal;
	public static int idRedCrystal;
	public static int idPurpleCrystal;
	public static int idGreenCrystal;
	//
	public static int idItemMarsTeleporter;
	public static int idItemRawDesh;
	public static int idItemGreenstone;
	public static int idItemIngotDesh;
	public static int idItemIngotElectrum;
	public static int idItemIngotQuandrium;
	public static int idItemIngotRhodium;
	public static int idItemDeshStick;
	
	public static int idToolElectrumSword;
	
	public static int idToolDeshSword;
	public static int idToolDeshPickaxe;
	public static int idToolDeshAxe;
	public static int idToolDeshSpade;
	public static int idToolDeshHoe;
	
	public static int idToolPlanetStoneSword;
	public static int idToolPlanetStonePickaxe;
	public static int idToolPlanetStoneAxe;
	public static int idToolPlanetStoneSpade;
	public static int idToolPlanetStoneHoe;
	
	public static int idToolQuandriumSword;
	public static int idToolQuandriumPickaxe;
	public static int idToolQuandriumAxe;
	public static int idToolQuandriumSpade;
	public static int idToolQuandriumHoe;
	
	public static int idToolRhodiumSword;
	public static int idToolRhodiumPickaxe;
	public static int idToolRhodiumAxe;
	public static int idToolRhodiumSpade;
	public static int idToolRhodiumHoe;

	public static int idGuiTankRefill;
	public static int idGuiAirDistributor;
	
	private void setDefaultValues()
    {
		try
		{
	        configuration.load();
	        
	        //Dimensions
			dimensionIDMars = Integer.parseInt(configuration.getOrCreateIntProperty("idDimensionMars", "Dimension", 29).value);

			//Blocks
			idBlockBreatheableAir = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockBreatheableAir", configuration.CATEGORY_BLOCK, 199).value);
	        idBlockPowerCrystal = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockPowerCrystal", configuration.CATEGORY_BLOCK, 200).value);
			idBlockMarsPortalFrame = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsPortalFrame", configuration.CATEGORY_BLOCK, 201).value);
			idBlockMarsPortalCenter = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsPortalCenter", configuration.CATEGORY_BLOCK, 202).value);
			idBlockMarsStone = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsStone", configuration.CATEGORY_BLOCK, 203).value);
			idBlockMarsGrass = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsGrass", configuration.CATEGORY_BLOCK, 204).value);
			idBlockMarsDirt = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsDirt", configuration.CATEGORY_BLOCK, 205).value);
			idBlockOreDesh = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreDesh", configuration.CATEGORY_BLOCK, 206).value);
			idBlockOreGreenstone = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreGreenstone", configuration.CATEGORY_BLOCK, 207).value);
			idBlockOreGreenstoneGlowing = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreGreenstoneGlowing", configuration.CATEGORY_BLOCK, 208).value);
			idBlockOreRhodium = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreRhodium", configuration.CATEGORY_BLOCK, 209).value);
			idBlockOreQuandrium = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreQuandrium", configuration.CATEGORY_BLOCK, 210).value);
			idBlockOreElectrum = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockOreElectrum", configuration.CATEGORY_BLOCK, 211).value);
			idBlockMarsCobblestone = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockMarsCobblestone", configuration.CATEGORY_BLOCK, 212).value);
	        idBlockCreeperEgg = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockCreeperEgg", configuration.CATEGORY_BLOCK, 213).value);
	        idBlockCreeperDungeonWall = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockCreeperDungeonWall", configuration.CATEGORY_BLOCK, 214).value);
	        idBlockTreasureChest = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockTreasureChest", configuration.CATEGORY_BLOCK, 215).value);
	        idBlockLandingPad = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockLandingPad", configuration.CATEGORY_BLOCK, 216).value);
	        idBlockBacterialSludgeMoving = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockBacterialSludgeMoving", configuration.CATEGORY_BLOCK, 217).value);
	        idBlockBacterialSludgeStill = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockBacterialSludgeStill", configuration.CATEGORY_BLOCK, 218).value);
	        idBlockUnlitTorch = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockUnlitTorch", configuration.CATEGORY_BLOCK, 219).value);
	        idBlockUnlitTorchLit = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockUnlitTorchLit", configuration.CATEGORY_BLOCK, 220).value);
	        idBlockAirDistributor = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockAirDistributor", configuration.CATEGORY_BLOCK, 221).value);
	        idBlockAirDistributorActive = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockAirDistributorActive", configuration.CATEGORY_BLOCK, 222).value);
	        idBlockAirPipe = Integer.parseInt(configuration.getOrCreateIntProperty("idBlockAirPipe", configuration.CATEGORY_BLOCK, 223).value);

	        //Items
	        idItemLightOxygenTankEmpty = Integer.parseInt(configuration.getOrCreateIntProperty("idItemLightOxygenTankEmpty", configuration.CATEGORY_ITEM, 9854).value);
	        idItemLightOxygenTank = Integer.parseInt(configuration.getOrCreateIntProperty("idItemLightOxygenTank", configuration.CATEGORY_ITEM, 9855).value);
	        idItemMedOxygenTankEmpty = Integer.parseInt(configuration.getOrCreateIntProperty("idItemMedOxygenTankEmpty", configuration.CATEGORY_ITEM, 9856).value);
	        idItemMedOxygenTank = Integer.parseInt(configuration.getOrCreateIntProperty("idItemMedOxygenTank", configuration.CATEGORY_ITEM, 9857).value);
	        idItemHeavyOxygenTankEmpty = Integer.parseInt(configuration.getOrCreateIntProperty("idItemHeavyOxygenTankEmpty", configuration.CATEGORY_ITEM, 9858).value);
	        idItemHeavyOxygenTank = Integer.parseInt(configuration.getOrCreateIntProperty("idItemHeavyOxygenTank", configuration.CATEGORY_ITEM, 9859).value);
	        idItemGalacticOrb = Integer.parseInt(configuration.getOrCreateIntProperty("idItemGalacticOrb", configuration.CATEGORY_ITEM, 9860).value);
	        idArmorOxygenMask = Integer.parseInt(configuration.getOrCreateIntProperty("idArmorOxygenMask", configuration.CATEGORY_ITEM, 9861).value);
	        idBlueCrystal = Integer.parseInt(configuration.getOrCreateIntProperty("idBlueCrystal", configuration.CATEGORY_ITEM, 9862).value);
	        idRedCrystal = Integer.parseInt(configuration.getOrCreateIntProperty("idRedCrystal", configuration.CATEGORY_ITEM, 9863).value);
	        idPurpleCrystal = Integer.parseInt(configuration.getOrCreateIntProperty("idPurpleCrystal", configuration.CATEGORY_ITEM, 9864).value);
	        idGreenCrystal = Integer.parseInt(configuration.getOrCreateIntProperty("idGreenCrystal", configuration.CATEGORY_ITEM, 9865).value);
	        idItemSpaceship = Integer.parseInt(configuration.getOrCreateIntProperty("idItemSpaceship", configuration.CATEGORY_ITEM, 9866).value);
			idItemMarsTeleporter = Integer.parseInt(configuration.getOrCreateIntProperty("idItemGalacticOrb", configuration.CATEGORY_ITEM, 9867).value);
	        
			//GUI
	        idGuiTankRefill = Integer.parseInt(configuration.getOrCreateIntProperty("GuiIdTankRefill", "GUI", 128).value);
	        idGuiAirDistributor = Integer.parseInt(configuration.getOrCreateIntProperty("idGuiAirDistributor", "GUI", 129).value);
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "GalacticraftMars has a problem loading it's configuration");
		}
		finally 
		{
			configuration.save();
			loaded = true;
		}
    }
}
