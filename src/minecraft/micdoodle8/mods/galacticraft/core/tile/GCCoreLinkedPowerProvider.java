package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

public class GCCoreLinkedPowerProvider extends PowerProvider
{
	public GCCoreTileEntityElectric electricTile;

	public GCCoreLinkedPowerProvider(GCCoreTileEntityElectric tile)
	{
		this.electricTile = tile;
	}

//	@Override
//	public boolean update(IPowerReceptor receptor)
//	{
//		return true;
//	}
//
//	@Override
//	public float useEnergy(float min, float max, boolean doUse)
//	{
//		float result = 0;
//
//		if(this.electricTile.bcEnergy * GalacticraftCore.toBuildcraftEnergyScalar >= min)
//		{
//			if(this.electricTile.bcEnergy * GalacticraftCore.toBuildcraftEnergyScalar <= max)
//			{
//				result = (float)(this.electricTile.bcEnergy * GalacticraftCore.toBuildcraftEnergyScalar);
//
//				if(doUse)
//				{
//					this.electricTile.bcEnergy = 0;
//				}
//			}
//			else
//			{
//				result = max;
//
//				if(doUse)
//				{
//					this.electricTile.bcEnergy -= max * GalacticraftCore.fromBuildcraftEnergyScalar;
//				}
//			}
//		}
//
//		return result;
//	}
//
//	@Override
//	public void receiveEnergy(float quantity, ForgeDirection from)
//	{
//		this.electricTile.bcEnergy = Math.max(Math.min(this.electricTile.bcEnergy + quantity * GalacticraftCore.fromBuildcraftEnergyScalar, this.electricTile.maxEnergy), 0);
//	}
}