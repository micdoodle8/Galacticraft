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
    @ObjectHolder(GCItemNames.oxTankLight)
    public static Item oxTankLight;
    @ObjectHolder(GCItemNames.oxTankMedium)
    public static Item oxTankMedium;
    @ObjectHolder(GCItemNames.oxTankHeavy)
    public static Item oxTankHeavy;
    @ObjectHolder(GCItemNames.oxMask)
    public static Item oxMask;
    @ObjectHolder(GCItemNames.rocketTierOne)
    public static Item rocketTierOne;
    @ObjectHolder(GCItemNames.rocketTierOneCargo1)
    public static Item rocketTierOneCargo1;
    @ObjectHolder(GCItemNames.rocketTierOneCargo2)
    public static Item rocketTierOneCargo2;
    @ObjectHolder(GCItemNames.rocketTierOneCargo3)
    public static Item rocketTierOneCargo3;
    @ObjectHolder(GCItemNames.rocketTierOneCreative)
    public static Item rocketTierOneCreative;
    @ObjectHolder(GCItemNames.sensorGlasses)
    public static Item sensorGlasses;
    @ObjectHolder(GCItemNames.sensorLens)
    public static Item sensorLens;
    @ObjectHolder(GCItemNames.steelPickaxe)
    public static Item steelPickaxe;
    @ObjectHolder(GCItemNames.steelAxe)
    public static Item steelAxe;
    @ObjectHolder(GCItemNames.steelHoe)
    public static Item steelHoe;
    @ObjectHolder(GCItemNames.steelSpade)
    public static Item steelSpade;
    @ObjectHolder(GCItemNames.steelSword)
    public static Item steelSword;
    @ObjectHolder(GCItemNames.steelHelmet)
    public static Item steelHelmet;
    @ObjectHolder(GCItemNames.steelChestplate)
    public static Item steelChestplate;
    @ObjectHolder(GCItemNames.steelLeggings)
    public static Item steelLeggings;
    @ObjectHolder(GCItemNames.steelBoots)
    public static Item steelBoots;
    @ObjectHolder(GCItemNames.oxygenVent)
    public static Item oxygenVent;
    @ObjectHolder(GCItemNames.oxygenFan)
    public static Item oxygenFan;
    @ObjectHolder(GCItemNames.oxygenConcentrator)
    public static Item oxygenConcentrator;
    //    @ObjectHolder(ItemNames.rocketEngine) public static Item rocketEngine;
    @ObjectHolder(GCItemNames.heavyPlatingTier1)
    public static Item heavyPlatingTier1;
    @ObjectHolder(GCItemNames.partNoseCone)
    public static Item partNoseCone;
    @ObjectHolder(GCItemNames.partFins)
    public static Item partFins;
    @ObjectHolder(GCItemNames.buggy)
    public static Item buggy;
    @ObjectHolder(GCItemNames.buggyInventory1)
    public static Item buggyInventory1;
    @ObjectHolder(GCItemNames.buggyInventory2)
    public static Item buggyInventory2;
    @ObjectHolder(GCItemNames.buggyInventory3)
    public static Item buggyInventory3;
    @ObjectHolder(GCItemNames.flag)
    public static Item flag;
    @ObjectHolder(GCItemNames.oxygenGear)
    public static Item oxygenGear;
    @ObjectHolder(GCItemNames.canvas)
    public static Item canvas;
    @ObjectHolder(GCItemNames.flagPole)
    public static Item flagPole;
    @ObjectHolder(GCItemNames.oilCanister)
    public static Item oilCanister;
    @ObjectHolder(GCItemNames.fuelCanister)
    public static Item fuelCanister;
    @ObjectHolder(GCItemNames.oxygenCanisterInfinite)
    public static Item oxygenCanisterInfinite;
    @ObjectHolder(GCItemNames.schematicBuggy)
    public static Item schematicBuggy;
    @ObjectHolder(GCItemNames.schematicRocketT2)
    public static Item schematicRocketT2;
    @ObjectHolder(GCItemNames.key)
    public static Item key;
    //    @ObjectHolder(ItemNames.foodItem) public static Item foodItem;
    @ObjectHolder(GCItemNames.battery)
    public static Item battery;
    @ObjectHolder(GCItemNames.infiniteBatery)
    public static Item infiniteBatery;
    @ObjectHolder(GCItemNames.wrench)
    public static Item wrench;
    @ObjectHolder(GCItemNames.cheeseCurd)
    public static Item cheeseCurd;
    @ObjectHolder(GCItemNames.meteoricIronRaw)
    public static Item meteoricIronRaw;
    @ObjectHolder(GCItemNames.cheeseBlock)
    public static Item cheeseBlock;
    @ObjectHolder(GCItemNames.prelaunchChecklist)
    public static Item prelaunchChecklist;
    @ObjectHolder(GCItemNames.dungeonFinder)
    public static Item dungeonFinder;
    //    @ObjectHolder(ItemNames.ic2compat) public static Item ic2compat;
    @ObjectHolder(GCItemNames.emergencyKit)
    public static Item emergencyKit;
    @ObjectHolder(GCItemNames.solarModule0)
    public static Item solarModule0;
    @ObjectHolder(GCItemNames.solarModule1)
    public static Item solarModule1;
    @ObjectHolder(GCItemNames.rawSilicon)
    public static Item rawSilicon;
    @ObjectHolder(GCItemNames.ingotCopper)
    public static Item ingotCopper;
    @ObjectHolder(GCItemNames.ingotTin)
    public static Item ingotTin;
    @ObjectHolder(GCItemNames.ingotAluminum)
    public static Item ingotAluminum;
    @ObjectHolder(GCItemNames.compressedCopper)
    public static Item compressedCopper;
    @ObjectHolder(GCItemNames.compressedTin)
    public static Item compressedTin;
    @ObjectHolder(GCItemNames.compressedAluminum)
    public static Item compressedAluminum;
    @ObjectHolder(GCItemNames.compressedSteel)
    public static Item compressedSteel;
    @ObjectHolder(GCItemNames.compressedBronze)
    public static Item compressedBronze;
    @ObjectHolder(GCItemNames.compressedIron)
    public static Item compressedIron;
    @ObjectHolder(GCItemNames.compressedWaferSolar)
    public static Item compressedWaferSolar;
    @ObjectHolder(GCItemNames.compressedWaferBasic)
    public static Item compressedWaferBasic;
    @ObjectHolder(GCItemNames.compressedWaferAdvanced)
    public static Item compressedWaferAdvanced;
    @ObjectHolder(GCItemNames.frequencyModule)
    public static Item frequencyModule;
    @ObjectHolder(GCItemNames.ambientThermalController)
    public static Item ambientThermalController;
    @ObjectHolder(GCItemNames.buggyMaterialWheel)
    public static Item buggyMaterialWheel;
    @ObjectHolder(GCItemNames.buggyMaterialSeat)
    public static Item buggyMaterialSeat;
    @ObjectHolder(GCItemNames.buggyMaterialStorage)
    public static Item buggyMaterialStorage;
    @ObjectHolder(GCItemNames.canisterTin)
    public static Item canisterTin;
    @ObjectHolder(GCItemNames.canisterCopper)
    public static Item canisterCopper;
    @ObjectHolder(GCItemNames.dehydratedApple)
    public static Item dehydratedApple;
    @ObjectHolder(GCItemNames.dehydratedCarrot)
    public static Item dehydratedCarrot;
    @ObjectHolder(GCItemNames.dehydratedMelon)
    public static Item dehydratedMelon;
    @ObjectHolder(GCItemNames.dehydratedPotato)
    public static Item dehydratedPotato;
    @ObjectHolder(GCItemNames.cheeseSlice)
    public static Item cheeseSlice;
    @ObjectHolder(GCItemNames.burgerBun)
    public static Item burgerBun;
    @ObjectHolder(GCItemNames.beefPattyRaw)
    public static Item beefPattyRaw;
    @ObjectHolder(GCItemNames.beefPattyCooked)
    public static Item beefPattyCooked;
    @ObjectHolder(GCItemNames.cheeseburger)
    public static Item cheeseburger;
    @ObjectHolder(GCItemNames.cannedBeef)
    public static Item cannedBeef;
    @ObjectHolder(GCItemNames.meteorChunk)
    public static Item meteorChunk;
    @ObjectHolder(GCItemNames.meteorChunkHot)
    public static Item meteorChunkHot;
    @ObjectHolder(GCItemNames.ingotMeteoricIron)
    public static Item ingotMeteoricIron;
    @ObjectHolder(GCItemNames.compressedMeteoricIron)
    public static Item compressedMeteoricIron;
    @ObjectHolder(GCItemNames.lunarSapphire)
    public static Item lunarSapphire;
    @ObjectHolder(GCItemNames.parachuteWhite)
    public static Item parachutePlain;
    @ObjectHolder(GCItemNames.parachuteBlack)
    public static Item parachuteBlack;
    @ObjectHolder(GCItemNames.parachuteLightBlue)
    public static Item parachuteBlue;
    @ObjectHolder(GCItemNames.parachuteLime)
    public static Item parachuteLime;
    @ObjectHolder(GCItemNames.parachuteBrown)
    public static Item parachuteBrown;
    @ObjectHolder(GCItemNames.parachuteBlue)
    public static Item parachuteDarkBlue;
    @ObjectHolder(GCItemNames.parachuteGray)
    public static Item parachuteDarkGray;
    @ObjectHolder(GCItemNames.parachuteGreen)
    public static Item parachuteDarkGreen;
    @ObjectHolder(GCItemNames.parachuteLightGray)
    public static Item parachuteGray;
    @ObjectHolder(GCItemNames.parachuteMagenta)
    public static Item parachuteMagenta;
    @ObjectHolder(GCItemNames.parachuteOrange)
    public static Item parachuteOrange;
    @ObjectHolder(GCItemNames.parachutePink)
    public static Item parachutePink;
    @ObjectHolder(GCItemNames.parachutePurple)
    public static Item parachutePurple;
    @ObjectHolder(GCItemNames.parachuteRed)
    public static Item parachuteRed;
    @ObjectHolder(GCItemNames.parachuteTeal)
    public static Item parachuteTeal;
    @ObjectHolder(GCItemNames.parachuteYellow)
    public static Item parachuteYellow;
    @ObjectHolder(GCItemNames.rocketEngineT1)
    public static Item rocketEngineT1;
    @ObjectHolder(GCItemNames.rocketBoosterT1)
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
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(900)), GCItemNames.oxTankLight);
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(1800)), GCItemNames.oxTankMedium);
        register(r, new ItemOxygenTank(defaultBuilder().maxDamage(2700)), GCItemNames.oxTankHeavy);
        register(r, new ItemOxygenMask(defaultBuilder().maxStackSize(1)), GCItemNames.oxMask);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.rocketTierOne);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.rocketTierOneCargo1);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.rocketTierOneCargo2);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.rocketTierOneCargo3);
        register(r, new ItemTier1Rocket(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.rocketTierOneCreative);
        register(r, new ItemSensorGlasses(defaultBuilder()), GCItemNames.sensorGlasses);
        register(r, new ItemPickaxeGC(defaultBuilder()), GCItemNames.steelPickaxe);
        register(r, new ItemAxeGC(defaultBuilder()), GCItemNames.steelAxe);
        register(r, new ItemHoeGC(defaultBuilder()), GCItemNames.steelHoe);
        register(r, new ItemShovelGC(defaultBuilder()), GCItemNames.steelSpade);
        register(r, new ItemSwordGC(defaultBuilder()), GCItemNames.steelSword);
        register(r, new ArmorItemGC(EquipmentSlotType.HEAD, defaultBuilder()), GCItemNames.steelHelmet);
        register(r, new ArmorItemGC(EquipmentSlotType.CHEST, defaultBuilder()), GCItemNames.steelChestplate);
        register(r, new ArmorItemGC(EquipmentSlotType.LEGS, defaultBuilder()), GCItemNames.steelLeggings);
        register(r, new ArmorItemGC(EquipmentSlotType.FEET, defaultBuilder()), GCItemNames.steelBoots);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenVent);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenFan);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.oxygenConcentrator);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.heavyPlatingTier1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rocketEngineT1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rocketBoosterT1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.partFins);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.partNoseCone);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.sensorLens);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.buggy);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.buggyInventory1);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.buggyInventory2);
        register(r, new ItemBuggy(defaultBuilder().maxStackSize(1)), GCItemNames.buggyInventory3);
        register(r, new ItemFlag(defaultBuilder().maxDamage(0)), GCItemNames.flag);
        register(r, new ItemOxygenGear(defaultBuilder()), GCItemNames.oxygenGear);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canvas);
        register(r, new ItemOilCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.oilCanister);
        register(r, new ItemFuelCanister(defaultBuilder().maxDamage(ItemCanisterGeneric.EMPTY_CAPACITY)), GCItemNames.fuelCanister);
        register(r, new ItemCanisterOxygenInfinite(defaultBuilder()), GCItemNames.oxygenCanisterInfinite);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.flagPole);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.schematicBuggy);
        register(r, new ItemSchematic(defaultBuilder().maxDamage(0).maxStackSize(1)), GCItemNames.schematicRocketT2);
        register(r, new ItemKey(defaultBuilder()), GCItemNames.key);
