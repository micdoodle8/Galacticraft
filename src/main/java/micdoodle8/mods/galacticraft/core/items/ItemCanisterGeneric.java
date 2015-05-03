package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
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
        if (itemStack != null && itemStack.getItem() == this.getContainerItem() && itemStack.getItemDamage() == ItemCanisterGeneric.EMPTY)
        {
            return null;
        }

        return new ItemStack(this.getContainerItem(), 1, ItemCanisterGeneric.EMPTY);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (ItemCanisterGeneric.EMPTY == par1ItemStack.getItemDamage())
        {
            final int stackSize = par1ItemStack.stackSize;

            if (!(par1ItemStack.getItem() instanceof ItemOilCanister))
            {
            	NBTTagCompound tag = new NBTTagCompound();
    			tag.setShort("id", (short)Item.getIdFromItem(GCItems.oilCanister));
    	        tag.setByte("Count", (byte)stackSize);
    	        tag.setShort("Damage", (short)ItemCanisterGeneric.EMPTY);
    	        par1ItemStack.readFromNBT(tag);
            }
        }
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
    	if (resource == null || resource.getFluid() == null || !(container.getItem() instanceof ItemCanisterGeneric))
        {
            return 0;
        }
        
        String fluidName = resource.getFluid().getName();
        if (container.getItemDamage() == ItemCanisterGeneric.EMPTY)
        {
        	//Empty canister - find a new canister to match the fluid
        	if (ConfigManagerCore.enableDebug) GCLog.info("Searching for matching container for "+fluidName);
        	for (String key : GalacticraftCore.itemList.keySet())
        	{
        		if (key.contains("CanisterFull"))
        		{
        			Item i = GalacticraftCore.itemList.get(key).getItem();
        			if (i instanceof ItemCanisterGeneric && fluidName.equalsIgnoreCase(((ItemCanisterGeneric)i).allowedFluid))
        			{
        				if (ConfigManagerCore.enableDebug) System.out.println("Found "+key);
                    	NBTTagCompound tag = new NBTTagCompound();
        				tag.setShort("id", (short)Item.getIdFromItem(i));
        		        tag.setByte("Count", (byte)1);
        		        tag.setShort("Damage", (short)ItemCanisterGeneric.EMPTY);
        				container.readFromNBT(tag);
        				if (ConfigManagerCore.enableDebug) System.out.println("Set container for " + ((ItemCanisterGeneric)container.getItem()).allowedFluid + " to "+container.getItem().getClass().getSimpleName());
        				break;
        			}
        		}
        	}
        }

        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric)container.getItem()).allowedFluid))
        {	
        	int added = super.fill(container, resource, doFill);
        	container.setItemDamage(Math.max(1, container.getItemDamage() - added));
        	return added;
        }
       
        return 0;
    }
    
    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
    	if (this.allowedFluid == null)
        	return null;
        
        container.stackTagCompound = null;
        
        super.fill(container, this.getFluid(container), true);

        FluidStack used = super.drain(container, maxDrain, doDrain);
        if (doDrain && used != null)
        {
        	container.setItemDamage(Math.min(container.getItemDamage() + used.amount, ItemCanisterGeneric.EMPTY));
        }
        return used;
    }

    @Override
    public FluidStack getFluid(ItemStack container)
    {
    	if (this.allowedFluid == null || ItemCanisterGeneric.EMPTY == container.getItemDamage())
        	return null;
        
        Fluid fluid = FluidRegistry.getFluid(this.allowedFluid);
        if (fluid == null) return null;

        return new FluidStack(fluid, ItemCanisterGeneric.EMPTY - container.getItemDamage());
    }
}
