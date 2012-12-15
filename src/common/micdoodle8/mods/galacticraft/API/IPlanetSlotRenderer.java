package micdoodle8.mods.galacticraft.API;

import net.minecraft.src.Tessellator;

public interface IPlanetSlotRenderer 
{
	public String getPlanetSprite();
	
	public String getPlanetName();
	
	public void renderSlot(int index, int x, int y, float slotHeight, Tessellator tessellator);
}
