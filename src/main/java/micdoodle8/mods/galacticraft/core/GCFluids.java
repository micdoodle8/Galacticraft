package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.blocks.MaterialOleaginous;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GCFluids
{
    public static Fluid fluidOil;
    public static Fluid fluidFuel;
    public static Fluid fluidOxygenGas;
    public static Fluid fluidHydrogenGas;
    public static Material materialOil = new MaterialOleaginous(MapColor.brownColor);

    public static void registerFluids()
    {
        fluidOxygenGas = registerFluid("oxygen", 1, 13, 295, true, "oxygen_gas");
        fluidHydrogenGas = registerFluid("hydrogen", 1, 1, 295, true, "hydrogen_gas");

        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(fluidFuel, 1000), new ItemStack(GCItems.fuelCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
        FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(fluidOil, 1000), new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
    }

    public static void registerOilandFuel()
    {
        //NOTE: the way this operates will depend on the order in which different mods initialize (normally alphabetical order)
        //Galacticraft can handle things OK if another mod registers oil or fuel first.  The other mod may not be so happy if GC registers oil or fuel first.

        String oilID = ConfigManagerCore.useOldOilFluidID ? "oilgc" : "oil";
        String fuelID = ConfigManagerCore.useOldFuelFluidID ? "fuelgc" : "fuel";

        // Oil:
        if (!FluidRegistry.isFluidRegistered(oilID))
        {
            ResourceLocation flowingOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_flow");
            ResourceLocation stillOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_still");
            Fluid gcFluidOil = new Fluid(oilID, stillOil, flowingOil).setDensity(800).setViscosity(1500);
            FluidRegistry.registerFluid(gcFluidOil);
        }
        else
        {
            GCLog.info("Galacticraft oil is not default, issues may occur.");
        }

        fluidOil = FluidRegistry.getFluid(oilID);

        if (fluidOil.getBlock() == null)
        {
            GCBlocks.registerOil();
            fluidOil.setBlock(GCBlocks.crudeOil);
        }
        else
        {
            GCBlocks.crudeOil = fluidOil.getBlock();
        }

        if (GCBlocks.crudeOil != null && Item.itemRegistry.getObject(new ResourceLocation("buildcraftenergy:items/bucketOil")) == null)
        {
            GCItems.bucketOil = new ItemBucketGC(GCBlocks.crudeOil);
            GCItems.bucketOil.setUnlocalizedName("bucket_oil");
            GCItems.registerItem(GCItems.bucketOil);
            FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(oilID, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(GCItems.bucketOil), new ItemStack(Items.bucket));
        }

        EventHandlerGC.bucketList.put(GCBlocks.crudeOil, GCItems.bucketOil);

        // Fuel:
        if (!FluidRegistry.isFluidRegistered(fuelID))
        {
            ResourceLocation flowingFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_flow");
            ResourceLocation stillFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_still");
            Fluid gcFluidFuel = new Fluid(fuelID, stillFuel, flowingFuel).setDensity(400).setViscosity(900);
            FluidRegistry.registerFluid(gcFluidFuel);
        }
        else
        {
            GCLog.info("Galacticraft fuel is not default, issues may occur.");
        }

        fluidFuel = FluidRegistry.getFluid(fuelID);

        if (fluidFuel.getBlock() == null)
        {
            GCBlocks.registerFuel();
            GCFluids.fluidFuel.setBlock(GCBlocks.fuel);
        }
        else
        {
            GCBlocks.fuel = fluidFuel.getBlock();
        }

        if (GCBlocks.fuel != null && Item.itemRegistry.getObject(new ResourceLocation("buildcraftenergy:items/bucketFuel")) == null)
        {
            GCItems.bucketFuel = new ItemBucketGC(GCBlocks.fuel);
            GCItems.bucketFuel.setUnlocalizedName("bucket_fuel");
            GCItems.registerItem(GCItems.bucketFuel);
            FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(fuelID, FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(GCItems.bucketFuel), new ItemStack(Items.bucket));
        }

        EventHandlerGC.bucketList.put(GCBlocks.fuel, GCItems.bucketFuel);
    }

    private static Fluid registerFluid(String fluidName, int density, int viscosity, int temperature, boolean gaseous, String fluidTexture)
    {
        Fluid returnFluid = FluidRegistry.getFluid(fluidName);

        if (returnFluid == null)
        {
            ResourceLocation texture = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/" + fluidTexture);
            FluidRegistry.registerFluid(new Fluid(fluidName, texture, texture).setDensity(density).setViscosity(viscosity).setTemperature(temperature).setGaseous(gaseous));
            returnFluid = FluidRegistry.getFluid(fluidName);
        }

        return returnFluid;
    }

    public static void registerLegacyFluids()
    {
        //If any other mod has registered "fuel" or "oil" and GC has not, then allow GC's appropriate canisters to be fillable with that one as well
        if (ConfigManagerCore.useOldFuelFluidID && FluidRegistry.isFluidRegistered("fuel"))
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("fuel"), 1000), new ItemStack(GCItems.fuelCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
        }
        if (ConfigManagerCore.useOldOilFluidID && FluidRegistry.isFluidRegistered("oil"))
        {
            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("oil"), 1000), new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
            //And allow Buildcraft oil buckets to be filled with oilgc
            if (CompatibilityManager.isBCraftEnergyLoaded())
            {
                // TODO Fix BC Oil compat
//        		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, 1000), GameRegistry.findItemStack("BuildCraft|Core", "bucketOil", 1), new ItemStack(Items.bucket)));
            }
        }

        //Register now any unregistered "oil", "fuel", "oilgc" and "fuelgc" fluids
        //This is for legacy compatibility with any 'in the world' tanks and items filled in different GC versions or with different GC config
        //In those cases, FluidUtil methods (and TileEntityRefinery) will attempt to fresh containers/tanks with the current fuel or oil type
        ResourceLocation flowingOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_flow");
        ResourceLocation flowingFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_flow");
        ResourceLocation stillOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_still");
        ResourceLocation stillFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_still");
        if (!FluidRegistry.isFluidRegistered("oil"))
        {
            FluidRegistry.registerFluid(new Fluid("oil", stillOil, flowingOil).setDensity(800).setViscosity(1500));
        }
        if (!FluidRegistry.isFluidRegistered("oilgc"))
        {
            FluidRegistry.registerFluid(new Fluid("oilgc", stillOil, flowingOil).setDensity(800).setViscosity(1500));
        }
        if (!FluidRegistry.isFluidRegistered("fuel"))
        {
            FluidRegistry.registerFluid(new Fluid("fuel", stillFuel, flowingFuel).setDensity(400).setViscosity(900));
        }
        if (!FluidRegistry.isFluidRegistered("fuelgc"))
        {
            FluidRegistry.registerFluid(new Fluid("fuelgc", stillFuel, flowingFuel).setDensity(400).setViscosity(900));
        }
    }
}
