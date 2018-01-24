package micdoodle8.mods.galacticraft.api.prefab.world.gen;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class BiomeDecoratorSpace
{
    protected Random rand;

    protected int posX;
    protected int posZ;

    /**
     * Note: the passed X,Z co-ordinates are now block co-ordinates, not chunk co-ordinates
     * @param world
     * @param random
     * @param posX
     * @param posZ
     */
    public void decorate(World world, Random random, int posX, int posZ)
    {
        if (this.getCurrentWorld() != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            this.setCurrentWorld(world);
            this.rand = random;
            this.posX = posX;
            this.posZ = posZ;
            BlockPos pos = new BlockPos(this.posX, 0, this.posZ);
            MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Pre(world, this.rand, pos));
            this.decorate();
            MinecraftForge.EVENT_BUS.post(new GCCoreEventPopulate.Post(world, this.rand, pos));
            this.setCurrentWorld(null);
            this.rand = null;
        }
    }

    protected abstract void setCurrentWorld(World world);

	protected abstract World getCurrentWorld();

	protected void generateOre(int amountPerChunk, WorldGenerator worldGenerator, int minY, int maxY)
    {
        World currentWorld = this.getCurrentWorld();
		for (int var5 = 0; var5 < amountPerChunk; ++var5)
        {
            final int var6 = this.posX + this.rand.nextInt(16);
            final int var7 = this.rand.nextInt(maxY - minY) + minY;
            final int var8 = this.posZ + this.rand.nextInt(16);
            worldGenerator.generate(currentWorld, this.rand, new BlockPos(var6, var7, var8));
        }
    }

    protected abstract void decorate();
}
