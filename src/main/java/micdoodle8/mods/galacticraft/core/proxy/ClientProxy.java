package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEffectHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InventoryTabGalacticraft;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import micdoodle8.mods.galacticraft.core.tick.GCCoreKeyHandlerClient;
import net.minecraft.item.EnumRarity;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
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
		
		FMLCommonHandler.instance().bus().register(new GCCoreKeyHandlerClient());
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion)
	{
		this.spawnParticle(particleID, position, motion, new Vector3(0, 0, 0));
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Vector3 color)
	{
		GCCoreEffectHandler.spawnParticle(particleID, position, motion, color);
	}
}
