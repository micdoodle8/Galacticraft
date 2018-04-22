package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityMeteorChunk extends Entity implements IProjectile
{
    private static final DataParameter<Boolean> IS_HOT = EntityDataManager.createKey(EntityMeteorChunk.class, DataSerializers.BOOLEAN);
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;

    public int canBePickedUp;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private boolean inGround;
    private float randYawInc;
    private float randPitchInc;

    private int knockbackStrength;

    public boolean isHot;

    public EntityMeteorChunk(World world)
    {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.randPitchInc = rand.nextFloat() * 20 - 10;
        this.randYawInc = rand.nextFloat() * 20 - 10;
    }

    public EntityMeteorChunk(World par1World, EntityLivingBase par2EntityLivingBase, float speed)
    {
        super(par1World);
        this.randPitchInc = rand.nextFloat() * 20 - 10;
        this.randYawInc = rand.nextFloat() * 20 - 10;
        this.shootingEntity = par2EntityLivingBase;

        if (par2EntityLivingBase instanceof EntityPlayer)
        {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY + par2EntityLivingBase.getEyeHeight(), par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F;
        this.posY -= 0.10000000149011612D;
        this.posZ -= MathHelper.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        this.motionZ = MathHelper.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES) * MathHelper.cos(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        this.motionY = -MathHelper.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES);
        this.shoot(this.motionX, this.motionY, this.motionZ, speed * 1.5F, 1.0F);
    }

    @Override
    public void shoot(double headingX, double headingY, double headingZ, float speed, float randMod)
    {
        float f2 = MathHelper.sqrt(headingX * headingX + headingY * headingY + headingZ * headingZ);
        headingX /= f2;
        headingY /= f2;
        headingZ /= f2;
        headingX += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingY += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingZ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * randMod;
        headingX *= speed;
        headingY *= speed;
        headingZ *= speed;
        this.motionX = headingX;
        this.motionY = headingY;
        this.motionZ = headingZ;
        float f3 = MathHelper.sqrt(headingX * headingX + headingZ * headingZ);
        this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(headingX, headingZ) * Constants.RADIANS_TO_DEGREES;
        this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(headingY, f3) * Constants.RADIANS_TO_DEGREES;
        this.ticksInGround = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(par1, par5) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(par3, f) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.ticksExisted > 400)
        {
            if (this.isHot)
            {
                this.isHot = false;
                this.setHot(this.isHot);
            }
        }
        else if (!this.world.isRemote)
        {
            this.setHot(this.isHot);
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) Math.atan2(this.motionX, this.motionZ) * Constants.RADIANS_TO_DEGREES;
            this.prevRotationPitch = this.rotationPitch = (float) Math.atan2(this.motionY, f) * Constants.RADIANS_TO_DEGREES;
        }

        BlockPos pos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState stateIn = this.world.getBlockState(pos);

        if (!stateIn.getBlock().isAir(this.world.getBlockState(pos), this.world, pos))
        {
            AxisAlignedBB axisalignedbb = stateIn.getCollisionBoundingBox(this.world, pos);

            if (axisalignedbb != null && axisalignedbb.contains(new Vec3d(this.posX, this.posY, this.posZ)))
            {
                this.inGround = true;
            }
        }

        if (this.inGround)
        {
            Block j = this.world.getBlockState(pos).getBlock();
            int k = j.getMetaFromState(this.world.getBlockState(pos));

            if (j == this.inTile && k == this.inData)
            {
                ++this.ticksInGround;

                if (this.ticksInGround == 1200)
                {
                    this.setDead();
                }
            }
            else
            {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else
        {
            ++this.ticksInAir;
            Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3, vec31, false, true, false);
            vec3 = new Vec3d(this.posX, this.posY, this.posZ);
            vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec31 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
            }

            this.rotationPitch += 1F;

            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;
            final double border = 0.3D;

            for (l = 0; l < list.size(); ++l)
            {
                Entity entity1 = list.get(l);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().grow(border, border, border);
                    RayTraceResult movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float f2;
            float f3;
            double damage = ConfigManagerCore.hardMode ? 3.2D : 1.6D;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit != null)
                {
                    f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int i1 = MathHelper.ceil(f2 * damage);

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null)
                    {
                        damagesource = new EntityDamageSourceIndirect("meteor_chunk", this, this).setProjectile();
                    }
                    else
                    {
                        damagesource = new EntityDamageSourceIndirect("meteor_chunk", this, this.shootingEntity).setProjectile();
                    }

                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman))
                    {
                        movingobjectposition.entityHit.setFire(2);
                    }

                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, i1))
                    {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase)
                        {
                            EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

                            if (!this.world.isRemote)
                            {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0)
                            {
                                f3 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (f3 > 0.0F)
                                {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f3, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f3);
                                }
                            }

                            if (this.shootingEntity != null)
                            {
                                EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.shootingEntity, entitylivingbase);
                            }

                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                            {
                                ((EntityPlayerMP) this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                            }
                        }

                        if (!(movingobjectposition.entityHit instanceof EntityEnderman))
                        {
                            this.setDead();
                        }
                    }
                    else
                    {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                }
                else
                {
                    this.xTile = movingobjectposition.getBlockPos().getX();
                    this.yTile = movingobjectposition.getBlockPos().getY();
                    this.zTile = movingobjectposition.getBlockPos().getZ();
                    IBlockState state = this.world.getBlockState(movingobjectposition.getBlockPos());
                    this.inTile = state.getBlock();
                    this.inData = this.inTile.getMetaFromState(state);
                    this.motionX = (float) (movingobjectposition.hitVec.x - this.posX);
                    this.motionY = (float) (movingobjectposition.hitVec.y - this.posY);
                    this.motionZ = (float) (movingobjectposition.hitVec.z - this.posZ);
                    f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / f2 * 0.05000000074505806D;
                    this.inGround = true;

                    IBlockState hitState = this.world.getBlockState(movingobjectposition.getBlockPos());
                    if (!this.inTile.isAir(hitState, this.world, movingobjectposition.getBlockPos()))
                    {
                        this.inTile.onEntityCollidedWithBlock(this.world, movingobjectposition.getBlockPos(), hitState, this);
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (!this.onGround)
            {
                this.rotationPitch += this.randPitchInc;
                this.rotationYaw += this.randYawInc;
            }

            float f4 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ);
                }
                this.isHot = false;
                f4 = 0.8F;
            }

            this.motionX *= f4;
            this.motionY *= f4;
            this.motionZ *= f4;
            this.motionY -= TransformerHooks.getGravityForEntity(this);
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(IS_HOT, false);
    }

    public boolean isHot()
    {
        return this.dataManager.get(IS_HOT);
    }

    public void setHot(boolean isHot)
    {
        this.dataManager.set(IS_HOT, isHot);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.world.isRemote && this.inGround)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(GCItems.meteorChunk, 1, 0)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    @Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10.0;
        return distance < d0 * d0;
    }
}
