package codechicken.core.featurehack;

import java.lang.reflect.Field;
import codechicken.core.ReflectionManager;
import codechicken.core.featurehack.mc.TextureLavaFX;
import codechicken.core.featurehack.mc.TextureLavaFlowFX;
import codechicken.core.featurehack.mc.TextureWaterFX;
import codechicken.core.featurehack.mc.TextureWaterFlowFX;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import codechicken.core.asm.TweakTransformer;
import codechicken.lib.asm.ObfMapping;

public class LiquidTextures
{
    public static IIcon[] newTextures = new IIcon[4];
    
    public static boolean replaceLava;
    public static boolean replaceWater;
    
    private static Field field_tex;
    
    public static void init()
    {
        replaceWater = TweakTransformer.tweaks.getTag("replaceWaterFX").setComment("Set this to true to use the pre1.5 water textures").getBooleanValue(false);
        replaceLava = TweakTransformer.tweaks.getTag("replaceLavaFX").setComment("Set this to true to use the pre1.5 lava textures").getBooleanValue(false);
        if(replaceWater)
        {
            newTextures[0] = new TextureWaterFX().texture;
            newTextures[1] = new TextureWaterFlowFX().texture;
        }
        if(replaceLava)
        {
            newTextures[2] = new TextureLavaFX().texture;
            newTextures[3] = new TextureLavaFlowFX().texture;
        }
        
        if(replaceWater || replaceLava)
        {
            MinecraftForge.EVENT_BUS.register(new LiquidTextures());
            field_tex = ReflectionManager.getField(new ObfMapping("net/minecraft/block/BlockLiquid", "field_149806_a", "[Lnet/minecraft/util/IIcon;"));
        }
    }

    @SubscribeEvent
    public void postStitch(TextureStitchEvent.Post event)
    {
        IIcon[] icons;
        if(replaceLava)
        {
            icons = ReflectionManager.get(field_tex, IIcon[].class, Blocks.flowing_lava);
            icons[0] = newTextures[2];
            icons[1] = newTextures[3];
            icons = ReflectionManager.get(field_tex, IIcon[].class, Blocks.lava);
            icons[0] = newTextures[2];
            icons[1] = newTextures[3];
        }
        if(replaceWater)
        {
            icons = ReflectionManager.get(field_tex, IIcon[].class, Blocks.flowing_water);
            icons[0] = newTextures[0];
            icons[1] = newTextures[1];
            icons = ReflectionManager.get(field_tex, IIcon[].class, Blocks.water);
            icons[0] = newTextures[0];
            icons[1] = newTextures[1];
        }
    }
}
