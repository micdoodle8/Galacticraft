package micdoodle8.mods.galacticraft.api.event.oxygen;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.Cancelable;

/**
 * Entity Living suffocation events.
 * 
 * Be sure to make the proper checks before cancelling oxygen events... world
 * providers, armor equipped, etc.
 */
public abstract class EventSuffocation extends LivingEvent
{
	public final WorldProvider provider;

	public EventSuffocation(EntityLivingBase entity)
	{
		super(entity);
		this.provider = entity.worldObj.provider;
	}

	/**
	 * This event is posted just before the living entity suffocates
	 * 
	 * Set the event as canceled to stop the living entity from suffocating
	 */
	@Cancelable
	public static class Pre extends EventSuffocation
	{
		public Pre(EntityLivingBase entity)
		{
			super(entity);
		}
	}

	/**
	 * This event is called after the living entity takes damage from oxygen
	 * suffocation
	 * 
	 * The event is not called if the pre event was canceled
	 */
	public static class Post extends EventSuffocation
	{
		public Post(EntityLivingBase entity)
		{
			super(entity);
		}
	}
}
