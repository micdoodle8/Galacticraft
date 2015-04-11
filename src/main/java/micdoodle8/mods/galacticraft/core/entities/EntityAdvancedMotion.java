package micdoodle8.mods.galacticraft.core.entities;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.network.PacketEntityUpdate.IEntityFullSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public abstract class EntityAdvancedMotion extends InventoryEntity implements IPacketReceiver, IControllableEntity, IEntityFullSync
{
    protected long ticks = 0;

    public float currentDamage;
    public int timeSinceHit;
    public int rockDirection;

    public double advancedPositionX;
    public double advancedPositionY;
    public double advancedPositionZ;
    public double advancedYaw;
    public double advancedPitch;
    public int posRotIncrements;

    protected boolean lastOnGround;

    public EntityAdvancedMotion(World world, float yOffset)
    {
        super(world);
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
        this.yOffset = yOffset;
    }

    public EntityAdvancedMotion(World world, float yOffset, double var2, double var4, double var6)
    {
        this(world, yOffset);
        this.yOffset = yOffset;
        this.setPosition(var2, var4 + this.yOffset, var6);
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
        return this.height - 1.0D;
    }

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
            final double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            final double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    @Override
    public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
    {
        if (this.worldObj.isRemote)
        {
            this.advancedPositionX = x;
            this.advancedPositionY = y;
            this.advancedPositionZ = z;
            this.advancedYaw = yaw;
            this.advancedPitch = pitch;
            this.motionX = motX;
            this.motionY = motY;
            this.motionZ = motZ;
            this.posRotIncrements = 5;
        }
        else
        {
            this.setPosition(x, y, z);
            this.setRotation(yaw, pitch);
            this.motionX = motX;
            this.motionY = motY;
            this.motionZ = motZ;
            if (onGround || this.forceGroundUpdate())
            {
                this.onGround = onGround;
            }
        }
    }

    protected boolean forceGroundUpdate()
    {
        return true;
    }

    @Override
    public void performHurtAnimation()
    {
        this.rockDirection = -this.rockDirection;
        this.timeSinceHit = 10;
        this.currentDamage *= 5;
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2)
    {
        if (this.isDead || var1.equals(DamageSource.cactus) || !this.allowDamageSource(var1))
        {
            return true;
        }
        else
        {
        	Entity e = var1.getEntity(); 
            if (this.isEntityInvulnerable() || this.posY > 300 || (e instanceof EntityLivingBase && !(e instanceof EntityPlayer)))
            {
                return false;
            }
            else
            {
	        	this.rockDirection = -this.rockDirection;
	            this.timeSinceHit = 10;
	            this.currentDamage = this.currentDamage + var2 * 10;
	            this.setBeenAttacked();
	
	            if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)
	            {
	                this.currentDamage = 100;
	            }
	
	            if (this.currentDamage > 70)
	            {
	                if (this.riddenByEntity != null)
	                {
	                    this.riddenByEntity.mountEntity(this);
	
	                    return false;
	                }
	
	                if (!this.worldObj.isRemote)
	                {
	                    this.dropItems();
	
	                    this.setDead();
	                }
	            }
	
	            return true;
            }
        }
    }

    public abstract List<ItemStack> getItemsDropped();

    public abstract boolean shouldMove();

    public abstract boolean shouldSpawnParticles();

    /**
     * @return map of the particle vectors. Map key is the position and map
     * value is the motion of the particles. Each entry will be spawned
     * as a separate particle
     */
    public abstract Map<Vector3, Vector3> getParticleMap();

    @SideOnly(Side.CLIENT)
    public abstract EntityFX getParticle(Random rand, double x, double y, double z, double motX, double motY, double motZ);

    public abstract void tickInAir();

    public abstract void tickOnGround();

    public abstract void onGroundHit();

    public abstract Vector3 getMotionVec();

    public abstract ArrayList<Object> getNetworkedData();

    /**
     * @return ticks between packets being sent to client
     */
    public abstract int getPacketTickSpacing();

    /**
     * @return players within this distance will recieve packets from this
     * entity
     */
    public abstract double getPacketSendDistance();

    public abstract void readNetworkedData(ByteBuf buffer);

    public abstract boolean allowDamageSource(DamageSource damageSource);

    public void dropItems()
    {
        if (this.getItemsDropped() == null)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped())
        {
            if (item != null)
            {
                this.entityDropItem(item, 0);
            }
        }
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
                this.posRotIncrements = i + 5;
                this.advancedPositionX = d;
                this.advancedPositionY = d1 + (this.riddenByEntity == null ? 1 : 0);
                this.advancedPositionZ = d2;
                this.advancedYaw = f;
                this.advancedPitch = f1;
            }
        }
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        if (this.shouldMove())
        {
            super.moveEntity(par1, par3, par5);
        }
    }

    public abstract boolean canSetPositionClient();

    public abstract boolean shouldSendAdvancedMotionPacket();

    @Override
    public void onUpdate()
    {
        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;

        super.onUpdate();

        if (this.canSetPositionClient() && this.worldObj.isRemote && (this.riddenByEntity == null || !(this.riddenByEntity instanceof EntityPlayer) || !FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.posRotIncrements > 0)
            {
                x = this.posX + (this.advancedPositionX - this.posX) / this.posRotIncrements;
                y = this.posY + (this.advancedPositionY - this.posY) / this.posRotIncrements;
                z = this.posZ + (this.advancedPositionZ - this.posZ) / this.posRotIncrements;
                var12 = MathHelper.wrapAngleTo180_double(this.advancedYaw - this.rotationYaw);
                this.rotationYaw = (float) (this.rotationYaw + var12 / this.posRotIncrements);
                this.rotationPitch = (float) (this.rotationPitch + (this.advancedPitch - this.rotationPitch) / this.posRotIncrements);
                --this.posRotIncrements;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                x = this.posX + this.motionX;
                y = this.posY + this.motionY;
                z = this.posZ + this.motionZ;
                this.setPosition(x, y, z);
            }
        }

        if (this.timeSinceHit > 0)
        {
            this.timeSinceHit--;
        }

        if (this.currentDamage > 0)
        {
            this.currentDamage--;
        }

        if (this.shouldSpawnParticles() && this.worldObj.isRemote)
        {
            this.spawnParticles(this.getParticleMap());
        }

        if (this.onGround)
        {
            this.tickOnGround();
        }
        else
        {
            this.tickInAir();
        }

        if (this.worldObj.isRemote)
        {
            Vector3 mot = this.getMotionVec();
            this.motionX = mot.x;
            this.motionY = mot.y;
            this.motionZ = mot.z;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
        }

        if (this.onGround && !this.lastOnGround)
        {
            this.onGroundHit();
        }

        if (shouldSendAdvancedMotionPacket())
        {
            if (this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketEntityUpdate(this));
            }

            if (!this.worldObj.isRemote && this.ticks % 5 == 0)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketEntityUpdate(this), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
            }
        }

        if (!this.worldObj.isRemote && this.ticks % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
        }

        if (this.worldObj.isRemote && this.ticks % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastOnGround = this.onGround;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        sendData.addAll(this.getNetworkedData());
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.readNetworkedData(buffer);
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticles(Map<Vector3, Vector3> points)
    {
        for (final Entry<Vector3, Vector3> vec : points.entrySet())
        {
            final Vector3 posVec = vec.getKey();
            final Vector3 motionVec = vec.getValue();

            this.spawnParticle(this.getParticle(this.rand, posVec.x, posVec.y, posVec.z, motionVec.x, motionVec.y, motionVec.z));
        }
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(EntityFX fx)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            if (fx != null)
            {
                mc.effectRenderer.addEffect(fx);
            }
        }
    }
}