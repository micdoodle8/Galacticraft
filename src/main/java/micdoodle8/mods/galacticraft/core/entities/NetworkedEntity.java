package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
    public NetworkedEntity(EntityType<?> type, World world)
    {
        super(type, world);

        if (world != null && world.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    @Override
    public void tick()
    {
        super.tick();

        PacketDynamic packet = new PacketDynamic(this);
        if (this.networkedDataChanged())
        {
            if (!this.world.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(packet, new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), this.getPacketRange(), GCCoreUtil.getDimensionType(this.world)));
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
