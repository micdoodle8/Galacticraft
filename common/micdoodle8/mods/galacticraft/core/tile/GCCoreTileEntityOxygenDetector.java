package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class GCCoreTileEntityOxygenDetector extends TileEntityAdvanced
{
    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (this.worldObj != null && !this.worldObj.isRemote && this.ticks % 50 == 0)
        {
            this.blockType = this.getBlockType();

            if (this.blockType != null && this.blockType instanceof GCCoreBlockOxygenDetector)
            {
                ((GCCoreBlockOxygenDetector) this.blockType).updateOxygenState(this.worldObj, this.xCoord, this.yCoord, this.zCoord, OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, new Vector3(this).translate(new Vector3(-1, -1, -1)), new Vector3(this).translate(new Vector3(2, 2, 2))));
            }
        }
    }
}
