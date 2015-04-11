package micdoodle8.mods.galacticraft.core.entities;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.*;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate.IEntityFullSync;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityBuggy extends Entity implements IInventory, IPacketReceiver, IDockable, IControllableEntity, IEntityFullSync
{
    public static final int tankCapacity = 1000;
    public FluidTank buggyFuelTank = new FluidTank(this.tankCapacity);
    protected long ticks = 0;
    public int buggyType;
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public double speed;
    public float wheelRotationZ;
    public float wheelRotationX;
    float maxSpeed = 0.5F;
    float accel = 0.2F;
    float turnFactor = 3.0F;
    public String texture;
    ItemStack[] cargoItems = new ItemStack[60];
    public double boatX;
    public double boatY;
    public double boatZ;
    public double boatYaw;
    public double boatPitch;
    public int boatPosRotationIncrements;
    private IFuelDock landingPad;

    public EntityBuggy(World var1)
    {
        super(var1);
        this.setSize(0.98F, 1F);
        this.yOffset = 2.5F;
        this.currentDamage = 18;
        this.timeSinceHit = 19;
        this.rockDirection = 20;
        this.speed = 0.0D;
        this.preventEntitySpawning = true;
        this.dataWatcher.addObject(this.currentDamage, new Integer(0));
        this.dataWatcher.addObject(this.timeSinceHit, new Integer(0));
        this.dataWatcher.addObject(this.rockDirection, new Integer(1));
        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
    }

    public EntityBuggy(World var1, double var2, double var4, double var6, int type)
    {
        this(var1);
        this.setPosition(var2, var4 + this.yOffset, var6);
        this.setBuggyType(type);
        this.cargoItems = new ItemStack[this.buggyType * 18];
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.buggyFuelTank.getFluid() == null ? 0 : this.buggyFuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    public ModelBase getModel()
    {
        return null;
    }

    public int getType()
    {
        return this.buggyType;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.height - 3.0D;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setBuggyType(int par1)
    {
        this.buggyType = par1;
    }

    @Override
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            final double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            final double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY - 2 + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.worldObj.isRemote)
        {
            this.boatX = x;
            this.boatY = y;
            this.boatZ = z;
            this.boatYaw = yaw;
            this.boatPitch = pitch;
            this.motionX = motX;
            this.motionY = motY;
            this.motionZ = motZ;
            this.boatPosRotationIncrements = 5;
        }
        else
        {
            this.setPosition(x, y, z);
            this.setRotation(yaw, pitch);
            this.motionX = motX;
            this.motionY = motY;
            this.motionZ = motZ;
        }
    }

    @Override
    public void performHurtAnimation()
    {
        this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
        this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
        this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) * 5));
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        if (this.isDead || var1.equals(DamageSource.cactus))
        {
            return true;
        }
        else
        {
        	Entity e = var1.getEntity(); 
            if (this.isEntityInvulnerable() || (e instanceof EntityLivingBase && !(e instanceof EntityPlayer)))
            {
                return false;
            }
            else
            {
	            this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
	            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
	            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf((int) (this.dataWatcher.getWatchableObjectInt(this.currentDamage) + var2 * 10)));
	            this.setBeenAttacked();
	
	            if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)
	            {
	                this.dataWatcher.updateObject(this.currentDamage, 100);
	            }
	
	            if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 2)
	            {
	                if (this.riddenByEntity != null)
	                {
	                    this.riddenByEntity.mountEntity(this);
	                }
	
	                if (!this.worldObj.isRemote)
	                {
	                    if (this.riddenByEntity != null)
	                    {
	                        this.riddenByEntity.mountEntity(this);
	                    }
	
	                    this.dropBuggyAsItem();
	                }
	
	                this.setDead();
	            }
	
	            return true;
            }
        }
    }

    public void dropBuggyAsItem()
    {
        List<ItemStack> dropped = this.getItemsDropped();

        if (dropped == null)
        {
            return;
        }

        for (final ItemStack item : dropped)
        {
            EntityItem entityItem = this.entityDropItem(item, 0);

            if (item.hasTagCompound())
            {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
            }
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();

        ItemStack buggy = new ItemStack(GCItems.buggy, 1, this.buggyType);
        buggy.setTagCompound(new NBTTagCompound());
        buggy.getTagCompound().setInteger("BuggyFuel", this.buggyFuelTank.getFluidAmount());
        items.add(buggy);

        for (ItemStack item : this.cargoItems)
        {
            if (item != null)
            {
                items.add(item);
            }
        }

        return items;
    }

    @Override
    public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
    {
        if (this.riddenByEntity != null)
        {
            if (this.riddenByEntity instanceof EntityPlayer && FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity))
            {
            }
            else
            {
                this.boatPosRotationIncrements = i + 5;
                this.boatX = d;
                this.boatY = d1 + (this.riddenByEntity == null ? 1 : 0);
                this.boatZ = d2;
                this.boatYaw = f;
                this.boatPitch = f1;
            }
        }
    }

    @Override
    public void onUpdate()
    {
        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;

        super.onUpdate();

        if (this.worldObj.isRemote)
        {
            this.wheelRotationX += Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 150.0F * (this.speed < 0 ? 1 : -1);
            this.wheelRotationX %= 360;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ * 0.9F));
        }

        if (this.worldObj.isRemote && !FMLClientHandler.instance().getClient().thePlayer.equals(this.worldObj.getClosestPlayerToEntity(this, -1)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.boatPosRotationIncrements > 0)
            {
                x = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                y = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                z = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + var12 / this.boatPosRotationIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                x = this.posX + this.motionX;
                y = this.posY + this.motionY;
                z = this.posZ + this.motionZ;
                if (this.riddenByEntity != null)
                {
                    this.setPosition(x, y, z);
                }

                if (this.onGround)
                {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
            return;
        }

        if (this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) > 0)
        {
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) - 1));
        }

        if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 0)
        {
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) - 1));
        }

        if (!this.onGround)
        {
            this.motionY -= WorldUtil.getGravityForEntity(this) * 0.5D;
        }

        if (this.inWater && this.speed > 0.2D)
        {
            this.worldObj.playSoundEffect((float) this.posX, (float) this.posY, (float) this.posZ, "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
        }

        this.speed *= 0.98D;

        if (this.speed > this.maxSpeed)
        {
            this.speed = this.maxSpeed;
        }

        if (this.isCollidedHorizontally)
        {
            this.speed *= 0.9;
            this.motionY = 0.15D;
        }

        if (this.worldObj.isRemote && this.buggyFuelTank.getFluid() != null && this.buggyFuelTank.getFluid().amount > 0)
        {
            this.motionX = -(this.speed * Math.cos((this.rotationYaw - 90F) * Math.PI / 180.0D));
            this.motionZ = -(this.speed * Math.sin((this.rotationYaw - 90F) * Math.PI / 180.0D));
        }

        if (this.worldObj.isRemote)
        {
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        if (!this.worldObj.isRemote && Math.abs(this.motionX * this.motionZ) > 0.000001)
        {
            double d = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d != 0 && this.ticks % MathHelper.floor_double(2 / d) == 0)
            {
                this.removeFuel(1);
            }
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.worldObj.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
        }
        else if (this.ticks % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        sendData.add(this.buggyType);
        sendData.add(this.buggyFuelTank);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.buggyType = buffer.readInt();

        try
        {
            this.buggyFuelTank = NetworkUtil.readFluidTank(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.buggyType = var1.getInteger("buggyType");
        final NBTTagList var2 = var1.getTagList("Items", 10);
        this.cargoItems = new ItemStack[this.getSizeInventory()];

        if (var1.hasKey("fuelTank"))
        {
            this.buggyFuelTank.readFromNBT(var1.getCompoundTag("fuelTank"));
        }

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.cargoItems.length)
            {
                this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("buggyType", this.buggyType);
        final NBTTagList var2 = new NBTTagList();

        if (this.buggyFuelTank.getFluid() != null)
        {
            var1.setTag("fuelTank", this.buggyFuelTank.writeToNBT(new NBTTagCompound()));
        }

        for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
        {
            if (this.cargoItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.cargoItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);
    }

    @Override
    public int getSizeInventory()
    {
        return this.buggyType * 18;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.cargoItems[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.cargoItems[var1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[var1].stackSize <= var2)
            {
                var3 = this.cargoItems[var1];
                this.cargoItems[var1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[var1].splitStack(var2);

                if (this.cargoItems[var1].stackSize == 0)
                {
                    this.cargoItems[var1] = null;
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
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.cargoItems[var1] != null)
        {
            final ItemStack var2 = this.cargoItems[var1];
            this.cargoItems[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.cargoItems[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return "Buggy";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return !this.isDead && var1.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean interactFirst(EntityPlayer var1)
    {
        if (this.worldObj.isRemote)
        {
            if (this.riddenByEntity == null)
            {
                var1.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.leftKey.getKeyCode()) + " / " + GameSettings.getKeyDisplayString(KeyHandlerClient.rightKey.getKeyCode()) + "  - " + GCCoreUtil.translate("gui.buggy.turn.name")));
                var1.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.accelerateKey.getKeyCode()) + "       - " + GCCoreUtil.translate("gui.buggy.accel.name")));
                var1.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.decelerateKey.getKeyCode()) + "       - " + GCCoreUtil.translate("gui.buggy.decel.name")));
                var1.addChatMessage(new ChatComponentText(GameSettings.getKeyDisplayString(KeyHandlerClient.openFuelGui.getKeyCode()) + "       - " + GCCoreUtil.translate("gui.buggy.inv.name")));
            }

            return true;
        }
        else
        {
            if (this.riddenByEntity != null)
            {
                var1.mountEntity(null);
                return true;
            }
            else
            {
                var1.mountEntity(this);
                return true;
            }
        }
    }

    @Override
    public boolean pressKey(int key)
    {
        if (this.worldObj.isRemote && (key == 6 || key == 8 || key == 9))
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketControllableEntity(key));
            return true;
        }

        switch (key)
        {
        case 0: // Accelerate
            this.speed += this.accel / 20D;
            return true;
        case 1: // Deccelerate
            this.speed -= this.accel / 20D;
            return true;
        case 2: // Left
            this.rotationYaw -= 0.5F * this.turnFactor;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ + 0.5F));
            return true;
        case 3: // Right
            this.rotationYaw += 0.5F * this.turnFactor;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ - 0.5F));
            return true;
        }

        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public int addFuel(FluidStack liquid, boolean doDrain)
    {
        final FluidStack liquidInTank = this.buggyFuelTank.getFluid();

        if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel") && this.landingPad != null)
        {
            if (liquidInTank == null || liquidInTank.amount + liquid.amount <= this.buggyFuelTank.getCapacity())
            {
                return this.buggyFuelTank.fill(liquid, doDrain);
            }
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.buggyFuelTank.drain(amount, true);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.buggyType == 0)
        {
            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.cargoItems.length; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        this.cargoItems[count].stackSize += stack.stackSize;
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.stackSize;
                    int surplus = origSize + stack.stackSize - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        this.cargoItems[count].stackSize = stackAt.getMaxStackSize();
                        this.markDirty();
                    }

                    stack.stackSize = surplus;
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    this.cargoItems[count].stackSize = origSize;
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.cargoItems.length; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.cargoItems[count] = stack;
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.cargoItems.length; i++)
        {
            ItemStack stackAt = this.cargoItems[i];

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.stackSize = 1;

            	if (doRemove && --stackAt.stackSize <= 0)
                {
                    this.cargoItems[i] = null;
                }

                if (doRemove)
                {
                    this.markDirty();
                }
                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        this.landingPad = pad;
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public void onPadDestroyed()
    {

    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityBuggyFueler;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public UUID getOwnerUUID()
    {
        if (this.riddenByEntity != null && !(this.riddenByEntity instanceof EntityPlayer))
        {
            return null;
        }

        return this.riddenByEntity != null ? ((EntityPlayer) this.riddenByEntity).getPersistentID() : null;
    }
}
