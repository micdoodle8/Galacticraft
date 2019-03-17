package micdoodle8.mods.galacticraft.core.energy.grid;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NetworkFinder
{
    public World worldObj;
    public BlockPos start;
    private BlockPos toIgnore;

    private Set<BlockPos> iterated = new HashSet<>();
    public List<IConductor> found = new LinkedList<>();

    public NetworkFinder(World world, BlockPos location, BlockPos ignore)
    {
        worldObj = world;
        start = location;
        toIgnore = ignore;
    }

    private void loopAll(BlockPos pos, EnumFacing dirIn)
    {
        TileEntity tileLast = worldObj.getTileEntity(pos);
        BlockPos obj;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if (facing != dirIn && ((IConductor) tileLast).canConnect(facing, NetworkType.POWER))
            {
                obj = pos.offset(facing);

                if (!iterated.contains(obj))
                {
                    iterated.add(obj);

                    TileEntity tile = worldObj.getTileEntity(obj);

                    if (tile instanceof IConductor)
                    {
                        // dirIn will be null if pos is the start block
                        if (dirIn == null || ((IConductor) tile).canConnect(dirIn, NetworkType.POWER))
                        {
                            found.add((IConductor) tile);
                            loopAll(obj, facing.getOpposite());
                        }
                    }
                }
            }
        }
    }

    public List<IConductor> exploreNetwork()
    {
        TileEntity startTile = worldObj.getTileEntity(start);
        if (startTile instanceof IConductor)
        {
            iterated.add(start);
            iterated.add(toIgnore);
            found.add((IConductor) startTile);
            loopAll(start, null);
        }

        return found;
    }
}
