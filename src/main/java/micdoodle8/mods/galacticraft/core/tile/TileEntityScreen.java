package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

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
    public static int maxTypes = 4;
	public DrawGameScreen screen;
	public boolean connectedUp;
	public boolean connectedDown;
	public boolean connectedLeft;
	public boolean connectedRight;
	public int connectionsUp;
	public int connectionsDown;
	public int connectionsLeft;
	public int connectionsRight;

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
		this.refreshConnections(false);
		this.markDirty();
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
		BlockVec3 vec = new BlockVec3(this);
		super.invalidate();
		TileEntity tile;

		boolean doUp = this.connectedUp;
		boolean doDown = this.connectedDown;
		boolean doLeft = this.connectedLeft;
		boolean doRight = this.connectedRight;

		this.connectedUp = this.connectedDown = this.connectedLeft = this.connectedRight = false;

		if (doUp)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 1);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).connectedDown = false;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doDown)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 0);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).connectedUp = false;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doLeft)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getLeft(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).connectedRight = false;
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (doRight)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getRight(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).connectedLeft = false;
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
		int meta = this.getBlockMetadata() & 7;
		if (meta < 2)
		{
			System.out.println("Up/down oriented screens cannot be multiscreen");
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
			if (!(tileUp instanceof TileEntityScreen && tileUp.getBlockMetadata() == meta))
			{
				this.connectedUp = false;
				this.markDirty();
			}
		}

		if (this.connectedDown)
		{
			tileDown = vec.getTileEntityOnSide(this.worldObj, 0);
			if (!(tileDown instanceof TileEntityScreen && tileDown.getBlockMetadata() == meta))
			{
				this.connectedDown = false;
				this.markDirty();
			}
		}

		if (this.connectedLeft)
		{
			int side = this.getLeft(meta);
			tileLeft = vec.getTileEntityOnSide(this.worldObj, side);
			if (!(tileLeft instanceof TileEntityScreen && tileLeft.getBlockMetadata() == meta))
			{
				this.connectedLeft = false;
				this.markDirty();
			}
		}

		if (this.connectedRight)
		{
			int side = this.getRight(meta);
			tileRight = vec.getTileEntityOnSide(this.worldObj, side);
			if (!(tileRight instanceof TileEntityScreen && tileRight.getBlockMetadata() == meta))
			{
				this.connectedRight = false;
				this.markDirty();
			}
		}

		//Now test whether a connection can be sustained with that other tile
		if (this.connectedUp)
		{
    		if (!this.tryConnectUp((TileEntityScreen)tileUp))
			{
				this.connectedUp = false;
				this.markDirty();
			}
		}

		if (this.connectedDown)
		{
			if (!this.tryConnectDown((TileEntityScreen)tileDown))
			{
				this.connectedDown = false;
				this.markDirty();
			}
		}

		if (this.connectedLeft)
		{
			if (!this.tryConnectLeft((TileEntityScreen)tileLeft))
			{
				this.connectedLeft = false;
				this.markDirty();
			}
		}

		if (this.connectedRight)
		{
			if (!this.tryConnectRight((TileEntityScreen)tileRight))
			{
				this.connectedRight = false;
				this.markDirty();
			}
		}

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
			
			this.updateClients();
		}
	}
	
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.imageType = nbt.getInteger("type");
        this.connectedDown = nbt.getBoolean("connectDown");
        this.connectedUp = nbt.getBoolean("connectUp");
        this.connectedLeft = nbt.getBoolean("connectLeft");
        this.connectedRight = nbt.getBoolean("connectRight");
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("type", this.imageType);
        nbt.setBoolean("connectDown", this.connectedDown);
        nbt.setBoolean("connectUp", this.connectedUp);
        nbt.setBoolean("connectLeft", this.connectedLeft);
        nbt.setBoolean("connectRight", this.connectedRight);
    }
    
    //TODO prevent 3x1 or longer sizes 
    public void checkScreenSize()
    {
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
    	while (left < 5)
    	{
    		if (tile.connectedLeft)
    		{
    			left++;
    			int side = this.getLeft(meta);
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, side);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec = vec.newVecSide(side);
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
    	while (right < 5)
    	{
    		if (tile.connectedRight)
    		{
    			right++;
    			int side = this.getRight(meta);
    			TileEntity newTile = vec.getTileEntityOnSide(this.worldObj, side);
    			if (newTile instanceof TileEntityScreen)
    			{
    				tile = (TileEntityScreen) newTile;
    				vec = vec.newVecSide(side);
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
    	
    	//TODO check total dimensions do not exceed 5 x 5
    	
    	if (this.checkWholeScreen(up, down, left, right))
    	{
	    	this.connectionsUp = up;
	    	this.connectionsDown = down;
	    	this.connectionsLeft = left;
	    	this.connectionsRight = right;
    	}
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
		int meta = this.getBlockMetadata() & 7;
    	BlockVec3 vec = new BlockVec3(this);
    	ArrayList<TileEntityScreen> screenList = new ArrayList<TileEntityScreen>();

		int side = this.getRight(meta);
		
		DrawGameScreen newScreen = null;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			newScreen = new DrawGameScreen(1.0F + left + right, 1.0F + up + down);
		}
		
    	for (int x = -left; x <= right; x++)
    	{
    		for (int z = -up; z <= down; z++)
    		{
    			BlockVec3 newVec = vec.clone().modifyPositionFromSide(ForgeDirection.getOrientation(side), x).modifyPositionFromSide(ForgeDirection.DOWN, z);
    			TileEntity tile = newVec.getTileEntity(this.worldObj);
    			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
    			{
    				TileEntityScreen screenTile = (TileEntityScreen)tile;
    				screenList.add(screenTile);
    				screenTile.screenOffsetx = x + left;
    				screenTile.screenOffsetz = z + up;
   					screenTile.screen = newScreen;
   					screenTile.imageType = this.imageType;
    			}
    			else
    			{
    				screenWhole = false;
    			}
    		}
    	}
    	
    	if (screenWhole)
    	{
        	for (TileEntityScreen scr : screenList)
       			scr.refreshConnections(false);
    		return true;
    	}

    	for (TileEntityScreen scr : screenList)
   			scr.resetToSingle();
    	
    	return false;
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
		
		screenTile.markDirty();
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
		
		screenTile.markDirty();		
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
		
		screenTile.markDirty();
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
		
		screenTile.markDirty();
		return true;
    }
}
