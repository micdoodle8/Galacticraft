package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileEntityLandingPadSingle extends TileEntity
{
    @Override
    public void updateEntity()
    {
        final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

        for (int x = this.xCoord - 1; x < this.xCoord + 2; x++)
        {
            for (int z = this.zCoord - 1; z < this.zCoord + 2; z++)
            {
                final BlockVec3 vector = new BlockVec3(x, this.yCoord, z);

                final TileEntity tile = vector.getTileEntity(this.worldObj);

                if (tile != null && tile instanceof TileEntityLandingPadSingle)
                {
                    attachedLaunchPads.add(tile);
                }
            }
        }

        if (attachedLaunchPads.size() == 9)
        {
            for (final TileEntity tile : attachedLaunchPads)
            {
                tile.invalidate();
                tile.getWorldObj().setBlock(tile.xCoord, tile.yCoord, tile.zCoord, Blocks.air);
                // ((GCCoreBlockLandingPadFull)GCCoreBlocks.landingPadFull).onBlockAdded(worldObj,
                // tile.xCoord, tile.yCoord, tile.zCoord);
            }

            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, GCBlocks.landingPadFull, 0, 3);
            final TileEntityLandingPad tile = (TileEntityLandingPad) this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord);

            if (tile != null)
            {
                tile.onCreate(new BlockVec3(this.xCoord, this.yCoord, this.zCoord));
            }
        }
    }
}
