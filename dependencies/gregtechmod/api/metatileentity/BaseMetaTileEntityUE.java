package gregtechmod.api.metatileentity;
/*
import gregtechmod.api.interfaces.IUETileEntity;
import gregtechmod.api.util.GT_Utility;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import universalelectricity.compatibility.Compatibility;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.core.block.IConductor;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.grid.IElectricityNetwork;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
*/
/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * This file contains all the needed 'implements' of the Interfaces for the Universal Electricity Stuff.
 * 
 * All of this is broken and since UE Energy is now BC Energy, I can't keep up the native compatibility without Upgrades. :(
 */
public class BaseMetaTileEntityUE extends BaseMetaTileEntity /*implements IUETileEntity*/ {
	/*
	public BaseMetaTileEntityUE() {
		super();
	}
	
	@Override
	public void updateStatus() {
		super.updateStatus();
		if (isServerSide()) {
			inputUE();
			outputUE();
		}
	}
	
	@Override
	public void chargeItem(ItemStack aStack) {
		super.chargeItem(aStack);
		double tEnergy = ElectricItemHelper.chargeItem(aStack, getJoules(), getVoltage());
		if (tEnergy > 0) setJoules(getJoules() - tEnergy);
	}
	
	@Override
	public void dischargeItem(ItemStack aStack) {
		super.dischargeItem(aStack);
		double tEnergy = ElectricItemHelper.dechargeItem(aStack, getMaxJoules() - getJoules(), getVoltage());
		if (tEnergy > 0) setJoules(getJoules() + tEnergy);
	}
	
	private void inputUE() {
		double tEnergy = consumeFromMultipleSides(this, getUEConsumingSides(), new ElectricityPack((getMaxJoules() - getJoules()) / getVoltage(), getVoltage())).getWatts();
		if (tEnergy > 0) {
			setJoules(getJoules() + tEnergy);
			mAverageEUInput[mAverageEUInputIndex]+=(tEnergy/Compatibility.IC2_RATIO);
		}
	}
	
	private void outputUE() {
		EnumSet<ForgeDirection> tOutputs = getUEProducingSides();
		for (ForgeDirection tOutputSide : ForgeDirection.VALID_DIRECTIONS) {
			IElectricityNetwork tOutputNetwork = ElectricityNetworkHelper.getNetworkFromTileEntity(VectorHelper.getConnectorFromSide(worldObj, new Vector3(this), tOutputSide), tOutputSide);
			if (tOutputNetwork != null) {
				double tOutputWatts = Math.min(tOutputNetwork.getRequest(this).getWatts(), Math.min(getJoules()-getMetaTileEntity().getMinimumStoredEU()*Compatibility.IC2_RATIO, getVoltage()*100));
				if (getJoules() > 0 && tOutputWatts > 0 && tOutputs.contains(tOutputSide)) {
					tOutputNetwork.startProducing(this, tOutputWatts / getVoltage(), getVoltage());
					setJoules(getJoules() - tOutputWatts);
					mAverageEUOutput[mAverageEUOutputIndex]+=(tOutputWatts/Compatibility.IC2_RATIO);
				} else {
					tOutputNetwork.stopProducing(this);
				}
				return;
			}
		}
	}
	
	public static ElectricityPack consumeFromMultipleSides(TileEntity tileEntity, EnumSet<ForgeDirection> approachingDirection, ElectricityPack requestPack) {
		ElectricityPack consumedPack = new ElectricityPack();
		if (tileEntity != null && approachingDirection != null && !approachingDirection.isEmpty()) {
			final List<IElectricityNetwork> connectedNetworks = ElectricityNetworkHelper.getNetworksFromMultipleSides(tileEntity, approachingDirection);
			if (connectedNetworks.size() > 0) {
				double wattsPerSide = (requestPack.getWatts() / connectedNetworks.size());
				double voltage = requestPack.voltage;
				for (IElectricityNetwork network : connectedNetworks) {
					if (wattsPerSide > 0 && requestPack.getWatts() > 0) {
						network.startRequesting(tileEntity, wattsPerSide / voltage, voltage);
						ElectricityPack receivedPack = network.consumeElectricity(tileEntity);
						consumedPack.amperes += receivedPack.amperes;
						consumedPack.voltage = Math.max(consumedPack.voltage, receivedPack.voltage);
					} else {
						network.stopRequesting(tileEntity);
					}
				}
			}
		}
		return consumedPack;
	}
	
	private EnumSet<ForgeDirection> getUEConsumingSides() {
		EnumSet<ForgeDirection> rSides = EnumSet.noneOf(ForgeDirection.class);
		for (byte i = 0; i < 6; i++) {
			if (inputEnergyFrom(i)) {
				Object tTileEntity = getTileEntityAtSide(i);
				if (tTileEntity != null && tTileEntity instanceof IConductor) {
					rSides.add(ForgeDirection.getOrientation(i));
				}
			}
		}
		return rSides;
	}
	
	private EnumSet<ForgeDirection> getUEProducingSides() {
		EnumSet<ForgeDirection> rSides = EnumSet.noneOf(ForgeDirection.class);
		for (byte i = 0; i < 6; i++) {
			if (outputsEnergyTo(i)) {
				Object tTileEntity = getTileEntityAtSide(i);
				if (tTileEntity != null && tTileEntity instanceof IConductor) {
					rSides.add(ForgeDirection.getOrientation(i));
				}
			}
		}
		return rSides;
	}
	
	@Override
	public float getVoltage() {
		return (float)(60*Math.pow(2, GT_Utility.getTier(getOutputVoltage()*getOutputAmperage())-1));
	}
	
	@Override
	public double getJoules() {
		return hasValidMetaTileEntity()?getMetaTileEntity().getEUVar()*Compatibility.IC2_RATIO+mRestJoules:0.0;
	}
	
	@Override
	public boolean canConnect(ForgeDirection aSide) {
		return inputEnergyFrom((byte)aSide.ordinal()) || outputsEnergyTo((byte)aSide.ordinal());
	}
	
	@Override
	public void setEnergyStored(float aEnergy) {
		if (hasValidMetaTileEntity()) {
			if (aEnergy < 0) aEnergy = 0;
			int tEnergy = (int)(aEnergy/Compatibility.IC2_RATIO);
			mRestJoules = aEnergy - (long)(tEnergy*(long)Compatibility.IC2_RATIO);
			getMetaTileEntity().setEUVar(tEnergy);
		}
	}
	
	@Override
	public float getMaxEnergyStored() {
		return getEUCapacity()*Compatibility.IC2_RATIO;
	}
	
	@Override
	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive) {
		return 0;
	}
	
	@Override
	public ElectricityPack provideElectricity(ForgeDirection from, ElectricityPack request, boolean doProvide) {
		return null;
	}
	
	@Override
	public float getRequest(ForgeDirection direction) {
		return 0;
	}
	
	@Override
	public float getProvide(ForgeDirection direction) {
		return 0;
	}*/
}