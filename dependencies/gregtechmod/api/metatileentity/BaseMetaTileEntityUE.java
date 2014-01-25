//package gregtechmod.api.metatileentity;
//
//import gregtechmod.api.interfaces.IUETileEntity;
//
//import java.util.EnumSet;
//
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.common.ForgeDirection;
//import universalelectricity.compatibility.Compatibility;
//import universalelectricity.core.block.IConductor;
//import universalelectricity.core.electricity.ElectricityPack;
//import universalelectricity.core.item.ElectricItemHelper;
//
///**
// * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
// * 
// * This file contains all the needed 'implements' of the Interfaces for the Universal Electricity Stuff.
// * 
// * I am using the BC3 Ratio, since that is a constant in comparison to IC² Energy (1 MJ is always 2,5 EU).
// */
//public class BaseMetaTileEntityUE extends BaseMetaTileEntity implements IUETileEntity {
//	
//	public BaseMetaTileEntityUE() {
//		super();
//	}
//	
//	@Override
//	public void updateStatus() {
//		super.updateStatus();
//	}
//	
//	@Override
//	public void chargeItem(ItemStack aStack) {
//		super.chargeItem(aStack);
//		float tEnergy = ElectricItemHelper.chargeItem(aStack, (float)(getOfferedEnergy() * (Compatibility.BC3_RATIO * 0.4F)));
//		if (tEnergy > 0) decreaseStoredEU((int)(tEnergy / (Compatibility.BC3_RATIO * 0.4F)) + 1, true);
//	}
//	
//	@Override
//	public void dischargeItem(ItemStack aStack) {
//		super.dischargeItem(aStack);
//		float tEnergy = ElectricItemHelper.dischargeItem(aStack, (float)(demandedEnergyUnits() * (Compatibility.BC3_RATIO * 0.4F)));
//		if (tEnergy > 0) increaseStoredEnergyUnits((int)(tEnergy / (Compatibility.BC3_RATIO * 0.4F)), true);
//	}
//	
//	@Override
//	public float receiveElectricity(ForgeDirection aSide, ElectricityPack aPacket, boolean doReceive) {
//		if (!getUEConsumingSides().contains(aSide)) return 0;
//		int aInserted = (int)(aPacket.getWatts()/(Compatibility.BC3_RATIO * 0.4F));
//		if (doReceive) return injectEnergyUnits((byte)aSide.ordinal(), aInserted, 1) ? aInserted : 0;
//		return getEUCapacity() - getStoredEU() >= aInserted ? aInserted : 0;
//	}
//	
//	@Override
//	public ElectricityPack provideElectricity(ForgeDirection aSide, ElectricityPack aRequested, boolean doProvide) {
//		if (!getUEProducingSides().contains(aSide)) return null;
//		int aExtracted = (int)getOfferedEnergy();
//		if (doProvide) return aRequested.getWatts()/(Compatibility.BC3_RATIO * 0.4F) >= getOfferedEnergy() ? decreaseStoredEU(aExtracted, false) ? new ElectricityPack(aExtracted * (Compatibility.BC3_RATIO * 0.4F), 1) : null : decreaseStoredEU((int)(aRequested.getWatts()/(Compatibility.BC3_RATIO * 0.4F)), false) ? new ElectricityPack((int)(aRequested.getWatts()/(Compatibility.BC3_RATIO * 0.4F)) * (Compatibility.BC3_RATIO * 0.4F), 1) : null;
//		return aRequested.getWatts()/(Compatibility.BC3_RATIO * 0.4F) >= getOfferedEnergy() ? new ElectricityPack(aExtracted * (Compatibility.BC3_RATIO * 0.4F), 1) : new ElectricityPack((int)(aRequested.getWatts()/(Compatibility.BC3_RATIO * 0.4F)) * (Compatibility.BC3_RATIO * 0.4F), 1);
//	}
//	
//	@Override
//	public float getRequest(ForgeDirection aSide) {
//		if (!getUEConsumingSides().contains(aSide)) return 0;
//		return (float)(demandedEnergyUnits() * (Compatibility.BC3_RATIO * 0.4F));
//	}
//	
//	@Override
//	public float getProvide(ForgeDirection aSide) {
//		if (!getUEProducingSides().contains(aSide)) return 0;
//		return (float)(getOfferedEnergy() * (Compatibility.BC3_RATIO * 0.4F));
//	}
//	
//	@Override
//	public float getVoltage() {
//		return (float)(getOfferedEnergy() * (Compatibility.BC3_RATIO * 0.4F));
//	}
//	
//	@Override
//	public boolean canConnect(ForgeDirection aSide) {
//		return getUEProducingSides().contains(aSide) || getUEConsumingSides().contains(aSide);
//	}
//	
//	private EnumSet<ForgeDirection> getUEConsumingSides() {
//		EnumSet<ForgeDirection> rSides = EnumSet.noneOf(ForgeDirection.class);
//		for (byte i = 0; i < 6; i++) {
//			if (inputEnergyFrom(i)) {
//				Object tTileEntity = getTileEntityAtSide(i);
//				if (tTileEntity != null && tTileEntity instanceof IConductor) {
//					rSides.add(ForgeDirection.getOrientation(i));
//				}
//			}
//		}
//		return rSides;
//	}
//	
//	private EnumSet<ForgeDirection> getUEProducingSides() {
//		EnumSet<ForgeDirection> rSides = EnumSet.noneOf(ForgeDirection.class);
//		for (byte i = 0; i < 6; i++) {
//			if (outputsEnergyTo(i)) {
//				Object tTileEntity = getTileEntityAtSide(i);
//				if (tTileEntity != null && tTileEntity instanceof IConductor) {
//					rSides.add(ForgeDirection.getOrientation(i));
//				}
//			}
//		}
//		return rSides;
//	}
//}