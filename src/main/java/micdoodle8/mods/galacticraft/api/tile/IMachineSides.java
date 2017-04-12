package micdoodle8.mods.galacticraft.api.tile;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * Used as a common interface for any machine with power, pipe, etc sides
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
     * Returns true if the setting succeeded - or true
     * if it is an attempt to set to the same value as currently.
     * 
     * Returns false if the setting failed (e.g.
     * because the side was already taken by something else)
     */
    public default boolean setSideElectricInput(FaceRelative newSide)
    {
        return false;
    }

    /*
     * Returns true if the setting succeeded - or true
     * if it is an attempt to set to the same value as currently.
     * 
     * Returns false if the setting failed (e.g.
     * because the side was already taken by something else)
     */
    public default boolean setSideElectricOutput(FaceRelative newSide)
    {
        return false;
    }

    /*
     * Returns true if the setting succeeded - or true
     * if it is an attempt to set to the same value as currently.
     * 
     * Returns false if the setting failed (e.g.
     * because the side was already taken by something else)
     */
    public default boolean setSidePipeInput(FaceRelative newSide)
    {
        return false;
    }

    /*
     * Returns true if the setting succeeded - or true
     * if it is an attempt to set to the same value as currently.
     * 
     * Returns false if the setting failed (e.g.
     * because the side was already taken by something else)
     */
    public default boolean setSidePipeOutput(FaceRelative newSide)
    {
        return false;
    }

    /*
     * Returns true if the setting succeeded - or true
     * if it is an attempt to set to the same value as currently.
     * 
     * Returns false if the setting failed (e.g.
     * because the side was already taken by something else)
     */
    public default boolean setSideRearFace(FaceRelative newSide)
    {
        return false;
    }
    
    /**
     * The different sides which a MachineSide (e.g. electric input)
     * can be set to.  These are relative to the front of the machine
     * (looking at the machine from the normal front). 
     */
    public enum FaceRelative
    {
        LEFT("l"),
        RIGHT("r"),
        REAR("b"),
        TOP("u"),
        BOTTOM("d"),
        NOT_SET("n");
        
        private final String name;

        FaceRelative(String newname)
        {
            this.name = newname;
        }
    }
    
    /**
     * Only used by block rendering.
     */
    public enum RenderFacesOne implements IStringSerializable
    {
        LEFT("l"),
        RIGHT("r"),
        REAR("b"),
        TOP("u"),
        BOTTOM("d");
        
        private final String name;

        RenderFacesOne(String newname)
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
        LEFT3("lu"),
        LEFT4("ld"),
        RIGHT1("rl"),
        RIGHT2("rb"),
        RIGHT3("ru"),
        RIGHT4("rd"),
        REAR1("bl"),
        REAR2("br"),
        REAR3("bu"),
        REAR4("bd");
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
        public static RenderFacesTWO getByName(FaceRelative electricIn, FaceRelative electricOut)
        {
            String result = electricIn.name() + electricOut.name();
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
    public default MachineSide renderLeft()
    {
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide renderRight()
    {
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide renderRear()
    {
        return MachineSide.PLAIN;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide renderTop()
    {
        return MachineSide.TOP;
    }

    /**
     * Can be used for rendering this machine in a GUI
     */
    public default MachineSide renderBase()
    {
        return MachineSide.BASE;
    }

    /**
     * Essential for block rendering - use in tiles
     * where there is only ONE configurable side
     */
    public default RenderFacesOne renderOne()
    {
        return null;
    }
    
    /**
     * Essential for block rendering - use in tiles
     * where there are TWO configurable sides
     */
    public default RenderFacesTWO renderTwo()
    {
        return null;
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
}
