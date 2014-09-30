package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.client.gui.screen.InGameScreen;
import net.minecraft.tileentity.TileEntity;

public class TileEntityScreen extends TileEntity
{
    public int imageType = 0;
    public int maxTypes = 4;
	public InGameScreen screen = new InGameScreen();
    
	@Override
    public boolean canUpdate()
    {
        return false;
    }

	public void changeChannel()
	{
		if (++this.imageType == maxTypes)
			this.imageType = 0;
	}
	
}
