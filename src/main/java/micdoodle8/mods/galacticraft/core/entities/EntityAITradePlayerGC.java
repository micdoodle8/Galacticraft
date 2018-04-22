package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayerGC extends EntityAIBase
{
    private EntityAlienVillager villager;

    public EntityAITradePlayerGC(EntityAlienVillager villagerIn)
    {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute()
    {
        if (!this.villager.isEntityAlive())
        {
            return false;
        }
        else if (this.villager.isInWater())
        {
            return false;
        }
        else if (!this.villager.onGround)
        {
            return false;
        }
        else if (this.villager.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = this.villager.getCustomer();
            return entityplayer == null ? false : (this.villager.getDistanceSq(entityplayer) > 16.0D ? false : entityplayer.openContainer instanceof Container);
        }
    }

    @Override
    public void startExecuting()
    {
        this.villager.getNavigator().clearPath();
    }

    @Override
    public void resetTask()
    {
        this.villager.setCustomer((EntityPlayer) null);
    }
}