package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelBipedGC;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import java.util.Iterator;

/**
 * This renders the thermal armor (unless RenderPlayerAPI is installed).
 * The thermal armor render is done after the corresponding body part of the player is drawn.
 * This ALSO patches RenderPlayer so that it uses ModelPlayerGC in place of ModelPlayer to draw the player.
 * <p>
 * Finally, this also adds a hook into rotateCorpse so as to fire a RotatePlayerEvent - used by the Cryogenic Chamber
 *
 * @author User
 */
public class RenderPlayerGC extends RenderPlayer
{
    public static ResourceLocation thermalPaddingTexture0;
    public static ResourceLocation thermalPaddingTexture1;
    public static ResourceLocation thermalPaddingTexture1_T2;
    public static ResourceLocation heatShieldTexture;
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

        // The following code removes the vanilla armor layer renderer and replaces it with the Galacticraft one
        boolean removedVanilla = false;
        Iterator<LayerRenderer<AbstractClientPlayer>> iterator = this.layerRenderers.iterator();
        while (iterator.hasNext())
        {
            LayerRenderer<AbstractClientPlayer> renderer = iterator.next();
            if (renderer.getClass().equals(LayerBipedArmor.class))
            {
                iterator.remove();
                removedVanilla = true;
            }
        }

        if (removedVanilla)
        {
            LayerBipedArmor playerArmor = new LayerBipedArmor(this)
            {
                @Override
                protected void initArmor()
                {
                    this.field_177189_c = new ModelBipedGC(0.5F);
                    this.field_177186_d = new ModelBipedGC(1.0F);
                }
            };
            this.addLayer(playerArmor);
        }

        if (GalacticraftCore.isPlanetsLoaded)
        {
            this.addLayer(new LayerThermalPadding(this));

            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_1.png");
            RenderPlayerGC.thermalPaddingTexture1_T2 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_t2_1.png");
            RenderPlayerGC.heatShieldTexture = new ResourceLocation("galacticraftplanets", "textures/misc/shield.png");

            this.addLayer(new LayerShield(this));
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
                final EntityPlayer player = (EntityPlayer) par1AbstractClientPlayer;

                if (player.ridingEntity instanceof ICameraZoomEntity)
                {
                    Entity rocket = player.ridingEntity;
                    float rotateOffset = ((ICameraZoomEntity)rocket).getRotateOffset();
                    if (rotateOffset > -10F)
                    {
                        GL11.glTranslatef(0, -rotateOffset, 0);
                        float anglePitch = rocket.prevRotationPitch;
                        float angleYaw = rocket.prevRotationYaw;
                        GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0, rotateOffset, 0);
                    }
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
