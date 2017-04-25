package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Field;

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
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemCanisterGeneric extends ItemFluidContainer
{
	//TODO:  in 1.11.2 implement CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY
	
    private String allowedFluid = null;
    public final static int EMPTY = Fluid.BUCKET_VOLUME + 1;
    private static boolean isTELoaded = CompatibilityManager.isTELoaded();
    private final ItemStack containerStack = new ItemStack(this, 1, 1);
    private final ItemStack emptyContainerStack = this.getContainerItem(this.containerStack);

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
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new FluidHandlerItemStack.SwapEmpty(containerStack, emptyContainerStack, this.capacity);
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

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 1));
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
            //Set this to a clean empty item
            container.setItemDamage(ItemCanisterGeneric.EMPTY);
        }
        else
        {
            //TODO: Refresh the Forge fluid contents (is this required in 1.11?)
            //container.setTagCompound(null);
        }

        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric) container.getItem()).allowedFluid))
        {
            int added = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container).fill(resource, doFill);
            if (doFill && added > 0)
            {
                container.setItemDamage(Math.max(1, container.getItemDamage() - added));
            }
            return added;
        }

        return 0;
    }

    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (this.allowedFluid == null || container.getItemDamage() >= ItemCanisterGeneric.EMPTY)
        {
            return null;
        }

        //TODO: Refresh the Forge fluid contents - is this necessary in 1.11?
        //container.setTagCompound(null);
        //super.fill(container, this.getFluid(container), true);

        FluidStack used = net.minecraftforge.fluids.FluidUtil.getFluidHandler(container).drain(maxDrain, doDrain);
        if (doDrain && used != null && used.amount > 0)
        {
            this.setNewDamage(container, container.getItemDamage() + used.amount);
        }
        return used;
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
    	try
    	{
    		Field itemId = container.getClass().getField("item");
    		itemId.setAccessible(true);
    		itemId.set(container, newItem);
    	}
    	catch (Exception ignore) { }
    }
}
