package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFlag extends Entity
{
    private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityFlag.class, DataSerializers.STRING);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FACING_ANGLE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.VARINT);
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public boolean indestructable = false;
    public FlagData flagData;

    public EntityFlag(World world)
    {
        super(world);
        this.setSize(0.4F, 3F);
        this.ignoreFrustumCheck = true;
    }

    public EntityFlag(World par1World, double x, double y, double z, int dir)
    {
        this(par1World);
        this.setFacingAngle(dir);
        this.setPosition(x, y, z);
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        Entity e = par1DamageSource.getTrueSource();
        boolean flag = e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode;

        if (!this.world.isRemote && !this.isDead && !this.indestructable)
        {
            if (this.isEntityInvulnerable(par1DamageSource))
            {
                return false;
            }
            else
            {
                this.markVelocityChanged();
                this.setDamage(this.getDamage() + par2 * 10);
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundType.METAL.getBreakSound(), SoundCategory.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 1.0F);

                if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)
                {
                    this.setDamage(100.0F);
                }

                if (flag || this.getDamage() > 40)
                {
                    if (!this.getPassengers().isEmpty())
                    {
                        this.removePassengers();
                    }

                    if (flag)
                    {
                        this.setDead();
                    }
                    else
                    {
                        this.setDead();
                        this.dropItemStack();
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(GCItems.flag, 1, this.getType());
    }

    public int getWidth()
    {
        return 25;
    }

    public int getHeight()
    {
        return 40;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.getCollisionBoundingBox();
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(OWNER, "");
        this.dataManager.register(DAMAGE, 0.0F);
        this.dataManager.register(TYPE, -1);
        this.dataManager.register(FACING_ANGLE, -1);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        this.setOwner(par1NBTTagCompound.getString("Owner"));
        this.setType(par1NBTTagCompound.getInteger("Type"));
        this.indestructable = par1NBTTagCompound.getBoolean("Indestructable");

        this.xPosition = par1NBTTagCompound.getDouble("TileX");
        this.yPosition = par1NBTTagCompound.getDouble("TileY");
        this.zPosition = par1NBTTagCompound.getDouble("TileZ");
        this.setFacingAngle(par1NBTTagCompound.getInteger("AngleI"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setString("Owner", String.valueOf(this.getOwner()));
        par1NBTTagCompound.setInteger("Type", Integer.valueOf(this.getType()));
        par1NBTTagCompound.setBoolean("Indestructable", this.indestructable);
        par1NBTTagCompound.setInteger("AngleI", this.getFacingAngle());
        par1NBTTagCompound.setDouble("TileX", this.xPosition);
        par1NBTTagCompound.setDouble("TileY", this.yPosition);
        par1NBTTagCompound.setDouble("TileZ", this.zPosition);
    }

    public void dropItemStack()
    {
        this.entityDropItem(new ItemStack(GCItems.flag, 1, this.getType()), 0.0F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if ((this.ticksExisted - 1) % 20 == 0 && this.world.isRemote)
        {
            this.flagData = ClientUtil.updateFlagData(this.getOwner(), Minecraft.getMinecraft().player.getDistance(this) < 50.0D);
        }

        Vector3 vec = new Vector3(this.posX, this.posY, this.posZ);
        vec = vec.translate(new Vector3(0, -1, 0));
        final Block blockAt = vec.getBlock(this.world);

        if (blockAt != null)
        {
            BlockPos pos = new BlockPos(vec.intX(), vec.intY(), vec.intZ());
            if (blockAt instanceof BlockFence)
            {

            }
            else if (blockAt.isAir(this.world.getBlockState(pos), this.world, pos))
            {
                this.motionY -= 0.02F;
            }
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
    {
        if (!this.world.isRemote)
        {
            this.setFacingAngle(this.getFacingAngle() + 3);
        }

        return true;
    }

    public void setOwner(String par1)
    {
        this.dataManager.set(OWNER, String.valueOf(par1));
    }

    public String getOwner()
    {
        return this.dataManager.get(OWNER);
    }

    public void setDamage(float par1)
    {
        this.dataManager.set(DAMAGE, Float.valueOf(par1));
    }

    public float getDamage()
    {
        return this.dataManager.get(DAMAGE);
    }

    public void setType(int par1)
    {
        this.dataManager.set(TYPE, Integer.valueOf(par1));
    }

    public int getType()
    {
        return this.dataManager.get(TYPE);
    }

    public void setFacingAngle(int par1)
    {
        this.dataManager.set(FACING_ANGLE, Integer.valueOf(par1));
    }

    public int getFacingAngle()
    {
        return this.dataManager.get(FACING_ANGLE);
    }
}
