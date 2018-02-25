package micdoodle8.mods.galacticraft.core.entities.player;

import api.player.server.ServerPlayerAPI;
import api.player.server.ServerPlayerBase;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.DamageSource;

public class GCPlayerBaseMP extends ServerPlayerBase
{
    public GCPlayerBaseMP(ServerPlayerAPI playerAPI)
    {
        super(playerAPI);
    }

    private IPlayerServer getClientHandler()
    {
        return GalacticraftCore.proxy.player;
    }

//    @Override
//    public void updateRidden()
//    {
//        this.getClientHandler().updateRiddenPre(this);
//        super.updateRidden();
//        this.getClientHandler().updateRiddenPost(this);
//    }
//
//    @Override
//    public void mountEntity(Entity par1Entity)
//    {
//        if (!this.getClientHandler().mountEntity(this, par1Entity))
//        {
//            super.mountEntity(par1Entity);
//        }
//    }


    @Override
    public void moveEntity(MoverType moverType, double x, double y, double z)
    {
        super.moveEntity(moverType, x, y, z);
        this.getClientHandler().move(this.player, moverType, x, y, z);
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (!this.getClientHandler().wakeUpPlayer(this.player, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        par2 = this.getClientHandler().attackEntityFrom(this.player, par1DamageSource, par2);

        if (par2 == -1)
        {
            return false;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ)
    {
        this.getClientHandler().knockBack(this.player, p_70653_1_, p_70653_2_, impulseX, impulseZ);
    }
}