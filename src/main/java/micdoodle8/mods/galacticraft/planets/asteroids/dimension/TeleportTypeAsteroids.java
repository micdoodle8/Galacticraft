package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

public class TeleportTypeAsteroids implements ITeleportType
{
	@Override
	public boolean useParachute()
	{
		return false;
	}

	@Override
	public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
	{
		if (player instanceof GCEntityPlayerMP)
		{
            int x = MathHelper.floor_double(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromX);
            int z = MathHelper.floor_double(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromZ);
            
            int attemptCount = 0;

        	//Small pre-generate with a chunk loading radius of 3, to make sure some asteroids get generated
        	//(if the world is already generated here, this will be very quick)
        	this.preGenChunks(world, x >> 4, z >> 4);

            do
            {
            	BlockVec3 bv3 = null;
	            if (world.provider instanceof WorldProviderAsteroids)
	            {
	            	bv3 = ((WorldProviderAsteroids) world.provider).getClosestAsteroidXZ(x, 0, z);
	            }
	            
	            if (bv3 != null)
	            {
                    if (ConfigManagerCore.enableDebug) FMLLog.info("Testing asteroid at x" + (bv3.x) + " y" + (bv3.y) + " z" + bv3.z);
	            	world.theChunkProviderServer.loadChunk(bv3.x >> 4, bv3.z >> 4);
	
	                for (int k = 208; k > 48; k--)
	                {
	                    if (!world.isAirBlock(bv3.x, k, bv3.z))
	                    {
	                    	if (ConfigManagerCore.enableDebug) FMLLog.info("Found asteroid at x" + (bv3.x) + " z" + (bv3.z));
	                        return new Vector3(bv3.x + 0.5, 310, bv3.z + 0.5);
	                    }
	                }
	                
	                for (int k = 208; k > 48; k--)
	                {
	                    if (!world.isAirBlock(bv3.x + 1, k, bv3.z + 1))
	                    {
	                    	if (ConfigManagerCore.enableDebug) FMLLog.info("Found asteroid at x" + (bv3.x + 1) + " z" + (bv3.z + 1));
	                        return new Vector3(bv3.x + 1.5, 310, bv3.z + 1.5);
	                    }
	                }
	                
	                for (int k = 208; k > 48; k--)
	                {
	                    if (!world.isAirBlock(bv3.x + 1, k, bv3.z - 1))
	                    {
	                    	if (ConfigManagerCore.enableDebug) FMLLog.info("Found asteroid at x" + (bv3.x + 1) + " z" + (bv3.z - 1));
	                        return new Vector3(bv3.x + 1.5, 310, bv3.z - 0.5);
	                    }
	                }
	                
	                for (int k = 208; k > 48; k--)
	                {
	                    if (!world.isAirBlock(bv3.x - 1, k, bv3.z + 1))
	                    {
	                    	if (ConfigManagerCore.enableDebug) FMLLog.info("Found asteroid at x" + (bv3.x - 1) + " z" + (bv3.z + 1));
	                        return new Vector3(bv3.x - 0.5, 310, bv3.z + 1.5);
	                    }
	                }
	                
	                for (int k = 208; k > 48; k--)
	                {
	                    if (!world.isAirBlock(bv3.x - 1, k, bv3.z - 1))
	                    {
	                    	if (ConfigManagerCore.enableDebug) FMLLog.info("Found asteroid at x" + (bv3.x - 1) + " z" + (bv3.z - 1));
	                        return new Vector3(bv3.x - 0.5, 310, bv3.z - 0.5);
	                    }
	                }
	                
	                //Failed to find an asteroid even though there should be one there
	                if (ConfigManagerCore.enableDebug) FMLLog.info("Removing drilled out asteroid at x" + (bv3.x) + " z" + (bv3.z));
	                ((WorldProviderAsteroids) world.provider).removeAsteroid(bv3.x, bv3.y, bv3.z);
	            }
	            
	            attemptCount++;
            } while (attemptCount < 5);

            player.addChatComponentMessage(new ChatComponentText("Failed to find good asteroid landing spot! Please report this as a bug"));
            return new Vector3(x, 310, z);
		}

        FMLLog.severe("Failed to cast player to GCEntityPlayerMP!");
        return new Vector3(player.posX, 310,  player.posZ);
	}

	@Override
	public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
	{
		return new Vector3(entity.posX, ConfigManagerCore.disableLander ? 250.0 : 900.0, entity.posZ);
	}

	@Override
	public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand)
	{
		return null;
	}

	private void preGenChunks(World w, int cx, int cz)
	{
		w.getChunkFromChunkCoords(cx, cz);
		for (int r = 1; r < 3; r++)
		{
			int xmin = cx - r;
			int xmax = cx + r;
			int zmin = cz - r;
			int zmax = cz + r;
			for (int i = -r; i < r; i++)
			{
				w.getChunkFromChunkCoords(xmin, cz + i);
				w.getChunkFromChunkCoords(xmax, cz - i);
				w.getChunkFromChunkCoords(cx - i, zmin);
				w.getChunkFromChunkCoords(cx + i, zmax);
			}
		}
	}
	
	@Override
	public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
	{
        if (!ridingAutoRocket && player instanceof GCEntityPlayerMP && ((GCEntityPlayerMP) player).getPlayerStats().teleportCooldown <= 0)
        {
            GCEntityPlayerMP gcPlayer = (GCEntityPlayerMP) player;

            if (gcPlayer.capabilities.isFlying)
            {
                gcPlayer.capabilities.isFlying = false;
            }

            if (!newWorld.isRemote)
            {
                EntityEntryPod entryPod = new EntityEntryPod(gcPlayer);

                newWorld.spawnEntityInWorld(entryPod);
            }
            
            gcPlayer.getPlayerStats().teleportCooldown = 10;
        }
	}
}
