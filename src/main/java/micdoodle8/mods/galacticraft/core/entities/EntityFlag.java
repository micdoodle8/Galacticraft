package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.FlagData;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFlag extends Entity
{
    private static final DataParameter<String> OWNER = EntityDataManager.createKey(EntityFlag.class, DataSerializers.STRING);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> FACING_ANGLE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.VARINT);
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public boolean indestructable = false;
    public FlagData flagData;

    public EntityFlag(EntityType<EntityFlag> type, World world)
    {
        super(type, world);
        this.ignoreFrustumCheck = true;
    }

    public EntityFlag(World world, double x, double y, double z, int dir)
    {
        this(GCEntities.FLAG.get(), world);
        this.setFacingAngle(dir);
        this.setPosition(x, y, z);
        this.xPosition = x;
        this.yPosition = y;
        this.zPosition = z;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        Entity e = par1DamageSource.getTrueSource();
        boolean flag = e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode;

        if (!this.world.isRemote && this.isAlive() && !this.indestructable)
        {
            if (this.isInvulnerableTo(par1DamageSource))
            {
                return false;
            }
            else
            {
                this.markVelocityChanged();
                this.setDamage(this.getDamage() + par2 * 10);
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundType.METAL.getBreakSound(), SoundCategory.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 1.0F);

                if (e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode)
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
                        this.remove();
                    }
                    else
                    {
                        this.remove();
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
        return new ItemStack(GCItems.flag, 1);
    }

    public int getFlagWidth()
    {
        return 25;
    }

    public int getFlagHeight()
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
    protected void registerData()
    {
        this.dataManager.register(OWNER, "");
        this.dataManager.register(DAMAGE, 0.0F);
        this.dataManager.register(FACING_ANGLE, -1);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        this.setOwner(nbt.getString("Owner"));
        this.indestructable = nbt.getBoolean("Indestructable");

        this.xPosition = nbt.getDouble("TileX");
        this.yPosition = nbt.getDouble("TileY");
        this.zPosition = nbt.getDouble("TileZ");
        this.setFacingAngle(nbt.getInt("AngleI"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt)
    {
        nbt.putString("Owner", String.valueOf(this.getOwner()));
        nbt.putBoolean("Indestructable", this.indestructable);
        nbt.putInt("AngleI", this.getFacingAngle());
        nbt.putDouble("TileX", this.xPosition);
        nbt.putDouble("TileY", this.yPosition);
        nbt.putDouble("TileZ", this.zPosition);
    }

    public void dropItemStack()
    {
        this.entityDropItem(new ItemStack(GCItems.flag, 1), 0.0F);
    }

    @Override
    public void tick()
    {
        super.tick();

        if ((this.ticksExisted - 1) % 20 == 0 && this.world.isRemote)
        {
            this.flagData = ClientUtil.updateFlagData(this.getOwner(), Minecraft.getInstance().player.getDistance(this) < 50.0D);
        }

        Vector3 vec = new Vector3((float) this.posX, (float) this.posY, (float) this.posZ);
        vec = vec.translate(new Vector3(0, -1, 0));
        final Block blockAt = vec.getBlock(this.world);

        if (blockAt != null)
        {
            BlockPos pos = new BlockPos(vec.intX(), vec.intY(), vec.intZ());
            if (blockAt instanceof FenceBlock)
            {

            }
            else if (blockAt.isAir(this.world.getBlockState(pos), this.world, pos))
            {
                this.setMotion(getMotion().add(0.0, -0.02F, 0.0F));
            }
        }

        this.move(MoverType.SELF, this.getMotion());
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand)
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

    public void setFacingAngle(int par1)
    {
        this.dataManager.set(FACING_ANGLE, Integer.valueOf(par1));
    }

    public int getFacingAngle()
    {
        return this.dataManager.get(FACING_ANGLE);
    }
}
