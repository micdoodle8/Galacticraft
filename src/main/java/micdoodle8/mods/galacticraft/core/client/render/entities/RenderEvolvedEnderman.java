package micdoodle8.mods.galacticraft.core.client.render.entities;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanEyes;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedEndermanHeldBlock;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEvolvedEnderman extends RenderLiving<EntityEvolvedEnderman>
{
    private static final ResourceLocation endermanTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/evolved_enderman.png");
    private ModelEvolvedEnderman endermanModel;
    private Random rnd = new Random();
    private boolean texSwitch;

    public RenderEvolvedEnderman(RenderManager manager)
    {
        super(manager, new ModelEvolvedEnderman(), 0.5F);
        this.endermanModel = (ModelEvolvedEnderman)super.mainModel;
        this.addLayer(new LayerEvolvedEndermanEyes(this));
        this.addLayer(new LayerEvolvedEndermanHeldBlock(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedEnderman entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedEnderman.endermanTexture;
    }

    @Override
    public void doRender(EntityEvolvedEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        IBlockState iblockstate = entity.getHeldBlockState();
        this.endermanModel.isCarrying = iblockstate != null;
        this.endermanModel.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            double d3 = 0.02D;
            x += this.rnd.nextGaussian() * d3;
            z += this.rnd.nextGaussian() * d3;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }
    
    @Override
    protected void preRenderCallback(EntityEvolvedEnderman entity, float partialTickTime)
    {
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}