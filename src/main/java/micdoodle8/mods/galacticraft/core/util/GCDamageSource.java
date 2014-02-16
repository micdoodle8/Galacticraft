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
public class GCDamageSource extends DamageSource
{
	public static final GCDamageSource spaceshipCrash = (GCDamageSource) new GCDamageSource("spaceshipCrash").setDamageBypassesArmor();
	public static final GCDamageSource oxygenSuffocation = (GCDamageSource) new GCDamageSource("oxygenSuffocation").setDamageBypassesArmor();

	public GCDamageSource(String damageType)
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
