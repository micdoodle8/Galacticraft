package micdoodle8.mods.galacticraft.api.transmission.core.item;

import micdoodle8.mods.galacticraft.api.transmission.EnergyHelper;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.world.World;

import java.util.List;

public abstract class ItemElectric extends Item implements IItemElectric
{
	public ItemElectric()
	{
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setNoRepair();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4)
	{
		String color = "";
		float joules = this.getElectricityStored(itemStack);

		if (joules <= this.getMaxElectricityStored(itemStack) / 3)
		{
			color = "\u00a74";
		}
		else if (joules > this.getMaxElectricityStored(itemStack) * 2 / 3)
		{
			color = "\u00a72";
		}
		else
		{
			color = "\u00a76";
		}

		list.add(color + EnergyHelper.getEnergyDisplayS(joules) + "/" + EnergyHelper.getEnergyDisplayS(this.getMaxElectricityStored(itemStack)));
	}

	/**
	 * Makes sure the item is uncharged when it is crafted and not charged.
	 * Change this if you do not want this to happen!
	 */
	@Override
	public void onCreated(ItemStack itemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		this.setElectricity(itemStack, 0);
	}

	@Override
	public float recharge(ItemStack itemStack, float energy, boolean doReceive)
	{
		float rejectedElectricity = Math.max(this.getElectricityStored(itemStack) + energy - this.getMaxElectricityStored(itemStack), 0);
		float energyToReceive = energy - rejectedElectricity;

		if (doReceive)
		{
			this.setElectricity(itemStack, this.getElectricityStored(itemStack) + energyToReceive);
		}

		return energyToReceive;
	}

	@Override
	public float discharge(ItemStack itemStack, float energy, boolean doTransfer)
	{
		float energyToTransfer = Math.min(this.getElectricityStored(itemStack), energy);

		if (doTransfer)
		{
			this.setElectricity(itemStack, this.getElectricityStored(itemStack) - energyToTransfer);
		}

		return energyToTransfer;
	}

	@Override
	public int getTier(ItemStack itemStack)
	{
		return 1;
	}

	@Override
	public void setElectricity(ItemStack itemStack, float joules)
	{
		// Saves the frequency in the ItemStack
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		float electricityStored = Math.max(Math.min(joules, this.getMaxElectricityStored(itemStack)), 0);
		itemStack.getTagCompound().setFloat("electricity", electricityStored);

		/** Sets the damage as a percentage to render the bar properly. */
		itemStack.setItemDamage((int) (100 - electricityStored / this.getMaxElectricityStored(itemStack) * 100));
	}

	@Override
	public float getTransfer(ItemStack itemStack)
	{
		return this.getMaxElectricityStored(itemStack) - this.getElectricityStored(itemStack);
	}

	/** Gets the energy stored in the item. Energy is stored using item NBT */
	@Override
	public float getElectricityStored(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}
		float energyStored = 0f;
		if (itemStack.getTagCompound().hasKey("electricity"))
		{
			NBTBase obj = itemStack.getTagCompound().getTag("electricity");
			if (obj instanceof NBTTagDouble)
			{
				energyStored = ((NBTTagDouble) obj).func_150288_h();
			}
			else if (obj instanceof NBTTagFloat)
			{
				energyStored = ((NBTTagFloat) obj).func_150288_h();
			}
		}

		/** Sets the damage as a percentage to render the bar properly. */
		itemStack.setItemDamage((int) (100 - energyStored / this.getMaxElectricityStored(itemStack) * 100));
		return energyStored;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(ElectricItemHelper.getUncharged(new ItemStack(this)));
		par3List.add(ElectricItemHelper.getWithCharge(new ItemStack(this), this.getMaxElectricityStored(new ItemStack(this))));
	}

	public static boolean isElectricItem(Item item)
	{
		if (item instanceof ItemElectric) return true;
		
		if (NetworkConfigHandler.isIndustrialCraft2Loaded())
		{
			if (item instanceof ic2.api.item.ISpecialElectricItem) return true;
		}
		
		return false;
	}
}
