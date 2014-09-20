package net.smart.render;

import net.smart.utilities.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class RenderPlayer extends net.minecraft.client.renderer.entity.RenderPlayer implements IRenderPlayer
{
	private ModelBiped modelArmorChestplate;
    private ModelBiped modelArmor;

	public RenderPlayer()
	{
		render = new SmartRenderRender(this);
	}

	public IModelPlayer createModel(ModelBiped existing, float f)
	{
		return new ModelPlayer(f);
	}

	public void initialize(ModelBiped modelBipedMain, ModelBiped modelArmorChestplate, ModelBiped modelArmor, float shadowSize)
	{
		this.mainModel = modelBipedMain;
		this.shadowSize = shadowSize;

		Reflect.SetField(net.minecraft.client.renderer.entity.RenderPlayer.class, this, SmartRenderInstall.RenderPlayer_modelBipedMain, modelBipedMain);
		Reflect.SetField(net.minecraft.client.renderer.entity.RenderPlayer.class, this, SmartRenderInstall.RenderPlayer_modelArmorChestplate, this.modelArmorChestplate = modelArmorChestplate);
		Reflect.SetField(net.minecraft.client.renderer.entity.RenderPlayer.class, this, SmartRenderInstall.RenderPlayer_modelArmor, this.modelArmor = modelArmor);
	}

	@Override
	public void doRender(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		render.renderPlayer(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	public void superRenderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		super.doRender(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer entityPlayer)
	{
		render.drawFirstPersonHand(entityPlayer);
	}

	public void superDrawFirstPersonHand(EntityPlayer entityPlayer)
	{
		super.renderFirstPersonArm(entityPlayer);
	}

	@Override
	protected void rotateCorpse(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		render.rotatePlayer(entityplayer, totalTime, actualRotation, f2);
	}

	public void superRotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		super.rotateCorpse(entityplayer, totalTime, actualRotation, f2);
	}

	@Override
	protected void preRenderCallback(AbstractClientPlayer entityplayer, float f)
	{
		render.renderSpecials(entityplayer, f);
	}

	public void superRenderSpecials(AbstractClientPlayer entityplayer, float f)
	{
		super.preRenderCallback(entityplayer, f);
	}

	@Override
	protected float handleRotationFloat(EntityLivingBase entityliving, float f)
	{
		render.beforeHandleRotationFloat(entityliving, f);
		float result = super.handleRotationFloat(entityliving, f);
		render.afterHandleRotationFloat(entityliving, f);
		return result;
	}

	public RenderManager getRenderManager()
	{
		return renderManager;
	}

	public ModelBiped getModelBipedMain()
	{
		return (ModelBiped)mainModel;
	}

	public ModelBiped getModelArmorChestplate()
	{
		return modelArmorChestplate;
	}

	public ModelBiped getModelArmor()
	{
		return modelArmor;
	}

	public IModelPlayer getRenderModelBipedMain()
	{
		return (ModelPlayer)getModelBipedMain();
	}

	public IModelPlayer getRenderModelArmorChestplate()
	{
		return (ModelPlayer)getModelArmorChestplate();
	}

	public IModelPlayer getRenderModelArmor()
	{
		return (ModelPlayer)getModelArmor();
	}

	public IModelPlayer[] getRenderModels()
	{
		if(allIModelPlayers == null)
			allIModelPlayers = new IModelPlayer[] { getRenderModelBipedMain(), getRenderModelArmorChestplate(), getRenderModelArmor() };
		return allIModelPlayers;
	}

	private IModelPlayer[] allIModelPlayers;

	private final SmartRenderRender render;
}
