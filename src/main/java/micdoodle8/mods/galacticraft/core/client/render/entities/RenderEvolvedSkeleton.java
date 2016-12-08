package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSkeleton extends RenderBiped<EntityEvolvedSkeleton>
{
    private static final ResourceLocation skeletonTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/skeleton.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/power.png");

    private final ModelEvolvedSkeleton model = new ModelEvolvedSkeleton(0.2F);
    private static int isBG2Loaded = 0;

    public RenderEvolvedSkeleton(RenderManager manager)
    {
        super(manager, new ModelEvolvedSkeleton(), 1.0F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void func_177177_a()
            {
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

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedSkeleton par1Entity)
    {
        return RenderEvolvedSkeleton.skeletonTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSkeleton par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
    }
}
