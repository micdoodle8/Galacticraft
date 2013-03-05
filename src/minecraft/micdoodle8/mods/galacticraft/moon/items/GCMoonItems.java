package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.client.ClientProxyMoon;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMoonItems
{
	public static Item cheeseCurd;
	public static Item meteoricIronRaw;
	public static Item meteoricIronIngot;
    public static Item cheeseBlock;
	
	public static void initItems()
	{
		GCMoonItems.cheeseCurd = new GCMoonItemCheese(GCMoonConfigManager.idItemCheeseCurd, 1, 0.1F, false).setIconIndex(0).setItemName("cheeseCurd");
		GCMoonItems.cheeseBlock = new GCMoonItemReed(GCMoonConfigManager.idItemBlockCheese, GCMoonBlocks.cheeseBlock).setMaxStackSize(1).setIconIndex(1).setItemName("cheeseBlock");
		GCMoonItems.meteoricIronRaw = new GCMoonItem(GCMoonConfigManager.idItemMeteoricIronRaw).setIconIndex(2).setItemName("meteoricIronRaw");
		GCMoonItems.meteoricIronIngot = new GCMoonItem(GCMoonConfigManager.idItemMeteoricIronIngot).setIconIndex(3).setItemName("meteoricIronIngot");
	}
}
