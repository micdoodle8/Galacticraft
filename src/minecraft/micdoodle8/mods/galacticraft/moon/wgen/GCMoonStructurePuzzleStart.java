package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

class GCMoonStructurePuzzleStart extends StructureStart
{
    public GCMoonStructurePuzzleStart(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        final GCMoonComponentPuzzle var7 = new GCMoonComponentPuzzle(par1World, par2Random, 0, (par3 << 4) + 2, 55, (par4 << 4) + 2);
        this.components.add(var7);
        var7.buildComponent(var7, this.components, par2Random);

        this.updateBoundingBox();
    }
}
