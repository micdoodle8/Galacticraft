package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeletonBoss extends RenderLiving
{
    private static final ResourceLocation skeletonBossTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/skeletonboss.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/power.png");

    private final ModelEvolvedSkeletonBoss model = new ModelEvolvedSkeletonBoss();

    public RenderEvolvedSkeletonBoss()
    {
        super(new ModelEvolvedSkeletonBoss(), 1.0F);
    }

    protected ResourceLocation func_110779_a(EntitySkeletonBoss par1EntityArrow)
    {
        return RenderEvolvedSkeletonBoss.skeletonBossTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntitySkeletonBoss) par1Entity);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
    }

    @Override
    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        BossStatus.setBossStatus((IBossDisplayData) par1EntityLiving, false);

        super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2)
    {
        if (((EntitySkeletonBoss) par1EntityLiving).throwTimer + ((EntitySkeletonBoss) par1EntityLiving).postThrowDelay == 0)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.3F, -1.6F, -1.2F);
            GL11.glTranslatef(0.1F, 0.0F, 0.0F);
            GL11.glRotatef(41, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 0.7F);
            this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Items.bow), 0);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(0.11F, -1.6F, -1.2F);
            GL11.glTranslatef(0.1F, 0.0F, 0.0F);
            GL11.glRotatef(46, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.7F, 0.7F, 0.7F);
            this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Items.bow), 0);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();

        final EntityPlayerSP player = minecraft.thePlayer;

        ItemStack helmetSlot = null;

        if (player != null && player.inventory.armorItemInSlot(3) != null)
        {
            helmetSlot = player.inventory.armorItemInSlot(3);
        }

        if (helmetSlot != null && helmetSlot.getItem() instanceof ItemSensorGlasses && minecraft.currentScreen == null)
        {
            if (par2 == 1)
            {
                final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
                this.bindTexture(RenderEvolvedSkeletonBoss.powerTexture);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                final float var5 = var4 * 0.01F;
                final float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                // this.model.aimedBow = true;
                this.setRenderPassModel(this.model);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                final float var7 = 0.5F;
                GL11.glColor4f(var7, var7, var7, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (par2 == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }
}
