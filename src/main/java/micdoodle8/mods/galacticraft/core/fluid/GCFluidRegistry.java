package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Credit to pupnewfster from the Mekanism project
 */
public class GCFluidRegistry
{
    private final DeferredRegister<Fluid> fluidRegister;
    private final DeferredRegister<Block> blockRegister;
    private final DeferredRegister<Item> itemRegister;

    public GCFluidRegistry() {
        blockRegister = new DeferredRegister<>(ForgeRegistries.BLOCKS, Constants.MOD_ID_CORE);
        fluidRegister = new DeferredRegister<>(ForgeRegistries.FLUIDS, Constants.MOD_ID_CORE);
        itemRegister = new DeferredRegister<>(ForgeRegistries.ITEMS, Constants.MOD_ID_CORE);
    }

    public FluidRegistrationEntry<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, FlowingFluidBlock, BucketItem> register(String name, FluidAttributes.Builder builder) {
        String flowingName = "flowing_" + name;
        String bucketName = name + "_bucket";
        //Create the registry object with dummy entries that we can use as part of the supplier but that works as use in suppliers
        FluidRegistrationEntry<ForgeFlowingFluid.Source, ForgeFlowingFluid.Flowing, FlowingFluidBlock, BucketItem> fluidRegistryObject = new FluidRegistrationEntry<>(name);
        //Pass in suppliers that are wrapped instead of direct references to the registry objects, so that when we update the registry object to
        // point to a new object it gets updated properly.
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(fluidRegistryObject::getStillFluid,
                fluidRegistryObject::getFlowingFluid, builder).bucket(fluidRegistryObject::getBucket).block(fluidRegistryObject::getBlock);
        //Update the references to objects that are retrieved from the deferred registers
        fluidRegistryObject.updateStill(fluidRegister.register(name, () -> new ForgeFlowingFluid.Source(properties)));
        fluidRegistryObject.updateFlowing(fluidRegister.register(flowingName, () -> new ForgeFlowingFluid.Flowing(properties)));
        fluidRegistryObject.updateBucket(itemRegister.register(bucketName, () -> new BucketItem(fluidRegistryObject::getStillFluid,
                new Item.Properties().maxStackSize(1).containerItem(Items.BUCKET))));
        //Note: The block properties used here is a copy of the ones for water
        fluidRegistryObject.updateBlock(blockRegister.register(name, () -> new FlowingFluidBlock(fluidRegistryObject::getStillFluid,
                Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())));
        return fluidRegistryObject;
    }

