package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.util.DamageSource;

public class DamageSourceGC extends DamageSource
{
    public static final DamageSourceGC spaceshipCrash = (DamageSourceGC) new DamageSourceGC("spaceshipCrash").setDamageBypassesArmor().setExplosion();
    public static final DamageSourceGC oxygenSuffocation = (DamageSourceGC) new DamageSourceGC("oxygenSuffocation").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSourceGC thermal = (DamageSourceGC) new DamageSourceGC("thermal").setDamageBypassesArmor().setDamageIsAbsolute();

    public DamageSourceGC(String damageType)
    {
        super(damageType);
    }
}
