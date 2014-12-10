package api.player.render;

public interface IRenderPlayer
{
	RenderPlayerBase getRenderPlayerBase(String baseId);

	java.util.Set<String> getRenderPlayerBaseIds();

	Object dynamic(String key, Object[] parameters);

	boolean realDoRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	boolean superDoRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	boolean localDoRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	void realDoRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void superDoRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void localDoRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	int realGetColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2);

	int superGetColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2);

	int localGetColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2);

	float realGetDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	float superGetDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	float localGetDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	net.minecraft.client.gui.FontRenderer realGetFontRendererFromRenderManager();

	net.minecraft.client.gui.FontRenderer superGetFontRendererFromRenderManager();

	net.minecraft.client.gui.FontRenderer localGetFontRendererFromRenderManager();

	net.minecraft.util.ResourceLocation realGetResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer);

	net.minecraft.util.ResourceLocation localGetResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer);

	float realHandleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	float superHandleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	float localHandleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	int realInheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat);

	int superInheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat);

	int localInheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat);

	void realLoadTexture(net.minecraft.util.ResourceLocation paramResourceLocation);

	void superLoadTexture(net.minecraft.util.ResourceLocation paramResourceLocation);

	void localLoadTexture(net.minecraft.util.ResourceLocation paramResourceLocation);

	void realLoadTextureOfEntity(net.minecraft.entity.Entity paramEntity);

	void superLoadTextureOfEntity(net.minecraft.entity.Entity paramEntity);

	void localLoadTextureOfEntity(net.minecraft.entity.Entity paramEntity);

	void realPassSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3);

	void superPassSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3);

	void localPassSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3);

	boolean realPerformStaticEntityRebuild();

	boolean superPerformStaticEntityRebuild();

	boolean localPerformStaticEntityRebuild();

	void realRenderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	void superRenderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	void localRenderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	void realRenderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer);

	void localRenderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer);

	void realRenderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt);

	void superRenderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt);

	void localRenderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt);

	void realRenderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void superRenderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void localRenderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void realRenderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void localRenderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void realRenderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4);

	void localRenderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4);

	void realRenderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat);

	void localRenderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat);

	void realRenderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3);

	void localRenderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3);

	void realRenderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat);

	void localRenderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat);

	float realRenderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	float superRenderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	float localRenderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat);

	void realRotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3);

	void localRotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3);

	int realSetArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat);

	int localSetArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat);

	void realSetPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat);

	void localSetPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat);

	void realSetRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager);

	void superSetRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager);

	void localSetRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager);

	void realSetRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase);

	void superSetRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase);

	void localSetRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase);

	void realUpdateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister);

	void superUpdateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister);

	void localUpdateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister);

	net.minecraft.client.model.ModelBase getMainModelField();

	void setMainModelField(net.minecraft.client.model.ModelBase mainModel);

	net.minecraft.client.model.ModelBiped getModelArmorField();

	void setModelArmorField(net.minecraft.client.model.ModelBiped modelArmor);

	net.minecraft.client.model.ModelBiped getModelArmorChestplateField();

	void setModelArmorChestplateField(net.minecraft.client.model.ModelBiped modelArmorChestplate);

	net.minecraft.client.model.ModelBiped getModelBipedMainField();

	void setModelBipedMainField(net.minecraft.client.model.ModelBiped modelBipedMain);

	net.minecraft.client.renderer.RenderBlocks getRenderBlocksField();

	void setRenderBlocksField(net.minecraft.client.renderer.RenderBlocks renderBlocks);

	net.minecraft.client.renderer.entity.RenderManager getRenderManagerField();

	void setRenderManagerField(net.minecraft.client.renderer.entity.RenderManager renderManager);

	net.minecraft.client.model.ModelBase getRenderPassModelField();

	void setRenderPassModelField(net.minecraft.client.model.ModelBase renderPassModel);

	float getShadowOpaqueField();

	void setShadowOpaqueField(float shadowOpaque);

	float getShadowSizeField();

	void setShadowSizeField(float shadowSize);

	net.minecraft.util.ResourceLocation getSteveTexturesField();

}
