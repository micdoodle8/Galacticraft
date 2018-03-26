package micdoodle8.mods.galacticraft.core.energy.tile;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.EnergizedItemManager;
import mekanism.api.energy.IEnergizedItem;
import micdoodle8.mods.galacticraft.api.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.grid.IElectricityNetwork;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.EnumSet;

public class TileBaseUniversalElectricalSource extends TileBaseUniversalElectrical
{
    /*
     * The main function to output energy each tick from a source.
     *
     * The source will attempt to produce into its outputDirections whatever energy
     * it has available, and will reduce its stored energy by the amount which is in fact used.
     *
     * Max output = this.storage.maxExtract.
     *
     * @return The amount of energy that was used.
     */
    public float produce()
    {
        this.storage.maxExtractRemaining = this.storage.maxExtract;
        float produced = this.extractEnergyGC(null, this.produce(false), false);
        this.storage.maxExtractRemaining -= produced;
        if (this.storage.maxExtractRemaining < 0)
        {
            this.storage.maxExtractRemaining = 0;
        }
        return produced;
    }

    /*
     * Function to produce energy each tick into the outputs of a source.
     * If simulate is true, no energy is in fact transferred.
     *
     * Note: even if simulate is false this does NOT reduce the source's own
     * energy storage by the amount produced, that needs to be done elsewhere
     * See this.produce() for an example.
     */
    public float produce(boolean simulate)
    {
        float amountProduced = 0;

        if (!this.worldObj.isRemote)
        {
            EnumSet<EnumFacing> outputDirections = this.getElectricalOutputDirections();
//            outputDirections.remove(EnumFacing.UNKNOWN);

            BlockVec3 thisVec = new BlockVec3(this);
            for (EnumFacing direction : outputDirections)
            {
                TileEntity tileAdj = thisVec.getTileEntityOnSide(this.worldObj, direction);

                if (tileAdj != null)
                {
                    float toSend = this.extractEnergyGC(null, Math.min(this.getEnergyStoredGC() - amountProduced, this.getEnergyStoredGC() / outputDirections.size()), true);
                    if (toSend <= 0)
                    {
                        continue;
                    }

                    if (tileAdj instanceof TileBaseConductor && ((TileBaseConductor)tileAdj).canConnect(direction.getOpposite(), NetworkType.POWER))
                    {
                        IElectricityNetwork network = ((IConductor) tileAdj).getNetwork();
                        if (network != null)
                        {
                            amountProduced += (toSend - network.produce(toSend, !simulate, this.tierGC, this));
                        }
                    }
                    else if (tileAdj instanceof TileBaseUniversalElectrical)
                    {
                        amountProduced += ((TileBaseUniversalElectrical) tileAdj).receiveElectricity(direction.getOpposite(), toSend, this.tierGC, !simulate);
                    }
                    else
                    {
                        amountProduced += EnergyUtil.otherModsEnergyTransfer(tileAdj, direction.getOpposite(), toSend, simulate);
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
        if (itemStack != null && itemStack.stackSize == 1)
        {
            Item item = itemStack.getItem();
            float maxExtractSave = this.storage.getMaxExtract();
            if (this.tierGC > 1)
            {
                this.storage.setMaxExtract(maxExtractSave * 2.5F);
            }
            float energyToCharge = this.storage.extractEnergyGC(this.storage.getMaxExtract(), true);

            if (item instanceof IItemElectric)
            {
                this.storage.extractEnergyGC(ElectricItemHelper.chargeItem(itemStack, energyToCharge), false);
            }
//            else if (EnergyConfigHandler.isRFAPILoaded() && item instanceof IEnergyContainerItem)
//            {
//                this.storage.extractEnergyGC(((IEnergyContainerItem)item).receiveEnergy(itemStack, (int) (energyToCharge * EnergyConfigHandler.TO_RF_RATIO), false) / EnergyConfigHandler.TO_RF_RATIO, false);
//            }
            else if (EnergyConfigHandler.isMekanismLoaded() && item instanceof IEnergizedItem && ((IEnergizedItem) item).canReceive(itemStack))
            {
                this.storage.extractEnergyGC((float) EnergizedItemManager.charge(itemStack, energyToCharge * EnergyConfigHandler.TO_MEKANISM_RATIO) / EnergyConfigHandler.TO_MEKANISM_RATIO, false);
            }
            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
            {
                if (item instanceof ISpecialElectricItem)
                {
                    ISpecialElectricItem specialElectricItem = (ISpecialElectricItem) item;
                    IElectricItemManager manager = specialElectricItem.getManager(itemStack);
                    double result = manager.charge(itemStack, (double) (energyToCharge * EnergyConfigHandler.TO_IC2_RATIO), this.tierGC + 1, false, false);
                    float energy = (float) result / EnergyConfigHandler.TO_IC2_RATIO;
                    this.storage.extractEnergyGC(energy, false);
                }
                else if (item instanceof IElectricItem)
                {
                    double result = ElectricItem.manager.charge(itemStack, (double) (energyToCharge * EnergyConfigHandler.TO_IC2_RATIO), this.tierGC + 1, false, false);
                    float energy = (float) result / EnergyConfigHandler.TO_IC2_RATIO;
                    this.storage.extractEnergyGC(energy, false);
                }
            }
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				int accepted = ((IEnergyContainerItem) itemStack.getItem()).receiveEnergy(itemStack, (int) Math.floor(this.getProvide(EnumFacing.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.provideElectricity(accepted * EnergyConfigHandler.TE_RATIO, true);
            //			}

            if (this.tierGC > 1)
            {
                this.storage.setMaxExtract(maxExtractSave);
            }
        }
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = "IC2")
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction)
    {
        //Don't add connection to IC2 grid if it's a Galacticraft tile
        if (receiver instanceof IElectrical || receiver instanceof IConductor || !(receiver instanceof IEnergyTile))
        {
            return false;
        }

        return this.getElectricalOutputDirections().contains(direction);
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
    public double getOfferedEnergy()
    {
        if (EnergyConfigHandler.disableIC2Output)
        {
            return 0.0;
        }

        return this.getProvide(null) * EnergyConfigHandler.TO_IC2_RATIO;
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
    public void drawEnergy(double amount)
    {
        if (EnergyConfigHandler.disableIC2Output)
        {
            return;
        }

        this.storage.extractEnergyGC((float) amount / EnergyConfigHandler.TO_IC2_RATIO, false);
    }

    @Annotations.RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySource", modID = "IC2")
    public int getSourceTier()
    {
        return this.tierGC + 1;
    }

    @Override
    @Annotations.RuntimeInterface(clazz = "mekanism.api.energy.ICableOutputter", modID = "Mekanism")
    public boolean canOutputTo(EnumFacing side)
    {
        return this.getElectricalOutputDirections().contains(side);
    }

    @Override
    public float getProvide(EnumFacing direction)
    {
        if (direction == null && EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, this.getElectricOutputDirection());
            if (tile instanceof IConductor)
            {
                //No power provide to IC2 mod if it's a Galacticraft wire on the output.  Galacticraft network will provide the power.
                return 0.0F;
            }
            return this.storage.extractEnergyGC(Float.MAX_VALUE, true);
        }

        if (this.getElectricalOutputDirections().contains(direction))
        {
            return this.storage.extractEnergyGC(Float.MAX_VALUE, true);
        }

        return 0F;
    }

    public EnumFacing getElectricOutputDirection()
    {
        return null;
    }

    @Annotations.RuntimeInterface(clazz = "cofh.api.energy.IEnergyProvider", modID = "")
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
    {
        if (EnergyConfigHandler.disableRFOutput)
        {
            return 0;
        }

        if (!this.getElectricalOutputDirections().contains(from))
        {
            return 0;
        }

        return MathHelper.floor_float(this.storage.extractEnergyGC(maxExtract / EnergyConfigHandler.TO_RF_RATIO, !simulate) * EnergyConfigHandler.TO_RF_RATIO);
    }
}
