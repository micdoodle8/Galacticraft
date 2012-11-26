package micdoodle8.mods.galacticraft.core.entities;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockRail;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import net.minecraftforge.common.IMinecartCollisionHandler;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCCoreEntityBuggy extends Entity implements IInventory
{
    /** Array of item stacks stored in minecart (for storage minecarts). */
    protected ItemStack[] cargoItems;
    protected int fuel;
    protected boolean field_70499_f;

    /** The type of minecart, 2 for powered, 1 for storage. */
    public int minecartType;
    public double pushX;
    public double pushZ;
//    protected final IUpdatePlayerListBox field_82344_g;
    protected boolean field_82345_h;
    protected static final int[][][] field_70500_g = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};

    /** appears to be the progress of the turn */
    protected int turnProgress;
    protected double minecartX;
    protected double minecartY;
    protected double minecartZ;
    protected double minecartYaw;
    protected double minecartPitch;
    @SideOnly(Side.CLIENT)
    protected double velocityX;
    @SideOnly(Side.CLIENT)
    protected double velocityY;
    @SideOnly(Side.CLIENT)
    protected double velocityZ;

    /* Forge: Minecart Compatibility Layer Integration. */
    public static float defaultMaxSpeedRail = 0.4f;
    public static float defaultMaxSpeedGround = 0.4f;
    public static float defaultMaxSpeedAirLateral = 0.4f;
    public static float defaultMaxSpeedAirVertical = -1f;
    public static double defaultDragRidden = 0.996999979019165D;
    public static double defaultDragEmpty = 0.9599999785423279D;
    public static double defaultDragAir = 0.94999998807907104D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static IMinecartCollisionHandler collisionHandler = null;

    /* Instance versions of the above physics properties */
    protected float maxSpeedRail;
    protected float maxSpeedGround;
    protected float maxSpeedAirLateral;
    protected float maxSpeedAirVertical;
    protected double dragAir;
    
    public float forwardAcceleration;

    public GCCoreEntityBuggy(World par1World)
    {
        super(par1World);
        this.cargoItems = new ItemStack[36];
        this.fuel = 0;
        this.field_70499_f = false;
        this.field_82345_h = true;
        this.preventEntitySpawning = true;
        this.setSize(3F, 3F);
        this.yOffset = this.height / 2.0F;
//        this.field_82344_g = par1World != null ? par1World.func_82735_a(this) : null; TODO

        maxSpeedRail = defaultMaxSpeedRail;
        maxSpeedGround = defaultMaxSpeedGround;
        maxSpeedAirLateral = defaultMaxSpeedAirLateral;
        maxSpeedAirVertical = defaultMaxSpeedAirVertical;
        dragAir = defaultDragAir;
    }

    public GCCoreEntityBuggy(World world, int type)
    {
        this(world);
        minecartType = type;
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
    }

    @Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    @Override
	public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
    
    public boolean canBePushed()
    {
        return canBePushed;
    }

    public GCCoreEntityBuggy(World par1World, double par2, double par4, double par6)
    {
        this(par1World);
        this.setPosition(par2, par4 + (double)this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.height - 4.0D;
//        return (double)this.height - 1.5D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.func_85032_ar())
            {
                return false;
            }
            else
            {
                this.func_70494_i(-this.func_70493_k());
                this.func_70497_h(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + par2 * 10);

                if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode)
                {
                    this.setDamage(100);
                }

                if (this.getDamage() > 40)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity(this);
                    }

                    this.setDead();
//                    dropCartAsItem(); TODO
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.func_70494_i(-this.func_70493_k());
        this.func_70497_h(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10);
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public void setDead()
    {
        if (this.field_82345_h)
        {
            for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
            {
                ItemStack var2 = this.getStackInSlot(var1);

                if (var2 != null)
                {
                    float var3 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var5 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var2.stackSize > 0)
                    {
                        int var6 = this.rand.nextInt(21) + 10;

                        if (var6 > var2.stackSize)
                        {
                            var6 = var2.stackSize;
                        }

                        var2.stackSize -= var6;
                        EntityItem var7 = new EntityItem(this.worldObj, this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, new ItemStack(var2.itemID, var6, var2.getItemDamage()));

                        if (var2.hasTagCompound())
                        {
                            var7.item.setTagCompound((NBTTagCompound)var2.getTagCompound().copy());
                        }

                        float var8 = 0.05F;
                        var7.motionX = (double)((float)this.rand.nextGaussian() * var8);
                        var7.motionY = (double)((float)this.rand.nextGaussian() * var8 + 0.2F);
                        var7.motionZ = (double)((float)this.rand.nextGaussian() * var8);
                        this.worldObj.spawnEntityInWorld(var7);
                    }
                }
            }
        }

        super.setDead();

//        if (this.field_82344_g != null)
//        {
//            this.field_82344_g.update();
//        } TODO
    }

    public void onUpdate()
    {
    	super.onUpdate();
    	
        if (ModLoader.getMinecraftInstance() != null && ModLoader.getMinecraftInstance().theWorld != null)
        {
            if (this.worldObj instanceof WorldServer && ModLoader.getMinecraftInstance().theWorld.getEntityByID(this.entityId) == null)
            {
                GCCoreEntityBuggy var1 = new GCCoreEntityBuggy(ModLoader.getMinecraftInstance().theWorld);
                var1.setPosition(this.posX, this.posY, this.posZ);
                ModLoader.getMinecraftInstance().theWorld.addEntityToWorld(this.entityId, var1);
            }

            if (this.worldObj instanceof WorldServer)
            {
            	GCCoreEntityBuggy var19 = (GCCoreEntityBuggy)ModLoader.getMinecraftInstance().theWorld.getEntityByID(this.entityId);
                var19.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                var19.setVelocity(this.motionX, this.motionY, this.motionZ);
                var19.forwardAcceleration = this.forwardAcceleration;
            }
        }
//        if (this.field_82344_g != null)
//        {
//            this.field_82344_g.update();
//        } TODO

        if (this.func_70496_j() > 0)
        {
            this.func_70497_h(this.func_70496_j() - 1);
        }

        if (this.getDamage() > 0)
        {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0 && minecartType == 2 && getClass() == GCCoreEntityBuggy.class)
        {
            this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        
        this.rotationYaw %= 360.0F;
        this.rotationPitch %= 360.0F;

        AxisAlignedBB box = null;
        box = boundingBox.expand(2D, 1.0D, 2D);

        List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var50 = 0; var50 < var15.size(); ++var50)
            {
                Entity var17 = (Entity)var15.get(var50);

                if (var17 != this.riddenByEntity && var17.canBePushed() && var17 instanceof EntityMinecart)
                {
                    var17.applyEntityCollision(this);
                }
            }
        }
        
        if (this.riddenByEntity != null )
        {
        	this.riddenByEntity.rotationPitch = this.rotationPitch;
        	
        	this.riddenByEntity.rotationYaw = this.rotationYaw;
        	
            if (Keyboard.isKeyDown(30))
            {
                this.rotationYaw = (float)((double)this.rotationYaw - 1.0D * (1.0D + this.forwardAcceleration / 2.0D));
            }

            if (Keyboard.isKeyDown(32))
            {
                this.rotationYaw = (float)((double)this.rotationYaw + 1.0D * (1.0D + this.forwardAcceleration / 2.0D));
            }

            if (Keyboard.isKeyDown(17))
            {
                this.forwardAcceleration += 0.02D;
            }

            if (Keyboard.isKeyDown(31))
            {
                this.forwardAcceleration -= 0.01D;
            }

            if (Keyboard.isKeyDown(42))
            {
                this.forwardAcceleration *= 0.75D;
            }

            this.fuel = (int)((double)this.fuel - this.forwardAcceleration);
        }
        else
        {
            this.forwardAcceleration *= 0.9D;
        }

        this.forwardAcceleration *= 0.98D;

        if (this.forwardAcceleration > 2.0F)
        {
            this.forwardAcceleration = 2.0F;
        }

        if (this.isCollidedHorizontally)
        {
            this.forwardAcceleration = 0.0F;
            
            this.motionY += 0.1D;
        }
        
        this.motionX = -(this.forwardAcceleration * Math.cos((double)(this.rotationYaw - 90) * Math.PI / 180.0D));
        this.motionZ = -(this.forwardAcceleration * Math.sin((double)(this.rotationYaw - 90) * Math.PI / 180.0D));
        
        this.motionY -= 0.04D;
        
        moveEntity(motionX, motionY, motionZ);

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double var45 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double var46 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double var5 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(var45, var46, var5);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            int var1 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            int var3 = MathHelper.floor_double(this.posZ);

            if (BlockRail.isRailBlockAt(this.worldObj, var1, var2 - 1, var3))
            {
                --var2;
            }

            double var4 = 0.4D;
            double var6 = 0.0078125D;
            int var8 = this.worldObj.getBlockId(var1, var2, var3);
        }
    }
    
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("Type", this.minecartType);

