package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.common.FMLCommonHandler;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InGameScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityScreen extends TileEntity
{
    public int imageType = 0;
    public int maxTypes = 4;
	public InGameScreen screen;
    
	public TileEntityScreen()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			this.screen = new InGameScreen(0F, 0F, 1.0F, 1.0F);
	}
	
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
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.imageType = nbt.getInteger("type");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("type", this.imageType);
    }
}
