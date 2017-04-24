package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Field;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
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
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            int imax = Math.max(st.length, 5);
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
