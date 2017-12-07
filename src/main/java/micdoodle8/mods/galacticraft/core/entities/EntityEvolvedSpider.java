package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
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
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0D);
        double difficulty = 0;
        switch (this.worldObj.getDifficulty())
        {
        case HARD : difficulty = 2D;
        break;
        case NORMAL : difficulty = 1D;
        break;
        default:
            break;
        }
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3 + 0.05 * difficulty);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D + difficulty);
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

        if (this.worldObj.rand.nextInt(100) == 0)
        {
            EntityEvolvedSkeleton entityskeleton = new EntityEvolvedSkeleton(this.worldObj);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
            this.worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }

        if (livingdata == null)
        {
            livingdata = new EntitySpider.GroupData();

            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
            {
                ((EntitySpider.GroupData)livingdata).func_111104_a(this.worldObj.rand);
            }
        }

        if (livingdata instanceof EntitySpider.GroupData)
        {
            int i = ((EntitySpider.GroupData)livingdata).potionEffectId;

            if (i > 0 && Potion.potionTypes[i] != null)
            {
                this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
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

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
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

    @Override
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
            this.dropItem(Items.fermented_spider_eye, 1);
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
                this.dropItem(Items.nether_wart, 1);
            }
            break;
        }
    }
}
