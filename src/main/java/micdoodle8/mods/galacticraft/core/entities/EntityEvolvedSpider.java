package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class EntityEvolvedSpider extends SpiderEntity implements IEntityBreathable
{
    public EntityEvolvedSpider(EntityType<? extends EntityEvolvedSpider> type, World world)
    {
        super(type, world);
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(22.0D);
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
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3 + 0.05 * difficulty);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2D + difficulty);
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
    {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        // onInitialSpawn is called for EntitySpider, which has a chance of adding a vanilla skeleton, remove these
        for (Entity entity : getPassengers())
        {
            entity.stopRiding();
            if (!(entity instanceof SkeletonEntity))
            {
                GCLog.severe("Removed unexpected passenger from spider: " + entity);
            }
            else
            {
                entity.remove();
            }
        }

        if (this.world.rand.nextInt(100) == 0)
        {
            EntityEvolvedSkeleton entityskeleton = new EntityEvolvedSkeleton(GCEntities.EVOLVED_SKELETON.get(), world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            entityskeleton.onInitialSpawn(worldIn, difficultyIn, reason, (ILivingEntityData)null, (CompoundNBT)null);
            this.world.addEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }

        if (spawnDataIn == null)
        {
            spawnDataIn = new SpiderEntity.GroupData();

            if (this.world.getDifficulty() == Difficulty.HARD && this.world.rand.nextFloat() < 0.1F * difficultyIn.getClampedAdditionalDifficulty())
            {
                ((SpiderEntity.GroupData)spawnDataIn).setRandomEffect(this.world.rand);
            }
        }

        if (spawnDataIn instanceof SpiderEntity.GroupData)
        {
            Effect potion = ((SpiderEntity.GroupData)spawnDataIn).effect;

            if (potion != null)
            {
                this.addPotionEffect(new EffectInstance(potion, Integer.MAX_VALUE));
            }
        }

        return spawnDataIn;
    }

//    protected void addRandomDrop()
//    {
//        switch (this.rand.nextInt(14))
//        {
//        case 0:
//        case 1:
//        case 2:
//            this.dropItem(GCItems.cheeseCurd, 1);
//            break;
//        case 3:
//        case 4:
//        case 5:
//            this.dropItem(Items.FERMENTED_SPIDER_EYE, 1);
//            break;
//        case 6:
//        case 7:
//            //Oxygen tank half empty or less
//            this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
//            break;
//        case 8:
//            this.dropItem(GCItems.oxygenGear, 1);
//            break;
//        case 9:
//            this.dropItem(GCItems.oxygenConcentrator, 1);
//            break;
//        default:
//            if (ConfigManagerCore.challengeMobDropsAndSpawning)
//            {
//                this.dropItem(Items.NETHER_WART, 1);
//            }
//            break;
//        }
//    } TODO Loot

//    @Override
//    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
//    {
//        super.dropLoot(wasRecentlyHit, lootingModifier > 1 ? lootingModifier - 1 : 0, source);
//        if (wasRecentlyHit && this.rand.nextFloat() < 0.025F + (float)lootingModifier * 0.02F)
//        {
//            this.addRandomDrop();
//        }
//    }
}
