package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.fluid.GCFluidRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.inventory.IInventorySettable;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

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

    public EntityLanderBase(EntityType<?> type, World world)
    {
        super(type, world);
//        this.setSize(3.0F, 3.0F);
    }

    public EntityLanderBase(EntityType<?> type, World world, double x, double y, double z)
    {
        this(type, world);
        this.setPosition(x, y, z);
    }

    public EntityLanderBase(EntityType<?> type, ServerPlayerEntity player)
    {
        this(type, player.world, player.getPosX(), player.getPosY(), player.getPosZ());

        GCPlayerStats stats = GCPlayerStats.get(player);
        this.stacks = NonNullList.withSize(stats.getRocketStacks().size() + 1, ItemStack.EMPTY);
        this.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), stats.getFuelLevel()));

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

        this.setPositionAndRotation(player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0);

        player.startRiding(this, true);
    }

    @Override
    public void updatePassenger(Entity passenger)
    {
        if (this.isPassenger(passenger))
        {
            passenger.setPosition(this.getPosX(), this.getPosY() + this.getMountedYOffset() + passenger.getYOffset(), this.getPosZ());
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
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        super.setPositionAndRotationDirect(x, y, z, yaw, pitch, posRotationIncrements, b);
        if (this.syncAdjustFlag && this.world.isBlockLoaded(new BlockPos(x, 255D, z)))
        {
            PlayerEntity p = Minecraft.getInstance().player;
            double dx = x - p.getPosX();
            double dz = z - p.getPosZ();
            if (dx * dx + dz * dz < 1024)
            {
                if (this.world.getEntityByID(this.getEntityId()) == null)
                {
                    try
                    {
                        ((ClientWorld) this.world).addEntity(this.getEntityId(), this);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                this.setRawPosition(x, y, z);

                int cx = MathHelper.floor(x / 16.0D);
                int cz = MathHelper.floor(z / 16.0D);

                if (!this.addedToChunk || this.chunkCoordX != cx || this.chunkCoordZ != cz)
                {
                    if (this.addedToChunk && this.world.isBlockLoaded(new BlockPos(this.chunkCoordX << 4, 255, this.chunkCoordZ << 4)))
                    {
                        this.world.getChunk(this.chunkCoordX, this.chunkCoordZ).removeEntityAtIndex(this, this.chunkCoordY);
                    }

                    this.addedToChunk = true;
                    this.world.getChunk(cx, cz).addEntity(this);
                }

                this.syncAdjustFlag = false;
            }
        }
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount();

        return (int) (fuelLevel * i / this.FUEL_TANK_CAPACITY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (this.ticks < 40 && this.getPosY() > 150)
        {
            if (this.getPassengers().isEmpty())
            {
                final PlayerEntity player = this.world.getClosestPlayer(this, 5);

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

        AxisAlignedBB box = this.getBoundingBox().grow(0.2D, 0.4D, 0.2D);

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
            double d0 = this.getPosX() - entityToPush.getPosX();
            double d1 = this.getPosZ() - entityToPush.getPosZ();
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
    protected void readAdditional(CompoundNBT nbt)
    {
        int invSize = nbt.getInt("rocketStacksLength");
        if (invSize < 3)
        {
            invSize = 3;
        }
        this.stacks = NonNullList.withSize(invSize, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.stacks);

        if (nbt.contains("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompound("fuelTank"));
        }

        if (nbt.contains("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        if (world.isRemote)
        {
            return;
        }
        nbt.putInt("rocketStacksLength", this.stacks.size());

        ItemStackHelper.saveAllItems(nbt, this.stacks);

        if (this.fuelTank.getFluid() != FluidStack.EMPTY)
        {
            nbt.put("fuelTank", this.fuelTank.writeToNBT(new CompoundNBT()));
        }

        UUID id = this.getOwnerUUID();

        if (id != null)
        {
            nbt.putLong("RiderUUID_LSB", id.getLeastSignificantBits());
            nbt.putLong("RiderUUID_MSB", id.getMostSignificantBits());
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
                this.setMotion(0.0, 0.0, 0.0);
            }

            if (this.shouldMove() && !this.lastShouldMove)
            {
                this.setMotion(getMotion().x, this.getInitialMotionY(), this.getMotion().z);
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
            objList.add(this.fuelTank.getFluid() == FluidStack.EMPTY ? 0 : this.fuelTank.getFluid().getAmount());
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

                this.fuelTank.setFluid(new FluidStack(GCFluids.FUEL.getFluid(), buffer.readInt()));

                this.shouldMoveServer = buffer.readBoolean();

                //Check has correct rider on client
                int shouldBeMountedId = buffer.readInt();
                if (this.getPassengers().isEmpty())
                {
                    if (shouldBeMountedId > -1)
                    {
                        Entity e = Minecraft.getInstance().world.getEntityByID(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof PlayerEntity)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.world.getWorldInfo().getGenerator(), ((ServerPlayerEntity) e).interactionManager.getGameType());
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
                        Entity e = Minecraft.getInstance().world.getEntityByID(shouldBeMountedId);
                        if (e != null)
                        {
                            if (e.dimension != this.dimension)
                            {
                                if (e instanceof PlayerEntity)
                                {
                                    e = WorldUtil.forceRespawnClient(this.dimension, e.world.getWorldInfo().getGenerator(), ((ServerPlayerEntity) e).interactionManager.getGameType());
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
        if (!this.getPassengers().isEmpty() && !(this.getPassengers().get(0) instanceof PlayerEntity))
        {
            return null;
        }

        UUID id;

        if (!this.getPassengers().isEmpty())
        {
            id = this.getPassengers().get(0).getUniqueID();

            this.persistantRiderUUID = id;
        }
        else
        {
            id = this.persistantRiderUUID;
        }

        return id;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.getPosY() + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.getPosX(), height, this.getPosZ());
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }
}