package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.item.Item;

public class AsteroidsItems
{
	public static Item grapple;
	public static Item tier3Rocket;
	public static Item thermalPadding;
    public static Item basicItem;
    public static ItemHeavyNoseCone heavyNoseCone;

	public static void initItems()
	{
		AsteroidsItems.grapple = new ItemGrappleHook("grapple");
		AsteroidsItems.tier3Rocket = new ItemTier3Rocket("itemTier3Rocket");
		AsteroidsItems.thermalPadding = new ItemThermalPadding("thermalPadding");
        AsteroidsItems.basicItem = new ItemBasicAsteroids();
        AsteroidsItems.heavyNoseCone = new ItemHeavyNoseCone("heavyNoseCone");

		AsteroidsItems.registerItems();
	}

	private static void registerItems()
	{
		GameRegistry.registerItem(AsteroidsItems.grapple, AsteroidsItems.grapple.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerItem(AsteroidsItems.tier3Rocket, AsteroidsItems.tier3Rocket.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerItem(AsteroidsItems.thermalPadding, AsteroidsItems.thermalPadding.getUnlocalizedName(), GalacticraftPlanets.MODID);
        GameRegistry.registerItem(AsteroidsItems.basicItem, AsteroidsItems.basicItem.getUnlocalizedName(), GalacticraftPlanets.MODID);
        GameRegistry.registerItem(AsteroidsItems.heavyNoseCone, AsteroidsItems.heavyNoseCone.getUnlocalizedName(), GalacticraftPlanets.MODID);
	}
}
