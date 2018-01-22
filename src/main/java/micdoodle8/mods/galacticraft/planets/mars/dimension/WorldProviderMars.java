package micdoodle8.mods.galacticraft.planets.mars.dimension;

import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.event.EventHandlerGC;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.RoomTreasureMars;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.WorldChunkManagerMars;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderMars extends WorldProviderSpace implements IGalacticraftWorldProvider, ISolarLevel
{
    private double solarMultiplier = -1D;

    @Override
    public Vector3 getFogColor()
    {
        float f = 1.0F - this.getStarBrightness(1.0F);
        return new Vector3(210F / 255F * f, 120F / 255F * f, 59F / 255F * f);
    }

    @Override
    public Vector3 getSkyColor()
    {
        float f = 1.0F - this.getStarBrightness(1.0F);
        return new Vector3(154 / 255.0F * f, 114 / 255.0F * f, 66 / 255.0F * f);
    }

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

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderMars.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return WorldChunkManagerMars.class;
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
    public String getInternalNameSuffix()
    {
        return "_mars";
    }

    @Override
    public int getDungeonSpacing()
    {
        return 704;
    }

    @Override
    public float getArrowGravity()
    {
        return 0.015F;
    }

    @Override
    public String getDungeonChestType()
    {
        return RoomTreasureMars.MARSCHEST;
    }

    @Override
    public List<Block> getSurfaceBlocks()
    {
        List<Block> list = new LinkedList<>();
        list.add(MarsBlocks.marsBlock);
        return list;
    }
}
