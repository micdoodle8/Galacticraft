package codechicken.core.internal;

import codechicken.core.CCUpdateChecker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class CCCEventHandler
{
    public static int renderTime;
    public static float renderFrame;

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == Phase.END) {
            CCUpdateChecker.tick();
            renderTime++;
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if(event.phase == Phase.START)
            renderFrame = event.renderTickTime;
    }
}
