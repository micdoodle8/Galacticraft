package micdoodle8.mods.galacticraft.core;

import java.lang.reflect.Field;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IEntityBreathable;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.event.LivingMoveEvent;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class GCCoreEvents
{
	@ForgeSubscribe
	public void onEntityFall(LivingFallEvent event)
	{
		if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			event.setCanceled(true);
			event.setResult(Result.DENY);
			return;
		}

		event.setCanceled(false);
		event.setResult(Result.ALLOW);
	}
	
	@ForgeSubscribe
	public void growTreeBonemeal(BonemealEvent event)
	{
		if (event.world.getBlockId(event.X, event.Y, event.Z) == GCCoreBlocks.sapling.blockID)
		{
			if (!event.world.isRemote && event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
			{
				((BlockSapling)GCCoreBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
				event.setResult(Result.ALLOW);
				return;
			}
		}

		event.setResult(Result.DENY);
	}

	@ForgeSubscribe
	public void livingMove(LivingMoveEvent event)
	{
        double d0;

		if (!(event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider) || event.entityLiving instanceof EntityPlayer)
		{
        	event.setCanceled(true);
        	return;
        }

        if (event.entityLiving.isInWater() && (!(event.entityLiving instanceof EntityPlayer) || !((EntityPlayer)event.entityLiving).capabilities.isFlying))
        {
            d0 = event.entityLiving.posY;
            event.entityLiving.moveFlying(event.forward, event.strafe, event.entityLiving.isAIEnabled() ? 0.04F : 0.02F);
            event.entityLiving.moveEntity(event.entityLiving.motionX, event.entityLiving.motionY, event.entityLiving.motionZ);
            event.entityLiving.motionX *= 0.800000011920929D;
            event.entityLiving.motionY *= 0.800000011920929D;
            event.entityLiving.motionZ *= 0.800000011920929D;
            event.entityLiving.motionY -= 0.02D;

            if (event.entityLiving.isCollidedHorizontally && event.entityLiving.isOffsetPositionInLiquid(event.entityLiving.motionX, event.entityLiving.motionY + 0.6000000238418579D - event.entityLiving.posY + d0, event.entityLiving.motionZ))
            {
                event.entityLiving.motionY = 0.30000001192092896D;
            }
        }
        else if (event.entityLiving.handleLavaMovement() && (!(event.entityLiving instanceof EntityPlayer) || !((EntityPlayer)event.entityLiving).capabilities.isFlying))
        {
            d0 = event.entityLiving.posY;
            event.entityLiving.moveFlying(event.forward, event.strafe, 0.02F);
            event.entityLiving.moveEntity(event.entityLiving.motionX, event.entityLiving.motionY, event.entityLiving.motionZ);
            event.entityLiving.motionX *= 0.5D;
            event.entityLiving.motionY *= 0.5D;
            event.entityLiving.motionZ *= 0.5D;
            event.entityLiving.motionY -= 0.02D;

            if (event.entityLiving.isCollidedHorizontally && event.entityLiving.isOffsetPositionInLiquid(event.entityLiving.motionX, event.entityLiving.motionY + 0.6000000238418579D - event.entityLiving.posY + d0, event.entityLiving.motionZ))
            {
                event.entityLiving.motionY = 0.30000001192092896D;
            }
        }
        else
        {
            float f2 = 0.91F;

            if (event.entityLiving.onGround)
            {
                f2 = 0.54600006F;
                int i = event.entityLiving.worldObj.getBlockId(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.boundingBox.minY) - 1, MathHelper.floor_double(event.entityLiving.posZ));

                if (i > 0)
                {
                    f2 = Block.blocksList[i].slipperiness * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            float f4;

            if (event.entityLiving.onGround)
            {
                if (event.entityLiving.isAIEnabled())
                {
                    f4 = event.entityLiving.getAIMoveSpeed();
                }
                else
                {
                    f4 = event.entityLiving.landMovementFactor;
                }

                f4 *= f3;
            }
            else
            {
                f4 = event.entityLiving.jumpMovementFactor;
            }

            event.entityLiving.moveFlying(event.forward, event.strafe, f4);
            f2 = 0.91F;

            if (event.entityLiving.onGround)
            {
                f2 = 0.54600006F;
                int j = event.entityLiving.worldObj.getBlockId(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.boundingBox.minY) - 1, MathHelper.floor_double(event.entityLiving.posZ));

                if (j > 0)
                {
                    f2 = Block.blocksList[j].slipperiness * 0.91F;
                }
            }

            if (event.entityLiving.isOnLadder())
            {
                float f5 = 0.15F;

                if (event.entityLiving.motionX < (double)(-f5))
                {
                    event.entityLiving.motionX = (double)(-f5);
                }

                if (event.entityLiving.motionX > (double)f5)
                {
                    event.entityLiving.motionX = (double)f5;
                }

                if (event.entityLiving.motionZ < (double)(-f5))
                {
                    event.entityLiving.motionZ = (double)(-f5);
                }

                if (event.entityLiving.motionZ > (double)f5)
                {
                    event.entityLiving.motionZ = (double)f5;
                }

                event.entityLiving.fallDistance = 0.0F;

                if (event.entityLiving.motionY < -0.15D)
                {
                    event.entityLiving.motionY = -0.15D;
                }

                boolean flag = event.entityLiving.isSneaking() && event.entityLiving instanceof EntityPlayer;

                if (flag && event.entityLiving.motionY < 0.0D)
                {
                    event.entityLiving.motionY = 0.0D;
                }
            }

            event.entityLiving.moveEntity(event.entityLiving.motionX, event.entityLiving.motionY, event.entityLiving.motionZ);

            if (event.entityLiving.isCollidedHorizontally && event.entityLiving.isOnLadder())
            {
                event.entityLiving.motionY = 0.2D;
            }

            if (event.entityLiving.worldObj.isRemote && (!event.entityLiving.worldObj.blockExists((int)event.entityLiving.posX, 0, (int)event.entityLiving.posZ) || !event.entityLiving.worldObj.getChunkFromBlockCoords((int)event.entityLiving.posX, (int)event.entityLiving.posZ).isChunkLoaded))
            {
                if (event.entityLiving.posY > 0.0D)
                {
                    event.entityLiving.motionY = -0.1D;
                }
                else
                {
                    event.entityLiving.motionY = 0.0D;
                }
            }
            else
            {
            	event.entityLiving.motionY += (-0.08D + ((IGalacticraftWorldProvider) event.entityLiving.worldObj.provider).getGravity());
            }

            event.entityLiving.motionY *= 0.9800000190734863D;
            event.entityLiving.motionX *= (double)f2;
            event.entityLiving.motionZ *= (double)f2;
        }

        event.entityLiving.prevLimbYaw = event.entityLiving.limbYaw;
        d0 = event.entityLiving.posX - event.entityLiving.prevPosX;
        double d1 = event.entityLiving.posZ - event.entityLiving.prevPosZ;
        float f6 = MathHelper.sqrt_double(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }

        event.entityLiving.limbYaw += (f6 - event.entityLiving.limbYaw) * 0.4F;
        event.entityLiving.limbSwing += event.entityLiving.limbYaw;
        event.setCanceled(false);
	}

	@ForgeSubscribe
	public void entityLivingEvent(LivingUpdateEvent event)
	{
		if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider && !(event.entityLiving instanceof EntityPlayer))
		{
//            if (event.entityLiving.worldObj.isRemote && (!event.entityLiving.worldObj.blockExists((int)event.entityLiving.posX, 0, (int)event.entityLiving.posZ) || !event.entityLiving.worldObj.getChunkFromBlockCoords((int)event.entityLiving.posX, (int)event.entityLiving.posZ).isChunkLoaded))
//            {
//            	
//            }
//            else
//            {
//            	
//            }
			if ((!(event.entityLiving instanceof IEntityBreathable) || (event.entityLiving instanceof IEntityBreathable && !((IEntityBreathable) event.entityLiving).canBreath())) && event.entityLiving.ticksExisted % 100 == 0)
			{
				event.entityLiving.attackEntityFrom(GalacticraftCore.oxygenSuffocation, 1);
			}
			
//			if (!(event.entityLiving instanceof EntityPlayer) && event.entityLiving.isJumping)
//			{
//				event.entityLiving.motionY += 0.368;
//			}
//			else if (!(event.entityLiving instanceof EntityPlayer) && event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider && event.entityLiving.onGround)
//			{
//			}
		}
	}

//	@ForgeSubscribe
//	public void onWorldLoad(WorldEvent.Load event)
//	{
//		if (event.world instanceof WorldServer)
//		{
//			((WorldServer)event.world).customTeleporters.add(new GCCoreTeleporter((WorldServer)event.world));
//			((WorldServer)event.world).customTeleporters.add(new GCCoreFromOrbitTeleporter((WorldServer)event.world, true));
//			((WorldServer)event.world).customTeleporters.add(new GCCoreToOrbitTeleporter((WorldServer)event.world, true));
//		}
//	}

	@ForgeSubscribe
	public void onBucketFill(FillBucketEvent event)
	{
		final ItemStack result = this.fillCustomBucket(event.world, event.target);

		if (result == null)
		{
			return;
		}

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	public ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		Class buildCraftClass = null;

		int bcOilID1 = -1;
		int bcOilID2 = -1;
		Item bcOilBucket = null;

		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (final Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("oilMoving"))
					{
						final Block block = (Block) f.get(null);

						bcOilID1 = block.blockID;
					}
					else if (f.getName().equals("oilStill"))
					{
						final Block block = (Block) f.get(null);

						bcOilID2 = block.blockID;
					}
					else if (f.getName().equals("bucketOil"))
					{
						final Item item = (Item) f.get(null);

						bcOilBucket = item;
					}
				}
			}
		}
		catch (final Throwable cnfe)
		{

		}

		final int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

		if ((blockID == bcOilID1 || blockID == bcOilID2 || blockID == GCCoreBlocks.crudeOilMoving.blockID || blockID == GCCoreBlocks.crudeOilStill.blockID) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0 && bcOilBucket != null)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);

			return new ItemStack(bcOilBucket);
		}
		else
		{
			return null;
		}
	}

	@ForgeSubscribe
	public void populate(PopulateChunkEvent.Post event)
	{
		final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkX, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);

		if (!doGen || GCCoreConfigManager.disableOilGen)
		{
			return;
		}

		final int worldX = event.chunkX << 4;
		final int worldZ = event.chunkZ << 4;

		this.doPopulate(event.world, event.rand, worldX, worldZ);
	}

	public static void doPopulate(World world, Random rand, int x, int z)
	{
		final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 16, z + 16);

		if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID)
		{
			return;
		}

		final boolean flag1 = rand.nextDouble() <= 0.1D * GCCoreConfigManager.oilGenFactor;
		final boolean flag2 = rand.nextDouble() <= 0.1D * GCCoreConfigManager.oilGenFactor;

		if (flag1 || flag2)
		{
			final int cx = x, cy = 20 + rand.nextInt(10), cz = z;

			final int r = 1 + rand.nextInt(2);

			final int r2 = r * r;

			for (int bx = -r; bx <= r; bx++)
			{
				for (int by = -r; by <= r; by++)
				{
					for (int bz = -r; bz <= r; bz++)
					{
						final int d2 = bx * bx + by * by + bz * bz;

						if (d2 <= r2)
						{
							world.setBlock(bx + cx, by + cy, bz + cz, GCCoreBlocks.crudeOilStill.blockID);
						}
					}
				}
			}
		}
	}
}
