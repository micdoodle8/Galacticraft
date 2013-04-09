package micdoodle8.mods.galacticraft.API;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;

public interface IDockable extends IFuelable
{
	public void setLandingPad(GCCoreTileEntityLandingPad pad);
	
	public GCCoreTileEntityLandingPad getLandingPad();
	
	public void onPadDestroyed();
}
