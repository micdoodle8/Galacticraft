package micdoodle8.mods.galacticraft.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Entity Living zero gravity events.
 * <p/>
 * Cancel the event to disable zero gravity effects on this entity
 * If Zero Gravity is disabled, the entity will fall under dimension-set gravity (probably very low!)
 */
public abstract class ZeroGravityEvent extends LivingEvent
{
    public final WorldProvider provider;

    public ZeroGravityEvent(EntityLivingBase entity)
    {
        super(entity);
        this.provider = entity.world.provider;
    }

    /**
     * Cancel this to return false on all freefall tests
     * "Freefall" = feet not on a block in a Zero G Dimension
     */
    @Cancelable
    public static class InFreefall extends ZeroGravityEvent
    {
        public InFreefall(EntityLivingBase entity)
        {
            super(entity);
        }
    }

    /**
     * Cancel this to block special Zero G dimension motion
     * (moving on walls, and jumping and landing on blocks)
     */
    @Cancelable
    public static class Motion extends ZeroGravityEvent
    {
        public Motion(EntityLivingBase entity)
        {
            super(entity);
        }
    }

    /**
     * Cancel this to block the sneak override on Zero G dimensions
     * when descending (no sneak) or landing (sneak during impact)
     * 
     * (This sneaking will mostly be cancelled anyhow if the Motion
     * event is cancelled)
     */
    @Cancelable
    public static class SneakOverride extends ZeroGravityEvent
    {
        public SneakOverride(EntityLivingBase entity)
        {
            super(entity);
        }
    }
}
