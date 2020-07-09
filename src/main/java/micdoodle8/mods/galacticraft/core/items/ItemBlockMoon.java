//package micdoodle8.mods.galacticraft.core.items;
//
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.Block;
//import net.minecraft.item.Rarity;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//public class ItemBlockMoon extends ItemBlockDesc
//{
//    public ItemBlockMoon(Block block)
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
//        String name;
//
//        switch (itemstack.getDamage())
//        {
//        case 0:
//        {
//            name = "coppermoon";
//            break;
//        }
//        case 1:
//        {
//            name = "tinmoon";
//            break;
//        }
//        case 2:
//        {
//            name = "cheesestone";
//            break;
//        }
//        case 3:
//        {
//            name = "moondirt";
//            break;
//        }
//        case 4:
//        {
//            name = "moonstone";
//            break;
//        }
//        case 5:
//        {
//            name = "moongrass";
//            break;
//        }
//        case 6:
//        {
//            name = "sapphiremoon";
//            break;
//        }
//        case 14:
//        {
//            name = "bricks";
//            break;
//        }
//        default:
//            name = "null";
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
