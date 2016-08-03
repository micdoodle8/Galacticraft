package codechicken.core.featurehack;

import codechicken.core.asm.TweakTransformer;
import codechicken.core.featurehack.mc.TextureLavaFX;
import codechicken.core.featurehack.mc.TextureLavaFlowFX;
import codechicken.core.featurehack.mc.TextureWaterFX;
import codechicken.core.featurehack.mc.TextureWaterFlowFX;
import codechicken.lib.render.TextureSpecial;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class LiquidTextures {
    public static TextureSpecial[] newTextures = new TextureSpecial[4];

    public static boolean replaceLava;
    public static boolean replaceWater;

    public static void init() {
        replaceWater = TweakTransformer.tweaks.getTag("replaceWaterFX").setComment("Set this to true to use the pre1.5 water textures").getBooleanValue(false);
        replaceLava = TweakTransformer.tweaks.getTag("replaceLavaFX").setComment("Set this to true to use the pre1.5 lava textures").getBooleanValue(false);
        if (replaceWater) {
            newTextures[0] = new TextureWaterFX().texture;
            newTextures[1] = new TextureWaterFlowFX().texture;
        }
        if (replaceLava) {
            newTextures[2] = new TextureLavaFX().texture;
            newTextures[3] = new TextureLavaFlowFX().texture;
        }

        if (replaceWater || replaceLava) {
            MinecraftForge.EVENT_BUS.register(new LiquidTextures());
        }
    }

    @SubscribeEvent
    public void postStitch(TextureStitchEvent.Post event) {
        Map<String, TextureAtlasSprite> uploadedSprites = event.map.mapUploadedSprites;
        if (replaceWater) {
            uploadedSprites.put("minecraft:blocks/water_still", newTextures[0]);
            uploadedSprites.put("minecraft:blocks/water_flow", newTextures[1]);
        }
        if (replaceLava) {
            uploadedSprites.put("minecraft:blocks/lava_still", newTextures[2]);
            uploadedSprites.put("minecraft:blocks/lava_flow", newTextures[3]);
        }
    }
}
