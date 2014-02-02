package micdoodle8.mods.galacticraft.moon.world.gen;

import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMoonBiomeGenBase.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMoonBiomeGenBase extends BiomeGenBase
{
	public static final BiomeGenBase moonFlat = new GCMoonBiomeGenFlat(102).setBiomeName("moon");

	public GCMoonBiomeGenBase(int var1)
	{
		super(var1);
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.rainfall = 0F;
	}

	@Override
	public GCMoonBiomeGenBase setColor(int var1)
	{
		return (GCMoonBiomeGenBase) super.setColor(var1);
	}

	@Override
	public float getSpawningChance()
	{
		return 0.1F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBiomeGrassColor()
	{
		double d0 = MathHelper.clamp_float(this.getFloatTemperature(), 0.0F, 1.0F);
		double d1 = MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
		return this.getModdedBiomeGrassColor(ColorizerGrass.getGrassColor(d0, d1));
	}
}
