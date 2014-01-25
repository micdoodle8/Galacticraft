package gregtechmod.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * A simple compound Interface for all my TileEntities.
 * 
 * Also delivers most of the Informations about my TileEntities.
 * 
 * It can cause Problems to include this Interface!
 */
public interface IGregTechTileEntity extends IGearEnergyTileEntity, ICoverable, IFluidHandler, ITurnable, IGregTechDeviceInformation, IUpgradableMachine, IDigitalChest, IDescribable, IMachineBlockUpdateable {
	/**
	 * gets the Error displayed on the GUI
	 */
	public int getErrorDisplayID();
	
	/**
	 * sets the Error displayed on the GUI
	 */
	public void setErrorDisplayID(int aErrorID);

	/**
	 * @return the MetaID of the Block or the MetaTileEntity ID.
	 */
	public int getMetaTileID();
	
	/**
	 * Internal Usage only!
	 */
	public int setMetaTileID(short aID);

	/**
	 * @return the MetaTileEntity which is belonging to this, or null if it doesnt has one.
	 */
	public IMetaTileEntity getMetaTileEntity();
	
	/**
	 * Sets the MetaTileEntity.
	 * Even though this uses the Universal Interface, certain BaseMetaTileEntities only accept one kind of MetaTileEntity
	 * so only use this if you are sure its the correct one or you will get a Class cast Error.
	 * @param aMetaTileEntity 
	 */
	public void setMetaTileEntity(IMetaTileEntity aMetaTileEntity);
	
	/**
	 * Causes a general Texture update.
	 * 
	 * Only used Client Side to mark Blocks dirty.
	 */
	public void issueTextureUpdate();
	
	/**
	 * Causes the Machine to send its initial Data, like Covers and its ID.
	 */
	public void issueClientUpdate();
	
	/**
	 * causes Explosion. Strength in Overload-EU
	 */
	public void doExplosion(int aExplosionEU);
	
    /**
     * Sets the Block on Fire in all 6 Directions
     */
    public void setOnFire();
    
    /**
     * Sets the Owner of the Machine. Returns the set Name.
     */
    public String setOwnerName(String aName);
    
    /**
     * gets the Name of the Machines Owner or "Player" if not set.
     */
    public String getOwnerName();
    
	/**
	 * Sets initial Values from NBT
	 * @param tNBT is the NBTTag of readFromNBT
	 * @param aID is the MetaTileEntityID
	 */
	public void setInitialValuesAsNBT(NBTTagCompound aNBT, short aID);
	
	/**
	 * @return 0 - 15 are Colors, 16-31 are the same Colors but on a bleached Machine, while -1 means uncolored and -2 means bleached.
	 */
	public byte getColorization();
	
	/**
	 * Sets the Color Modulation of the Block
	 * @param aColor the Color you want to set it to. -1 for reset, -2 for bleached
	 */
	public byte setColorization(byte aColor);
	
	
	/**
	 * Called when leftclicking the TileEntity
	 */
	public void onLeftclick(EntityPlayer aPlayer);
	
	/**
	 * Called when rightclicking the TileEntity
	 */
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float par1, float par2, float par3);
	
	public float getBlastResistance(byte aSide);
	public int getTextureIndex(byte aSide, byte aMeta);
	public Icon getTextureIcon(byte aSide, byte aMeta);
}