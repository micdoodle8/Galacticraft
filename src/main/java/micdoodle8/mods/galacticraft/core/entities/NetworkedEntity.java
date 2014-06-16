package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;



public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
	public NetworkedEntity(World par1World)
	{
		super(par1World);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, this.getPacketRange()));
			// PacketDispatcher.sendPacketToAllAround(this.posX, this.posY,
			// this.posZ, this.getPacketRange(),
			// this.worldObj.provider.dimensionId,
			// GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
			// this, this.getNetworkedData(new ArrayList<Object>())));
		}
	}

	public abstract double getPacketRange();
}
