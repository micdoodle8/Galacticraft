package gregtechmod.api.interfaces;

import gregtechmod.api.util.GT_Config;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Warning, this Interface has just been made to be able to add multiple kinds of MetaTileEntities (Cables, Pipes, Transformers, but not the regular Blocks)
 * 
 * Don't implement this yourself and expect it to work. Extend @MetaTileEntity itself.
 */
public interface IMetaTileEntity extends ISidedInventory, IFluidTank, IFluidHandler {
	/**
	 * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
	 * 
	 * 0 = BaseMetaTileEntity
	 * 1 = BaseMetaPipeEntity
	 */
	public byte getTileEntityBaseType();
	
	/**
	 * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
	 * @return a newly created and ready MetaTileEntity
	 */
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);
	
	/**
	 * @return an ItemStack representing this MetaTileEntity.
	 */
	public ItemStack getStackForm(long aAmount);
	
	/**
	 * Sets the BaseMetaTileEntity of this
	 */
	public void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity);
	
	/**
	 * new getter for the BaseMetaTileEntity, which restricts usage to certain Functions.
	 */
	public IGregTechTileEntity getBaseMetaTileEntity();

	/**
	 * when placing a Machine in World, to initialize default Modes. aNBT can be null!
	 */
	public void initDefaultModes(NBTTagCompound aNBT);
	
	/**
	 * ^= writeToNBT
	 */
	public void saveNBTData(NBTTagCompound aNBT);
	
	/**
	 * ^= readFromNBT
	 */
	public void loadNBTData(NBTTagCompound aNBT);
	
	/**
	 * Adds the NBT-Information to the ItemStack, when being dismanteled properly
	 * Used to store Machine specific Upgrade Data.
	 */
	public void setItemNBT(NBTTagCompound aNBT);
	
	/**
	 * Called in the registered MetaTileEntity when the Server starts, to reset static variables
	 */
	public void onServerStart();
	
	/**
	 * Called in the registered MetaTileEntity when the Server ticks the first time, to initialize static variables
	 */
	public void onFirstServerTick();
	
	/**
	 * Called in the registered MetaTileEntity when the Server stops, to reset static variables
	 */
	public void onServerStop();
	
	/**
	 * Called to set Configuration values for this MetaTileEntity.
	 * Use aConfig.addAdvConfig(GT_Config_Category.machineconfig, "MetaTileEntityName.Ability", DEFAULT_VALUE); to set the Values.
	 */
	public void onConfigLoad(GT_Config aConfig);
	
	/**
	 * If a Cover of that Type can be placed on this Side.
	 * Also Called when the Facing of the Block Changes and a Cover is on said Side.
	 */
	public boolean allowCoverOnSide(byte aSide, int aCoverID);
	
	/**
	 * When a Player rightclicks the Facing with a Screwdriver.
	 * Getting called after the Cover is getting set.
	 */
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ);
	
	/**
	 * Called right before this Machine explodes
	 */
	public void onExplosion();
	
	/**
	 * The First processed Tick which was passed to this MetaTileEntity
	 */
	public void onFirstTick();
	
	/**
	 * The Tick before all the generic handling happens, what gives a slightly faster reaction speed.
	 * Don't use this if you really don't need to. @onPostTick is better suited for ticks.
	 * This happens still after the Cover handling.
	 */
	public void onPreTick();
	
	/**
	 * The Tick after all the generic handling happened.
	 * Recommended to use this like updateEntity.
	 */
	public void onPostTick();
	
	/**
	 * Called when this MetaTileEntity gets (intentionally) disconnected from the BaseMetaTileEntity.
	 * Doesn't get called when this thing is moved by Frames or similar hacks.
	 */
	public void inValidate();
	
	/**
	 * Called when the BaseMetaTileEntity gets invalidated, what happens right before the @inValidate above gets called
	 */
	public void onRemoval();
	
    /**
     * @param aFacing
     * @return if aFacing would be a valid Facing for this Device. Used for wrenching.
     */
    public boolean isFacingValid(byte aFacing);
    
	/**
	 * From new ISidedInventory
	 */
	public boolean allowPullStack(int aIndex, byte aSide, ItemStack aStack);
	
	/**
	 * From new ISidedInventory
	 */
	public boolean allowPutStack(int aIndex, byte aSide, ItemStack aStack);
	
    /**
     * @return if aIndex is a valid Slot. false for things like HoloSlots. Is used for determining if an Item is dropped upon Block destruction and for Inventory Access Management
     */
    public boolean isValidSlot(int aIndex);
    
    /**
     * @return if aIndex can be set to Zero stackSize, when being removed.
     */
    public boolean setStackToZeroInsteadOfNull(int aIndex);

    /**
     * If this Side can connect to inputting pipes
     */
    public boolean isLiquidInput(byte aSide);
    
    /**
     * If this Side can connect to outputting pipes
     */
    public boolean isLiquidOutput(byte aSide);
    
	/**
	 * Just an Accessor for the Name variable.
	 */
	public String getMetaName();
	
    /**
     * @return true if the Machine can be accessed
     */
    public boolean isAccessAllowed(EntityPlayer aPlayer);
    
    /**
     * @return if the Machine can be dismanteled right now.
     */
	public boolean isWrenchable();
	
	/**
	 * When a Machine Update occurs
	 */
	public void onMachineBlockUpdate();

	/**
	 * a Player rightclicks the Machine
	 * Sneaky rightclicks are not getting passed to this!
	 * @return 
	 */
	public boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ);
	
	/**
	 * a Player leftclicks the Machine
	 * Sneaky leftclicks are getting passed to this unlike with the rightclicks.
	 */
	public void onLeftclick(EntityPlayer aPlayer);
	
	/**
	 * Called Clientside with the Data got from @getUpdateData
	 */
	public void onValueUpdate(byte aValue);
	
	/**
	 * return a small bit of Data, like a secondary Facing for example with this Function, for the Client.
	 * The BaseMetaTileEntity detects changes to this Value and will then send an Update.
	 * This is only for Information, which is visible as Texture to the outside.
	 * 
	 * If you just want to have an Active/Redstone State then set the Active State inside the BaseMetaTileEntity instead.
	 */
	public byte getUpdateData();
	
	/**
	 * For the rare case you need this Function
	 */
	public void receiveClientEvent(byte aEventID, byte aValue);
	
	/**
	 * Called to actually play the Sound.
	 * Do not insert Client/Server checks. That is already done for you.
	 * Do not use @playSoundEffect, Minecraft doesn't like that at all. Use @playSound instead.
	 */
    public void doSound(byte aIndex, double aX, double aY, double aZ);
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ);
    public void stopSoundLoop(byte aValue, double aX, double aY, double aZ);
	
    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    public void sendSound(byte aIndex);
    
    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    public void sendLoopStart(byte aIndex);
    
    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    public void sendLoopEnd(byte aIndex);
    
	/**
	 * If this is just a simple Machine, which can be wrenched at 100%
	 */
	public boolean isSimpleMachine();
	
	/**
	 * If there should be a Lag Warning if something laggy happens during this Tick.
	 * 
	 * The Advanced Pump uses this to not cause the Lag Message, while it scans for all close Fluids.
	 */
	public boolean doTickProfilingMessageDuringThisTick();
	
    /**
     * returns the DebugLog
     */
	public ArrayList<String> getSpecialDebugInfo(EntityPlayer aPlayer, int aLogLevel, ArrayList<String> aList);

	/**
	 * get a small Description
	 */
	public String getDescription();
	
	/**
	 * In case the Output Voltage varies.
	 */
	public String getSpecialVoltageToolTip();
	
	/**
	 * Index of the Texture, if the Icon call fails. You have to implement this even if you don't need it.
	 * 
	 * @param aSide is the Side of the Block
	 * @param aFacing is the direction the Block is facing
	 * @param aActive if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!)
	 * @param aRedstone if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
	 */
	public abstract int getTextureIndex(byte aSide, byte aFacing, boolean aActive, boolean aRedstone);
	
	/**
	 * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
	 * 
	 * @param aSide is the Side of the Block
	 * @param aFacing is the direction the Block is facing
	 * @param aActive if the Machine is currently active (use this instead of calling mBaseMetaTileEntity.mActive!!!)
	 * @param aRedstone if the Machine is currently outputting a RedstoneSignal (use this instead of calling mBaseMetaTileEntity.mRedstone!!!)
	 */
	public Icon getTextureIcon(byte aSide, byte aFacing, boolean aActive, boolean aRedstone);
	
	/**
	 * Register Icons here. This gets called when the Icons get initialized by the Base Block
	 * Best is you put your Icons in a static Array for quick and easy access without relying on the MetaTileList.
	 * @param aBlockIconRegister The Block Icon Register
	 */
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister aBlockIconRegister);
	
	/**
	 * Gets the Output for the comparator on the given Side
	 */
	public byte getComparatorValue(byte aSide);
	
	public float getExplosionResistance(byte aSide);
	
	@Override
	public String getInvName();
	
	public String[] getInfoData();
	public boolean isGivingInformation();
	
	public ItemStack[] getRealInventory();
}