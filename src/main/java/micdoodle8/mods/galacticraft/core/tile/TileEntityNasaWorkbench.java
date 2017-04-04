package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityNasaWorkbench extends TileEntityMulti implements IMultiBlock
{
    public TileEntityNasaWorkbench()
    {
        super(null);
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(GalacticraftCore.instance, GuiIdsCore.NASA_WORKBENCH_ROCKET, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        return true;
    }

    @Override
    public void update()
    {
        super.update();

        // TODO: Find a more efficient way to fix this
        //          Broken since 1.8 and this is an inefficient fix
        int buildHeight = this.worldObj.getHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (getPos().getY() + y > buildHeight)
            {
                return;
            }

            for (int x = -1; x < 2; x++)
            {
                for (int z = -1; z < 2; z++)
                {
                    final BlockPos vecToAdd = new BlockPos(getPos().getX() + x, getPos().getY() + y, getPos().getZ() + z);

                    if (!vecToAdd.equals(getPos()))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            TileEntity tile = this.worldObj.getTileEntity(vecToAdd);
                            if (tile instanceof TileEntityMulti)
                            {
                                BlockPos pos = ((TileEntityMulti) tile).mainBlockPosition;
                                if (pos == null || !pos.equals(getPos()))
                                {
                                    ((TileEntityMulti) tile).mainBlockPosition = getPos();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();
        int buildHeight = world.getHeight() - 1;

        for (int y = 1; y < 3; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }

            for (int x = -1; x < 2; x++)
            {
                for (int z = -1; z < 2; z++)
                {
                    final BlockPos vecToAdd = new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z);

                    if (!vecToAdd.equals(placedPosition))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecToAdd, placedPosition, 3);
                        }
                    }
                }
            }
        }

        if (placedPosition.getY() + 3 > buildHeight)
        {
            return;
        }
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
                                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
                            }

                            this.worldObj.setBlockToAir(pos);
                        }
                        else if (y != 0 && y != 3)
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
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
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
    }
}
