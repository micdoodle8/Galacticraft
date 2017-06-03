package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.EntitySkeletonBoss;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeletonBoss extends RenderLiving<EntitySkeletonBoss>
{
    private static final ResourceLocation skeletonBossTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/skeletonboss.png");
//    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/power.png");

//    private final ModelEvolvedSkeletonBoss model = new ModelEvolvedSkeletonBoss();

    public RenderEvolvedSkeletonBoss(RenderManager manager)
    {
        super(manager, new ModelEvolvedSkeletonBoss(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySkeletonBoss par1Entity)
    {
        return RenderEvolvedSkeletonBoss.skeletonBossTexture;
    }

    @Override
    protected void preRenderCallback(EntitySkeletonBoss par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        GL11.glRotatef((float) (Math.pow(par1EntityLiving.deathTicks, 2) / 5.0F + (Math.pow(par1EntityLiving.deathTicks, 2) / 5.0F - Math.pow(par1EntityLiving.deathTicks - 1, 2) / 5.0F) * par2), 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void doRender(EntitySkeletonBoss par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        BossStatus.setBossStatus((IBossDisplayData) par1EntityLiving, false);

        super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
    }
}
