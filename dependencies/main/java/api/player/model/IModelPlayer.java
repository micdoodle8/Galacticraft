package api.player.model;

public interface IModelPlayer
{
	ModelPlayerBase getModelPlayerBase(String baseId);

	java.util.Set<String> getModelPlayerBaseIds();

	float getExpandParameter();

	String getModelPlayerType();

	Object dynamic(String key, Object[] parameters);

	net.minecraft.client.model.ModelRenderer realGetRandomModelBox(java.util.Random paramRandom);

	net.minecraft.client.model.ModelRenderer superGetRandomModelBox(java.util.Random paramRandom);

	net.minecraft.client.model.ModelRenderer localGetRandomModelBox(java.util.Random paramRandom);

	net.minecraft.client.model.TextureOffset realGetTextureOffset(String paramString);

	net.minecraft.client.model.TextureOffset superGetTextureOffset(String paramString);

	net.minecraft.client.model.TextureOffset localGetTextureOffset(String paramString);

	void realRender(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void superRender(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void localRender(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

	void realRenderCloak(float paramFloat);

	void superRenderCloak(float paramFloat);

	void localRenderCloak(float paramFloat);

	void realRenderEars(float paramFloat);

	void superRenderEars(float paramFloat);

	void localRenderEars(float paramFloat);

	void realSetLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3);

	void superSetLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3);

	void localSetLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3);

	void realSetRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity);

	void superSetRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity);

	void localSetRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity);

	void realSetTextureOffset(String paramString, int paramInt1, int paramInt2);

	void superSetTextureOffset(String paramString, int paramInt1, int paramInt2);

	void localSetTextureOffset(String paramString, int paramInt1, int paramInt2);

	boolean getAimedBowField();

	void setAimedBowField(boolean aimedBow);

	net.minecraft.client.model.ModelRenderer getBipedBodyField();

	void setBipedBodyField(net.minecraft.client.model.ModelRenderer bipedBody);

	net.minecraft.client.model.ModelRenderer getBipedCloakField();

	void setBipedCloakField(net.minecraft.client.model.ModelRenderer bipedCloak);

	net.minecraft.client.model.ModelRenderer getBipedEarsField();

	void setBipedEarsField(net.minecraft.client.model.ModelRenderer bipedEars);

	net.minecraft.client.model.ModelRenderer getBipedHeadField();

	void setBipedHeadField(net.minecraft.client.model.ModelRenderer bipedHead);

	net.minecraft.client.model.ModelRenderer getBipedHeadwearField();

	void setBipedHeadwearField(net.minecraft.client.model.ModelRenderer bipedHeadwear);

	net.minecraft.client.model.ModelRenderer getBipedLeftArmField();

	void setBipedLeftArmField(net.minecraft.client.model.ModelRenderer bipedLeftArm);

	net.minecraft.client.model.ModelRenderer getBipedLeftLegField();

	void setBipedLeftLegField(net.minecraft.client.model.ModelRenderer bipedLeftLeg);

	net.minecraft.client.model.ModelRenderer getBipedRightArmField();

	void setBipedRightArmField(net.minecraft.client.model.ModelRenderer bipedRightArm);

	net.minecraft.client.model.ModelRenderer getBipedRightLegField();

	void setBipedRightLegField(net.minecraft.client.model.ModelRenderer bipedRightLeg);

	java.util.List<?> getBoxListField();

	void setBoxListField(java.util.List<?> boxList);

	int getHeldItemLeftField();

	void setHeldItemLeftField(int heldItemLeft);

	int getHeldItemRightField();

	void setHeldItemRightField(int heldItemRight);

	boolean getIsChildField();

	void setIsChildField(boolean isChild);

	boolean getIsRidingField();

	void setIsRidingField(boolean isRiding);

	boolean getIsSneakField();

	void setIsSneakField(boolean isSneak);

	float getOnGroundField();

	void setOnGroundField(float onGround);

	int getTextureHeightField();

	void setTextureHeightField(int textureHeight);

	int getTextureWidthField();

	void setTextureWidthField(int textureWidth);

}
