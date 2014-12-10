package net.smart.render;

import java.util.*;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

import net.smart.render.statistics.*;

public class SmartRenderRender extends SmartRenderContext
{
	public IRenderPlayer irp;

	public SmartRenderRender(IRenderPlayer irp)
	{
		this.irp = irp;

		modelBipedMain = irp.createModel(irp.getModelBipedMain(), 0.0F).getRenderModel();
		SmartRenderModel modelArmorChestplate = irp.createModel(irp.getModelArmorChestplate(), 1.0F).getRenderModel();
		SmartRenderModel modelArmor = irp.createModel(irp.getModelArmor(), 0.5F).getRenderModel();

		irp.initialize(modelBipedMain.mp, modelArmorChestplate.mp, modelArmor.mp, 0.5F);
	}

	public void renderPlayer(AbstractClientPlayer entityplayer, double d, double d1, double d2, float f, float renderPartialTicks)
	{
		SmartStatistics statistics = SmartStatisticsFactory.getInstance(entityplayer);
		if(statistics != null)
		{
			boolean isInventory = d == 0.0F && d1 == 0.0F && d2 == 0.0F && f == 0.0F && renderPartialTicks == 1.0F;
			boolean isSleeping = entityplayer.isPlayerSleeping();

			float totalVerticalDistance = statistics.getTotalVerticalDistance(renderPartialTicks);
			float currentVerticalSpeed = statistics.getCurrentVerticalSpeed(renderPartialTicks);
			float totalDistance = statistics.getTotalDistance(renderPartialTicks);
			float currentSpeed = statistics.getCurrentSpeed(renderPartialTicks);

			double distance = 0;
			double verticalDistance = 0;
			double horizontalDistance = 0;
			float currentCameraAngle = 0;
			float currentVerticalAngle = 0;
			float currentHorizontalAngle = 0;

			if (!isInventory)
			{
				double xDiff = entityplayer.posX - entityplayer.prevPosX;
				double yDiff = entityplayer.posY - entityplayer.prevPosY;
				double zDiff = entityplayer.posZ - entityplayer.prevPosZ;

				verticalDistance = Math.abs(yDiff);
				horizontalDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
				distance = Math.sqrt(horizontalDistance * horizontalDistance + verticalDistance * verticalDistance);

				currentCameraAngle = entityplayer.rotationYaw / RadiantToAngle;
				currentVerticalAngle = (float)Math.atan(yDiff / horizontalDistance);
				if(Float.isNaN(currentVerticalAngle))
					currentVerticalAngle = Quarter;

				currentHorizontalAngle = (float)-Math.atan(xDiff / zDiff);
				if (Float.isNaN(currentHorizontalAngle))
					if(Float.isNaN(statistics.prevHorizontalAngle))
						currentHorizontalAngle = currentCameraAngle;
					else
						currentHorizontalAngle = statistics.prevHorizontalAngle;
				else if (zDiff < 0)
					currentHorizontalAngle += Half;

				statistics.prevHorizontalAngle = currentHorizontalAngle;
			}

			IModelPlayer[] modelPlayers = irp.getRenderModels();

			for(int i = 0; i < modelPlayers.length; i++)
			{
				SmartRenderModel modelPlayer = modelPlayers[i].getRenderModel();

				modelPlayer.isInventory = isInventory;

				modelPlayer.totalVerticalDistance = totalVerticalDistance;
				modelPlayer.currentVerticalSpeed = currentVerticalSpeed;
				modelPlayer.totalDistance = totalDistance;
				modelPlayer.currentSpeed = currentSpeed;

				modelPlayer.distance = distance;
				modelPlayer.verticalDistance = verticalDistance;
				modelPlayer.horizontalDistance = horizontalDistance;
				modelPlayer.currentCameraAngle = currentCameraAngle;
				modelPlayer.currentVerticalAngle = currentVerticalAngle;
				modelPlayer.currentHorizontalAngle = currentHorizontalAngle;
				modelPlayer.prevOuterRenderData = getPreviousRendererData(entityplayer);
				modelPlayer.isSleeping = isSleeping;
			}
		}

		irp.superRenderPlayer(entityplayer, d, d1, d2, f, renderPartialTicks);
	}

