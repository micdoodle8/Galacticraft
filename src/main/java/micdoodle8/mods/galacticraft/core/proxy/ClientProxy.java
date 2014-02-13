package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.client.EnumHelperClient;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public static GCCoreInventoryExtended dummyInventory = new GCCoreInventoryExtended();
	
	public static EnumRarity galacticraftItem = EnumRarity.common;//EnumHelperClient.addRarity("GCRarity", 9, "Space");
	
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		if (!Loader.isModLoaded("TConstruct") || TabRegistry.getTabList().size() < 3)
		{
			TabRegistry.registerTab(new InventoryTabVanilla());
		}

		TabRegistry.registerTab(new InventoryTabGalacticraft());
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
}