//        register(r, new ItemFood(defaultBuilder()), ItemNames.foodItem);
        register(r, new ItemBattery(defaultBuilder().maxDamage(ItemElectricBase.DAMAGE_RANGE)), GCItemNames.battery);
        register(r, new ItemBatteryInfinite(defaultBuilder()), GCItemNames.infiniteBatery);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), GCItemNames.meteorChunk);
        register(r, new ItemMeteorChunk(defaultBuilder().maxStackSize(16)), GCItemNames.meteorChunkHot);
        register(r, new ItemUniversalWrench(defaultBuilder().maxDamage(256)), GCItemNames.wrench);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(1).saturation(0.1F).fastToEat().build())), GCItemNames.cheeseCurd);
//		GCItems.cheeseBlock = new ItemBlockCheese(GCBlocks.cheeseBlock, "cheeseBlock");
        register(r, new ItemBase(defaultBuilder()), GCItemNames.meteoricIronRaw);
        register(r, new ItemPreLaunchChecklist(defaultBuilder()), GCItemNames.prelaunchChecklist);
        register(r, new ItemDungeonFinder(defaultBuilder()), GCItemNames.dungeonFinder);
//        register(r, new ItemIC2Compat(defaultBuilder()), ItemNames.ic2compat); TODO
        register(r, new ItemEmergencyKit(defaultBuilder().maxDamage(0)), GCItemNames.emergencyKit);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.solarModule0);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.solarModule1);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.rawSilicon);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotCopper);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotAluminum);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedCopper);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedAluminum);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedSteel);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedBronze);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferSolar);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferBasic);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedWaferAdvanced);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.frequencyModule);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ambientThermalController);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialWheel);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialSeat);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.buggyMaterialStorage);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canisterTin);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.canisterCopper);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.3F).build())), GCItemNames.dehydratedApple);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), GCItemNames.dehydratedCarrot);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.3F).build())), GCItemNames.dehydratedMelon);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), GCItemNames.dehydratedPotato);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.1F).build())), GCItemNames.cheeseSlice);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.8F).build())), GCItemNames.burgerBun);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(2).saturation(0.3F).build())), GCItemNames.beefPattyRaw);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(4).saturation(0.6F).build())), GCItemNames.beefPattyCooked);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(14).saturation(1.0F).build())), GCItemNames.cheeseburger);
        register(r, new ItemBase(defaultBuilder().food((new Food.Builder()).hunger(8).saturation(0.6F).build())), GCItemNames.cannedBeef);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.ingotMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.compressedMeteoricIron);
        register(r, new ItemBase(defaultBuilder()), GCItemNames.lunarSapphire);
        Item.Properties parachuteProps = defaultBuilder().maxDamage(0).maxStackSize(1);
        register(r, new ItemParaChute(DyeColor.WHITE, parachuteProps), GCItemNames.parachuteWhite);
        register(r, new ItemParaChute(DyeColor.BLACK, parachuteProps), GCItemNames.parachuteBlack);
        register(r, new ItemParaChute(DyeColor.LIGHT_BLUE, parachuteProps), GCItemNames.parachuteLightBlue);
        register(r, new ItemParaChute(DyeColor.LIME, parachuteProps), GCItemNames.parachuteLime);
        register(r, new ItemParaChute(DyeColor.BROWN, parachuteProps), GCItemNames.parachuteBrown);
        register(r, new ItemParaChute(DyeColor.BLUE, parachuteProps), GCItemNames.parachuteBlue);
        register(r, new ItemParaChute(DyeColor.GRAY, parachuteProps), GCItemNames.parachuteGray);
        register(r, new ItemParaChute(DyeColor.GREEN, parachuteProps), GCItemNames.parachuteGreen);
        register(r, new ItemParaChute(DyeColor.LIGHT_GRAY, parachuteProps), GCItemNames.parachuteLightGray);
        register(r, new ItemParaChute(DyeColor.MAGENTA, parachuteProps), GCItemNames.parachuteMagenta);
        register(r, new ItemParaChute(DyeColor.ORANGE, parachuteProps), GCItemNames.parachuteOrange);
        register(r, new ItemParaChute(DyeColor.PINK, parachuteProps), GCItemNames.parachutePink);
        register(r, new ItemParaChute(DyeColor.PURPLE, parachuteProps), GCItemNames.parachutePurple);
        register(r, new ItemParaChute(DyeColor.RED, parachuteProps), GCItemNames.parachuteRed);
        register(r, new ItemParaChute(DyeColor.CYAN, parachuteProps), GCItemNames.parachuteTeal);
        register(r, new ItemParaChute(DyeColor.YELLOW, parachuteProps), GCItemNames.parachuteYellow);

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
