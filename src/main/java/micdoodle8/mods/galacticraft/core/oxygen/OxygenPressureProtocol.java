package micdoodle8.mods.galacticraft.core.oxygen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class OxygenPressureProtocol
{
    public final static Map<Block, ArrayList<Integer>> nonPermeableBlocks = new HashMap<Block, ArrayList<Integer>>();

    static
    {
        for (final String s : ConfigManagerCore.sealableIDs)
        {
            try
            {
            	BlockTuple bt = ConfigManagerCore.stringToBlock(s, "External Sealable IDs", true); 
            	if (bt == null) continue;

    			int meta = bt.meta;

                if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(bt.block))
                {
                    final ArrayList<Integer> list = OxygenPressureProtocol.nonPermeableBlocks.get(bt.block);
                    if (!list.contains(meta))
                    {
                        list.add(meta);
                    }
                    else
                    {
                        GCLog.info("[config] External Sealable IDs: skipping duplicate entry '" + s + "'.");
                    }
                }
                else
                {
                    final ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(meta);
                    OxygenPressureProtocol.nonPermeableBlocks.put(bt.block, list);
                }
            }
            catch (final Exception e)
            {
                GCLog.severe("[config] External Sealable IDs: error parsing '" + s + "'. Must be in the form Blockname or BlockName:metadata");
            }
        }
    }

    public static void updateSealerStatus(TileEntityOxygenSealer head)
    {
        try
        {
            head.threadSeal = new ThreadFindSeal(head);
        }
        catch (IllegalThreadStateException e)
        {

        }
    }

    public static void onEdgeBlockUpdated(World world, BlockVec3 vec)
    {
        if (ConfigManagerCore.enableSealerEdgeChecks)
        {
            TickHandlerServer.scheduleNewEdgeCheck(world.provider.dimensionId, vec);
        }
    }

    public static boolean canBlockPassAir(World world, Block block, BlockVec3 vec, int side)
    {
        //Check leaves first, because their isOpaqueCube() test depends on graphics settings
        //(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
        if (block instanceof BlockLeavesBase)
        {
            return true;
        }

        if (block.isOpaqueCube())
        {
            return block instanceof BlockGravel || block.getMaterial() == Material.cloth || block instanceof BlockSponge;

        }

        if (block instanceof BlockGlass || block instanceof BlockStainedGlass)
        {
            return false;
        }

        if (block instanceof IPartialSealableBlock)
        {
            return !((IPartialSealableBlock) block).isSealed(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side));
        }

        //Solid but non-opaque blocks, for example special glass
        if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
        {
            ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
            if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(vec.getBlockMetadata(world)))
            {
                return false;
            }
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof BlockSlab)
        {
            return !(side == 0 && (vec.getBlockMetadata(world) & 8) == 8 || side == 1 && (vec.getBlockMetadata(world) & 8) == 0);
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof BlockFarmland || block instanceof BlockEnchantmentTable || block instanceof BlockLiquid)
        {
            return side != 1;
        }

        if (block instanceof BlockPistonBase)
        {
            BlockPistonBase piston = (BlockPistonBase) block;
            int meta = vec.getBlockMetadata(world);
            if (BlockPistonBase.isExtended(meta))
            {
                int facing = BlockPistonBase.getPistonOrientation(meta);
                return side != facing;
            }
            return false;
        }

        //General case - this should cover any block which correctly implements isBlockSolidOnSide
        //including most modded blocks - Forge microblocks in particular is covered by this.
        // ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
        return !block.isSideSolid(world, vec.x, vec.y, vec.z, ForgeDirection.getOrientation(side ^ 1));
    }
}
