package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

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
            if (!this.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(packet, new TargetPoint(GCCoreUtil.getDimensionID(this.world), this.posX, this.posY, this.posZ, this.getPacketRange()));
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
