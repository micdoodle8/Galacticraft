package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                if (bt == null)
                {
                    continue;
                }

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

    public static void onEdgeBlockUpdated(World world, BlockPos vec)
    {
        if (ConfigManagerCore.enableSealerEdgeChecks)
        {
            TickHandlerServer.scheduleNewEdgeCheck(GCCoreUtil.getDimensionID(world), vec);
        }
    }

    @Deprecated
    public static boolean canBlockPassAir(World world, Block block, BlockPos pos, Direction side)
    {
        return canBlockPassAir(world, world.getBlockState(pos), pos, side);
    }

    public static boolean canBlockPassAir(World world, BlockState state, BlockPos pos, Direction side)
    {
        Block block = state.getBlock();
        if (block == null)
        {
            return true;
        }

        if (block instanceof IPartialSealableBlock)
        {
            return !((IPartialSealableBlock) block).isSealed(world, pos, side);
        }

        //Check leaves first, because their isOpaqueCube() test depends on graphics settings
        //(See net.minecraft.block.BlockLeaves.isOpaqueCube()!)
        if (block instanceof LeavesBlock)
        {
            return true;
        }

        if (block.isOpaqueCube(state))
        {
            return block instanceof GravelBlock || block.getMaterial(state) == Material.CLOTH || block instanceof SpongeBlock;

        }

        if (block instanceof GlassBlock || block instanceof StainedGlassBlock)
        {
            return false;
        }

        //Solid but non-opaque blocks, for example special glass
        if (OxygenPressureProtocol.nonPermeableBlocks.containsKey(block))
        {
            ArrayList<Integer> metaList = OxygenPressureProtocol.nonPermeableBlocks.get(block);
            if (metaList.contains(Integer.valueOf(-1)) || metaList.contains(state.getBlock().getMetaFromState(state)))
            {
                return false;
            }
        }

        //Half slab seals on the top side or the bottom side according to its metadata
        if (block instanceof SlabBlock)
        {
            int meta = state.getBlock().getMetaFromState(state);
            return !(side == Direction.DOWN && (meta & 8) == 8 || side == Direction.UP && (meta & 8) == 0);
        }

        //Farmland etc only seals on the solid underside
        if (block instanceof FarmlandBlock || block instanceof EnchantingTableBlock || block instanceof BlockLiquid)
        {
            return side != Direction.UP;
        }

        if (block instanceof PistonBlock)
        {
            if (((Boolean) state.getValue(PistonBlock.EXTENDED)).booleanValue())
            {
                Direction facing = state.getValue(PistonBlock.FACING);
                return side != facing;
            }
            return false;
        }

        //General case - this should cover any block which correctly implements isBlockSolidOnSide
        //including most modded blocks - Forge microblocks in particular is covered by this.
        // ### Any exceptions in mods should implement the IPartialSealableBlock interface ###
        return !block.isSideSolid(state, world, pos, side.getOpposite());
    }
}
