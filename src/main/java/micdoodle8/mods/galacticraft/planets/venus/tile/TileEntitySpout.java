package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TileEntitySpout extends TileEntity implements ITickable
{
    private final Random rand = new Random(System.currentTimeMillis());

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            if (rand.nextInt(400) == 0)
            {
                IBlockState stateAbove = this.world.getBlockState(this.getPos().up());
                if (stateAbove.getBlock().isAir(this.world.getBlockState(this.getPos().up()), this.world, this.getPos().up()))
                {
                    double posX = (double)pos.getX() + 0.45 + rand.nextDouble() * 0.1;
                    double posY = (double)pos.getY() + 1.0;
                    double posZ = (double)pos.getZ() + 0.45 + rand.nextDouble() * 0.1;
                    for (int i = 0; i < 4 + rand.nextInt(4); ++i)
                    {
                        GalacticraftPlanets.spawnParticle("acidVapor", new Vector3(posX, posY, posZ), new Vector3(rand.nextDouble() * 0.5 - 0.25, rand.nextDouble() * 0.5 + 0.5, rand.nextDouble() * 0.5 - 0.25));
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}
