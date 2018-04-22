package micdoodle8.mods.galacticraft.core.tile;

import java.util.Arrays;
import java.util.List;

import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties.MachineSidesModel;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Used as a common interface for any TileEntity with configurable power, pipe, etc sides
 *
 */
public interface IMachineSides extends ITileClientUpdates
{
    /**
     * The different sides which a MachineSide (e.g. electric input)
     * can be set to.  These are relative to the front of the machine
     * (looking at the machine from the normal front). 
     */
    public enum Face
    {
        LEFT("l", 2, 1),
        RIGHT("r", 0, 2),
        REAR("b", 1, 0),
        TOP("u", 4, 4),
        BOTTOM("d", 3, 3),
        NOT_SET("n", 5, 5),
        NOT_CONFIGURABLE("fail", 6, 6);
        
        private final String name;
        private final int next;
        private final int prior;
        public static Face[] Horizontals = new Face[] { Face.LEFT, Face.REAR, Face.RIGHT };
        public static Face[] AllAvailable = new Face[] { Face.LEFT, Face.REAR, Face.RIGHT, Face.TOP, Face.BOTTOM };

        Face(String newname, int theNext, int thePrior)
        {
            this.name = newname;
            this.next = theNext;
            this.prior = thePrior;
        }
        
        public String getName()
        {
            return this.name;
        }

        public Face next()
        {
            return Face.values()[this.next];
        }

        public Face prior()
        {
            return Face.values()[this.prior];
        }
    }
    
    /*
     * All the different possible types of configurable side.
     * 
     * Each of these can correspond to a texture and a function
     * according to the block type.  Many may be unused.
     * 
     * this.listConfigurableSides() provides a list of which ones
     * the tile actually uses.
     */
    public enum MachineSide
    {
        FRONT("front"),
        REARDECO("reardeco"),
        PLAIN("plain"),
        TOP("top"),
        BASE("bottom"),
        ELECTRIC_IN("elec_in"),
        ELECTRIC_OUT("elec_out"),
        PIPE_IN("pipe_in"),
        PIPE_OUT("pipe_out"),
        CUSTOM("custom");
        
        private final String name;

        MachineSide(String newname)
        {
            this.name = newname;
        }
    }

    
    //--------------METHODS--------------
    /**
     * The front of this machine, with the graphic.
     * This CANNOT be set: use a wrench to change the block's facing to change it
     * (If player wants to change the front then rotate the block's facing and all other sides)
     */
    public EnumFacing getFront();

    /**
     * Used internally by tileEntity logic
     */
    public default EnumFacing getElectricInputDirection()
    {
        return null;
    }

    /**
     * Used internally by tileEntity logic
     */
    public default EnumFacing getElectricOutputDirection()
    {
        return null;
    }

    /**
     * Used internally by tileEntity logic
     */
    public default EnumFacing getPipeInputDirection()
    {
        return null;
    }

    /**
     * Used internally by tileEntity logic
     */
    public default EnumFacing getPipeOutputDirection()
    {
        return null;
    }

    /**
     * Used internally by tileEntity logic
     */
    public default EnumFacing getCustomDirection()
    {
        return null;
    }

