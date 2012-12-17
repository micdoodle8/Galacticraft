package micdoodle8.mods.galacticraft.core;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityPlayer;

public class GCCoreDamageSource extends DamageSource
{
    public static DamageSource spaceshipExplosion = new GCCoreDamageSource("spaceshipExplosion");
    
	public GCCoreDamageSource(String par1Str) 
	{
		super(par1Str);
	}

	@Override
    public String getDeathMessage(EntityPlayer par1EntityPlayer)
    {
        return par1EntityPlayer.username + GalacticraftCore.lang.get("death." + this.damageType);
    }
}
