//package micdoodle8.mods.galacticraft.planets.mars.items;
//
//import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
//import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
//import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.Style;
//import net.minecraft.util.text.StringTextComponent;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockMachine extends ItemBlockDesc
//{
//    public ItemBlockMachine(Block block)
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
//    public boolean placeBlockAt(ItemStack itemStack, PlayerEntity player, World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, BlockState state)
//    {
//        int metaAt = itemStack.getDamage();
//
//        //If it is a Cryogenic Chamber, check the space
//        if (metaAt == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
//        {
//            for (int y = 0; y < 3; y++)
//            {
//                BlockState stateAt = world.getBlockState(pos.add(0, y, 0));
//
//                if (this.getBlock() == MarsBlocks.machine)
//                {
//                    if (!stateAt.getMaterial().isReplaceable())
//                    {
//                        if (world.isRemote)
//                        {
//                            Minecraft.getInstance().ingameGUI.setOverlayMessage(new StringTextComponent(GCCoreUtil.translate("gui.warning.noroom")).setStyle(new Style().setColor(TextFormatting.RED)).getFormattedText(), false);
//                        }
//                        return false;
//                    }
//                }
//            }
//        }
//        return super.placeBlockAt(itemStack, player, world, pos, facing, hitX, hitY, hitZ, state);
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        int index = 0;
//        int typenum = itemstack.getDamage() & 12;
//
//        if (this.getBlock() == MarsBlocks.machine)
//        {
//            if (typenum == BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
//            {
//                index = 2;
//            }
//            else if (typenum == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
//            {
//                index = 1;
//            }
//        }
//        else if (this.getBlock() == MarsBlocks.machineT2)
//        {
//            return ((BlockMachineBase) MarsBlocks.machineT2).getUnlocalizedName(typenum);
//        }
//
//        return this.getBlock().getUnlocalizedName() + "." + index;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public Rarity getRarity(ItemStack par1ItemStack)
//    {
//        return ClientProxyCore.galacticraftItem;
//    }
//
//    @Override
//    public String getUnlocalizedName()
//    {
//        return this.getBlock().getUnlocalizedName() + ".0";
//    }
//}
