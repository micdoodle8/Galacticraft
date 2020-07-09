//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockSolar extends ItemBlockDesc
//{
//    public ItemBlockSolar(Block block)
//    {
//        super(block);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//    }
//
//    @Override
//    public String getUnlocalizedName(ItemStack par1ItemStack)
//    {
//        int index = Math.min(Math.max(par1ItemStack.getDamage() / 4, 0), BlockSolar.EnumSolarType.values().length);
//
//        String name = BlockSolar.EnumSolarType.values()[index].getName();
//
//        return this.getBlock().getUnlocalizedName() + "." + name;
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
//    public int getMetadata(int damage)
//    {
//        return damage;
//    }
//}
