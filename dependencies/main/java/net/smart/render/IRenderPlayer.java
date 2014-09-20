package net.smart.render;

import net.minecraft.client.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.player.*;

public interface IRenderPlayer
{
	IModelPlayer createModel(ModelBiped existing, float f);

	void initialize(ModelBiped modelBipedMain, ModelBiped modelArmorChestplate, ModelBiped modelArmor, float shadowSize);

	void superRenderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks);

	void superDrawFirstPersonHand(EntityPlayer entityPlayer);

	void superRotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2);

	void superRenderSpecials(AbstractClientPlayer entityplayer, float f);

	RenderManager getRenderManager();

	ModelBiped getModelBipedMain();

	ModelBiped getModelArmorChestplate();

	ModelBiped getModelArmor();

	IModelPlayer[] getRenderModels();
}
