package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class EntityLanderBase extends EntityAdvancedMotion implements IInventorySettable, IScaleableFuelLevel
{
    private final int FUEL_TANK_CAPACITY = 5000;
    public FluidTank fuelTank = new FluidTank(this.FUEL_TANK_CAPACITY);
    protected boolean hasReceivedPacket;
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private Boolean shouldMoveClient;
    private Boolean shouldMoveServer;
    private ArrayList prevData;
    private boolean networkDataChanged;
    private boolean syncAdjustFlag = true;

    public EntityLanderBase(World var1)
    {
        super(var1);
        this.setSize(3.0F, 3.0F);
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
    }

    @Override
    public boolean shouldSendAdvancedMotionPacket()
    {
        return this.shouldMoveClient != null && this.shouldMoveServer != null;
    }

    @Override
    public boolean canSetPositionClient()
    {
        return this.shouldSendAdvancedMotionPacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, b);
        if (this.syncAdjustFlag && this.worldObj.isBlockLoaded(new BlockPos(x, 255D, z)))
        {
            EntityPlayer p = FMLClientHandler.instance().getClientPlayerEntity();
            double dx = x - p.posX;
            double dz = z - p.posZ;
            if (dx * dx + dz * dz < 1024)
            {
                if (!this.worldObj.loadedEntityList.contains(this))
                {
                    try {
                        this.worldObj.loadedEntityList.add(this);
                    } catch (Exception e) { e.printStackTrace(); }
                }

                this.posX = x;
                this.posY = y;
                this.posZ = z;
                
                int cx = MathHelper.floor_double(x / 16.0D);
                int cz = MathHelper.floor_double(z / 16.0D);

                if (!this.addedToChunk || this.chunkCoordX != cx || this.chunkCoordZ != cz)
                {
                    if (this.addedToChunk && this.worldObj.isBlockLoaded(new BlockPos(this.chunkCoordX << 4, 255, this.chunkCoordZ << 4), true))
                    {
                        this.worldObj.getChunkFromChunkCoords(this.chunkCoordX, this.chunkCoordZ).removeEntityAtIndex(this, this.chunkCoordY);
                    }

                    this.addedToChunk = true;
                    this.worldObj.getChunkFromChunkCoords(cx, cz).addEntity(this);
                }
                
                this.syncAdjustFlag = false;
            }
        }
    }
    
    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.FUEL_TANK_CAPACITY);
    }

    public EntityLanderBase(World var1, double var2, double var4, double var6, float yOffset)
    {
        this(var1);
        this.setPosition(var2, var4, var6);
    }

    public EntityLanderBase(EntityPlayerMP player, float yOffset)
    {
        this(player.worldObj, player.posX, player.posY, player.posZ, yOffset);

        GCPlayerStats stats = GCPlayerStats.get(player);
        this.containedItems = new ItemStack[stats.getRocketStacks().length + 1];
        this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().length; i++)
        {
            if (stats.getRocketStacks()[i] != null)
            {
                this.containedItems[i] = stats.getRocketStacks()[i].copy();
            }
            else
            {
                this.containedItems[i] = null;
            }
        }

        this.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

        player.startRiding(this, true);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticks < 40 && this.posY > 150)
        {
            if (this.getPassengers().isEmpty())
            {
                final EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 5);

                if (player != null && player.getRidingEntity() == null)
                {
                    player.startRiding(this);
                }
            }
        }

        if (!this.worldObj.isRemote)
        {
            this.checkFluidTankTransfer(this.containedItems.length - 1, this.fuelTank);
        }

        AxisAlignedBB box = this.getEntityBoundingBox().expand(0.2D, 0.4D, 0.2D);

        final List<Entity> var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (Entity entity : var15)
            {
                if (!this.getPassengers().contains(entity))
                {
                    this.pushEntityAway(entity);
                }
            }
        }
    }


    private void checkFluidTankTransfer(int slot, FluidTank tank)
    {
        FluidUtil.tryFillContainerFuel(tank, this.containedItems, slot);
    }

    private void pushEntityAway(Entity entityToPush)
    {
        if (!this.getPassengers().contains(entityToPush) && this.getRidingEntity() != entityToPush)
        {
            double d0 = this.posX - entityToPush.posX;
            double d1 = this.posZ - entityToPush.posZ;
            double d2 = MathHelper.abs_max(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = MathHelper.sqrt_double(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= 1.0F - entityToPush.entityCollisionReduction;
                d1 *= 1.0F - entityToPush.entityCollisionReduction;
                entityToPush.addVelocity(-d0, 0.0D, -d1);
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = nbt.getTagList("Items", 10);

        int invSize = nbt.getInteger("rocketStacksLength");
        if (invSize < 3)
        {
            invSize = 3;
        }
        this.containedItems = new ItemStack[invSize];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containedItems.length)
            {
                this.containedItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }

        if (nbt.hasKey("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
    	if (worldObj.isRemote) return;
        final NBTTagList nbttaglist = new NBTTagList();

        nbt.setInteger("rocketStacksLength", this.containedItems.length);

        for (int i = 0; i < this.containedItems.length; ++i)
        {
            if (this.containedItems[i] != null)
            {
                final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.containedItems[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbt.setTag("Items", nbttaglist);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }

        UUID id = this.getOwnerUUID();

        if (id != null)
        {
            nbt.setLong("RiderUUID_LSB", id.getLeastSignificantBits());
            nbt.setLong("RiderUUID_MSB", id.getMostSignificantBits());
        }
    }

    @Override
    public boolean shouldMove()
    {
        if (this.shouldMoveClient == null || this.shouldMoveServer == null)
        {
            return false;
        }

        if (this.ticks < 40)
        {
            return false;
        }

        return !this.onGround;
    }

    public abstract double getInitialMotionY();

    @Override
    public void tickInAir()
    {
        if (this.worldObj.isRemote)
        {
            if (!this.shouldMove())
            {
                this.motionY = this.motionX = this.motionZ = 0.0F;
            }

            if (this.shouldMove() && !this.lastShouldMove)
            {
                this.motionY = this.getInitialMotionY();
            }

            this.lastShouldMove = this.shouldMove();
        }
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();

        if (!this.worldObj.isRemote)
        {
            Integer cargoLength = this.containedItems != null ? this.containedItems.length : 0;
            objList.add(cargoLength);
            objList.add(this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);
        }

        if (this.worldObj.isRemote)
        {
            this.shouldMoveClient = this.shouldMove();
            objList.add(this.shouldMoveClient);
        }
        else
        {
            this.shouldMoveServer = this.shouldMove();
            objList.add(this.shouldMoveServer);
            //Server send rider information for client to check
            objList.add(this.getPassengers().isEmpty() ? -1 : this.getPassengers().get(0).getEntityId());
        }

        this.networkDataChanged = !objList.equals(this.prevData);
        this.prevData = objList;
        return objList;
    }

    @Override
    public boolean networkedDataChanged()
    {
        return this.networkDataChanged || this.shouldMoveClient == null || this.shouldMoveServer == null;
    }

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    @Override
    public int getPacketTickSpacing()
    {
        return 2;
    }

    @Override
    public double getPacketSendDistance()
    {
        return 250.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            if (this.worldObj.isRemote)
            {
                if (!this.hasReceivedPacket)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
                this.hasReceivedPacket = true;
                }

                int cargoLength = buffer.readInt();
                if (this.containedItems == null || this.containedItems.length == 0)
                {
                    this.containedItems = new ItemStack[cargoLength];
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
                }

                this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, buffer.readInt()));

                this.shouldMoveServer = buffer.readBoolean();

                //Check has correct rider on client
                int shouldBeMountedId = buffer.readInt();
                if (this.getPassengers().isEmpty())
                {
                    if (shouldBeMountedId > -1)
                    {
                        Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof EntityPlayer)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP) e).interactionManager.getGameType().getID());
                                    e.startRiding(this);
                                    this.syncAdjustFlag = true;
                                }
                            }
                            else
                            {
                                e.startRiding(this);
                                this.syncAdjustFlag = true;
                            }
                        }
                    }
                }
                else if (this.getPassengers().get(0).getEntityId() != shouldBeMountedId)
                {
                    if (shouldBeMountedId == -1)
                    {
                        this.removePassengers();
                    }
                    else
                    {
                        Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof EntityPlayer)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP) e).interactionManager.getGameType().getID());
                                    e.startRiding(this, true);
                                    this.syncAdjustFlag = true;
                                }
                            }
                            else
                            {
                                e.startRiding(this, true);
                                this.syncAdjustFlag = true;
                            }
                        }
                    }
                }
            }
            else
            {
                this.shouldMoveClient = buffer.readBoolean();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean allowDamageSource(DamageSource damageSource)
    {
        return !damageSource.isExplosion();
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        return new ArrayList<ItemStack>(Arrays.asList(this.containedItems));
    }

    @Override
    public int getSizeInventory()
    {
        return this.containedItems.length;
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.containedItems = new ItemStack[size];
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return false;
    }

    @Override
    public double getPacketRange()
    {
        return 50.0D;
    }

    @Override
    public UUID getOwnerUUID()
    {
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof EntityPlayer))
        {
            return null;
        }

        UUID id;

        if (!this.getPassengers().isEmpty())
        {
            id = this.getPassengers().get(0).getPersistentID();

            this.persistantRiderUUID = id;
        }
        else
        {
            id = this.persistantRiderUUID;
        }

        return id;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks)
    {
        double height = this.posY + (double)this.getEyeHeight();
        if (height > 255D) height = 255D;
        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
    }
}