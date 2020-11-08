package micdoodle8.mods.galacticraft.core.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.world.gen.dungeon.RoomTreasure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DimensionOverworldOrbit extends DimensionSpaceStation implements IOrbitDimension, IZeroGDimension, ISolarLevel, IExitHeight
{
    Set<Entity> freefallingEntities = new HashSet<>();

    public DimensionOverworldOrbit(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn, 0.0F);
    }

//    @Override
//    public DimensionType getDimensionType()
//    {
//        return GCDimensions.ORBIT;
//    }

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
        return 24000L;
    }

    @Override
    public boolean isDaytime()
    {
        final float a = this.world.getCelestialAngle(0F);
        //TODO: adjust this according to size of planet below
        return a < 0.42F || a > 0.58F;
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
        return 64;
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

//	@Override
//	public String getWelcomeMessage()
//	{
//		return "Entering Earth Orbit";
//	}
//
//	@Override
//	public String getDepartMessage()
//	{
//		return "Leaving Earth Orbit";
//	}

    @Override
    public CelestialBody getCelestialBody()
    {
        return GalacticraftCore.satelliteSpaceStation;
    }

    @Override
    public float getGravity()
    {
        return 0.075F;
    }

    @Override
    public double getMeteorFrequency()
    {
        return 0;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.5D;
    }

//    @Override
//    @Deprecated
//    public String getPlanetToOrbit()
//    {
//        return "overworld";
//    }

    @Override
    public DimensionType getPlanetIdToOrbit()
    {
        return DimensionType.OVERWORLD;
    }

    @Override
    public int getYCoordToTeleportToPlanet()
    {
        return 30;
    }

//    @Override
//    public String getSaveFolder()
//    {
//        return "DIM_SPACESTATION" + this.getDimension();
//    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        return ConfigManagerCore.spaceStationEnergyScalar.get();
    }

    @Override
    public double getYCoordinateToTeleport()
    {
        return 750;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier > 0;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.4F;
    }

    @Override
    public boolean inFreefall(Entity entity)
    {
        return freefallingEntities.contains(entity);
    }

    @Override
    public void setInFreefall(Entity entity)
    {
        freefallingEntities.add(entity);
    }

    @Override
    public void updateWeather(Runnable defaultLogic)
    {
        freefallingEntities.clear();
        super.updateWeather(defaultLogic);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void setSpinDeltaPerTick(float angle)
//    {
//        SkyProviderOrbit skyProvider = ((SkyProviderOrbit) this.getSkyRenderer());
//        if (skyProvider != null)
//        {
//            skyProvider.spinDeltaPerTick = angle;
//        }
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public float getSkyRotation()
//    {
//        SkyProviderOrbit skyProvider = ((SkyProviderOrbit) this.getSkyRenderer());
//        return skyProvider.spinAngle;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void createSkyProvider()
//    {
//        this.setSkyRenderer(new SkyProviderOrbit(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png"), true, true));
////        this.setSpinDeltaPerTick(this.getSpinManager().getSpinRate()); TODO Spin
//
//        if (this.getCloudRenderer() == null)
//        {
//            this.setCloudRenderer(new CloudRenderer());
//        }
//    } TODO Sky providers


    @Override
    public void setSpinDeltaPerTick(float angle)
    {

    }

    @Override
    public float getSkyRotation()
    {
        return 0;
    }

    @Override
    public void createSkyProvider()
    {

    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        return new Vec3d(0.0, 0.0, 0.0);
    }

    @Override
    public int getDungeonSpacing()
    {
        return 0;
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
