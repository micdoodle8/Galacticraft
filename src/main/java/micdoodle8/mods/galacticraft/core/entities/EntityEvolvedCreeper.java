package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityEvolvedCreeper extends CreeperEntity implements IEntityBreathable
{
    //    private float sizeXBase = -1.0F;
//    private float sizeYBase;
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityEvolvedCreeper.class, DataSerializers.BOOLEAN);
    private static final UUID babySpeedBoostUUID = UUID.fromString("ef67a435-32a4-4efd-b218-e7431438b109");
    private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost evolved creeper", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);

    public EntityEvolvedCreeper(EntityType<? extends EntityEvolvedCreeper> type, World worldIn)
    {
        super(type, worldIn);
        this.goalSelector.goals.clear();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new CreeperSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, OcelotEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(IS_CHILD, false);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);

        if (this.isChild())
        {
            nbt.putBoolean("IsBaby", true);
        }
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);

        if (nbt.getBoolean("IsBaby"))
        {
            this.setChild(true);
        }
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

//    public void setChildSize(boolean isChild)
//    {
//        this.setCreeperScale(isChild ? 0.5F : 1.0F);
//    }

//    @Override
//    protected final void setSize(float sizeX, float sizeY)
//    {
//        boolean flag = this.sizeXBase > 0.0F && this.sizeYBase > 0.0F;
//        this.sizeXBase = sizeX;
//        this.sizeYBase = sizeY;
//
//        if (!flag)
//        {
//            this.setCreeperScale(1.0F);
//        }
//    }

//    protected final void setCreeperScale(float scale)
//    {
//        super.setSize(this.sizeXBase * scale, this.sizeYBase * scale);
    //FMLLog.info("" + this.sizeYBase + " " + scale);
//    }

    @Override
    public boolean isChild()
    {
        return this.dataManager.get(IS_CHILD);
    }

    @Override
    protected int getExperiencePoints(PlayerEntity p_70693_1_)
    {
        if (this.isChild())
        {
            this.experienceValue = (this.experienceValue * 5) / 2;
        }

        return super.getExperiencePoints(p_70693_1_);
    }

    public void setChild(boolean isChild)
    {
        this.dataManager.set(IS_CHILD, isChild);

        if (this.world != null && !this.world.isRemote)
        {
            IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(babySpeedBoostModifier);

            if (isChild)
            {
                iattributeinstance.applyModifier(babySpeedBoostModifier);
            }
        }

//        this.setChildSize(isChild);
    }

//    @Override
//    protected void jump()
//    {
//        this.motionY = 0.45D / WorldUtil.getGravityFactor(this);
//        if (this.motionY < 0.22D)
//        {
//            this.motionY = 0.22D;
//        }
//
//        if (this.isPotionActive(Effects.JUMP_BOOST))
//        {
//            this.motionY += (this.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
//        }
//
//        if (this.isSprinting())
//        {
//            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
//            this.motionX -= MathHelper.sin(f) * 0.2F;
//            this.motionZ += MathHelper.cos(f) * 0.2F;
//        }
//
//        this.isAirBorne = true;
//        ForgeHooks.onLivingJump(this);
//    }

//    @Override
//    protected Item getDropItem()
//    {
//        if (this.isBurning())
//        {
//            return Items.BLAZE_ROD;
//        }
//        return Items.REDSTONE;
//    } TODO Loot

//    protected void addRandomDrop()
//    {
//        switch (this.rand.nextInt(12))
//        {
//        case 0:
//        case 1:
//        case 2:
//        case 3:
//            this.entityDropItem(new ItemStack(Blocks.SAND), 0.0F);
//            break;
//        case 4:
//        case 5:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 6:
//            this.dropItem(GCItems.oxygenGear, 1);
//            break;
//        case 7:
//        case 8:
//            this.entityDropItem(new ItemStack(Blocks.ICE), 0.0F);
//            break;
//        default:
//            if (ConfigManagerCore.challengeMobDropsAndSpawning) this.dropItem(Items.REEDS, 1);
//            break;
//        }
//    }
//
//    @Override
//    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
//    {
//        // No loot table
//        this.dropFewItems(wasRecentlyHit, lootingModifier);
//        this.dropEquipment(wasRecentlyHit, lootingModifier);
//    }
//
//    @Override
//    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
//    {
//        super.dropFewItems(wasRecentlyHit, lootingModifier);
//
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
//        {
//            this.addRandomDrop();
//        }
//    }
}
