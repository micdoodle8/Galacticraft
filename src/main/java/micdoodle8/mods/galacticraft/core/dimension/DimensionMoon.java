package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonChunkGenerator;
import micdoodle8.mods.galacticraft.core.dimension.chunk.MoonGenSettings;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.core.world.gen.BiomeMoonHills;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import micdoodle8.mods.galacticraft.planets.venus.dimension.VenusBiomeProvider;
import micdoodle8.mods.galacticraft.planets.venus.dimension.VenusBiomeProviderSettings;
import micdoodle8.mods.galacticraft.planets.venus.dimension.VenusBiomeProviderTypes;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusChunkGenerator;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;

public class DimensionMoon extends DimensionSpace implements IGalacticraftDimension, ISolarLevel
{
    public DimensionMoon(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn, 0.0F);
    }

//    @Override
//    public DimensionType getDimensionType()
//    {
//        return GCDimensions.MOON;
//    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        return new Vec3d(0, 0, 0);
    }


//    @Override
//    public Vector3 getFogColor()
//    {
//        return new Vector3(0, 0, 0);
//    }
//
//    @Override
//    public Vector3 getSkyColor()
//    {
//        return new Vector3(0, 0, 0);
//    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 192000L;
    }

    @Override
    public ChunkGenerator createChunkGenerator()
    {
        MoonGenSettings settings = new MoonGenSettings();
        BiomeProviderType<MoonBiomeProviderSettings, MoonBiomeProvider> type = MoonBiomeProviderTypes.MOON_TYPE;
        MoonBiomeProviderSettings providerSettings = type.createSettings(world.getWorldInfo()).setGeneratorSettings(settings);
        return new MoonChunkGenerator(this.world, type.create(providerSettings), settings);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getStarBrightness(float par1)
//    {
//        final float var2 = this.world.getCelestialAngle(par1);
//        float var3 = 1.0F - (MathHelper.cos(var2 * Constants.twoPI) * 2.0F + 0.25F);
//
//        if (var3 < 0.0F)
//        {
//            var3 = 0.0F;
//        }
//
//        if (var3 > 1.0F)
//        {
//            var3 = 1.0F;
//        }
//
//        return var3 * var3 * 0.5F + 0.3F;
//    }

    @Override
    public boolean isSkyColored()
    {
        return false;
    }

//    @Override
//    public double getHorizon()
//    {
//        return 44.0D;
//    }

    @Override
    public int getSeaLevel()
    {
        return 68;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
    {
        for (int i = chunkPosIn.getXStart(); i <= chunkPosIn.getXEnd(); ++i)
        {
            for (int j = chunkPosIn.getZStart(); j <= chunkPosIn.getZEnd(); ++j)
            {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null)
                {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
    {
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(posX, 0, posZ);
        Biome biome = this.world.getBiome(blockpos$mutableblockpos);
        BlockState blockstate = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !blockstate.getBlock().isIn(BlockTags.VALID_SPAWN))
        {
            return null;
        }
        else
        {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (i < 0)
            {
                return null;
            }
            else if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, posX & 15, posZ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, posX & 15, posZ & 15))
            {
                return null;
            }
            else
            {
                for (int j = i + 1; j >= 0; --j)
                {
                    blockpos$mutableblockpos.setPos(posX, j, posZ);
                    BlockState blockstate1 = this.world.getBlockState(blockpos$mutableblockpos);
                    if (!blockstate1.getFluidState().isEmpty())
                    {
                        break;
                    }

                    if (blockstate1.equals(blockstate))
                    {
                        return blockpos$mutableblockpos.up().toImmutable();
                    }
                }

                return null;
            }
        }
    }

    //Overriding  so that beds do not explode on Moon
    @Override
    public boolean canRespawnHere()
    {
        if (EventHandlerGC.bedActivated)
        {
            EventHandlerGC.bedActivated = false;
            return true;
        }
        return false;
    }

    @Override
    public float getGravity()
    {
        return 0.062F;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.7D;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        return 1.4D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier > 0;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.18F;
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return GalacticraftCore.moonMoon;
    }

    @Override
    public int getDungeonSpacing()
    {
        return 704;
    }

    @Override
    public ResourceLocation getDungeonChestType()
    {
        return RoomTreasure.MOONCHEST;
    }

    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }
}
