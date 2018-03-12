package micdoodle8.mods.galacticraft.core.client.render.entities.layer;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemThermalPaddingTier2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        PlayerGearData gearData = GalacticraftCore.proxy.getGearData((EntityPlayer) entitylivingbaseIn);

        if (gearData != null)
        {
            int padding = gearData.getThermalPadding(armorSlot - 1);
            if (padding != GCPlayerHandler.GEAR_NOT_PRESENT)
            {
                switch (padding)
                {
                case Constants.GEAR_ID_THERMAL_PADDING_T1_HELMET:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_CHESTPLATE:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_LEGGINGS:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_BOOTS:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, armorSlot);
                case Constants.GEAR_ID_THERMAL_PADDING_T2_HELMET:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_CHESTPLATE:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_LEGGINGS:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_BOOTS:
                    return new ItemStack(VenusItems.thermalPaddingTier2, 1, armorSlot);
                default:
                    break;
                }
            }
        }

        return null;
    }

    @Override
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
            this.func_177179_a(t, armorSlot);
            this.renderer.bindTexture(itemstack.getItem() instanceof ItemThermalPaddingTier2 ? RenderPlayerGC.thermalPaddingTexture1_T2 : RenderPlayerGC.thermalPaddingTexture1);
            t.render(entitylivingbaseIn, f2, f3, f5, f6, f7, f8);

            // Start alpha render
            GlStateManager.disableLighting();
            Minecraft.getMinecraft().renderEngine.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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

            GlStateManager.color(r, g, b, 0.4F * sTime);
            t.render(entitylivingbaseIn, f2, f3, f5, f6, f7, f8);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
        }
    }

    @Override
    protected void initArmor()
    {
        this.field_177189_c = new ModelPlayerGC(0.9F, false);  //Head close to helmet
        this.field_177186_d = new ModelPlayerGC(0.05F, false);  //Chest and limbs close to skin
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
