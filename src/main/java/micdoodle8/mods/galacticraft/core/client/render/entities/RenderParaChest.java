package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderParaChest extends Render<EntityParachest>
{
    private static final ResourceLocation[] textures = { new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/plain.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/black.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/blue.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/lime.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/brown.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/darkblue.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/darkgray.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/darkgreen.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/gray.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/magenta.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/orange.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/pink.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/purple.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/red.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/teal.png"),
                                                    new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachute/yellow.png") };
    public static final ResourceLocation parachestTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachest.png");

    private final ModelParaChest chestModel;

    public RenderParaChest(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1F;
        this.chestModel = new ModelParaChest();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityParachest entity)
    {
        return RenderParaChest.textures[ItemParaChute.getParachuteDamageValueFromDyeEnum(entity.color)];
    }

    @Override
    public void doRender(EntityParachest entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x - 0.5F, (float) y, (float) z);

        this.bindEntityTexture(entity);

        if (!entity.isDead)
        {
            this.chestModel.renderAll();
        }

        GL11.glPopMatrix();
    }
}
