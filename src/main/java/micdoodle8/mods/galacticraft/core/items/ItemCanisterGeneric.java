package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemCanisterGeneric extends ItemFluidContainer
{
    public final static int EMPTY = Fluid.BUCKET_VOLUME + 1;
    private static boolean isTELoaded = CompatibilityManager.isTELoaded();
	
    private String allowedFluid = null;

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
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new ItemCanisterGenericHandler(stack);
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            list.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        //Workaround for strange behaviour in TE Transposer
        if (isTELoaded)
        {
            if (JavaUtil.instance.isCalledBy("thermalexpansion.block.machine.TileTransposer"))
            {
                return ItemStack.EMPTY;
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
        if (resource == null || resource.getFluid() == null || resource.amount <= 0 || container == null || container.getItemDamage() <= 1 || !(container.getItem() instanceof ItemCanisterGeneric))
        {
            return 0;
        }
        String fluidName = resource.getFluid().getName();

        int capacityPlusOne = container.getItemDamage();
        if (capacityPlusOne <= 1)
        {
        	if (capacityPlusOne < 1)
        	{
	            //It shouldn't be possible, but just in case, set this to a proper filled item
        		container.setItemDamage(1);
        	}
        	return 0;
        }
        if (capacityPlusOne >= ItemCanisterGeneric.EMPTY)
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
            if (capacityPlusOne > ItemCanisterGeneric.EMPTY)
            {
	            //It shouldn't be possible, but just in case, set this to a proper empty item
            	capacityPlusOne = ItemCanisterGeneric.EMPTY;
	            container.setItemDamage(capacityPlusOne);
            }
        }
        
        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric) container.getItem()).allowedFluid))
        {
            int added = Math.min(resource.amount, capacityPlusOne - 1);
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

        FluidStack used = this.getFluid(container);
        if (used != null && used.amount > maxDrain) used.amount = maxDrain;
        if (doDrain && used != null && used.amount > 0)
        {
            this.setNewDamage(container, container.getItemDamage() + used.amount);
        }
        return used;
    }

    protected void setNewDamage(ItemStack container, int newDamage)
    {
        newDamage = Math.min(newDamage, ItemCanisterGeneric.EMPTY);
        container.setItemDamage(newDamage);
        if (newDamage == ItemCanisterGeneric.EMPTY)
        {
            if (container.getItem() != GCItems.oilCanister)
            {
                this.replaceEmptyCanisterItem(container, GCItems.oilCanister);
                return;
            }
        }
    }

    private void replaceEmptyCanisterItem(ItemStack container, Item newItem)
    {
    	try
    	{
    		Class itemStack = container.getClass();
    		Field itemId = itemStack.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "item" : "field_151002_e");
    		itemId.setAccessible(true);
    		itemId.set(container, newItem);
    		Method forgeInit = itemStack.getDeclaredMethod("forgeInit");
    		forgeInit.setAccessible(true);
    		forgeInit.invoke(container);
    	}
    	catch (Exception ignore) { }
    }
    
    public FluidStack getFluid(ItemStack container)
    {
        String fluidName = ((ItemCanisterGeneric) container.getItem()).allowedFluid;
        if (fluidName == null || container.getItemDamage() >= ItemCanisterGeneric.EMPTY )
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
