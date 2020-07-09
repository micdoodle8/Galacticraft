//package micdoodle8.mods.galacticraft.planets.asteroids.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockAsteroidRock;
//import net.minecraft.block.Block;
//import net.minecraft.item.BlockItem;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockAsteroids extends BlockItem
//{
//    public ItemBlockAsteroids(Block block)
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
//        String name = BlockAsteroidRock.EnumBlockBasic.values()[itemstack.getDamage()].getName();
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