    public void register(IEventBus bus) {
        blockRegister.register(bus);
        fluidRegister.register(bus);
        itemRegister.register(bus);
    }

//    public static Fluid fluidOil;
//    public static Fluid fluidFuel;
//    public static Fluid fluidOxygenGas;
//    public static Fluid fluidHydrogenGas;
//    public static Material materialOil = new MaterialOleaginous(MapColor.BROWN);
//
//    public static void registerFluids()
//    {
//        fluidOxygenGas = registerFluid("oxygen", 1, 13, 295, true, "oxygen_gas");
//        fluidHydrogenGas = registerFluid("hydrogen", 1, 1, 295, true, "hydrogen_gas");
//    }
//
//    public static void registerOilandFuel()
//    {
//        //NOTE: the way this operates will depend on the order in which different mods initialize (normally alphabetical order)
//        //Galacticraft can handle things OK if another mod registers oil or fuel first.  The other mod may not be so happy if GC registers oil or fuel first.
//
//        String oilID = ConfigManagerCore.useOldOilFluidID ? "oilgc" : "oil";
//        String fuelID = ConfigManagerCore.useOldFuelFluidID ? "fuelgc" : "fuel";
//
//        // Oil:
//        if (!FluidRegistry.isFluidRegistered(oilID))
//        {
//            ResourceLocation flowingOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_flow");
//            ResourceLocation stillOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_still");
//            Fluid gcFluidOil = new Fluid(oilID, stillOil, flowingOil).setDensity(800).setViscosity(1500);
//            FluidRegistry.registerFluid(gcFluidOil);
//        }
//        else
//        {
//            GCLog.info("Galacticraft oil is not default, issues may occur.");
//        }
//
//        fluidOil = FluidRegistry.getFluid(oilID);
//
//        if (fluidOil.getBlock() == null)
//        {
//            GCBlocks.registerOil();
//            fluidOil.setBlock(GCBlocks.crudeOil);
//        }
//        else
//        {
//            GCBlocks.crudeOil = fluidOil.getBlock();
//        }
//
//        if (GCBlocks.crudeOil != null && !FluidRegistry.getBucketFluids().contains(fluidOil))
//        {
//        	FluidRegistry.addBucketForFluid(GCFluids.fluidOil);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull oil out of other mods tanks
//            GCItems.bucketOil = new ItemBucketGC(GCBlocks.crudeOil, fluidOil);
//            GCItems.bucketOil.setUnlocalizedName("bucket_oil");
//            GCItems.registerItem(GCItems.bucketOil);
//            EventHandlerGC.bucketList.put(GCBlocks.crudeOil, GCItems.bucketOil);
//        }
//
//        // Fuel:
//        if (!FluidRegistry.isFluidRegistered(fuelID))
//        {
//            ResourceLocation flowingFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_flow");
//            ResourceLocation stillFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_still");
//            Fluid gcFluidFuel = new Fluid(fuelID, stillFuel, flowingFuel).setDensity(400).setViscosity(900);
//            FluidRegistry.registerFluid(gcFluidFuel);
//        }
//        else
//        {
//            GCLog.info("Galacticraft fuel is not default, issues may occur.");
//        }
//
//        fluidFuel = FluidRegistry.getFluid(fuelID);
//
//        if (fluidFuel.getBlock() == null)
//        {
//            GCBlocks.registerFuel();
//            GCFluids.fluidFuel.setBlock(GCBlocks.fuel);
//        }
//        else
//        {
//            GCBlocks.fuel = fluidFuel.getBlock();
//        }
//
//        if (GCBlocks.fuel != null && !FluidRegistry.getBucketFluids().contains(fluidFuel))
//        {
//        	FluidRegistry.addBucketForFluid(GCFluids.fluidFuel);  //Create a Universal Bucket AS WELL AS our type, this is needed to pull fuel out of other mods tanks
//            GCItems.bucketFuel = new ItemBucketGC(GCBlocks.fuel, fluidFuel);
//            GCItems.bucketFuel.setUnlocalizedName("bucket_fuel");
//            GCItems.registerItem(GCItems.bucketFuel);
//            EventHandlerGC.bucketList.put(GCBlocks.fuel, GCItems.bucketFuel);
//        }
//    }
//
//    private static Fluid registerFluid(String fluidName, int density, int viscosity, int temperature, boolean gaseous, String fluidTexture)
//    {
//        Fluid returnFluid = FluidRegistry.getFluid(fluidName);
//
//        if (returnFluid == null)
//        {
//            ResourceLocation texture = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/" + fluidTexture);
//            FluidRegistry.registerFluid(new Fluid(fluidName, texture, texture).setDensity(density).setViscosity(viscosity).setTemperature(temperature).setGaseous(gaseous));
//            returnFluid = FluidRegistry.getFluid(fluidName);
//        }
//        else
//        {
//            returnFluid.setGaseous(gaseous);
//        }
//
//        return returnFluid;
//    }
//
//    public static void registerLegacyFluids()
//    {
//        //If any other mod has registered "fuel" or "oil" and GC has not, then allow GC's appropriate canisters to be fillable with that one as well
////        if (ConfigManagerCore.useOldFuelFluidID && FluidRegistry.isFluidRegistered("fuel"))
////        {
////            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("fuel"), 1000), new ItemStack(GCItems.fuelCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
////        }
////        if (ConfigManagerCore.useOldOilFluidID && FluidRegistry.isFluidRegistered("oil"))
////        {
////            FluidContainerRegistry.registerFluidContainer(new FluidContainerRegistry.FluidContainerData(new FluidStack(FluidRegistry.getFluid("oil"), 1000), new ItemStack(GCItems.oilCanister, 1, 1), new ItemStack(GCItems.oilCanister, 1, ItemCanisterGeneric.EMPTY)));
//            //And allow Buildcraft oil buckets to be filled with oilgc
////            if (CompatibilityManager.isBCraftEnergyLoaded())
////            {
//                // TODO Fix BC Oil compat
////        		FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(GalacticraftCore.fluidOil, 1000), GameRegistry.findItemStack("buildcraftcore", "bucketOil", 1), new ItemStack(Items.bucket)));
////            }
////        }
//
//        //Register now any unregistered "oil", "fuel", "oilgc" and "fuelgc" fluids
//        //This is for legacy compatibility with any 'in the world' tanks and items filled in different GC versions or with different GC config
//        //In those cases, FluidUtil methods (and TileEntityRefinery) will attempt to fresh containers/tanks with the current fuel or oil type
//        ResourceLocation flowingOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_flow");
//        ResourceLocation flowingFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_flow");
//        ResourceLocation stillOil = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/oil_still");
//        ResourceLocation stillFuel = new ResourceLocation(Constants.TEXTURE_PREFIX + "blocks/fluids/fuel_still");
//        if (!FluidRegistry.isFluidRegistered("oil"))
//        {
//            FluidRegistry.registerFluid(new Fluid("oil", stillOil, flowingOil).setDensity(800).setViscosity(1500));
//        }
//        if (!FluidRegistry.isFluidRegistered("oilgc"))
//        {
//            FluidRegistry.registerFluid(new Fluid("oilgc", stillOil, flowingOil).setDensity(800).setViscosity(1500));
//        }
//        if (!FluidRegistry.isFluidRegistered("fuel"))
//        {
//            FluidRegistry.registerFluid(new Fluid("fuel", stillFuel, flowingFuel).setDensity(400).setViscosity(900));
//        }
//        if (!FluidRegistry.isFluidRegistered("fuelgc"))
//        {
//            FluidRegistry.registerFluid(new Fluid("fuelgc", stillFuel, flowingFuel).setDensity(400).setViscosity(900));
//        }
//    }
//
//    public static void registerDispenserBehaviours()
//    {
//        IDispenseItemBehavior ibehaviordispenseitem = new DefaultDispenseItemBehavior()
//        {
//            private final DefaultDispenseItemBehavior dispenseBehavior = new DefaultDispenseItemBehavior();
//            @Override
//            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
//            {
//                ItemBucketGC itembucket = (ItemBucketGC)stack.getItem();
//                BlockPos blockpos = source.getBlockPos().offset((Direction)source.getBlockState().getValue(DispenserBlock.FACING));
//                if (itembucket.tryPlaceContainedLiquid((PlayerEntity)null, source.getWorld(), blockpos))
//                {
//                    return new ItemStack(Items.BUCKET);
//                }
//                else
//                {
//                    return this.dispenseBehavior.dispense(source, stack);
//                }
//            }
//        };
//        if (GCItems.bucketFuel != null)
//        {
//            DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.bucketFuel, ibehaviordispenseitem);
//        }
//        if (GCItems.bucketOil != null)
//        {
//            DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.bucketOil, ibehaviordispenseitem);
//        }
//        if (GalacticraftCore.isPlanetsLoaded)
//        {
//            if (MarsItems.bucketSludge != null)
//            {
//                DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(MarsItems.bucketSludge, ibehaviordispenseitem);
//            }
//            if (VenusItems.bucketSulphuricAcid != null)
//            {
//                DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(VenusItems.bucketSulphuricAcid, ibehaviordispenseitem);
//            }
//        }
//
//        // The following code is for other objects, not liquids, but it's convenient to keep it all together
//
//        DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.meteorChunk, new ProjectileDispenseBehavior()
//        {
//            @Override
//            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stack)
//            {
//                EntityMeteorChunk meteor = new EntityMeteorChunk(worldIn);
//                meteor.setPosition(position.getX(), position.getY(), position.getZ());
//                if (stack.getItemDamage() > 0)
//                {
//                    meteor.setFire(20);
//                    meteor.isHot = true;
//                }
//                meteor.canBePickedUp = 1;
//                return meteor;
//            }
//            @Override
//            protected float getProjectileVelocity()
//            {
//                return 1.0F;
//            }
//        });
//
//        DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(GCItems.rocketTier1, new DefaultDispenseItemBehavior()
//        {
//            @Override
//            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
//            {
//                World world = source.getWorld();
//                BlockPos pos = source.getBlockPos().offset((Direction)source.getBlockState().getValue(DispenserBlock.FACING), 2);
//                BlockState iblockstate = world.getBlockState(pos);
//                boolean rocketPlaced = false;
//                if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
//                {
//                    float centerX = pos.getX() + 0.5F;
//                    float centerY = pos.getY() + 0.4F;
//                    float centerZ = pos.getZ() + 0.5F;
//                    rocketPlaced = ItemTier1Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
//                }
//
//                if (rocketPlaced)
//                {
//                    stack.splitStack(1);
//                }
//                return stack;
//            }
//        });
//
//        if (GalacticraftCore.isPlanetsLoaded)
//        {
//            DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(MarsItems.rocketMars, new DefaultDispenseItemBehavior()
//            {
//                @Override
//                public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
//                {
//                    World world = source.getWorld();
//                    BlockPos pos = source.getBlockPos().offset((Direction)source.getBlockState().getValue(DispenserBlock.FACING), 2);
//                    BlockState iblockstate = world.getBlockState(pos);
//                    boolean rocketPlaced = false;
//                    if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
//                    {
//                        float centerX = pos.getX() + 0.5F;
//                        float centerY = pos.getY() + 0.4F;
//                        float centerZ = pos.getZ() + 0.5F;
//                        rocketPlaced = ItemTier2Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
//                    }
//
//                    if (rocketPlaced)
//                    {
//                        stack.splitStack(1);
//                    }
//                    return stack;
//                }
//            });
//
//            DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(AsteroidsItems.tier3Rocket, new DefaultDispenseItemBehavior()
//            {
//                @Override
//                public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
//                {
//                    World world = source.getWorld();
//                    BlockPos pos = source.getBlockPos().offset((Direction)source.getBlockState().getValue(DispenserBlock.FACING), 2);
//                    BlockState iblockstate = world.getBlockState(pos);
//                    boolean rocketPlaced = false;
//                    if (iblockstate.getBlock() == GCBlocks.landingPadFull && GCBlocks.landingPadFull.getMetaFromState(iblockstate) == 0)
//                    {
//                        float centerX = pos.getX() + 0.5F;
//                        float centerY = pos.getY() + 0.4F;
//                        float centerZ = pos.getZ() + 0.5F;
//                        rocketPlaced = ItemTier3Rocket.placeRocketOnPad(stack, world, world.getTileEntity(pos), centerX, centerY, centerZ);
//                    }
//
//                    if (rocketPlaced)
//                    {
//                        stack.splitStack(1);
//                    }
//                    return stack;
//                }
//            });
//        }
//    }
//
//    public static void registerBCFuel()
//    {
//        BuildcraftFuelRegistry.fuel.addFuel(GCFluids.fluidFuel, 5 * MjAPI.MJ, 9000);
//    }
}
