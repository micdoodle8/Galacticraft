package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public interface IPlayerServer
{
    void updateRiddenPre(EntityPlayerMP player);

    void updateRiddenPost(EntityPlayerMP player);

    boolean dismountEntity(EntityPlayerMP player, Entity par1Entity);

    void move(EntityPlayerMP player, MoverType type, double x, double y, double z);

    boolean wakeUpPlayer(EntityPlayerMP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn);

    float attackEntityFrom(EntityPlayerMP player, DamageSource par1DamageSource, float par2);

    void knockBack(EntityPlayerMP player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ);

    boolean isSpectator(EntityPlayerMP player);

    void setNoClip(EntityPlayerMP player, boolean noClip);
}
