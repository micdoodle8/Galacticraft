package micdoodle8.mods.galacticraft.core.util;

import net.minecraft.util.DamageSource;

public class DamageSourceGC extends DamageSource
{
    public static final DamageSourceGC spaceshipCrash = (DamageSourceGC) new DamageSourceGC("spaceship_crash").setDamageBypassesArmor().setExplosion();
    public static final DamageSourceGC oxygenSuffocation = (DamageSourceGC) new DamageSourceGC("oxygen_suffocation").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSourceGC thermal = (DamageSourceGC) new DamageSourceGC("thermal").setDamageBypassesArmor().setDamageIsAbsolute();
    public static final DamageSourceGC acid = (DamageSourceGC) new DamageSourceGC("sulphuric_acid");
    public static final DamageSourceGC laserTurret = new DamageSourceGC("laser_turret");

    public DamageSourceGC(String damageType)
    {
        super(damageType);
    }
}
