package codechicken.nei;

import codechicken.nei.api.IStackPositioner;

import java.util.ArrayList;

public class OffsetPositioner implements IStackPositioner
{
    public OffsetPositioner(int x, int y)
    {
        offsetx = x;
        offsety = y;
    }
    
    @Override
    public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ai)
    {
        for(PositionedStack stack : ai)
        {
            stack.relx+=offsetx;
            stack.rely+=offsety;
        }
        return ai;
    }
    
    public int offsetx;
    public int offsety;    
}