	public void drawFirstPersonHand(EntityPlayer entityPlayer)
	{
		modelBipedMain.firstPerson = true;
		irp.superDrawFirstPersonHand(entityPlayer);
		modelBipedMain.firstPerson = false;
	}

	public void rotatePlayer(AbstractClientPlayer entityplayer, float totalTime, float actualRotation, float f2)
	{
		boolean isLocal = entityplayer instanceof EntityPlayerSP;
		boolean isInventory = f2 == 1.0F && isLocal && Minecraft.getMinecraft().currentScreen instanceof GuiInventory;
		if(!isInventory)
		{
			float forwardRotation = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f2;

			if(entityplayer.isPlayerSleeping())
			{
				actualRotation = 0;
				forwardRotation = 0;
			}

			float workingAngle;
			Minecraft minecraft = Minecraft.getMinecraft();
			if(!isLocal)
			{
				workingAngle = -entityplayer.rotationYaw;
				workingAngle += minecraft.renderViewEntity.rotationYaw;
			}
			else
				workingAngle = actualRotation - getPreviousRendererData(entityplayer).rotateAngleY * RadiantToAngle;

			if(minecraft.gameSettings.thirdPersonView == 2 && !minecraft.renderViewEntity.isPlayerSleeping())
				workingAngle += 180F;

			IModelPlayer[] modelPlayers = irp.getRenderModels();

			for(int i = 0; i < modelPlayers.length; i++)
			{
				SmartRenderModel modelPlayer = modelPlayers[i].getRenderModel();

				modelPlayer.actualRotation = actualRotation;
				modelPlayer.forwardRotation = forwardRotation;
				modelPlayer.workingAngle = workingAngle;
			}

			actualRotation = 0;
		}

		irp.superRotatePlayer(entityplayer, totalTime, actualRotation, f2);
	}

	public void renderSpecials(AbstractClientPlayer entityplayer, float f)
	{
		modelBipedMain.bipedEars.beforeRender();
		modelBipedMain.bipedCloak.beforeRender(entityplayer, f);
		irp.superRenderSpecials(entityplayer, f);
		modelBipedMain.bipedCloak.afterRender();
		modelBipedMain.bipedEars.afterRender();
	}

	public void beforeHandleRotationFloat(EntityLivingBase entityliving, float f)
	{
		if(entityliving instanceof EntityPlayer)
		{
			SmartStatistics statistics = SmartStatisticsFactory.getInstance((EntityPlayer)entityliving);
			if (statistics != null)
				entityliving.ticksExisted += statistics.ticksRiding;
		}
	}

    public void afterHandleRotationFloat(EntityLivingBase entityliving, float f)
    {
    	if(entityliving instanceof EntityPlayer)
		{
			SmartStatistics statistics = SmartStatisticsFactory.getInstance((EntityPlayer)entityliving);
			if (statistics != null)
				entityliving.ticksExisted -= statistics.ticksRiding;
		}
    }

	public static RendererData getPreviousRendererData(EntityPlayer entityplayer)
	{
		if(++previousRendererDataAccessCounter > 1000)
		{
			List players = Minecraft.getMinecraft().theWorld.playerEntities;

			Iterator<EntityPlayer> iterator = previousRendererData.keySet().iterator();
			while(iterator.hasNext())
				if(!players.contains(iterator.next()))
					iterator.remove();

			previousRendererDataAccessCounter = 0;
		}

		RendererData result = previousRendererData.get(entityplayer);
		if(result == null)
			previousRendererData.put(entityplayer, result = new RendererData());
		return result;
	}

	private static Map<EntityPlayer, RendererData> previousRendererData = new HashMap<EntityPlayer, RendererData>();
	private static int previousRendererDataAccessCounter = 0;

	public final SmartRenderModel modelBipedMain;
}
