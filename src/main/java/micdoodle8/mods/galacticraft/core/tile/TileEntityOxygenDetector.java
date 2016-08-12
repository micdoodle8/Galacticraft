package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityOxygenDetector extends TileEntityAdvanced
{
    @Override
    public void update()
    {
        super.update();

        if (this.worldObj != null && !this.worldObj.isRemote && this.ticks % 50 == 0)
        {
            this.blockType = this.getBlockType();

            if (this.blockType != null && this.blockType instanceof BlockOxygenDetector)
            {
                ((BlockOxygenDetector) this.blockType).updateOxygenState(this.worldObj, this.getPos(), OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, AxisAlignedBB.fromBounds(this.getPos().getX() - 1, this.getPos().getY() - 1, this.getPos().getZ() - 1, this.getPos().getX() + 2, this.getPos().getY() + 2, this.getPos().getZ() + 2)));
            }
        }
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    public int getPacketCooldown()
    {
        return 0;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return false;
    }
}
