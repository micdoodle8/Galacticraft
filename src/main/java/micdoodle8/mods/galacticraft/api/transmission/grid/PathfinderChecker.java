package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Check if a conductor connects with another.
 *
 * @author Calclavia
 */
public class PathfinderChecker extends Pathfinder
{
    public PathfinderChecker(final World world, final INetworkConnection targetConnector, final NetworkType networkType, final INetworkConnection... ignoreConnector)
    {
        super(new IPathCallBack()
        {
            @Override
            public Set<BlockVec3> getConnectedNodes(Pathfinder finder, BlockVec3 currentNode)
            {
                Set<BlockVec3> neighbors = new HashSet<BlockVec3>();

                for (int i = 0; i < 6; i++)
                {
                    ForgeDirection direction = ForgeDirection.getOrientation(i);
                    BlockVec3 position = currentNode.clone().modifyPositionFromSide(direction);
                    TileEntity connectedBlock = position.getTileEntity(world);

                    if (connectedBlock instanceof ITransmitter && !Arrays.asList(ignoreConnector).contains(connectedBlock))
                    {
                        if (((ITransmitter) connectedBlock).canConnect(direction.getOpposite(), networkType))
                        {
                            neighbors.add(position);
                        }
                    }
                }

                return neighbors;
            }

            @Override
            public boolean onSearch(Pathfinder finder, BlockVec3 node)
            {
                if (node.getTileEntity(world) == targetConnector)
                {
                    finder.results.add(node);
                    return true;
                }

                return false;
            }
        });
    }
}
