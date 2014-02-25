package micdoodle8.mods.galacticraft.mars.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsTileEntityCryogenicChamber.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEntityCryogenicChamber extends TileEntityMulti implements IMultiBlock
{
	public boolean isOccupied;

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return TileEntity.INFINITE_EXTENT_AABB;
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
//		if (this.worldObj.isRemote)
//		{
//			return false;
//		}
//
//		EnumStatus enumstatus = this.sleepInBedAt(entityPlayer, this.xCoord, this.yCoord, this.zCoord);
//
//		switch (enumstatus)
//		{
//		case OK:
//			if (this.worldObj instanceof WorldServer && entityPlayer instanceof EntityPlayerMP)
//			{
//				((EntityPlayerMP) entityPlayer).getServerForPlayer().getEntityTracker().func_151248_b(((EntityPlayerMP) entityPlayer), new S0BPacketAnimation(((EntityPlayerMP) entityPlayer), 2));
//				Packet17Sleep packet17sleep = new Packet17Sleep(entityPlayer, 0, this.xCoord, this.yCoord, this.zCoord);
//				((WorldServer) this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(entityPlayer, packet17sleep);
//				((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(this.xCoord, this.yCoord, this.zCoord, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
//				((EntityPlayerMP) entityPlayer).playerNetServerHandler.sendPacketToPlayer(packet17sleep);
//			}
//
//			return true;
//		case NOT_POSSIBLE_NOW:
//			entityPlayer.addChatMessage(new ChatComponentTranslation("I can't use this for another " + ((GCEntityPlayerMP) entityPlayer).getCryogenicChamberCooldown() / 20 + " seconds"));
//			return false;
//		default:
//			return false;
//		}
		
		return false; // TODO Fix cryo chambers
	}

	public EnumStatus sleepInBedAt(EntityPlayer entityPlayer, int par1, int par2, int par3)
	{
		if (!this.worldObj.isRemote)
		{
			if (entityPlayer.isPlayerSleeping() || !entityPlayer.isEntityAlive())
			{
				return EnumStatus.OTHER_PROBLEM;
			}

			if (!this.worldObj.provider.isSurfaceWorld())
			{
				return EnumStatus.NOT_POSSIBLE_HERE;
			}

			if (((GCEntityPlayerMP) entityPlayer).getCryogenicChamberCooldown() > 0)
			{
				return EnumStatus.NOT_POSSIBLE_NOW;
			}
		}

		if (entityPlayer.isRiding())
		{
			entityPlayer.mountEntity((Entity) null);
		}

		entityPlayer.setPosition(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F);

		entityPlayer.sleeping = true;
		entityPlayer.sleepTimer = 0;
		entityPlayer.playerLocation = new ChunkCoordinates(par1, par2, par3);
		entityPlayer.motionX = entityPlayer.motionZ = entityPlayer.motionY = 0.0D;

		if (!this.worldObj.isRemote)
		{
			this.worldObj.updateAllPlayersSleepingFlag();
		}

		return EnumStatus.OK;
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}

	@Override
	public void onCreate(Vector3 placedPosition)
	{
		this.mainBlockPosition = placedPosition;

		int x1 = 0;
		int x2 = 0;
		int z1 = 0;
		int z2 = 0;

		switch (this.getBlockMetadata() - BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
		case 0:
			x1 = 0;
			x2 = 0;
			z1 = -1;
			z2 = 1;
			break;
		case 1:
			x1 = 0;
			x2 = 0;
			z1 = -1;
			z2 = 1;
			break;
		case 2:
			x1 = -1;
			x2 = 1;
			z1 = 0;
			z2 = 0;
			break;
		case 3:
			x1 = -1;
			x2 = 1;
			z1 = 0;
			z2 = 0;
			break;
		}

		for (int x = x1; x <= x2; x++)
		{
			for (int z = z1; z <= z2; z++)
			{
				for (int y = 0; y < 4; y++)
				{
					final Vector3 vecToAdd = new Vector3(placedPosition.x + x, placedPosition.y + y, placedPosition.z + z);

					if (!vecToAdd.equals(placedPosition))
					{
						((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 5);
					}
				}
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
		final Vector3 thisBlock = new Vector3(this);

		int x1 = 0;
		int x2 = 0;
		int z1 = 0;
		int z2 = 0;

		switch (this.getBlockMetadata() - BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
		{
		case 0:
			x1 = 0;
			x2 = 0;
			z1 = -1;
			z2 = 1;
			break;
		case 1:
			x1 = 0;
			x2 = 0;
			z1 = -1;
			z2 = 1;
			break;
		case 2:
			x1 = -1;
			x2 = 1;
			z1 = 0;
			z2 = 0;
			break;
		case 3:
			x1 = -1;
			x2 = 1;
			z1 = 0;
			z2 = 0;
			break;
		}

		for (int x = x1; x <= x2; x++)
		{
			for (int z = z1; z <= z2; z++)
			{
				for (int y = 0; y < 4; y++)
				{
					if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
					{
						FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, MarsBlocks.machine, Block.getIdFromBlock(MarsBlocks.machine) >> 12 & 255);
					}
					
					this.worldObj.setBlockToAir(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z);
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.isOccupied = nbt.getBoolean("IsChamberOccupied");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("IsChamberOccupied", this.isOccupied);
	}
}
