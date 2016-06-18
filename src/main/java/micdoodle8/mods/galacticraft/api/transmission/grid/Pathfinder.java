package micdoodle8.mods.galacticraft.api.transmission.grid;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that allows flexible pathfinding for different positions. Compared to
 * AStar pathfinding, this version is faster but does not calculated the most
 * optimal path.
 *
 * @author Calclavia
 */
public class Pathfinder
{
    /**
     * A pathfinding call back interface used to call back on paths.
     */
    public IPathCallBack callBackCheck;

    /**
     * A list of nodes that the pathfinder already went through.
     */
    public Set<BlockVec3> closedSet;

    /**
     * The resulted path found by the pathfinder. Could be null if no path was
     * found.
     */
    public Set<BlockVec3> results;

    public Pathfinder(IPathCallBack callBack)
    {
        this.callBackCheck = callBack;
        this.reset();
    }

    /**
     * @return True on success finding, false on failure.
     */
    public boolean findNodes(BlockVec3 currentNode)
    {
        this.closedSet.add(currentNode);

        if (this.callBackCheck.onSearch(this, currentNode))
        {
            return false;
        }

        for (BlockVec3 node : this.callBackCheck.getConnectedNodes(this, currentNode))
        {
            if (!this.closedSet.contains(node))
            {
                if (this.findNodes(node))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called to execute the pathfinding operation.
     */
    public Pathfinder init(BlockVec3 startNode)
    {
        this.findNodes(startNode);
        return this;
    }

    public Pathfinder reset()
    {
        this.closedSet = new HashSet<BlockVec3>();
        this.results = new HashSet<BlockVec3>();
        return this;
    }
}
