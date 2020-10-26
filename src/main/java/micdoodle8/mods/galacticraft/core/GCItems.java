package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.items.*;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCItems
{
    @ObjectHolder(ItemNames.oxTankLight)
    public static Item oxTankLight;
    @ObjectHolder(ItemNames.oxTankMedium)
    public static Item oxTankMedium;
    @ObjectHolder(ItemNames.oxTankHeavy)
    public static Item oxTankHeavy;
    @ObjectHolder(ItemNames.oxMask)
    public static Item oxMask;
    @ObjectHolder(ItemNames.rocketTierOne)
    public static Item rocketTierOne;
    @ObjectHolder(ItemNames.rocketTierOneCargo1)
    public static Item rocketTierOneCargo1;
    @ObjectHolder(ItemNames.rocketTierOneCargo2)
    public static Item rocketTierOneCargo2;
    @ObjectHolder(ItemNames.rocketTierOneCargo3)
    public static Item rocketTierOneCargo3;
    @ObjectHolder(ItemNames.rocketTierOneCreative)
    public static Item rocketTierOneCreative;
    @ObjectHolder(ItemNames.sensorGlasses)
    public static Item sensorGlasses;
    @ObjectHolder(ItemNames.sensorLens)
    public static Item sensorLens;
    @ObjectHolder(ItemNames.steelPickaxe)
    public static Item steelPickaxe;
    @ObjectHolder(ItemNames.steelAxe)
    public static Item steelAxe;
    @ObjectHolder(ItemNames.steelHoe)
    public static Item steelHoe;
    @ObjectHolder(ItemNames.steelSpade)
    public static Item steelSpade;
    @ObjectHolder(ItemNames.steelSword)
    public static Item steelSword;
    @ObjectHolder(ItemNames.steelHelmet)
    public static Item steelHelmet;
    @ObjectHolder(ItemNames.steelChestplate)
    public static Item steelChestplate;
    @ObjectHolder(ItemNames.steelLeggings)
    public static Item steelLeggings;
    @ObjectHolder(ItemNames.steelBoots)
    public static Item steelBoots;
    @ObjectHolder(ItemNames.oxygenVent)
    public static Item oxygenVent;
    @ObjectHolder(ItemNames.oxygenFan)
    public static Item oxygenFan;
    @ObjectHolder(ItemNames.oxygenConcentrator)
    public static Item oxygenConcentrator;
    //    @ObjectHolder(ItemNames.rocketEngine) public static Item rocketEngine;
    @ObjectHolder(ItemNames.heavyPlatingTier1)
    public static Item heavyPlatingTier1;
    @ObjectHolder(ItemNames.partNoseCone)
    public static Item partNoseCone;
    @ObjectHolder(ItemNames.partFins)
    public static Item partFins;
    @ObjectHolder(ItemNames.buggy)
    public static Item buggy;
    @ObjectHolder(ItemNames.buggyInventory1)
    public static Item buggyInventory1;
    @ObjectHolder(ItemNames.buggyInventory2)
    public static Item buggyInventory2;
    @ObjectHolder(ItemNames.buggyInventory3)
    public static Item buggyInventory3;
    @ObjectHolder(ItemNames.flag)
    public static Item flag;
    @ObjectHolder(ItemNames.oxygenGear)
    public static Item oxygenGear;
    @ObjectHolder(ItemNames.canvas)
    public static Item canvas;
    @ObjectHolder(ItemNames.flagPole)
    public static Item flagPole;
    @ObjectHolder(ItemNames.oilCanister)
    public static Item oilCanister;
    @ObjectHolder(ItemNames.fuelCanister)
    public static Item fuelCanister;
    @ObjectHolder(ItemNames.oxygenCanisterInfinite)
    public static Item oxygenCanisterInfinite;
    @ObjectHolder(ItemNames.schematicBuggy)
    public static Item schematicBuggy;
    @ObjectHolder(ItemNames.schematicRocketT2)
    public static Item schematicRocketT2;
    @ObjectHolder(ItemNames.key)
    public static Item key;
    //    @ObjectHolder(ItemNames.foodItem) public static Item foodItem;
    @ObjectHolder(ItemNames.battery)
    public static Item battery;
    @ObjectHolder(ItemNames.infiniteBatery)
    public static Item infiniteBatery;
    @ObjectHolder(ItemNames.wrench)
    public static Item wrench;
    @ObjectHolder(ItemNames.cheeseCurd)
    public static Item cheeseCurd;
    @ObjectHolder(ItemNames.meteoricIronRaw)
    public static Item meteoricIronRaw;
    @ObjectHolder(ItemNames.cheeseBlock)
    public static Item cheeseBlock;
    @ObjectHolder(ItemNames.prelaunchChecklist)
    public static Item prelaunchChecklist;
    @ObjectHolder(ItemNames.dungeonFinder)
    public static Item dungeonFinder;
    //    @ObjectHolder(ItemNames.ic2compat) public static Item ic2compat;
    @ObjectHolder(ItemNames.emergencyKit)
    public static Item emergencyKit;
    @ObjectHolder(ItemNames.solarModule0)
    public static Item solarModule0;
    @ObjectHolder(ItemNames.solarModule1)
    public static Item solarModule1;
    @ObjectHolder(ItemNames.rawSilicon)
    public static Item rawSilicon;
    @ObjectHolder(ItemNames.ingotCopper)
    public static Item ingotCopper;
    @ObjectHolder(ItemNames.ingotTin)
    public static Item ingotTin;
    @ObjectHolder(ItemNames.ingotAluminum)
    public static Item ingotAluminum;
    @ObjectHolder(ItemNames.compressedCopper)
    public static Item compressedCopper;
    @ObjectHolder(ItemNames.compressedTin)
    public static Item compressedTin;
    @ObjectHolder(ItemNames.compressedAluminum)
    public static Item compressedAluminum;
    @ObjectHolder(ItemNames.compressedSteel)
    public static Item compressedSteel;
    @ObjectHolder(ItemNames.compressedBronze)
    public static Item compressedBronze;
    @ObjectHolder(ItemNames.compressedIron)
    public static Item compressedIron;
    @ObjectHolder(ItemNames.compressedWaferSolar)
    public static Item compressedWaferSolar;
    @ObjectHolder(ItemNames.compressedWaferBasic)
    public static Item compressedWaferBasic;
    @ObjectHolder(ItemNames.compressedWaferAdvanced)
    public static Item compressedWaferAdvanced;
    @ObjectHolder(ItemNames.frequencyModule)
    public static Item frequencyModule;
    @ObjectHolder(ItemNames.ambientThermalController)
    public static Item ambientThermalController;
    @ObjectHolder(ItemNames.buggyMaterialWheel)
    public static Item buggyMaterialWheel;
    @ObjectHolder(ItemNames.buggyMaterialSeat)
    public static Item buggyMaterialSeat;
    @ObjectHolder(ItemNames.buggyMaterialStorage)
    public static Item buggyMaterialStorage;
    @ObjectHolder(ItemNames.canisterTin)
    public static Item canisterTin;
    @ObjectHolder(ItemNames.canisterCopper)
    public static Item canisterCopper;
    @ObjectHolder(ItemNames.dehydratedApple)
    public static Item dehydratedApple;
    @ObjectHolder(ItemNames.dehydratedCarrot)
    public static Item dehydratedCarrot;
    @ObjectHolder(ItemNames.dehydratedMelon)
    public static Item dehydratedMelon;
    @ObjectHolder(ItemNames.dehydratedPotato)
    public static Item dehydratedPotato;
    @ObjectHolder(ItemNames.cheeseSlice)
    public static Item cheeseSlice;
    @ObjectHolder(ItemNames.burgerBun)
    public static Item burgerBun;
    @ObjectHolder(ItemNames.beefPattyRaw)
    public static Item beefPattyRaw;
    @ObjectHolder(ItemNames.beefPattyCooked)
    public static Item beefPattyCooked;
    @ObjectHolder(ItemNames.cheeseburger)
    public static Item cheeseburger;
    @ObjectHolder(ItemNames.cannedBeef)
    public static Item cannedBeef;
    @ObjectHolder(ItemNames.meteorChunk)
    public static Item meteorChunk;
    @ObjectHolder(ItemNames.meteorChunkHot)
    public static Item meteorChunkHot;
    @ObjectHolder(ItemNames.ingotMeteoricIron)
    public static Item ingotMeteoricIron;
    @ObjectHolder(ItemNames.compressedMeteoricIron)
    public static Item compressedMeteoricIron;
    @ObjectHolder(ItemNames.lunarSapphire)
    public static Item lunarSapphire;
    @ObjectHolder(ItemNames.parachutePlain)
    public static Item parachutePlain;
    @ObjectHolder(ItemNames.parachuteBlack)
    public static Item parachuteBlack;
    @ObjectHolder(ItemNames.parachuteBlue)
    public static Item parachuteBlue;
    @ObjectHolder(ItemNames.parachuteLime)
    public static Item parachuteLime;
    @ObjectHolder(ItemNames.parachuteBrown)
    public static Item parachuteBrown;
    @ObjectHolder(ItemNames.parachuteDarkBlue)
    public static Item parachuteDarkBlue;
    @ObjectHolder(ItemNames.parachuteDarkGray)
    public static Item parachuteDarkGray;
    @ObjectHolder(ItemNames.parachuteDarkGreen)
    public static Item parachuteDarkGreen;
    @ObjectHolder(ItemNames.parachuteGray)
    public static Item parachuteGray;
    @ObjectHolder(ItemNames.parachuteMagenta)
    public static Item parachuteMagenta;
    @ObjectHolder(ItemNames.parachuteOrange)
    public static Item parachuteOrange;
    @ObjectHolder(ItemNames.parachutePink)
    public static Item parachutePink;
    @ObjectHolder(ItemNames.parachutePurple)
    public static Item parachutePurple;
    @ObjectHolder(ItemNames.parachuteRed)
    public static Item parachuteRed;
    @ObjectHolder(ItemNames.parachuteTeal)
    public static Item parachuteTeal;
    @ObjectHolder(ItemNames.parachuteYellow)
    public static Item parachuteYellow;
    @ObjectHolder(ItemNames.rocketEngineT1)
    public static Item rocketEngineT1;
    @ObjectHolder(ItemNames.rocketBoosterT1)
    public static Item rocketBoosterT1;

//    public static ArmorMaterial ARMOR_SENSOR_GLASSES = EnumHelper.addArmorMaterial("SENSORGLASSES", "", 200, new int[] { 0, 0, 0, 0 }, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);
//    public static ArmorMaterial ARMOR_STEEL = EnumHelper.addArmorMaterial("steel", "", 30, new int[] { 3, 6, 8, 3 }, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
//    public static ToolMaterial TOOL_STEEL = EnumHelper.addToolMaterial("steel", 3, 768, 5.0F, 2, 8);

    public static ArrayList<Item> hiddenItems = new ArrayList<Item>();
    public static LinkedList<ItemCanisterGeneric> canisterTypes = new LinkedList<ItemCanisterGeneric>();
    public static HashMap<ItemStack, ItemStack> itemChanges = new HashMap<>(4, 1.0F);

    public static Item.Properties defaultBuilder()
    {
        return new Item.Properties().group(GalacticraftCore.galacticraftItemsTab);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        register(r, new ItemOxygenTank(1, defaultBuilder().maxDamage(900)), ItemNames.oxTankLight);
        register(r, new ItemOxygenTank(2, defaultBuilder().maxDamage(1800)), ItemNames.oxTankMedium);
        register(r, new ItemOxygenTank(3, defaultBuilder().maxDamage(2700)), ItemNames.oxTankHeavy);
        register(r, new ItemOxygenMask(defaultBuilder().maxStackSize(1)), ItemNames.oxMask);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.rocketTierOne);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.rocketTierOneCargo1);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.rocketTierOneCargo2);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.rocketTierOneCargo3);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.rocketTierOneCreative);
        register(r, new ItemSensorGlasses(defaultBuilder()), ItemNames.sensorGlasses);
        register(r, new ItemPickaxeGC(defaultBuilder()), ItemNames.steelPickaxe);
        register(r, new ItemAxeGC(defaultBuilder()), ItemNames.steelAxe);
        register(r, new ItemHoeGC(defaultBuilder()), ItemNames.steelHoe);
        register(r, new ItemShovelGC(defaultBuilder()), ItemNames.steelSpade);
        register(r, new ItemSwordGC(defaultBuilder()), ItemNames.steelSword);
        register(r, new ArmorItemGC(EquipmentSlotType.HEAD, defaultBuilder()), ItemNames.steelHelmet);
        register(r, new ArmorItemGC(EquipmentSlotType.CHEST, defaultBuilder()), ItemNames.steelChestplate);
        register(r, new ArmorItemGC(EquipmentSlotType.LEGS, defaultBuilder()), ItemNames.steelLeggings);
        register(r, new ArmorItemGC(EquipmentSlotType.FEET, defaultBuilder()), ItemNames.steelBoots);
        register(r, new ItemBase(defaultBuilder()), ItemNames.oxygenVent);
        register(r, new ItemBase(defaultBuilder()), ItemNames.oxygenFan);
        register(r, new ItemBase(defaultBuilder()), ItemNames.oxygenConcentrator);
        register(r, new ItemBase(defaultBuilder()), ItemNames.heavyPlatingTier1);
        register(r, new ItemBase(defaultBuilder()), ItemNames.rocketEngineT1);
        register(r, new ItemBase(defaultBuilder()), ItemNames.rocketBoosterT1);
        register(r, new ItemBase(defaultBuilder()), ItemNames.partFins);
        register(r, new ItemBase(defaultBuilder()), ItemNames.partNoseCone);
        register(r, new ItemBase(defaultBuilder()), ItemNames.sensorLens);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), ItemNames.buggy);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), ItemNames.buggyInventory1);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), ItemNames.buggyInventory2);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), ItemNames.buggyInventory3);
        register(r, new ItemFlag(defaultBuilder().maxDamage(0)), ItemNames.flag);
        register(r, new ItemOxygenGear(defaultBuilder()), ItemNames.oxygenGear);
        register(r, new ItemBase(defaultBuilder()), ItemNames.canvas);
        register(r, new ItemOilCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), ItemNames.oilCanister);
        register(r, new ItemFuelCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), ItemNames.fuelCanister);
        register(r, new ItemCanisterOxygenInfinite(defaultBuilder()), ItemNames.oxygenCanisterInfinite);
        register(r, new ItemBase(defaultBuilder()), ItemNames.flagPole);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.schematicBuggy);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), ItemNames.schematicRocketT2);
        register(r, new ItemKey(defaultBuilder()), ItemNames.key);
