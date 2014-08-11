package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.util.DamageSource;

public class DamageSourceGC extends DamageSource
{
	public static final DamageSourceGC spaceshipCrash = (DamageSourceGC) new DamageSourceGC("spaceshipCrash").setDamageBypassesArmor();
	public static final DamageSourceGC oxygenSuffocation = (DamageSourceGC) new DamageSourceGC("oxygenSuffocation").setDamageBypassesArmor();
	public static final DamageSourceGC thermal = (DamageSourceGC) new DamageSourceGC("thermal").setDamageBypassesArmor();

	public DamageSourceGC(String damageType)
	{
		super(damageType);
	}
}
