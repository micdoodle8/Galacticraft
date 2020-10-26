package micdoodle8.mods.galacticraft.planets.mars.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.biome.BiomeMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.MarsChunkGenerator;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.RoomTreasureMars;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class DimensionMars extends DimensionSpace implements IGalacticraftDimension, ISolarLevel
{
    private double solarMultiplier = -1D;

    public DimensionMars(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn, 0.0F);
    }

//    @Override
//    public Vector3 getFogColor()
//    {
//        float f = 1.0F - this.getStarBrightness(1.0F);
//        return new Vector3(210F / 255F * f, 120F / 255F * f, 59F / 255F * f);
//    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        float f = 1.0F - this.getStarBrightness(1.0F);
        return new Vec3d(210F / 255F * f, 120F / 255F * f, 59F / 255F * f);
    }


//
//    @Override
//    public Vector3 getSkyColor()
//    {
//        float f = 1.0F - this.getStarBrightness(1.0F);
//        return new Vector3(154 / 255.0F * f, 114 / 255.0F * f, 66 / 255.0F * f);
//    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 24660L;
    }

//    @Override
//    public Class<? extends ChunkGenerator> getChunkProviderClass()
//    {
//        return ChunkProviderMars.class;
//    }

    @OnlyIn(Dist.CLIENT)
    public float getStarBrightness(float par1)
    {
        float f1 = this.world.getCelestialAngle(par1);
        float f2 = 1.0F - (MathHelper.cos(f1 * Constants.twoPI) * 2.0F + 0.25F);

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        return f2 * f2 * 0.75F;
    }

//    @Override
//    public double getHorizon()
//    {
//        return 44.0D;
//    }

    @Override
    public int getSeaLevel()
    {
        return 76;
    }

//    @Override
//    public boolean canCoordinateBeSpawn(int var1, int var2)
//    {
//        return true;
//    }

    //Overriding so that beds do not explode on Mars
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
        return 0.058F;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.9D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier >= 2;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.38F;
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return MarsModule.planetMars;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        if (this.solarMultiplier < 0D)
        {
            double s = this.getSolarSize();
            this.solarMultiplier = s * s * s;
        }
        return this.solarMultiplier;
    }

    @Override
    public int getDungeonSpacing()
    {
        return 704;
    }

//    @Override
//    public DimensionType getDimensionType()
//    {
//        return GCPlanetDimensions.MARS;
//    }


    @Override
    public ChunkGenerator<?> createChunkGenerator()
    {
        MarsGenSettings settings = new MarsGenSettings();
        return new MarsChunkGenerator(this.world, BiomeProviderType.FIXED.create(BiomeProviderType.FIXED.createSettings(this.world.getWorldInfo()).setBiome(BiomeMars.marsFlat)), settings);
    }

    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos chunkPosIn, boolean checkValid)
    {
        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid)
    {
        return null;
    }

    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    @Override
    public float getArrowGravity()
    {
        return 0.015F;
    }

    @Override
    public ResourceLocation getDungeonChestType()
    {
        return RoomTreasureMars.MARSCHEST;
    }
}
