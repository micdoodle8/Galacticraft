package micdoodle8.mods.galacticraft.api.item;

import java.lang.reflect.Field;
import java.util.HashMap;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.item.ItemStack;

public class GCItems
{
    // GALACTICRAFT CORE BLOCKS:
    // "rocketLaunchPad"
    // "buggyFuelingPad"
    // "oxygenDistributor"
    // "oxygenCompressor"
    // "oxygenCollector"
    // "refinery"
    // "fuelLoader"
    // "oxygenSealer"
    // "oxygenDetector"
    // "cargoLoader"
    // "cargoUnloader"
    // "oxygenPipe"
    // "nasaWorkbench"
    // "fallenMeteor"
    // "tinDecorationBlock1"
    // "tinDecorationBlock2"
    // "airLockFrame"
    // "sealableCopperWire"
    // "sealableOxygenPipe"
    // "sealableCopperCable"
    // "sealableGoldCable"
    // "sealableHighVoltageCable"
    // "sealableGlassFibreCable"
    // "sealableLowVoltageCable"
    // "sealableStonePipeItem"
    // "sealableCobblestonePipeItem"
    // "sealableStonePipeFluid"
    // "sealableCobblestonePipeFluid"
    // "sealableStonePipePower"
    // "sealableGoldPipePower"
    // "treasureChestTier1"
    // "parachest"
    // "solarPanelBasic"
    // "solarPanelAdvanced"
    // "copperWire"
    // "coalGenerator"
    // "batteryBox"
    // "electricFurnace"

    // GALACTICRAFT CORE ITEMS:
    // "oxygenTankLightFull"
    // "oxygenTankMediumFull"
    // "oxygenTankHeavyFull"
    // "oxygenTankLightEmpty"
    // "oxygenTankMediumEmpty"
    // "oxygenTankHeavyEmpty"
    // "oxygenMask"
    // "rocketTier1"
    // "rocketTier1_18cargo"
    // "rocketTier1_36cargo"
    // "rocketTier1_54cargo"
    // "rocketTier1_prefueled"
    // "heavyDutyPickaxe"
    // "heavyDutyShovel"
    // "heavyDutyAxe"
    // "heavyDutyHoe"
    // "heavyDutySword"
    // "heavyDutyHelmet"
    // "heavyDutyChestplate"
    // "heavyDutyLeggings"
    // "heavyDutyBoots"
    // "tinCanister"
    // "copperCanister"
    // "oxygenVent"
    // "oxygenFan"
    // "oxygenConcentrator"
    // "heavyPlatingTier1"
    // "rocketEngineTier1"
    // "rocketBoosterTier1"
    // "rocketFins"
    // "rocketNoseCone"
    // "sensorLens"
    // "moonBuggy"
    // "moonBuggy_18cargo"
    // "moonBuggy_36cargo"
    // "moonBuggy_54cargo"
    // "flagAmerican"
    // "flagBlack"
    // "flagLightBlue"
    // "flagLime"
    // "flagBrown"
    // "flagBlue"
    // "flagGray"
    // "flagGreen"
    // "flagLightGray"
    // "flagMagenta"
    // "flagOrange"
    // "flagPink"
    // "flagPurple"
    // "flagRed"
    // "flagCyan"
    // "flagYellow"
    // "flagWhite"
    // "oxygenGear"
    // "parachuteWhite"
    // "parachuteBlack"
    // "parachuteLightBlue"
    // "parachuteLime"
    // "parachuteBrown"
    // "parachuteBlue"
    // "parachuteGray"
    // "parachuteGreen"
    // "parachuteLightGray"
    // "parachutePink"
    // "parachuteOrange"
    // "parachutePink"
    // "parachutePurple"
    // "parachuteRed"
    // "parachuteCyan"
    // "parachuteYellow"
    // "canvas"
    // "fuelCanisterFull"
    // "oilCanisterFull"
    // "liquidCanisterEmpty"
    // "steelPole"
    // "oilExtractor"
    // "schematicMoonBuggy"
    // "schematicRocketTier2"
    // "tier1Key"
    // "buggyMaterialWheel"
    // "buggyMaterialSeat"
    // "buggyMaterialStorage"
    // "solarModuleSingle"
    // "solarModuleFull"
    // "batteryEmpty"
    // "batteryFull"
    // "infiniteBattery"
    // "rawSilicon"
    // "ingotCopper"
    // "ingotTin"
    // "ingotAluminum"
    // "ingotSteel"
    // "ingotBronze"
    // "compressedCopper"
    // "compressedTin"
    // "compressedAluminum"
    // "compressedSteel"
    // "compressedBronze"
    // "compressedIron"
    // "waferSolar"
    // "waferBasic"
    // "waferAdvanced"
    // "dehydratedApple"
    // "dehydratedCarrot"
    // "dehydratedMelon"
    // "dehydratedPotato"
    // "meteorThrowable"
    // "meteorThrowableHot"

    /**
     * Request an itemstack from Galacticraft.
     * 
     * Be sure to check if the itemstack is null, since items may be disabled,
     * planets addon might not be installed, etc.
     * 
     * Since the items are initialized and added to list in pre-init, you should
     * only be calling this method in init or later.
     * 
     * @param key
     *            Item identifier, see {@link GCItems} for item/block keys
     * @param amount
     *            Stack size
     * @return A stack of Galacticraft items with the passed key.
     */
    public static ItemStack requestItem(String key, int amount)
    {
        try
        {
            Class<?> clazz = Class.forName(GCItems.getItemListClass());
            Field f = clazz.getDeclaredField("itemList");
            Object o = f.get(null);

            if (o instanceof HashMap)
            {
                @SuppressWarnings("unchecked")
                HashMap<String, ItemStack> blockMap = (HashMap<String, ItemStack>) o;
                ItemStack stack = blockMap.get(key);
                return new ItemStack(stack.itemID, amount, stack.getItemDamage());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Request an itemstack from Galacticraft.
     * 
     * Be sure to check if the itemstack is null, since items may be disabled,
     * planets addon might not be installed, etc.
     * 
     * Since the items are initialized and added to list in pre-init, you should
     * only be calling this method in init or later.
     * 
     * @param key
     *            Item identifier, see {@link GCItems} for item/block keys
     * @param amount
     *            Stack size
     * @return A stack of Galacticraft items with the passed key.
     */
    public static ItemStack requestBlock(String key, int amount)
    {
        try
        {
            Class<?> clazz = Class.forName(GCItems.getItemListClass());
            Field f = clazz.getDeclaredField("blocksList");
            Object o = f.get(null);

            if (o instanceof HashMap)
            {
                @SuppressWarnings("unchecked")
                HashMap<String, ItemStack> blockMap = (HashMap<String, ItemStack>) o;
                ItemStack stack = blockMap.get(key);
                return new ItemStack(stack.itemID, amount, stack.getItemDamage());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    private static final String getItemListClass()
    {
        return "micdoodle8.mods.galacticraft.core.GalacticraftCore";
    }
}
