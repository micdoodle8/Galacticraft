package codechicken.core.internal;

import java.util.EnumSet;

import codechicken.core.CCUpdateChecker;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler
{
    public static int renderTime;
    public static float renderFrame;
    
    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData)
    {
        if(type.contains(TickType.RENDER))
            renderFrame = (Float) tickData[0];
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData)
    {
        if(type.contains(TickType.CLIENT))
        {
            CCUpdateChecker.tick();
            renderTime++;
        }
    }

    @Override
    public EnumSet<TickType> ticks()
    {
        return EnumSet.of(TickType.CLIENT, TickType.RENDER);
    }

    @Override
    public String getLabel()
    {
        return "CodeChicken Core internals";
    }

}
