package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLanderFlameFX;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumClientPacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreEntityLander extends GCCoreEntityAdvanced implements IInventorySettable, IPacketReceiver, IScaleableFuelLevel
{
    private final int tankCapacity = 5000;
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);

    public double ySpeed;
    public double startingYSpeed;
    float maxSpeed = 0.05F;
    float minSpeed = 0.3F;
    float accel = 0.04F;
    float turnFactor = 2.0F;
    private double lastMotionY;
    public ItemStack[] chestContents = new ItemStack[0];
    public int numUsingPlayers;
    public GCCorePlayerMP playerSpawnedIn;
    private final float MAX_PITCH_ROTATION = 25.0F;
    public boolean landed;

    public float rumble;

    public GCCoreEntityLander(World var1)
    {
        super(var1, -5.0D, 2.5F);
        this.setSize(3.0F, 4.5F);
        this.ySpeed = 0.0D;
        this.startingYSpeed = -5.0D;
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    public GCCoreEntityLander(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4 + this.yOffset, var6);
    }

    public GCCoreEntityLander(GCCorePlayerMP player)
    {
        this(player.worldObj, player.posX, player.posY, player.posZ);
        this.playerSpawnedIn = player;

        this.chestContents = new ItemStack[player.getRocketStacks().length + 1];
        this.fuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, player.getFuelLevel()));

        for (int i = 0; i < player.getRocketStacks().length; i++)
        {
            this.chestContents[i] = player.getRocketStacks()[i];
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.rumble > 0)
        {
            this.rumble--;
        }

        if (this.rumble < 0)
        {
            this.rumble++;
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.posX += this.rumble * Math.abs(this.motionY) / 20F;
            this.riddenByEntity.posZ += this.rumble * Math.abs(this.motionY) / 20F;
        }

        if (this.motionY != 0.0D)
        {
            this.performHurtAnimation();

            this.rumble = (float) this.rand.nextInt(3) - 3;
        }

        if (this.ticks < 40 && this.posY > 100)
        {
            if (this.riddenByEntity == null)
            {
                final EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 5);

                if (player != null)
                {
                    player.mountEntity(this);
                }
            }
        }

        if (!this.worldObj.isRemote)
        {
            final FluidStack liquid = this.fuelTank.getFluid();

            if (liquid != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().getFluid().getName().equalsIgnoreCase("Fuel"))
            {
                if (FluidContainerRegistry.isEmptyContainer(this.chestContents[this.chestContents.length - 1]))
                {
                    boolean isCanister = this.chestContents[this.chestContents.length - 1].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage()));
                    final int amountToFill = Math.min(liquid.amount, isCanister ? GCCoreItems.fuelCanister.getMaxDamage() - 1 : FluidContainerRegistry.BUCKET_VOLUME);

                    if (isCanister)
                    {
                        this.chestContents[this.chestContents.length - 1] = new ItemStack(GCCoreItems.fuelCanister, 1, GCCoreItems.fuelCanister.getMaxDamage() - amountToFill);
                    }
                    else
                    {
                        this.chestContents[this.chestContents.length - 1] = FluidContainerRegistry.fillFluidContainer(liquid, this.chestContents[this.chestContents.length - 1]);
                    }

                    this.fuelTank.drain(amountToFill, true);
                }
            }
        }

        this.lastMotionY = this.motionY;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = nbt.getTagList("Items");

        this.chestContents = new ItemStack[nbt.getInteger("rocketStacksLength")];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.chestContents.length)
            {
                this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.landed = nbt.getBoolean("landed");

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        final NBTTagList nbttaglist = new NBTTagList();

        nbt.setInteger("rocketStacksLength", this.chestContents.length);

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);

        nbt.setBoolean("landed", this.landed);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public int getSizeInventory()
    {
        return this.chestContents.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.chestContents[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2);

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            final ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return StatCollector.translateToLocal("container.lander");
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest()
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
    }

    @Override
    public void closeChest()
    {
        --this.numUsingPlayers;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return false;
    }

    @Override
    public void onInventoryChanged()
    {
    }

    @Override
    public boolean interactFirst(EntityPlayer var1)
    {
        if (this.worldObj.isRemote)
        {
            if (this.riddenByEntity != null)
            {
                this.riddenByEntity.mountEntity(this);
            }

            return true;
        }
        else if (this.riddenByEntity == null && this.onGround && var1 instanceof EntityPlayerMP)
        {
            GCCoreUtil.openParachestInv((EntityPlayerMP) var1, this);
            return true;
        }
        else if (var1 instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) var1).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.ZOOM_CAMERA, new Object[] { 0 }));
            var1.mountEntity(null);
            return true;
        }
        else
        {
            return true;
        }
    }

    @Override
    protected boolean forceGroundUpdate()
    {
        return false;
    }

    @Override
    public boolean pressKey(int key)
    {
        switch (key)
        {
        case 0: // Accelerate
        {
            if (!this.onGround)
            {
                this.rotationPitch -= 0.5F * this.turnFactor;
            }

            return true;
        }
        case 1: // Deccelerate
        {
            if (!this.onGround)
            {
                this.rotationPitch += 0.5F * this.turnFactor;
            }

            return true;
        }
        case 2: // Left
        {
            if (!this.onGround)
            {
                this.rotationYaw -= 0.5F * this.turnFactor;
            }

            return true;
        }
        case 3: // Right
        {
            if (!this.onGround)
            {
                this.rotationYaw += 0.5F * this.turnFactor;
            }

            return true;
        }
        case 4: // Space
        {
            this.ySpeed += 0.05F;
            return true;
        }
        case 5: // LShift
        {
            this.ySpeed -= 0.005F;
            return true;
        }
        }

        return false;
    }

    @Override
    public boolean shouldMove()
    {
        if (this.ticks < 40)
        {
            return this.riddenByEntity != null;
        }

        return !this.landed;
    }

    @Override
    public boolean shouldSpawnParticles()
    {
        return !this.landed && this.posY < 256;
    }

    @Override
    public Map<Vector3, Vector3> getParticleMap()
    {
        final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double y1 = -5.0D;

        new Vector3(this);

        final Map<Vector3, Vector3> particleMap = new HashMap<Vector3, Vector3>();
        final float angle1 = (float) ((this.rotationYaw - 40.0F) * Math.PI / 180.0F);
        final float angle2 = (float) ((this.rotationYaw + 40.0F) * Math.PI / 180.0F);
        final float angle3 = (float) ((this.rotationYaw + 180 - 40.0F) * Math.PI / 180.0F);
        final float angle4 = (float) ((this.rotationYaw + 180 + 40.0F) * Math.PI / 180.0F);
        final float pitch = (float) Math.sin(this.rotationPitch * Math.PI / 180.0F);
        particleMap.put(new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle1) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle1) * Math.cos(pitch))), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle2) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle2) * Math.cos(pitch))), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle3) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle3) * Math.cos(pitch))), new Vector3(x1, y1, z1));
        particleMap.put(new Vector3(this).translate(new Vector3(0.4 * Math.cos(angle4) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle4) * Math.cos(pitch))), new Vector3(x1, y1, z1));
        return particleMap;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ)
    {
        return new GCCoreEntityLanderFlameFX(this.worldObj, x, y, z, motX, motY, motZ);
    }

    @Override
    public void tickInAir()
    {
        if (this.landed)
        {
            this.motionY = 0;
            return;
        }

        if (this.rotationPitch >= this.MAX_PITCH_ROTATION)
        {
            this.rotationPitch = this.MAX_PITCH_ROTATION;
        }
        else if (this.rotationPitch <= -this.MAX_PITCH_ROTATION)
        {
            this.rotationPitch = -this.MAX_PITCH_ROTATION;
        }

        this.ySpeed *= 0.98D;

        this.startingYSpeed += this.ySpeed * this.accel;

        this.startingYSpeed = this.startingYSpeed < -5.0F ? -5.0F : this.startingYSpeed > -this.minSpeed ? -this.minSpeed : this.startingYSpeed;

        if (this.worldObj.isRemote)
        {
            this.motionY = this.startingYSpeed;
        }
    }

    @Override
    public void tickOnGround()
    {
        this.rotationPitch = 0.0F;
        this.rotationYaw = 0.0F;
    }

    @Override
    public void onGroundHit()
    {
        if (!this.worldObj.isRemote)
        {
            if (Math.abs(this.lastMotionY) > 2.0D)
            {
                if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
                {
                    final Object[] toSend2 = { 0 };
                    ((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.ZOOM_CAMERA, toSend2));

                    this.riddenByEntity.mountEntity(this);
                }

                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 12, true);

                this.setDead();
            }

            this.landed = true;
        }
    }

    @Override
    public Vector3 getMotionVec()
    {
        return new Vector3(-(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D)), this.ticks < 40 ? 0 : this.motionY, -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D)));
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();
        objList.add(this.landed);
        Integer cargoLength = this.chestContents != null ? this.chestContents.length : 0;
        objList.add(cargoLength);
        objList.add(this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);
        return objList;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 5;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 50.0D;
    }

    @Override
    public void readNetworkedData(ByteArrayDataInput dataStream)
    {
        this.landed = dataStream.readBoolean();

        int cargoLength = dataStream.readInt();
        if (this.chestContents == null || this.chestContents.length == 0)
        {
            this.chestContents = new ItemStack[cargoLength];
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 21, new Object[] { this.entityId }));
        }

        this.fuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, dataStream.readInt()));
    }

    @Override
    public boolean allowDamageSource(DamageSource damageSource)
    {
        return this.landed && !damageSource.isExplosion();
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();

        for (final ItemStack stack : this.chestContents)
        {
            items.add(stack);
        }

        return items;
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.chestContents = new ItemStack[size];
    }
}
