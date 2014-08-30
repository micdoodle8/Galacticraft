package api.player.render;

public abstract class RenderPlayerBase
{
	public RenderPlayerBase(RenderPlayerAPI renderPlayerAPI)
	{
		this.internalRenderPlayerAPI = renderPlayerAPI;
		this.renderPlayerAPI = renderPlayerAPI.renderPlayer;
		this.renderPlayer = renderPlayerAPI.renderPlayer.getRenderPlayer();
	}

	public void beforeBaseAttach(boolean onTheFly)
	{
	}

	public void afterBaseAttach(boolean onTheFly)
	{
	}

	public void beforeLocalConstructing()
	{
	}

	public void afterLocalConstructing()
	{
	}

	public void beforeBaseDetach(boolean onTheFly)
	{
	}

	public void afterBaseDetach(boolean onTheFly)
	{
	}

	public Object dynamic(String key, Object[] parameters)
	{
		return internalRenderPlayerAPI.dynamicOverwritten(key, parameters, this);
	}

	public final int hashCode()
	{
		return super.hashCode();
	}

	public void beforeDoRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public boolean doRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenDoRenderLabel(this);

		boolean _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localDoRenderLabel(paramEntityLivingBase);
		else if(overwritten != this)
			_result = overwritten.doRenderLabel(paramEntityLivingBase);
		else
			_result = false;

