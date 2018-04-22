//package micdoodle8.mods.galacticraft.planets.mars.client.nei;
//
//import codechicken.nei.api.IHighlightHandler;
//import codechicken.nei.api.ItemInfo;
//import codechicken.nei.guihook.GuiContainerManager;
//import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
//import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.World;
//
//import java.util.List;
//
//public class GCMarsNEIHighlightHandler implements IHighlightHandler
//{
//    @Override
//    public List<String> handleTextData(ItemStack stack, World world, EntityPlayer player, RayTraceResult mop, List<String> currenttip, ItemInfo.Layout layout)
//    {
//        String name = null;
//        try
//        {
//            String s = GuiContainerManager.itemDisplayNameShort(stack);
//            if (s != null && !s.endsWith("Unnamed"))
//            {
//                name = s;
//            }
//
//            if (name != null)
//            {
//                currenttip.add(name);
//            }
//        }
//        catch (Exception e)
//        {
//        }
//
//        if (stack.getItem() == Items.REDSTONE)
//        {
//            IBlockState state = world.getBlockState(mop.getBlockPos());
//            int md = state.getBlock().getMetaFromState(state);
//            String s = "" + md;
//            if (s.length() < 2)
//            {
//                s = " " + s;
//            }
//            currenttip.set(currenttip.size() - 1, name + " " + s);
//        }
//
//        return currenttip;
//    }
//
//    @Override
//    public ItemStack identifyHighlight(World world, EntityPlayer player, RayTraceResult mop)
//    {
//        BlockPos pos = mop.getBlockPos();
//        IBlockState state = world.getBlockState(pos);
//        Block b = state.getBlock();
//        int meta = b.getMetaFromState(state);
//        if (b == MarsBlocks.marsBlock)
//        {
//            if (meta == 2)
//            {
//                return new ItemStack(MarsBlocks.marsBlock, 1, 2);
//            }
//
//            if (meta == 9)
//            {
//                return new ItemStack(MarsBlocks.marsBlock, 1, 9);
//            }
//        }
//        else if (b == AsteroidBlocks.blockBasic)
//        {
//            if (meta == 4)
//            {
//                return new ItemStack(AsteroidBlocks.blockBasic, 1, 4);
//            }
//        }
//        return null;
//    }
//}
