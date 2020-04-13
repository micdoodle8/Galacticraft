package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.Sets;
import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkProvider;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockSolarArrayController;
import micdoodle8.mods.galacticraft.planets.venus.dimension.WorldProviderVenus;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class TileEntitySolarArrayController extends TileBaseUniversalElectricalSource implements IDisableableMachine, IInventoryDefaults, ISidedInventory, IConnector
{
    private int solarStrength = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;
    public static final int MAX_GENERATE_WATTS = 1000;
    @NetworkedField(targetSide = Side.CLIENT)
    public int generateWatts = 0;
    private Set<ITransmitter> solarArray = Sets.newHashSet();
    @NetworkedField(targetSide = Side.CLIENT)
    public int connectedInfo = 0;

    private boolean initialised = false;

    public TileEntitySolarArrayController()
    {
        super("container.solar_array_controller.name");
        this.storage.setMaxExtract(TileEntitySolarArrayController.MAX_GENERATE_WATTS);
        this.storage.setMaxReceive(TileEntitySolarArrayController.MAX_GENERATE_WATTS);
        this.storage.setCapacity(50000);
        this.initialised = true;
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            this.storage.setCapacity(50000);
            this.initialised = true;
        }

        if (!this.world.isRemote)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);

            EnumSet<EnumFacing> outputDirections = EnumSet.noneOf(EnumFacing.class);
            outputDirections.addAll(Arrays.asList(EnumFacing.HORIZONTALS));
            outputDirections.removeAll(this.getElectricalOutputDirections());

            BlockVec3 thisVec = new BlockVec3(this);
            solarArray.clear();
            for (EnumFacing direction : outputDirections)
            {
                TileEntity tileAdj = thisVec.getTileEntityOnSide(this.world, direction);

                if (tileAdj != null)
                {
                    if (tileAdj instanceof INetworkProvider)
                    {
                        if (tileAdj instanceof ITransmitter)
                        {
                            if (((ITransmitter) tileAdj).canConnect(direction.getOpposite(), NetworkType.SOLAR_MODULE))
                            {
                                if (((INetworkProvider) tileAdj).getNetwork() instanceof SolarModuleNetwork)
                                {
                                    solarArray.addAll(((SolarModuleNetwork) ((INetworkProvider) tileAdj).getNetwork()).getTransmitters());
                                }
                            }
                        }
                        else
                        {
                            if (((INetworkProvider) tileAdj).getNetwork() instanceof SolarModuleNetwork)
                            {
                                solarArray.addAll(((SolarModuleNetwork) ((INetworkProvider) tileAdj).getNetwork()).getTransmitters());
                            }
                        }
                    }
                }
            }
        }

        super.update();

        if (!this.world.isRemote)
        {
            this.recharge(this.getInventory().get(0));

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (!this.getDisabled(0) && this.ticks % 20 == 0)
            {
                this.solarStrength = 0;
                int arraySizeWithinRange = 0;
                if (this.world.isDaytime() && (this.world.provider instanceof IGalacticraftWorldProvider || !this.world.isRaining() && !this.world.isThundering()))
                {
                    for (ITransmitter transmitter : solarArray)
                    {
                        TileEntity tile = (TileEntity) transmitter;
                        Vec3i diff = tile.getPos().subtract(this.getPos());
                        if (Math.abs(diff.getX()) <= 16 && diff.getY() == 0 && Math.abs(diff.getZ()) <= 16)
                        {
                            arraySizeWithinRange++;
                            if (this.world.canBlockSeeSky(tile.getPos()))
                            {
                                boolean valid = true;

                                for (int y = this.getPos().getY() + 1; y < 256; y++)
                                {
                                    IBlockState state = this.world.getBlockState(this.getPos().add(0, y, 0));

                                    if (state.getBlock().isOpaqueCube(state))
                                    {
                                        valid = false;
                                        break;
                                    }
                                }

                                if (valid)
                                {
                                    this.solarStrength++;
                                }
                            }
                        }
                    }
                }

                connectedInfo = solarStrength << 16 | arraySizeWithinRange;
            }
        }

        float angle = this.world.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.world.getCelestialAngle(1.0F) + angle) * 360.0F;
        if (!(this.world.provider instanceof WorldProviderSpaceStation)) celestialAngle += 12.5F;
        if (this.world.provider instanceof WorldProviderVenus) celestialAngle = 180F - celestialAngle;
        celestialAngle %= 360;
        boolean isDaytime = this.world.isDaytime() && (celestialAngle < 180.5F || celestialAngle > 359.5F) || this.world.provider instanceof WorldProviderSpaceStation;

        if (!this.world.isRemote)
        {
            int generated = this.getGenerate();
            if (generated > 0)
            {
                this.generateWatts = Math.min(Math.max(generated, 0), TileEntitySolarArrayController.MAX_GENERATE_WATTS);
            }
            else
            {
                this.generateWatts = 0;
            }
        }

        this.produce();
    }

    public int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        return (int) Math.floor(solarStrength * 6.3F * this.getSolarBoost());
    }

    public float getSolarBoost()
    {
        float result = (float) (this.world.provider instanceof ISolarLevel ? ((ISolarLevel) this.world.provider).getSolarEnergyMultiplier() : 1.0F);
        if (this.world.provider instanceof WorldProviderSpaceStation)
        {
            // 10% boost for new solar on space stations
            result *= 1.1F;
        }
        if (this.world.provider instanceof WorldProviderVenus)
        {
            if (this.pos.getY() > 90)
            {
                result += (this.pos.getY() - 90) / 1000F;   //Small improvement on Venus at higher altitudes
            }
        }
        return result;
    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.storage.setCapacity(nbt.getFloat("maxEnergy"));
        this.setDisabled(0, nbt.getBoolean("disabled"));
        this.disableCooldown = nbt.getInteger("disabledCooldown");

        this.initialised = false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("maxEnergy", this.getMaxEnergyStoredGC());
        nbt.setInteger("disabledCooldown", this.disableCooldown);
        nbt.setBoolean("disabled", this.getDisabled(0));

        return nbt;
    }

	/*@Override
    public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockSolarArrayController)
        {
            return state.getValue(BlockSolarArrayController.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront());
    }

    @Override
    public EnumFacing getElectricOutputDirection()
    {
        return getFront();
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC() * i / this.getMaxEnergyStoredGC());
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        return type == NetworkType.POWER && direction == this.getElectricOutputDirection() ||
                type == NetworkType.SOLAR_MODULE && direction != this.getElectricOutputDirection() && direction.getAxis() != EnumFacing.Axis.Y;
    }

    public int getPossibleArraySize()
    {
        return connectedInfo & 0xFFFF;
    }

    public int getActualArraySize()
    {
        return (connectedInfo >> 16) & 0xFFFF;
    }
}
