package micdoodle8.mods.galacticraft.core.tile;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLCommonHandler;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.client.gui.screen.InGameScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityScreen extends TileEntity
{
    public int imageType = 0;
    public int maxTypes = 4;
	public InGameScreen screen;
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
	
	public TileEntityScreen()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			this.screen = new InGameScreen(1.0F, 1.0F);
	}

	@Override
	public void invalidate()
	{
		int meta = this.getBlockMetadata() & 7;
		BlockVec3 vec = new BlockVec3(this);
		super.invalidate();
		TileEntity tile;

		if (this.connectedUp)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 1);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (this.connectedDown)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 0);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (this.connectedLeft)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getLeft(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
		if (this.connectedRight)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, this.getRight(meta));
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
				((TileEntityScreen)tile).refreshConnections(true);
			}			
		}
	}
	
	public void refreshConnections(boolean doScreen)
	{
		int meta = this.getBlockMetadata() & 7;
		if (meta < 2)
		{
			System.out.println("Up/down oriented screens cannot be multiscreen");
			this.resetToSingle();
			return;
		}
		
		TileEntity tile;
		BlockVec3 vec = new BlockVec3(this);
		
		if (this.connectedUp)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 1);
			//First, check the neighbour is in the same orientation 
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
	    		if (!this.tryConnectUp((TileEntityScreen)tile))
					this.connectedUp = false;
			}
			else
				this.connectedUp = false;
		}

		if (this.connectedDown)
		{
			tile = vec.getTileEntityOnSide(this.worldObj, 0);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
	    		if (!this.tryConnectDown((TileEntityScreen)tile))
					this.connectedDown = false;
			}
			else
				this.connectedDown = false;
		}

		if (this.connectedLeft)
		{
			int side = this.getLeft(meta);
			tile = vec.getTileEntityOnSide(this.worldObj, side);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
	    		if (!this.tryConnectLeft((TileEntityScreen)tile))
					this.connectedLeft = false;
			}
			else
				this.connectedLeft = false;
		}

		if (this.connectedRight)
		{
			int side = this.getRight(meta);
			tile = vec.getTileEntityOnSide(this.worldObj, side);
			if (tile instanceof TileEntityScreen && tile.getBlockMetadata() == meta)
			{
	    		if (!this.tryConnectRight((TileEntityScreen)tile))
					this.connectedRight = false;
			}
			else
				this.connectedRight = false;
		}

		if (doScreen)
			this.checkScreenSize();
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

		//Temporary code for test purposes - always try to connect
		this.connectedUp = this.connectedDown = this.connectedLeft = this.connectedRight = true;
		this.refreshConnections(true);
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
    
    //TODO prevent 3x1 or longer 
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
    
    private boolean checkWholeScreen(int up, int down, int left, int right)
    {
    	if (up + down + left + right == 0)
    	{
    		this.resetToSingle();
    		return true;
    	}

    	System.out.println("Checking screen size at "+this.xCoord+","+this.zCoord+": Up "+up+" Dn "+down+" Lf "+left+" Rg "+right);

    	boolean screenWhole = true;
		int meta = this.getBlockMetadata() & 7;
    	BlockVec3 vec = new BlockVec3(this);
    	ArrayList<TileEntityScreen> screenList = new ArrayList<TileEntityScreen>();

		int side = this.getRight(meta);
		
		InGameScreen newScreen = null;
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			newScreen = new InGameScreen(1.0F + left + right, 1.0F + up + down);
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
    
    public void resetToSingle()
    {
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			this.screen = new InGameScreen(1.0F, 1.0F);
		this.screenOffsetx = 0;
		this.screenOffsetz = 0;   		
    	this.connectionsUp = 0;
    	this.connectionsDown = 0;
    	this.connectionsLeft = 0;
    	this.connectionsRight = 0;
    	this.connectedDown = this.connectedLeft = this.connectedRight = this.connectedUp = false;
    }
    
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

		this.connectedUp = true;
		screenTile.connectedDown = true;
		if (this.connectedLeft) screenTile.connectedLeft = true;
		if (this.connectedRight) screenTile.connectedRight = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same left-right connections
		if ((this.connectedLeft && !screenTile.connectedLeft) || (this.connectedLeft && !screenTile.connectedLeft))
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

		this.connectedDown = true;
		screenTile.connectedUp = true;
		if (this.connectedLeft) screenTile.connectedLeft = true;
		if (this.connectedRight) screenTile.connectedRight = true;
		screenTile.refreshConnections(false);
		//Undo if the neighbour could not maintain the same left-right connections
		if ((this.connectedLeft && !screenTile.connectedLeft) || (this.connectedLeft && !screenTile.connectedLeft))
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
		if ((this.connectedUp && !screenTile.connectedUp) || (this.connectedDown && !screenTile.connectedDown))
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
		if ((this.connectedUp && !screenTile.connectedUp) || (this.connectedDown && !screenTile.connectedDown))
		{
			screenTile.connectedLeft = false;
			return false;
		}
		
		return true;
    }
}
