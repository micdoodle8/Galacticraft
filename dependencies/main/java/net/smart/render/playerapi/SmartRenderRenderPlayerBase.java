package net.smart.render.playerapi;

import net.minecraft.client.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

import api.player.render.*;

import net.smart.render.*;
import net.smart.render.IRenderPlayer;

public class SmartRenderRenderPlayerBase extends RenderPlayerBase implements IRenderPlayer
{
	public SmartRenderRenderPlayerBase(RenderPlayerAPI renderPlayerAPI)
	{
		super(renderPlayerAPI);
	}

	public SmartRenderRender getRenderRender()
	{
		if (render == null)
			render = new SmartRenderRender(this);
		return render;
	}

	public IModelPlayer createModel(ModelBiped existing, float f)
	{
		return SmartRender.getPlayerBase((api.player.model.ModelPlayer)existing);
	}

	public void initialize(ModelBiped modelBipedMain, ModelBiped modelArmorChestplate, ModelBiped modelArmor, float shadowSize)
	{
		renderPlayerAPI.setMainModelField(modelBipedMain);
		renderPlayerAPI.setShadowSizeField(0.5F);

		renderPlayerAPI.setModelBipedMainField(modelBipedMain);
		renderPlayerAPI.setModelArmorChestplateField(modelArmorChestplate);
		renderPlayerAPI.setModelArmorField(modelArmor);
	}

	@Override
	public void renderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		getRenderRender().renderPlayer(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	public void superRenderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		super.renderPlayer(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	@Override
	public void renderFirstPersonArm(EntityPlayer entityPlayer)
	{
		getRenderRender().drawFirstPersonHand(entityPlayer);
	}

	public void superDrawFirstPersonHand(EntityPlayer entityPlayer)
	{
		super.renderFirstPersonArm(entityPlayer);
	}

	@Override
	public void rotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		getRenderRender().rotatePlayer(entityplayer, totalTime, actualRotation, f2);
	}

	public void superRotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		super.rotatePlayer(entityplayer, totalTime, actualRotation, f2);
	}

	@Override
	public void renderSpecials(AbstractClientPlayer entityplayer, float f)
	{
		getRenderRender().renderSpecials(entityplayer, f);
	}

	public void superRenderSpecials(AbstractClientPlayer entityplayer, float f)
	{
		super.renderSpecials(entityplayer, f);
	}

	@Override
	public void beforeHandleRotationFloat(EntityLivingBase entityliving, float f)
	{
		getRenderRender().beforeHandleRotationFloat(entityliving, f);
	}

	@Override
	 public void afterHandleRotationFloat(EntityLivingBase entityliving, float f)
    {
		getRenderRender().afterHandleRotationFloat(entityliving, f);
    }

	public RenderManager getRenderManager()
	{
		return renderPlayerAPI.getRenderManagerField();
	}

	public ModelBiped getModelBipedMain()
	{
		return renderPlayerAPI.getModelBipedMainField();
	}

	public ModelBiped getModelArmorChestplate()
	{
		return renderPlayerAPI.getModelArmorChestplateField();
	}

	public ModelBiped getModelArmor()
	{
		return renderPlayerAPI.getModelArmorField();
	}

	public IModelPlayer[] getRenderModels()
	{
		api.player.model.ModelPlayer[] modelPlayers = api.player.model.ModelPlayerAPI.getAllInstances();
		if(allModelPlayers != null && (allModelPlayers == modelPlayers || modelPlayers.length == 0 && allModelPlayers.length == 0))
			return allIModelPlayers;

		allModelPlayers = modelPlayers;
		allIModelPlayers = new IModelPlayer[modelPlayers.length];
		for(int i=0; i<allIModelPlayers.length; i++)
			allIModelPlayers[i] = SmartRender.getPlayerBase(allModelPlayers[i]);
		return allIModelPlayers;
	}

	private api.player.model.ModelPlayer[] allModelPlayers;
	private IModelPlayer[] allIModelPlayers;
	private SmartRenderRender render;
}
