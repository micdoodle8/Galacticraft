package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedSpider extends EntitySpider implements IEntityBreathable
{
    public EntityEvolvedSpider(World par1World)
    {
        super(par1World);
        this.setSize(1.5F, 1.0F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0D);
        double difficulty = 0;
        switch (this.world.getDifficulty())
        {
        case HARD : difficulty = 2D;
        break;
        case NORMAL : difficulty = 1D;
        break;
        default:
            break;
        }
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3 + 0.05 * difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D + difficulty);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        // onInitialSpawn is called for EntitySpider, which has a chance of adding a vanilla skeleton, remove these
        for (Entity entity : getPassengers())
        {
            entity.dismountRidingEntity();
            if (!(entity instanceof EntitySkeleton))
            {
                GCLog.severe("Removed unexpected passenger from spider: " + entity);
            }
            else
            {
                entity.setDead();
            }
        }

        if (this.world.rand.nextInt(100) == 0)
        {
            EntityEvolvedSkeleton entityskeleton = new EntityEvolvedSkeleton(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (livingdata == null)
        {
            livingdata = new EntitySpider.GroupData();

            if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
            {
                ((EntitySpider.GroupData)livingdata).setRandomEffect(this.world.rand);
            }
        }

        if (livingdata instanceof EntitySpider.GroupData)
        {
            Potion potion = ((EntitySpider.GroupData)livingdata).effect;

            if (potion != null)
            {
                this.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
            }
        }

        return livingdata;
    }

    @Override
    protected void jump()
    {
        this.motionY = 0.52D / WorldUtil.getGravityFactor(this);

        if (this.motionY < 0.26D)
        {
            this.motionY = 0.26D;
        }

        if (this.isPotionActive(MobEffects.JUMP_BOOST))
        {
            this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw / Constants.RADIANS_TO_DEGREES;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    protected void addRandomDrop()
    {
        switch (this.rand.nextInt(14))
        {
        case 0:
        case 1:
        case 2:
            this.dropItem(GCItems.cheeseCurd, 1);
            break;
        case 3:
        case 4:
        case 5:
            this.dropItem(Items.FERMENTED_SPIDER_EYE, 1);
            break;
        case 6:
        case 7:
            //Oxygen tank half empty or less
            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
            break;
        case 8:
            this.dropItem(GCItems.oxygenGear, 1);
            break;
        case 9:
            this.dropItem(GCItems.oxygenConcentrator, 1);
            break;
        default:
            if (ConfigManagerCore.challengeMobDropsAndSpawning)
            {
                this.dropItem(Items.NETHER_WART, 1);
            }
            break;
        }
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        super.dropLoot(wasRecentlyHit, lootingModifier > 1 ? lootingModifier - 1 : 0, source);
        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
        {
            this.addRandomDrop();
        }
    }
}
