package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * GCCoreSlotRocketBench.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreSlotRocketBench extends Slot
{
	private final int index;
	private final int x, y, z;
	private final EntityPlayer player;

	public GCCoreSlotRocketBench(IInventory par2IInventory, int par3, int par4, int par5, int x, int y, int z, EntityPlayer player)
	{
		super(par2IInventory, par3, par4, par5);
		this.index = par3;
		this.x = x;
		this.y = y;
		this.z = z;
		this.player = player;
	}

	@Override
	public void onSlotChanged()
	{
		if (this.player instanceof EntityPlayerMP)
		{
			final Object[] toSend = { this.x, this.y, this.z };

			for (int var12 = 0; var12 < this.player.worldObj.playerEntities.size(); ++var12)
			{
				final EntityPlayerMP player = (EntityPlayerMP) this.player.worldObj.playerEntities.get(var12);

				if (player.dimension == this.player.worldObj.provider.dimensionId)
				{
					final double var14 = this.x - player.posX;
					final double var16 = this.y - player.posY;
					final double var18 = this.z - player.posZ;

					if (var14 * var14 + var16 * var16 + var18 * var18 < 20 * 20)
					{
						GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, this.x, this.y, this.z), player);
					}
				}
			}
		}
	}

	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		switch (this.index)
		{
		case 1:
			return itemStack.getItem() == GCCoreItems.partNoseCone ? true : false;
		case 2:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 3:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 4:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 5:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 6:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 7:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 8:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 9:
			return itemStack.getItem() == GCCoreItems.heavyPlatingTier1 ? true : false;
		case 10:
			return itemStack.getItem() == GCCoreItems.partFins ? true : false;
		case 11:
			return itemStack.getItem() == GCCoreItems.partFins ? true : false;
		case 12:
			return itemStack.getItem() == GCCoreItems.rocketEngine && itemStack.getItemDamage() == 0 ? true : false;
		case 13:
			return itemStack.getItem() == GCCoreItems.partFins ? true : false;
		case 14:
			return itemStack.getItem() == GCCoreItems.partFins ? true : false;
		case 15:
			return true;
		case 16:
			return true;
		case 17:
			return true;
		}

		return false;
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
