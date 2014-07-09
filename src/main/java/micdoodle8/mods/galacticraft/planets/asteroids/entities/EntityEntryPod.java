package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.entities.InventoryEntity;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;

public class EntityEntryPod extends InventoryEntity implements IInventorySettable, IScaleableFuelLevel, ICameraZoomEntity
{
    private final int tankCapacity = 5000;
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);
    public boolean waitForPlayer;
    private boolean lastWaitForPlayer;
    public boolean lastOnGround;
    private double lastMotionY;

    public EntityEntryPod(World world)
    {
        super(world);
    }

    @Override
    protected void entityInit()
    {

    }

    @Override
    public double getMountedYOffset()
    {
        return super.getMountedYOffset() - 0.5;
    }

    @Override
    public void onUpdate()
    {
        if (this.waitForPlayer)
        {
            if (this.riddenByEntity != null)
            {
                if (this.ticksExisted >= 40)
                {
                    if (!this.worldObj.isRemote)
                    {
                        Entity e = this.riddenByEntity;
                        this.riddenByEntity.ridingEntity = null;
                        this.riddenByEntity = null;
                        e.mountEntity(this);
                    }

                    this.waitForPlayer = false;
                }
                else
                {
                    this.motionX = this.motionY = this.motionZ = 0.0D;
                    this.riddenByEntity.motionX = this.riddenByEntity.motionY = this.riddenByEntity.motionZ = 0;
                }
            }
            else
            {
                this.motionX = this.motionY = this.motionZ = 0.0D;
            }
        }

        if (!this.waitForPlayer && this.lastWaitForPlayer)
        {
            this.motionY = -0.5D;
        }

        super.onUpdate();

        if (this.onGround)
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
        }
        else
        {
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        this.lastWaitForPlayer = this.waitForPlayer;
        this.lastOnGround = this.onGround;
        this.lastMotionY = this.motionY;
    }

    @Override
    public double getPacketRange()
    {
        return 0;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        NBTTagList itemList = nbt.getTagList("Items", 10);
        this.containedItems = new ItemStack[nbt.getInteger("RocketStacksLength")];

        for (int i = 0; i < itemList.tagCount(); ++i)
        {
            NBTTagCompound itemTag = itemList.getCompoundTagAt(i);
            int slotID = itemTag.getByte("Slot") & 255;

            if (slotID >= 0 && slotID < this.containedItems.length)
            {
                this.containedItems[slotID] = ItemStack.loadItemStackFromNBT(itemTag);
            }
        }

        if (nbt.hasKey("FuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("FuelTank"));
        }

        this.waitForPlayer = nbt.getBoolean("WaitingForPlayer");

        this.lastOnGround = this.onGround;
        this.lastMotionY = this.motionY;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        NBTTagList itemList = new NBTTagList();

        nbt.setInteger("RocketStacksLength", this.containedItems.length);

        for (int i = 0; i < this.containedItems.length; ++i)
        {
            if (this.containedItems[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) i);
                this.containedItems[i].writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        nbt.setTag("Items", itemList);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("FuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }

        nbt.setBoolean("WaitingForPlayer", this.waitForPlayer);
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.containedItems = new ItemStack[size];
    }

    @Override
    public int getSizeInventory()
    {
        return this.containedItems.length;
    }

    @Override
    public String getInventoryName()
    {
        return "Asteroids Pod";
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return false;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (!this.worldObj.isRemote)
        {
            list.add(this.containedItems != null ? this.containedItems.length : 0);
            list.add(this.fuelTank.getFluidAmount());

            list.add(this.waitForPlayer);
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.worldObj.isRemote)
        {
            int cargoLength = buffer.readInt();

            if (this.containedItems == null || this.containedItems.length == 0)
            {
                this.containedItems = new ItemStack[cargoLength];
                GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
            }

            this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, buffer.readInt()));

            this.waitForPlayer = buffer.readBoolean();
        }
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {

    }

    @Override
    public int getScaledFuelLevel(int scale)
    {
        return (int) ((double) this.fuelTank.getFluidAmount() / (double) this.tankCapacity * scale);
    }
}
