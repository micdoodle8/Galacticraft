//package micdoodle8.mods.galacticraft.core.client.render.entities.layer;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerHandler;
//import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
//import net.minecraft.client.renderer.entity.layers.ArmorLayer;
//import net.minecraft.client.renderer.entity.model.PlayerModel;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.inventory.EquipmentSlotType;
//import net.minecraft.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//public class LayerThermalPadding extends ArmorLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>, PlayerModel<AbstractClientPlayerEntity>>
//{
//    private final RenderPlayerGC renderer;
//
//    public LayerThermalPadding(RenderPlayerGC renderer)
//    {
//        super(renderer, new PlayerModel<>(0.55F, false), new PlayerModel<>(0.05F, false));
//        this.renderer = renderer;
//    }
//
//    @Override
//    protected void setModelVisible(PlayerModel<AbstractClientPlayerEntity> model)
//    {
//        model.setVisible(false);
//    }
//
//    @Override
//    public boolean shouldCombineTextures()
//    {
//        return false;
//    }
//
//    @Override
//    protected void setModelSlotVisible(PlayerModel<AbstractClientPlayerEntity> model, EquipmentSlotType slotIn)
//    {
//        setModelVisible(model);
//        switch (slotIn)
//        {
//        case HEAD:
//            model.bipedRightLeg.showModel = true;
//            model.bipedLeftLeg.showModel = true;
//            break;
//        case CHEST:
//            model.bipedRightLeg.showModel = true;
//            model.bipedLeftLeg.showModel = true;
//            break;
//        case LEGS:
//            model.bipedBody.showModel = true;
//            model.bipedRightArm.showModel = true;
//            model.bipedLeftArm.showModel = true;
//            break;
//        case FEET:
//            model.bipedHead.showModel = true;
//            model.bipedHeadwear.showModel = true;
//        }
//    }
//
//    public ItemStack getItemStackFromSlot(LivingEntity living, EquipmentSlotType slotIn)
//    {
//        PlayerGearData gearData = GalacticraftCore.proxy.getGearData((PlayerEntity) living);
//
//        if (gearData != null)
//        {
//            int padding = gearData.getThermalPadding(slotIn.getSlotIndex() - 1);
//            if (padding != GCPlayerHandler.GEAR_NOT_PRESENT)
//            {
////                switch (padding)
////                {
////                case Constants.GEAR_ID_THERMAL_PADDING_T1_HELMET:
////                case Constants.GEAR_ID_THERMAL_PADDING_T1_CHESTPLATE:
////                case Constants.GEAR_ID_THERMAL_PADDING_T1_LEGGINGS:
////                case Constants.GEAR_ID_THERMAL_PADDING_T1_BOOTS:
////                    return new ItemStack(AsteroidsItems.thermalPadding, 1); // TODO Flatten thermal padding
////                case Constants.GEAR_ID_THERMAL_PADDING_T2_HELMET:
////                case Constants.GEAR_ID_THERMAL_PADDING_T2_CHESTPLATE:
////                case Constants.GEAR_ID_THERMAL_PADDING_T2_LEGGINGS:
////                case Constants.GEAR_ID_THERMAL_PADDING_T2_BOOTS:
////                    return new ItemStack(VenusItems.thermalPaddingTier2, 1); // TODO Flatten thermal padding
////                default:
////                    break;
////                }
//            }
//        }
//
//        return null;   //This null is OK, it's used only as flag by calling code in this same class
//    }
//
//    @Override
//    public void render(AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
//    {
//        this.renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.CHEST);
//        this.renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.LEGS);
//        this.renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.FEET);
//        this.renderArmorLayer(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EquipmentSlotType.HEAD);
//    }
//
//    private void renderArmorLayer(AbstractClientPlayerEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EquipmentSlotType slotIn)
//    {
//        ItemStack itemstack = this.getItemStackFromSlot(entityLivingBaseIn, slotIn);
//
//        if (itemstack != null)
//        {
//            PlayerModel<AbstractClientPlayerEntity> model = this.func_215337_a(slotIn);
//            model.setModelAttributes(this.renderer.getEntityModel());
//            model.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
//            this.setModelSlotVisible(model, slotIn);
////            this.renderer.bindTexture(itemstack.getItem() instanceof ItemThermalPaddingTier2 ? RenderPlayerGC.thermalPaddingTexture1_T2 : RenderPlayerGC.thermalPaddingTexture1);
//            model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//
//            // Start alpha render
//            GlStateManager.disableLighting();
//            Minecraft.getInstance().textureManager.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
//            GlStateManager.enableAlphaTest();
//            GlStateManager.enableBlend();
//            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            float time = entityLivingBaseIn.ticksExisted / 10.0F;
//            float sTime = (float) Math.sin(time) * 0.5F + 0.5F;
//
//            float r = 0.2F * sTime;
//            float g = 1.0F * sTime;
//            float b = 0.2F * sTime;
//
//            if (entityLivingBaseIn.world.getDimension() instanceof IGalacticraftDimension)
//            {
//                float modifier = ((IGalacticraftDimension) entityLivingBaseIn.world.getDimension()).getThermalLevelModifier();
//
//                if (modifier > 0)
//                {
//                    b = g;
//                    g = r;
//                }
//                else if (modifier < 0)
//                {
//                    r = g;
//                    g = b;
//                }
//            }
//
//            GlStateManager.color4f(r, g, b, 0.4F * sTime);
//            model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//            GlStateManager.color4f(1, 1, 1, 1);
//            GlStateManager.disableBlend();
//            GlStateManager.enableAlphaTest();
//            GlStateManager.enableLighting();
//        }
//    }
//
//    @Override
//    public PlayerModel<AbstractClientPlayerEntity> func_215337_a(EquipmentSlotType slotIn)
//    {
//        return slotIn == EquipmentSlotType.FEET ? this.modelLeggings : this.modelArmor;  //FEET is intended here, actually picks up the helmet (yes really)
//    }
//}
