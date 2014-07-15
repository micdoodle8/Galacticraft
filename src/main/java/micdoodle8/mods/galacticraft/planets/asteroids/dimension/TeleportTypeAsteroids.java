package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
            int x = (int)Math.floor(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromX);
            int z = (int)Math.floor(((GCEntityPlayerMP) player).getPlayerStats().coordsTeleportedFromZ);

            if (world.getChunkProvider() instanceof ChunkProviderServer)
            {
                ChunkProviderServer chunkProviderServer = (ChunkProviderServer) world.getChunkProvider();
                if (chunkProviderServer.currentChunkProvider instanceof ChunkProviderAsteroids)
                {
                    ChunkProviderAsteroids chunkProvider = (ChunkProviderAsteroids) chunkProviderServer.currentChunkProvider;
                    BlockVec3 bv3 = chunkProvider.isLargeAsteroidAt(x, z);
                    int numTries = 0;

                    // Try a few more times just in case (shouldn't happen):
                    while (bv3 == null && numTries < 4)
                    {
                        ForgeDirection direction = ForgeDirection.getOrientation(numTries + 2);
                        bv3 = chunkProvider.isLargeAsteroidAt(x + direction.offsetX * 128, z + direction.offsetZ * 128);
                        numTries++;
                    }

                    if (bv3 != null)
                    {
                        world.theChunkProviderServer.loadChunk(bv3.x >> 4, bv3.z >> 4);

                        int k = 208;

                        while (k > 48)
                        {
                            if (!world.isAirBlock(bv3.x, k, bv3.z))
                            {
                                FMLLog.info("Found asteroid at x" + (bv3.x) + " z" + (bv3.z));
                                return new Vector3(bv3.x + 0.5, 310, bv3.z + 0.5);
                            }

                            k--;
                        }
                    }

                    player.addChatComponentMessage(new ChatComponentText("Failed to find valid asteroid landing spot! Please report this as a bug"));
                    return new Vector3(bv3.x, 310, bv3.z);
                }
            }
		}

		return null;
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

            EntityEntryPod entryPod = new EntityEntryPod(newWorld);
            entryPod.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);
            entryPod.waitForPlayer = true;
            entryPod.riddenByEntity = player;
            player.ridingEntity = entryPod;

            entryPod.containedItems = new ItemStack[gcPlayer.getPlayerStats().rocketStacks.length + 1];
            entryPod.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, gcPlayer.getPlayerStats().fuelLevel));

            for (int i = 0; i < gcPlayer.getPlayerStats().rocketStacks.length; i++)
            {
                entryPod.containedItems[i] = gcPlayer.getPlayerStats().rocketStacks[i];
            }

            if (!newWorld.isRemote)
            {
                newWorld.spawnEntityInWorld(entryPod);
            }

            gcPlayer.getPlayerStats().teleportCooldown = 10;
        }
	}
}
