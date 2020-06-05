//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.util.text.Style;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.client.FMLClientHandler;
//
//public class ItemBlockNasaWorkbench extends ItemBlockDesc
//{
//    public ItemBlockNasaWorkbench(Block block)
//    {
//        super(block);
//    }
//
//    @Override
//    public boolean placeBlockAt(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, BlockState state)
//    {
//        for (int x = -1; x < 2; x++)
//        {
//            for (int y = 0; y < 4; y++)
//            {
//                for (int z = -1; z < 2; z++)
//                {
//                    if (!(x == 0 && y == 0 && z == 0))
//                    {
//                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
//                        {
//                            BlockState stateAt = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
//
//                            if ((y == 0 || y == 3) && x == 0 && z == 0)
//                            {
//                                if (!stateAt.getMaterial().isReplaceable())
//                                {
//                                    if (world.isRemote)
//                                    {
//                                        Minecraft.getInstance().ingameGUI.setOverlayMessage(new StringTextComponent(GCCoreUtil.translate("gui.warning.noroom")).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText(), false);
//                                    }
//                                    return false;
//                                }
//                            }
//                            else if (y != 0 && y != 3)
//                            {
//                                if (!stateAt.getMaterial().isReplaceable())
//                                {
//                                    if (world.isRemote)
//                                    {
//                                        Minecraft.getInstance().ingameGUI.setOverlayMessage(new StringTextComponent(GCCoreUtil.translate("gui.warning.noroom")).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText(), false);
//                                    }
//                                    return false;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
//    }
//}