//        register(r, new ItemFood(defaultBuilder()), ItemNames.foodItem);
        register(r, new ItemBattery(defaultBuilder().maxDamage(ItemElectricBase.DAMAGE_RANGE)), ItemNames.battery);
        register(r, new ItemBatteryInfinite(defaultBuilder()), ItemNames.infiniteBatery);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), ItemNames.meteorChunk);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), ItemNames.meteorChunkHot);
        register(r, new ItemUniversalWrench(defaultBuilder().maxDamage(256)), ItemNames.wrench);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(1).saturation(0.1F).fastToEat().build())), ItemNames.cheeseCurd);
//		GCItems.cheeseBlock = new ItemBlockCheese(GCBlocks.cheeseBlock, "cheeseBlock");
        register(r, new ItemBase(defaultBuilder()), ItemNames.meteoricIronRaw);
        register(r, new ItemPreLaunchChecklist(defaultBuilder()), ItemNames.prelaunchChecklist);
        register(r, new ItemBase(defaultBuilder()), ItemNames.dungeonFinder);
//        register(r, new ItemIC2Compat(defaultBuilder()), ItemNames.ic2compat); TODO
        register(r, new ItemEmergencyKit(defaultBuilder().maxDamage(0)), ItemNames.emergencyKit);
        register(r, new ItemBase(defaultBuilder()), ItemNames.solarModule0);
        register(r, new ItemBase(defaultBuilder()), ItemNames.solarModule1);
        register(r, new ItemBase(defaultBuilder()), ItemNames.rawSilicon);
        register(r, new ItemBase(defaultBuilder()), ItemNames.ingotCopper);
        register(r, new ItemBase(defaultBuilder()), ItemNames.ingotTin);
        register(r, new ItemBase(defaultBuilder()), ItemNames.ingotAluminum);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedCopper);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedTin);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedAluminum);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedSteel);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedBronze);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedIron);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedWaferSolar);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedWaferBasic);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedWaferAdvanced);
        register(r, new ItemBase(defaultBuilder()), ItemNames.frequencyModule);
        register(r, new ItemBase(defaultBuilder()), ItemNames.ambientThermalController);
        register(r, new ItemBase(defaultBuilder()), ItemNames.buggyMaterialWheel);
        register(r, new ItemBase(defaultBuilder()), ItemNames.buggyMaterialSeat);
        register(r, new ItemBase(defaultBuilder()), ItemNames.buggyMaterialStorage);
        register(r, new ItemBase(defaultBuilder()), ItemNames.canisterTin);
        register(r, new ItemBase(defaultBuilder()), ItemNames.canisterCopper);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.3F).build())), ItemNames.dehydratedApple);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), ItemNames.dehydratedCarrot);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.3F).build())), ItemNames.dehydratedMelon);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), ItemNames.dehydratedPotato);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.1F).build())), ItemNames.cheeseSlice);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.8F).build())), ItemNames.burgerBun);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), ItemNames.beefPattyRaw);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.6F).build())), ItemNames.beefPattyCooked);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(14).saturation(1.0F).build())), ItemNames.cheeseburger);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), ItemNames.cannedBeef);
        register(r, new ItemBase(defaultBuilder()), ItemNames.ingotMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), ItemNames.compressedMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), ItemNames.lunarSapphire);
        Item.Properties parachuteProps = defaultBuilder().maxDamage(0).maxStackSize(1);
        register(r, new ItemParaChute(DyeColor.WHITE, parachuteProps), ItemNames.parachutePlain);
        register(r, new ItemParaChute(DyeColor.BLACK, parachuteProps), ItemNames.parachuteBlack);
        register(r, new ItemParaChute(DyeColor.LIGHT_BLUE, parachuteProps), ItemNames.parachuteBlue);
        register(r, new ItemParaChute(DyeColor.LIME, parachuteProps), ItemNames.parachuteLime);
        register(r, new ItemParaChute(DyeColor.BROWN, parachuteProps), ItemNames.parachuteBrown);
        register(r, new ItemParaChute(DyeColor.BLUE, parachuteProps), ItemNames.parachuteDarkBlue);
        register(r, new ItemParaChute(DyeColor.GRAY, parachuteProps), ItemNames.parachuteDarkGray);
        register(r, new ItemParaChute(DyeColor.GREEN, parachuteProps), ItemNames.parachuteDarkGreen);
        register(r, new ItemParaChute(DyeColor.LIGHT_GRAY, parachuteProps), ItemNames.parachuteGray);
        register(r, new ItemParaChute(DyeColor.MAGENTA, parachuteProps), ItemNames.parachuteMagenta);
        register(r, new ItemParaChute(DyeColor.ORANGE, parachuteProps), ItemNames.parachuteOrange);
        register(r, new ItemParaChute(DyeColor.PINK, parachuteProps), ItemNames.parachutePink);
        register(r, new ItemParaChute(DyeColor.PURPLE, parachuteProps), ItemNames.parachutePurple);
        register(r, new ItemParaChute(DyeColor.RED, parachuteProps), ItemNames.parachuteRed);
        register(r, new ItemParaChute(DyeColor.CYAN, parachuteProps), ItemNames.parachuteTeal);
        register(r, new ItemParaChute(DyeColor.YELLOW, parachuteProps), ItemNames.parachuteYellow);

