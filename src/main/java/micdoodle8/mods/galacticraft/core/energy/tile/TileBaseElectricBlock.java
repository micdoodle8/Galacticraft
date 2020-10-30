package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.LogicalSide;

import java.util.EnumSet;

public abstract class TileBaseElectricBlock extends TileBaseUniversalElectrical implements IDisableableMachine, IConnector
{
    //	public int energyPerTick = 200;
    //	private final float ueMaxEnergy;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int disableCooldown = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean hasEnoughEnergyToRun = false;
    public boolean noRedstoneControl = false;

    public TileBaseElectricBlock(TileEntityType<?> type)
    {
        super(type);
    }

    public boolean shouldPullEnergy()
    {
        return this.shouldUseEnergy() || this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC();
    }

    public abstract boolean shouldUseEnergy();

    public abstract Direction getElectricInputDirection();

    public abstract ItemStack getBatteryInSlot();

    //	public TileBaseElectricBlock()
    //	{
    //		this.storage.setMaxReceive(ueWattsPerTick);
    //		this.storage.setMaxExtract(0);
    //		this.storage.setCapacity(maxEnergy);
    ////		this.ueMaxEnergy = maxEnergy;
    ////		this.ueWattsPerTick = ueWattsPerTick;
    //
    //		/*
    //		 * if (PowerFramework.currentFramework != null) { this.bcPowerProvider =
    //		 * new GCCoreLinkedPowerProvider(this);
    //		 * this.bcPowerProvider.configure(20, 1, 10, 10, 1000); }
    //		 */
    //	}

    //	@Override
    //	public float getMaxEnergyStored()
    //	{
    //		return this.ueMaxEnergy;
    //	}

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC(null) * i / this.getMaxEnergyStoredGC(null));
        //- this.ueWattsPerTick;
    }

    //	@Override
    //	public float getRequest(EnumFacing direction)
    //	{
    //		if (this.shouldPullEnergy())
    //		{
    //			return this.ueWattsPerTick * 2;
    //		}
    //		else
    //		{
    //			return 0;
    //		}
    //	}
    //
    //	@Override
    //	public float getProvide(EnumFacing direction)
    //	{
    //		return 0;
    //	}

    @Override
    public void tick()
    {
        if (!this.world.isRemote)
        {
            if (this.shouldPullEnergy() && this.getEnergyStoredGC(null) < this.getMaxEnergyStoredGC(null) && this.getBatteryInSlot() != null && this.getElectricInputDirection() != null)
            {
                this.discharge(this.getBatteryInSlot());
            }

            if (this.getEnergyStoredGC(null) > this.storage.getMaxExtract() && (this.noRedstoneControl || !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos())))
            {
                this.hasEnoughEnergyToRun = true;
                if (this.shouldUseEnergy())
                {
                    this.storage.extractEnergyGC(this.storage.getMaxExtract(), false);
                }
                else
                {
                    this.slowDischarge();
                }
            }
            else
            {
                this.hasEnoughEnergyToRun = false;
                this.slowDischarge();
            }
        }

        super.tick();

        if (!this.world.isRemote)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }
        }
    }

    public void slowDischarge()
    {
        if (this.ticks % 10 == 0)
        {
            this.storage.extractEnergyGC(5F, false);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        nbt.putBoolean("isDisabled", this.getDisabled(0));
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        this.setDisabled(0, nbt.getBoolean("isDisabled"));
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 10;
        }
    }

    public abstract Direction getFront();

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public Direction getFacing(World world, BlockPos pos)
//    {
//        return this.getFront();
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public boolean setFacing(World world, BlockPos pos, Direction newDirection, PlayerEntity player)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public boolean wrenchCanRemove(World world, BlockPos pos, PlayerEntity player)
//    {
//        return false;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.tile.IWrenchable", modID = CompatibilityManager.modidIC2)
//    public List<ItemStack> getWrenchDrops(World world, BlockPos pos, BlockState state, TileEntity te, PlayerEntity player, int fortune)
//    {
//        List<ItemStack> drops = Lists.newArrayList();
//        drops.add(this.getBlockType().getPickBlock(state, null, this.world, this.getPos(), player));
//        return drops;
//    } TODO IC Support

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        if (this.getElectricInputDirection() == null)
        {
            return EnumSet.noneOf(Direction.class);
        }

        return EnumSet.of(this.getElectricInputDirection());
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity entityplayer)
    {
        return this.getWorld().getTileEntity(this.getPos()) == this && entityplayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricInputDirection();
    }

    public String getGUIstatus()
    {
        if (!this.noRedstoneControl && RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.off");
        }

        if (this.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready");
        }

        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.missingpower");
        }

        return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.active");
    }

    /**
     * @param missingInput = dynamically: null if all inputs are present, or a string if an input (e.g. oxygen, fuel) is missing
     * @param activeString = the specific 'Running' / 'Processing' etc string for this machine
     * @return
     */
    public String getGUIstatus(String missingInput, String activeString, boolean shorten)
    {
        if (!this.noRedstoneControl && RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.off");
        }

        if (this.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate(shorten ? "gui.status.missingpower.short" : "gui.status.missingpower");
        }

        if (missingInput != null)
        {
            return missingInput;
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.ready");
        }

        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + GCCoreUtil.translate(shorten ? "gui.status.missingpower.short" : "gui.status.missingpower");
        }

        if (activeString != null)
        {
            return activeString;
        }

        return EnumColor.RED + GCCoreUtil.translate("gui.status.unknown");
    }
}
