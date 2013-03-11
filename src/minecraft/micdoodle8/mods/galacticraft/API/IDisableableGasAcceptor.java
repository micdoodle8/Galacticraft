package micdoodle8.mods.galacticraft.API;

import mekanism.api.IGasAcceptor;

public interface IDisableableGasAcceptor extends IGasAcceptor
{
	public void setNotRecievingGas();
	
	public void setRecievingGas();
}
