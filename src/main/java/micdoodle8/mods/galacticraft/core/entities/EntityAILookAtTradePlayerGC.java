package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayerGC extends EntityAIWatchClosest
{
    private final EntityAlienVillager theMerchant;

    public EntityAILookAtTradePlayerGC(EntityAlienVillager theMerchantIn)
    {
        super(theMerchantIn, EntityPlayer.class, 8.0F);
        this.theMerchant = theMerchantIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theMerchant.isTrading())
        {
            this.closestEntity = this.theMerchant.getCustomer();
            return true;
        }
        else
        {
            return false;
        }
    }
}