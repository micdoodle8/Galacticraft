package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityNasaWorkbench extends TileEntityMulti implements IMultiBlock
{
    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(GalacticraftCore.instance, GuiIdsCore.NASA_WORKBENCH_ROCKET, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        return true;
    }

    @Override
    public void onCreate(BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();
        int buildHeight = this.worldObj.getHeight() - 1;
        
        for (int y = 1; y < 3; y++)
        {
        	if (placedPosition.getY() + y > buildHeight) return;

	        for (int x = -1; x < 2; x++)
	        {
                for (int z = -1; z < 2; z++)
                {
                    final BlockPos vecToAdd = new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z);

                    if (!vecToAdd.equals(placedPosition))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
                        }
                    }
                }
            }
        }

    	if (placedPosition.getY() + 3 > buildHeight) return;
        final BlockPos vecToAdd = new BlockPos(placedPosition.getX(), placedPosition.getY() + 3, placedPosition.getZ());
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);       
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockVec3 thisBlock = new BlockVec3(this);

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (Math.abs(x) != 1 || Math.abs(z) != 1)
                    {
                        BlockPos pos = new BlockPos(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z);

                        if ((y == 0 || y == 3) && x == 0 && z == 0)
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.func_180533_a(pos, this.worldObj.getBlockState(pos));
                            }

                            this.worldObj.destroyBlock(pos, y == 0);
                        }
                        else if (y != 0 && y != 3)
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.func_180533_a(pos, this.worldObj.getBlockState(pos));
                            }

                            this.worldObj.setBlockToAir(pos);
                        }
                    }
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
    	return AxisAlignedBB.fromBounds(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
    }
}
