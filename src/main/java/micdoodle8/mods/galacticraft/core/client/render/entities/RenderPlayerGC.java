/*
package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.lwjgl.opengl.GL11;

public class RenderPlayerGC extends RenderPlayer
{
    public ModelBiped modelThermalPadding;
    public ModelBiped modelThermalPaddingHelmet;
    private static ResourceLocation thermalPaddingTexture0;
    private static ResourceLocation thermalPaddingTexture1;
    public static boolean flagThermalOverride = false;

    public RenderPlayerGC()
    {
        super();
        this.mainModel = new ModelPlayerGC(0.0F);
        this.modelBipedMain = (ModelPlayerGC) this.mainModel;
        this.modelArmorChestplate = new ModelPlayerGC(1.0F);
        this.modelArmor = new ModelPlayerGC(0.5F);
        this.modelThermalPadding = new ModelPlayerGC(0.25F);
        this.modelThermalPaddingHelmet = new ModelPlayerGC(0.9F);

        if (GalacticraftCore.isPlanetsLoaded)
        {
            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_1.png");
        }
    }

    @Override
    protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.renderModel(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);

        if (RenderPlayerGC.thermalPaddingTexture0 != null)
        {
            PlayerGearData gearData = ClientProxyCore.playerItemData.get(par1EntityLivingBase.getCommandSenderName());

            if (gearData != null && !RenderPlayerGC.flagThermalOverride)
            {
                ModelBiped modelBiped;

                for (int i = 0; i < 4; ++i)
                {
                    if (i == 0)
                    {
                        modelBiped = this.modelThermalPaddingHelmet;
                    }
                    else
                    {
                        modelBiped = this.modelThermalPadding;
                    }

                    int padding = gearData.getThermalPadding(i);

                    if (padding >= 0 && !par1EntityLivingBase.isInvisible())
                    {
                        GL11.glColor4f(1, 1, 1, 1);
                        this.bindTexture(RenderPlayerGC.thermalPaddingTexture1);
                        modelBiped.bipedHead.showModel = i == 0;
                        modelBiped.bipedHeadwear.showModel = i == 0;
                        modelBiped.bipedBody.showModel = i == 1 || i == 2;
                        modelBiped.bipedRightArm.showModel = i == 1;
                        modelBiped.bipedLeftArm.showModel = i == 1;
                        modelBiped.bipedRightLeg.showModel = i == 2 || i == 3;
                        modelBiped.bipedLeftLeg.showModel = i == 2 || i == 3;
                        modelBiped.onGround = this.mainModel.onGround;
                        modelBiped.isRiding = this.mainModel.isRiding;
                        modelBiped.isChild = this.mainModel.isChild;
                        if (this.mainModel instanceof ModelBiped)
                        {
                            modelBiped.heldItemLeft = ((ModelBiped) this.mainModel).heldItemLeft;
                            modelBiped.heldItemRight = ((ModelBiped) this.mainModel).heldItemRight;
                            modelBiped.isSneak = ((ModelBiped) this.mainModel).isSneak;
                            modelBiped.aimedBow = ((ModelBiped) this.mainModel).aimedBow;
                        }
                        modelBiped.setLivingAnimations(par1EntityLivingBase, par2, par3, 0.0F);
                        modelBiped.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);

                        // Start alpha render
                        GL11.glDisable(GL11.GL_LIGHTING);
                        this.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        float time = par1EntityLivingBase.ticksExisted / 10.0F;
                        float sTime = (float) Math.sin(time) * 0.5F + 0.5F;

                        float r = 0.2F * sTime;
                        float g = 1.0F * sTime;
                        float b = 0.2F * sTime;

                        if (par1EntityLivingBase.worldObj.provider instanceof IGalacticraftWorldProvider)
                        {
                            float modifier = ((IGalacticraftWorldProvider) par1EntityLivingBase.worldObj.provider).getThermalLevelModifier();

                            if (modifier > 0)
                            {
                                b = g;
                                g = r;
                            }
                            else if (modifier < 0)
                            {
                                r = g;
                                g = b;
                            }
                        }

                        GL11.glColor4f(r, g, b, 0.4F * sTime);
                        modelBiped.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glColor4f(1, 1, 1, 1);
                        GL11.glEnable(GL11.GL_LIGHTING);
                    }
                }
            }
        }
    }

    @Override
    protected void rotateCorpse(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
    {
        if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(par1AbstractClientPlayer);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.shouldRotate == null || event.shouldRotate)
            {
                GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
            super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
        }
    }

    public static class RotatePlayerEvent extends PlayerEvent
    {
        public Boolean shouldRotate = null;

        public RotatePlayerEvent(AbstractClientPlayer player)
        {
            super(player);
        }
    }
}
*/
