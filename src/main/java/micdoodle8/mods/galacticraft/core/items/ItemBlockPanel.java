package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlockPanel extends ItemBlockGlassGC
{
    public ItemBlockPanel(Block block)
    {
        super(block);
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!player.isSneaking())
        {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isOpaqueCube() && !(state.getBlock() instanceof BlockPanelLighting))
        {
            if (world.isRemote)
            {
                BlockPanelLighting.updateClient(stack.getItemDamage(), state);
            }
            else
            {
                int meta = stack.getItemDamage();
                if (meta > 3) meta = 0;
                GCPlayerStats stats = GCPlayerStats.get(player);
                IBlockState[] panels = stats.getPanel_lighting();
                panels[meta] = state;
            }
        }

        return false;
    }
}
