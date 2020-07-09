package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.entities.EntityAIArrowAttack;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EntityCreeperBoss extends EntityBossBase implements IEntityBreathable, IRangedAttackMob
{
    protected long ticks = 0;
    public int headsRemaining = 3;
    private Entity targetEntity;

    public EntityCreeperBoss(EntityType<? extends EntityCreeperBoss> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(2.0F, 7.0F);
//        this.isImmuneToFire = true;
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new EntityAIArrowAttack(this, 1.0D, 25, 20.0F));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, null));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
        if (damageSource.getDamageType().equals("fireball"))
        {
            if (this.isInvulnerableTo(damageSource))
            {
                return false;
            }
            else if (super.attackEntityFrom(damageSource, damage))
            {
                Entity entity = damageSource.getTrueSource();

                if (this.getPassengers().contains(entity) && this.getRidingEntity() != entity)
                {
                    if (entity != this && entity instanceof LivingEntity)
                    {
                        this.setAttackTarget((LivingEntity) entity);
                    }

                    return true;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    @Override
    protected void registerAttributes()
    {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.05F);
    }

    @Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        this.playSound(GCSounds.bossOuch, this.getSoundVolume(), this.getSoundPitch() - 0.15F);
        return null;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return null;
    }

    //    @Override
//    protected String getHurtSound()
//    {
//        this.playSound(Constants.TEXTURE_PREFIX + "entity.ouch", this.getSoundVolume(), this.getSoundPitch() - 0.15F);
//        return null;
//    }
//
//    @Override
//    protected String getDeathSound()
//    {
//        return null;
//    }

    @Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();

        if (!this.world.isRemote)
        {
            if (this.deathTicks == 1)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionID(this.world), new Object[]{getSoundPitch() - 0.1F}), new PacketDistributor.TargetPoint(this.posX, this.posY, this.posZ, 40.0D, GCCoreUtil.getDimensionID(this.world)));
            }
        }
    }

    @Override
    public void livingTick()
    {
        this.ticks++;

        if (this.getHealth() <= 0)
        {
            this.headsRemaining = 0;
        }
        else if (this.getHealth() <= this.getMaxHealth() / 3.0)
        {
            this.headsRemaining = 1;
        }
        else if (this.getHealth() <= 2 * (this.getMaxHealth() / 3.0))
        {
            this.headsRemaining = 2;
        }

        final PlayerEntity player = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0, false);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.getDistanceSq(player) < 400.0D)
            {
                this.getNavigator().getPathToEntityLiving(player, 0);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        super.livingTick();
    }

//    @Override
//    protected Item getDropItem()
//    {
//        return Items.ARROW;
//    }
//
//    @Override
//    public ItemEntity entityDropItem(ItemStack par1ItemStack, float par2)
//    {
//        final ItemEntity entityitem = new ItemEntity(this.world, this.posX, this.posY + par2, this.posZ, par1ItemStack);
//        entityitem.motionY = -2.0D;
//        entityitem.setDefaultPickupDelay();
//        if (this.captureDrops)
//        {
//            this.capturedDrops.add(entityitem);
//        }
//        else
//        {
//            this.world.addEntity(entityitem);
//        }
//        return entityitem;
//    }
//
//    @Override
//    protected void dropFewItems(boolean b, int i)
//    {
//        if (this.rand.nextInt(200) - i >= 5)
//        {
//            return;
//        }
//
//        if (i > 0)
//        {
//            final ItemStack var2 = new ItemStack(Items.BOW);
//            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5, false);
//            this.entityDropItem(var2, 0.0F);
//        }
//        else
//        {
//            this.dropItem(Items.BOW, 1);
//        }
//    } TODO Item drops

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public ItemStack getGuaranteedLoot(Random rand)
    {
        List<ItemStack> stackList = new LinkedList<>();
        stackList.addAll(GalacticraftRegistry.getDungeonLoot(2));
        boolean hasT3Rocket = false;
        boolean hasAstroMiner = false;
        // Check if player seems to have Tier 3 rocket or Astro Miner already - in that case we don't want more
        // (we don't really want him giving powerful schematics to his friends who are still on Overworld) 
        final PlayerEntity player = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0, false);
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            if (stats != null)
            {
                for (ISchematicPage page : stats.getUnlockedSchematics())
                {
                    if (page.getPageID() == ConfigManagerPlanets.idSchematicRocketT3)
                    {
                        hasT3Rocket = true;
                    }
                    else if (page.getPageID() == ConfigManagerPlanets.idSchematicRocketT3 + 1)
                    {
                        hasAstroMiner = true;
                    }
                }
            }
        }
        // The following code assumes the list start is hard coded to: Cargo Rocket, T3 Rocket, Astro Miner in that order
        // (see MarsModule.init())
        //
        // Remove schematics which he already has
        if (hasT3Rocket && hasAstroMiner)
        {
            // (but do not remove both, otherwise the list is too short)
            if (stackList.size() == 3)
            {
                stackList.remove(1 + rand.nextInt(2));
            }
            else
            {
                stackList.remove(2);
                stackList.remove(1);
            }
        }
        else if (hasT3Rocket)
        {
            stackList.remove(1);
        }
        else if (hasAstroMiner)
        {
            stackList.remove(2);
        }
        // If he does not yet have the T3 rocket, limit the list size to 2 so 50% chance of getting it
        // otherwise return the full list (note: addons could have added more schematics to the list)
        int range = (!hasT3Rocket) ? 2 : stackList.size();
        return stackList.get(rand.nextInt(range)).copy();
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity entitylivingbase, float f)
    {
        this.world.playEvent(null, 1024, new BlockPos(this), 0);
        double d3 = this.posX;
        double d4 = this.posY + 5.5D;
        double d5 = this.posZ;
        double d6 = entitylivingbase.posX - d3;
        double d7 = entitylivingbase.posY + entitylivingbase.getEyeHeight() * 0.5D - d4;
        double d8 = entitylivingbase.posZ - d5;
        EntityProjectileTNT projectileTNT = EntityProjectileTNT.createEntityProjectileTNT(this.world, this, d6 * 0.5D, d7 * 0.5D, d8 * 0.5D);

        projectileTNT.posY = d4;
        projectileTNT.posX = d3;
        projectileTNT.posZ = d5;
        this.world.addEntity(projectileTNT);
    }

    @Override
    public int getChestTier()
    {
        return 2;
    }

    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(MarsItems.key, 1), 0.5F);
    }

    @Override
    public BossInfo.Color getHealthBarColor()
    {
        return BossInfo.Color.YELLOW;
    }

//    @Override
//    public void setSwingingArms(boolean swingingArms) {}

    @Override
    public void onKillCommand()
    {
        this.setHealth(0.0F);
    }
}
