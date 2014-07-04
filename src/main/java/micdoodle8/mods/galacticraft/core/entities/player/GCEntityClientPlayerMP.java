package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.items.ItemBlockLandingPad;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerClient;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Random;

public class GCEntityClientPlayerMP extends EntityClientPlayerMP
{
	private final Random rand = new Random();

	private boolean usingParachute;
	private boolean lastUsingParachute;
	public boolean usingAdvancedGoggles;
	public int thermalLevel;
	private int thirdPersonView = 0;
	public long tick;
	public boolean oxygenSetupValid = true;
	AxisAlignedBB boundingBoxBefore;
	public boolean touchedGround = false;
	public boolean lastOnGround;
	private ResourceLocation galacticraftCape;
	private ThreadDownloadImageData galacticraftCapeImageData;

	private double distanceSinceLastStep;
	private int lastStep;
	public boolean inFreefall;
	public boolean inFreefallFirstCheck;
	public boolean lastRidingCameraZoomEntity;

	public Gravity gdir = Gravity.down;
	public float gravityTurnRate;
	public float gravityTurnRatePrev;
	public float gravityTurnVecX;
	public float gravityTurnVecY;
	public float gravityTurnVecZ;
	public float gravityTurnYaw;

	public int spaceRaceInviteTeamID;

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

