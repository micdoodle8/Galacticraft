package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Credit to pupnewfster from the Mekanism project
 */
@ParametersAreNonnullByDefault
public class FluidRegistrationEntry<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends FlowingFluidBlock, BUCKET extends Item>
{
    private RegistryObject<STILL> stillRO;
    private RegistryObject<FLOWING> flowingRO;
    private RegistryObject<BLOCK> blockRO;
    private RegistryObject<BUCKET> bucketRO;

    public FluidRegistrationEntry(String name)
    {
        this.stillRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name), ForgeRegistries.FLUIDS);
        this.flowingRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, "flowing_" + name), ForgeRegistries.FLUIDS);
        this.blockRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name), ForgeRegistries.BLOCKS);
        this.bucketRO = RegistryObject.of(new ResourceLocation(Constants.MOD_ID_CORE, name + "_bucket"), ForgeRegistries.ITEMS);
    }

    public FlowingFluid getStillFluid() //FLUIDS AREN'T REGISTERED YET. This is placeholder code to prevent crashes
    {
        return Fluids.WATER/*stillRO.get()*/; //todo
    }

    public FlowingFluid getFlowingFluid()
    {
        return Fluids.FLOWING_WATER/*flowingRO.get()*/; //todo
    }

    public FlowingFluidBlock getBlock()
    {
        return (FlowingFluidBlock) Blocks.WATER/*blockRO.get()*/; //todo
    }

    public Item getBucket()
    {
        return Items.WATER_BUCKET/*bucketRO.get()*/; //todo
    }

    //Make sure these update methods are package local as only the FluidDeferredRegister should be messing with them
    void updateStill(RegistryObject<STILL> stillRO)
    {
        this.stillRO = Objects.requireNonNull(stillRO);
    }

    void updateFlowing(RegistryObject<FLOWING> flowingRO)
    {
        this.flowingRO = Objects.requireNonNull(flowingRO);
    }

    void updateBlock(RegistryObject<BLOCK> blockRO)
    {
        this.blockRO = Objects.requireNonNull(blockRO);
    }

    void updateBucket(RegistryObject<BUCKET> bucketRO)
    {
        this.bucketRO = Objects.requireNonNull(bucketRO);
    }

    @Nonnull
    public FlowingFluid getFluid() //TODO
    {
        //Default our fluid to being the still variant
        return getStillFluid();
    }
}