//        GCItems.registerHarvestLevels();
//
//        GCItems.registerItems();
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 15), new ItemStack(GCItems.foodItem, 1, 0));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 16), new ItemStack(GCItems.foodItem, 1, 1));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 17), new ItemStack(GCItems.foodItem, 1, 2));
//        GCItems.itemChanges.put(new ItemStack(GCItems.basicItem, 1, 18), new ItemStack(GCItems.foodItem, 1, 3));

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
            ClientProxyCore.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
        });

        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.fuelCanister);
        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.oilCanister);
    }

//    public static void oreDictRegistrations()
//    {
//        for (int i = 0; i < ItemBasic.names.length; i++)
//        {
//            if (ItemBasic.names[i].contains("ingot") || ItemBasic.names[i].contains("compressed") || ItemBasic.names[i].contains("wafer"))
//            {
//                String name = ItemBasic.names[i];
//                while (name.contains("_"))
//                {
//                    int loc = name.indexOf("_");
//                    name = name.substring(0, loc) + name.substring(loc + 1, loc + 2).toUpperCase() + name.substring(loc + 2, name.length());
//                }
//                OreDictionary.registerOre(name, new ItemStack(GCItems.basicItem, 1, i));
//            }
//        }
//
//        OreDictionary.registerOre("foodCheese", new ItemStack(GCItems.cheeseCurd, 1, 0));
//        OreDictionary.registerOre("compressedMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 1));
//        OreDictionary.registerOre("ingotMeteoricIron", new ItemStack(GCItems.itemBasicMoon, 1, 0));
//        if (CompatibilityManager.useAluDust())
//        {
//            OreDictionary.registerOre("dustAluminum", new ItemStack(GCItems.ic2compat, 1, 0));
//            OreDictionary.registerOre("dustAluminium", new ItemStack(GCItems.ic2compat, 1, 0));
//        }
//        if (CompatibilityManager.isIc2Loaded())
//        {
//            OreDictionary.registerOre("crushedAluminum", new ItemStack(GCItems.ic2compat, 1, 2));
//            OreDictionary.registerOre("crushedPurifiedAluminum", new ItemStack(GCItems.ic2compat, 1, 1));
//            OreDictionary.registerOre("dustTinyTitanium", new ItemStack(GCItems.ic2compat, 1, 7));
//        }
//
//        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.oilCanister, Constants.MOD_ID_CORE, "oil_canister_partial", 7));
//        GalacticraftCore.proxy.registerCanister(new PartialCanister(GCItems.fuelCanister, Constants.MOD_ID_CORE, "fuel_canister_partial", 7));
//        OreDictionary.registerOre(ConfigManagerCore.otherModsSilicon.get(), new ItemStack(GCItems.basicItem, 1, 2));
//    }


