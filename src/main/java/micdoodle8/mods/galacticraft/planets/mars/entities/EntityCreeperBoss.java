package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityAIArrowAttack;
import micdoodle8.mods.galacticraft.core.entities.EntityBossBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class EntityCreeperBoss extends EntityBossBase implements IEntityBreathable, IRangedAttackMob
{
    protected long ticks = 0;
    public int headsRemaining = 3;
    private Entity targetEntity;

    public EntityCreeperBoss(World par1World)
    {
        super(par1World);
        this.setSize(2.0F, 7.0F);
        this.isImmuneToFire = true;
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 25, 20.0F));
        this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 0, true, false, null));
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage)
    {
        if (damageSource.getDamageType().equals("fireball"))
        {
            if (this.isEntityInvulnerable(damageSource))
            {
                return false;
            }
            else if (super.attackEntityFrom(damageSource, damage))
            {
                Entity entity = damageSource.getEntity();

                if (this.riddenByEntity != entity && this.ridingEntity != entity)
                {
                    if (entity != this && entity instanceof EntityLivingBase)
                    {
                        this.setAttackTarget((EntityLivingBase) entity);
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
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0F * ConfigManagerCore.dungeonBossHealthMod);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.05F);
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
    protected String getLivingSound()
    {
        return null;
    }

    @Override
    protected String getHurtSound()
    {
        this.playSound(Constants.TEXTURE_PREFIX + "entity.ouch", this.getSoundVolume(), this.getSoundPitch() - 0.15F);
        return null;
    }

    @Override
    protected String getDeathSound()
    {
        return null;
    }

    @Override
    protected void onDeathUpdate()
    {
        super.onDeathUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.deathTicks == 1)
            {
                GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_PLAY_SOUND_BOSS_DEATH, GCCoreUtil.getDimensionID(this.worldObj), new Object[] { getSoundPitch() - 0.1F }), new TargetPoint(GCCoreUtil.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 40.0D));
            }
        }
    }

    @Override
    public void onLivingUpdate()
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

        final EntityPlayer player = this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0);

        if (player != null && !player.equals(this.targetEntity))
        {
            if (this.getDistanceSqToEntity(player) < 400.0D)
            {
                this.getNavigator().getPathToEntityLiving(player);
                this.targetEntity = player;
            }
        }
        else
        {
            this.targetEntity = null;
        }

        super.onLivingUpdate();
    }

    @Override
    protected Item getDropItem()
    {
        return Items.arrow;
    }

    @Override
    public EntityItem entityDropItem(ItemStack par1ItemStack, float par2)
    {
        final EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + par2, this.posZ, par1ItemStack);
        entityitem.motionY = -2.0D;
        entityitem.setDefaultPickupDelay();
        if (this.captureDrops)
        {
            this.capturedDrops.add(entityitem);
        }
        else
        {
            this.worldObj.spawnEntityInWorld(entityitem);
        }
        return entityitem;
    }

    @Override
    protected void dropFewItems(boolean b, int i)
    {
        if (this.rand.nextInt(200) - i >= 5)
        {
            return;
        }

        if (i > 0)
        {
            final ItemStack var2 = new ItemStack(Items.bow);
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 5);
            this.entityDropItem(var2, 0.0F);
        }
        else
        {
            this.dropItem(Items.bow, 1);
        }
    }

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
        final EntityPlayer player = this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 20.0);
        if (player != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            if (stats != null)
            {
                for (ISchematicPage page : stats.getUnlockedSchematics())
                {
                    if (page.getPageID() == ConfigManagerAsteroids.idSchematicRocketT3)
                    {
                        hasT3Rocket = true;
                    }
                    else if (page.getPageID() == ConfigManagerAsteroids.idSchematicRocketT3 + 1)
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
    public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f)
    {
        this.worldObj.playAuxSFXAtEntity(null, 1014, new BlockPos((int) this.posX, (int) this.posY, (int) this.posZ), 0);
        double d3 = this.posX;
        double d4 = this.posY + 5.5D;
        double d5 = this.posZ;
        double d6 = entitylivingbase.posX - d3;
        double d7 = entitylivingbase.posY + entitylivingbase.getEyeHeight() * 0.5D - d4;
        double d8 = entitylivingbase.posZ - d5;
        EntityProjectileTNT projectileTNT = new EntityProjectileTNT(this.worldObj, this, d6 * 0.5D, d7 * 0.5D, d8 * 0.5D);

        projectileTNT.posY = d4;
        projectileTNT.posX = d3;
        projectileTNT.posZ = d5;
        this.worldObj.spawnEntityInWorld(projectileTNT);
    }

    @Override
    public int getChestTier()
    {
        return 2;
    }

    @Override
    public void dropKey()
    {
        this.entityDropItem(new ItemStack(MarsItems.key, 1, 0), 0.5F);
    }
}
