package micdoodle8.mods.galacticraft.core.items;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBucketGalacticraft extends ItemBucket {

	private String texture_prefix;

	public ItemBucketGalacticraft(Block block, String texture_prefix) {
		super(block);
		this.texture_prefix = texture_prefix;
		setContainerItem(Items.bucket);
		setCreativeTab(GalacticraftCore.galacticraftItemsTab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", texture_prefix));
	}
}
