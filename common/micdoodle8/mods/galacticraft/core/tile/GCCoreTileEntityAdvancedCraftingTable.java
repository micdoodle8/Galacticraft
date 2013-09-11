package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreTileEntityAdvancedCraftingTable extends TileEntityMulti implements IMultiBlock
{
    public GCCoreTileEntityAdvancedCraftingTable()
    {
        super(GalacticraftCore.CHANNELENTITIES);
    }

    @Override
    public void validate()
    {
        super.validate();
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    @Override
    public void onCreate(Vector3 placedPosition)
    {
        this.mainBlockPosition = placedPosition;

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    final Vector3 vecToAdd = Vector3.add(placedPosition, new Vector3(x, y, z));

                    if (!vecToAdd.equals(placedPosition))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            if ((y == 0 || y == 3) && x == 0 && z == 0)
                            {
                                ((GCCoreBlockMulti) GCCoreBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
                            }
                            else if (y != 0 && y != 3)
                            {
                                ((GCCoreBlockMulti) GCCoreBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final Vector3 thisBlock = new Vector3(this);

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (Math.abs(x) != 1 || Math.abs(z) != 1)
                    {
                        if ((y == 0 || y == 3) && x == 0 && z == 0)
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCCoreBlocks.nasaWorkbench.blockID & 4095, GCCoreBlocks.nasaWorkbench.blockID >> 12 & 255);
                            }
                            this.worldObj.setBlock(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, 0, 0, 3);
                        }
                        else if (y != 0 && y != 3)
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCCoreBlocks.nasaWorkbench.blockID & 4095, GCCoreBlocks.nasaWorkbench.blockID >> 12 & 255);
                            }
                            this.worldObj.setBlock(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, 0, 0, 3);
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
        return TileEntity.INFINITE_EXTENT_AABB;
    }
}
