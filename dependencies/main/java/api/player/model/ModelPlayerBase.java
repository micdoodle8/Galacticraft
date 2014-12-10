package api.player.model;

public abstract class ModelPlayerBase
{
	public ModelPlayerBase(ModelPlayerAPI modelPlayerAPI)
	{
		this.internalModelPlayerAPI = modelPlayerAPI;
		this.modelPlayerAPI = modelPlayerAPI.modelPlayer;
		this.modelPlayer = modelPlayerAPI.modelPlayer.getModelPlayer();
	}

	public void beforeBaseAttach(boolean onTheFly)
	{
	}

	public void afterBaseAttach(boolean onTheFly)
	{
	}

	public void beforeLocalConstructing(float paramFloat)
	{
	}

	public void afterLocalConstructing(float paramFloat)
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
		return internalModelPlayerAPI.dynamicOverwritten(key, parameters, this);
	}

	public final int hashCode()
	{
		return super.hashCode();
	}

	public void beforeGetRandomModelBox(java.util.Random paramRandom)
	{
	}

	public net.minecraft.client.model.ModelRenderer getRandomModelBox(java.util.Random paramRandom)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenGetRandomModelBox(this);

		net.minecraft.client.model.ModelRenderer _result;
		if(overwritten == null)
			_result = modelPlayerAPI.localGetRandomModelBox(paramRandom);
		else if(overwritten != this)
			_result = overwritten.getRandomModelBox(paramRandom);
		else
			_result = null;

		return _result;
	}

	public void afterGetRandomModelBox(java.util.Random paramRandom)
	{
	}

	public void beforeGetTextureOffset(String paramString)
	{
	}

	public net.minecraft.client.model.TextureOffset getTextureOffset(String paramString)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenGetTextureOffset(this);

		net.minecraft.client.model.TextureOffset _result;
		if(overwritten == null)
			_result = modelPlayerAPI.localGetTextureOffset(paramString);
		else if(overwritten != this)
			_result = overwritten.getTextureOffset(paramString);
		else
			_result = null;

		return _result;
	}

	public void afterGetTextureOffset(String paramString)
	{
	}

	public void beforeRender(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
	}

	public void render(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenRender(this);

		if(overwritten == null)
			modelPlayerAPI.localRender(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
		else if(overwritten != this)
			overwritten.render(paramEntity, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);

	}

	public void afterRender(net.minecraft.entity.Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
	{
	}

	public void beforeRenderCloak(float paramFloat)
	{
	}

	public void renderCloak(float paramFloat)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenRenderCloak(this);

		if(overwritten == null)
			modelPlayerAPI.localRenderCloak(paramFloat);
		else if(overwritten != this)
			overwritten.renderCloak(paramFloat);

	}

	public void afterRenderCloak(float paramFloat)
	{
	}

	public void beforeRenderEars(float paramFloat)
	{
	}

	public void renderEars(float paramFloat)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenRenderEars(this);

		if(overwritten == null)
			modelPlayerAPI.localRenderEars(paramFloat);
		else if(overwritten != this)
			overwritten.renderEars(paramFloat);

	}

	public void afterRenderEars(float paramFloat)
	{
	}

	public void beforeSetLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void setLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenSetLivingAnimations(this);

		if(overwritten == null)
			modelPlayerAPI.localSetLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);
		else if(overwritten != this)
			overwritten.setLivingAnimations(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);

	}

	public void afterSetLivingAnimations(net.minecraft.entity.EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
	{
	}

	public void beforeSetRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
	}

	public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenSetRotationAngles(this);

		if(overwritten == null)
			modelPlayerAPI.localSetRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
		else if(overwritten != this)
			overwritten.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);

	}

	public void afterSetRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
	}

	public void beforeSetTextureOffset(String paramString, int paramInt1, int paramInt2)
	{
	}

	public void setTextureOffset(String paramString, int paramInt1, int paramInt2)
	{
		ModelPlayerBase overwritten = internalModelPlayerAPI.GetOverwrittenSetTextureOffset(this);

		if(overwritten == null)
			modelPlayerAPI.localSetTextureOffset(paramString, paramInt1, paramInt2);
		else if(overwritten != this)
			overwritten.setTextureOffset(paramString, paramInt1, paramInt2);

	}

	public void afterSetTextureOffset(String paramString, int paramInt1, int paramInt2)
	{
	}

	protected final api.player.model.ModelPlayer modelPlayer;
	protected final IModelPlayer modelPlayerAPI;
	private final ModelPlayerAPI internalModelPlayerAPI;
}
