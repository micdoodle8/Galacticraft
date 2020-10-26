//package micdoodle8.mods.galacticraft.core.items;
//
//import java.util.HashMap;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
//import micdoodle8.mods.galacticraft.core.util.FluidUtil;
//import net.minecraft.block.Block;
//import net.minecraft.fluid.Fluid;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.Items;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.BucketItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.util.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import net.minecraftforge.fluids.capability.IFluidHandlerItem;
//
//public class ItemBucketGC extends BucketItem implements ISortableItem, ICapabilityProvider
//{
////	private String texture_prefix;
//	public Fluid accepts;
//	private static HashMap<String, ItemBucketGC> allFluids = new HashMap();
//
//    public ItemBucketGC(Block block, Fluid fluid)
//    {
//        super(block);
//        this.setContainerItem(Items.BUCKET);
//        this.accepts = fluid;
//        //TODO - could be getNameFuzzy
//        allFluids.put(fluid.getName(), this);
//    }
//
//    public static ItemBucketGC getBucketForFluid(Fluid fluid)
//    {
//        //TODO - could be getNameFuzzy
//    	return allFluids.get(fluid.getName());
//    }
//
//    public static ItemStack fillBucketFrom(IFluidHandler fluidHandler)
//    {
//        FluidStack liquid = fluidHandler.drain(1000, false);
//        if (liquid != null && liquid.amount == 1000)
//        {
//        	ItemBucketGC test = ItemBucketGC.getBucketForFluid(liquid.getFluid());
//        	if (test != null)
//        	{
//        		fluidHandler.drain(1000, true);
//        		return new ItemStack(test);
//        	}
//        }
//        return null;
//    }
//
//    public ItemStack drainBucketTo(IFluidHandler fluidHandler)
//    {
//    	FluidStack fs = new FluidStack(this.accepts, 1000);
//        int transferred = fluidHandler.fill(fs, false);
//        if (transferred == 1000)
//        {
//        	fluidHandler.fill(fs, true);
//        	return new ItemStack(Items.BUCKET);
//        }
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }
//
//    @Override
//    public EnumSortCategoryItem getCategory()
//    {
//        return EnumSortCategoryItem.BUCKET;
//    }
//
//    @Override
//    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt)
//    {
//        return new FluidHandlerBucketGC(stack);
//    }
//
//    @Override
//    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    @Nullable
//    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
//    {
//        return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
//    }
//
//
//    public static class FluidHandlerBucketGC implements IFluidHandlerItem, ICapabilityProvider
//    {
//        public static final String FLUID_NBT_KEY = "Fluid";
//
//        @Nonnull
//        protected ItemStack container;
//        protected int capacity;
//        private ItemBucketGC item;
//
//        public FluidHandlerBucketGC(@Nonnull ItemStack container)
//        {
//        	container.setCount(1);
//            this.container = container;
//            this.item = (ItemBucketGC) container.getItem();
//            this.capacity = 1000;
//        }
//
//        @Nonnull
//        @Override
//        public ItemStack getContainer()
//        {
//            return container;
//        }
//
//        @Nullable
//        public FluidStack getFluid()
//        {
//            return new FluidStack(item.accepts, this.capacity);
//        }
//
//        protected void setFluid(FluidStack fluid)
//        {
//        }
//
//        @Override
//        public IFluidTankProperties[] getTankProperties()
//        {
//            return new IFluidTankProperties[] { new FluidTankProperties(getFluid(), capacity) };
//        }
//
//        @Override
//        public int fill(FluidStack resource, IFluidHandler.FluidAction action)
//        {
//        	//This is a full bucket already, can't be filled
//            return 0;
//        }
//
//        @Override
//        public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action)
//        {
//            if (resource == null || !this.canDrainFluidType(resource))
//            {
//                return null;
//            }
//            return drain(resource.amount, doDrain);
//        }
//
//        @Override
//        public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action)
//        {
//            if (maxDrain < this.capacity)
//            {
//                return null;
//            }
//            if (doDrain)
//            {
//                container = new ItemStack(Items.BUCKET);
//            }
//            return this.getFluid();
//        }
//
//        public boolean canFillFluidType(FluidStack fluid)
//        {
//        	return false;
//        }
//
//        public boolean canDrainFluidType(FluidStack fluid)
//        {
//            return FluidUtil.fluidsSame(item.accepts, fluid.getFluid(), true);
//        }
//
//        protected void setContainerToEmpty()
//        {
//            container = new ItemStack(Items.BUCKET);
//        }
//
//        @Override
//        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing)
//        {
//            return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        @Nullable
//        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
//        {
//            return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
//        }
//    }
//}
