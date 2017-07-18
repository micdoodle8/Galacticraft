package micdoodle8.mods.galacticraft.core.client.render.entities;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelSpaceChicken;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceChicken;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSpaceChicken extends RenderLiving<EntityEvolvedEnderman>
{
    private static final ResourceLocation chickenTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/space_chicken.png");
    private ModelSpaceChicken chickenModel;
    private Random rnd = new Random();

    public RenderSpaceChicken(RenderManager manager)
    {
        super(manager, new ModelSpaceChicken(), 0.5F);
        this.chickenModel = (ModelSpaceChicken)super.mainModel;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySpaceChicken entity)
    {
        return RenderSpaceChicken.chickenTexture;
    }

    @Override
    public void doRender(EntitySpaceChicken entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
} 
