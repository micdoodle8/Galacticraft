package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ItemBlockSpaceStationBase extends ItemBlockGC
{
    public ItemBlockSpaceStationBase(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState state)
    {
        for (int y = 0; y < 3; y++)
        {
            Block blockAt = world.getBlockState(new BlockPos(pos.getX(), pos.getY() + y, pos.getZ())).getBlock();

            if (!blockAt.getMaterial().isReplaceable())
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }
        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
    }
}