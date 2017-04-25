package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSolar extends ItemBlockDesc
{
    public ItemBlockSolar(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public boolean placeBlockAt(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, IBlockState state)
    {
        for (int y = 1; y <= 2; y++)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    BlockPos posAt = pos.add(y == 2 ? x : 0, y, y == 2 ? z : 0);
                    Block block = world.getBlockState(posAt).getBlock();

                    if (block.getMaterial() != Material.air && !block.isReplaceable(world, posAt))
                    {
                        if (world.isRemote)
                        {
                            FMLClientHandler.instance().getClient().ingameGUI.setRecordPlaying(new ChatComponentText(GCCoreUtil.translate("gui.warning.noroom")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)).getFormattedText(), false);
                        }
                        return false;
                    }
                }
            }
        }

        for (int x = -2; x <= 2; x++)
        {
            for (int z = -2; z <= 2; z++)
            {
                BlockPos posAt = pos.add(x, 0, z);
                Block block = world.getBlockState(posAt).getBlock();

                if (block == GCBlocks.solarPanel)
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

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int index = Math.min(Math.max(par1ItemStack.getItemDamage() / 4, 0), BlockSolar.EnumSolarType.values().length);

        String name = BlockSolar.EnumSolarType.values()[index].getName();

        return this.getBlock().getUnlocalizedName() + "." + name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}
