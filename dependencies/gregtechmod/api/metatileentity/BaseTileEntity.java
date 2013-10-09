package gregtechmod.api.metatileentity;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IHasWorldObjectAndCoords;
import gregtechmod.api.util.GT_Log;
import gregtechmod.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.packet.Packet54PlayNoteBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * The Functions my old TileEntities and my BaseMetaTileEntities have in common.
 * 
 * Basically everything a TileEntity should have.
 */
public abstract class BaseTileEntity extends TileEntity implements IHasWorldObjectAndCoords {
	/**
	 * If this TileEntity checks for the Chunk to be loaded before returning World based values.
	 * The AdvPump hacks this to false to ensure everything runs properly even when far Chunks are not actively loaded.
	 * But anything else should not cause worfin' Chunks, uhh I mean orphan Chunks.
	 */
	public boolean ignoreUnloadedChunks = true;
	
	@Override public final int   getXCoord() {return        xCoord;}
	@Override public final short getYCoord() {return (short)yCoord;}
	@Override public final int   getZCoord() {return        zCoord;}
	@Override public final World getWorld () {return      worldObj;}
	@Override public final boolean isServerSide() {return !getWorld().isRemote;}
	@Override public final boolean isClientSide() {return  getWorld().isRemote;}
    @Override public final int getRandomNumber(int aRange) {return getWorld().rand.nextInt(aRange);}
	@Override public final BiomeGenBase getBiome(int aX, int aZ) {return getWorld().getBiomeGenForCoords(aX, aZ);}
	@Override public final BiomeGenBase getBiome() {return getBiome(getXCoord(), getZCoord());}
    @Override public final short getBlockID(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return 0; return (short)getWorld().getBlockId(aX, aY, aZ);}
    @Override public final short getBlockIDOffset(int aX, int aY, int aZ) {return getBlockID(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
    @Override public final short getBlockIDAtSide(byte aSide) {return getBlockIDAtSideAndDistance(aSide, 1);}
    @Override public final short getBlockIDAtSideAndDistance(byte aSide, int aDistance) {return getBlockID(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
    @Override public final byte getMetaID(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return 0; return (byte)getWorld().getBlockMetadata(aX, aY, aZ);}
    @Override public final byte getMetaIDOffset(int aX, int aY, int aZ) {return getMetaID(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
    @Override public final byte getMetaIDAtSide(byte aSide) {return getMetaIDAtSideAndDistance(aSide, 1);}
    @Override public final byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {return getMetaID(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
    @Override public final byte getLightLevel(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return 0; return (byte)(getWorld().getLightBrightness(aX, aY, aZ)*15);}
	@Override public final byte getLightLevelOffset(int aX, int aY, int aZ) {return getLightLevel(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
	@Override public final byte getLightLevelAtSide(byte aSide) {return getLightLevelAtSideAndDistance(aSide, 1);}
	@Override public final byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {return getLightLevel(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
	@Override public final boolean getSky(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return false; return getWorld().canBlockSeeTheSky(aX, aY, aZ);}
	@Override public final boolean getSkyOffset(int aX, int aY, int aZ) {return getSky(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
	@Override public final boolean getSkyAtSide(byte aSide) {return getSkyAtSideAndDistance(aSide, 1);}
	@Override public final boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {return getSky(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
	@Override public final boolean getAir(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return true; return GT_Utility.isAirBlock(getWorld(), aX, aY, aZ);}
	@Override public final boolean getAirOffset(int aX, int aY, int aZ) {return getAir(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
	@Override public final boolean getAirAtSide(byte aSide) {return getAirAtSideAndDistance(aSide, 1);}
	@Override public final boolean getAirAtSideAndDistance(byte aSide, int aDistance) {return getAir(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
	@Override public final TileEntity getTileEntity(int aX, int aY, int aZ) {if (ignoreUnloadedChunks && !getWorld().blockExists(aX, aY, aZ)) return null; return getWorld().getBlockTileEntity(aX, aY, aZ);}
    @Override public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {return getTileEntity(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
    @Override public final TileEntity getTileEntityAtSide(byte aSide) {return getTileEntityAtSideAndDistance(aSide, 1);}
    @Override public final TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {return getTileEntity(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
    @Override public final IInventory getIInventory(int aX, int aY, int aZ) {TileEntity tTileEntity = getTileEntity(aX, aY, aZ); if (tTileEntity != null && tTileEntity instanceof IInventory) return (IInventory)tTileEntity; return null;}
    @Override public final IInventory getIInventoryOffset(int aX, int aY, int aZ) {return getIInventory(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
    @Override public final IInventory getIInventoryAtSide(byte aSide) {return getIInventoryAtSideAndDistance(aSide, 1);}
    @Override public final IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {return getIInventory(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
    @Override public final IFluidHandler getITankContainer(int aX, int aY, int aZ) {TileEntity tTileEntity = getTileEntity(aX, aY, aZ); if (tTileEntity != null && tTileEntity instanceof IFluidHandler) return (IFluidHandler)tTileEntity; return null;}
    @Override public final IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {return getITankContainer(getXCoord()+aX, getYCoord()+aY, getZCoord()+aZ);}
    @Override public final IFluidHandler getITankContainerAtSide(byte aSide) {return getITankContainerAtSideAndDistance(aSide, 1);}
    @Override public final IFluidHandler getITankContainerAtSideAndDistance(byte aSide, int aDistance) {return getITankContainer(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));}
    @Override public final boolean openGUI(EntityPlayer aPlayer, int aID) {return openGUI(aPlayer, aID, GregTech_API.gregtechmod);}
    @Override public final boolean openGUI(EntityPlayer aPlayer, int aID, Object aMod) {if (aPlayer == null || aMod == null) return false; aPlayer.openGui(aMod, aID, getWorld(), getXCoord(), getYCoord(), getZCoord()); return true;}
    @Override public final int   getOffsetX(byte aSide, int aMultiplier) {return         getXCoord() + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier ;}
    @Override public final short getOffsetY(byte aSide, int aMultiplier) {return (short)(getYCoord() + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);}
    @Override public final int   getOffsetZ(byte aSide, int aMultiplier) {return         getZCoord() + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier ;}
    
	@Override public final void sendBlockEvent(byte aID, byte aValue) {
		try {
			for (Object tObject : getWorld().playerEntities) {
				if (tObject instanceof EntityPlayerMP) {
					EntityPlayerMP tPlayer = (EntityPlayerMP)tObject;
					if (Math.abs(tPlayer.posX - getXCoord()) < 256 && Math.abs(tPlayer.posZ - getZCoord()) < 256) {
						tPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet54PlayNoteBlock(getXCoord(), getYCoord(), getZCoord(), getBlockIDOffset(0, 0, 0), aID, aValue));
					}
				} else {
					getWorld().addBlockEvent(getXCoord(), getYCoord(), getZCoord(), getBlockIDOffset(0, 0, 0), aID, aValue);
					break;
				}
			}
		} catch(Throwable e) {
			getWorld().addBlockEvent(getXCoord(), getYCoord(), getZCoord(), getBlockIDOffset(0, 0, 0), aID, aValue);
			if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);
		}
	}
}
