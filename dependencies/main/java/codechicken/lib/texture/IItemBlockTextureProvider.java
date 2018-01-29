package codechicken.lib.texture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * Created by covers1624 on 30/10/2016.
 * TODO Document.
 */
public interface IItemBlockTextureProvider {

    TextureAtlasSprite getTexture(EnumFacing side, ItemStack stack);

}
