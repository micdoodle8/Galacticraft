package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class AsteroidsItems
{
	public static Item itemGrapple;
	
	public static void initItems()
	{
		AsteroidsItems.itemGrapple = new ItemGrappleHook("grapple");
		
		AsteroidsItems.registerItems();
	}
	
	private static void registerItems()
	{
		GameRegistry.registerItem(AsteroidsItems.itemGrapple, AsteroidsItems.itemGrapple.getUnlocalizedName(), GalacticraftPlanets.MODID);
	}
}
