package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelAssemblyTable;
import net.minecraft.tileentity.TileEntity;

public class GCCoreTileEntityAdvancedCraftingTable extends TileEntity
{
    public GCCoreModelAssemblyTable model = new GCCoreModelAssemblyTable();

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.model = new GCCoreModelAssemblyTable();
      	}
  	}
}
