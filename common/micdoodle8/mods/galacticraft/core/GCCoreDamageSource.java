package micdoodle8.mods.galacticraft.core;

import net.minecraft.util.DamageSource;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreDamageSource extends DamageSource
{
    public static final GCCoreDamageSource spaceshipCrash = (GCCoreDamageSource) new GCCoreDamageSource("spaceshipCrash").setDeathMessage("%1$s was in a spaceship crash!").setDamageBypassesArmor();
    public static final GCCoreDamageSource oxygenSuffocation = (GCCoreDamageSource) new GCCoreDamageSource("oxygenSuffocation").setDeathMessage("%1$s ran out of oxygen!").setDamageBypassesArmor();

    public GCCoreDamageSource(String damageType)
    {
        super(damageType);
    }

    public GCCoreDamageSource setDeathMessage(String deathMessage)
    {
        LanguageRegistry.instance().addStringLocalization("death.attack." + this.damageType, deathMessage);
        return this;
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
