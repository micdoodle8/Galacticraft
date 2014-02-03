package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEntityOxygenBubble.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEntityOxygenBubble extends Entity implements IPacketReceiver, ISizeable
{
	private float size;
	protected long ticks = 0;

	public GCCoreTileEntityOxygenDistributor distributor;

	public GCCoreEntityOxygenBubble(World world, Vector3 mainBlockVec, GCCoreTileEntityOxygenDistributor distributor)
	{
		this(world);
		this.posX = mainBlockVec.x + 0.5D;
		this.posY = mainBlockVec.y + 1.0D;
		this.posZ = mainBlockVec.z + 0.5D;
		this.distributor = distributor;
	}

	public GCCoreEntityOxygenBubble(World world)
	{
		super(world);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}

	@Override
	protected boolean pushOutOfBlocks(double par1, double par3, double par5)
	{
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
	{
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
	}

	@Override
	public void onEntityUpdate()
	{
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		if (this.distributor != null)
		{
			final Vector3 vec = new Vector3(this.distributor);

			this.posX = vec.x + 0.5D;
			this.posY = vec.y + 1.0D;
			this.posZ = vec.z + 0.5D;
		}

		super.onEntityUpdate();

		final TileEntity tileAt = this.worldObj.getBlockTileEntity(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1.0), MathHelper.floor_double(this.posZ));

		if (tileAt instanceof GCCoreTileEntityOxygenDistributor)
		{
			final GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) tileAt;

			this.distributor = distributor;
		}

		if (this.distributor != null && (this.distributor.oxygenBubble == null || this.distributor.oxygenBubble.equals(this)) && !this.worldObj.isRemote)
		{
			this.distributor.oxygenBubble = this;
		}
		else if (!this.worldObj.isRemote)
		{
			this.setDead();
		}

		if (tileAt == null && !this.worldObj.isRemote)
		{
			this.setDead();
		}

		if (!this.worldObj.isRemote && this.distributor != null)
		{
			if (this.distributor.getEnergyStored() > 0.0F && this.distributor.storedOxygen > this.distributor.oxygenPerTick)
			{
				this.size += 0.01F;
			}
			else
			{
				this.size -= 0.1F;
			}

			this.size = Math.min(Math.max(this.size, 0.0F), 10.0F);
		}

		if (this.distributor != null)
		{
			final Vector3 vec = new Vector3(this.distributor);

			this.posX = vec.x + 0.5D;
			this.posY = vec.y + 1.0D;
			this.posZ = vec.z + 0.5D;
		}

		if (!this.worldObj.isRemote && this.ticks % 5 == 0)
		{
			GCCorePacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 50);
		}
	}

	public Packet getDescriptionPacket()
	{
		return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.size);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.size = dataStream.readFloat();
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	public void setSize(float bubbleSize)
	{
		this.size = bubbleSize;
	}

	@Override
	public float getSize()
	{
		return this.size;
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		if (nbt.getTags().contains("bubbleSize"))
		{
			this.size = (float) nbt.getDouble("bubbleSize");
		}
		else
		{
			this.size = nbt.getFloat("bubbleSizeF");
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setFloat("bubbleSizeF", this.size);
	}
}
