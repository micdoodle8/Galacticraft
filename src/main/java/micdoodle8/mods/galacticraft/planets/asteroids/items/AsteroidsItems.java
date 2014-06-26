package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class AsteroidsItems
{
	public static Item itemGrapple;
	public static Item itemTier3Rocket;
	public static Item itemThermalPadding;

	public static void initItems()
	{
		AsteroidsItems.itemGrapple = new ItemGrappleHook("grapple");
		AsteroidsItems.itemTier3Rocket = new ItemTier3Rocket("itemTier3Rocket");
		AsteroidsItems.itemThermalPadding = new ItemThermalPadding("thermalPadding");

		AsteroidsItems.registerItems();
	}

	private static void registerItems()
	{
		GameRegistry.registerItem(AsteroidsItems.itemGrapple, AsteroidsItems.itemGrapple.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerItem(AsteroidsItems.itemTier3Rocket, AsteroidsItems.itemTier3Rocket.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerItem(AsteroidsItems.itemThermalPadding, AsteroidsItems.itemThermalPadding.getUnlocalizedName(), GalacticraftPlanets.MODID);
	}
}