    /*
     * Returns true if the setting succeeded
     * (Also returns true if it is an attempt to set it
     * to the same value as it is currently.)
     * 
     * Returns false if the MachineSide is not configurable.
     * 
     * Use isValidForSide() first to test whether the setting
     * is valid for the side here.
     */
    public default boolean setSide(MachineSide sideToSet, Face newSide)
    {
        if (this.getConfigurationType() == IMachineSidesProperties.NOT_CONFIGURABLE)
        {
            return false;
        }
        
        for (MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.test(sideToSet))
            {
                msp.set(newSide);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Like setSide() but using index for performance
     * - for internal use
     * 
     * index matches the index in getConfigurableSides()
     */
    public default boolean setSide(int index, Face newSide)
    {
        if (this.getConfigurationType() == IMachineSidesProperties.NOT_CONFIGURABLE)
        {
            return false;
        }
        
        MachineSidePack[] msps = this.getAllMachineSides();
        if (index >= 0 && index < msps.length)
        {
            msps[index].set(newSide);
            return true;
        }
        
        return false;
    }

    /**
     * Like getSide(MachineSide) but using index for performance
     * - for internal use
     * 
     * index matches the index in getConfigurableSides()
     */
    public default Face getSide(int index)
    {
        MachineSidePack[] msps = this.getAllMachineSides();
        if (index >= 0 && index < msps.length)
            return msps[index].get();

        return Face.NOT_CONFIGURABLE;
    }
    
    public default Face getSide(MachineSide sideToGet)
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.test(sideToGet))
            {
                return msp.get();
            }
        }
        return Face.NOT_CONFIGURABLE;
    }
    
    /*
     * Like getSide() but results limited to sides
     * which the blockstate is allowed to render
     * 
     * index matches the index in getConfigurableSides()
     */
    public default Face getAllowedSide(int index)
    {
        Face test = this.getSide(index);
        for (Face face : this.allowableFaces())
        {
            if (face == test)
                return test;
        }
        return this.listDefaultFaces()[index];
    }
    
    /**
     * Returns true if the machine side can be set to this face
     * 
     * By default, returns all allowable configurable faces
     * 
     * Overide this for special tile logic for specific sides
     */
    public default boolean isValidForSide(MachineSide sideToSet, Face newSide)
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.test(sideToSet))
            {
                return Arrays.asList(this.allowableFaces()).contains(newSide);
            }
        }
        
        //TODO: could maybe return true for default settings for this.getConfigurationType()
        //e.g. front == front, top == top, base == bottom
        return false;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide textureTypeLeft()
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.get() == Face.LEFT)
            {
                return msp.getRenderType();
            }
        }
        
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide textureTypeRight()
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.get() == Face.RIGHT)
            {
                return msp.getRenderType();
            }
        }
        
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide textureTypeRear()
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.get() == Face.REAR)
            {
                return msp.getRenderType();
            }
        }
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide textureTypeTop()
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.get() == Face.TOP)
            {
                return msp.getRenderType();
            }
        }
         return MachineSide.TOP;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide textureTypeBase()
    {
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            if (msp.get() == Face.BOTTOM)
            {
                return msp.getRenderType();
            }
        }
        
        return MachineSide.BASE;
    }

    /**
     * Essential for block rendering - use in getActualState()
     * Automatically returns a valid property as long as getConfigurationType() matches the property
     * (see BlockMachineTiered for an example)
     * 
     * Override this if you want more options.
     */
    public default IMachineSidesProperties.MachineSidesModel buildBlockStateProperty()
    {
        int length = listConfigurableSides().length;
        switch (length)
        {
        case 1:
            return IMachineSidesProperties.getModelForOneFace(this.getAllowedSide(0)).validFor(getConfigurationType());
        case 2:
        default:
            return IMachineSidesProperties.getModelForTwoFaces(this.getAllowedSide(0), this.getAllowedSide(1)).validFor(getConfigurationType());
        }
    }

    /**
     * Call this from a Block's getActualState() method
     *
     * @param state   The blockstate prior to addition of this property
     * @param tile    The tile entity matching the block position (null will return a default value)
     * @param renderType  The calling block's MACHINESIDES_RENDERTYPE
     * @param key     The name given to the machine sides property in the calling block e.g. SIDES
     * 
     * @return   A blockstate with the property added
     */
    public static IBlockState addPropertyForTile(IBlockState state, TileEntity tile, IMachineSidesProperties renderType, PropertyEnum<MachineSidesModel> key)
    {
        if (tile instanceof IMachineSides)
        {
            IMachineSides tileSides = (IMachineSides) tile;
            return state.withProperty(key, tileSides.buildBlockStateProperty());
        }
        else
            return state.withProperty(key, renderType.getDefault());
    }
    
    /**
     * For testing purposes - Sneak Wrench to activate this
     */
    public default void nextSideConfiguration(TileEntity te)
    {
        if (this.getConfigurationType() == IMachineSidesProperties.NOT_CONFIGURABLE)
        {
            return;
        }
        
        int length = listConfigurableSides().length;
        
        //TODO: adapt result of Face.next() and Face.prior() according to Horiz or All models
        switch (length)
        {
        case 1:
            this.setSide(0, this.getSide(0).next());
            break;
        case 2:
            Face leadingSide = this.getSide(0);
            if (this.getSide(1) != leadingSide.next())
            {
                this.setSide(1, leadingSide.prior());
                this.setSide(0, leadingSide.next());
            }
            else
            {
                this.setSide(1, leadingSide.prior());
            }
            break;
        default:
            //Override this for 3 or more configurable sides!
        }
        
        if (te.getWorld().isRemote)
        {
            te.getWorld().markBlockRangeForRenderUpdate(te.getPos(), te.getPos());
        }
        else
        {
            this.updateAllInDimension();
        }
    }

    /**
     * Array with all the configurable sides for this machine
     * (not including the front)
     *      
     * Any sides not included in this list are defaults:
     *   PLAIN for left, right and rear sides
     *   TOP for top
     *   BASE for bottom 
     */
    public MachineSide[] listConfigurableSides();

    /**
     * Array with the default faces to match entries in listConfigurableSides()
     */
    public Face[] listDefaultFaces();
    
    
    /**
     * All the different allowable faces which the configurable sides can be configured to
     * (for example it might be horizontal faces only, for this machine)
     */
    public default Face[] allowableFaces()
    {
        return this.getConfigurationType().allowableFaces();
    }
    
    /** 
     * The type of configuration (one or two sides to configure,
     * only horizontal settings or all allowed) - this needs to match
     * the blockstate model implementation, take it from the Block.
     * (Look at TileEnergyStorageModule for an example.)
     */
    public IMachineSidesProperties getConfigurationType();
    
    /**
     * Call this in getAllMachineSides() or else in the class constructor
     */
    public default void initialiseSides()
    {
        int length = listConfigurableSides().length;
        this.setupMachineSides(length);
        for (int i = 0; i < length; i++)
        {
            this.getAllMachineSides()[i] = new MachineSidePack(listConfigurableSides()[i], listDefaultFaces()[i]);
        }
    }

    /**
     * The array storing all configurable machine sides
     * and their current settings in this tile.
     * 
     * IMPORTANT: Run initialiseSides() in the first call to this
     * if it would otherwise be null - see examples in GC code
     */
    public MachineSidePack[] getAllMachineSides();

    /**
     * Create an array of MachineSidePack[length]
     * which can later be get using getAllMachineSides()
     */
    public void setupMachineSides(int length);
    
    public default void addMachineSidesToNBT(NBTTagCompound par1nbtTagCompound)
    {
        NBTTagList tagList = new NBTTagList();
        for(MachineSidePack msp : this.getAllMachineSides())
        {
            NBTTagCompound tag = new NBTTagCompound();
            msp.writeToNBT(tag);
            tagList.appendTag(tag);
        }
        par1nbtTagCompound.setTag("macsides", tagList);
    }


    public default void readMachineSidesFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        NBTTagList tagList = par1nbtTagCompound.getTagList("macsides", 10);
        MachineSidePack[] msps = this.getAllMachineSides();
        if (tagList.tagCount() == msps.length)
        {
            for (int i = 0; i < tagList.tagCount(); ++i)
            {
                msps[i].readFromNBT(tagList.getCompoundTagAt(i));
            }
        }
    }

    @Override
    public default void buildDataPacket(int[] data)
    {
        MachineSidePack[] msps = this.getAllMachineSides();
        for (int i = 0; i < msps.length; ++i)
        {
            data[i] = msps[i].get().ordinal();
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public default void updateClient(List<Object> data)
    {
        MachineSidePack[] msps = this.getAllMachineSides();
        for (int i = 0; i < msps.length; ++i)
        {
            msps[i].set((Integer) data.get(i + 1));
        }
        BlockPos pos = ((TileEntity)this).getPos();
        ((TileEntity)this).getWorld().markBlockRangeForRenderUpdate(pos, pos);
    }

    /**
     * A container object for Machine Sides settings
     * This should be implemented in an array by each TileEntity
     *
     */
    public class MachineSidePack
    {
        private MachineSide theSide;
        private Face currentFace;
        
        public MachineSidePack(MachineSide type, Face defaultFace)
        {
            assert(type != null);
            this.theSide = type;
            this.set(defaultFace);
        }
        
        public void writeToNBT(NBTTagCompound tag)
        {
            tag.setInteger("ts", this.theSide.ordinal());
            tag.setInteger("cf", this.currentFace.ordinal());
        }

        public void readFromNBT(NBTTagCompound tag)
        {
            int ts = -1;
            int cf = -1;
            if (tag.hasKey("ts"))
            {
                ts = tag.getInteger("ts");
            }
            if (tag.hasKey("cf"))
            {
                cf = tag.getInteger("cf");
            }
            if (ts == theSide.ordinal() && cf >= 0 && cf < Face.values().length)
            {
                this.currentFace = Face.values()[cf];
            }
        }

        public void set(Face face)
        {
            this.currentFace = (face == null) ? Face.NOT_SET : face;
        }

        public void set(int index)
        {
            if (index >= 0 && index < Face.values().length)
                this.currentFace = Face.values()[index];
        }

        public Face get()
        {
            return this.currentFace;
        }

        public MachineSide getRenderType()
        {
            return this.theSide;
        }

        public boolean test(MachineSide type)
        {
            return this.theSide == type;
        }
    }
}
