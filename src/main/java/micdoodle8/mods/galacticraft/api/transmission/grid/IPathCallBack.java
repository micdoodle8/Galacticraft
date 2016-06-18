package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;

import java.util.Set;

public interface IPathCallBack
{
    /**
     * @param finder      - The Pathfinder object.
     * @param currentNode - The node being iterated through.
     * @return A set of nodes connected to the currentNode. Essentially one
     * should return a set of neighboring nodes.
     */
    public Set<BlockVec3> getConnectedNodes(Pathfinder finder, BlockVec3 currentNode);

    /**
     * Called when looping through nodes.
     *
     * @param finder - The Pathfinder.
     * @param node   - The node being searched.
     * @return True to stop the path finding operation.
     */
    public boolean onSearch(Pathfinder finder, BlockVec3 node);
}