//        if (isPoweredCart())
//        {
//            par1NBTTagCompound.setDouble("PushX", this.pushX);
//            par1NBTTagCompound.setDouble("PushZ", this.pushZ);
//            par1NBTTagCompound.setInteger("Fuel", this.fuel);
//        }

        if (getSizeInventory() > 0)
        {
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

            par1NBTTagCompound.setTag("Items", var2);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.minecartType = par1NBTTagCompound.getInteger("Type");

//        if (isPoweredCart())
//        {
//            this.pushX = par1NBTTagCompound.getDouble("PushX");
//            this.pushZ = par1NBTTagCompound.getDouble("PushZ");
//            try
//            {
//                this.fuel = par1NBTTagCompound.getInteger("Fuel");
//            }
//            catch (ClassCastException e)
//            {
//                this.fuel = par1NBTTagCompound.getShort("Fuel");
//            }
//        }

        if (getSizeInventory() > 0)
        {
            NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
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
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
//    public void applyEntityCollision(Entity par1Entity)
//    {
//        MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent(this, par1Entity));
//        if (getCollisionHandler() != null)
//        {
//            getCollisionHandler().onEntityCollision(this, par1Entity);
//            return;
//        }
//        if (!this.worldObj.isRemote)
//        {
//            if (par1Entity != this.riddenByEntity)
//            {
//                if (par1Entity instanceof EntityLiving && !(par1Entity instanceof EntityPlayer) && !(par1Entity instanceof EntityIronGolem) && canBeRidden() && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && par1Entity.ridingEntity == null)
//                {
//                    par1Entity.mountEntity(this);
//                }
//
//                double var2 = par1Entity.posX - this.posX;
//                double var4 = par1Entity.posZ - this.posZ;
//                double var6 = var2 * var2 + var4 * var4;
//
//                if (var6 >= 9.999999747378752E-5D)
//                {
//                    var6 = (double)MathHelper.sqrt_double(var6);
//                    var2 /= var6;
//                    var4 /= var6;
//                    double var8 = 1.0D / var6;
//
//                    if (var8 > 1.0D)
//                    {
//                        var8 = 1.0D;
//                    }
//
//                    var2 *= var8;
//                    var4 *= var8;
//                    var2 *= 0.10000000149011612D;
//                    var4 *= 0.10000000149011612D;
//                    var2 *= (double)(1.0F - this.entityCollisionReduction);
//                    var4 *= (double)(1.0F - this.entityCollisionReduction);
//                    var2 *= 0.5D;
//                    var4 *= 0.5D;
//
//                    if (par1Entity instanceof GCCoreEntityBuggy)
//                    {
//                        double var10 = par1Entity.posX - this.posX;
//                        double var12 = par1Entity.posZ - this.posZ;
//                        Vec3 var14 = this.worldObj.getWorldVec3Pool().getVecFromPool(var10, 0.0D, var12).normalize();
//                        Vec3 var15 = this.worldObj.getWorldVec3Pool().getVecFromPool((double)MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F)).normalize();
//                        double var16 = Math.abs(var14.dotProduct(var15));
//
//                        if (var16 < 0.800000011920929D)
//                        {
//                            return;
//                        }
//
//                        double var18 = par1Entity.motionX + this.motionX;
//                        double var20 = par1Entity.motionZ + this.motionZ;
//
//                        if (((GCCoreEntityBuggy)par1Entity).isPoweredCart() && !isPoweredCart())
//                        {
//                            this.motionX *= 0.20000000298023224D;
//                            this.motionZ *= 0.20000000298023224D;
//                            this.addVelocity(par1Entity.motionX - var2, 0.0D, par1Entity.motionZ - var4);
//                            par1Entity.motionX *= 0.949999988079071D;
//                            par1Entity.motionZ *= 0.949999988079071D;
//                        }
//                        else if (!((GCCoreEntityBuggy)par1Entity).isPoweredCart() && isPoweredCart())
//                        {
//                            par1Entity.motionX *= 0.20000000298023224D;
//                            par1Entity.motionZ *= 0.20000000298023224D;
//                            par1Entity.addVelocity(this.motionX + var2, 0.0D, this.motionZ + var4);
//                            this.motionX *= 0.949999988079071D;
//                            this.motionZ *= 0.949999988079071D;
//                        }
//                        else
//                        {
//                            var18 /= 2.0D;
//                            var20 /= 2.0D;
//                            this.motionX *= 0.20000000298023224D;
//                            this.motionZ *= 0.20000000298023224D;
//                            this.addVelocity(var18 - var2, 0.0D, var20 - var4);
//                            par1Entity.motionX *= 0.20000000298023224D;
//                            par1Entity.motionZ *= 0.20000000298023224D;
//                            par1Entity.addVelocity(var18 + var2, 0.0D, var20 + var4);
//                        }
//                    }
//                    else
//                    {
//                        this.addVelocity(-var2, 0.0D, -var4);
//                        par1Entity.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
//                    }
//                }
//            }
//        }
//    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return (minecartType == 1 && getClass() == GCCoreEntityBuggy.class ? 27 : 0);
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.cargoItems[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
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
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return "container.minecart";
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged() {}

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
//        if (canBeRidden())
//        {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer)
            {
                return true;
            }

            if (!this.worldObj.isRemote)
            {
                par1EntityPlayer.mountEntity(this);
            }
//        }
//        else if (getSizeInventory() > 0)
//        {
//            if (!this.worldObj.isRemote)
//            {
//                par1EntityPlayer.displayGUIChest(this);
//            }
//        }
//        else if (this.minecartType == 2 && getClass() == GCCoreEntityBuggy.class)
//        {
//            ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
//
//            if (var2 != null && var2.itemID == Item.coal.shiftedIndex)
//            {
//                if (--var2.stackSize == 0)
//                {
//                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
//                }
//
//                this.fuel += 3600;
//            }
//
//            this.pushX = this.posX - par1EntityPlayer.posX;
//            this.pushZ = this.posZ - par1EntityPlayer.posZ;
//        }

        return true;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.minecartX = par1;
        this.minecartY = par3;
        this.minecartZ = par5;
        this.minecartYaw = (double)par7;
        this.minecartPitch = (double)par8;
        this.turnProgress = par9 + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double par1, double par3, double par5)
    {
        this.velocityX = this.motionX = par1;
        this.velocityY = this.motionY = par3;
        this.velocityZ = this.motionZ = par5;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    /**
     * Is this minecart powered (Fuel > 0)
     */
    public boolean isMinecartPowered()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Set if this minecart is powered (Fuel > 0)
     */
    protected void setMinecartPowered(boolean par1)
    {
        if (par1)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & -2)));
        }
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public int getDamage()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void func_70497_h(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    public int func_70496_j()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void func_70494_i(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int func_70493_k()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
}
