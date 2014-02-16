package micdoodle8.mods.galacticraft.core.world.gen;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;
import cpw.mods.fml.common.FMLLog;

/**
 * GCMoonMapGenVillage.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class MapGenMoonVillage extends MapGenStructure
{
	public static List<BiomeGenBase> villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] { BiomeGenBaseMoon.moonFlat });
	private final int terrainType;
	private static boolean initialized;

	static
	{
		try
		{
			MapGenMoonVillage.initiateStructures();
		}
		catch (Throwable e)
		{
			;
		}
	}

	public static void initiateStructures() throws Throwable
	{
		if (!MapGenMoonVillage.initialized)
		{
			MapGenStructureIO.registerStructure(StructureStartMoonVillage.class, "MoonVillage");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageField.class, "MoonField1");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageField2.class, "MoonField2");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageHouse.class, "MoonHouse");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageRoadPiece.class, "MoonRoadPiece");
			MapGenStructureIO.func_143031_a(GCMoonComponentVillagePathGen.class, "MoonPath");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageTorch.class, "MoonTorch");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageStartPiece.class, "MoonWell");
			MapGenStructureIO.func_143031_a(ComponentMoonVillageWoodHut.class, "MoonWoodHut");
		}

		MapGenMoonVillage.initialized = true;
	}

	public MapGenMoonVillage()
	{
		this.terrainType = 0;
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int i, int j)
	{
		final byte numChunks = 32;
		final byte offsetChunks = 8;
		final int oldi = i;
		final int oldj = j;

		if (i < 0)
		{
			i -= numChunks - 1;
		}

		if (j < 0)
		{
			j -= numChunks - 1;
		}

		int randX = i / numChunks;
		int randZ = j / numChunks;
		final Random var7 = this.worldObj.setRandomSeed(i, j, 10387312);
		randX *= numChunks;
		randZ *= numChunks;
		randX += var7.nextInt(numChunks - offsetChunks);
		randZ += var7.nextInt(numChunks - offsetChunks);

		if (oldi == randX && oldj == randZ)
		{
			return true;
		}

		return false;
	}

	@Override
	protected StructureStart getStructureStart(int par1, int par2)
	{
		FMLLog.info("Generating Moon Village at x" + par1 * 16 + " z" + par2 * 16);
		return new StructureStartMoonVillage(this.worldObj, this.rand, par1, par2, this.terrainType);
	}

	@Override
	public String func_143025_a()
	{
		return "MoonVillage";
	}
}
