package micdoodle8.mods.galacticraft.core.entities;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
    public NetworkedEntity(World par1World)
    {
        super(par1World);
        
        if (par1World != null && par1World.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        PacketDynamic packet = new PacketDynamic(this);
        if (this.networkedDataChanged())
        {
            if (!this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(packet, new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, this.getPacketRange()));
            }
            else
            {
                GalacticraftCore.packetPipeline.sendToServer(packet);
            }
        }
    }

    public abstract boolean networkedDataChanged();

    public abstract double getPacketRange();
}
