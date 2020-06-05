//package micdoodle8.mods.galacticraft.core.items;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class ItemBlockGlassGC extends ItemBlockDesc
//{
//    public ItemBlockGlassGC(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    @Override
//    public int getMetadata(int damage)
//    {
//        return damage;
//    }
//
//    @Override
//    public boolean placeBlockAt(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, BlockState newState)
//    {
//        if (!world.setBlockState(pos, newState, 3)) return false;
//
//        BlockState state = world.getBlockState(pos);
//        if (state.getBlock().getClass() == this.block.getClass())
//        {
//            setTileEntityNBT(world, player, pos, stack);
//            this.block.onBlockPlacedBy(world, pos, state, player, stack);
//        }
//
//        return true;
//    }
//}
