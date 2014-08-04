package micdoodle8.mods.galacticraft.api.transmission.item;

import ic2.api.item.IElectricItemManager;
import micdoodle8.mods.galacticraft.api.transmission.EnergyHelper;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.ElectricItemManagerIC2;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.ElectricItemManagerIC2_1710;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.core.items.ItemBatteryInfinite;
import micdoodle8.mods.miccore.Annotations.AltForVersion;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import micdoodle8.mods.miccore.Annotations.VersionSpecific;
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

import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.relauncher.FMLInjectionData;

public abstract class ItemElectric extends Item implements IItemElectric
{
	private static Object itemManagerIC2;
	public float transferMax;
	private DefaultArtifactVersion mcVersion = null;
	
	public ItemElectric()
	{
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
		this.setNoRepair();
		this.setMaxTransfer();
		
        this.mcVersion = new DefaultArtifactVersion((String) FMLInjectionData.data()[4]);
		
		if (NetworkConfigHandler.isIndustrialCraft2Loaded())
		{
			if (VersionParser.parseRange("[1.7.2]").containsVersion(mcVersion))
				itemManagerIC2 = new ElectricItemManagerIC2();
			else
				itemManagerIC2 = new ElectricItemManagerIC2_1710();
		}
	}

	protected void setMaxTransfer()
	{
		this.transferMax = 200;
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
		if (energyToReceive > this.transferMax)
		{
			rejectedElectricity += energyToReceive - this.transferMax;
			energyToReceive = this.transferMax;
		}

		if (doReceive)
		{
			this.setElectricity(itemStack, this.getElectricityStored(itemStack) + energyToReceive);
		}

		return energyToReceive;
	}

	@Override
	public float discharge(ItemStack itemStack, float energy, boolean doTransfer)
	{
		float energyToTransfer = Math.min(Math.min(this.getElectricityStored(itemStack), energy), this.transferMax);

		if (doTransfer)
		{
			this.setElectricity(itemStack, this.getElectricityStored(itemStack) - energyToTransfer);
		}

		return energyToTransfer;
	}

	@Override
	public int getTierGC(ItemStack itemStack)
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
		return Math.min(this.transferMax, this.getMaxElectricityStored(itemStack) - this.getElectricityStored(itemStack));
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
			if (item instanceof ic2.api.item.ISpecialElectricItem)
			{	
				return true;
			}
		}
		
		return false;
	}

	//The following seven methods are for Mekanism compatibility 

	@RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
	public double getEnergy(ItemStack itemStack)
	{
		return this.getElectricityStored(itemStack) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
	public void setEnergy(ItemStack itemStack, double amount)
	{
		this.setElectricity(itemStack, (float) amount * NetworkConfigHandler.MEKANISM_RATIO);
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
	public double getMaxEnergy(ItemStack itemStack)
	{
		return this.getMaxElectricityStored(itemStack) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
	public double getMaxTransfer(ItemStack itemStack)
	{
		return this.getTransfer(itemStack) * NetworkConfigHandler.TO_MEKANISM_RATIO;
	}

	@RuntimeInterface(clazz = "mekanism.api.energy.IEnergizedItem", modID = "Mekanism")
	public boolean canReceive(ItemStack itemStack)
	{
		return (itemStack != null && !(itemStack.getItem() instanceof ItemBatteryInfinite));
	}

	public boolean canSend(ItemStack itemStack)
	{
		return true;
	}

	public boolean isMetadataSpecific(ItemStack itemStack)
	{
		return false;
	}
	
	//All the following methods are for IC2 compatibility
	
	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public IElectricItemManager getManager(ItemStack itemstack)
	{
		return (IElectricItemManager) ItemElectric.itemManagerIC2;
	}

	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public boolean canProvideEnergy(ItemStack itemStack)
	{
		return true;
	}

	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public Item getChargedItem(ItemStack itemStack)
	{
		return itemStack.getItem();
	}

	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public Item getEmptyItem(ItemStack itemStack)
	{
		return itemStack.getItem();	
	}

	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public int getTier(ItemStack itemStack)
	{
		return 1;
	}

	@VersionSpecific(version = "[1.7.10]")
	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public double getMaxCharge(ItemStack itemStack)
	{
		return this.getMaxElectricityStored(itemStack) * NetworkConfigHandler.TO_IC2_RATIO;
	}

	@AltForVersion(version = "[1.7.2]")
	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public int getMaxChargeB(ItemStack itemStack)
	{
		return (int) (this.getMaxElectricityStored(itemStack) * NetworkConfigHandler.TO_IC2_RATIO);
	}

	@VersionSpecific(version = "[1.7.10]")
	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public double getTransferLimit(ItemStack itemStack)
	{
		return this.transferMax * NetworkConfigHandler.TO_IC2_RATIO;
	}

	@VersionSpecific(version = "[1.7.2]")
	@RuntimeInterface(clazz = "ic2.api.item.ISpecialElectricItem", modID = "IC2")
	public int getTransferLimitB(ItemStack itemStack)
	{
		return (int) (this.transferMax * NetworkConfigHandler.TO_IC2_RATIO);
	}
}
