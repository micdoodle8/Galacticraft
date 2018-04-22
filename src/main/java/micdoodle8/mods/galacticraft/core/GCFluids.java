package micdoodle8.mods.galacticraft.core;

import buildcraft.api.fuels.BuildcraftFuelRegistry;
import buildcraft.api.mj.MjAPI;
import micdoodle8.mods.galacticraft.core.blocks.MaterialOleaginous;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.items.ItemBucketGC;
import micdoodle8.mods.galacticraft.core.items.ItemTier1Rocket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemTier2Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GCFluids
{
    public static Fluid fluidOil;
    public static Fluid fluidFuel;
    public static Fluid fluidOxygenGas;
    public static Fluid fluidHydrogenGas;
    public static Material materialOil = new MaterialOleaginous(MapColor.BROWN);

    public static void registerFluids()
    {
        fluidOxygenGas = registerFluid("oxygen", 1, 13, 295, true, "oxygen_gas");
        fluidHydrogenGas = registerFluid("hydrogen", 1, 1, 295, true, "hydrogen_gas");
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

        if (GCBlocks.crudeOil != null && !FluidRegistry.getBucketFluids().contains(fluidOil))
        {
        	FluidRegistry.addBucketForFluid(GCFluids.fluidOil);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull oil out of other mods tanks
            GCItems.bucketOil = new ItemBucketGC(GCBlocks.crudeOil, fluidOil);
            GCItems.bucketOil.setUnlocalizedName("bucket_oil");
            GCItems.registerItem(GCItems.bucketOil);
            EventHandlerGC.bucketList.put(GCBlocks.crudeOil, GCItems.bucketOil);
        }

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

        if (GCBlocks.fuel != null && !FluidRegistry.getBucketFluids().contains(fluidFuel))
        {
        	FluidRegistry.addBucketForFluid(GCFluids.fluidFuel);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fuel out of other mods tanks
            GCItems.bucketFuel = new ItemBucketGC(GCBlocks.fuel, fluidFuel);
            GCItems.bucketFuel.setUnlocalizedName("bucket_fuel");
            GCItems.registerItem(GCItems.bucketFuel);
            EventHandlerGC.bucketList.put(GCBlocks.fuel, GCItems.bucketFuel);
        }
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
        else
        {
            returnFluid.setGaseous(gaseous);
        }

        return returnFluid;
    }

    public static void registerLegacyFluids()
    {
        //If any other mod has registered "fuel" or "oil" and GC has not, then allow GC's appropriate canisters to be fillable with that one as well
//        if (ConfigManagerCore.useOldFuelFluidID && FluidRegistry.isFluidRegistered("fuel"))
//        {
//            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("fuel"), 1000), new ItemStack(GCItems.fuelCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
//        }
//        if (ConfigManagerCore.useOldOilFluidID && FluidRegistry.isFluidRegistered("oil"))
//        {
//            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("oil"), 1000), new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
            //And allow Buildcraft oil buckets to be filled with oilgc
//            if (CompatibilityManager.isBCraftEnergyLoaded())
//            {
                // TODO Fix BC Oil compat
//        		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, 1000), GameRegistry.findItemStack("buildcraftcore", "bucketOil", 1), new ItemStack(Items.bucket)));
//            }
//        }

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

    public static void registerDispenserBehaviours()
    {
        IBehaviorDispenseItem ibehaviordispenseitem = new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem dispenseBehavior = new BehaviorDefaultDispenseItem();
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                ItemBucketGC itembucket = (ItemBucketGC)stack.getItem();
                BlockPos blockpos = source.getBlockPos().offset((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING));
                if (itembucket.tryPlaceContainedLiquid((EntityPlayer)null, source.getWorld(), blockpos))
                {
                    return new ItemStack(Items.BUCKET);
                }
                else
                {
                    return this.dispenseBehavior.dispense(source, stack);
                }
            }
        };
        if (GCItems.bucketFuel != null)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.bucketFuel, ibehaviordispenseitem);
        }
        if (GCItems.bucketOil != null)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.bucketOil, ibehaviordispenseitem);
        }
        if (GalacticraftCore.isPlanetsLoaded)
        {
            if (MarsItems.bucketSludge != null)
            {
                BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MarsItems.bucketSludge, ibehaviordispenseitem);
            }
            if (VenusItems.bucketSulphuricAcid != null)
            {
                BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(VenusItems.bucketSulphuricAcid, ibehaviordispenseitem);
            }
        }

        // The following code is for other objects, not liquids, but it's convenient to keep it all together
        
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.meteorChunk, new BehaviorProjectileDispense()
        {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stack)
            {
                EntityMeteorChunk meteor = new EntityMeteorChunk(worldIn);
                meteor.setPosition(position.getX(), position.getY(), position.getZ());
                if (stack.getItemDamage() > 0)
                {
                    meteor.setFire(20);
                    meteor.isHot = true;
                }
                meteor.canBePickedUp = 1;
                return meteor;
            }
            @Override
            protected float getProjectileVelocity()
            {
                return 1.0F;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.rocketTier1, new BehaviorDefaultDispenseItem()
        {
            @Override
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                World world = source.getWorld();
                BlockPos pos = source.getBlockPos().offset((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING), 2);
                IBlockState iblockstate = world.getBlockState(pos);
                boolean rocketPlaced = false;
                if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
                {
                    float centerX = pos.getX() + 0.5F;
                    float centerY = pos.getY() + 0.4F;
                    float centerZ = pos.getZ() + 0.5F;
                    rocketPlaced = ItemTier1Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
                }

                if (rocketPlaced)
                {
                    stack.splitStack(1);
                }
                return stack;
            }
        });

        if (GalacticraftCore.isPlanetsLoaded)
        {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(MarsItems.rocketMars, new BehaviorDefaultDispenseItem()
            {
                @Override
                public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
                {
                    World world = source.getWorld();
                    BlockPos pos = source.getBlockPos().offset((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING), 2);
                    IBlockState iblockstate = world.getBlockState(pos);
                    boolean rocketPlaced = false;
                    if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
                    {
                        float centerX = pos.getX() + 0.5F;
                        float centerY = pos.getY() + 0.4F;
                        float centerZ = pos.getZ() + 0.5F;
                        rocketPlaced = ItemTier2Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
                    }

                    if (rocketPlaced)
                    {
                        stack.splitStack(1);
                    }
                    return stack;
                }
            });
            
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(AsteroidsItems.tier3Rocket, new BehaviorDefaultDispenseItem()
            {
                @Override
                public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
                {
                    World world = source.getWorld();
                    BlockPos pos = source.getBlockPos().offset((EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING), 2);
                    IBlockState iblockstate = world.getBlockState(pos);
                    boolean rocketPlaced = false;
                    if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
                    {
                        float centerX = pos.getX() + 0.5F;
                        float centerY = pos.getY() + 0.4F;
                        float centerZ = pos.getZ() + 0.5F;
                        rocketPlaced = ItemTier3Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
                    }

                    if (rocketPlaced)
                    {
                        stack.splitStack(1);
                    }
                    return stack;
                }
            });
        }
    }

    public static void registerBCFuel()
    {
        BuildcraftFuelRegistry.fuel.addFuel(GCFluids.fluidFuel, 5 * MjAPI.MJ, 9000);
    }
}
