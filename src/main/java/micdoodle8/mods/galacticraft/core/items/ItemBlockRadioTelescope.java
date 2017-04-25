package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ItemBlockRadioTelescope extends ItemBlockGC
{
    public ItemBlockRadioTelescope(Block block)
    {
        super(block);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState state)
    {
        int buildHeight = world.getHeight() - 1;
        int y = pos.getY();

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }


        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                BlockPos blockpos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                Block block = world.getBlockState(blockpos).getBlock();

                if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
                {
                    if (world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                    }
                    return false;
                }
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                BlockPos blockpos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                Block block = world.getBlockState(blockpos).getBlock();

                if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
                {
                    if (world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                    }
                    return false;
                }
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }
        for (int x = -3; x < 4; x++)
        {
            for (int z = -3; z < 4; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 6)
                {
                    continue;
                }

                BlockPos blockpos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                Block block = world.getBlockState(blockpos).getBlock();

                if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
                {
                    if (world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                    }
                    return false;
                }
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }
        for (int x = -3; x < 4; x++)
        {
            for (int z = -3; z < 4; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 6)
                {
                    continue;
                }

                BlockPos blockpos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                Block block = world.getBlockState(blockpos).getBlock();

                if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
                {
                    if (world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                    }
                    return false;
                }
            }
        }

        if (++y > buildHeight)
        {
            BlockPos blockpos = new BlockPos(pos.getX(), y, pos.getZ());
            Block block = world.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
            {
                if (world.isRemote)
                {
                    FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                }
                return false;
            }
        }
        for (int x = -2; x < 3; x++)
        {
            for (int z = -2; z < 3; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 4)
                {
                    continue;
                }

                BlockPos blockpos = new BlockPos(pos.getX() + x, y, pos.getZ() + z);
                Block block = world.getBlockState(blockpos).getBlock();

                if (block.getMaterial() != Material.air && !block.isReplaceable(world, blockpos))
                {
                    if (world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                    }
                    return false;
                }
            }
        }
        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
    }
}