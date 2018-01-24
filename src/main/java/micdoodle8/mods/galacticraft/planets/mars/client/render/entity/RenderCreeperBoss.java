package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelCreeperBoss;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCreeperBoss;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCreeperBoss extends RenderLiving<EntityCreeperBoss>
{
    private static final ResourceLocation creeperTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/creeper.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/power.png");
    private final ModelBase creeperModel = new ModelCreeperBoss(2.0F);

    public RenderCreeperBoss(RenderManager renderManager)
    {
        super(renderManager, new ModelCreeperBoss(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCreeperBoss entity)
    {
        return RenderCreeperBoss.creeperTexture;
    }

//    protected int func_27006_a(EntityCreeperBoss par1EntityCreeper, int par2, float par3)
//    {
//        if (par1EntityCreeper.headsRemaining == 1)
//        {
//            if (par2 == 1)
//            {
//                final float var4 = par1EntityCreeper.ticksExisted + par3;
//                this.bindTexture(RenderCreeperBoss.powerTexture);
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
//        return -1;
//    }

    @Override
    protected void preRenderCallback(EntityCreeperBoss par1EntityLiving, float par2)
    {
        GL11.glScalef(4.0F, 4.0F, 4.0F);
        GL11.glRotatef((float) (Math.pow(par1EntityLiving.deathTicks, 2) / 5.0F + (Math.pow(par1EntityLiving.deathTicks, 2) / 5.0F - Math.pow(par1EntityLiving.deathTicks - 1, 2) / 5.0F) * par2), 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected int getColorMultiplier(EntityCreeperBoss par1EntityLivingBase, float par2, float par3)
    {
        return super.getColorMultiplier(par1EntityLivingBase, par2, par3);
    }

//    @Override
//    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
//    {
//        return this.func_27006_a((EntityCreeperBoss) par1EntityLivingBase, par2, par3);
//    }
//
//    @Override
//    protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
//    {
//        return -1;
//    }
}
