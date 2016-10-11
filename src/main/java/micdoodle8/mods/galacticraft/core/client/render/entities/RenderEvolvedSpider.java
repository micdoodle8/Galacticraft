package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
public class RenderEvolvedSpider extends RenderLiving<EntityEvolvedSpider>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/spider.png");

    public RenderEvolvedSpider(RenderManager manager)
    {
        super(manager, new ModelEvolvedSpider(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedSpider par1Entity)
    {
        return RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSpider par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
    }

    @Override
    protected float getDeathMaxRotation(EntityEvolvedSpider par1EntityLiving)
    {
        return 180.0F;
    }
}
