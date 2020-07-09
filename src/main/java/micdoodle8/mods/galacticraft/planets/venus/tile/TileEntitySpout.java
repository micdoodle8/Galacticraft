package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.client.fx.VenusParticles;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

public class TileEntitySpout extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.spout)
    public static TileEntityType<TileEntitySpout> TYPE;

    private final Random rand = new Random(System.currentTimeMillis());

    public TileEntitySpout()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (this.world.isRemote)
        {
            if (rand.nextInt(400) == 0)
            {
                BlockState stateAbove = this.world.getBlockState(this.getPos().up());
                if (stateAbove.getBlock().isAir(this.world.getBlockState(this.getPos().up()), this.world, this.getPos().up()))
                {
                    double posX = (double) pos.getX() + 0.45 + rand.nextDouble() * 0.1;
                    double posY = (double) pos.getY() + 1.0;
                    double posZ = (double) pos.getZ() + 0.45 + rand.nextDouble() * 0.1;
                    for (int i = 0; i < 4 + rand.nextInt(4); ++i)
                    {
                        world.addParticle(VenusParticles.ACID_VAPOR, posX, posY, posZ, rand.nextDouble() * 0.5 - 0.25, rand.nextDouble() * 0.5 + 0.5, rand.nextDouble() * 0.5 - 0.25);
                    }
                }
            }
        }
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }
}
