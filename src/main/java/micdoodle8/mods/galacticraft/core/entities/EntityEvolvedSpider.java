package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

/**
 * EntitySpider.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class EntityEvolvedSpider extends EntitySpider implements IEntityBreathable
{
	public EntityEvolvedSpider(World par1World)
	{
		super(par1World);
		this.setSize(1.4F, 0.9F);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0F);
	}

	@Override
	public boolean canBreath()
	{
		return true;
	}
	
	@Override
    protected boolean isAIEnabled()
    {
        return true;
    }
}
