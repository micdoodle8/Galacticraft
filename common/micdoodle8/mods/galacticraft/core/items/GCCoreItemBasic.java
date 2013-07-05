//package micdoodle8.mods.galacticraft.core.items;
//
//import java.util.List;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import net.minecraft.client.renderer.texture.IconRegister;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.Icon;
//
//public class GCCoreItemBasic extends Item
//{
//	protected Icon[] icons = new Icon[256];
//
//	public static final String[] names = {
//		"canister_tin",
//		"canister_copper",
//		"canvas",
//		"flagpole",
//		"sensor_lens",
//		"rocket_nose_cone",
//		"rocket_fin",
//		"rocket_engine",
//		"heavy_plating",
//		"air_condenser",
//		"air_fan",
//		"air_vent"};
//
//	public GCCoreItemBasic(int id)
//	{
//		super(id);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//	}
//
//	@Override
//    public CreativeTabs getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftTab;
//    }
//
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerIcons(IconRegister iconRegister)
//	{
//		int i = 0;
//
//		for (final String name : GCCoreItemBasic.names)
//		{
//			this.icons[i++] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "" + name + GalacticraftCore.TEXTURE_SUFFIX);
//		}
//	}
//
//	@Override
//	public String getUnlocalizedName(ItemStack itemStack)
//	{
//		return "item." + GCCoreItemBasic.names[itemStack.getItemDamage()];
//	}
//
//	@Override
//	public Icon getIconFromDamage(int damage)
//	{
//		if (this.icons.length > damage)
//		{
//			return this.icons[damage];
//		}
//
//		return super.getIconFromDamage(damage);
//	}
//
//    @Override
//    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
//    {
//    	for (int i = 0; i < this.names.length; i++)
//    	{
//            par3List.add(new ItemStack(par1, 1, i));
//    	}
//    }
//
//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }
// }
