package codechicken.lib.raytracer;

import java.util.List;

/**
 * Created by covers1624 on 8/10/2016.
 * This is a completely random interface, mainly used to have a standard method to get cuboids from a tile.
 * For a somewhat standard impl, use the first index in the array to indicate the blocks bounds, and anything else for other clickable's or boxes.
 */
public interface ICuboidProvider {

    /**
     * Everything must return pure bounds not offset by the tiles position.
     *
     * @return A list of cuboids for a tile.
     */
    List<IndexedCuboid6> getIndexedCuboids();

}
