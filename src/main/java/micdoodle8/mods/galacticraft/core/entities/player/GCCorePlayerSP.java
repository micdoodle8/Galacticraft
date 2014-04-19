package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.event.GCCoreEventWakePlayer;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;

/**
 * GCCorePlayerSP.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCorePlayerSP extends EntityClientPlayerMP
{
	private final Random rand = new Random();

	private boolean usingParachute;
	private boolean lastUsingParachute;
	public boolean usingAdvancedGoggles;
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

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

    public GCCorePlayerSP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandler, StatFileWriter statFileWriter)
	{
		super(minecraft, world, session, netHandler, statFileWriter);

		if (!GalacticraftCore.playersClient.containsKey(this.getGameProfile().getName()))
		{
			GalacticraftCore.playersClient.put(this.getGameProfile().getName(), this);
		}
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
			GCCoreEventWakePlayer event = new GCCoreEventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
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
			this.galacticraftCape = GCCorePlayerSP.getLocationCape2(this.getGameProfile().getName());
			this.galacticraftCapeImageData = GCCorePlayerSP.getDownloadImage(this.galacticraftCape, GCCorePlayerSP.getCapeURL(this.getGameProfile().getName()), null, null);
		}
	}

	public static ResourceLocation getLocationCape2(String par0Str)
	{
		return new ResourceLocation("cloaksGC/" + StringUtils.stripControlCodes(par0Str));
	}

	private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(par0ResourceLocation);

		if (object == null)
		{
			object = new ThreadDownloadImageData(par1Str, par2ResourceLocation, par3IImageBuffer);
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
		if ((GCCoreConfigManager.overrideCapes || !super.getTextureCape().isTextureUploaded()) && this.galacticraftCape != null)
		{
			return this.galacticraftCape;
		}

		return super.getLocationCape();
	}

	@Override
	public ThreadDownloadImageData getTextureCape()
	{
		if ((GCCoreConfigManager.overrideCapes || !super.getTextureCape().isTextureUploaded()) && this.galacticraftCape != null)
		{
			return this.galacticraftCapeImageData;
		}

		return super.getTextureCape();
	}

	@Override
	public void onDeath(DamageSource var1)
	{
		GalacticraftCore.playersClient.remove(this);

		super.onDeath(var1);
	}

	@Override
	public void onLivingUpdate()
	{
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

//		// If the player is on the moon, not airbourne and not riding anything
//		if (this.worldObj != null && this.worldObj.provider instanceof GCMoonWorldProvider && this.onGround && this.ridingEntity == null)
//		{
//			int iPosX = (int)Math.floor(this.posX);
//			int iPosY = (int)Math.floor(this.posY - 2);
//			int iPosZ = (int)Math.floor(this.posZ);
//			
//			// If the block below is the moon block
//			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCCoreBlocks.blockMoon)
//			{
//				// And is the correct metadata (moon turf)
//				if (this.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
//				{
//					// If it has been long enough since the last step
//					if (this.distanceSinceLastStep > 0.09)
//					{
//						Vector3 pos = new Vector3(this);
//						// Set the footprint position to the block below and add random number to stop z-fighting
//						pos.y = MathHelper.floor_double(this.posY - 1) + this.rand.nextFloat() / 100.0F;
//						
//						// Adjust footprint to left or right depending on step count
//						switch (this.lastStep)
//						{
//						case 0:
//							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw + 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw + 90)) * 0.25));
//							break;
//						case 1:
//							pos.translate(new Vector3(Math.sin(Math.toRadians(-this.rotationYaw - 90)) * 0.25, 0, Math.cos(Math.toRadians(-this.rotationYaw - 90)) * 0.25));
//							break;
//						}
//						
//						ClientProxyCore.footprintRenderer.addFootprint(pos, this.rotationYaw);
//						
//						// Increment and cap step counter at 1
//						this.lastStep++;
//						this.lastStep %= 2;
//						this.distanceSinceLastStep = 0;
//					}
//					else
//					{
//						double motionSqrd = (this.motionX * this.motionX + this.motionZ * this.motionZ);
//						
//						// Even when the player is still, motion isn't exactly zero
//						if (motionSqrd > 0.001)
//						{
//							this.distanceSinceLastStep += motionSqrd;
//						}
//					}
//				}
//			}
//		}
		
		if (!this.onGround && this.lastOnGround)
		{
			this.touchedGround = true;
		}

		if (this.getParachute())
		{
			this.fallDistance = 0.0F;
		}

		PlayerGearData gearData = null;

		for (PlayerGearData gearData2 : ClientProxyCore.playerItemData)
		{
			if (gearData2.getPlayer().getGameProfile().getName().equals(this.getGameProfile().getName()))
			{
				gearData = gearData2;
				break;
			}
		}

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
	}

	@Override
    public void moveEntity(double par1, double par3, double par5)
    {
    	super.moveEntity(par1, par3, par5);
    	this.updateFeet(par1, par5);
    }

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = (motionX * motionX + motionZ * motionZ);
		
		// If the player is on the moon, not airbourne and not riding anything
		if (motionSqrd > 0.001 && this.worldObj != null && this.worldObj.provider instanceof GCMoonWorldProvider && this.ridingEntity == null)
		{
			int iPosX = (int)Math.floor(this.posX);
			int iPosY = (int)Math.floor(this.posY - 2);
			int iPosZ = (int)Math.floor(this.posZ);
			
			// If the block below is the moon block
			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCCoreBlocks.blockMoon)
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

		if (!GalacticraftCore.playersClient.containsKey(this.getGameProfile().getName()) || this.tick % 360 == 0)
		{
			GalacticraftCore.playersClient.put(this.getGameProfile().getName(), this);
		}

		if (this != null && this.getParachute() && !this.capabilities.isFlying && !this.handleWaterMovement())
		{
			this.motionY = -0.5D;
			this.motionX *= 0.5F;
			this.motionZ *= 0.5F;
		}

		super.onUpdate();
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
		if (this.usingAdvancedGoggles)
		{
			this.usingAdvancedGoggles = false;
		}
		else
		{
			this.usingAdvancedGoggles = true;
		}
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
}
