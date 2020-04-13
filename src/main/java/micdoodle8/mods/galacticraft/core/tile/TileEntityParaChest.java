package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockParaChest;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;
import java.util.List;

public class TileEntityParaChest extends TileEntityAdvanced implements IInventorySettable, IScaleableFuelLevel
{
    private final int tankCapacity = 5000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public boolean adjacentChestChecked = false;
    public float lidAngle;
    public float prevLidAngle;
    public int numUsingPlayers;
    @NetworkedField(targetSide = Side.CLIENT)
    public EnumDyeColor color = EnumDyeColor.RED;

    public TileEntityParaChest()
    {
        super("container.parachest.name");
        this.color = EnumDyeColor.RED;
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
        final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
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
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        int size = nbt.getInteger("chestContentLength");
        if ((size - 3) % 18 != 0)
        {
            size += 18 - ((size - 3) % 18);
        }
        this.inventory = NonNullList.withSize(size, ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, this.getInventory());

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }

        if (nbt.hasKey("color"))
        {
            this.color = EnumDyeColor.byDyeDamage(nbt.getInteger("color"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger("chestContentLength", this.getInventory().size());
        ItemStackHelper.saveAllItems(nbt, this.getInventory());

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }

        nbt.setInteger("color", this.color.getDyeDamage());
        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    @Override
    public void update()
    {
        super.update();
        float f;

        if (!this.world.isRemote && this.numUsingPlayers != 0 && (this.ticks + this.getPos().getX() + this.getPos().getY() + this.getPos().getZ()) % 200 == 0)
        {
            this.numUsingPlayers = 0;
            f = 5.0F;
            List<?> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.getPos().getX() - f, this.getPos().getY() - f, this.getPos().getZ() - f, this.getPos().getX() + 1 + f, this.getPos().getY() + 1 + f, this.getPos().getZ() + 1 + f));
            Iterator<?> iterator = list.iterator();

            while (iterator.hasNext())
            {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

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
    public void openInventory(EntityPlayer player)
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
        this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, this.numUsingPlayers);
        this.world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockType(), false);
        this.world.notifyNeighborsOfStateChange(this.getPos().down(), this.getBlockType(), false);
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
        if (this.getBlockType() != null && this.getBlockType() instanceof BlockParaChest)
        {
            --this.numUsingPlayers;
            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, this.numUsingPlayers);
            this.world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockType(), false);
            this.world.notifyNeighborsOfStateChange(this.getPos().down(), this.getBlockType(), false);
        }
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
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
        EnumDyeColor color = this.color;

        super.decodePacketdata(buffer);

        if (this.world.isRemote && color != this.color)
        {
            this.world.markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }
}
