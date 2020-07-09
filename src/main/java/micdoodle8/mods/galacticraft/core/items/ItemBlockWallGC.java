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
//public class ItemBlockWallGC extends BlockItem
//{
//    private static final String[] types = new String[] {
//            "tin",
//            "tin",
//            "moon",
//            "moon_bricks",
//            "mars",
//            "mars_bricks"
//    };
//
//    public ItemBlockWallGC(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    @Override
//    public int getMetadata(int meta)
//    {
//        return meta;
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
//        int meta = itemstack.getDamage();
//
//        if (meta < 0 || meta >= types.length)
//        {
//            meta = 0;
//        }
//        return super.getUnlocalizedName() + "." + types[meta];
//    }
//}