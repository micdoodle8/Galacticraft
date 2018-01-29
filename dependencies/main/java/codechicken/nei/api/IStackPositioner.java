package codechicken.nei.api;

import codechicken.nei.api.stack.PositionedStack;

import java.util.ArrayList;

/**
 * For repositioning recipes in overlay renderers.
 */
public interface IStackPositioner {
    ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai);
}
