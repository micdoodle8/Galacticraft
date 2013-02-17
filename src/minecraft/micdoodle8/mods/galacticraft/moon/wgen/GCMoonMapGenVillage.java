package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class GCMoonMapGenVillage extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List villageSpawnBiomes = Arrays.asList(new BiomeGenBase[] {GCMoonBiomeGenBase.moonFlat});

    /** World terrain type, 0 for normal, 1 for flat map */
    private int terrainType;
    private int field_82665_g;
    private final int field_82666_h;

    public GCMoonMapGenVillage()
    {
        this.terrainType = 0;
        this.field_82665_g = 32;
        this.field_82666_h = 8;
    }

    public GCMoonMapGenVillage(Map par1Map)
    {
        this();
        final Iterator var2 = par1Map.entrySet().iterator();

        while (var2.hasNext())
        {
            final Entry var3 = (Entry)var2.next();

            if (((String)var3.getKey()).equals("size"))
            {
                this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.terrainType, 0);
            }
            else if (((String)var3.getKey()).equals("distance"))
            {
                this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.field_82665_g, this.field_82666_h + 1);
            }
        }
    }

    @Override
	protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        final int var3 = par1;
        final int var4 = par2;

        if (par1 < 0)
        {
            par1 -= this.field_82665_g - 1;
        }

        if (par2 < 0)
        {
            par2 -= this.field_82665_g - 1;
        }

        int var5 = par1 / this.field_82665_g;
        int var6 = par2 / this.field_82665_g;
        final Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387312);
        var5 *= this.field_82665_g;
        var6 *= this.field_82665_g;
        var5 += var7.nextInt(this.field_82665_g - this.field_82666_h);
        var6 += var7.nextInt(this.field_82665_g - this.field_82666_h);

        if (var3 == var5 && var4 == var6)
        {
            return true;
        }

        return false;
    }

    @Override
	protected StructureStart getStructureStart(int par1, int par2)
    {
        return new GCMoonStructureVillageStart(this.worldObj, this.rand, par1, par2, this.terrainType);
    }
}
