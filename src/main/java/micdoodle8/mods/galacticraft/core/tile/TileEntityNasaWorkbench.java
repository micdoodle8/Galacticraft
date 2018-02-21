package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityNasaWorkbench extends TileEntityMulti implements IMultiBlock
{
    private boolean initialised;
    
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
        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getPos(), this.worldObj);
        }
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.NASA_WORKBENCH;
    }
    
    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.worldObj.getHeight() - 1;

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
                    if (Math.abs(x) != 1 || Math.abs(z) != 1)
                    {
                        positions.add(new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z));
                    }
                }
            }
        }

        if (placedPosition.getY() + 3 <= buildHeight)
        {
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + 3, placedPosition.getZ()));
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.NASA_WORKBENCH)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(thisBlock));
                }
                this.worldObj.setBlockToAir(pos);
            }
        }
        this.worldObj.destroyBlock(thisBlock, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return AxisAlignedBB.fromBounds(getPos().getX() - 1, getPos().getY() - 1, getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 5, getPos().getZ() + 2);
    }
}
