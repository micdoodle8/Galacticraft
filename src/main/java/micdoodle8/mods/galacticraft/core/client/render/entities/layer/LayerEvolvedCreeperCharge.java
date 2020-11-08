package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerEvolvedCreeperCharge extends EnergyLayer<EntityEvolvedCreeper, ModelEvolvedCreeper>
{
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final ModelEvolvedCreeper creeperModel = new ModelEvolvedCreeper(2.0F);

    public LayerEvolvedCreeperCharge(IEntityRenderer<EntityEvolvedCreeper, ModelEvolvedCreeper> renderer) {
        super(renderer);
    }

    @Override
    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    @Override
    protected ResourceLocation func_225633_a_() {
        return LIGHTNING_TEXTURE;
    }

    @Override
    protected ModelEvolvedCreeper func_225635_b_() {
        return this.creeperModel;
    }
}
