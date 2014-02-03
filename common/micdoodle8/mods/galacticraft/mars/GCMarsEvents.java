package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore.OrientCameraEvent;
import micdoodle8.mods.galacticraft.core.client.render.entities.GCCoreRenderPlayer.RotatePlayerEvent;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.event.GCCoreEventWakePlayer;
import micdoodle8.mods.galacticraft.core.event.GCCoreLandingPadRemovalEvent;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.dimension.GCMarsWorldProvider;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityCryogenicChamber;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.world.gen.GCMarsWorldGenEggs;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsEvents.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsEvents
{
	@ForgeSubscribe
	public void onLivingDeath(LivingDeathEvent event)
	{
		if (event.source.damageType.equals("slimeling") && event.source instanceof EntityDamageSource)
		{
			EntityDamageSource source = (EntityDamageSource) event.source;

			if (source.getEntity() instanceof GCMarsEntitySlimeling && !source.getEntity().worldObj.isRemote)
			{
				((GCMarsEntitySlimeling) source.getEntity()).kills++;
			}
		}
	}

	@ForgeSubscribe
	public void onLivingAttacked(LivingAttackEvent event)
	{
		if (!event.entity.isEntityInvulnerable() && !event.entity.worldObj.isRemote && event.entityLiving.getHealth() <= 0.0F && !(event.source.isFireDamage() && event.entityLiving.isPotionActive(Potion.fireResistance)))
		{
			Entity entity = event.source.getEntity();

			if (entity instanceof GCMarsEntitySlimeling)
			{
				GCMarsEntitySlimeling entitywolf = (GCMarsEntitySlimeling) entity;

				if (entitywolf.isTamed())
				{
					event.entityLiving.recentlyHit = 100;
					event.entityLiving.attackingPlayer = null;
				}
			}
		}
	}

	@ForgeSubscribe
	public void onPlayerWakeUp(GCCoreEventWakePlayer event)
	{
		ChunkCoordinates c = event.entityPlayer.playerLocation;
		int blockID = event.entityPlayer.worldObj.getBlockId(c.posX, c.posY, c.posZ);
		int metadata = event.entityPlayer.worldObj.getBlockMetadata(c.posX, c.posY, c.posZ);

		if (blockID == GCMarsBlocks.machine.blockID && metadata >= GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA)
		{
			if (event.flag1 == false && event.flag2 == true && event.flag3 == true)
			{
				event.result = EnumStatus.NOT_POSSIBLE_HERE;

				if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && event.bypassed && event.entityPlayer instanceof GCCorePlayerSP)
				{
					PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 3, new Object[] {}));
				}
			}
			else if (event.flag1 == false && event.flag2 == false && event.flag3 == true)
			{
				if (!event.entityPlayer.worldObj.isRemote)
				{
					event.entityPlayer.heal(5.0F);
					((GCCorePlayerMP) event.entityPlayer).setCryogenicChamberCooldown(6000);

					for (WorldServer worldServer : MinecraftServer.getServer().worldServers)
					{
						worldServer.setWorldTime(0);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onPlayerRotate(RotatePlayerEvent event)
	{
		ChunkCoordinates c = event.entityPlayer.playerLocation;
		int blockID = event.entityPlayer.worldObj.getBlockId(c.posX, c.posY, c.posZ);
		int metadata = event.entityPlayer.worldObj.getBlockMetadata(c.posX, c.posY, c.posZ);

		if (blockID == GCMarsBlocks.machine.blockID && metadata >= GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA)
		{
			event.shouldRotate = false;
		}
	}

	private WorldGenerator eggGenerator;

	@ForgeSubscribe
	public void onPlanetDecorated(GCCoreEventPopulate.Post event)
	{
		if (this.eggGenerator == null)
		{
			this.eggGenerator = new GCMarsWorldGenEggs(GCMarsBlocks.rock.blockID);
		}

		if (event.worldObj.provider instanceof GCMarsWorldProvider)
		{
			int eggsPerChunk = 2;
			int x;
			int y;
			int z;

			for (int eggCount = 0; eggCount < eggsPerChunk; ++eggCount)
			{
				x = event.chunkX + event.rand.nextInt(16) + 8;
				y = event.rand.nextInt(128);
				z = event.chunkZ + event.rand.nextInt(16) + 8;
				this.eggGenerator.generate(event.worldObj, event.rand, x, y, z);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void orientCamera(OrientCameraEvent event)
	{
		EntityPlayer entity = Minecraft.getMinecraft().thePlayer;

		if (entity != null)
		{
			int x = MathHelper.floor_double(entity.posX);
			int y = MathHelper.floor_double(entity.posY);
			int z = MathHelper.floor_double(entity.posZ);
			int blockID = Minecraft.getMinecraft().theWorld.getBlockId(x, y, z);
			TileEntity tile = Minecraft.getMinecraft().theWorld.getBlockTileEntity(x, y, z);

			if (tile instanceof GCMarsTileEntityCryogenicChamber)
			{
				int var12 = Block.blocksList[blockID].getBedDirection(Minecraft.getMinecraft().theWorld, x, y, z);
				GL11.glRotatef(-var12 * 90, 0.0F, 1.0F, 0.0F);

				float rotation = 0.0F;

				switch (tile.getBlockMetadata() - GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA)
				{
				case 0:
					rotation = 270.0F;
					break;
				case 1:
					rotation = 90.0F;
					break;
				case 2:
					rotation = 180.0F;
					break;
				case 3:
					rotation = 0.0F;
					break;
				}

				GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

				GL11.glTranslatef(0, -1, 0);
			}
		}
	}

	@ForgeSubscribe
	public void onLandingPadRemoved(GCCoreLandingPadRemovalEvent event)
	{
		TileEntity tile = event.world.getBlockTileEntity(event.x, event.y, event.z);

		if (tile instanceof IFuelDock)
		{
			IFuelDock dock = (IFuelDock) tile;

			GCMarsTileEntityLaunchController launchController = null;

			for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
			{
				if (connectedTile instanceof GCMarsTileEntityLaunchController)
				{
					launchController = (GCMarsTileEntityLaunchController) event.world.getBlockTileEntity(((GCMarsTileEntityLaunchController) connectedTile).xCoord, ((GCMarsTileEntityLaunchController) connectedTile).yCoord, ((GCMarsTileEntityLaunchController) connectedTile).zCoord);
					break;
				}
			}

			if (launchController != null)
			{
				if (!launchController.getDisabled(0) && launchController.getEnergyStored() > 0.0F)
				{
					event.allow = !launchController.launchPadRemovalDisabled;
				}
			}
		}
	}
}