	public GCEntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandler, StatFileWriter statFileWriter)
	{
		super(minecraft, world, session, netHandler, statFileWriter);
	}

	@Override
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
	{
		this.wakeUpPlayer(par1, par2, par3, false);
	}

	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3, boolean bypass)
	{
		ChunkCoordinates c = this.playerLocation;

		if (c != null)
		{
			EventWakePlayer event = new EventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
			MinecraftForge.EVENT_BUS.post(event);

			if (bypass || event.result == null || event.result == EnumStatus.OK)
			{
				super.wakeUpPlayer(par1, par2, par3);
			}
		}
	}

	@Override
	protected void setupCustomSkin()
	{
		super.setupCustomSkin();

		if (ClientProxyCore.capeMap.containsKey(this.getGameProfile().getName()))
		{
			this.galacticraftCape = GCEntityClientPlayerMP.getLocationCape2(this.getGameProfile().getName());
			this.galacticraftCapeImageData = GCEntityClientPlayerMP.getDownloadImage(this.galacticraftCape, GCEntityClientPlayerMP.getCapeURL(this.getGameProfile().getName()));
		}
	}

	public static ResourceLocation getLocationCape2(String par0Str)
	{
		return new ResourceLocation("cloaksGC/" + StringUtils.stripControlCodes(par0Str));
	}

	private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(par0ResourceLocation);

		if (object == null)
		{
			object = new ThreadDownloadImageData(par1Str, null, null);
			texturemanager.loadTexture(par0ResourceLocation, (ITextureObject) object);
		}

		return (ThreadDownloadImageData) object;
	}

	public static String getCapeURL(String par0Str)
	{
		return ClientProxyCore.capeMap.get(par0Str);
	}

	@Override
	public ResourceLocation getLocationCape()
	{
		if ((ConfigManagerCore.overrideCapes || !super.getTextureCape().isTextureUploaded()) && this.galacticraftCape != null)
		{
			return this.galacticraftCape;
		}

		return super.getLocationCape();
	}

	@Override
	public ThreadDownloadImageData getTextureCape()
	{
		if ((ConfigManagerCore.overrideCapes || !super.getTextureCape().isTextureUploaded()) && this.galacticraftCape != null)
		{
			return this.galacticraftCapeImageData;
		}

		return super.getTextureCape();
	}

	@Override
	public void onDeath(DamageSource var1)
	{
		super.onDeath(var1);
	}

	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.provider instanceof WorldProviderOrbit)
		{
			((WorldProviderOrbit) this.worldObj.provider).spinUpdate(this);
		}

		if (this.boundingBox != null && this.boundingBoxBefore == null)
		{
			this.boundingBoxBefore = this.boundingBox;
			this.boundingBox.setBounds(this.boundingBoxBefore.minX + 0.4, this.boundingBoxBefore.minY + 0.9, this.boundingBoxBefore.minZ + 0.4, this.boundingBoxBefore.maxX - 0.4, this.boundingBoxBefore.maxY - 0.9, this.boundingBoxBefore.maxZ - 0.4);
		}
		else if (this.boundingBox != null && this.boundingBoxBefore != null)
		{
			this.boundingBox.setBB(this.boundingBoxBefore);
		}

		super.onLivingUpdate();
		if (this.inFreefall)
		{
			this.limbSwing -= this.limbSwingAmount;
			this.limbSwingAmount = this.prevLimbSwingAmount;
		}

		boolean ridingThirdPersonEntity = this.ridingEntity instanceof ICameraZoomEntity && ((ICameraZoomEntity) this.ridingEntity).defaultThirdPerson();

		if (ridingThirdPersonEntity && !this.lastRidingCameraZoomEntity)
		{
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;
		}

		if (this.ridingEntity != null && this.ridingEntity instanceof ICameraZoomEntity)
		{
			TickHandlerClient.zoom(((ICameraZoomEntity) this.ridingEntity).getCameraZoom());
		}
		else
		{
			TickHandlerClient.zoom(4.0F);
		}

		this.lastRidingCameraZoomEntity = ridingThirdPersonEntity;

		if (!this.onGround && this.lastOnGround)
		{
			this.touchedGround = true;
		}

		if (this.getParachute())
		{
			this.fallDistance = 0.0F;
		}

		PlayerGearData gearData = ClientProxyCore.playerItemData.get(this.getPersistentID());

		this.usingParachute = false;

		if (gearData != null)
		{
			this.usingParachute = gearData.getParachute() != null;
		}

		if (this.getParachute() && this.onGround)
		{
			this.setParachute(false);
			FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = this.getThirdPersonView();
		}

		if (!this.lastUsingParachute && this.usingParachute)
		{
			FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(GalacticraftCore.ASSET_PREFIX + "player.parachute"), (float) this.posX, (float) this.posY, (float) this.posZ, 0.95F + this.rand.nextFloat() * 0.1F, 1.0F));
		}

		this.lastUsingParachute = this.usingParachute;
		this.lastOnGround = this.onGround;

		ItemStack gs = this.inventory.getStackInSlot(0);
		if (gs != null && gs.getItem() instanceof ItemBlockLandingPad && this.worldObj.provider instanceof WorldProviderOrbit)
		{
			if (gs.stackSize <= 6)
			{
				this.setGravity(Gravity.GDirections[gs.stackSize - 1]);
			}
		}
	}

	@Override
	public void moveEntity(double par1, double par3, double par5)
	{
		super.moveEntity(par1, par3, par5);
		this.updateFeet(par1, par5);
	}

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = motionX * motionX + motionZ * motionZ;

		// If the player is on the moon, not airbourne and not riding anything
		if (motionSqrd > 0.001 && this.worldObj != null && this.worldObj.provider instanceof WorldProviderMoon && this.ridingEntity == null)
		{
			int iPosX = (int) Math.floor(this.posX);
			int iPosY = (int) Math.floor(this.posY - 2);
			int iPosZ = (int) Math.floor(this.posZ);

			// If the block below is the moon block
			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCBlocks.blockMoon)
			{
				// And is the correct metadata (moon turf)
				if (this.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
				{
					// If it has been long enough since the last step
					if (this.distanceSinceLastStep > 0.35)
					{
						Vector3 pos = new Vector3(this);
						// Set the footprint position to the block below and add random number to stop z-fighting
						pos.y = MathHelper.floor_double(this.posY - 1) + this.rand.nextFloat() / 100.0F;

						// Adjust footprint to left or right depending on step count
						switch (this.lastStep)
						{
						case 0:
							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw + 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw + 90)) * 0.25));
							break;
						case 1:
							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw - 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw - 90)) * 0.25));
							break;
						}

						ClientProxyCore.footprintRenderer.addFootprint(pos, this.rotationYaw);

						// Increment and cap step counter at 1
						this.lastStep++;
						this.lastStep %= 2;
						this.distanceSinceLastStep = 0;
					}
					else
					{
						this.distanceSinceLastStep += motionSqrd;
					}
				}
			}
		}
	}

	@Override
	public void onUpdate()
	{
		this.tick++;

		if (this != null && this.getParachute() && !this.capabilities.isFlying && !this.handleWaterMovement())
		{
			this.motionY = -0.5D;
			this.motionX *= 0.5F;
			this.motionZ *= 0.5F;
		}

		super.onUpdate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getBedOrientationInDegrees()
	{
		if (this.playerLocation != null)
		{
			int x = this.playerLocation.posX;
			int y = this.playerLocation.posY;
			int z = this.playerLocation.posZ;

			if (this.worldObj.getTileEntity(x, y, z) instanceof TileEntityAdvanced)
			{
				int j = this.worldObj.getBlock(x, y, z).getBedDirection(this.worldObj, x, y, z);

				switch (this.worldObj.getBlockMetadata(x, y, z) - 4)
				{
				case 0:
					return 90.0F;
				case 1:
					return 270.0F;
				case 2:
					return 180.0F;
				case 3:
					return 0.0F;
				}
			}
			else
			{
				return super.getBedOrientationInDegrees();
			}
		}

		return super.getBedOrientationInDegrees();
	}

	public void setUsingGoggles(boolean b)
	{
		this.usingAdvancedGoggles = b;
	}

	public boolean getUsingGoggles()
	{
		return this.usingAdvancedGoggles;
	}

	public void toggleGoggles()
	{
        this.usingAdvancedGoggles = !this.usingAdvancedGoggles;
	}

	public void setParachute(boolean tf)
	{
		this.usingParachute = tf;

		if (!tf)
		{
			this.lastUsingParachute = false;
		}
	}

	public boolean getParachute()
	{
		return this.usingParachute;
	}

	public void setThirdPersonView(int view)
	{
		this.thirdPersonView = view;
	}

	public int getThirdPersonView()
	{
		return this.thirdPersonView;
	}

	public void setGravity(Gravity newGravity)
	{
		if (this.gdir == newGravity)
		{
			return;
		}
		this.gravityTurnRatePrev = this.gravityTurnRate = 0.0F;
		float turnSpeed = 0.05F;
		this.gravityTurnVecX = 0.0F;
		this.gravityTurnVecY = 0.0F;
		this.gravityTurnVecZ = 0.0F;
		this.gravityTurnYaw = 0.0F;

		switch (this.gdir.intValue)
		{
		case 1:
			switch (newGravity.intValue)
			{
			case 1:
				break;
			case 2:
				this.gravityTurnVecX = -2.0F;
				break;
			case 3:
				this.gravityTurnVecY = -1.0F;
				this.gravityTurnYaw = -90.0F;
				break;
			case 4:
				this.gravityTurnVecY = 1.0F;
				this.gravityTurnYaw = 90.0F;
				break;
			case 5:
				this.gravityTurnVecX = 1.0F;
				break;
			case 6:
				this.gravityTurnVecX = -1.0F;
			}

			break;
		case 2:
			switch (newGravity.intValue)
			{
			case 1:
				this.gravityTurnVecX = -2.0F;
				break;
			case 2:
				break;
			case 3:
				this.gravityTurnVecY = 1.0F;
				this.gravityTurnYaw = 90.0F;
				break;
			case 4:
				this.gravityTurnVecY = -1.0F;
				this.gravityTurnYaw = -90.0F;
				break;
			case 5:
				this.gravityTurnVecX = -1.0F;
				break;
			case 6:
				this.gravityTurnVecX = 1.0F;
			}

			break;
		case 3:
			switch (newGravity.intValue)
			{
			case 1:
				this.gravityTurnVecY = 1.0F;
				this.gravityTurnYaw = 90.0F;
				break;
			case 2:
				this.gravityTurnVecY = -1.0F;
				this.gravityTurnYaw = -90.0F;
				break;
			case 3:
				break;
			case 4:
				this.gravityTurnVecZ = -2.0F;
				break;
			case 5:
				this.gravityTurnVecZ = -1.0F;
				this.gravityTurnYaw = -180.0F;
				break;
			case 6:
				this.gravityTurnVecZ = 1.0F;
			}

			break;
		case 4:
			switch (newGravity.intValue)
			{
			case 1:
				this.gravityTurnVecY = -1.0F;
				this.gravityTurnYaw = -90.0F;
				break;
			case 2:
				this.gravityTurnVecY = 1.0F;
				this.gravityTurnYaw = 90.0F;
				break;
			case 3:
				this.gravityTurnVecZ = -2.0F;
				break;
			case 4:
				break;
			case 5:
				this.gravityTurnVecZ = 1.0F;
				this.gravityTurnYaw = -180.0F;
				break;
			case 6:
				this.gravityTurnVecZ = -1.0F;
			}

			break;
		case 5:
			switch (newGravity.intValue)
			{
			case 1:
				this.gravityTurnVecX = -1.0F;
				break;
			case 2:
				this.gravityTurnVecX = 1.0F;
				break;
			case 3:
				this.gravityTurnVecZ = 1.0F;
				this.gravityTurnYaw = 180.0F;
				break;
			case 4:
				this.gravityTurnVecZ = -1.0F;
				this.gravityTurnYaw = 180.0F;
				break;
			case 5:
				break;
			case 6:
				this.gravityTurnVecX = -2.0F;
			}

			break;
		case 6:
			switch (newGravity.intValue)
			{
			case 1:
				this.gravityTurnVecX = 1.0F;
				break;
			case 2:
				this.gravityTurnVecX = -1.0F;
				break;
			case 3:
				this.gravityTurnVecZ = -1.0F;
				break;
			case 4:
				this.gravityTurnVecZ = 1.0F;
				break;
			case 5:
				this.gravityTurnVecX = -2.0F;
			case 6:
			}
			break;
		}

		this.gdir = newGravity;
	}

	public static enum Gravity
	{
		down(0, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), up(1, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F), west(2, 0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.0F, 0.0F, -1.0F, 1.0F, 0.0F), east(3, 0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.5F, -1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F), south(4, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 1.0F), north(5, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, -0.5F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, -1.0F);

		public int intValue;
		public float pitchGravityX;
		public float pitchGravityY;
		public float yawGravityX;
		public float yawGravityY;
		public float yawGravityZ;
		public float thetaX;
		public float thetaZ;
		public float sneakVecX;
		public float sneakVecY;
		public float sneakVecZ;
		public float eyeVecX;
		public float eyeVecY;
		public float eyeVecZ;
		public static Gravity[] GDirections = { down, up, west, east, south, north };

		private Gravity(int value, float pitchX, float pitchY, float yawX, float yawY, float yawZ, float thetaX, float thetaZ, float sneakX, float sneakY, float sneakZ, float eyeX, float eyeY, float eyeZ)
		{
			this.intValue = value;
			this.pitchGravityX = pitchX;
			this.pitchGravityY = pitchY;
			this.yawGravityX = yawX;
			this.yawGravityY = yawY;
			this.yawGravityZ = yawZ;
			this.thetaX = thetaX;
			this.thetaZ = thetaZ;
			this.sneakVecX = sneakX;
			this.sneakVecY = sneakY;
			this.sneakVecZ = sneakZ;
			this.eyeVecX = eyeX;
			this.eyeVecY = eyeY;
			this.eyeVecZ = eyeZ;
		}
	}

	public void reOrientCamera(float par1)
	{
		EntityLivingBase entityLivingBase = this.mc.renderViewEntity;
		float f1 = entityLivingBase.yOffset - 1.62F;
		float pitch = entityLivingBase.prevRotationPitch + (entityLivingBase.rotationPitch - entityLivingBase.prevRotationPitch) * par1;
		float yaw = entityLivingBase.prevRotationYaw + (entityLivingBase.rotationYaw - entityLivingBase.prevRotationYaw) * par1 + 180.0F;
		float eyeHeightChange = entityLivingBase.yOffset - entityLivingBase.width / 2.0F;

		GL11.glTranslatef(0.0F, -f1, 0.0F);
		GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 0.1F);

		GL11.glRotatef(180.0F * this.gdir.thetaX, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(180.0F * this.gdir.thetaZ, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(pitch * this.gdir.pitchGravityX, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(pitch * this.gdir.pitchGravityY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(yaw * this.gdir.yawGravityX, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(yaw * this.gdir.yawGravityY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(yaw * this.gdir.yawGravityZ, 0.0F, 0.0F, 1.0F);

		GL11.glTranslatef(entityLivingBase.ySize * this.gdir.sneakVecX, entityLivingBase.ySize * this.gdir.sneakVecY, entityLivingBase.ySize * this.gdir.sneakVecZ);

		GL11.glTranslatef(eyeHeightChange * this.gdir.eyeVecX, eyeHeightChange * this.gdir.eyeVecY, eyeHeightChange * this.gdir.eyeVecZ);

		if (this.gravityTurnRate < 1.0F)
		{
			GL11.glRotatef(90.0F * (this.gravityTurnRatePrev + (this.gravityTurnRate - this.gravityTurnRatePrev) * par1), this.gravityTurnVecX, this.gravityTurnVecY, this.gravityTurnVecZ);
		}

		//omit this for interesting 3P views
		//GL11.glTranslatef(0.0F, 0.0F, -0.1F);
		//GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
		//GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
		//GL11.glTranslatef(0.0F, f1, 0.0F);

	}
}
