package micdoodle8.mods.galacticraft.core;

import net.minecraft.util.DamageSource;

public class GCCoreDamageSource extends DamageSource
{
    public static final GCCoreDamageSource spaceshipCrash = (GCCoreDamageSource) new GCCoreDamageSource("spaceshipCrash").setDamageBypassesArmor();
    public static final GCCoreDamageSource oxygenSuffocation = (GCCoreDamageSource) new GCCoreDamageSource("oxygenSuffocation").setDamageBypassesArmor();

    public GCCoreDamageSource(String damageType)
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
