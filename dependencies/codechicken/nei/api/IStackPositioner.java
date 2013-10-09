package codechicken.nei.api;

import java.util.ArrayList;

import codechicken.nei.PositionedStack;

/**
 * For repositioning recipes in overlay renderers.
 * 
 */
public interface IStackPositioner
{
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai);
}
