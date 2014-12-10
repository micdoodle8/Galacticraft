package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import cpw.mods.fml.client.FMLClientHandler;

public class EntityCelestialFake extends EntityAdvancedMotion implements IIgnoreShift, IPacketReceiver
{
    private boolean lastShouldMove;
    private UUID persistantRiderUUID;
    private Boolean shouldMoveClient;
    private Boolean shouldMoveServer;
	private boolean hasReceivedPacket;

	public EntityCelestialFake(World var1)
	{
		this(var1, 0F);
	}
	
	public EntityCelestialFake(World var1, float yOffset)
    {
        super(var1, yOffset);
        this.setSize(3.0F, 1.0F);
    }

    @Override
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
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

    public EntityCelestialFake(World var1, double var2, double var4, double var6, float yOffset)
    {
        this(var1, yOffset);
        this.setPosition(var2, var4 + this.yOffset, var6);
    }

    public EntityCelestialFake(EntityPlayerMP player, float yOffset)
    {
        this(player.worldObj, player.posX, player.posY, player.posZ, yOffset);

        this.setPositionAndRotation(player.posX, player.posY, player.posZ, 0, 0);

        this.riddenByEntity = player;
        player.ridingEntity = this;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticks < 40 && this.posY > 150)
        {
            if (this.riddenByEntity == null)
            {
                final EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 5);

                if (player != null && player.ridingEntity == null)
                {
                    player.mountEntity(this);
                }
            }
        }

        AxisAlignedBB box = this.boundingBox.expand(0.2D, 0.4D, 0.2D);

        final List<Entity> var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (Entity entity : var15)
            {
                if (entity != this.riddenByEntity)
                {
                    this.pushEntityAway(entity);
                }
            }
        }
    }

    private void pushEntityAway(Entity entityToPush)
    {
        if (this.riddenByEntity != entityToPush && this.ridingEntity != entityToPush)
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
        if (nbt.hasKey("RiderUUID_LSB"))
        {
            this.persistantRiderUUID = new UUID(nbt.getLong("RiderUUID_LSB"), nbt.getLong("RiderUUID_MSB"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        final NBTTagList nbttaglist = new NBTTagList();

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
        return false;
    }

    @Override
    public void tickInAir()
    {
        if (this.worldObj.isRemote)
        {
            this.motionY = this.motionX = this.motionZ = 0.0F;

            this.lastShouldMove = false;
        }
    }

    @Override
    public ArrayList<Object> getNetworkedData()
    {
        final ArrayList<Object> objList = new ArrayList<Object>();

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
            objList.add(this.riddenByEntity == null ? -1 : this.riddenByEntity.getEntityId());
        }

        return objList;
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
            if (this.worldObj.isRemote)
            {
                this.hasReceivedPacket = true;
                this.shouldMoveServer = buffer.readBoolean();

                //Check has correct rider on client
                int shouldBeMountedId = buffer.readInt();
                if (this.riddenByEntity == null)
                {
                	 if (shouldBeMountedId > -1)
                	 {
                		 Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
                		 if (e != null) e.mountEntity(this);
                	 }
                }
                else if (this.riddenByEntity.getEntityId() != shouldBeMountedId)
                {
                	if (shouldBeMountedId == -1)
                	{
                		this.riddenByEntity.mountEntity(null);
                	}
                	else
                	{
                		Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
               		 	if (e != null) e.mountEntity(this);
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
        if (this.riddenByEntity != null && !(this.riddenByEntity instanceof EntityPlayer))
        {
            return null;
        }

        UUID id;

        if (riddenByEntity != null)
        {
            id = ((EntityPlayer) this.riddenByEntity).getPersistentID();

            if (id != null)
            {
                this.persistantRiderUUID = id;
            }
        }
        else
        {
            id = this.persistantRiderUUID;
        }

        return id;
    }

	@Override
	public boolean pressKey(int key) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean shouldSpawnParticles() {
		return false;
	}

	@Override
	public Map<Vector3, Vector3> getParticleMap() {
		return null;
	}

	@Override
	public EntityFX getParticle(Random rand, double x, double y, double z,
			double motX, double motY, double motZ) {
		return null;
	}

	@Override
	public void tickOnGround() {
		this.tickInAir();
	}

	@Override
	public void onGroundHit() {
		
	}

	@Override
	public Vector3 getMotionVec() {
		return new Vector3(0, 0, 0);
	}

	@Override
	public boolean shouldIgnoreShiftExit() {
		return true;
	}
}