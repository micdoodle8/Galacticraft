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
    public void moveEntity(double x, double y, double z)
    {
        double d0 = y;
        double origX = x;
        double origZ = z;

        List<AxisAlignedBB> list = this.getCollidingBoundingBoxes(this.getEntityBoundingBox().addCoord(x, y, z));

        for (AxisAlignedBB axisalignedbb : list)
        {
            y = axisalignedbb.calculateYOffset(this.getEntityBoundingBox(), y);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

        for (AxisAlignedBB axisalignedbb1 : list)
        {
            x = axisalignedbb1.calculateXOffset(this.getEntityBoundingBox(), x);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

        for (AxisAlignedBB axisalignedbb2 : list)
        {
            z = axisalignedbb2.calculateZOffset(this.getEntityBoundingBox(), z);
        }

        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

        this.resetPositionToBB();
        this.isCollided = y != y && d0 < 0.0D;

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
        World w = this.worldObj;
        int xs = MathHelper.floor_double(bb.minX) - 1;
        int xe = MathHelper.ceiling_double_int(bb.maxX);
        int ys = MathHelper.floor_double(bb.minY) - 1;
        int ye = MathHelper.ceiling_double_int(bb.maxY);
        int zs = MathHelper.floor_double(bb.minZ) - 1;
        int ze = MathHelper.ceiling_double_int(bb.maxZ);
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
                                iblockstate1.addCollisionBoxToList(w, mutablePos, bb, list, null);
                        }
                    }
                }
            }
        }
        
        return list;
    }
}
