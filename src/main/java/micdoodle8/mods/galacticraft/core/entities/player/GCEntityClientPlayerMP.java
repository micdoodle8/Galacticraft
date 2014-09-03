package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.Session;
import net.minecraft.world.World;

public class GCEntityClientPlayerMP extends EntityClientPlayerMP
{
    public GCEntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandler, StatFileWriter statFileWriter)
    {
        super(minecraft, world, session, netHandler, statFileWriter);
    }

    private IPlayerClient getClientHandler()
    {
        return ClientProxyCore.playerClientHandler;
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (!this.getClientHandler().wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return this.getClientHandler().isEntityInsideOpaqueBlock(this, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public void onLivingUpdate()
    {
        this.getClientHandler().onLivingUpdatePre(this);
        super.onLivingUpdate();
        this.getClientHandler().onLivingUpdatePost(this);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        this.getClientHandler().moveEntity(this, par1, par3, par5);
    }

    @Override
    public void onUpdate()
    {
        this.getClientHandler().onUpdate(this);
        super.onUpdate();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getBedOrientationInDegrees()
    {
        return this.getClientHandler().getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
    }

    public final GCPlayerStatsClient getPlayerStats()
    {
        return GCPlayerStatsClient.get(this);
    }

    public static GCPlayerStatsClient getPlayerStats(EntityClientPlayerMP player)
    {
        return GCPlayerStatsClient.get(player);
    }
}
