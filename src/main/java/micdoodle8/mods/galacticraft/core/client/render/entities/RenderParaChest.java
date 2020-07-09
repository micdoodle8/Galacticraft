package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderParaChest extends EntityRenderer<EntityParachest>
{
    private static final ResourceLocation[] textures = {new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/plain.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/orange.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/magenta.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/blue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/yellow.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/lime.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/pink.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkgray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/gray.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/teal.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/purple.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkblue.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/brown.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/darkgreen.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/red.png"),
            new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachute/black.png")};
    public static final ResourceLocation PARACHEST_TEXTURE = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/parachest.png");

    private final ModelParaChest chestModel;

    public RenderParaChest(EntityRendererManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.chestModel = new ModelParaChest();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityParachest entity)
    {
        return RenderParaChest.textures[entity.color.ordinal()];
    }

    @Override
    public void doRender(EntityParachest entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x - 0.5F, (float) y, (float) z);

        this.bindEntityTexture(entity);

        if (entity.isAlive())
        {
            this.chestModel.renderAll();
        }

        GL11.glPopMatrix();
    }
}
