package codechicken.lib.model.blockbakery;

import codechicken.lib.block.property.unlisted.UnlistedMapProperty;
import codechicken.lib.block.property.unlisted.UnlistedMapProperty.IMapStringGenerator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 26/12/2016.
 * TODO, Maybe move away from this, Instead of storing the entire render context inside the state, store the important data, then compute if necessary, will require changes basically everywhere.
 */
public class BlockBakeryProperties {

    public static final UnlistedMapProperty LAYER_FACE_SPRITE_MAP;

    static {
        LAYER_FACE_SPRITE_MAP = new UnlistedMapProperty("layer_face_sprite");

        LAYER_FACE_SPRITE_MAP.setStringGenerator(new IMapStringGenerator() {
            @SuppressWarnings ("unchecked")
            @Override
            public String makeString(Map map) {
                StringBuilder builder = new StringBuilder();
                for (Entry<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>> layerEntry : ((Map<BlockRenderLayer, Map<EnumFacing, TextureAtlasSprite>>) map).entrySet()) {
                    builder.append(layerEntry.getKey().toString()).append(",");
                    for (Entry<EnumFacing, TextureAtlasSprite> faceSpriteEntry : layerEntry.getValue().entrySet()) {
                        builder.append(faceSpriteEntry.getKey()).append(",").append(faceSpriteEntry.getValue().getIconName()).append(",");
                    }
                }
                return builder.toString();
            }
        });
    }
}
