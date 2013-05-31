package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBow;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GCCoreRenderPlayer extends RenderPlayer
{
    public GCCoreRenderPlayer()
    {
        super();
        this.mainModel = new GCCoreModelPlayer(0.0F);
        this.modelBipedMain = (GCCoreModelPlayer) this.mainModel;
        this.modelArmorChestplate = new GCCoreModelPlayer(1.0F);
        this.modelArmor = new GCCoreModelPlayer(0.5F);
    }

    public ModelBiped getModel()
    {
        return this.modelBipedMain;
    }

    @Override
    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        final ItemStack itemstack = par1EntityLiving.getHeldItem();
        float f2;

        if (itemstack != null && itemstack.getItem() instanceof GCCoreItemBow)
        {
            GL11.glPushMatrix();

            if (this.mainModel.isChild)
            {
                f2 = 0.5F;
                GL11.glTranslatef(0.0F, 0.625F, 0.0F);
                GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
                GL11.glScalef(f2, f2, f2);
            }

            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            f2 = 0.625F;
            GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
            GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(f2, -f2, f2);
            GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);

            this.renderManager.itemRenderer.renderItem(par1EntityLiving, itemstack, 0);

            GL11.glPopMatrix();
        }
        else
        {
            super.renderEquippedItems(par1EntityLiving, par2);
        }
    }

    @Override
    public void renderPlayer(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9)
    {
        final float f2 = 1.0F;
        GL11.glColor3f(f2, f2, f2);
        final ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = itemstack != null ? 1 : 0;

        if (itemstack != null && par1EntityPlayer.getItemInUseCount() > 0)
        {
            final EnumAction enumaction = itemstack.getItemUseAction();

            if (enumaction == EnumAction.block)
            {
                this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
            }
            else if (enumaction == EnumAction.bow)
            {
                this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
            }
        }

        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = par1EntityPlayer.isSneaking();
        double d3 = par4 - par1EntityPlayer.yOffset;

        if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP))
        {
            d3 -= 0.125D;
        }

        this.doRenderLiving(par1EntityPlayer, par2, d3, par6, par8, par9);
        this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
        this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
        this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
    }

    private float interpolateRotation(float par1, float par2, float par3)
    {
        float f3;

        for (f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F)
        {
            ;
        }

        while (f3 >= 180.0F)
        {
            f3 -= 360.0F;
        }

        return par1 + par3 * f3;
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);

        if (this.renderPassModel != null)
        {
            this.renderPassModel.onGround = this.mainModel.onGround;
        }

        this.mainModel.isRiding = par1EntityLiving.isRiding();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isRiding = this.mainModel.isRiding;
        }

        this.mainModel.isChild = par1EntityLiving.isChild();

        if (this.renderPassModel != null)
        {
            this.renderPassModel.isChild = this.mainModel.isChild;
        }

        try
        {
            final float f2 = this.interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
            final float f3 = this.interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
            final float f4 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
            this.renderLivingAt(par1EntityLiving, par2, par4, par6);
            final float f5 = this.handleRotationFloat(par1EntityLiving, par9);
            this.rotateCorpse(par1EntityLiving, f5, f2, par9);
            final float f6 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(par1EntityLiving, par9);
            GL11.glTranslatef(0.0F, -24.0F * f6 - 0.0078125F, 0.0F);
            float f7 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
            float f8 = par1EntityLiving.limbSwing - par1EntityLiving.limbYaw * (1.0F - par9);

            if (par1EntityLiving.isChild())
            {
                f8 *= 3.0F;
            }

            if (f7 > 1.0F)
            {
                f7 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            this.mainModel.setLivingAnimations(par1EntityLiving, f8, f7, par9);
            this.renderModel(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);
            float f9;
            int i;
            float f10;
            float f11;

            for (int j = 0; j < 4; ++j)
            {
                i = this.shouldRenderPass(par1EntityLiving, j, par9);

                if (i > 0)
                {
                    this.renderPassModel.setLivingAnimations(par1EntityLiving, f8, f7, par9);
                    this.renderPassModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);

                    if ((i & 240) == 16)
                    {
                        this.func_82408_c(par1EntityLiving, j, par9);
                        this.renderPassModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);
                    }

                    if ((i & 15) == 15)
                    {
                        f9 = par1EntityLiving.ticksExisted + par9;
                        this.loadTexture("%blur%/misc/glint.png");
                        GL11.glEnable(GL11.GL_BLEND);
                        f10 = 0.5F;
                        GL11.glColor4f(f10, f10, f10, 1.0F);
                        GL11.glDepthFunc(GL11.GL_EQUAL);
                        GL11.glDepthMask(false);

                        for (int k = 0; k < 2; ++k)
                        {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            f11 = 0.76F;
                            GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
                            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                            GL11.glMatrixMode(GL11.GL_TEXTURE);
                            GL11.glLoadIdentity();
                            final float f12 = f9 * (0.001F + k * 0.003F) * 20.0F;
                            final float f13 = 0.33333334F;
                            GL11.glScalef(f13, f13, f13);
                            GL11.glRotatef(30.0F - k * 60.0F, 0.0F, 0.0F, 1.0F);
                            GL11.glTranslatef(0.0F, f12, 0.0F);
                            GL11.glMatrixMode(GL11.GL_MODELVIEW);
                            this.renderPassModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glDepthMask(true);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GL11.glEnable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glDepthFunc(GL11.GL_LEQUAL);
                    }

                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glDepthMask(true);
            this.renderEquippedItems(par1EntityLiving, par9);
            final float f14 = par1EntityLiving.getBrightness(par9);
            i = this.getColorMultiplier(par1EntityLiving, f14, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((i >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
                {
                    GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                    this.mainModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);

                    for (int l = 0; l < 4; ++l)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, l, par9) >= 0)
                        {
                            GL11.glColor4f(f14, 0.0F, 0.0F, 0.4F);
                            this.renderPassModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }
                }

                if ((i >> 24 & 255) > 0)
                {
                    f9 = (i >> 16 & 255) / 255.0F;
                    f10 = (i >> 8 & 255) / 255.0F;
                    final float f15 = (i & 255) / 255.0F;
                    f11 = (i >> 24 & 255) / 255.0F;
                    GL11.glColor4f(f9, f10, f15, f11);
                    this.mainModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);

                    for (int i1 = 0; i1 < 4; ++i1)
                    {
                        if (this.inheritRenderPass(par1EntityLiving, i1, par9) >= 0)
                        {
                            GL11.glColor4f(f9, f10, f15, f11);
                            this.renderPassModel.render(par1EntityLiving, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(par1EntityLiving, par2, par4, par6);
    }

    @Override
    public boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = RenderManager.instance.renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
            return true;
        }
        else
        {
            return false;
        }
    }
}
