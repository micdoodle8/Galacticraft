package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.common.FMLCommonHandler;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.screen.DrawGameScreen;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityScreen extends TileEntity
{
    public int imageType = 0;
    public static int maxTypes;
	public DrawGameScreen screen;
	public boolean connectedUp;
	public boolean connectedDown;
	public boolean connectedLeft;
	public boolean connectedRight;
	public int connectionsUp;
	public int connectionsDown;
	public int connectionsLeft;
	public int connectionsRight;
	public boolean isMultiscreen;

	public int screenOffsetx = 0;
	public int screenOffsetz = 0;
	
	private int requiresUpdate = 0;
	
	@Override
	public void validate()
	{
        super.validate();
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			this.screen = new DrawGameScreen(1.0F, 1.0F);
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_VIEWSCREEN_REQUEST, new Object[] { this.worldObj.provider.dimensionId, this.xCoord, this.yCoord, this.zCoord} ));
		}
	}
	
	public void updateClients()
	{
		int connectedFlags = 0;
        if (this.connectedUp) connectedFlags += 8;
        if (this.connectedDown) connectedFlags += 4;
        if (this.connectedLeft) connectedFlags += 2;
        if (this.connectedRight) connectedFlags += 1;
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_VIEWSCREEN, new Object[] { this.xCoord, this.yCoord, this.zCoord, this.imageType, connectedFlags } ), this.worldObj.provider.dimensionId);
	}
	
	@Override
	public void invalidate()
	{
		int meta = this.getBlockMetadata() & 7;
		super.invalidate();
		this.breakScreen(meta);
	}

	/**
	 * Call when a screen (which maybe part of a multiscreen) is either
	 * broken or rotated.
	 * 
	 * @param meta  The meta of the screen prior to breaking or rotation
	 */
	public void breakScreen(int meta)
	{
		BlockVec3 vec = new BlockVec3(this);
		TileEntity tile;
		int side = this.getRight(meta);

		int left = this.connectionsLeft;
		int right = this.connectionsRight;
		int up = this.connectionsUp;
		int down = this.connectionsDown;
	
		boolean doUp = this.connectedUp;
		boolean doDown = this.connectedDown;
		boolean doLeft = this.connectedLeft;
		boolean doRight = this.connectedRight;

    	for (int x = -left; x <= right; x++)
    	{
    		for (int z = -up; z <= down; z++)
    		{
    			if (x == 0 && z == 0) this.resetToSingle();
    			else
    			{
	    			BlockVec3 newVec = vec.clone().modifyPositionFromSide(ForgeDirection.getOrientation(side), x).modifyPositionFromSide(ForgeDirection.DOWN, z);
	    			tile = newVec.getTileEntity(this.worldObj);
	    			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
	    			{
	    				((TileEntityScreen)tile).resetToSingle();
	    			}
    			}
    		}
   		}
    	
    	//TODO  Try to generate largest screen possible out of remaining blocks

		this.connectedUp = this.connectedDown = this.connectedLeft = this.connectedRight = false;

		if (doUp)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 1);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta && !tile.isInvalid())
			{
				if (doLeft) ((TileEntityScreen)tile).connectedLeft = true; 
				if (doRight) ((TileEntityScreen)tile).connectedRight = true;
				((TileEntityScreen)tile).connectedUp = true;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doDown)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 0);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta && !tile.isInvalid())
			{
				if (doLeft) ((TileEntityScreen)tile).connectedLeft = true; 
				if (doRight) ((TileEntityScreen)tile).connectedRight = true;
				((TileEntityScreen)tile).connectedDown = true;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doLeft)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getLeft(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta && !tile.isInvalid())
			{
				if (doUp) ((TileEntityScreen)tile).connectedUp = true; 
				if (doDown) ((TileEntityScreen)tile).connectedDown = true;
				((TileEntityScreen)tile).connectedLeft = true;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doRight)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getRight(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta && !tile.isInvalid())
			{
				if (doUp) ((TileEntityScreen)tile).connectedUp = true; 
				if (doDown) ((TileEntityScreen)tile).connectedDown = true;
				((TileEntityScreen)tile).connectedRight = true;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
	}

	/**
	 * Check whether the screen can sustain 'multi-screen' connections on each of its 4 sides
	 * (note: this can be called recursively from inside itself)
	 * 
	 * @param  doScreen  If true, build a new multi-screen if connections are found
	 */
	public void refreshConnections(boolean doScreen)
	{
		this.log("Starting connection check");

		int meta = this.getBlockMetadata() & 7;
		if (meta < 2)
		{
			//TODO System.out.println("Up/down oriented screens cannot be multiscreen");
			this.resetToSingle();
			return;
		}
		
		TileEntity tileUp = null;
		TileEntity tileDown = null;
		TileEntity tileLeft = null;
		TileEntity tileRight = null;
		BlockVec3 vec = new BlockVec3(this);

		//First, basic check that a neighbour is there and in the same orientation 
		if (this.connectedUp)
		{
			tileUp = vec.getTileEntityOnSide(this.worldObj, 1);
			if (!(tileUp instanceof TileEntityScreen && tileUp.getBlockMetadata() == meta && !tileUp.isInvalid()))
				this.connectedUp = false;
		}

		if (this.connectedDown)
		{
			tileDown = vec.getTileEntityOnSide(this.worldObj, 0);
			if (!(tileDown instanceof TileEntityScreen && tileDown.getBlockMetadata() == meta && !tileDown.isInvalid()))
				this.connectedDown = false;
		}

		if (this.connectedLeft)
		{
			int side = this.getLeft(meta);
			tileLeft = vec.getTileEntityOnSide(this.worldObj, side);
			if (!(tileLeft instanceof TileEntityScreen && tileLeft.getBlockMetadata() == meta && !tileLeft.isInvalid()))
				this.connectedLeft = false;
		}

		if (this.connectedRight)
		{
			int side = this.getRight(meta);
			tileRight = vec.getTileEntityOnSide(this.worldObj, side);
			if (!(tileRight instanceof TileEntityScreen && tileRight.getBlockMetadata() == meta && !tileRight.isInvalid()))
				this.connectedRight = false;
		}

		//Now test whether a connection can be sustained with that other tile
		if (this.connectedUp)
		{
    		if (tileUp == null || !this.tryConnectUp((TileEntityScreen)tileUp))
				this.connectedUp = false;
		}

		if (this.connectedDown)
		{
			if (tileDown == null || !this.tryConnectDown((TileEntityScreen)tileDown))
				this.connectedDown = false;
		}

		if (this.connectedLeft)
		{
			if (tileLeft == null || !this.tryConnectLeft((TileEntityScreen)tileLeft))
				this.connectedLeft = false;
		}

		if (this.connectedRight)
		{
			if (tileRight == null || !this.tryConnectRight((TileEntityScreen)tileRight))
				this.connectedRight = false;
		}
		this.log("Ending connection check");
		if (doScreen)
		{
			this.checkScreenSize();
			this.markDirty();
		}
	}
	
	@Override
    public boolean canUpdate()
    {
        return false;
    }

	/**
	 * Cycle through different screen contents
	 */
	public void changeChannel()
	{
		if (!this.worldObj.isRemote)
		{
			if (++this.imageType >= maxTypes)
				this.imageType = 0;

			//Temporary code for test purposes - always try to connect
			this.connectedUp = this.connectedDown = this.connectedLeft = this.connectedRight = true;
			this.refreshConnections(true);
			this.markDirty();

			this.updateClients();
		}
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.imageType = nbt.getInteger("type");
        this.connectionsDown = nbt.getInteger("connectionsDown");
        this.connectionsUp = nbt.getInteger("connectionsUp");
        this.connectionsLeft = nbt.getInteger("connectionsLeft");
        this.connectionsRight = nbt.getInteger("connectionsRight");
        this.isMultiscreen = nbt.getBoolean("multiscreen");
        this.connectedUp = (this.connectionsUp > 0);
        this.connectedDown = (this.connectionsDown > 0);
        this.connectedLeft = (this.connectionsLeft > 0);
        this.connectedRight = (this.connectionsRight > 0);
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("type", this.imageType);
        nbt.setInteger("connectionsDown", this.connectionsDown);
        nbt.setInteger("connectionsUp", this.connectionsUp);
        nbt.setInteger("connectionsLeft", this.connectionsLeft);
        nbt.setInteger("connectionsRight", this.connectionsRight);
        nbt.setBoolean("multiscreen", this.isMultiscreen);
    }
    
    public void checkScreenSize()
    {
    	this.log("Checking screen size");
    	int up = 0;
    	int down = 0;
    	int left = 0;
    	int right = 0;
		int meta = this.getBlockMetadata() & 7;

    	BlockVec3 vec = new BlockVec3(this);
    	TileEntityScreen tile = this;
    	while (up < 5)
    	{
    		if (tile.connectedUp)
    		{
    			up++;
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, 1);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec.translate(0,  1,  0);
    			}
    			else
    			{
    				System.out.println("Debug - connected up to a non-screen tile");
    				tile.connectedUp = false;
    				tile.markDirty();
    				up--;
    				break;
    			}
    		}
    		else
    			break;
    	}

    	vec = new BlockVec3(this);
    	tile = this;
    	while (down < 5)
    	{
    		if (tile.connectedDown)
    		{
    			down++;
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, 0);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec.translate(0,  -1,  0);
    			}
    			else
    			{
    				System.out.println("Debug - connected down to a non-screen tile");
    				tile.connectedDown = false;
    				tile.markDirty();
    				down--;
    				break;
    			}
    		}
    		else
    			break;
    	}

    	vec = new BlockVec3(this);
    	tile = this;
		int leftside = this.getLeft(meta);
    	while (left < ((up + down == 0) ? 1 : 5))
    	{
    		if (tile.connectedLeft)
    		{
    			left++;
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, leftside);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec = vec.newVecSide(leftside);
    			}
    			else
    			{
    				System.out.println("Debug - connected left to a non-screen tile");
    				tile.connectedLeft = false;
    				tile.markDirty();
    				left--;
    				break;
    			}
    		}
    		else
    			break;
    	}

    	vec = new BlockVec3(this);
    	tile = this;
		int rightside = this.getRight(meta);
    	while (right < ((up + down == 0) ? 1 : 5))
    	{
    		if (tile.connectedRight)
    		{
    			right++;
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, rightside);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec = vec.newVecSide(rightside);
    			}
    			else
    			{
    				System.out.println("Debug - connected right to a non-screen tile");
    				tile.connectedRight = false;
    				tile.markDirty();
    				right--;
    				break;
    			}
    		}
    		else
    			break;
    	}
    	
    	this.log("Screen size check midpoint "+up+" "+down+" "+left+" "+right+" ");

    	vec = new BlockVec3(this);
    	TileEntity newtile = vec.getTileEntityOnSide(this.worldObj, 1);
    	TileEntityScreen tileUp = (newtile instanceof TileEntityScreen) ? (TileEntityScreen)newtile : null; 
    	newtile = vec.getTileEntityOnSide(this.worldObj, 0);
    	TileEntityScreen tileDown = (newtile instanceof TileEntityScreen) ? (TileEntityScreen)newtile : null;
    	newtile = vec.getTileEntityOnSide(this.worldObj, leftside);
    	TileEntityScreen tileLeft = (newtile instanceof TileEntityScreen) ? (TileEntityScreen)newtile : null;
    	newtile = vec.getTileEntityOnSide(this.worldObj, rightside);
    	TileEntityScreen tileRight = (newtile instanceof TileEntityScreen) ? (TileEntityScreen)newtile : null;
    	//Prevent 3 x 1 and longer
    	if (left + right == 0 && up + down >= 1)
    	{
    		if (up > 0 && !tileUp.connectedUp)  //No need for null check if up > 0
    		{
    			up = 1;
    			down = 0;
    		}
    		else
    		{
    			up = 0;
    			if (tileDown != null && !tileDown.connectedDown)
    				down = 1;
    			else
    				down = 0;
    		}
    	}
    	if (up + down == 0 && left + right >= 1)
    	{
    		if (left > 0 && !tileLeft.connectedLeft)  //No need for null check if right > 0
    		{
    			left = 1;
    			right = 0;
    		}
    		else
    		{
    			left = 0;
    			if (tileRight != null && !tileRight.connectedRight)
    				right = 1;
    			else
    				right = 0;
    		}
    	}
    	
    	if (up == 0)
    	{	
    		this.connectedUp = false;
    		if (tileUp != null) tileUp.connectedDown = false;
    	}
    	if (down == 0)
    	{	
    		this.connectedDown = false;
    		if (tileDown != null) tileDown.connectedUp = false;
    	}
    	if (left == 0)
    	{	
    		this.connectedLeft = false;
    		if (tileLeft != null) tileLeft.connectedRight = false;
    	}
    	if (right == 0)
    	{	
    		this.connectedRight = false;
    		if (tileRight != null) tileRight.connectedLeft = false;
    	}

    	this.log("Finished screen size check");
    	
    	this.checkWholeScreen(up, down, left, right);
    }
    
    /**
     * After figuring out the screen edges (overall screen dimensions)
     * check that the screen is a whole A x B rectangle with no tiles missing
     * 
     * If it is whole, set all tiles in the screen to match this screen type 
     * 
     * @param up  Number of blocks the screen edge is away from this in the up direction 
     * @param down  Number of blocks the screen edge is away from this in the down direction
     * @param left  Number of blocks the screen edge is away from this in the left direction
     * @param right  Number of blocks the screen edge is away from this in the right direction
     * @return  True if the screen was whole
     */
    private boolean checkWholeScreen(int up, int down, int left, int right)
    {
    	if (up + down + left + right == 0)
    	{
    		this.resetToSingle();
    		return true;
    	}

    	//System.out.println("Checking screen size at "+this.xCoord+","+this.zCoord+": Up "+up+" Dn "+down+" Lf "+left+" Rg "+right);

    	boolean screenWhole = true;
    	boolean existingScreen = false;
    	int barrierUp = up;
    	int barrierDown = down;
    	int barrierLeft = left;
    	int barrierRight = right;
    	
		int meta = this.getBlockMetadata() & 7;
    	BlockVec3 vec = new BlockVec3(this);
    	ArrayList<TileEntityScreen> screenList = new ArrayList<TileEntityScreen>();

		int side = this.getRight(meta);
		
		DrawGameScreen newScreen = null;
		boolean serverside = true;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			newScreen = new DrawGameScreen(1.0F + left + right, 1.0F + up + down);
			serverside = false;
		}
		
    	for (int x = -left; x <= right; x++)
    	{
    		for (int z = -up; z <= down; z++)
    		{
    			BlockVec3 newVec = vec.clone().modifyPositionFromSide(ForgeDirection.getOrientation(side), x).modifyPositionFromSide(ForgeDirection.DOWN, z);
    			TileEntity tile = newVec.getTileEntity(this.worldObj);
    			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta && !tile.isInvalid())
    			{
    				TileEntityScreen screenTile = (TileEntityScreen)tile;
    				screenList.add(screenTile);
    				
    				if (screenTile.isMultiscreen)
    				{
    					if (screenTile.connectionsUp > z + up)
    					{
    						barrierUp = up - (screenTile.connectionsUp - z - up);
    						existingScreen = true;
    					}
    					if (screenTile.connectionsDown > down - z)
    					{
    						barrierDown = down - (screenTile.connectionsDown + z - down);
    						existingScreen = true;
    					}
    					if (screenTile.connectionsLeft > x + left)
    					{
    						barrierLeft = left - (screenTile.connectionsLeft - x - left);
    						existingScreen = true;
    					}
    					if (screenTile.connectionsRight > right - x)
    					{
    						barrierRight = right - (screenTile.connectionsRight + x - right);
    						existingScreen = true;
    					}
    				}
    			}
    			else
    			{
    				screenWhole = false;
    			}
    		}
    	}
    	
    	if (!screenWhole)
    	{
	    	for (TileEntityScreen scr : screenList)
	   			scr.resetToSingle();
	    	
	    	return false;
    	}

    	if (existingScreen)
    	{
    		return this.checkWholeScreen(barrierUp, barrierDown, barrierLeft, barrierRight);
    	}
    	
    	Iterator<TileEntityScreen> it = screenList.iterator();
    	for (int x = -left; x <= right; x++)
    	{
    		for (int z = -up; z <= down; z++)
    		{
    			TileEntityScreen screenTile = it.next();   			
				screenTile.screenOffsetx = x + left;
				screenTile.screenOffsetz = z + up;
				screenTile.screen = newScreen;
				screenTile.connectionsLeft = x + left;
				screenTile.connectionsRight = right - x;
				screenTile.connectionsUp = z + up;
				screenTile.connectionsDown = down - z;
				screenTile.isMultiscreen = true;
				if (serverside)
				{
					screenTile.imageType = this.imageType;
					screenTile.markDirty();
					screenTile.updateClients();
				}
    			screenTile.refreshConnections(false);
    		}
    	}

    	this.connectionsUp = up;
    	this.connectionsDown = down;
    	this.connectionsLeft = left;
    	this.connectionsRight = right;

       	return true;
    }
    
    /**
     * Reset the screen to a 1x1 size, not part of a 'multi-screen'
     */
    public void resetToSingle()
    {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			this.screen = new DrawGameScreen(1.0F, 1.0F);
		this.screenOffsetx = 0;
		this.screenOffsetz = 0;   		
    	this.connectionsUp = 0;
    	this.connectionsDown = 0;
    	this.connectionsLeft = 0;
    	this.connectionsRight = 0;
    	this.isMultiscreen = false;
    	this.connectedDown = this.connectedLeft = this.connectedRight = this.connectedUp = false;
		this.markDirty();
    }
    
    /**
     * Get the Minecraft direction which is on the left side
     * for the block orientation given by metadata
     */
    private int getLeft(int meta)
    {
		switch (meta)
		{
		case 2:
			return 4;
		case 3:
			return 5;
		case 4:
			return 3;
		case 5:
			return 2;
		}
		return 4;
    }
    
    /**
     * Get the Minecraft direction which is on the right side
     * for the block orientation given by metadata
     */
    private int getRight(int meta)
    {
		switch (meta)
		{
		case 2:
			return 5;
		case 3:
			return 4;
		case 4:
			return 2;
		case 5:
			return 3;
		}
		return 5;
    }

    private boolean tryConnectUp(TileEntityScreen screenTile)
    {
		if (screenTile.connectedDown)
			return true;		//No checks?

		screenTile.connectedDown = true;
		if (this.connectedLeft) screenTile.connectedLeft = true;
		if (this.connectedRight) screenTile.connectedRight = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same left-right connections
		if ((this.connectedLeft ^ screenTile.connectedLeft) || (this.connectedRight ^ screenTile.connectedRight))
		{
			screenTile.connectedDown = false;
			return false;
		}
		
		return true;
    }

    private boolean tryConnectDown(TileEntityScreen screenTile)
    {
		if (screenTile.connectedUp)
			return true;		//No checks?

		screenTile.connectedUp = true;
		if (this.connectedLeft) screenTile.connectedLeft = true;
		if (this.connectedRight) screenTile.connectedRight = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same left-right connections
		if ((this.connectedLeft ^ screenTile.connectedLeft) || (this.connectedRight ^ screenTile.connectedRight))
		{
			screenTile.connectedUp = false;
			return false;
		}
		
		return true;
    }
    
    private boolean tryConnectLeft(TileEntityScreen screenTile)
    {
		if (screenTile.connectedRight)
			return true;		//No checks?

		if ((screenTile.connectedUp && !this.connectedUp) || (screenTile.connectedDown && !this.connectedDown))
			return false;

		screenTile.connectedRight = true;
		if (this.connectedUp) screenTile.connectedUp = true;
		if (this.connectedDown) screenTile.connectedDown = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same up-down connections
		if ((this.connectedUp ^ screenTile.connectedUp) || (this.connectedDown ^ screenTile.connectedDown))
		{
			screenTile.connectedRight = false;
			return false;
		}
		
		return true;
    }

    private boolean tryConnectRight(TileEntityScreen screenTile)
    {
		if (screenTile.connectedLeft)
			return true;		//No checks?

		if ((screenTile.connectedUp && !this.connectedUp) || (screenTile.connectedDown && !this.connectedDown))
			return false;
		
		screenTile.connectedLeft = true;
		if (this.connectedUp) screenTile.connectedUp = true;
		if (this.connectedDown) screenTile.connectedDown = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same up-down connections
		if ((this.connectedUp ^ screenTile.connectedUp) || (this.connectedDown ^ screenTile.connectedDown))
		{
			screenTile.connectedLeft = false;
			return false;
		}
		
		return true;
    }
    
    private void log(String msg)
    {
    	String connections = "";
    	if (this.connectedUp) connections = "U";
    	if (this.connectedDown) connections += "D";
    	if (this.connectedLeft) connections += "L";
    	if (this.connectedRight) connections += "R";
    	//System.out.println(msg + " at "+this.xCoord+","+this.zCoord+" "+connections);
    }
}
