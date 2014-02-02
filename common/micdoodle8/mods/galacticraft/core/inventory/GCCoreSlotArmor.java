package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreSlotArmor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotArmor extends Slot
{
	final int armorType;
	final EntityPlayer thePlayer;

	public GCCoreSlotArmor(EntityPlayer thePlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6)
	{
		super(par2IInventory, par3, par4, par5);
		this.thePlayer = thePlayer;
		this.armorType = par6;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		final Item item = par1ItemStack == null ? null : par1ItemStack.getItem();
		return item != null && item.isValidArmor(par1ItemStack, this.armorType, this.thePlayer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBackgroundIconIndex()
	{
		return ItemArmor.func_94602_b(this.armorType);
	}
}
