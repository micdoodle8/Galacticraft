package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class LayerThermalPadding extends LayerArmorBase<ModelBiped>
{
    private final RenderPlayer renderer;

    public LayerThermalPadding(RenderPlayer playerRendererIn)
    {
        super(playerRendererIn);
        this.renderer = playerRendererIn;
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    @Override
    public ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot)
    {
        PlayerGearData gearData = ModelPlayerGC.getGearData((EntityPlayer) entitylivingbaseIn);

        if (gearData != null)
        {
            int padding = gearData.getThermalPadding(armorSlot - 1);
            if (padding >= 0)
            {
                switch (padding)
                {
                case 0:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, 0);
                case 1:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, 1);
                case 2:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, 2);
                case 3:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, 3);
                default:
                    break;
                }
            }
        }

        return null;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f2, float f3, float partialTicks, float f5, float f6, float f7, float scale)
    {
        this.renderLayerGC(entitylivingbaseIn, f2, f3, partialTicks, f5, f6, f7, scale, 4);
        this.renderLayerGC(entitylivingbaseIn, f2, f3, partialTicks, f5, f6, f7, scale, 3);
        this.renderLayerGC(entitylivingbaseIn, f2, f3, partialTicks, f5, f6, f7, scale, 2);
        this.renderLayerGC(entitylivingbaseIn, f2, f3, partialTicks, f5, f6, f7, scale, 1);
    }

    private void renderLayerGC(EntityLivingBase entitylivingbaseIn, float f2, float f3, float partialTicks, float f5, float f6, float f7, float f8, int armorSlot)
    {
        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);

        if (itemstack != null)
        {
            ModelBiped t = this.func_177175_a(armorSlot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entitylivingbaseIn, f2, f3, partialTicks);
            t = getArmorModelHook(entitylivingbaseIn, itemstack, armorSlot, t);
            this.func_177179_a(t, armorSlot);
            this.renderer.bindTexture(RenderPlayerGC.thermalPaddingTexture1);
            t.render(entitylivingbaseIn, f2, f3, f5, f6, f7, f8);
            this.renderer.bindTexture(RenderPlayerGC.thermalPaddingTexture0);

            // Start alpha render
            GL11.glDisable(GL11.GL_LIGHTING);
            Minecraft.getMinecraft().renderEngine.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            float time = entitylivingbaseIn.ticksExisted / 10.0F;
            float sTime = (float) Math.sin(time) * 0.5F + 0.5F;

            float r = 0.2F * sTime;
            float g = 1.0F * sTime;
            float b = 0.2F * sTime;

            if (entitylivingbaseIn.worldObj.provider instanceof IGalacticraftWorldProvider)
            {
                float modifier = ((IGalacticraftWorldProvider) entitylivingbaseIn.worldObj.provider).getThermalLevelModifier();

                if (modifier > 0)
                {
                    b = g;
                    g = r;
                }
                else if (modifier < 0)
                {
                    r = g;
                    g = b;
                }
            }

            GL11.glColor4f(r, g, b, 0.4F * sTime);
            t.render(entitylivingbaseIn, f2, f3, f5, f6, f7, f8);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    @Override
    protected void initArmor()
    {
        this.field_177189_c = new ModelPlayerGC(0.1F, false);
        this.field_177186_d = new ModelPlayerGC(0.2F, false);
    }

    @Override
    public ModelBiped func_177175_a(int slot)
    {
        return slot == 1 ? this.field_177189_c : this.field_177186_d;
    }

    @Override
    protected void func_177179_a(ModelBiped model, int armorSlot)
    {
        model.setInvisible(false);

        switch (armorSlot)
        {
        case 4:
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        case 3:
//            model.bipedBody.showModel = true;
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        case 2:
            model.bipedBody.showModel = true;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            break;
        case 1:
            model.bipedHead.showModel = true;
            model.bipedHeadwear.showModel = true;
        }
    }
}
