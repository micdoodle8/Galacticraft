package micdoodle8.mods.galacticraft.core.client.fx;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class EntityFXLaunchParticle extends Particle
{
    public EntityFXLaunchParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
    
    @Override
    public void move(double x, double y, double z)
    {
        double d0 = y;
        double origX = x;
        double origZ = z;

        List<AxisAlignedBB> list = this.getCollidingBoundingBoxes(this.getBoundingBox().expand(x, y, z));

        for (AxisAlignedBB axisalignedbb : list)
        {
            y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
        }

        this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));

        for (AxisAlignedBB axisalignedbb1 : list)
        {
            x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
        }

        this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));

        for (AxisAlignedBB axisalignedbb2 : list)
        {
            z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
        }

        this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));

        this.resetPositionToBB();
        this.onGround = d0 != y && d0 < 0.0D;

        if (origX != x)
        {
            this.motionX = 0.0D;
        }

        if (origZ != z)
        {
            this.motionZ = 0.0D;
        }
    }

    /**
     * Simplified for performance: no colliding boxes for entities (most/all of the entities will be other launch particles)
     */
    public List<AxisAlignedBB> getCollidingBoundingBoxes(AxisAlignedBB bb)
    {
        List<AxisAlignedBB> list = new LinkedList<>();
        World w = this.world;
        int xs = MathHelper.floor(bb.minX) - 1;
        int xe = MathHelper.ceil(bb.maxX);
        int ys = MathHelper.floor(bb.minY) - 1;
        int ye = MathHelper.ceil(bb.maxY);
        int zs = MathHelper.floor(bb.minZ) - 1;
        int ze = MathHelper.ceil(bb.maxZ);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        IBlockState iblockstate1;
        boolean xends, xzmiddle;

        for (int x = xs; x <= xe; ++x)
        {
            xends = (x == xs || x == xe); 
            for (int z = zs; z <= ze; ++z)
            {
                if (xends)
                {
                    if (z == zs || z == ze)
                        continue;

                    xzmiddle = false; 
                }
                else
                {
                    xzmiddle = z > zs && z < ze;
                }
                
                if (w.isBlockLoaded(mutablePos.setPos(x, 64, z)))
                {
                    for (int y = ys; y <= ye; ++y)
                    {
                        if (y != ys && y != ye || xzmiddle)
                        {
                            mutablePos.setPos(x, y, z);
                            iblockstate1 = w.getBlockState(mutablePos);
                            if (!(iblockstate1.getBlock() instanceof BlockAir))
                                iblockstate1.addCollisionBoxToList(w, mutablePos, bb, list, null, false);
                        }
                    }
                }
            }
        }
        
        return list;
    }
}
