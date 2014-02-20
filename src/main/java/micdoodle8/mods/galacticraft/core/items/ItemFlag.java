package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemFlag.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemFlag extends Item implements IHoldableItem
{
//	public static final String[] names = { "american", // 0
//	"black", // 1
//	"blue", // 2
//	"green", // 3
//	"brown", // 4
//	"darkblue", // 5
//	"darkgray", // 6
//	"darkgreen", // 7
//	"gray", // 8
//	"magenta", // 9
//	"orange", // 10
//	"pink", // 11
//	"purple", // 12
//	"red", // 13
//	"teal", // 14
//	"yellow", // 15
//	"white" }; // 16

	public ItemFlag(String assetName)
	{
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName(assetName);
		this.setTextureName("arrow");
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer player, int par4)
	{
		final int useTime = this.getMaxItemUseDuration(par1ItemStack) - par4;

		boolean placed = false;

		final MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, player, true);

		float var7 = useTime / 5.0F;
		var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

		if (var7 > 1.0F)
		{
			var7 = 1.0F;
		}

		if (var7 == 1.0F && var12 != null && var12.typeOfHit == MovingObjectType.BLOCK)
		{
			final int x = var12.blockX;
			final int y = var12.blockY;
			final int z = var12.blockZ;

			if (!par2World.isRemote)
			{
				final EntityFlag flag = new EntityFlag(par2World, x + 0.5F, y + 1.0F, z + 0.5F, player.rotationYaw - 90F);

				if (par2World.getEntitiesWithinAABB(EntityFlag.class, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 3, z + 1)).isEmpty())
				{
					par2World.spawnEntityInWorld(flag);
					flag.setType(par1ItemStack.getItemDamage());
					flag.setOwner(player.getGameProfile().getName());
					placed = true;
				}
				else
				{
					player.addChatMessage(new ChatComponentText("Flag already placed here!"));
				}
			}

			if (placed)
			{
				final int var2 = this.getInventorySlotContainItem(player, par1ItemStack);

				if (var2 >= 0 && !player.capabilities.isCreativeMode)
				{
					if (--player.inventory.mainInventory[var2].stackSize <= 0)
					{
						player.inventory.mainInventory[var2] = null;
					}
				}
			}
		}
	}

	private int getInventorySlotContainItem(EntityPlayer player, ItemStack stack)
	{
		for (int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2)
		{
			if (player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].isItemEqual(stack))
			{
				return var2;
			}
		}

		return -1;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.none;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxy.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item.flag";
	}

	@Override
	public boolean shouldHoldLeftHandUp(EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean shouldHoldRightHandUp(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean shouldCrouch(EntityPlayer player)
	{
		return false;
	}
}
