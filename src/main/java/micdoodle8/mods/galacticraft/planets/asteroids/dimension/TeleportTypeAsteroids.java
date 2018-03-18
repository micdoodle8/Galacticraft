package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLLog;

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
            GCPlayerStats stats = GCPlayerStats.get(player);
            int x = MathHelper.floor_double(stats.getCoordsTeleportedFromX());
            int z = MathHelper.floor_double(stats.getCoordsTeleportedFromZ());
            int limit = ConfigManagerCore.otherPlanetWorldBorders - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {   
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= - limit / z;
                    z = -limit;
                }
            }

            int attemptCount = 0;

            //Small pre-generate with a chunk loading radius of 3, to make sure some asteroids get generated
            //(if the world is already generated here, this will be very quick)
            this.preGenChunks(world, x >> 4, z >> 4);

            do
            {
                BlockVec3 bv3 = null;
                if (world.provider instanceof WorldProviderAsteroids)
                {
                    bv3 = ((WorldProviderAsteroids) world.provider).getClosestAsteroidXZ(x, 0, z, true);
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
                        GCLog.info("Testing asteroid at x" + (bv3.x) + " y" + (bv3.y) + " z" + bv3.z);
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
                        GCLog.info("Removing drilled out asteroid at x" + (bv3.x) + " z" + (bv3.z));
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
            if (!world.isAirBlock(new BlockPos(x, k, z)))
            {
                if (Math.abs(k - yorig) > 20)
                {
                    continue;
                }
                //Clear the downward path of small asteroids and any other asteroid rock
                for (int y = k + 2; y < 256; y++)
                {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(new BlockPos(x, y, z));
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(new BlockPos(x - 1, y, z));
                    }
                    if (world.getBlockState(new BlockPos(x, y, z - 1)).getBlock() == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(new BlockPos(x, y, z - 1));
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z - 1)).getBlock() == AsteroidBlocks.blockBasic)
                    {
                        world.setBlockToAir(new BlockPos(x - 1, y, z - 1));
                    }
                }
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Found asteroid at x" + (x) + " z" + (z));
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
            if (!world.isAirBlock(new BlockPos(x, k, z)))
            {
                this.makePlatform(world, x, k - 1, z);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x - 1, k, z)))
            {
                this.makePlatform(world, x - 1, k - 1, z);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x - 1, k, z - 1)))
            {
                this.makePlatform(world, x - 1, k - 1, z - 1);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x, k, z - 1)))
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
        if (world.isAirBlock(new BlockPos(x, y, z)))
        {
            world.setBlockState(new BlockPos(x, y, z), AsteroidBlocks.blockBasic.getStateFromMeta(meta), 2);
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
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.capabilities.isFlying)
                {
                    player.capabilities.isFlying = false;
                }

                if (!newWorld.isRemote)
                {
                    EntityEntryPod entryPod = new EntityEntryPod(player);

                    entryPod.forceSpawn = true;
                    newWorld.spawnEntityInWorld(entryPod);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        SchematicRegistry.unlockNewPage(player, new ItemStack(GCItems.schematic, 1, 1)); //Knows how to build T2 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematic, 1, 0)); //Knows how to build T3 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematic, 1, 2)); //Knows how to build Astro Miner
        ItemStack[] rocketStacks = new ItemStack[20];
        stats.setFuelLevel(1000);
        int i = 0;
        rocketStacks[i++] = new ItemStack(GCItems.oxMask);
        rocketStacks[i++] = new ItemStack(GCItems.oxygenGear);
        rocketStacks[i++] = new ItemStack(GCItems.oxTankMedium);
        rocketStacks[i++] = new ItemStack(GCItems.oxTankHeavy);
        rocketStacks[i++] = new ItemStack(GCItems.oxTankHeavy);
        rocketStacks[i++] = new ItemStack(AsteroidsItems.canisterLOX);
        rocketStacks[i++] = new ItemStack(AsteroidsItems.canisterLOX);
        rocketStacks[i++] = new ItemStack(AsteroidsItems.canisterLOX);
        rocketStacks[i++] = new ItemStack(AsteroidsItems.basicItem, 32, 7);
        rocketStacks[i++] = new ItemStack(Blocks.glass_pane, 16);
        rocketStacks[i++] = new ItemStack(Blocks.planks, 32, 2);
        rocketStacks[i++] = new ItemStack(MarsItems.marsItemBasic, 16, 2); //Desh ingot
        rocketStacks[i++] = new ItemStack(GCItems.basicItem, 8, 13); //Basic Wafer
        rocketStacks[i++] = new ItemStack(GCItems.basicItem, 2, 1); //Solar Panels
        rocketStacks[i++] = new ItemStack(GCItems.foodItem, 16, 0);  //Canned food
        rocketStacks[i++] = new ItemStack(Items.egg, 12);

        rocketStacks[i++] = new ItemStack(Items.spawn_egg, 2, EntityList.classToIDMapping.get(EntityCow.class));
        rocketStacks[i++] = new ItemStack(Items.potionitem, 4, 8262); //Night Vision Potion
        rocketStacks[i++] = new ItemStack(MarsBlocks.machine, 1, 4); //Cryogenic Chamber
        rocketStacks[i++] = new ItemStack(MarsItems.rocketMars, 1, IRocketType.EnumRocketType.INVENTORY36.ordinal());
        //rocketStacks[15] = new ItemStack(GCBlocks.brightLamp, 4);
        //rocketStacks[16] = new ItemStack(GCBlocks.aluminumWire, 32);
        stats.setRocketStacks(rocketStacks);
    }
}
