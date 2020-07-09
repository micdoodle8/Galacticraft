package micdoodle8.mods.galacticraft.planets.asteroids.dimension;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3D;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockAsteroidRock;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class TeleportTypeAsteroids implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3D getPlayerSpawnLocation(ServerWorld world, ServerPlayerEntity player)
    {
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            int x = MathHelper.floor(stats.getCoordsTeleportedFromX());
            int z = MathHelper.floor(stats.getCoordsTeleportedFromZ());
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
                if (world.getDimension() instanceof DimensionAsteroids)
                {
                    bv3 = ((DimensionAsteroids) world.getDimension()).getClosestAsteroidXZ(x, 0, z, true);
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
                    this.loadChunksAround(bv3.x, bv3.z, 2, world.getChunkProvider());
                    this.loadChunksAround(bv3.x, bv3.z, -3, world.getChunkProvider());

                    if (goodAsteroidEntry(world, bv3.x, bv3.y, bv3.z))
                    {
                        return new Vector3D(bv3.x, 310, bv3.z);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3D(bv3.x + 2, 310, bv3.z + 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3D(bv3.x + 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3D(bv3.x - 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3D(bv3.x - 2, 310, bv3.z + 2);
                    }

                    //Failed to find an asteroid even though there should be one there
                    if (ConfigManagerCore.enableDebug)
                    {
                        GCLog.info("Removing drilled out asteroid at x" + (bv3.x) + " z" + (bv3.z));
                    }
                    ((DimensionAsteroids) world.getDimension()).removeAsteroid(bv3.x, bv3.y, bv3.z);
                }

                attemptCount++;
            }
            while (attemptCount < 5);

            GCLog.info("Failed to find good large asteroid landing spot! Falling back to making a small one.");
            this.makeSmallLandingSpot(world, x, z);
            return new Vector3D(x, 310, z);
        }

        GCLog.severe("Null player when teleporting to Asteroids!");
        return new Vector3D(0, 310, 0);
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
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x, y, z), false);
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x - 1, y, z), false);
                    }
                    if (world.getBlockState(new BlockPos(x, y, z - 1)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x, y, z - 1), false);
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z - 1)).getBlock() instanceof BlockAsteroidRock)
                    {
                        world.removeBlock(new BlockPos(x - 1, y, z - 1), false);
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
        this.loadChunksAround(x, z, -1, world.getChunkProvider());

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

    private void loadChunksAround(int x, int z, int i, AbstractChunkProvider cp)
    {
        cp.getChunk(x >> 4, z >> 4, true);
        if ((x + i) >> 4 != x >> 4)
        {
            cp.getChunk((x + i) >> 4, z >> 4, true);
            if ((z + i) >> 4 != z >> 4)
            {
                cp.getChunk(x >> 4, (z + i) >> 4, true);
                cp.getChunk((x + i) >> 4, (z + i) >> 4, true);
            }
        }
        else if ((z + i) >> 4 != z >> 4)
        {
            cp.getChunk(x >> 4, (z + i) >> 4, true);
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
        if (world.isAirBlock(new BlockPos(x, y, z)))
        {
            Block block = world.rand.nextInt(3) == 0 ? AsteroidBlocks.rock0 : AsteroidBlocks.rock1;
            world.setBlockState(new BlockPos(x, y, z), block.getDefaultState(), 2);
        }
    }

    @Override
    public Vector3D getEntitySpawnLocation(ServerWorld world, Entity entity)
    {
        return new Vector3D(entity.posX, ConfigManagerCore.disableLander ? 250.0 : 900.0, entity.posZ);
    }

    @Override
    public Vector3D getParaChestSpawnLocation(ServerWorld world, ServerPlayerEntity player, Random rand)
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
        w.getChunk(chunkX, chunkZ);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, ServerPlayerEntity player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.abilities.isFlying)
                {
                    player.abilities.isFlying = false;
                }

                if (!newWorld.isRemote)
                {
                    EntityEntryPod entryPod = EntityEntryPod.createEntityEntryPod(player);

                    boolean previous = CompatibilityManager.forceLoadChunks((ServerWorld) newWorld);
                    entryPod.forceSpawn = true;
                    newWorld.addEntity(entryPod);
                    CompatibilityManager.forceLoadChunksEnd((ServerWorld) newWorld, previous);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(ServerPlayerEntity player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        SchematicRegistry.unlockNewPage(player, new ItemStack(GCItems.schematicRocketT2, 1)); //Knows how to build T2 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematicRocketT3, 1)); //Knows how to build T3 rocket
        SchematicRegistry.unlockNewPage(player, new ItemStack(MarsItems.schematicAstroMiner, 1)); //Knows how to build Astro Miner
        NonNullList<ItemStack> rocketStacks = NonNullList.create();
        stats.setFuelLevel(1000);
        rocketStacks.add(new ItemStack(GCItems.oxMask));
        rocketStacks.add(new ItemStack(GCItems.oxygenGear));
        rocketStacks.add(new ItemStack(GCItems.oxTankMedium));
        rocketStacks.add(new ItemStack(GCItems.oxTankHeavy));
        rocketStacks.add(new ItemStack(GCItems.oxTankHeavy));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.canisterLOX));
        rocketStacks.add(new ItemStack(AsteroidsItems.thermalCloth, 32));
        rocketStacks.add(new ItemStack(Blocks.GLASS_PANE, 16));
        rocketStacks.add(new ItemStack(Blocks.OAK_PLANKS, 32));
        rocketStacks.add(new ItemStack(MarsItems.ingotDesh, 16)); //Desh ingot
        rocketStacks.add(new ItemStack(GCItems.compressedWaferBasic, 8)); //Basic Wafer
        rocketStacks.add(new ItemStack(GCItems.solarModule1, 2)); //Solar Panels
        rocketStacks.add(new ItemStack(GCItems.dehydratedApple, 16));  //Canned food
        rocketStacks.add(new ItemStack(Items.EGG, 12));

        SpawnEggItem egg = SpawnEggItem.getEgg(EntityType.COW);
        ItemStack spawnEgg = new ItemStack(egg, 2);
        rocketStacks.add(spawnEgg);
        rocketStacks.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION, 4), Potions.LONG_NIGHT_VISION)); //Night Vision Potion
        rocketStacks.add(new ItemStack(MarsBlocks.cryoChamber, 1)); //Cryogenic Chamber
        rocketStacks.add(new ItemStack(MarsItems.rocketTierTwoCargo2, 1));
        stats.setRocketStacks(rocketStacks);
    }
}
