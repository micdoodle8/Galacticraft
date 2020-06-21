package micdoodle8.mods.galacticraft.api.event.oxygen;

import net.minecraft.entity.LivingEntity;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Entity Living suffocation events.
 * <p/>
 * Be sure to make the proper checks before cancelling oxygen events... world
 * providers, armor equipped, etc.
 */
public abstract class GCCoreOxygenSuffocationEvent extends LivingEvent
{
    public final Dimension dimension;

    public GCCoreOxygenSuffocationEvent(LivingEntity entity)
    {
        super(entity);
        this.dimension = entity.world.getDimension();
    }

    /**
     * This event is posted just before the living entity suffocates
     * <p/>
     * Set the event as cancelled to stop the living entity from suffocating
     * <p/>
     * IF THE Pre EVENT IS CANCELLED, THE "WARNING: OXYGEN SETUP INVALID!" HUD MESSAGE WILL NOT BE SHOWN
     */
    @Cancelable
    public static class Pre extends GCCoreOxygenSuffocationEvent
    {
        public Pre(LivingEntity entity)
        {
            super(entity);
        }
    }

    /**
     * This event is called after the living entity takes damage from oxygen
     * suffocation
     * <p/>
     * The event is not called if the pre event was canceled
     */
    public static class Post extends GCCoreOxygenSuffocationEvent
    {
        public Post(LivingEntity entity)
        {
            super(entity);
        }
    }
}
