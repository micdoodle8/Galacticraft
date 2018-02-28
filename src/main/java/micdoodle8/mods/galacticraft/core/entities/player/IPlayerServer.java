package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public interface IPlayerServer
{
    void updateRiddenPre(EntityPlayerMP player);

    void updateRiddenPost(EntityPlayerMP player);

    boolean mountEntity(EntityPlayerMP player, Entity par1Entity);

    void moveEntity(EntityPlayerMP player, double par1, double par3, double par5);

    boolean wakeUpPlayer(EntityPlayerMP player, boolean par1, boolean par2, boolean par3);

    float attackEntityFrom(EntityPlayerMP player, DamageSource par1DamageSource, float par2);

    void knockBack(EntityPlayerMP player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ);

    boolean isSpectator(EntityPlayerMP player);

    void setNoClip(EntityPlayerMP player, boolean noClip);
}
