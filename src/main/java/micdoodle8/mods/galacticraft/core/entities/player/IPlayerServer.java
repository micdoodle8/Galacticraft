package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;

public interface IPlayerServer
{
    public void clonePlayer(EntityPlayerMP player, EntityPlayer oldPlayer, boolean keepInv);

    public void updateRiddenPre(EntityPlayerMP player);

    public void updateRiddenPost(EntityPlayerMP player);

    public boolean mountEntity(EntityPlayerMP player, Entity par1Entity);

    public void moveEntity(EntityPlayerMP player, double par1, double par3, double par5);

    public boolean wakeUpPlayer(EntityPlayerMP player, boolean par1, boolean par2, boolean par3);

    public float attackEntityFrom(EntityPlayerMP player, DamageSource par1DamageSource, float par2);

    public void knockBack(EntityPlayerMP player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ);
}
