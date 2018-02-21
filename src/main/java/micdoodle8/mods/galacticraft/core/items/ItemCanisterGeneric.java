package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.JavaUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

//In Minecraft 1.10, dual implementation of both old and new ItemFluidContainer system :)
public abstract class ItemCanisterGeneric extends ItemFluidContainer implements IFluidContainerItem
{
    private String allowedFluid = null;
    public final static int EMPTY = Fluid.BUCKET_VOLUME + 1;
    private static boolean isTELoaded = CompatibilityManager.isTELoaded();

    public ItemCanisterGeneric(String assetName)
    {
        super(Fluid.BUCKET_VOLUME);
        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(assetName);
        this.setContainerItem(GCItems.oilCanister);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean isItemTool(ItemStack stack)
    {
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new ItemCanisterGenericHandler(stack);
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return ItemCanisterGeneric.EMPTY - 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        //Workaround for strange behaviour in TE Transposer
        if (isTELoaded)
        {
            if (JavaUtil.instance.isCalledBy("thermalexpansion.block.machine.TileTransposer"))
            {
                return null;
            }
        }

        return new ItemStack(this.getContainerItem(), 1, ItemCanisterGeneric.EMPTY);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (ItemCanisterGeneric.EMPTY == par1ItemStack.getItemDamage())
        {
            if (par1ItemStack.getItem() != GCItems.oilCanister)
            {
                this.replaceEmptyCanisterItem(par1ItemStack, GCItems.oilCanister);
            }
            par1ItemStack.setTagCompound(null);
        }
        else if (par1ItemStack.getItemDamage() <= 0)
        {
            par1ItemStack.setItemDamage(1);
        }
    }

    public void setAllowedFluid(String name)
    {
        this.allowedFluid = name;
    }

    public String getAllowedFluid()
    {
        return this.allowedFluid;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (resource == null || resource.getFluid() == null || resource.amount == 0 || container == null || container.getItemDamage() <= 1 || !(container.getItem() instanceof ItemCanisterGeneric))
        {
            return 0;
        }

        String fluidName = resource.getFluid().getName();
        if (container.getItemDamage() >= ItemCanisterGeneric.EMPTY)
        {
            //Empty canister - find a new canister to match the fluid
            for (ItemCanisterGeneric i : GCItems.canisterTypes)
            {
                if (fluidName.equalsIgnoreCase(i.allowedFluid))
                {
                    if (!doFill)
                    {
                        return Math.min(resource.amount, this.capacity);
                    }

                    this.replaceEmptyCanisterItem(container, i);
                    break;
                }
            }
            //Delete any Forge fluid contents and set this to a clean empty item
            container.setTagCompound(null);
            container.setItemDamage(ItemCanisterGeneric.EMPTY);
        }
        else
        {
            //Refresh the Forge fluid contents
            container.setTagCompound(null);
            super_fill(container, this.getFluid(container), true);
        }

        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric) container.getItem()).allowedFluid))
        {
            int added = super_fill(container, resource, doFill);
            if (doFill && added > 0)
            {
                container.setItemDamage(Math.max(1, container.getItemDamage() - added));
            }
            return added;
        }

        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (this.allowedFluid == null || container.getItemDamage() >= ItemCanisterGeneric.EMPTY)
        {
            return null;
        }

        //Refresh the Forge fluid contents
        container.setTagCompound(null);
        super_fill(container, this.getFluid(container), true);

        FluidStack used = super_drain(container, maxDrain, doDrain);
        if (doDrain && used != null && used.amount > 0)
        {
            this.setNewDamage(container, container.getItemDamage() + used.amount);
        }
        return used;
    }
    
    private int super_fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        if (!doFill)
        {
            if (!container.hasTagCompound() || !container.getTagCompound().hasKey("Fluid"))
            {
                return Math.min(capacity, resource.amount);
            }

            FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));

            if (stack == null)
            {
                return Math.min(capacity, resource.amount);
            }

            if (!stack.isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (!container.hasTagCompound())
        {
            container.setTagCompound(new NBTTagCompound());
        }

        if (!container.getTagCompound().hasKey("Fluid"))
        {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount)
            {
                fluidTag.setInteger("Amount", capacity);
                container.getTagCompound().setTag("Fluid", fluidTag);
                return capacity;
            }

            container.getTagCompound().setTag("Fluid", fluidTag);
            return resource.amount;
        }

        NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
        FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!stack.isFluidEqual(resource))
        {
            return 0;
        }

        int filled = capacity - stack.amount;
        if (resource.amount < filled)
        {
            stack.amount += resource.amount;
            filled = resource.amount;
        }
        else
        {
            stack.amount = capacity;
        }

        container.getTagCompound().setTag("Fluid", stack.writeToNBT(fluidTag));
        return filled;
    }

    private FluidStack super_drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (!container.hasTagCompound() || !container.getTagCompound().hasKey("Fluid"))
        {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(container.getTagCompound().getCompoundTag("Fluid"));
        if (stack == null)
        {
            return null;
        }

        int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);
        if (doDrain)
        {
            if (currentAmount == stack.amount)
            {
                container.getTagCompound().removeTag("Fluid");

                if (container.getTagCompound().hasNoTags())
                {
                    container.setTagCompound(null);
                }
                return stack;
            }

            NBTTagCompound fluidTag = container.getTagCompound().getCompoundTag("Fluid");
            fluidTag.setInteger("Amount", currentAmount - stack.amount);
            container.getTagCompound().setTag("Fluid", fluidTag);
        }
        return stack;
    }

    protected void setNewDamage(ItemStack container, int newDamage)
    {
        newDamage = Math.min(newDamage, ItemCanisterGeneric.EMPTY);
        if (newDamage == ItemCanisterGeneric.EMPTY)
        {
            container.setTagCompound(null);
            if (container.getItem() != GCItems.oilCanister)
            {
                this.replaceEmptyCanisterItem(container, GCItems.oilCanister);
                return;
            }
        }

        container.setItemDamage(newDamage);
    }

    private void replaceEmptyCanisterItem(ItemStack container, Item newItem)
    {
        //This is a neat trick to change the item ID in an ItemStack
        final int stackSize = container.stackSize;
        NBTTagCompound tag = new NBTTagCompound();
        container.writeToNBT(tag);
        ResourceLocation resourceloc = (ResourceLocation)Item.REGISTRY.getNameForObject(newItem);
        if (resourceloc != null) tag.setString("id", resourceloc.toString());
        tag.setShort("Damage", (short) ItemCanisterGeneric.EMPTY);
        container.readFromNBT(tag);
    }

    @Override
    public FluidStack getFluid(ItemStack container)
    {
        String fluidName = ((ItemCanisterGeneric) container.getItem()).allowedFluid;
        if (fluidName == null || ItemCanisterGeneric.EMPTY == container.getItemDamage())
        {
            return null;
        }

        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null)
        {
            return null;
        }

        return new FluidStack(fluid, ItemCanisterGeneric.EMPTY - container.getItemDamage());
    }
}
