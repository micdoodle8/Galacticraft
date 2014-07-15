package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.power.EnergySource.EnergySourceAdjacent;
import micdoodle8.mods.galacticraft.api.power.IEnergyHandlerGC;
import micdoodle8.mods.galacticraft.api.transmission.compatibility.NetworkConfigHandler;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.transmission.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Method;
import java.util.EnumSet;

public class TileEntityUniversalElectricalSource extends TileEntityUniversalElectrical
{
	public float produce()
	{
		return this.extractEnergyGC(null, this.produce(false), false);
	}

	public float produce(boolean simulate)
	{
		int amountProduced = 0;

		if (!this.worldObj.isRemote)
		{
			EnumSet<ForgeDirection> outputDirections = this.getElectricalOutputDirections();
			outputDirections.remove(ForgeDirection.UNKNOWN);

			BlockVec3 thisVec = new BlockVec3(this);
			for (ForgeDirection direction : outputDirections)
			{
				TileEntity tileAdj = thisVec.getTileEntityOnSide(this.worldObj, direction);

				if (tileAdj != null)
				{
					if (tileAdj instanceof TileEntityConductor)
					{
						IElectricityNetwork network = ((IConductor) tileAdj).getNetwork();
						if (network != null)
						{
                            float toSend = this.extractEnergyGC(null, this.getEnergyStoredGC() - amountProduced, true);

                            if (toSend > 0)
                            {
                                amountProduced += (toSend - network.produce(toSend, true, this));
                            }
						}
					}
					else if (tileAdj instanceof IEnergyHandlerGC)
					{
						EnergySourceAdjacent source = new EnergySourceAdjacent(direction.getOpposite());
						amountProduced += ((IEnergyHandlerGC) tileAdj).receiveEnergyGC(source, (this.getEnergyStoredGC() - amountProduced) / outputDirections.size(), simulate);
					}
				}
			}
		}

		return amountProduced;
	}

