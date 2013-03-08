package micdoodle8.mods.galacticraft.moon.wgen;

import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class GCMoonMapGenPuzzle extends MapGenStructure
{
    @Override
	protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        final int var3 = par1;
        final int var4 = par2;

//        if (par1 < 0)
//        {
//            par1 -= this.field_82665_g - 1;
//        }
//
//        if (par2 < 0)
//        {
//            par2 -= this.field_82665_g - 1;
//        }
//
//        int var5 = par1 / this.field_82665_g;
//        int var6 = par2 / this.field_82665_g;
//        final Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387312);
//        var5 *= this.field_82665_g;
//        var6 *= this.field_82665_g;
//        var5 += var7.nextInt(this.field_82665_g - this.field_82666_h);
//        var6 += var7.nextInt(this.field_82665_g - this.field_82666_h);

        if (var3 % 2 == 0 && var4 % 2 == 0)
        {
            return true;
        }

        return false;
    }

    @Override
	protected StructureStart getStructureStart(int par1, int par2)
    {
        return new GCMoonStructurePuzzleStart(this.worldObj, this.rand, par1, par2, 0);
    }
}
