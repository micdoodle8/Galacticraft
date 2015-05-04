package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

import java.util.List;

public abstract class ItemCanisterGeneric extends ItemFluidContainer
{
    private String allowedFluid = null;
    public final static int EMPTY = FluidContainerRegistry.BUCKET_VOLUME + 1;
    private static boolean isTELoaded = Loader.isModLoaded("ThermalExpansion"); 
	
	public ItemCanisterGeneric(String assetName)
    {
        super(0, FluidContainerRegistry.BUCKET_VOLUME);
        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setUnlocalizedName(assetName);
        this.setContainerItem(GCItems.oilCanister);
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
	    	StackTraceElement[] st = Thread.currentThread().getStackTrace();
			int imax = Math.max(st.length,5);
	        for (int i = 1; i < imax; i++)
	        {
	            String ste = st[i].getClassName();
	            if (ste.equals("thermalexpansion.block.machine.TileTransposer"))
	            {
	                return null;
	            }
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
            par1ItemStack.stackTagCompound = null;
        }
        else if (par1ItemStack.getItemDamage() <= 0) par1ItemStack.setItemDamage(1);
    }

    public void setAllowedFluid(String name)
    {
    	this.allowedFluid = new String(name);
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
        if (container.getItemDamage() == ItemCanisterGeneric.EMPTY)
        {
        	//Empty canister - find a new canister to match the fluid
        	for (String key : GalacticraftCore.itemList.keySet())
        	{
        		if (key.contains("CanisterFull"))
        		{
        			Item i = GalacticraftCore.itemList.get(key).getItem();
        			if (i instanceof ItemCanisterGeneric && fluidName.equalsIgnoreCase(((ItemCanisterGeneric)i).allowedFluid))
        			{
        	        	if (!doFill) return Math.min(resource.amount, this.capacity);
        	        	
        				this.replaceEmptyCanisterItem(container, i);
        				break;
        			}
        		}     		
        	}
        	//Delete any Forge fluid contents
	    	container.stackTagCompound = null;
        }
        else
        {
	    	//Refresh the Forge fluid contents
	    	container.stackTagCompound = null;
	    	super.fill(container, this.getFluid(container), true);
        }
    	
        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric)container.getItem()).allowedFluid))
        {	
        	int added = super.fill(container, resource, doFill);
        	if (doFill && added > 0) container.setItemDamage(Math.max(1, container.getItemDamage() - added));
        	return added;
        }
       
        return 0;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
    	if (this.allowedFluid == null || container.getItemDamage() >= ItemCanisterGeneric.EMPTY)
        	return null;
        
    	//Refresh the Forge fluid contents
    	container.stackTagCompound = null;
        super.fill(container, this.getFluid(container), true);

        FluidStack used = super.drain(container, maxDrain, doDrain);
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
            container.stackTagCompound = null;
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
		tag.setShort("id", (short)Item.getIdFromItem(newItem));
        tag.setByte("Count", (byte)stackSize);
        tag.setShort("Damage", (short)ItemCanisterGeneric.EMPTY);
        container.readFromNBT(tag);    	
    }

    @Override
    public FluidStack getFluid(ItemStack container)
    {
    	String fluidName = ((ItemCanisterGeneric)container.getItem()).allowedFluid; 
    	if (fluidName == null || ItemCanisterGeneric.EMPTY == container.getItemDamage())
        	return null;
        
        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null) return null;

        return new FluidStack(fluid, ItemCanisterGeneric.EMPTY - container.getItemDamage());
    }
}
