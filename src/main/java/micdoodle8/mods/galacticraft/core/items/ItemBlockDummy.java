//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockDummy extends BlockItem
//{
//    public ItemBlockDummy(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
////    @Override
////    @OnlyIn(Dist.CLIENT)
////    public IIcon getIconFromDamage(int par1)
////    {
////        return this.getBlock().getIcon(0, par1);
////    }
//
//    @Override
//    public int getMetadata(int damage)
//    {
//        return damage;
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
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        int metadata = itemstack.getDamage();
//        String blockName = "";
//
//        switch (metadata)
//        {
//        case 1:
//            blockName = "spaceStationBase";
//            break;
//        case 2:
//            blockName = "launchPad";
//            break;
//        case 3:
//            blockName = "nasaWorkbench";
//            break;
//        case 4:
//            blockName = "solar";
//            break;
//        case 5:
//            blockName = "cryogenicChamber";
//            break;
//        default:
//            blockName = null;
//            break;
//        }
//
//        return this.getBlock().getUnlocalizedName() + "." + blockName;
//    }
//
//    @Override
//    public String getUnlocalizedName()
//    {
//        return this.getBlock().getUnlocalizedName() + ".0";
//    }
//}
