package codechicken.core.featurehack;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FeatureHack
{
    private static boolean updateHookEnabled = false;
    private static boolean renderHookEnabled = false;
    
    public static void enableUpdateHook()
    {
        if(updateHookEnabled)
            return;
        
        updateHookEnabled = true;
        if(FMLCommonHandler.instance().getSide().isClient())
            enableClientUpdateHook();
    }

    @SideOnly(Side.CLIENT)
    public static void enableRenderHook()
    {
        if(renderHookEnabled)
            return;
        
        renderHookEnabled = true;
        RenderingRegistry.registerEntityRenderingHandler(EntityUpdateHook.class, new RenderNull());
    }

    @SideOnly(Side.CLIENT)
    private static void enableClientUpdateHook()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityRenderHook.class, new RenderEntityRenderHook());
    }
}
