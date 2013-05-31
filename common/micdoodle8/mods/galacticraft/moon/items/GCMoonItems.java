package micdoodle8.mods.galacticraft.moon.items;

import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import net.minecraft.item.Item;

public class GCMoonItems
{
	public static Item cheeseCurd;
	public static Item meteoricIronRaw;
	public static Item meteoricIronIngot;
    public static Item cheeseBlock;

	public static void initItems()
	{
		GCMoonItems.cheeseCurd = 			new GCMoonItemCheese(GCMoonConfigManager.idItemCheeseCurd, 1, 0.1F, false)				.setUnlocalizedName("cheeseCurd");
		GCMoonItems.cheeseBlock = 			new GCMoonItemReed(GCMoonConfigManager.idItemBlockCheese, GCMoonBlocks.cheeseBlock)		.setUnlocalizedName("cheeseBlock");
		GCMoonItems.meteoricIronRaw = 		new GCMoonItem(GCMoonConfigManager.idItemMeteoricIronRaw, "meteoric_iron_raw")			.setUnlocalizedName("meteoricIronRaw");
		GCMoonItems.meteoricIronIngot = 	new GCMoonItem(GCMoonConfigManager.idItemMeteoricIronIngot, "meteoric_iron_ingot")		.setUnlocalizedName("meteoricIronIngot");
	}
}
