package micdoodle8.mods.galacticraft.core.entities.player;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;

public class GCPlayerBaseSP extends ClientPlayerBase
{
    public GCPlayerBaseSP(ClientPlayerAPI playerAPI)
    {
        super(playerAPI);
    }

    private IPlayerClient getClientHandler()
    {
        return ClientProxyCore.playerClientHandler;
    }

//    @Override
//    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
//    {
//        if (!this.getClientHandler().wakeUpPlayer(this, par1, par2, par3))
//        {
//            super.wakeUpPlayer(par1, par2, par3);
//        }
//    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return this.getClientHandler().isEntityInsideOpaqueBlock(this.player, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public void onLivingUpdate()
    {
        this.getClientHandler().onLivingUpdatePre(this.player);
        super.onLivingUpdate();
        this.getClientHandler().onLivingUpdatePost(this.player);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        this.getClientHandler().moveEntity(this.player, par1, par3, par5);
    }

    @Override
    public void onUpdate()
    {
        this.getClientHandler().onUpdate(this.player);
        super.onUpdate();
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public float getBedOrientationInDegrees()
//    {
//        return this.getClientHandler().getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
//    }
}
