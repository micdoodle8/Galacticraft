package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenDetector;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityOxygenDetector extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.oxygenDetector)
    public static TileEntityType<TileEntityOxygenDetector> TYPE;

    private int ticks = 49;
    private AxisAlignedBB oxygenSearch;

    public TileEntityOxygenDetector()
    {
        super(TYPE);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote && ++this.ticks == 50)
        {
            this.ticks = 0;
            if (this.getBlockState().getBlock() instanceof BlockOxygenDetector)
            {
                boolean oxygenFound = false;
                if (this.world.getDimension() instanceof IGalacticraftDimension && !((IGalacticraftDimension) this.world.getDimension()).hasBreathableAtmosphere())
                {
                    oxygenFound = OxygenUtil.isAABBInBreathableAirBlock(this.world, this.oxygenSearch, false);
                }
                else
                {
                    for (Direction side : Direction.values())
                    {
                        BlockPos offset = this.pos.offset(side, 1);
                        BlockState bs = this.world.getBlockState(offset);
                        if (!bs.isSolidSide(world, offset, side.getOpposite())) // TODO Test... Not solid?
//                        if (!bs.getBlock().isSideSolid(bs, world, offset, side.getOpposite()))
                        {
                            oxygenFound = true;
                            break;
                        }
                    }
                }
                this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockOxygenDetector.ACTIVE, oxygenFound));
            }
        }
    }

    @Override
    public void onLoad()
    {
        this.oxygenSearch = new AxisAlignedBB(this.getPos().getX() - 0.6, this.getPos().getY() - 0.6, this.getPos().getZ() - 0.6, this.getPos().getX() + 1.6, this.getPos().getY() + 1.6, this.getPos().getZ() + 1.6);
    }
}
