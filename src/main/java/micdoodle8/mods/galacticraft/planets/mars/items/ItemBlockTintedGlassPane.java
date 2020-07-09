//package micdoodle8.mods.galacticraft.planets.mars.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.*;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.DyeItem;
//import net.minecraft.item.Rarity;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockTintedGlassPane extends BlockItem
//{
//    public ItemBlockTintedGlassPane(Block block)
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
//    /*@Override
//    @OnlyIn(Dist.CLIENT)
//    public IIcon getIconFromDamage(int par1)
//    {
//        return this.getBlock().getIcon(0, par1);
//    }*/
//
//    @Override
//    public String getUnlocalizedName(ItemStack itemstack)
//    {
//        return this.getBlock().getUnlocalizedName() + "." + DyeItem.DYE_COLORS[~itemstack.getDamage() & 15];
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
