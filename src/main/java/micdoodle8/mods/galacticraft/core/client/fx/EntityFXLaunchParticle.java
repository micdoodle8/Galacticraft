package micdoodle8.mods.galacticraft.core.client.fx;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class EntityFXLaunchParticle extends EntityFX
{
    public EntityFXLaunchParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    }
    
    @Override
    public void moveEntity(double x, double y, double z)
    {
        double d0 = y;

        List<AxisAlignedBB> list = this.getCollidingBoundingBoxes(this.getEntityBoundingBox().addCoord(x, y, z));
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

        for (AxisAlignedBB blocker : list)
        {
            y = blocker.calculateYOffset(axisalignedbb, y);
        }
        axisalignedbb = axisalignedbb.offset(0.0D, y, 0.0D);

        for (AxisAlignedBB blocker : list)
        {
            x = blocker.calculateXOffset(axisalignedbb, x);
        }
        axisalignedbb = axisalignedbb.offset(x, 0.0D, 0.0D);

        for (AxisAlignedBB blocker : list)
        {
            z = blocker.calculateZOffset(axisalignedbb, z);
        }
        axisalignedbb = axisalignedbb.offset(0.0D, 0.0D, z);

        this.setEntityBoundingBox(axisalignedbb);
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
        this.isCollided = false;
    }

    protected void resetPositionToBB()
    {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0D;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D;
    }
    
    /**
     * Simplified for performance: no colliding boxes for entities (most/all of the entities will be other launch particles)
     */
    public List<AxisAlignedBB> getCollidingBoundingBoxes(AxisAlignedBB bb)
    {
        List<AxisAlignedBB> list = new LinkedList<>();
        World w = this.worldObj;
        int xs = MathHelper.floor_double(bb.minX);
        int xe = MathHelper.floor_double(bb.maxX);
        int ys = MathHelper.floor_double(bb.minY) - 1;
        int ye = MathHelper.floor_double(bb.maxY);
        int zs = MathHelper.floor_double(bb.minZ);
        int ze = MathHelper.floor_double(bb.maxZ);
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = xs; x <= xe; ++x)
        {
            for (int z = zs; z <= ze; ++z)
            {
                if (w.isBlockLoaded(mutablePos.set(x, 64, z)))
                {
                    for (int y = ys; y <= ye; ++y)
                    {
                        mutablePos.set(x, y, z);
                        IBlockState iblockstate1 = w.getBlockState(mutablePos);
                        iblockstate1.getBlock().addCollisionBoxesToList(w, mutablePos, iblockstate1, bb, list, null);
                    }
                }
            }
        }
        
        return list;
    }
}
