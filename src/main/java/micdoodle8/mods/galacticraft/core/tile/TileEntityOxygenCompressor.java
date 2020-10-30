package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.inventory.ContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenCompressor;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityOxygenCompressor extends TileEntityOxygen implements INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.oxygenCompressor)
    public static TileEntityType<TileEntityOxygenCompressor> TYPE;

    public static final int TANK_TRANSFER_SPEED = 2;
    private boolean usingEnergy = false;

    public TileEntityOxygenCompressor()
    {
        super(TYPE, 1200, 16);
        this.storage.setMaxExtract(15);
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        if (!this.world.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(2);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.tick();

        if (!this.world.isRemote)
        {
            this.usingEnergy = false;
            if (this.getOxygenStored() > 0 && this.hasEnoughEnergyToRun)
            {
                ItemStack tank0 = this.getInventory().get(0);

                if (!tank0.isEmpty())
                {
                    if (tank0.getItem() instanceof ItemOxygenTank && tank0.getDamage() > 0)
                    {
                        tank0.setDamage(tank0.getDamage() - TileEntityOxygenCompressor.TANK_TRANSFER_SPEED);
                        this.setOxygenStored(this.getOxygenStored() - TileEntityOxygenCompressor.TANK_TRANSFER_SPEED);
                        this.usingEnergy = true;
                    }
                }
            }
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0, 1, 2};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamage() > 1;
            case 1:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            case 2:
                return itemstack.getDamage() < itemstack.getItem().getMaxDamage();
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank && itemstack.getDamage() == 0;
        case 1:
            return ItemElectricBase.isElectricItemEmpty(itemstack);
        case 2:
            return FluidUtil.isEmptyContainer(itemstack);
        default:
            return false;
        }
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        case 2:
            return itemstack.getItem() instanceof IItemOxygenSupply;
        }

        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.usingEnergy;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockOxygenCompressor)
        {
            return state.get(BlockOxygenCompressor.FACING).rotateY();
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(1);
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerOxygenCompressor(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.oxygen_compressor");
    }
}
