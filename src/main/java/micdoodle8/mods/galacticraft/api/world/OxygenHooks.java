package micdoodle8.mods.galacticraft.api.world;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

import java.lang.reflect.Method;

public class OxygenHooks
{
    private static Class<?> oxygenUtilClass;
    private static Method combusionTestMethod;
    private static Method breathableAirBlockMethod;
    private static Method breathableAirBlockEntityMethod;
    private static Method torchHasOxygenMethod;
    private static Method oxygenBubbleMethod;
    private static Method validOxygenSetupMethod;

    /**
     * Test whether fire can burn in this world's atmosphere (outside any oxygen bubble).
     * 
     * @param provider   The WorldProvider for this dimension
     * 
     * @return   False if fire burns normally
     *            True if fire cannot burn in this world
     *            
     */
	public static boolean noAtmosphericCombustion(WorldProvider provider)
    {
        try
        {
            if (combusionTestMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                combusionTestMethod = oxygenUtilClass.getDeclaredMethod("noAtmosphericCombustion", WorldProvider.class);
            }
            return (Boolean)combusionTestMethod.invoke(null, provider);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    
	/**
	 * Test whether a bounding box (normally a block but it could be an entity)
	 * is inside an oxygen bubble or oxygen sealed space, on an otherwise
	 * oxygen-free world.  (Do not use this on the Overworld or other oxygen-rich
	 * world,, it will return false negatives!!)
	 * 
	 * NOTE: In a complex build where this block is surrounded by air-permeable blocks
	 * on all sides (for example torches, ladders, signs, wires, chests etc etc)
	 * then it may have to look quite far to find whether it is in oxygen or not - 
	 * it will check up to 5 blocks in each direction.  This can impose a performance
	 * load in the unlikely event there are permeable blocks in all directions.  It is
	 * therefore advisable not to call this every tick: 1 tick in 5 should be plenty.
	 * 
	 * @param world		The World
	 * @param bb		AxisAligned BB representing the block (e.g. a torch), or maybe the side of a block
	 * @return			True if the bb is in oxygen, otherwise false.
	 */
	public static boolean isAABBInBreathableAirBlock(World world, AxisAlignedBB bb)
    {
        try
        {
            if (breathableAirBlockMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                breathableAirBlockMethod = oxygenUtilClass.getDeclaredMethod("isAABBInBreathableAirBlock", World.class, AxisAlignedBB.class);
            }
            return (Boolean)breathableAirBlockMethod.invoke(null, world, bb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
	
   
    /**
     * Special version of the oxygen AABB check for living entities.
     * This is based on checking the oxygen contact of a small box centred
     * at the entity's eye height.  The small box has sides equal to half
     * the width of the entity.  This is a good approximation to head size and
     * position for most types of mobs. 
     * 
     * @param entity
     * @return  True if the entity's head is in an oxygen bubble or block, false otherwise 
     */
	public static boolean isAABBInBreathableAirBlock(EntityLivingBase entity)
    {
        try
        {
            if (breathableAirBlockEntityMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                breathableAirBlockEntityMethod = oxygenUtilClass.getDeclaredMethod("isAABBInBreathableAirBlock", EntityLivingBase.class);
            }
            return (Boolean)breathableAirBlockEntityMethod.invoke(null, entity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
    
    
	/**
	 * Simplified (better performance) version of the block oxygen check
	 * for use with torch blocks and other oxygen-requiring blocks
	 * which can access oxygen on any side.
	 * 
	 * NOTE:  this does not run an inOxygenBubble() check, you will need to do
	 * that also.
	 * 
	 * @param world
	 * @param block		The block type of this torch being checked - currently unused
	 * @param x			x coordinate of the torch
	 * @param y			y coordinate of the torch
	 * @param z			z coordinate of the torch
	 * @return	True if there is a (sealed) oxygen block accessible, otherwise false.
	 */
    public static boolean checkTorchHasOxygen(World world, Block block, int x, int y, int z)
    {
        try
        {
            if (torchHasOxygenMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                torchHasOxygenMethod = oxygenUtilClass.getDeclaredMethod("isAABBInBreathableAirBlock", World.class, Block.class, int.class, int.class, int.class);
            }
            return (Boolean)torchHasOxygenMethod.invoke(null, world, block, x, y, z);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

	
    /**
     * Test whether a location is inside an Oxygen Bubble from an Oxygen Distributor
     * 
     * @param worldObj	World
     * @param avgX		avg X, avgY, avgZ are the average co-ordinates of the location
     * @param avgY		(for example, the central point of a block being tested
     * @param avgZ		or the average position of the centre of a living entity)
     * @return  True if it is in an oxygen bubble, otherwise false
     */
    public static boolean inOxygenBubble(World worldObj, double avgX, double avgY, double avgZ)
	{
        try
        {
            if (oxygenBubbleMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                oxygenBubbleMethod = oxygenUtilClass.getDeclaredMethod("inOxygenBubble", World.class, double.class, double.class, double.class);
            }
            return (Boolean)oxygenBubbleMethod.invoke(null, worldObj, avgX, avgY, avgZ);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
	}

    /**
     * Test whether a player is wearing a working set of Galacticraft
     * oxygen-breathing apparatus (mask, gear + tank)
     * 
     * @param player
     * @return True if the setup is valid, otherwise false
     */
    public static boolean hasValidOxygenSetup(EntityPlayerMP player)
    {
        try
        {
            if (validOxygenSetupMethod == null)
            {
                if (oxygenUtilClass == null)
                {
                    oxygenUtilClass = Class.forName("micdoodle8.mods.galacticraft.core.util.OxygenUtil");
                }
                validOxygenSetupMethod = oxygenUtilClass.getDeclaredMethod("hasValidOxygenSetup", EntityPlayerMP.class);
            }
            return (Boolean)validOxygenSetupMethod.invoke(null, player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
