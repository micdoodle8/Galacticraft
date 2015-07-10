package micdoodle8.mods.galacticraft.core.client.render.entities;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedCreeper extends RenderCreeper
{
    private static final ResourceLocation creeperTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/creeper.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/power.png");

    private final ModelBase creeperModel = new ModelEvolvedCreeper(0.2F);

    public RenderEvolvedCreeper()
    {
        super(FMLClientHandler.instance().getClient().getRenderManager());
        this.mainModel = new ModelEvolvedCreeper();
    }

    protected ResourceLocation func_110779_a(EntityCreeper par1EntityArrow)
    {
        return RenderEvolvedCreeper.creeperTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((EntityCreeper) par1Entity);
    }

    protected void updateCreeperScale(EntityCreeper par1GCEntityCreeper, float par2)
    {
        float var4 = par1GCEntityCreeper.getCreeperFlashIntensity(par2);
        final float var5 = 1.0F + MathHelper.sin(var4 * 100.0F) * var4 * 0.01F;

        if (var4 < 0.0F)
        {
            var4 = 0.0F;
        }

        if (var4 > 1.0F)
        {
            var4 = 1.0F;
        }

        var4 *= var4;
        var4 *= var4;
        final float var6 = (1.0F + var4 * 0.4F) * var5;
        final float var7 = (1.0F + var4 * 0.1F) / var5;
        GL11.glScalef(0.2F + var6, 0.2F + var7, 0.2F + var6);
    }

    protected int updateCreeperColorMultiplier(EntityCreeper par1GCEntityCreeper, float par2, float par3)
    {
        final float var5 = par1GCEntityCreeper.getCreeperFlashIntensity(par3);

        if ((int) (var5 * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int var6 = (int) (var5 * 0.2F * 255.0F);

            if (var6 < 0)
            {
                var6 = 0;
            }

            if (var6 > 255)
            {
                var6 = 255;
            }

            final short var7 = 255;
            final short var8 = 255;
            final short var9 = 255;
            return var6 << 24 | var7 << 16 | var8 << 8 | var9;
        }
    }

    protected int func_77061_b(EntityCreeper par1GCEntityCreeper, int par2, float par3)
    {
        return -1;
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        this.updateCreeperScale((EntityCreeper) par1EntityLiving, par2);
    }

    @Override
    protected int getColorMultiplier(EntityLivingBase par1EntityLiving, float par2, float par3)
    {
        return this.updateCreeperColorMultiplier((EntityCreeper) par1EntityLiving, par2, par3);
    }

//    @Override
//    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
//    {
//        EntityEvolvedCreeper creeper = (EntityEvolvedCreeper) par1EntityLiving;
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
//                final float var4 = creeper.ticksExisted * 2 + par3;
//                this.bindTexture(RenderEvolvedCreeper.powerTexture);
//                GL11.glMatrixMode(GL11.GL_TEXTURE);
//                GL11.glLoadIdentity();
//                final float var5 = var4 * 0.01F;
//                final float var6 = var4 * 0.01F;
//                GL11.glTranslatef(var5, var6, 0.0F);
//                this.setRenderPassModel(this.creeperModel);
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
//        return super.shouldRenderPass(creeper, par2, par3);
//    } TODO
//
//    @Override
//    protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
//    {
//        return this.func_77061_b((EntityCreeper) par1EntityLiving, par2, par3);
//    }
}
