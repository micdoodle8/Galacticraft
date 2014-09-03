package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;

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
        if (player != null)
        {
            GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(player);
            int x = MathHelper.floor_double(stats.coordsTeleportedFromX);
            int z = MathHelper.floor_double(stats.coordsTeleportedFromZ);

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
                    //Check whether the returned asteroid is too far from the desired entry location in which case, give up
                    if (bv3.distanceSquared(new BlockVec3(x, 128, z)) > 25600)
                    {
                        break;
                    }

                    if (ConfigManagerCore.enableDebug)
                    {
                        FMLLog.info("Testing asteroid at x" + (bv3.x) + " y" + (bv3.y) + " z" + bv3.z);
                    }
                    this.loadChunksAround(bv3.x, bv3.z, 2, world.theChunkProviderServer);
                    this.loadChunksAround(bv3.x, bv3.z, -3, world.theChunkProviderServer);

                    if (goodAsteroidEntry(world, bv3.x, bv3.y, bv3.z))
                    {
                        return new Vector3(bv3.x, 310, bv3.z);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3(bv3.x + 2, 310, bv3.z + 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3(bv3.x + 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3(bv3.x - 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3(bv3.x - 2, 310, bv3.z + 2);
                    }

                    //Failed to find an asteroid even though there should be one there
                    if (ConfigManagerCore.enableDebug)
                    {
                        FMLLog.info("Removing drilled out asteroid at x" + (bv3.x) + " z" + (bv3.z));
                    }
                    ((WorldProviderAsteroids) world.provider).removeAsteroid(bv3.x, bv3.y, bv3.z);
                }

                attemptCount++;
            }
            while (attemptCount < 5);

            FMLLog.info("Failed to find good large asteroid landing spot! Falling back to making a small one.");
            this.makeSmallLandingSpot(world, x, z);
            return new Vector3(x, 310, z);
        }

        FMLLog.severe("Null player when teleporting to Asteroids!");
        return new Vector3(0, 310, 0);
    }

    private boolean goodAsteroidEntry(World world, int x, int yorig, int z)
    {
        for (int k = 208; k > 48; k--)
        {
            if (!world.isAirBlock(x, k, z))
            {
                if (Math.abs(k - yorig) > 20)
                {
                    continue;
                }
                //Clear the downward path of small asteroids and any other asteroid rock
                for (int y = k + 2; y < 256; y++)
                {
                    if (world.getBlock(x, y, z) == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(x, y, z);
                    }
                    if (world.getBlock(x - 1, y, z) == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(x - 1, y, z);
                    }
                    if (world.getBlock(x, y, z - 1) == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(x, y, z - 1);
                    }
                    if (world.getBlock(x - 1, y, z - 1) == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(x - 1, y, z - 1);
                    }
                }
                if (ConfigManagerCore.enableDebug)
                {
                    FMLLog.info("Found asteroid at x" + (x) + " z" + (z));
                }
                return true;
            }
        }
        return false;
    }

    private void makeSmallLandingSpot(World world, int x, int z)
    {
        this.loadChunksAround(x, z, -1, (ChunkProviderServer) world.getChunkProvider());

        for (int k = 255; k > 48; k--)
        {
            if (!world.isAirBlock(x, k, z))
            {
                this.makePlatform(world, x, k - 1, z);
                return;
            }
            if (!world.isAirBlock(x - 1, k, z))
            {
                this.makePlatform(world, x - 1, k - 1, z);
                return;
            }
            if (!world.isAirBlock(x - 1, k, z - 1))
            {
                this.makePlatform(world, x - 1, k - 1, z - 1);
                return;
            }
            if (!world.isAirBlock(x, k, z - 1))
            {
                this.makePlatform(world, x, k - 1, z - 1);
                return;
            }
        }

        this.makePlatform(world, x, 48 + world.rand.nextInt(128), z);
        return;
    }

    private void loadChunksAround(int x, int z, int i, ChunkProviderServer cp)
    {
        cp.loadChunk(x >> 4, z >> 4);
        if ((x + i) >> 4 != x >> 4)
        {
            cp.loadChunk((x + i) >> 4, z >> 4);
            if ((z + i) >> 4 != z >> 4)
            {
                cp.loadChunk(x >> 4, (z + i) >> 4);
                cp.loadChunk((x + i) >> 4, (z + i) >> 4);
            }
        }
        else if ((z + i) >> 4 != z >> 4)
        {
            cp.loadChunk(x >> 4, (z + i) >> 4);
        }
    }

    private void makePlatform(World world, int x, int y, int z)
    {
        for (int xx = -3; xx < 3; xx++)
        {
            for (int zz = -3; zz < 3; zz++)
            {
                if (xx == -3 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                if (xx == 2 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                doBlock(world, x + xx, y, z + zz);
            }
        }
        for (int xx = -2; xx < 2; xx++)
        {
            for (int zz = -2; zz < 2; zz++)
            {
                doBlock(world, x + xx, y - 1, z + zz);
            }
        }
        doBlock(world, x - 1, y - 2, z - 1);
        doBlock(world, x - 1, y - 2, z);
        doBlock(world, x, y - 2, z);
        doBlock(world, x, y - 2, z - 1);
    }

    private void doBlock(World world, int x, int y, int z)
    {
        int meta = (int) (world.rand.nextFloat() * 1.5F);
        if (world.isAirBlock(x, y, z))
        {
            world.setBlock(x, y, z, AsteroidBlocks.blockBasic, meta, 2);
        }
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
        this.preGenChunk(w, cx, cz);
        for (int r = 1; r < 3; r++)
        {
            int xmin = cx - r;
            int xmax = cx + r;
            int zmin = cz - r;
            int zmax = cz + r;
            for (int i = -r; i < r; i++)
            {
                this.preGenChunk(w, xmin, cz + i);
                this.preGenChunk(w, xmax, cz - i);
                this.preGenChunk(w, cx - i, zmin);
                this.preGenChunk(w, cx + i, zmax);
            }
        }
    }

    private void preGenChunk(World w, int chunkX, int chunkZ)
    {
        w.getChunkFromChunkCoords(chunkX, chunkZ);

        //Search for and add large asteroids nearby
        IChunkProvider cp = ((ChunkProviderServer) w.getChunkProvider()).currentChunkProvider;
        if (cp instanceof ChunkProviderAsteroids)
        {
            ((ChunkProviderAsteroids) cp).addLargeAsteroids(chunkX, chunkZ);
        }
        else
        {
            FMLLog.info("GC bug: wrong chunk provider type, found: " + cp.getClass().getName());
        }
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        GCPlayerStats stats = GCEntityPlayerMP.getPlayerStats(player);
        if (!ridingAutoRocket && player != null && stats.teleportCooldown <= 0)
        {
            if (player.capabilities.isFlying)
            {
                player.capabilities.isFlying = false;
            }

            if (!newWorld.isRemote)
            {
                EntityEntryPod entryPod = new EntityEntryPod(player);

                newWorld.spawnEntityInWorld(entryPod);
            }

            stats.teleportCooldown = 10;
        }
    }
}
