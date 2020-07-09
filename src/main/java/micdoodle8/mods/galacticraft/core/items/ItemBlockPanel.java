//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ActionResultType;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class ItemBlockPanel extends ItemBlockDesc
//{
//    public ItemBlockPanel(Block block)
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
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        String name = "";
//
//        int meta = par1ItemStack.getDamage();
//        if (meta >= BlockPanelLighting.PANELTYPES_LENGTH)
//        {
//            meta = 0;
//        }
//
//        return this.getBlock().getUnlocalizedName() + "_" + meta;
//    }
//
//    @Override
//    public ActionResultType onItemUseFirst(PlayerEntity player, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, Hand hand)
//    {
//        if (!player.isSneaking())
//        {
//            return ActionResultType.PASS;
//        }
//        BlockState state = world.getBlockState(pos);
//        if (state.getBlock().isOpaqueCube(state) && !(state.getBlock() instanceof BlockPanelLighting))
//        {
//        	ItemStack stack;
//            if (hand == Hand.OFF_HAND)
//            {
//            	stack = player.inventory.offHandInventory.get(0);
//            }
//            else
//            {
//            	stack = player.inventory.getStackInSlot(player.inventory.currentItem);
//            }
//            if (stack.getItem() != this)
//            {
//            	return ActionResultType.FAIL;
//            }
//            if (world.isRemote)
//            {
//                BlockPanelLighting.updateClient(stack.getDamage(), state);
//            }
//            else
//            {
//                int meta = stack.getDamage();
//                if (meta >= BlockPanelLighting.PANELTYPES_LENGTH) meta = 0;
//                GCPlayerStats stats = GCPlayerStats.get(player);
//                BlockState[] panels = stats.getPanelLightingBases();
//                panels[meta] = state;
//            }
//        }
//
//        return ActionResultType.PASS;
//    }
//}
