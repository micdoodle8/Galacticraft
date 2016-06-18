package micdoodle8.mods.galacticraft.api.event.oxygen;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Entity Living suffocation events.
 * <p/>
 * Be sure to make the proper checks before cancelling oxygen events... world
 * providers, armor equipped, etc.
 */
public abstract class GCCoreOxygenSuffocationEvent extends LivingEvent
{
    public final WorldProvider provider;

    public GCCoreOxygenSuffocationEvent(EntityLivingBase entity)
    {
        super(entity);
        this.provider = entity.worldObj.provider;
    }

    /**
     * This event is posted just before the living entity suffocates
     * <p/>
     * Set the event as canceled to stop the living entity from suffocating
     * IF THE PRE EVENT IS CANCELED, THE "WARNING: OXYGEN SETUP INVALID!" HUD MESSAGE WILL NOT BE SHOWN
     */
    @Cancelable
    public static class Pre extends GCCoreOxygenSuffocationEvent
    {
        public Pre(EntityLivingBase entity)
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
        public Post(EntityLivingBase entity)
        {
            super(entity);
        }
    }
}
