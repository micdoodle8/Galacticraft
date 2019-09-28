package micdoodle8.mods.galacticraft.core.energy.tile;

import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;

public abstract class TileBaseElectricBlockWithInventory extends TileBaseElectricBlock implements IInventoryDefaults
{
    public TileBaseElectricBlockWithInventory(String tileName)
    {
        super(tileName);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.getHorizontal(((this.getBlockMetadata() & 3) + 1) % 4);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }
}
