package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.*;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;
import org.lwjgl.opengl.GL11;

/**
 * This renders the thermal armor (unless RenderPlayerAPI is installed).
 * The thermal armor render is done after the corresponding body part of the player is drawn.
 * This ALSO patches RenderPlayer so that it uses ModelPlayerGC in place of ModelPlayer to draw the player.
 *
 * Finally, this also adds a hook into rotateCorpse so as to fire a RotatePlayerEvent - used by the Cryogenic Chamber
 *
 * @author User
 *
 */
public class RenderPlayerGC extends RenderPlayer
{
    public static ResourceLocation thermalPaddingTexture0;
    public static ResourceLocation thermalPaddingTexture1;
    public static boolean flagThermalOverride = false;
    private static Boolean isSmartRenderLoaded = null;

    public RenderPlayerGC()
    {
        this(false);
    }

    public RenderPlayerGC(boolean smallArms)
    {
        super(FMLClientHandler.instance().getClient().getRenderManager(), smallArms);
        this.mainModel = new ModelPlayerGC(0.0F, smallArms);
        this.addLayer(new LayerOxygenTanks(this));
        this.addLayer(new LayerOxygenGear(this));
        this.addLayer(new LayerOxygenMask(this));
        this.addLayer(new LayerOxygenParachute(this));
        this.addLayer(new LayerFrequencyModule(this));

        if (GalacticraftCore.isPlanetsLoaded)
        {
            this.addLayer(new LayerThermalPadding(this));

            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftplanets", "textures/misc/thermalPadding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftplanets", "textures/misc/thermalPadding_1.png");
        }
    }

    @Override
    protected void rotateCorpse(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
    {
        if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(par1AbstractClientPlayer);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
                super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
            }
            else if (event.shouldRotate == null || event.shouldRotate)
            {
                GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
          	if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
        	{
                final EntityPlayer player = (EntityPlayer)par1AbstractClientPlayer;

                if (player.ridingEntity instanceof EntityTieredRocket)
                {
                    EntityTieredRocket rocket = (EntityTieredRocket) player.ridingEntity;
                    GL11.glTranslatef(0, -rocket.getRotateOffset(), 0);
                    float anglePitch = rocket.prevRotationPitch;
                    float angleYaw = rocket.prevRotationYaw;
                    GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
                    GL11.glTranslatef(0, rocket.getRotateOffset(), 0);
                }
        	}
          	super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
        }
    }

    public static class RotatePlayerEvent extends PlayerEvent
    {
        public Boolean shouldRotate = null;
        public boolean vanillaOverride = false;

        public RotatePlayerEvent(AbstractClientPlayer player)
        {
            super(player);
        }
    }
}
