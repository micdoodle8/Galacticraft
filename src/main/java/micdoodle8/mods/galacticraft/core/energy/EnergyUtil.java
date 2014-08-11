package micdoodle8.mods.galacticraft.core.energy;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IStrictEnergyAcceptor;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerReceptor;

public class EnergyUtil
{

	public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
	{
		TileEntity[] adjacentConnections = new TileEntity[6];

		boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();
		//boolean isTELoaded = EnergyConfigHandler.isThermalExpansionLoaded();
		boolean isIC2Loaded = EnergyConfigHandler.isIndustrialCraft2Loaded();
		boolean isBCLoaded = EnergyConfigHandler.isBuildcraftLoaded();

		BlockVec3 thisVec = new BlockVec3(tile);
		for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

			if (tileEntity instanceof IConnector)
			{
				if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
				continue;
			}
			else if (isMekLoaded && (tileEntity instanceof IStrictEnergyAcceptor || tileEntity instanceof ICableOutputter))
			{
				//Do not connect GC wires directly to Mek Universal Cables
				try
				{
					if (Class.forName("codechicken.multipart.TileMultipart").isInstance(tileEntity))
						continue;
				} catch (Exception e) { e.printStackTrace(); }

				if (tileEntity instanceof IStrictEnergyAcceptor && ((IStrictEnergyAcceptor) tileEntity).canReceiveEnergy(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
				else if (tileEntity instanceof ICableOutputter && ((ICableOutputter) tileEntity).canOutputTo(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}
			/*else if (isTELoaded && tileEntity instanceof IEnergyHandler)
			{
				if (((IEnergyHandler) tileEntity).canInterface(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}*/
			else if (isIC2Loaded && tileEntity instanceof IEnergyTile)
			{
				if (tileEntity instanceof IEnergyConductor)
					continue;
				
				if (tileEntity instanceof IEnergyAcceptor)
				{
					if (((IEnergyAcceptor) tileEntity).acceptsEnergyFrom(tile, direction.getOpposite()))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
						continue;
					}
				}
				if (tileEntity instanceof IEnergyEmitter)
				{
					if (((IEnergyEmitter) tileEntity).emitsEnergyTo(tile, direction.getOpposite()))
					{
						adjacentConnections[direction.ordinal()] = tileEntity;
						continue;
					}
				}
			}
			else if (isBCLoaded)
			{
				//Do not connect GC wires to BC wooden power pipes
				try
				{
					Class<?> clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
					if (clazzPipeTile.isInstance(tileEntity))
					{				
						Class<?> clazzPipeWood = Class.forName("buildcraft.transport.pipes.PipePowerWood");
						Object pipe = clazzPipeTile.getField("pipe").get(tileEntity);
						if (clazzPipeWood.isInstance(pipe))
							continue;
					}
				} catch (Exception e) { e.printStackTrace(); }

				//New BC API
				if (EnergyConfigHandler.getBuildcraftVersion() == 6 && MjAPI.getMjBattery(tileEntity, MjAPI.DEFAULT_POWER_FRAMEWORK, direction.getOpposite()) != null)
					adjacentConnections[direction.ordinal()] = tileEntity;
				
				//Legacy BC API
				if (tileEntity instanceof IPowerReceptor && ((IPowerReceptor) tileEntity).getPowerReceiver(direction.getOpposite()) != null)
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}			
			}
		}

		return adjacentConnections;
	}
}
