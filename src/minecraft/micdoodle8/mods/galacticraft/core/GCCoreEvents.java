package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.dimension.GCCoreTeleporter;
import net.minecraft.block.BlockSapling;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.WorldEvent;

public class GCCoreEvents
{
	@ForgeSubscribe
	public void growTreeBonemeal(BonemealEvent event)
	{
		if (event.world.getBlockId(event.X, event.Y, event.Z) == GCCoreBlocks.sapling.blockID)
		{
			if (!event.world.isRemote)
			{
                ((BlockSapling)GCCoreBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
                --event.entityPlayer.inventory.getCurrentItem().stackSize;
				event.setResult(Result.DENY);
			}
		}
		
		event.setResult(Result.DENY);
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load event)
	{
		if (event.world instanceof WorldServer)
		{
			((WorldServer)event.world).customTeleporters.add(new GCCoreTeleporter((WorldServer)event.world));
		}
	}
	
	@ForgeSubscribe
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if (event.world.provider.dimensionId == 0)
		{
			GalacticraftCore.playersServer.clear();
		}
	}
}
