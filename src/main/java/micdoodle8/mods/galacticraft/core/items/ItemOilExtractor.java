package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemOilExtractor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemOilExtractor extends Item
{
	protected IIcon[] icons = new IIcon[5];

	public ItemOilExtractor(String assetName)
	{
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName(assetName);
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

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer player)
	{
		if (this.getNearestOilBlock(player) != null)
		{
			if (this.openCanister(player) != null)
			{
				player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
			}
		}

		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.icons = new IIcon[5];

		for (int i = 0; i < this.icons.length; i++)
		{
			this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + (i + 1));
		}

		this.itemIcon = this.icons[0];
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		Vector3 blockHit = this.getNearestOilBlock(player);

		if (blockHit != null)
		{
			final int x = MathHelper.floor_double(blockHit.x);
			final int y = MathHelper.floor_double(blockHit.y);
			final int z = MathHelper.floor_double(blockHit.z);

			if (this.isOilBlock(player, player.worldObj, x, y, z, false))
			{
				if (this.openCanister(player) != null)
				{
					final ItemStack canister = this.openCanister(player);

					if (canister != null && count % 5 == 0 && canister.getItemDamage() > 1)
					{
						this.isOilBlock(player, player.worldObj, x, y, z, true);
						canister.setItemDamage(Math.max(canister.getItemDamage() - 200, 1));
					}
				}
			}
		}
	}

	private ItemStack openCanister(EntityPlayer player)
	{
		for (final ItemStack stack : player.inventory.mainInventory)
		{
			if (stack != null && stack.getItem() instanceof ItemOilCanister)
			{
				if (stack.getMaxDamage() - stack.getItemDamage() >= 0 && stack.getMaxDamage() - stack.getItemDamage() < GCItems.oilCanister.getMaxDamage() - 1)
				{
					return stack;
				}
			}
		}

		return null;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer player)
	{
		return par1ItemStack;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		final int count2 = useRemaining / 2;

		switch (count2 % 5)
		{
		case 0:
			if (useRemaining == 0)
			{
				return this.icons[0];
			}
			return this.icons[4];
		case 1:
			return this.icons[3];
		case 2:
			return this.icons[2];
		case 3:
			return this.icons[1];
		case 4:
			return this.icons[0];
		}

		return this.icons[0];
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer player, int par4)
	{
		if (par2World.isRemote)
		{
			this.itemIcon = this.icons[0];
		}
	}

	private boolean isOilBlock(EntityPlayer player, World world, int x, int y, int z, boolean doDrain)
	{
		Block block = world.getBlock(x, y, z);

		if (block instanceof IFluidBlock)
		{
			IFluidBlock fluidBlockHit = (IFluidBlock) block;
			Fluid fluidHit = FluidRegistry.lookupFluidForBlock(block);

			if (fluidHit != null)
			{
				if (fluidHit.getName().equalsIgnoreCase("oil"))
				{
					FluidStack stack = fluidBlockHit.drain(world, x, y, z, doDrain);
					return stack != null && stack.amount > 0;
				}
			}
		}

		return false;
	}

	private Vector3 getNearestOilBlock(EntityPlayer par1EntityPlayer)
	{
		final float var4 = 1.0F;
		final float var5 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * var4;
		final float var6 = par1EntityPlayer.prevRotationYaw + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * var4;
		final double var7 = par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * var4;
		final double var9 = par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * var4 + 1.62D - par1EntityPlayer.yOffset;
		final double var11 = par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * var4;
		final Vector3 var13 = new Vector3(var7, var9, var11);
		final float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
		final float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
		final float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		final float var17 = MathHelper.sin(-var5 * 0.017453292F);
		final float var18 = var15 * var16;
		final float var20 = var14 * var16;
		double var21 = 5.0D;

		if (par1EntityPlayer instanceof EntityPlayerMP)
		{
			var21 = ((EntityPlayerMP) par1EntityPlayer).theItemInWorldManager.getBlockReachDistance();
		}

		for (double dist = 0.0; dist <= var21; dist += 1D)
		{
			final Vector3 var23 = var13.translate(new Vector3(var18 * dist, var17 * dist, var20 * dist));

			if (this.isOilBlock(par1EntityPlayer, par1EntityPlayer.worldObj, MathHelper.floor_double(var23.x), MathHelper.floor_double(var23.y), MathHelper.floor_double(var23.z), false))
			{
				return var23;
			}
		}

		return null;
	}
}
