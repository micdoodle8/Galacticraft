package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.network.*;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate.IEntityFullSync;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityBuggy extends Entity implements IInventory, IPacketReceiver, IDockable, IControllableEntity, IEntityFullSync
{
    public enum BuggyType
    {
        NO_INVENTORY(0, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_0.png")),
        INVENTORY_1(18, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_1.png")),
        INVENTORY_2(36, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_2.png")),
        INVENTORY_3(54, new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/buggy_3.png"));

        private final int invSize;
        private final ResourceLocation textureLoc;

        BuggyType(int invSize, ResourceLocation textureLoc)
        {
            this.invSize = invSize;
            this.textureLoc = textureLoc;
        }

        public int getInvSize()
        {
            return invSize;
        }

        public static BuggyType byId(int id)
        {
            return values()[id];
        }

        public ResourceLocation getTextureLoc()
        {
            return textureLoc;
        }
    }

    private static final DataParameter<Integer> CURRENT_DAMAGE = EntityDataManager.createKey(EntityBuggy.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBuggy.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> ROCK_DIRECTION = EntityDataManager.createKey(EntityBuggy.class, DataSerializers.VARINT);
    public static final int tankCapacity = 1000;
    public FluidTank buggyFuelTank = new FluidTank(EntityBuggy.tankCapacity);
    protected long ticks = 0;
    public BuggyType buggyType;
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
    private NonNullList<ItemStack> stacks = NonNullList.withSize(60, ItemStack.EMPTY);
    public double boatX;
    public double boatY;
    public double boatZ;
    public double boatYaw;
    public double boatPitch;
    public int boatPosRotationIncrements;
    private IFuelDock landingPad;
    private int timeClimbing;
    private boolean shouldClimb;

    public EntityBuggy(EntityType<EntityBuggy> type, World world)
    {
        super(type, world);
//        this.setSize(1.4F, 0.6F);
        this.speed = 0.0D;
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
//        this.isImmuneToFire = true;

        if (world.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    public void setBuggyType(BuggyType type)
    {
        if (this.buggyType != null)
        {
            throw new RuntimeException("Cannot change buggy type");
        }
        this.buggyType = type;
        this.stacks = NonNullList.withSize(this.buggyType.getInvSize(), ItemStack.EMPTY);
    }

    public static Item getItemFromType(BuggyType buggyType)
    {
        switch (buggyType)
        {
        default:
        case NO_INVENTORY:
            return GCItems.buggy;
        case INVENTORY_1:
            return GCItems.buggyInventory1;
        case INVENTORY_2:
            return GCItems.buggyInventory2;
        case INVENTORY_3:
            return GCItems.buggyInventory3;
        }
    }

    public static BuggyType getTypeFromItem(Item item)
    {
        if (item == GCItems.buggy)
        {
            return BuggyType.NO_INVENTORY;
        }
        if (item == GCItems.buggyInventory1)
        {
            return BuggyType.INVENTORY_1;
        }
        if (item == GCItems.buggyInventory2)
        {
            return BuggyType.INVENTORY_2;
        }
        if (item == GCItems.buggyInventory3)
        {
            return BuggyType.INVENTORY_3;
        }
        return null;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    //    public EntityBuggy(World var1, double var2, double var4, double var6, int type)
//    {
//        this(var1);
//        this.setPosition(var2, var4, var6);
//        this.setBuggyType(type);
//        this.stacks = NonNullList.withSize(this.buggyType * 18, ItemStack.EMPTY);
//    }

    @Override
    protected void registerData()
    {
        this.dataManager.register(CURRENT_DAMAGE, 0);
        this.dataManager.register(TIME_SINCE_HIT, 0);
        this.dataManager.register(ROCK_DIRECTION, 1);
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.buggyFuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.buggyFuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / EntityBuggy.tankCapacity);
    }

//    public ModelBase getModel()
//    {
//        return null;
//    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(getItemFromType(getBuggyType()), 1);
    }

    public BuggyType getBuggyType()
    {
        return buggyType;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public double getMountedYOffset()
    {
        return this.getHeight() - 3.0D;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return this.isAlive();
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            final double offsetX = Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            final double offsetZ = Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D + 114.8) * -0.5D;
            passenger.setPosition(this.getPosX() + offsetX, this.getPosY() + 0.4F + passenger.getYOffset(), this.getPosZ() + offsetZ);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.world.isRemote)
        {
            this.boatX = x;
            this.boatY = y;
            this.boatZ = z;
            this.boatYaw = yaw;
            this.boatPitch = pitch;
            this.setMotion(motX, motY, motZ);
            this.boatPosRotationIncrements = 5;
        }
        else
        {
            this.setPosition(x, y, z);
            this.setRotation(yaw, pitch);
            this.setMotion(motX, motY, motZ);
        }
    }

    @Override
    public void performHurtAnimation()
    {
        this.dataManager.set(ROCK_DIRECTION, -this.dataManager.get(ROCK_DIRECTION));
        this.dataManager.set(TIME_SINCE_HIT, 10);
        this.dataManager.set(CURRENT_DAMAGE, this.dataManager.get(CURRENT_DAMAGE) * 5);
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        if (!this.isAlive() || var1.equals(DamageSource.CACTUS))
        {
            return true;
        }
        else
        {
            Entity e = var1.getTrueSource();
            boolean flag = e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode;

            if (this.isInvulnerableTo(var1) || (e instanceof LivingEntity && !(e instanceof PlayerEntity)))
            {
                return false;
            }
            else
            {
                this.dataManager.set(ROCK_DIRECTION, -this.dataManager.get(ROCK_DIRECTION));
                this.dataManager.set(TIME_SINCE_HIT, 10);
                this.dataManager.set(CURRENT_DAMAGE, (int) (this.dataManager.get(CURRENT_DAMAGE) + var2 * 10));
                this.markVelocityChanged();

                if (e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode)
                {
                    this.dataManager.set(CURRENT_DAMAGE, 100);
                }

                if (flag || this.dataManager.get(CURRENT_DAMAGE) > 2)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.removePassengers();
                    }

                    if (flag)
                    {
                        this.remove();
                    }
                    else
                    {
                        this.remove();
                        if (!this.world.isRemote)
                        {
                            this.dropBuggyAsItem();
                        }
                    }
                    this.remove();
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
            ItemEntity entityItem = this.entityDropItem(item, 0);

            if (item.hasTag())
            {
                entityItem.getItem().setTag(item.getTag().copy());
            }
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();

        ItemStack buggy = new ItemStack(getItemFromType(getBuggyType()), 1);
        buggy.setTag(new CompoundNBT());
        buggy.getTag().putInt("BuggyFuel", this.buggyFuelTank.getFluidAmount());
        items.add(buggy);

        for (ItemStack item : this.stacks)
        {
            if (!item.isEmpty())
            {
                items.add(item);
            }
        }

        return items;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        if (!this.getPassengers().isEmpty())
        {
            if (!this.getPassengers().contains(Minecraft.getInstance().player))
            {
                this.boatPosRotationIncrements = posRotationIncrements + 5;
                this.boatX = x;
                this.boatY = y;
                this.boatZ = z;
                this.boatYaw = yaw;
                this.boatPitch = pitch;
            }
        }
    }

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.world.isRemote)
        {
            this.wheelRotationX += Math.sqrt(this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z) * 150.0F * (this.speed < 0 ? 1 : -1);
            this.wheelRotationX %= 360;
            this.wheelRotationZ = Math.max(-30.0F, Math.min(30.0F, this.wheelRotationZ * 0.9F));
        }

        if (this.world.isRemote && !Minecraft.getInstance().player.equals(this.world.getClosestPlayer(this, -1)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.boatPosRotationIncrements > 0)
            {
                x = this.getPosX() + (this.boatX - this.getPosX()) / this.boatPosRotationIncrements;
                y = this.getPosY() + (this.boatY - this.getPosY()) / this.boatPosRotationIncrements;
                z = this.getPosZ() + (this.boatZ - this.getPosZ()) / this.boatPosRotationIncrements;
                var12 = MathHelper.wrapDegrees(this.boatYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + var12 / this.boatPosRotationIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                x = this.getPosX() + this.getMotion().x;
                y = this.getPosY() + this.getMotion().y;
                z = this.getPosZ() + this.getMotion().z;
                if (!this.getPassengers().isEmpty())
                {
                    this.setPosition(x, y, z);
                }

                if (this.onGround)
                {
                    this.setMotion(this.getMotion().mul(0.5, 0.5, 0.5));
                }

                this.setMotion(this.getMotion().mul(0.9900000095367432D, 0.949999988079071D, 0.9900000095367432D));
            }
            return;
        }

        if (this.dataManager.get(TIME_SINCE_HIT) > 0)
        {
            this.dataManager.set(TIME_SINCE_HIT, this.dataManager.get(TIME_SINCE_HIT) - 1);
        }

        if (this.dataManager.get(CURRENT_DAMAGE) > 0)
        {
            this.dataManager.set(CURRENT_DAMAGE, this.dataManager.get(CURRENT_DAMAGE) - 1);
        }

        if (!this.onGround)
        {
            this.setMotion(this.getMotion().add(0.0, -TransformerHooks.getGravityForEntity(this) * 0.5D, 0.0));
        }

        if (this.inWater && this.speed > 0.2D)
        {
            this.world.playSound(null, (float) this.getPosX(), (float) this.getPosY(), (float) this.getPosZ(), SoundEvents.ENTITY_GENERIC_BURN, SoundCategory.NEUTRAL, 0.5F, 2.6F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.8F);
        }

        this.speed *= 0.98D;

        if (this.speed > this.maxSpeed)
        {
            this.speed = this.maxSpeed;
        }

        if (this.collidedHorizontally && this.shouldClimb)
        {
            this.speed *= 0.9;
            double motY = 0.15D * ((-Math.pow((this.timeClimbing) - 1, 2)) / 250.0F) + 0.15F;
            motY = Math.max(-0.15, motY);
            this.setMotion(this.getMotion().x, motY, this.getMotion().z);
            this.shouldClimb = false;
        }

        if ((this.getMotion().x == 0 || this.getMotion().z == 0) && !this.onGround)
        {
            this.timeClimbing++;
        }
        else
        {
            this.timeClimbing = 0;
        }

        if (this.world.isRemote && this.buggyFuelTank.getFluid() != FluidStack.EMPTY && this.buggyFuelTank.getFluid().getAmount() > 0)
        {
            this.setMotion(-(this.speed * Math.cos((this.rotationYaw - 90F) / Constants.RADIANS_TO_DEGREES_D)), getMotion().y, -(this.speed * Math.sin((this.rotationYaw - 90F) / Constants.RADIANS_TO_DEGREES_D)));
        }

        if (this.world.isRemote)
        {
            this.move(MoverType.SELF, this.getMotion());
        }

        if (!this.world.isRemote && Math.abs(this.getMotion().x * this.getMotion().z) > 0.000001)
        {
            double d = this.getMotion().x * this.getMotion().x + this.getMotion().z * this.getMotion().z;

            if (d != 0 && this.ticks % (MathHelper.floor(2 / d) + 1) == 0)
            {
                this.removeFuel(1);
            }
        }

        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();

        if (this.world.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
        }
        else if (this.ticks % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 50.0D, GCCoreUtil.getDimensionType(this.world)));
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new PacketDistributor.TargetPoint(this.getPosX(), this.getPosY(), this.getPosZ(), 50.0D, GCCoreUtil.getDimensionType(this.world)));
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (this.world.isRemote)
        {
            return;
        }
        sendData.add(this.buggyType);
        sendData.add(this.buggyFuelTank);
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.buggyType = BuggyType.values()[buffer.readInt()];

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
    protected void readAdditional(CompoundNBT nbt)
    {
        this.buggyType = BuggyType.values()[nbt.getInt("buggyType")];
        ItemStackHelper.loadAllItems(nbt, this.stacks);

        if (nbt.contains("fuelTank"))
        {
            this.buggyFuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        if (world.isRemote)
        {
            return;
        }
        nbt.putInt("buggyType", this.buggyType.ordinal());
        final ListNBT var2 = new ListNBT();

        if (this.buggyFuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.buggyFuelTank.writeToNBT(new CompoundNBT()));
        }

        ItemStackHelper.saveAllItems(nbt, stacks);
    }

    @Override
    public int getSizeInventory()
    {
        return this.buggyType.getInvSize();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.stacks.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stacks, index, count);

        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.stacks, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.stacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity var1)
    {
        return this.isAlive() && var1.getDistanceSq(this) <= 64.0D;
    }

    @Override
    public void markDirty()
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void openInventory(PlayerEntity player)
    {
    }

    //We don't use these because we use forge containers
    @Override
    public void closeInventory(PlayerEntity player)
    {
    }

    @Override
    public void clear()
    {

    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand)
    {
        if (this.world.isRemote)
        {
            if (this.getPassengers().isEmpty())
            {
                player.sendMessage(new StringTextComponent(KeyHandlerClient.leftKey.getLocalizedName() + " / " + KeyHandlerClient.rightKey.getLocalizedName() + "  - " + GCCoreUtil.translate("gui.buggy.turn")));
                player.sendMessage(new StringTextComponent(KeyHandlerClient.accelerateKey.getLocalizedName() + "       - " + GCCoreUtil.translate("gui.buggy.accel")));
                player.sendMessage(new StringTextComponent(KeyHandlerClient.decelerateKey.getLocalizedName() + "       - " + GCCoreUtil.translate("gui.buggy.decel")));
                player.sendMessage(new StringTextComponent(KeyHandlerClient.openFuelGui.getLocalizedName() + "       - " + GCCoreUtil.translate("gui.buggy.inv")));
            }

            return true;
        }
        else
        {
            if (this.getPassengers().contains(player))
            {
                this.removePassenger(player);

                return true;
            }
            else
            {
                player.startRiding(this);
                return true;
            }
        }
    }

    @Override
    public boolean pressKey(int key)
    {
        if (this.world.isRemote && (key == 6 || key == 8 || key == 9))
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_CONTROL_ENTITY, GCCoreUtil.getDimensionType(this.world), new Object[]{key}));
            return true;
        }

        switch (key)
        {
        case 0: // Accelerate
            this.speed += this.accel / 20D;
            this.shouldClimb = true;
            return true;
        case 1: // Deccelerate
            this.speed -= this.accel / 20D;
            this.shouldClimb = true;
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
    public int addFuel(FluidStack liquid, IFluidHandler.FluidAction action)
    {
        if (this.landingPad != null)
        {
            return FluidUtil.fillWithGCFuel(this.buggyFuelTank, liquid, action);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.buggyFuelTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.buggyType == BuggyType.NO_INVENTORY)
        {
            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (stackAt.getItem() == stack.getItem() && stackAt.getCount() < stackAt.getMaxStackSize())
            {
                if (stackAt.getCount() + stack.getCount() <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        stackAt.grow(stack.getCount());
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.getCount();
                    int surplus = origSize + stack.getCount() - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        stackAt.setCount(stackAt.getMaxStackSize());
                        this.markDirty();
                    }

                    stack.setCount(surplus);
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    stackAt.setCount(origSize);
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (!stackAt.isEmpty())
            {
                if (doAdd)
                {
                    this.stacks.set(count, stack);
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
        for (int i = 0; i < this.stacks.size(); i++)
        {
            ItemStack stackAt = this.getStackInSlot(i);

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.setCount(1);

                if (doRemove)
                {
                    stackAt.shrink(1);
                    this.markDirty();
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, ItemStack.EMPTY);
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

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public UUID getOwnerUUID()
    {
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof PlayerEntity))
        {
            return null;
        }

        return !this.getPassengers().isEmpty() ? this.getPassengers().get(0).getUniqueID() : null;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.stacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean inFlight()
    {
        return false;
    }
}