		return _result;
	}

	public void afterDoRenderLabel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public void beforeDoRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void doRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenDoRenderShadowAndFire(this);

		if(overwritten == null)
			renderPlayerAPI.localDoRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else if(overwritten != this)
			overwritten.doRenderShadowAndFire(paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	public void afterDoRenderShadowAndFire(net.minecraft.entity.Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void beforeGetColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2)
	{
	}

	public int getColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenGetColorMultiplier(this);

		int _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localGetColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);
		else if(overwritten != this)
			_result = overwritten.getColorMultiplier(paramEntityLivingBase, paramFloat1, paramFloat2);
		else
			_result = 0;

		return _result;
	}

	public void afterGetColorMultiplier(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2)
	{
	}

	public void beforeGetDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public float getDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenGetDeathMaxRotation(this);

		float _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localGetDeathMaxRotation(paramEntityLivingBase);
		else if(overwritten != this)
			_result = overwritten.getDeathMaxRotation(paramEntityLivingBase);
		else
			_result = 0;

		return _result;
	}

	public void afterGetDeathMaxRotation(net.minecraft.entity.EntityLivingBase paramEntityLivingBase)
	{
	}

	public void beforeGetFontRendererFromRenderManager()
	{
	}

	public net.minecraft.client.gui.FontRenderer getFontRendererFromRenderManager()
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenGetFontRendererFromRenderManager(this);

		net.minecraft.client.gui.FontRenderer _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localGetFontRendererFromRenderManager();
		else if(overwritten != this)
			_result = overwritten.getFontRendererFromRenderManager();
		else
			_result = null;

		return _result;
	}

	public void afterGetFontRendererFromRenderManager()
	{
	}

	public void beforeGetResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer)
	{
	}

	public net.minecraft.util.ResourceLocation getResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenGetResourceLocationFromPlayer(this);

		net.minecraft.util.ResourceLocation _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localGetResourceLocationFromPlayer(paramAbstractClientPlayer);
		else if(overwritten != this)
			_result = overwritten.getResourceLocationFromPlayer(paramAbstractClientPlayer);
		else
			_result = null;

		return _result;
	}

	public void afterGetResourceLocationFromPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer)
	{
	}

	public void beforeHandleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public float handleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenHandleRotationFloat(this);

		float _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localHandleRotationFloat(paramEntityLivingBase, paramFloat);
		else if(overwritten != this)
			_result = overwritten.handleRotationFloat(paramEntityLivingBase, paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterHandleRotationFloat(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public void beforeInheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
	{
	}

	public int inheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenInheritRenderPass(this);

		int _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localInheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);
		else if(overwritten != this)
			_result = overwritten.inheritRenderPass(paramEntityLivingBase, paramInt, paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterInheritRenderPass(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
	{
	}

	public void beforeLoadTexture(net.minecraft.util.ResourceLocation paramResourceLocation)
	{
	}

	public void loadTexture(net.minecraft.util.ResourceLocation paramResourceLocation)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenLoadTexture(this);

		if(overwritten == null)
			renderPlayerAPI.localLoadTexture(paramResourceLocation);
		else if(overwritten != this)
			overwritten.loadTexture(paramResourceLocation);

	}

	public void afterLoadTexture(net.minecraft.util.ResourceLocation paramResourceLocation)
	{
	}

	public void beforeLoadTextureOfEntity(net.minecraft.entity.Entity paramEntity)
	{
	}

	public void loadTextureOfEntity(net.minecraft.entity.Entity paramEntity)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenLoadTextureOfEntity(this);

		if(overwritten == null)
			renderPlayerAPI.localLoadTextureOfEntity(paramEntity);
		else if(overwritten != this)
			overwritten.loadTextureOfEntity(paramEntity);

	}

	public void afterLoadTextureOfEntity(net.minecraft.entity.Entity paramEntity)
	{
	}

	public void beforePassSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void passSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenPassSpecialRender(this);

		if(overwritten == null)
			renderPlayerAPI.localPassSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.passSpecialRender(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterPassSpecialRender(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforePerformStaticEntityRebuild()
	{
	}

	public boolean performStaticEntityRebuild()
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenPerformStaticEntityRebuild(this);

		boolean _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localPerformStaticEntityRebuild();
		else if(overwritten != this)
			_result = overwritten.performStaticEntityRebuild();
		else
			_result = false;

		return _result;
	}

	public void afterPerformStaticEntityRebuild()
	{
	}

	public void beforeRenderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public void renderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderArrowsStuckInEntity(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);
		else if(overwritten != this)
			overwritten.renderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);

	}

	public void afterRenderArrowsStuckInEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public void beforeRenderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer)
	{
	}

	public void renderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderFirstPersonArm(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderFirstPersonArm(paramEntityPlayer);
		else if(overwritten != this)
			overwritten.renderFirstPersonArm(paramEntityPlayer);

	}

	public void afterRenderFirstPersonArm(net.minecraft.entity.player.EntityPlayer paramEntityPlayer)
	{
	}

	public void beforeRenderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
	{
	}

	public void renderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderLivingLabel(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);
		else if(overwritten != this)
			overwritten.renderLivingLabel(paramEntity, paramString, paramDouble1, paramDouble2, paramDouble3, paramInt);

	}

	public void afterRenderLivingLabel(net.minecraft.entity.Entity paramEntity, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
	{
	}

	public void beforeRenderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
	}

	public void renderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderModel(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else if(overwritten != this)
			overwritten.renderModel(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

	}

	public void afterRenderModel(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
	}

	public void beforeRenderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void renderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderPlayer(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		else if(overwritten != this)
			overwritten.renderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);

	}

	public void afterRenderPlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
	{
	}

	public void beforeRenderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
	{
	}

	public void renderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderPlayerNameAndScoreLabel(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);
		else if(overwritten != this)
			overwritten.renderPlayerNameAndScoreLabel(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);

	}

	public void afterRenderPlayerNameAndScoreLabel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
	{
	}

	public void beforeRenderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
	}

	public void renderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderPlayerScale(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderPlayerScale(paramAbstractClientPlayer, paramFloat);
		else if(overwritten != this)
			overwritten.renderPlayerScale(paramAbstractClientPlayer, paramFloat);

	}

	public void afterRenderPlayerScale(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
	}

	public void beforeRenderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void renderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderPlayerSleep(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);
		else if(overwritten != this)
			overwritten.renderPlayerSleep(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3);

	}

	public void afterRenderPlayerSleep(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3)
	{
	}

	public void beforeRenderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
	}

	public void renderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderSpecials(this);

		if(overwritten == null)
			renderPlayerAPI.localRenderSpecials(paramAbstractClientPlayer, paramFloat);
		else if(overwritten != this)
			overwritten.renderSpecials(paramAbstractClientPlayer, paramFloat);

	}

	public void afterRenderSpecials(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat)
	{
	}

	public void beforeRenderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public float renderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRenderSwingProgress(this);

		float _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localRenderSwingProgress(paramEntityLivingBase, paramFloat);
		else if(overwritten != this)
			_result = overwritten.renderSwingProgress(paramEntityLivingBase, paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterRenderSwingProgress(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat)
	{
	}

	public void beforeRotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void rotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenRotatePlayer(this);

		if(overwritten == null)
			renderPlayerAPI.localRotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);
		else if(overwritten != this)
			overwritten.rotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);

	}

	public void afterRotatePlayer(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void beforeSetArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
	}

	public int setArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenSetArmorModel(this);

		int _result;
		if(overwritten == null)
			_result = renderPlayerAPI.localSetArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else if(overwritten != this)
			_result = overwritten.setArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else
			_result = 0;

		return _result;
	}

	public void afterSetArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
	}

	public void beforeSetPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
	}

	public void setPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenSetPassArmorModel(this);

		if(overwritten == null)
			renderPlayerAPI.localSetPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);
		else if(overwritten != this)
			overwritten.setPassArmorModel(paramAbstractClientPlayer, paramInt, paramFloat);

	}

	public void afterSetPassArmorModel(net.minecraft.client.entity.AbstractClientPlayer paramAbstractClientPlayer, int paramInt, float paramFloat)
	{
	}

	public void beforeSetRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager)
	{
	}

	public void setRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenSetRenderManager(this);

		if(overwritten == null)
			renderPlayerAPI.localSetRenderManager(paramRenderManager);
		else if(overwritten != this)
			overwritten.setRenderManager(paramRenderManager);

	}

	public void afterSetRenderManager(net.minecraft.client.renderer.entity.RenderManager paramRenderManager)
	{
	}

	public void beforeSetRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase)
	{
	}

	public void setRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenSetRenderPassModel(this);

		if(overwritten == null)
			renderPlayerAPI.localSetRenderPassModel(paramModelBase);
		else if(overwritten != this)
			overwritten.setRenderPassModel(paramModelBase);

	}

	public void afterSetRenderPassModel(net.minecraft.client.model.ModelBase paramModelBase)
	{
	}

	public void beforeUpdateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister)
	{
	}

	public void updateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister)
	{
		RenderPlayerBase overwritten = internalRenderPlayerAPI.GetOverwrittenUpdateIcons(this);

		if(overwritten == null)
			renderPlayerAPI.localUpdateIcons(paramIIconRegister);
		else if(overwritten != this)
			overwritten.updateIcons(paramIIconRegister);

	}

	public void afterUpdateIcons(net.minecraft.client.renderer.texture.IIconRegister paramIIconRegister)
	{
	}

	protected final net.minecraft.client.renderer.entity.RenderPlayer renderPlayer;
	protected final IRenderPlayer renderPlayerAPI;
	private final RenderPlayerAPI internalRenderPlayerAPI;
}
