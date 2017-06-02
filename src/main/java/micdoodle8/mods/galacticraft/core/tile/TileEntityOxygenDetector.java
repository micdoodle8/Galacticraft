package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityOxygenDetector extends TileEntity implements ITickable
{
    private int ticks = 49;
    private AxisAlignedBB oxygenSearch;

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote && ++this.ticks == 50) 
        {
            this.ticks = 0;
            if (this.getBlockType() instanceof BlockOxygenDetector)
            {
                boolean oxygenFound = false;
                if (this.worldObj.provider instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider)this.worldObj.provider).hasBreathableAtmosphere())
                {
                    oxygenFound = OxygenUtil.isAABBInBreathableAirBlock(this.worldObj, this.oxygenSearch, false);
                }
                else
                {
                    for (EnumFacing side : EnumFacing.VALUES)
                    {
                        BlockPos offset = this.pos.offset(side, 1);
                        IBlockState bs = this.worldObj.getBlockState(offset);
                        if (!bs.getBlock().isSideSolid(worldObj, offset, side.getOpposite()))
                        {
                            oxygenFound = true;
                            break;
                        }
                    }
                }
                ((BlockOxygenDetector) this.blockType).updateOxygenState(this.worldObj, this.getPos(), oxygenFound);
            }
        }
    }
    
    @Override
    public void onLoad()
    {
        this.oxygenSearch = AxisAlignedBB.fromBounds(this.getPos().getX() - 0.6, this.getPos().getY() - 0.6, this.getPos().getZ() - 0.6, this.getPos().getX() + 1.6, this.getPos().getY() + 1.6, this.getPos().getZ() + 1.6);
    }
}
