//package micdoodle8.mods.galacticraft.core.blocks.pipe;
//
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//
//public class GCCoreTileEntity extends TileEntity
//{
//	public int direction;
//	
//    @Override
//    public void validate()
//    {
//     	super.validate();
//
//        if (!isInvalid() && worldObj != null)
//        {
//        	this.onTileEntityCreation();
//        }
//    }
//    
//    public void onTileEntityCreation()
//    {
//    	
//    }
//
//    @Override
//    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
//    {
//    	super.readFromNBT(par1NBTTagCompound);
//    	direction = par1NBTTagCompound.getInteger("direction");
//    }
//
//    @Override
//    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
//    {
//    	super.writeToNBT(par1NBTTagCompound);
//    	par1NBTTagCompound.setInteger("direction", direction);
//    }
//    
//    public int getDirection (int dir)
//    {
//    	return this.direction;
//    }
//    
//    public void setDirection (int dir)
//    {
//    	this.direction = dir;
//    }
//}