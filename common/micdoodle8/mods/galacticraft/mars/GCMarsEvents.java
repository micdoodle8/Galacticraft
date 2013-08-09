package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.FMLLog;

public class GCMarsEvents
{
    @ForgeSubscribe
    public void onLivingDeath(LivingDeathEvent event)
    {
        FMLLog.info("Done1");
        if (event.source.damageType.equals("slimeling") && event.source instanceof EntityDamageSource)
        {
            FMLLog.info("Done2");
            EntityDamageSource source = (EntityDamageSource) event.source;
            
            if (source.getEntity() instanceof GCMarsEntitySlimeling && !source.getEntity().worldObj.isRemote)
            {
                FMLLog.info("Done3");
                ((GCMarsEntitySlimeling) source.getEntity()).kills++;
            }
        }
    }

    @ForgeSubscribe
    public void onLivingAttacked(LivingAttackEvent event)
    {
        if (!event.entity.isEntityInvulnerable() && !event.entity.worldObj.isRemote && event.entityLiving.func_110143_aJ() <= 0.0F && !(event.source.isFireDamage() && event.entityLiving.isPotionActive(Potion.fireResistance)))
        {
            Entity entity = event.source.getEntity();
            
            if (entity instanceof GCMarsEntitySlimeling)
            {
                GCMarsEntitySlimeling entitywolf = (GCMarsEntitySlimeling)entity;

                if (entitywolf.isTamed())
                {
                    event.entityLiving.recentlyHit = 100;
                    event.entityLiving.attackingPlayer = null;
                }
            }
        }
    }
}
