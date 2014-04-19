package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemWrench.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemUniversalWrench extends Item
{
	public ItemUniversalWrench(String assetName)
	{
		super();
		this.setUnlocalizedName(assetName);
		this.setMaxStackSize(1);
		this.setMaxDamage(256);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = "BuildCraft|Core")
	public boolean canWrench(EntityPlayer entityPlayer, int x, int y, int z)
	{
		return true;
	}

	@RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = "BuildCraft|Core")
	public void wrenchUsed(EntityPlayer entityPlayer, int x, int y, int z)
	{
		ItemStack stack = entityPlayer.inventory.getCurrentItem();

		if (stack != null)
		{
			stack.damageItem(1, entityPlayer);

			if (stack.getItemDamage() >= stack.getMaxDamage())
			{
				stack.stackSize--;
			}

			if (stack.stackSize <= 0)
			{
				entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
			}
		}
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		Block blockID = world.getBlock(x, y, z);

		if (blockID == Blocks.furnace || blockID == Blocks.lit_furnace || blockID == Blocks.dropper || blockID == Blocks.hopper || blockID == Blocks.dispenser || blockID == Blocks.piston || blockID == Blocks.sticky_piston)
		{
			int metadata = world.getBlockMetadata(x, y, z);

			int[] rotationMatrix = { 1, 2, 3, 4, 5, 0 };

			if (blockID == Blocks.furnace || blockID == Blocks.lit_furnace)
			{
				rotationMatrix = ForgeDirection.ROTATION_MATRIX[0];
			}

			world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.getOrientation(rotationMatrix[metadata]).ordinal(), 3);
			this.wrenchUsed(entityPlayer, x, y, z);

			return true;
		}

		return false;
	}
}
