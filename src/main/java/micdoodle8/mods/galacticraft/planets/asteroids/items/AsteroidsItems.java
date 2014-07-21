package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.item.Item;

public class AsteroidsItems
{
	public static Item grapple;
	public static Item tier3Rocket;
	public static Item thermalPadding;
    public static Item basicItem;
    public static Item methaneCanister;
    public static Item canisterLOX;
    public static Item canisterLN2;
    public static Item atmosphericValve;
    public static ItemHeavyNoseCone heavyNoseCone;

	public static void initItems()
	{
		AsteroidsItems.grapple = new ItemGrappleHook("grapple");
		AsteroidsItems.tier3Rocket = new ItemTier3Rocket("itemTier3Rocket");
		AsteroidsItems.thermalPadding = new ItemThermalPadding("thermalPadding");
        AsteroidsItems.basicItem = new ItemBasicAsteroids();
        AsteroidsItems.methaneCanister = new ItemCanisterMethane("methaneCanisterPartial");
        AsteroidsItems.canisterLOX = new ItemCanisterLiquidOxygen("canisterPartialLOX");
        AsteroidsItems.canisterLN2 = new ItemCanisterLiquidNitrogen("canisterPartialLN2");
        AsteroidsItems.atmosphericValve = new ItemAtmosphericValve("atmosphericValve");
        AsteroidsItems.heavyNoseCone = new ItemHeavyNoseCone("heavyNoseCone");

		AsteroidsItems.registerItems();
	}

	private static void registerItems()
	{
        registerItem(AsteroidsItems.grapple);
        registerItem(AsteroidsItems.tier3Rocket);
        registerItem(AsteroidsItems.thermalPadding);
        registerItem(AsteroidsItems.basicItem);
        registerItem(AsteroidsItems.methaneCanister);
        registerItem(AsteroidsItems.canisterLOX);
        registerItem(AsteroidsItems.canisterLN2);
        registerItem(AsteroidsItems.atmosphericValve);
        registerItem(AsteroidsItems.heavyNoseCone);
	}

    private static void registerItem(Item item)
    {
        GameRegistry.registerItem(item, item.getUnlocalizedName(), Constants.MOD_ID_PLANETS);
    }
}
