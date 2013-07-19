package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class GCCoreTileEntitySolarSingle extends TileEntity
{
    @Override
    public void updateEntity()
    {
        final ArrayList<TileEntity> attachedSolars = new ArrayList<TileEntity>();

        for (int x = this.xCoord - 1; x < this.xCoord + 2; x++)
        {
            for (int z = this.zCoord - 1; z < this.zCoord + 2; z++)
            {
                final Vector3 vector = new Vector3(x, this.yCoord, z);

                final TileEntity tile = vector.getTileEntity(this.worldObj);

                if (tile instanceof GCCoreTileEntitySolarSingle && tile.getBlockMetadata() == this.getBlockMetadata())
                {
                    attachedSolars.add(tile);
                }
            }
        }

        if (attachedSolars.size() == 9)
        {
            for (final TileEntity tile : attachedSolars)
            {
                tile.invalidate();
                tile.worldObj.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, 0);
            }

            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, this.getBlockType().blockID, this.getBlockMetadata() + 1, 3);
            final GCCoreTileEntitySolar tile = (GCCoreTileEntitySolar) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);

            if (tile instanceof IMultiBlock)
            {
                ((IMultiBlock) tile).onCreate(new Vector3(this.xCoord, this.yCoord, this.zCoord));
            }
        }
    }
}
