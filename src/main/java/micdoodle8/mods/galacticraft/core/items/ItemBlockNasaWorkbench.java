package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ItemBlockNasaWorkbench extends ItemBlockDesc
{
    public ItemBlockNasaWorkbench(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState state)
    {
        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            IBlockState stateAt = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));

                            if ((y == 0 || y == 3) && x == 0 && z == 0)
                            {
                                if (!stateAt.getMaterial().isReplaceable())
                                {
                                    if (world.isRemote)
                                    {
                                        FMLClientHandler.instance().getClient().ingameGUI.setOverlayMessage(new TextComponentString(GCCoreUtil.translate("gui.warning.noroom")).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText(), false);
                                    }
                                    return false;
                                }
                            }
                            else if (y != 0 && y != 3)
                            {
                                if (!stateAt.getMaterial().isReplaceable())
                                {
                                    if (world.isRemote)
                                    {
                                        FMLClientHandler.instance().getClient().ingameGUI.setOverlayMessage(new TextComponentString(GCCoreUtil.translate("gui.warning.noroom")).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText(), false);
                                    }
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
    }
}