package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP.PlayerWakeUpEvent;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlocks;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EnumStatus;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChunkCoordinates;
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
        if (event.source.damageType.equals("slimeling") && event.source instanceof EntityDamageSource)
        {
            EntityDamageSource source = (EntityDamageSource) event.source;

            if (source.getEntity() instanceof GCMarsEntitySlimeling && !source.getEntity().worldObj.isRemote)
            {
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
                GCMarsEntitySlimeling entitywolf = (GCMarsEntitySlimeling) entity;

                if (entitywolf.isTamed())
                {
                    event.entityLiving.recentlyHit = 100;
                    event.entityLiving.attackingPlayer = null;
                }
            }
        }
    }
    
    @ForgeSubscribe
    public void onPlayerWakeUp(PlayerWakeUpEvent event)
    {
        ChunkCoordinates c = event.entityPlayer.playerLocation;
        int blockID = event.entityPlayer.worldObj.getBlockId(c.posX, c.posY, c.posZ);
        int metadata = event.entityPlayer.worldObj.getBlockMetadata(c.posX, c.posY, c.posZ);
        
        if (blockID == GCMarsBlocks.machine.blockID && metadata >= GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA && event.flag1 == false && event.flag2 == true && event.flag3 == true)
        {
            event.result = EnumStatus.NOT_POSSIBLE_HERE;
        }
    }
}
