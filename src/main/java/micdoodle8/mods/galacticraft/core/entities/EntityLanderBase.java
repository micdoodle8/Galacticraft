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
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
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
        if (this.syncAdjustFlag && this.world.isBlockLoaded(new BlockPos(x, 255D, z)))
        {
            EntityPlayer p = FMLClientHandler.instance().getClientPlayerEntity();
            double dx = x - p.posX;
            double dz = z - p.posZ;
            if (dx * dx + dz * dz < 1024)
            {
                if (!this.world.loadedEntityList.contains(this))
                {
                    try {
                        this.world.loadedEntityList.add(this);
                    } catch (Exception e) { e.printStackTrace(); }
                }

                this.posX = x;
                this.posY = y;
                this.posZ = z;
                
                int cx = MathHelper.floor(x / 16.0D);
                int cz = MathHelper.floor(z / 16.0D);

                if (!this.addedToChunk || this.chunkCoordX != cx || this.chunkCoordZ != cz)
                {
                    if (this.addedToChunk && this.world.isBlockLoaded(new BlockPos(this.chunkCoordX << 4, 255, this.chunkCoordZ << 4), true))
                    {
                        this.world.getChunkFromChunkCoords(this.chunkCoordX, this.chunkCoordZ).removeEntityAtIndex(this, this.chunkCoordY);
                    }

                    this.addedToChunk = true;
                    this.world.getChunkFromChunkCoords(cx, cz).addEntity(this);
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
        this(player.world, player.posX, player.posY, player.posZ, yOffset);

        GCPlayerStats stats = GCPlayerStats.get(player);
        this.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, stats.getFuelLevel()));

        for (int i = 0; i < stats.getRocketStacks().size(); i++)
        {
            if (!stats.getRocketStacks().get(i).isEmpty())
            {
                this.stacks.set(i, stats.getRocketStacks().get(i).copy());
            }
            else
            {
                this.stacks.get(i).setCount(0);
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
                final EntityPlayer player = this.world.getClosestPlayerToEntity(this, 5);

                if (player != null && player.getRidingEntity() == null)
                {
                    player.startRiding(this);
                }
            }
        }

        if (!this.world.isRemote)
        {
            this.checkFluidTankTransfer(this.stacks.size() - 1, this.fuelTank);
        }

        AxisAlignedBB box = this.getEntityBoundingBox().grow(0.2D, 0.4D, 0.2D);

        final List<Entity> var15 = this.world.getEntitiesWithinAABBExcludingEntity(this, box);

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
        FluidUtil.tryFillContainerFuel(tank, this.stacks, slot);
    }

    private void pushEntityAway(Entity entityToPush)
    {
        if (!this.getPassengers().contains(entityToPush) && this.getRidingEntity() != entityToPush)
        {
            double d0 = this.posX - entityToPush.posX;
            double d1 = this.posZ - entityToPush.posZ;
            double d2 = MathHelper.absMax(d0, d1);

            if (d2 >= 0.009999999776482582D)
            {
                d2 = MathHelper.sqrt(d2);
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
        int invSize = nbt.getInteger("rocketStacksLength");
        if (invSize < 3)
        {
            invSize = 3;
        }
        this.stacks = NonNullList.withSize(invSize, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.stacks);

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
        if (world.isRemote) return;
        nbt.setInteger("rocketStacksLength", this.stacks.size());

        ItemStackHelper.saveAllItems(nbt, this.stacks);

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
        if (this.world.isRemote)
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

        if (!this.world.isRemote)
        {
            Integer cargoLength = this.stacks != null ? this.stacks.size() : 0;
            objList.add(cargoLength);
            objList.add(this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount);
        }

        if (this.world.isRemote)
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
            if (this.world.isRemote)
            {
                if (!this.hasReceivedPacket)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
                this.hasReceivedPacket = true;
                }

                int cargoLength = buffer.readInt();
                if (this.stacks == null || this.stacks.isEmpty())
                {
                    this.stacks = NonNullList.withSize(cargoLength, ItemStack.EMPTY);
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
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.world.getDifficulty().getDifficultyId(), e.world.getWorldInfo().getTerrainType().getName(), ((EntityPlayerMP) e).interactionManager.getGameType().getID());
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
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.world.getDifficulty().getDifficultyId(), e.world.getWorldInfo().getTerrainType().getName(), ((EntityPlayerMP) e).interactionManager.getGameType().getID());
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
        return this.stacks;
    }

    @Override
    public int getSizeInventory()
    {
        return this.stacks.size();
    }

    @Override
    public void setSizeInventory(int size)
    {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
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
    public int getBrightnessForRender()
    {
        double height = this.posY + (double)this.getEyeHeight();
        if (height > 255D) height = 255D;
        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
    }
}