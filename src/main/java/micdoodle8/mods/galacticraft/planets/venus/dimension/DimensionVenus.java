package micdoodle8.mods.galacticraft.planets.venus.dimension;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.DimensionSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.api.world.IWeatherProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.ParticleAcidVapor;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusChunkGenerator;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.RoomTreasureVenus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.IParticleData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DimensionVenus extends DimensionSpace implements IGalacticraftDimension, ISolarLevel, IWeatherProvider
{
    private double solarMultiplier = -0.36D;
    private float prevRainingStrength;
    private float rainingStrength;
    private boolean raining = false;
    private int rainTime = 100;
    private int rainChange = 100;
    private float targetRain = 0.0F;

    public DimensionVenus(World worldIn, DimensionType typeIn)
    {
        super(worldIn, typeIn, 0.0F);
    }

    @Override
    public Vec3d getFogColor(float celestialAngle, float partialTicks)
    {
        float night = this.getStarBrightness(1.0F);
        float day = 1.0F - this.getStarBrightness(1.0F);
        float dayColR = 203.0F / 255.0F;
        float dayColG = 147.0F / 255.0F;
        float dayColB = 0.0F / 255.0F;
        float nightColR = 131.0F / 255.0F;
        float nightColG = 108.0F / 255.0F;
        float nightColB = 46.0F / 255.0F;
        return new Vec3d(dayColR * day + nightColR * night,
                dayColG * day + nightColG * night,
                dayColB * day + nightColB * night);
    }

//    @Override
//    public Vector3 getSkyColor()
//    {
//        float night = this.getStarBrightness(1.0F);
//        float day = 1.0F - this.getStarBrightness(1.0F);
//        float dayColR = 255.0F / 255.0F;
//        float dayColG = 207.0F / 255.0F;
//        float dayColB = 81.0F / 255.0F;
//        float nightColR = 118.0F / 255.0F;
//        float nightColG = 89.0F / 255.0F;
//        float nightColB = 21.0F / 255.0F;
//        return new Vector3(dayColR * day + nightColR * night,
//                dayColG * day + nightColG * night,
//                dayColB * day + nightColB * night);
//    }

    @Override
    public IParticleData getParticle(ClientWorld world, double x, double y, double z)
    {
//        return new ParticleAcidVapor(world, x, y, z, 0.0D, 0.0D, 0.0D, 0.95F);
        return null; // TODO Acid particles
    }

    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 720000L; // 30 times longer than earth
    }

    @Override
    public ChunkGenerator createChunkGenerator()
    {
        VenusGenSettings settings = new VenusGenSettings();
        BiomeProviderType<VenusBiomeProviderSettings, VenusBiomeProvider> type = VenusBiomeProviderTypes.VENUS_TYPE;
        VenusBiomeProviderSettings providerSettings = type.createSettings(world.getWorldInfo()).setGeneratorSettings(settings);
        return new VenusChunkGenerator(this.world, type.create(providerSettings), settings);
    }

//    @Override
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
        return 0.0375F;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.9D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier >= 3;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.38F;
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return VenusModule.planetVenus;
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
    public boolean shouldDisablePrecipitation()
    {
        return false;
    }

//    @Override
//    public DimensionType getDimensionType()
//    {
//        return GCPlanetDimensions.VENUS;
//    }

    @Override
    public int getDungeonSpacing()
    {
        return 704;
    }

    @Override
    public float getArrowGravity()
    {
        return 0.0275F;
    }

    @Override
    public ResourceLocation getDungeonChestType()
    {
        return RoomTreasureVenus.VENUSCHEST;
    }

    @Override
    public boolean canDoRainSnowIce(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Particle getParticle(ClientWorld world, double x, double y, double z)
//    {
//        return new ParticleAcidVapor(world, x, y, z, 0.0D, 0.0D, 0.0D, 0.95F);
//    }


    @Override
    protected void updateWeatherOverride(Runnable defaultLogic)
    {
        if (!this.world.isRemote)
        {
            if (--this.rainTime <= 0)
            {
                this.raining = !this.raining;
                if (this.raining)
                {
                    this.rainTime = (this.world.rand.nextInt(3600) + 1000);
                }
                else
                {
                    this.rainTime = (this.world.rand.nextInt(2000) + 1000);
                }
            }

            if (--this.rainChange <= 0)
            {
                this.targetRain = 0.15F + this.world.rand.nextFloat() * 0.45F;
                this.rainChange = (this.world.rand.nextInt(200) + 100);
            }

            float strength = this.world.rainingStrength;
            this.world.prevRainingStrength = strength;
            if (this.raining && strength < this.targetRain)
            {
                strength += 0.004F;
            }
            else if (!this.raining || strength > this.targetRain)
            {
                strength -= 0.004F;
            }
            this.world.rainingStrength = MathHelper.clamp(strength, 0.0F, 0.6F);
        }
    }

    @Override
    public void weatherSounds(int j, Minecraft mc, World world, BlockPos blockpos, double xx, double yy, double zz, Random random)
    {
        if ((int) yy >= blockpos.getY() + 1 && world.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos).getY() > blockpos.getY())
        {
            mc.world.playSound(xx, yy, zz, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.WEATHER, 0.025F, 0.6F + random.nextFloat() * 0.2F, false);
        }
        else
        {
            mc.world.playSound(xx, yy, zz, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.WEATHER, 0.04F, 0.8F + random.nextFloat() * 0.06F + random.nextFloat() * 0.06F, false);
        }
    }

    @Override
    public int getSoundInterval(float rainStrength)
    {
        int result = 80 - (int) (rainStrength * 88F);
        return result > 0 ? result : 0;
    }
}
