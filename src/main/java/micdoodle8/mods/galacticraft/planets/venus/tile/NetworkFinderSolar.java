package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NetworkFinderSolar
{
    public World worldObj;
    public BlockVec3 start;
    private BlockVec3 toIgnore;

    private Set<BlockVec3> iterated = new HashSet<>();
    public List<ITransmitter> found = new LinkedList<>();

    public NetworkFinderSolar(World world, BlockVec3 location, BlockVec3 ignore)
    {
        worldObj = world;
        start = location;

        toIgnore = ignore;
    }

    private void loopAll(int x, int y, int z, int dirIn)
    {
        BlockVec3 obj = null;
        for (int dir = 0; dir < 6; dir++)
        {
            if (dir == dirIn)
            {
                continue;
            }
            switch (dir)
            {
            case 0:
                obj = new BlockVec3(x, y - 1, z);
                break;
            case 1:
                obj = new BlockVec3(x, y + 1, z);
                break;
            case 2:
                obj = new BlockVec3(x, y, z - 1);
                break;
            case 3:
                obj = new BlockVec3(x, y, z + 1);
                break;
            case 4:
                obj = new BlockVec3(x - 1, y, z);
                break;
            case 5:
                obj = new BlockVec3(x + 1, y, z);
                break;
            }

            if (!iterated.contains(obj))
            {
                iterated.add(obj);

                TileEntity tileEntity = worldObj.getTileEntity(new BlockPos(obj.x, obj.y, obj.z));

                if (tileEntity instanceof ITransmitter && ((ITransmitter) tileEntity).getNetworkType() == NetworkType.SOLAR_MODULE)
                {
                    found.add((ITransmitter) tileEntity);
                    loopAll(obj.x, obj.y, obj.z, dir ^ 1);
                }
            }
        }
    }

    public List<ITransmitter> exploreNetwork()
    {
        TileEntity tile = start.getTileEntity(worldObj);
        if (tile instanceof ITransmitter && ((ITransmitter) tile).getNetworkType() == NetworkType.SOLAR_MODULE)
        {
            iterated.add(start);
            iterated.add(toIgnore);
            found.add((ITransmitter) tile);
            loopAll(start.x, start.y, start.z, 6);
        }

        return found;
    }
}
