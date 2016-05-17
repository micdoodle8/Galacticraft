package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityEvolvedZombie extends EntityZombie implements IEntityBreathable
{
    private int conversionTime = 0;

    public EntityEvolvedZombie(World par1World)
    {
        super(par1World);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
        this.setSize(0.6F, 1.95F);
    }

    protected void applyEntityAI()
    {
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[]{EntityPigZombie.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    public IAttribute getReinforcementsAttribute()
    {
        return EntityZombie.reinforcementChance;
    }
    
    @Override
    protected void jump()
    {
        this.motionY = 0.48D / WorldUtil.getGravityFactor(this);
        if (this.motionY < 0.24D) this.motionY = 0.24D;

        if (this.isPotionActive(Potion.jump))
        {
            this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
        ForgeHooks.onLivingJump(this);
    }

    @Override
    protected void dropFewItems(boolean b, int i)
    {
        if (this.rand.nextInt(200) - i >= 5)
        {
            return;
        }

        switch (this.rand.nextInt(10))
        {
            case 0:
            case 1:
            case 9:
            	//Dehydrated carrot
                this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 16), 0.0F);
                break;
            case 2:
            case 3:
                this.dropItem(GCItems.meteoricIronRaw, 1);
                break;
            case 4:
            case 5:
                this.entityDropItem(new ItemStack(GCItems.basicItem, 1, 18), 0.0F);
                break;
            case 6:
            	//Oxygen tank half empty or less
                this.entityDropItem(new ItemStack(GCItems.oxTankMedium, 1, 901 + this.rand.nextInt(900)), 0.0F);
                break;
            case 7:
                this.dropItem(GCItems.oxMask, 1);
                break;
            case 8:
                this.dropItem(GCItems.oxygenVent, 1);
                break;
        }
    }
}
