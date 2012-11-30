package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.ModelBase;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import org.lwjgl.input.Keyboard;

public class GCCoreEntityBuggy extends Entity implements IInventory
{
    public int fuel;
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public double speed;
    float maxSpeed = 1.0F;
    float accel = 0.2F;
    float turn = 3.0F;
    public String texture;
    ItemStack[] cargoItems;

    public GCCoreEntityBuggy(World var1)
    {
        super(var1);
        this.setSize(0.98F, 0.7F);
        this.yOffset = 2.5F;
        this.cargoItems = new ItemStack[36];
        this.fuel = 0;
        this.currentDamage = 18;
        this.timeSinceHit = 19;
        this.rockDirection = 20;
        this.speed = 0.0D;
        this.preventEntitySpawning = true;
        this.dataWatcher.addObject(this.currentDamage, new Integer(0));
        this.dataWatcher.addObject(this.timeSinceHit, new Integer(0));
        this.dataWatcher.addObject(this.rockDirection, new Integer(1));
    }

    public GCCoreEntityBuggy(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4 + this.yOffset, var6);
    }

    public ModelBase getModel()
    {
        return null;
    }

    @Override
	protected void entityInit() {}

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    @Override
	public AxisAlignedBB getCollisionBox(Entity var1)
    {
        return var1.boundingBox;
    }

    /**
     * returns the bounding box for this entity
     */
    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
	public boolean canBePushed()
    {
        return false;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    @Override
	public double getMountedYOffset()
    {
        return this.height - 3.0D;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
	public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
	public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY - 1 + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @Override
	public void performHurtAnimation()
    {
        this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
        this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
        this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) * 5));
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        boolean var3 = false;

        if (this.isDead)
        {
            return true;
        }
        else
        {
            this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) + var2 * 5));
            this.setBeenAttacked();

            if (var1.getEntity() instanceof EntityPlayer)
            {
                ;
            }

            if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 40 || var3)
            {
                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                if (!this.worldObj.isRemote)
                {
//                    this.dropItem(mod_cars.car.shiftedIndex, 1);
//
//                    for (int var4 = 0; var4 < this.getSizeInventory(); ++var4)
//                    {
//                        ItemStack var5 = this.getStackInSlot(var4);
//
//                        if (var5 != null)
//                        {
//                            this.entityDropItem(var5, 0.0F);
//                        }
//                    }
                }

                this.setDead();
            }

            return true;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
	public void onUpdate()
    {
        super.onUpdate();

//        if (ModLoader.getMinecraftInstance() != null && ModLoader.getMinecraftInstance().theWorld != null)
//        {
//            if (this.worldObj instanceof WorldServer && ModLoader.getMinecraftInstance().theWorld.getEntityByID(this.entityId) == null)
//            {
//            	GCCoreEntityBuggy var1 = new GCCoreEntityBuggy(ModLoader.getMinecraftInstance().theWorld);
//                var1.setPosition(this.posX, this.posY, this.posZ);
//                ModLoader.getMinecraftInstance().theWorld.addEntityToWorld(this.entityId, var1);
//            }
//
//            if (this.worldObj instanceof WorldServer)
//            {
//                GCCoreEntityBuggy var19 = (GCCoreEntityBuggy)ModLoader.getMinecraftInstance().theWorld.getEntityByID(this.entityId);
//                var19.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
//                var19.setVelocity(this.motionX, this.motionY, this.motionZ);
//                var19.speed = this.speed;
//            }
//        }

        if (this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) > 0)
        {
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) - 1));
        }

        if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 0)
        {
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) - 1));
        }

        this.rotationYaw %= 360.0F;
        this.rotationPitch %= 360.0F;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte var20 = 5;
        double var2 = 0.0D;
        int var4;

        for (var4 = 0; var4 < var20; ++var4)
        {
            double var5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var4 + 0) / var20 - 0.125D;
            double var7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var4 + 1) / var20 - 0.125D;
        }

        double var21;

        if (var2 < 1.0D)
        {
            var21 = var2 * 2.0D - 1.0D;
            this.motionY += 0.04D * var21;
        }
        else
        {
            if (this.motionY < 0.0D)
            {
                this.motionY /= 2.0D;
            }

            this.motionY += 0.007D;
        }

        if (this.fuel <= 0)
        {
            for (var4 = 0; var4 < this.getSizeInventory(); ++var4)
            {
                ItemStack var22 = this.getStackInSlot(var4);

                if (var22 != null && var22.itemID == Item.coal.shiftedIndex)
                {
                    this.decrStackSize(var4, 1);
                    this.fuel += 1500;

                    if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer)
                    {
                        EntityPlayer var6 = (EntityPlayer)this.riddenByEntity;
                        var6.addChatMessage("Added Fuel");
                        break;
                    }
                }
            }
        }

        if (this.riddenByEntity != null)
        {
            if (Keyboard.isKeyDown(30))
            {
                this.rotationYaw = (float)(this.rotationYaw - this.turn * (1.0D + this.speed / 2.0D));
            }

            if (Keyboard.isKeyDown(32))
            {
                this.rotationYaw = (float)(this.rotationYaw + this.turn * (1.0D + this.speed / 2.0D));
            }

            if (Keyboard.isKeyDown(17))
            {
                this.speed += 0.02D;
            }

            if (Keyboard.isKeyDown(31))
            {
                this.speed -= 0.01D;
            }

            if (Keyboard.isKeyDown(42))
            {
                this.speed *= 0.75D;
            }

            this.fuel = (int)(this.fuel - this.speed);
        }
        else
        {
            this.speed *= 0.9D;
        }

        if (this.inWater && this.speed > 0.2D)
        {
            this.worldObj.playSoundEffect(((float)this.posX), ((float)this.posY), ((float)this.posZ), "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
        }

        this.speed *= 0.98D;

        if (this.speed > this.maxSpeed)
        {
            this.speed = this.maxSpeed;
        }
    	
        if (this.isCollidedHorizontally)
        {
            this.speed *= 0.9;
            this.motionY = 0.1D;
        }

        if (ModLoader.isGUIOpen((Class)null) && this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && Keyboard.isKeyDown(46))
        {
            EntityPlayer var23 = (EntityPlayer)this.riddenByEntity;

            if (!this.worldObj.isRemote)
            {
                var23.displayGUIChest(this);
            }
            else
            {
                System.out.println("sending packet");
            }
        }

        this.motionX = -(this.speed * Math.cos((this.rotationYaw - 90F) * Math.PI / 180.0D));
        this.motionZ = -(this.speed * Math.sin((this.rotationYaw - 90F) * Math.PI / 180.0D));
        var21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	protected void readEntityFromNBT(NBTTagCompound var1)
    {
        this.fuel = var1.getInteger("Fuel");
        NBTTagList var2 = var1.getTagList("Items");
        this.cargoItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.cargoItems.length)
            {
                this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("fuel", this.fuel);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
        {
            if (this.cargoItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.cargoItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        var1.setTag("Items", var2);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
	public int getSizeInventory()
    {
        return 27;
    }

    /**
     * Returns the stack in slot i
     */
    @Override
	public ItemStack getStackInSlot(int var1)
    {
        return this.cargoItems[var1];
    }
    
    @Override
	public void applyEntityCollision(Entity par1Entity)
    {
    	
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
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

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
	public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.cargoItems[var1] != null)
        {
            ItemStack var2 = this.cargoItems[var1];
            this.cargoItems[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
	public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.cargoItems[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    @Override
	public String getInvName()
    {
        return "Car";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
	public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    @Override
	public void onInventoryChanged() {}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
	public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
	public void openChest() {}

    @Override
	public void closeChest() {}

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
	public boolean interact(EntityPlayer var1)
    {
        ItemStack var2 = var1.inventory.getCurrentItem();

        if (this.worldObj.isRemote)
        {
            return true;
        }
        else if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != var1)
        {
            return true;
        }
        else
        {
            var1.mountEntity(this);
            return true;
        }
    }
}