//    /**
//     * Do not call this until after mod loading is complete
//     * because JEI doesn't have an internal item blacklist
//     * until it services an FMLLoadCompleteEvent.
//     * (Seriously?!)
//     */
//    public static void hideItemsJEI(IIngredientBlacklist jeiHidden)
//    {
//        if (jeiHidden != null)
//        {
//            for (ItemStack item : GCItems.itemChanges.keySet())
//            {
//                jeiHidden.addIngredientToBlacklist(item.copy());
//            }
//
//            for (Item item : GCItems.hiddenItems)
//            {
//                jeiHidden.addIngredientToBlacklist(new ItemStack(item, 1, 0));
//            }
//
//            for (Block block : GCBlocks.hiddenBlocks)
//            {
//                jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, 0));
//                if (block == GCBlocks.slabGCDouble)
//                {
//                    for (int j = 1; j < (GalacticraftCore.isPlanetsLoaded ? 7 : 4); j++)
//                        jeiHidden.addIngredientToBlacklist(new ItemStack(block, 1, j));
//                }
//            }
//        }
//    }

//
//    public static void registerHarvestLevels()
//    {
//        GCItems.steelPickaxe.setHarvestLevel("pickaxe", 4);
//        GCItems.steelAxe.setHarvestLevel("axe", 4);
//        GCItems.steelSpade.setHarvestLevel("shovel", 4);
//    } TODO

