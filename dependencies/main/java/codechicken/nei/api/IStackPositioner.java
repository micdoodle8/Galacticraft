package codechicken.nei.api;

import codechicken.nei.PositionedStack;

import java.util.ArrayList;

/**
 * For repositioning recipes in overlay renderers.
 * 
 */
public interface IStackPositioner
{
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai);
}
