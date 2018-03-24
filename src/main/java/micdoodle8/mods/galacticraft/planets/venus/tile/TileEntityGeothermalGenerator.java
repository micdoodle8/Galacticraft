package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockGeothermalGenerator;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

public class TileEntityGeothermalGenerator extends TileBaseUniversalElectricalSource implements IInventoryDefaults, ISidedInventory, IConnector, IDisableableMachine
{
    public static final int MAX_GENERATE_GJ_PER_TICK = 200;
    public static final int MIN_GENERATE_GJ_PER_TICK = 30;

    private boolean validSpout;

    private ItemStack[] containingItems = new ItemStack[1];
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;

    @Annotations.NetworkedField(targetSide = Side.CLIENT)
    public int generateWatts = 0;

    public TileEntityGeothermalGenerator()
    {
        this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
        this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            this.receiveEnergyGC(null, this.generateWatts, false);
        }

        super.update();

        if (this.ticks % 20 == 0)
        {
            BlockPos below = this.getPos().down();
            IBlockState stateBelow = this.worldObj.getBlockState(below);

            boolean lastValidSpout = this.validSpout;
            this.validSpout = false;
            if (stateBelow.getBlock() == VenusBlocks.spout)
            {
                BlockPos pos1 = below.down();
                for (; this.getPos().getY() - pos1.getY() < 20; pos1 = pos1.down())
                {
                    IBlockState state = this.worldObj.getBlockState(pos1);
                    if (state.getBlock() == VenusModule.sulphuricAcid.getBlock())
                    {
                        this.validSpout = true;
                        break;
                    }
                    else if (!state.getBlock().isAir(this.worldObj, pos1))
                    {
                        // Not valid
                        break;
                    }
                }
            }

            if (this.worldObj.isRemote && this.validSpout != lastValidSpout)
            {
                // Update active texture
                this.worldObj.markBlockForUpdate(this.getPos());
            }
        }

        if (!this.worldObj.isRemote)
        {
            this.recharge(this.containingItems[0]);

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            this.generateWatts = Math.min(Math.max(this.getGenerate(), 0), TileEntityGeothermalGenerator.MAX_GENERATE_GJ_PER_TICK);
        }
        else
        {
            if (this.generateWatts > 0 && this.ticks % ((int) ((float)MAX_GENERATE_GJ_PER_TICK / (this.generateWatts + 1)) * 5 + 1) == 0)
            {
                double posX = pos.getX() + 0.5;
                double posY = pos.getY() + 1.0;
                double posZ = pos.getZ() + 0.5;
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX - 0.25, posY, posZ - 0.25), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX - 0.25, posY, posZ), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX - 0.25, posY, posZ + 0.25), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX, posY, posZ - 0.25), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX, posY, posZ), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX, posY, posZ + 0.25), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX + 0.25, posY, posZ - 0.25), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX + 0.25, posY, posZ), new Vector3(0.0, 0.025, 0.0));
                GalacticraftPlanets.spawnParticle("acidExhaust", new Vector3(posX + 0.25, posY, posZ + 0.25), new Vector3(0.0, 0.025, 0.0));
            }
        }

        this.produce();
    }

    private int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        if (!this.validSpout)
        {
            return 0;
        }

        int diff = TileEntityGeothermalGenerator.MAX_GENERATE_GJ_PER_TICK - TileEntityGeothermalGenerator.MIN_GENERATE_GJ_PER_TICK;
        return (int) Math.floor((Math.sin(this.ticks / 50.0F) * 0.5F + 0.5F) * diff + TileEntityGeothermalGenerator.MIN_GENERATE_GJ_PER_TICK);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList items = nbt.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < items.tagCount(); ++i)
        {
            NBTTagCompound tagAt = items.getCompoundTagAt(i);
            int slot = tagAt.getByte("Slot") & 255;

            if (slot < this.containingItems.length)
            {
                this.containingItems[slot] = ItemStack.loadItemStackFromNBT(tagAt);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        final NBTTagList list = new NBTTagList();

        for (int i = 0; i < this.containingItems.length; ++i)
        {
            if (this.containingItems[i] != null)
            {
                final NBTTagCompound tagAt = new NBTTagCompound();
                tagAt.setByte("Slot", (byte) i);
                this.containingItems[i].writeToNBT(tagAt);
                list.appendTag(tagAt);
            }
        }

        nbt.setTag("Items", list);
    }

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricOutputDirection();
    }

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public EnumFacing getFront()
    {
        IBlockState state = this.worldObj.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockGeothermalGenerator)
        {
            return state.getValue(BlockGeothermalGenerator.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront().rotateY());
    }

    @Override
    public EnumFacing getElectricOutputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.geothermal_generator.name");
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            if (this.disabled != disabled && this.worldObj.isRemote)
            {
                // Update active texture
                this.worldObj.markBlockForUpdate(this.getPos());
            }

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
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.containingItems[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        if (this.containingItems[slot] != null)
        {
            ItemStack var3;

            if (this.containingItems[slot].stackSize <= amount)
            {
                var3 = this.containingItems[slot];
                this.containingItems[slot] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[slot].splitStack(amount);

                if (this.containingItems[slot].stackSize == 0)
                {
                    this.containingItems[slot] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slot)
    {
        if (this.containingItems[slot] != null)
        {
            final ItemStack var2 = this.containingItems[slot];
            this.containingItems[slot] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack)
    {
        this.containingItems[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit())
        {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

     @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
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

    public boolean hasValidSpout()
    {
        return validSpout;
    }
}
