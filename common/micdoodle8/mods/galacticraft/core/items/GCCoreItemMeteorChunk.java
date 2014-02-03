package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteorChunk;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreItemMeteorChunk.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemMeteorChunk extends Item
{
	public static final String[] names = { "meteorChunk", "meteorChunkHot" };

	public static final int METEOR_BURN_TIME = 45 * 20;

	public GCCoreItemMeteorChunk(int id, String assetName)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.tabMaterials);
		this.setTextureName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5)
	{
		if (itemstack.getItemDamage() == 1 && !world.isRemote)
		{
			if (itemstack.hasTagCompound())
			{
				float meteorBurnTime = itemstack.getTagCompound().getFloat("MeteorBurnTimeF");

				if (meteorBurnTime >= 0.5F)
				{
					meteorBurnTime -= 0.5F;
					itemstack.getTagCompound().setFloat("MeteorBurnTimeF", meteorBurnTime);
				}
				else
				{
					itemstack.setItemDamage(0);
					itemstack.stackTagCompound = null;
				}
			}
			else
			{
				itemstack.setTagCompound(new NBTTagCompound());
				itemstack.getTagCompound().setFloat("MeteorBurnTimeF", GCCoreItemMeteorChunk.METEOR_BURN_TIME);
			}
		}
	}

	@Override
	public void onCreated(ItemStack itemstack, World world, EntityPlayer entityPlayer)
	{
		super.onCreated(itemstack, world, entityPlayer);

		if (itemstack.getItemDamage() == 1)
		{
			if (!itemstack.hasTagCompound())
			{
				itemstack.setTagCompound(new NBTTagCompound());
			}

			itemstack.getTagCompound().setFloat("MeteorBurnTimeF", GCCoreItemMeteorChunk.METEOR_BURN_TIME);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
	}

	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (itemstack.getItemDamage() > 0)
		{
			float burnTime = 0.0F;

			if (itemstack.hasTagCompound())
			{
				float meteorBurnTime = itemstack.getTagCompound().getFloat("MeteorBurnTimeF");
				burnTime = Math.round(meteorBurnTime / 10.0F) / 2.0F;
			}
			else
			{
				burnTime = 45.0F;
			}

			par3List.add(StatCollector.translateToLocal("item.hotDescription.name") + " " + burnTime + "s");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return "item." + GCCoreItemMeteorChunk.names[itemStack.getItemDamage()];
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
		{
			--itemStack.stackSize;
		}

		world.playSoundAtEntity(player, "random.bow", 1.0F, 0.0001F / (Item.itemRand.nextFloat() * 0.1F));

		if (!world.isRemote)
		{
			GCCoreEntityMeteorChunk meteor = new GCCoreEntityMeteorChunk(world, player, 1.0F);

			if (itemStack.getItemDamage() > 0)
			{
				meteor.setFire(20);
				meteor.isHot = true;
			}

			world.spawnEntityInWorld(meteor);
		}

		return itemStack;
	}

}
