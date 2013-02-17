package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class GCCoreEntityControllable extends Entity
{
	public GCCoreEntityControllable(World par1World)
	{
		super(par1World);
	}

	public abstract void keyPressed(int par1, EntityPlayer par2EntityPlayer);
}
