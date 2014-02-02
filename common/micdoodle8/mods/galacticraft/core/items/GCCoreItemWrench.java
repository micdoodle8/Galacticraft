package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
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
public class GCCoreItemWrench extends Item
{
	public GCCoreItemWrench(int id, String assetName)
	{
		super(id);
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
	public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);

		if (blockID == Block.furnaceIdle.blockID || blockID == Block.furnaceBurning.blockID || blockID == Block.dropper.blockID || blockID == Block.hopperBlock.blockID || blockID == Block.dispenser.blockID || blockID == Block.pistonBase.blockID || blockID == Block.pistonStickyBase.blockID)
		{
			int metadata = world.getBlockMetadata(x, y, z);

			int[] rotationMatrix = { 1, 2, 3, 4, 5, 0 };

			if (blockID == Block.furnaceIdle.blockID || blockID == Block.furnaceBurning.blockID)
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
