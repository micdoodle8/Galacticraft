package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityEvolvedSkeleton extends EntitySkeleton implements IEntityBreathable
{
	private static final ItemStack defaultHeldItem = new ItemStack(Items.bow, 1);

	public EntityEvolvedSkeleton(World par1World)
	{
		super(par1World);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(3, new EntityAIFleeSun(this, 0.25F));
		this.tasks.addTask(4, new EntityAIArrowAttack(this, 0.25F, 30, 20));
		this.tasks.addTask(5, new EntityAIWander(this, 0.25F));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35F);
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}
}