//    public static void registerItems()
//    {
//        GCItems.registerItem(GCItems.rocketTier1);
//        GCItems.registerItem(GCItems.oxMask);
//        GCItems.registerItem(GCItems.oxygenGear);
//        GCItems.registerItem(GCItems.oxTankLight);
//        GCItems.registerItem(GCItems.oxTankMedium);
//        GCItems.registerItem(GCItems.oxTankHeavy);
//        GCItems.registerItem(GCItems.oxygenCanisterInfinite);
//        GCItems.registerItem(GCItems.sensorLens);
//        GCItems.registerItem(GCItems.sensorGlasses);
//        GCItems.registerItem(GCItems.wrench);
//        GCItems.registerItem(GCItems.steelPickaxe);
//        GCItems.registerItem(GCItems.steelAxe);
//        GCItems.registerItem(GCItems.steelHoe);
//        GCItems.registerItem(GCItems.steelSpade);
//        GCItems.registerItem(GCItems.steelSword);
//        GCItems.registerItem(GCItems.steelHelmet);
//        GCItems.registerItem(GCItems.steelChestplate);
//        GCItems.registerItem(GCItems.steelLeggings);
//        GCItems.registerItem(GCItems.steelBoots);
//        GCItems.registerItem(GCItems.oxygenVent);
//        GCItems.registerItem(GCItems.oxygenFan);
//        GCItems.registerItem(GCItems.oxygenConcentrator);
//        GCItems.registerItem(GCItems.rocketEngine);
//        GCItems.registerItem(GCItems.heavyPlatingTier1);
//        GCItems.registerItem(GCItems.partNoseCone);
//        GCItems.registerItem(GCItems.partFins);
//        GCItems.registerItem(GCItems.flagPole);
//        GCItems.registerItem(GCItems.canvas);
//        GCItems.registerItem(GCItems.oilCanister);
//        GCItems.registerItem(GCItems.fuelCanister);
//        GCItems.registerItem(GCItems.schematic);
//        GCItems.registerItem(GCItems.key);
//        GCItems.registerItem(GCItems.buggy);
//        GCItems.registerItem(GCItems.foodItem);
//        GCItems.registerItem(GCItems.battery);
//        GCItems.registerItem(GCItems.infiniteBatery);
//        GCItems.registerItem(GCItems.meteorChunk);
//        GCItems.registerItem(GCItems.cheeseCurd);
//        GCItems.registerItem(GCItems.meteoricIronRaw);
////		GCItems.registerItem(GCItems.cheeseBlock);
//        GCItems.registerItem(GCItems.flag);
//        GCItems.registerItem(GCItems.prelaunchChecklist);
//        GCItems.registerItem(GCItems.dungeonFinder);
//        GCItems.registerItem(GCItems.emergencyKit);
//
//        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.fuelCanister);
//        GCItems.canisterTypes.add((ItemCanisterGeneric) GCItems.oilCanister);
//
//        if (CompatibilityManager.useAluDust()) GCItems.registerItem(GCItems.ic2compat);
//    }

//    public static void registerItem(Item item)
//    {
//        String name = item.getUnlocalizedName().substring(5);
//        if (item.getRegistryName() == null)
//        {
//            item.setRegistryName(name);
//        }
//        GCCoreUtil.registerGalacticraftItem(name, item);
//        GalacticraftCore.itemListTrue.add(item);
//        GalacticraftCore.proxy.postRegisterItem(item);
//    }
//
//    public static void registerItems(IForgeRegistry<Item> registry)
//    {
//        for (ItemStack item : GalacticraftCore.itemList)
//        {
//            registry.register(item.getItem());
//        }
//    }
}
