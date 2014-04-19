package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.util.DamageSource;

/**
 * GCCoreDamageSource.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class DamageSourceGC extends DamageSource
{
	public static final DamageSourceGC spaceshipCrash = (DamageSourceGC) new DamageSourceGC("spaceshipCrash").setDamageBypassesArmor();
	public static final DamageSourceGC oxygenSuffocation = (DamageSourceGC) new DamageSourceGC("oxygenSuffocation").setDamageBypassesArmor();

	public DamageSourceGC(String damageType)
	{
		super(damageType);
	}

	@Override
	public DamageSource setDamageBypassesArmor()
	{
		return super.setDamageBypassesArmor();
	}

	@Override
	public DamageSource setDamageAllowedInCreativeMode()
	{
		return super.setDamageAllowedInCreativeMode();
	}

	@Override
	public DamageSource setFireDamage()
	{
		return super.setFireDamage();
	}
}
