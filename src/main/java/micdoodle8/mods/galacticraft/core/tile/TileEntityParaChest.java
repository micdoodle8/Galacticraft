package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

public class TileEntityParaChest extends TileEntityAdvanced implements IInventorySettable, IScaleableFuelLevel
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.parachest)
    public static TileEntityType<TileEntityParaChest> TYPE;

    private final int tankCapacity = 5000;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public final FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public boolean adjacentChestChecked = false;
    public float lidAngle;
    public float prevLidAngle;
    public int numUsingPlayers;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public DyeColor color = DyeColor.RED;

    public TileEntityParaChest()
    {
        super(TYPE);
        this.color = DyeColor.RED;
        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    public void onLoad()
    {
        if (this.world.isRemote)
        {
            //Request size + contents information from server
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
        }
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    protected boolean handleInventory()
    {
        // Custom size loading, so handle in this class
        return false;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        int size = nbt.getInt("chestContentLength");
        if ((size - 3) % 18 != 0)
        {
            size += 18 - ((size - 3) % 18);
        }
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, this.getInventory());

        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        if (nbt.contains("color"))
        {
            this.color = DyeColor.values()[nbt.getInt("color")];
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        nbt.putInt("chestContentLength", this.getInventory().size());
        ItemStackHelper.saveAllItems(nbt, this.getInventory());

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundNBT()));
        }

        nbt.putInt("color", this.color.ordinal());
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    @Override
    public void tick()
    {
        super.tick();
        float f;

        if (!this.world.isRemote && this.numUsingPlayers != 0 && (this.ticks + this.getPos().getX() + this.getPos().getY() + this.getPos().getZ()) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            f = 5.0F;
            List<?> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(this.getPos().getX() - f, this.getPos().getY() - f, this.getPos().getZ() - f, this.getPos().getX() + 1 + f, this.getPos().getY() + 1 + f, this.getPos().getZ() + 1 + f));

            for (Object o : list)
            {
                PlayerEntity entityplayer = (PlayerEntity) o;

                if (entityplayer.openContainer instanceof ContainerParaChest)
                {
                    ++this.numUsingPlayers;
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        f = 0.1F;
        double d0;

        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F)
        {
            double d1 = this.getPos().getX() + 0.5D;
            d0 = this.getPos().getZ() + 0.5D;

            this.world.playSound(null, d1, this.getPos().getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F)
        {
            float f1 = this.lidAngle;

            if (this.numUsingPlayers > 0)
            {
                this.lidAngle += f;
            }
            else
            {
                this.lidAngle -= f;
            }

            if (this.lidAngle > 1.0F)
            {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;

            if (this.lidAngle < f2 && f1 >= f2)
            {
                d0 = this.getPos().getX() + 0.5D;
                double d2 = this.getPos().getZ() + 0.5D;

                this.world.playSound(null, d0, this.getPos().getY() + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
                this.lidAngle = 0.0F;
            }
        }

        if (!this.world.isRemote)
        {
            this.checkFluidTankTransfer(this.getInventory().size() - 1, this.fuelTank);
        }
    }

    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.getInventory(), slot);
    }

    @Override
    public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
            return true;
        }
        else
        {
            return super.receiveClientEvent(par1, par2);
        }
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
        this.world.addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 1, this.numUsingPlayers);
        this.world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockState().getBlock());
        this.world.notifyNeighborsOfStateChange(this.getPos().down(), this.getBlockState().getBlock());
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        --this.numUsingPlayers;
        this.world.addBlockEvent(this.getPos(), this.getBlockState().getBlock(), 1, this.numUsingPlayers);
        this.world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockState().getBlock());
        this.world.notifyNeighborsOfStateChange(this.getPos().down(), this.getBlockState().getBlock());
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void remove()
    {
        super.remove();
        this.updateContainingBlockInfo();
    }

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        DyeColor color = this.color;

        super.decodePacketdata(buffer);

//        if (this.world.isRemote && color != this.color)
//        {
//            this.world.markBlockRangeForRenderUpdate(getPos(), getPos()); TODO Necessary?
//        }
    }
}
