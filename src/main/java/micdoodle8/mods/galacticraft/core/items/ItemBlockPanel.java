package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockPanel extends ItemBlockDesc
{
    public ItemBlockPanel(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        String name = "";

        int meta = par1ItemStack.getItemDamage(); 
        if (meta >= BlockPanelLighting.PANELTYPES_LENGTH)
        {
            meta = 0;
        }

        return this.getBlock().getUnlocalizedName() + "_" + meta;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if (!player.isSneaking())
        {
            return EnumActionResult.PASS;
        }
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock().isOpaqueCube(state) && !(state.getBlock() instanceof BlockPanelLighting))
        {
        	ItemStack stack;
            if (hand == EnumHand.OFF_HAND)
            {
            	stack = player.inventory.offHandInventory.get(0);
            }
            else
            {
            	stack = player.inventory.getStackInSlot(player.inventory.currentItem);
            }
            if (stack.getItem() != this)
            {
            	return EnumActionResult.FAIL;
            }
            if (world.isRemote)
            {
                BlockPanelLighting.updateClient(stack.getItemDamage(), state);
            }
            else
            {
                int meta = stack.getItemDamage();
                if (meta >= BlockPanelLighting.PANELTYPES_LENGTH) meta = 0;
                GCPlayerStats stats = GCPlayerStats.get(player);
                IBlockState[] panels = stats.getPanelLightingBases();
                panels[meta] = state;
            }
        }

        return EnumActionResult.PASS;
    }
}
