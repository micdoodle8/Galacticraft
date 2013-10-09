package gregtechmod.api.events;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.world.WorldEvent;

@Cancelable
public class GT_ScannerEvent extends WorldEvent {
	
	public final EntityPlayer mPlayer;
	public final int mX, mY, mZ, mScanLevel;
	public final ArrayList<String> mList;
	public final byte mSide;
	public final float mClickX, mClickY, mClickZ;
	public final TileEntity mTileEntity;
	public final Block mBlock;
	
	/**
	 * used to determine the amount of Energy this Scan is costing.
	 */
	public int mEUCost = 0;
	
	public GT_ScannerEvent(World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ, byte aSide, int aScanLevel, Block aBlock, TileEntity aTileEntity, ArrayList<String> aList, float aClickX, float aClickY, float aClickZ) {
		super(aWorld);
		mPlayer = aPlayer;
		mScanLevel = aScanLevel;
		mTileEntity = aTileEntity;
		mBlock = aBlock;
		mList = aList;
		mSide = aSide;
		mX = aX;
		mY = aY;
		mZ = aZ;
		mClickX = aClickX;
		mClickY = aClickY;
		mClickZ = aClickZ;
	}
}