	/**
	 * Recharges electric item.
	 */
	public void recharge(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			Item item = itemStack.getItem();
			float energyToCharge = this.storage.extractEnergyGC(this.storage.getMaxExtract(), true);

			if (item instanceof IItemElectric)
			{
				this.storage.extractEnergyGC(ElectricItemHelper.chargeItem(itemStack, energyToCharge), false);
			}
			else if (NetworkConfigHandler.isIndustrialCraft2Loaded())
			{
				try {
					Class<?> itemElectricIC2 = Class.forName("ic2.api.item.ISpecialElectricItem");
					Class<?> itemElectricIC2B = Class.forName("ic2.api.item.IElectricItem");
					Class<?> itemManagerIC2 = Class.forName("ic2.api.item.IElectricItemManager");
					if (itemElectricIC2.isInstance(item))
					{
						Object IC2item = itemElectricIC2.cast(item);
						Method getMan = itemElectricIC2.getMethod("getManager", ItemStack.class);
						Object IC2manager = getMan.invoke(IC2item, itemStack);
						//For 1.7.10 - Method methodCharge = itemManagerIC2.getMethod("charge", ItemStack.class, double.class, int.class, boolean.class, boolean.class);
						//For 1.7.10 - double result = (Double) methodCharge.invoke(IC2manager, itemStack, energyToCharge * NetworkConfigHandler.TO_IC2_RATIO, 4, false, false);
						Method methodCharge = itemManagerIC2.getMethod("charge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
						int result = (Integer) methodCharge.invoke(IC2manager, itemStack, (int) (energyToCharge * NetworkConfigHandler.TO_IC2_RATIO), 4, false, false);
						//float energy = (float) ((ISpecialElectricItem)item).getManager(itemStack).charge(itemStack, energyToCharge * NetworkConfigHandler.TO_IC2_RATIO, 4, false, false) * NetworkConfigHandler.IC2_RATIO;
						float energy = result * NetworkConfigHandler.IC2_RATIO;
						this.storage.extractEnergyGC(energy, false);
					}
					else if (itemElectricIC2B.isInstance(item))
					{
						Class<?> electricItemIC2 = Class.forName("ic2.api.item.ElectricItem");
						Object IC2manager = electricItemIC2.getField("manager").get(null);
						//For 1.7.10 - Method methodCharge = itemManagerIC2.getMethod("charge", ItemStack.class, double.class, int.class, boolean.class, boolean.class);
						//For 1.7.10 - double result = (Double) methodCharge.invoke(IC2manager, itemStack, energyToCharge * NetworkConfigHandler.TO_IC2_RATIO, 4, false, false);
						Method methodCharge = itemManagerIC2.getMethod("charge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
						int result = (Integer) methodCharge.invoke(IC2manager, itemStack, (int) (energyToCharge * NetworkConfigHandler.TO_IC2_RATIO), 4, false, false);
						//float energy = (float) ((ISpecialElectricItem)item).getManager(itemStack).charge(itemStack, energyToCharge * NetworkConfigHandler.TO_IC2_RATIO, 4, false, false) * NetworkConfigHandler.IC2_RATIO;
						float energy = result * NetworkConfigHandler.IC2_RATIO;
						this.storage.extractEnergyGC(energy, false);				
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			//			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
			//			{
			//				int accepted = ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) Math.floor(this.getProvide(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_TE_RATIO), false);
			//				this.provideElectricity(accepted * NetworkConfigHandler.TE_RATIO, true);
			//			}
		}
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = "IC2")
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		//Don't add connection to IC2 grid if it's a Galacticraft tile
		if (receiver instanceof IElectrical || receiver instanceof IConductor)
			return false;

		try {
			Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
			if (!energyTile.isInstance(receiver)) return false; 
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return this.getElectricalOutputDirections().contains(direction);
	}
	
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public double getOfferedEnergy()
	{
		return this.getProvide(ForgeDirection.UNKNOWN) * NetworkConfigHandler.TO_IC2_RATIO;
	}

	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public void drawEnergy(double amount)
	{
		this.storage.extractEnergyGC((float) amount * NetworkConfigHandler.IC2_RATIO, false);
	}

	//This is code for 1.7.10:
	/*	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public void drawEnergy(double energy)
	{
		return;
	}
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public double getOfferedEnergy()
	{
		return this.getNetwork().totalAvailable;
	}

	
	@RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
	public int getSourceTier()
	{
		return 1;
	}
	*/
	
	//	@Override
	//	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide)
	//	{
	//		if (this.getElectricalOutputDirections().contains(from))
	//		{
	//			if (!doProvide)
	//			{
	//				return ElectricityPack.getFromWatts(this.getProvide(from), this.getVoltage());
	//			}
	//
	//			return this.provideElectricity(request, doProvide);
	//		}
	//
	//		return new ElectricityPack();
	//	}
	/**
	 * A non-side specific version of provideElectricity for you to optionally
	 * use it internally.
	 */
	//	public ElectricityPack provideElectricity(ElectricityPack request, boolean doProvide)
	//	{
	//		if (request != null)
	//		{
	//			float requestedEnergy = Math.min(request.getWatts(), this.energyStored);
	//
	//			if (doProvide)
	//			{
	//				this.setEnergyStored(this.energyStored - requestedEnergy);
	//			}
	//
	//			return ElectricityPack.getFromWatts(requestedEnergy, this.getVoltage());
	//		}
	//
	//		return new ElectricityPack();
	//	}

	//	public ElectricityPack provideElectricity(float energy, boolean doProvide)
	//	{
	//		return this.provideElectricity(ElectricityPack.getFromWatts(energy, this.getVoltage()), doProvide);
	//	}
	
	//	public void produce()
	//	{
	//		if (!this.worldObj.isRemote)
	//		{
	//			for (ForgeDirection outputDirection : this.getElectricalOutputDirections())
	//			{
	//				if (outputDirection != ForgeDirection.UNKNOWN)
	//				{
	//					if (!this.produceUE(outputDirection))
	//					{
	//						this.produceExternal(outputDirection);
	//					}
	//				}
	//			}
	//		}
	//	}

	//	public boolean produceUE(ForgeDirection outputDirection)
	//	{
	//		if (!this.worldObj.isRemote && outputDirection != null && outputDirection != ForgeDirection.UNKNOWN)
	//		{
	//			float provide = this.getProvide(outputDirection);
	//
	//			if (provide > 0)
	//			{
	//				Vector3 thisVec = new Vector3(this);
	//				TileEntity outputTile = thisVec.modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
	//				IElectricityNetwork outputNetwork = NetworkHelper.getElectricalNetworkFromTileEntity(outputTile, outputDirection);
	//
	//				if (outputNetwork != null)
	//				{
	//					ElectricityPack powerRequest = outputNetwork.getRequest(this);
	//
	//					if (powerRequest.getWatts() > 0)
	//					{
	//						ElectricityPack sendPack = ElectricityPack.min(ElectricityPack.getFromWatts(this.getEnergyStored(), this.getVoltage()), ElectricityPack.getFromWatts(provide, this.getVoltage()));
	//						float rejectedPower = outputNetwork.produce(sendPack, true, this);
	//						this.provideElectricity(Math.max(sendPack.getWatts() - rejectedPower, 0), true);
	//						return true;
	//					}
	//				}
	//				else if (outputTile instanceof IElectrical)
	//				{
	//					float requestedEnergy = ((IElectrical) outputTile).getRequest(outputDirection.getOpposite());
	//
	//					if (requestedEnergy > 0)
	//					{
	//						ElectricityPack sendPack = ElectricityPack.min(ElectricityPack.getFromWatts(this.getEnergyStored(), this.getVoltage()), ElectricityPack.getFromWatts(provide, this.getVoltage()));
	//						float acceptedEnergy = ((IElectrical) outputTile).receiveElectricity(outputDirection.getOpposite(), sendPack, true);
	//						this.provideElectricity(acceptedEnergy, true);
	//						return true;
	//					}
	//				}
	//			}
	//		}
	//
	//		return false;
	//	}

	//	public boolean produceExternal(ForgeDirection outputDirection)
	//	{
	//		if (!this.worldObj.isRemote && outputDirection != null && outputDirection != ForgeDirection.UNKNOWN)
	//		{
	//			float provide = this.getProvide(outputDirection);
	//
	//			if (this.getEnergyStored() >= provide && provide > 0)
	//			{
	//				TileEntity adjacentEntity = new Vector3(this).modifyPositionFromSide(outputDirection).getTileEntity(this.worldObj);
	//			
	//				if (NetworkConfigHandler.isThermalExpansionLoaded())
	//				{
	//					if (adjacentEntity instanceof IEnergyHandler)
	//					{
	//						int teProvide = (int) Math.floor(provide * NetworkConfigHandler.TO_TE_RATIO);
	//						int energyUsed = Math.min(((IEnergyHandler) adjacentEntity).receiveEnergy(outputDirection.getOpposite(), teProvide, false), teProvide);
	//						this.provideElectricity(energyUsed * NetworkConfigHandler.TE_RATIO, true);
	//						return true;
	//					}
	//				}
	//		
	////				if (NetworkConfigHandler.isBuildcraftLoaded())
	////				{
	////					if (adjacentEntity instanceof IPowerReceptor)
	////					{
	////						PowerReceiver receiver = ((IPowerReceptor) adjacentEntity).getPowerReceiver(outputDirection.getOpposite());
	////
	////						if (receiver != null)
	////						{
	////							if (receiver.powerRequest() > 0)
	////							{
	////								float bc3Provide = provide * NetworkConfigHandler.TO_BC_RATIO;
	////								float energyUsed = Math.min(receiver.receiveEnergy(Type.MACHINE, bc3Provide, outputDirection.getOpposite()), bc3Provide);
	////								this.provideElectricity(energyUsed * NetworkConfigHandler.BC3_RATIO, true);
	////							}
	////						}
	////
	////						return true;
	////					}
	////				}
	//			}
	//		}
	//
	//		return false;
	//	}

	@Override
	public float getProvide(ForgeDirection direction)
	{
		if (direction == ForgeDirection.UNKNOWN && NetworkConfigHandler.isIndustrialCraft2Loaded())
		{
			TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, this.getElectricalOutputDirectionMain());
			if (tile instanceof IConductor)
			{
				//No power provide to IC2 mod if it's a Galacticraft wire on the output.  Galacticraft network will provide the power.
				return 0.0F;
			}
		}

		if (this.getElectricalOutputDirections().contains(direction))
			return this.storage.extractEnergyGC(Float.MAX_VALUE, true);

		return 0F;
	}

	public ForgeDirection getElectricalOutputDirectionMain()
	{
		return ForgeDirection.UNKNOWN;
	}
	
}
