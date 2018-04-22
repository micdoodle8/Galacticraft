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
import net.minecraft.inventory.EntityEquipmentSlot;
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
    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn)
    {
        model.setVisible(false);

        switch (slotIn)
        {
        case HEAD:
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        case CHEST:
            model.bipedRightLeg.showModel = true;
            model.bipedLeftLeg.showModel = true;
            break;
        case LEGS:
            model.bipedBody.showModel = true;
            model.bipedRightArm.showModel = true;
            model.bipedLeftArm.showModel = true;
            break;
        case FEET:
            model.bipedHead.showModel = true;
            model.bipedHeadwear.showModel = true;
        }
    }

    public ItemStack getItemStackFromSlot(EntityLivingBase living, EntityEquipmentSlot slotIn)
    {
        PlayerGearData gearData = GalacticraftCore.proxy.getGearData((EntityPlayer) living);

        if (gearData != null)
        {
            int padding = gearData.getThermalPadding(slotIn.getSlotIndex() - 1);
            if (padding != GCPlayerHandler.GEAR_NOT_PRESENT)
            {
                switch (padding)
                {
                case Constants.GEAR_ID_THERMAL_PADDING_T1_HELMET:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_CHESTPLATE:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_LEGGINGS:
                case Constants.GEAR_ID_THERMAL_PADDING_T1_BOOTS:
                    return new ItemStack(AsteroidsItems.thermalPadding, 1, slotIn.getSlotIndex() - 1);
                case Constants.GEAR_ID_THERMAL_PADDING_T2_HELMET:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_CHESTPLATE:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_LEGGINGS:
                case Constants.GEAR_ID_THERMAL_PADDING_T2_BOOTS:
                    return new ItemStack(VenusItems.thermalPaddingTier2, 1, slotIn.getSlotIndex() - 1);
                default:
                    break;
                }
            }
        }

        return null;   //This null is OK, it's used only as flag by calling code in this same class
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
        this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);
    }

    private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
    {
        ItemStack itemstack = this.getItemStackFromSlot(entityLivingBaseIn, slotIn);

        if (itemstack != null)
        {
            ModelBiped model = this.getModelFromSlot(slotIn);
            model.setModelAttributes(this.renderer.getMainModel());
            model.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.setModelSlotVisible(model, slotIn);
            this.renderer.bindTexture(itemstack.getItem() instanceof ItemThermalPaddingTier2 ? RenderPlayerGC.thermalPaddingTexture1_T2 : RenderPlayerGC.thermalPaddingTexture1);
            model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            // Start alpha render
            GlStateManager.disableLighting();
            Minecraft.getMinecraft().renderEngine.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            float time = entityLivingBaseIn.ticksExisted / 10.0F;
            float sTime = (float) Math.sin(time) * 0.5F + 0.5F;

            float r = 0.2F * sTime;
            float g = 1.0F * sTime;
            float b = 0.2F * sTime;

            if (entityLivingBaseIn.world.provider instanceof IGalacticraftWorldProvider)
            {
                float modifier = ((IGalacticraftWorldProvider) entityLivingBaseIn.world.provider).getThermalLevelModifier();

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
            model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableLighting();
        }
    }

    @Override
    protected void initArmor()
    {
        this.modelLeggings = new ModelPlayerGC(0.55F, false);  //Head inside Oxygen Mask
        this.modelArmor = new ModelPlayerGC(0.05F, false);  //Chest and limbs close to skin
    }

    @Override
    public ModelBiped getModelFromSlot(EntityEquipmentSlot slotIn)
    {
        return slotIn == EntityEquipmentSlot.FEET ? this.modelLeggings : this.modelArmor;  //FEET is intended here, actually picks up the helmet (yes really)
    }
}
