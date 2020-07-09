//package micdoodle8.mods.galacticraft.planets.venus.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockVenusRock;
//import net.minecraft.block.Block;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockBasicVenus extends BlockItem
//{
//    public ItemBlockBasicVenus(Block block)
//    {
//        super(block);
////        this.setMaxDamage(0);
////        this.setHasSubtypes(true);
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
//        String name;
//
//        switch (itemstack.getDamage())
//        {
//        default:
//            name = BlockVenusRock.EnumBlockBasicVenus.values()[itemstack.getDamage()].getName();
//        }
//
//        return this.getBlock().getUnlocalizedName() + "." + name;
//    }
//
//    @Override
//    public String getUnlocalizedName()
//    {
//        return this.getBlock().getUnlocalizedName() + ".0";
//    }
//}
