package micdoodle8.mods.galacticraft.planets.venus.dimension;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.api.world.IWeatherProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.EntityAcidVaporFX;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.ChunkProviderVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.WorldChunkManagerVenus;
import micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon.RoomChestVenus;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderVenus extends WorldProviderSpace implements IGalacticraftWorldProvider, ISolarLevel, IWeatherProvider
{
    private double solarMultiplier = 0.36D;
    private float prevRainingStrength;
    private float rainingStrength;
    private boolean raining = false;
    private int rainTime = 100;
    private int rainChange = 100;
    private float targetRain = 0.0F;

    @Override
    public Vector3 getFogColor()
    {
        float night = this.getStarBrightness(1.0F);
        float day = 1.0F - this.getStarBrightness(1.0F);
        float dayColR = 203.0F / 255.0F;
        float dayColG = 147.0F / 255.0F;
        float dayColB = 0.0F / 255.0F;
        float nightColR = 131.0F / 255.0F;
        float nightColG = 108.0F / 255.0F;
        float nightColB = 46.0F / 255.0F;
        return new Vector3(dayColR * day + nightColR * night,
                dayColG * day + nightColG * night,
                dayColB * day + nightColB * night);
    }

    @Override
    public Vector3 getSkyColor()
    {
        float night = this.getStarBrightness(1.0F);
        float day = 1.0F - this.getStarBrightness(1.0F);
        float dayColR = 255.0F / 255.0F;
        float dayColG = 207.0F / 255.0F;
        float dayColB = 81.0F / 255.0F;
        float nightColR = 118.0F / 255.0F;
        float nightColG = 89.0F / 255.0F;
        float nightColB = 21.0F / 255.0F;
        return new Vector3(dayColR * day + nightColR * night,
                dayColG * day + nightColG * night,
                dayColB * day + nightColB * night);
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
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderVenus.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerVenus.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        float f1 = this.worldObj.getCelestialAngle(par1);
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

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 76;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
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
    public String getInternalNameSuffix()
    {
        return "_venus";
    }

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
    public String getDungeonChestType()
    {
        return RoomChestVenus.VENUSCHEST;
    }

    @Override
    public List<Block> getSurfaceBlocks()
    {
        List<Block> list = new LinkedList<>();
        list.add(VenusBlocks.venusBlock);
        return list;
    }
    @Override
    public boolean canDoRainSnowIce(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EntityFX getParticle(WorldClient world, double x, double y, double z)
    {
        return new EntityAcidVaporFX(world, x, y, z, 0.0D, 0.0D, 0.0D, 0.95F);
    }

    @Override
    protected void updateWeatherOverride()
    {
        if (!this.worldObj.isRemote)
        {
            if (--this.rainTime <= 0)
            {
                this.raining = !this.raining;
                if (this.raining)
                {
                    this.rainTime = (this.worldObj.rand.nextInt(3600) + 1000);
                }
                else
                {
                    this.rainTime = (this.worldObj.rand.nextInt(2000) + 1000);
                }
            }

            if (--this.rainChange <= 0)
            {
                this.targetRain = 0.15F + this.worldObj.rand.nextFloat() * 0.45F;
                this.rainChange = (this.worldObj.rand.nextInt(200) + 100);
            }

            float strength = this.worldObj.rainingStrength;
            this.worldObj.prevRainingStrength = strength;
            if (this.raining && strength < this.targetRain)
            {
                strength += 0.004F;
            }
            else if (!this.raining || strength > this.targetRain)
            {
                strength -= 0.004F;
            }
            this.worldObj.rainingStrength = MathHelper.clamp_float(strength, 0.0F, 0.6F);
        }
    }

    @Override
    public void weatherSounds(int j, Minecraft mc, World world, BlockPos blockpos, double xx, double yy, double zz, Random random)
    {
        if ((int)yy >= blockpos.getY() + 1 && world.getPrecipitationHeight(blockpos).getY() > blockpos.getY())
        {
            mc.theWorld.playSound(xx, yy, zz, "random.fizz", 0.03F, 0.6F + random.nextFloat() * 0.2F, false);
        }
        else
        {
            mc.theWorld.playSound(xx, yy, zz, "random.fizz", 0.04F, 0.8F + random.nextFloat() * 0.06F + random.nextFloat() * 0.06F, false);
        }
    }

    @Override
    public int getSoundInterval(float rainStrength)
    {
        return 80 - (int)(rainStrength * 88F);
    }
}
