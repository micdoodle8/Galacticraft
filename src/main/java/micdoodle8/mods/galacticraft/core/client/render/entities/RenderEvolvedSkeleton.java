package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeleton;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeleton extends RenderBiped
{
    private static final ResourceLocation skeletonTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/skeleton.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/power.png");

    private final ModelEvolvedSkeleton model = new ModelEvolvedSkeleton(0.2F);
    private static int isBG2Loaded = 0;

    public RenderEvolvedSkeleton()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager(), new ModelEvolvedSkeleton(), 1.0F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this) {
            protected void func_177177_a() {
                this.field_177189_c = new ModelSkeleton(0.5F, true);
                this.field_177186_d = new ModelSkeleton(1.0F, true);
            }
        });

        //Compatibility with BattleGear2
        try
        {
            Class<?> clazz = Class.forName("mods.battlegear2.MobHookContainerClass");

            //accessing this: public static final int Skell_Arrow_Datawatcher = 25;
            RenderEvolvedSkeleton.isBG2Loaded = clazz.getField("Skell_Arrow_Datawatcher").getInt(null);
        }
        catch (Exception e)
        {
        }
    }

    protected ResourceLocation func_110779_a(EntitySkeleton par1EntityArrow)
    {
        return RenderEvolvedSkeleton.skeletonTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntitySkeleton) par1Entity);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
    }

//    @Override
//    protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2)
//    {
//        if (RenderEvolvedSkeleton.isBG2Loaded > 0)
//        {
//            if (par1EntityLiving.getDataWatcher().getWatchedObject(RenderEvolvedSkeleton.isBG2Loaded) == null)
//            {
//                par1EntityLiving.getDataWatcher().addObject(RenderEvolvedSkeleton.isBG2Loaded, (byte) -1);
//            }
//        }
//
//        GL11.glPushMatrix();
//        GL11.glTranslatef(-0.3F, -0.3F, -0.6F);
//        GL11.glTranslatef(0.1F, 0.0F, 0.0F);
//        GL11.glRotatef(41, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
//        GL11.glScalef(0.5F, 0.5F, 0.5F);
//        FMLClientHandler.instance().getClient().getItemRenderer().renderItem(par1EntityLiving, new ItemStack(Items.bow), ItemCameraTransforms.TransformType.NONE);
//        GL11.glPopMatrix();
//
//        GL11.glPushMatrix();
//        GL11.glTranslatef(0.11F, -0.3F, -0.6F);
//        GL11.glTranslatef(0.1F, 0.0F, 0.0F);
//        GL11.glRotatef(46, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
//        GL11.glScalef(0.5F, 0.5F, 0.5F);
//        this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Items.bow), 0);
//        GL11.glPopMatrix();
//
//        super.renderEquippedItems(par1EntityLiving, par2);
//    }

//    @Override
//    public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
//    {
//        super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
//        this.field_82423_g.aimedBow = this.field_82425_h.aimedBow = this.modelBipedMain.aimedBow = true;
//    }

    public void func_82422_c()
    {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

//    @Override
//    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
//    {
//        final Minecraft minecraft = FMLClientHandler.instance().getClient();
//
//        final EntityPlayerSP player = minecraft.thePlayer;
//
//        ItemStack helmetSlot = null;
//
//        if (player != null && player.inventory.armorItemInSlot(3) != null)
//        {
//            helmetSlot = player.inventory.armorItemInSlot(3);
//        }
//
//        if (helmetSlot != null && helmetSlot.getItem() instanceof ItemSensorGlasses && minecraft.currentScreen == null)
//        {
//            if (par2 == 1)
//            {
//                final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
//                this.bindTexture(RenderEvolvedSkeleton.powerTexture);
//                GL11.glMatrixMode(GL11.GL_TEXTURE);
//                GL11.glLoadIdentity();
//                final float var5 = var4 * 0.01F;
//                final float var6 = var4 * 0.01F;
//                GL11.glTranslatef(var5, var6, 0.0F);
//                this.model.aimedBow = true;
//                this.setRenderPassModel(this.model);
//                GL11.glMatrixMode(GL11.GL_MODELVIEW);
//                GL11.glEnable(GL11.GL_BLEND);
//                final float var7 = 0.5F;
//                GL11.glColor4f(var7, var7, var7, 1.0F);
//                GL11.glDisable(GL11.GL_LIGHTING);
//                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//                return 1;
//            }
//
//            if (par2 == 2)
//            {
//                GL11.glMatrixMode(GL11.GL_TEXTURE);
//                GL11.glLoadIdentity();
//                GL11.glMatrixMode(GL11.GL_MODELVIEW);
//                GL11.glEnable(GL11.GL_LIGHTING);
//                GL11.glDisable(GL11.GL_BLEND);
//            }
//        }
//
//        return super.shouldRenderPass(par1EntityLiving, par2, par3);
//    }
//
//    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
//    {
//        ItemStack stack = par1EntityLiving.getLastActiveItems()[0];
//        par1EntityLiving.getLastActiveItems()[0] = null;
//        super.renderEquippedItems(par1EntityLiving, par2);
//        par1EntityLiving.getLastActiveItems()[0] = stack;
//    }
}
