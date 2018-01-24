package micdoodle8.mods.galacticraft.core.client.render.item;

import java.lang.reflect.Field;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureDungeonFinder extends TextureAtlasSprite
{
    /** Current compass heading in radians */
    public double currentAngle;
    /** Speed and direction of compass rotation */
    public double angleDelta;

    public TextureDungeonFinder(String iconName)
    {
        super(iconName);
    }

    @Override
    public void updateAnimation()
    {
        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.world != null && minecraft.player != null)
        {
            this.updateCompass(minecraft.world, minecraft.player, (double)minecraft.player.rotationYaw, false, false);
        }
        else
        {
            this.updateCompass((World)null, null, 0.0D, true, false);
        }
    }

    /**
     * Updates the compass based on the given x,z coords and camera direction
     */
    public void updateCompass(World worldIn, EntityPlayerSP player, double angle, boolean flag1, boolean flag2)
    {
        if (!this.framesTextureData.isEmpty())
        {
            double d0 = 0.0D;

            if (worldIn != null && player != null && !flag1)
            {
                if (worldIn.provider instanceof IGalacticraftWorldProvider && player.inventory.hasItemStack(new ItemStack(GCItems.dungeonFinder)))
                {
                    double direction = GCPlayerStatsClient.get(player).getDungeonDirection();
                    angle = (angle - direction) % 360.0D;
                    d0 = -angle / Constants.RADIANS_TO_DEGREES_D;
                }
                else
                {
                    d0 = Math.random() * Math.PI * 2.0D;
                }
            }

            if (flag2)
            {
                this.currentAngle = d0;
            }
            else
            {
                double d3;

                for (d3 = d0 - this.currentAngle; d3 < -Math.PI; d3 += (Math.PI * 2D))
                {
                    ;
                }

                while (d3 >= Math.PI)
                {
                    d3 -= (Math.PI * 2D);
                }

                d3 = MathHelper.clamp(d3, -1.0D, 1.0D);
                this.angleDelta += d3 * 0.1D;
                this.angleDelta *= 0.8D;
                this.currentAngle += this.angleDelta;
            }

            int i;

            for (i = (int)((this.currentAngle / (Math.PI * 2D) + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
            {
                ;
            }

            if (i != this.frameCounter)
            {
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }

    public void register(TextureMap map)
    {
        try
        {
            Field f = map.getClass().getDeclaredField(GCCoreUtil.isDeobfuscated() ? "mapRegisteredSprites" : "field_110574_e");
            f.setAccessible(true);
            Map<String, TextureAtlasSprite> textureMap = (Map<String, TextureAtlasSprite>) f.get(map);
            textureMap.put(this.getIconName(), this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}