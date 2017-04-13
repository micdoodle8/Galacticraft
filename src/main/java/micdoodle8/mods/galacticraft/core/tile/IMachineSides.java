package micdoodle8.mods.galacticraft.core.tile;

import java.util.Arrays;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * Used as a common interface for any TileEntity with configurable power, pipe, etc sides
 *
 */
public interface IMachineSides
{
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

    public default boolean setSide(int index, Face newSide)
    {
        MachineSidePack[] msps = this.getAllMachineSides();
        if (index >= 0 && index < msps.length)
        {
            msps[index].set(newSide);
            return true;
        }
        
        return false;
    }

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
     */
    public default Face getAllowedSide(int index)
    {
        Face test = this.getSide(index);
        for (Face face : this.allowableFaces())
        {
            if (face == test)
                return test;
        }
        return this.allowableFaces()[0];
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
        return false;
    }
    
    public default Face[] allowableFaces()
    {
        return Face.Horizontals;
    }

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
    
    /**
     * Only used by block rendering.
     */
    public enum RenderFacesAll implements IStringSerializable
    {
        LEFT("l"),
        RIGHT("r"),
        REAR("b"),
        TOP("u"),
        BOTTOM("d");
        
        private final String name;

        RenderFacesAll(String newname)
        {
            this.name = newname;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
    
    /**
     * Only used by block rendering.
     */
    public enum RenderFacesHoriz implements IStringSerializable
    {
        LEFT("l"),
        RIGHT("r"),
        REAR("b");
        
        private final String name;

        RenderFacesHoriz(String newname)
        {
            this.name = newname;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
    
    /**
     * Only used by block rendering.
     * This will add a lot of extra block models, do not use unless you have to
     */
    public enum RenderFacesTWO implements IStringSerializable
    {
        LEFT1("lr"),
        LEFT2("lb"),
//        LEFT3("lu"),
//        LEFT4("ld"),
        RIGHT1("rl"),
        RIGHT2("rb"),
//        RIGHT3("ru"),
//        RIGHT4("rd"),
        REAR1("bl"),
        REAR2("br");
 //       REAR3("bu"),
 //       REAR4("bd");
        //Temporarily exclude these, otherwise crazy numbers of blockstates for Energy Storage Module
//        TOP1("ul"),
//        TOP2("ur"),
//        TOP3("ub"),
//        TOP4("ud"),
//        BOTTOM1("dl"),
//        BOTTOM2("dr"),
//        BOTTOM3("db"),
//        BOTTOM4("du");
        
        private final String name;

        RenderFacesTWO(String newname)
        {
            this.name = newname;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        /**
         * The inputs aren't necessarily electricIn and electricOut, these
         * are only examples - as used for the Energy Storage Module!
         * 
         * It could also be electricIn and pipeIn for example - whatever
         * is needed according to the block model
         */
        public static IStringSerializable getByName(Face electricIn, Face electricOut)
        {
            String result = electricIn.getName() + electricOut.getName();
            for (RenderFacesTWO test : RenderFacesTWO.values())
            {
                if (result.equals(test.name)) return test;
            }
            return RenderFacesTWO.RIGHT1;
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
     * Usage: Cast this to RenderFacesHoriz or RenderFacesTWO
     * according to the size of this block's listConfigurableSides()
     * 
     * Override this if you want more options.
     */
    public default IStringSerializable buildBlockStateProperty()
    {
        int length = listConfigurableSides().length;
        switch (length)
        {
        case 1:
            return RenderFacesHoriz.values()[this.getAllowedSide(0).ordinal()];
        case 2:
        default:
            return RenderFacesTWO.getByName(this.getAllowedSide(0), this.getAllowedSide(1));
        }
    }

    /**
     * For testing purposes - Sneak Wrench to activate this
     */
    public default void nextSideConfiguration(TileEntity te)
    {
        int length = listConfigurableSides().length;
        
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
        
        if (te.getWorld().isRemote) te.getWorld().markBlockForUpdate(te.getPos());
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
    
    /**
     * This can be an empty method.  (It will never be called!)
     */
    public void validate_RememberToSendClientUpdateRequest();
    
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

    public default void sendUpdateToClient(EntityPlayerMP player)
    {
        int[] data = new int[4];
        MachineSidePack[] msps = this.getAllMachineSides();
        for (int i = 0; i < msps.length; ++i)
        {
            data[i] = msps[i].get().ordinal();
        }
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_SIDES, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { ((TileEntity)this).getPos(), data[0], data[1], data[2], data[3] }), player);
    }

    public default void updateClient(List<Object> data)
    {
        MachineSidePack[] msps = this.getAllMachineSides();
        for (int i = 0; i < msps.length; ++i)
        {
            msps[i].set((Integer) data.get(i + 1));
        }
    }

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
