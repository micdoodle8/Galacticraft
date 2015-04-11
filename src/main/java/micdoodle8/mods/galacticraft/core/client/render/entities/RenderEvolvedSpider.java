package micdoodle8.mods.galacticraft.core.client.render.entities;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSpider;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSpider extends RenderLiving
{
    private static final ResourceLocation spiderEyesTextures = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/spider_eyes.png");
    private static final ResourceLocation spiderTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/spider.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/power.png");

    private final ModelBase model = new ModelEvolvedSpider(0.2F);

    public RenderEvolvedSpider()
    {
        super(new ModelEvolvedSpider(), 1.0F);
        this.setRenderPassModel(new ModelEvolvedSpider());
    }

    protected ResourceLocation func_110779_a(EntitySpider par1EntityArrow)
    {
        return RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntitySpider) par1Entity);
    }

    protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider)
    {
        return 180.0F;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
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
        
        if (par2 == 3)
        {
            this.bindTexture(spiderEyesTextures);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            if (par1EntityLiving.isInvisible())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            GL11.glDisable(GL11.GL_LIGHTING);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            return 1;
        }

        if (helmetSlot != null && helmetSlot.getItem() instanceof ItemSensorGlasses && minecraft.currentScreen == null)
        {
            if (par2 == 1)
            {
                final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
                this.bindTexture(RenderEvolvedSpider.powerTexture);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                final float var5 = var4 * 0.01F;
                final float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
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

    @Override
    protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving)
    {
        return this.setSpiderDeathMaxRotation((EntitySpider) par1EntityLiving);
    }
}
