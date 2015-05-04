package micdoodle8.mods.galacticraft.api.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.gui.IUpdatePlayerListBox;

/**
 * Implement into entities that make a sound all the time, like rockets
 */
public interface IEntityNoisy
{
	@SideOnly(Side.CLIENT)
	public IUpdatePlayerListBox getSoundUpdater();

	@SideOnly(Side.CLIENT)
	public ISound setSoundUpdater(EntityPlayerSP player);
}
