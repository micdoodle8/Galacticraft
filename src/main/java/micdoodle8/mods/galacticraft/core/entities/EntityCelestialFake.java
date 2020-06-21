package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class EntityCelestialFake extends EntityAdvancedMotion implements IIgnoreShift
{
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private Boolean shouldMoveClient;
    private Boolean shouldMoveServer;
    private boolean hasReceivedPacket;
    private ArrayList prevData;
    private boolean networkDataChanged;

    public EntityCelestialFake(EntityType<EntityCelestialFake> type, World world)
    {
        super(type, world);
    }

    public EntityCelestialFake(World var1, double var2, double var4, double var6)
    {
        this(GCEntities.CELESTIAL_FAKE.get(), var1);
        this.setPosition(var2, var4, var6);
    }

    public EntityCelestialFake(ServerPlayerEntity player)
    {
        this(player.world, player.posX, player.posY, player.posZ);

        this.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

        player.startRiding(this, true);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
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
    public void tick()
    {
        if (this.getPassengers().isEmpty())
        {
            this.remove();
            return;
        }
        super.tick();

        if (this.ticks < 40 && this.posY > 150)
        {
            if (this.getPassengers().isEmpty())
            {
                final PlayerEntity player = this.world.getClosestPlayer(this, 5);

                if (player != null && player.getRidingEntity() == null)
                {
                    player.startRiding(this, true);
                }
            }
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
    protected void readAdditional(CompoundNBT nbt)
    {
        if (nbt.contains("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        final ListNBT nbttaglist = new ListNBT();

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
        return false;
    }

    @Override
    public void tickInAir()
    {
        if (this.world.isRemote)
        {
            this.setMotion(0.0, 0.0, 0.0);

            this.lastShouldMove = false;
        }
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();

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
        return this.networkDataChanged;
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
        return 500.0D;
    }

    @Override
    public void readNetworkedData(ByteBuf buffer)
    {
        try
        {
            if (this.world.isRemote)
            {
                this.hasReceivedPacket = true;
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
                            e.startRiding(this, true);
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
                            e.startRiding(this, true);
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
        return null;
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

    @Override
    public boolean pressKey(int key)
    {
        return false;
    }

    @Override
    public int getSizeInventory()
    {
        return 0;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public void spawnParticles()
    {
    }

    @Override
    public void tickOnGround()
    {
        this.tickInAir();
    }

    @Override
    public void onGroundHit()
    {

    }

    @Override
    public Vector3 getMotionVec()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return true;
    }
}