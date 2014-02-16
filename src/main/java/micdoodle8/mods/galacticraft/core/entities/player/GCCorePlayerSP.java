package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.ArrayList;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.event.GCCoreEventWakePlayer;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tick.GCCoreTickHandlerClient;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	public int clientSpaceStationID;

	public ArrayList<ISchematicPage> unlockedSchematics = new ArrayList<ISchematicPage>();

	public GCCorePlayerSP(Minecraft par1Minecraft, World par2World, Session par3Session, NetHandlerPlayClient par4NetClientHandler, StatFileWriter statFileWriter)
	{
		super(par1Minecraft, par2World, par3Session, par4NetClientHandler, statFileWriter);

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

		if (GCCoreConfigManager.overrideCapes)
		{
			this.galacticraftCape = AbstractClientPlayer.getLocationCape(this.getGameProfile().getName());
			this.galacticraftCapeImageData = GCCorePlayerSP.getImageData(this.galacticraftCape, GCCorePlayerSP.getCapeURL(this.getGameProfile().getName()), null, null);
		}
	}

	public static String getCapeURL(String par0Str)
	{
		return "";
//		return ClientProxyCore.capeMap.get(par0Str); TODO Fix capes
	}

	private static ThreadDownloadImageData getImageData(ResourceLocation par0ResourceLocation, String par1Str, ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer)
	{
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();

		ThreadDownloadImageData object = new ThreadDownloadImageData(par1Str, par2ResourceLocation, par3IImageBuffer);
		texturemanager.loadTexture(par0ResourceLocation, object);

		return object;
	}

	@Override
	public ResourceLocation getLocationCape()
	{
		if (!GCCoreConfigManager.overrideCapes || !this.getTextureCape().isTextureUploaded())
		{
			return super.getLocationCape();
		}

		return this.galacticraftCape;
	}

	@Override
	public ThreadDownloadImageData getTextureCape()
	{
		if (!GCCoreConfigManager.overrideCapes || !this.galacticraftCapeImageData.isTextureUploaded())
		{
			return super.getTextureCape();
		}

		return this.galacticraftCapeImageData;
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

		if (!this.onGround && this.lastOnGround)
		{
			this.touchedGround = true;
		}

		if (this.getParachute())
		{
			this.fallDistance = 0.0F;
		}

		PlayerGearData gearData = null;

		for (PlayerGearData gearData2 : GCCoreTickHandlerClient.playerItemData)
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
	@SideOnly(Side.CLIENT)
	public IIcon getItemIcon(ItemStack par1ItemStack, int par2)
	{
		IIcon icon = super.getItemIcon(par1ItemStack, par2);

		if (par1ItemStack.getItem() == Items.fishing_rod && this.fishEntity != null)
		{
			icon = Items.fishing_rod.func_94597_g();
		}
		else
		{
			if (par1ItemStack.getItem().requiresMultipleRenderPasses())
			{
				return par1ItemStack.getItem().getIcon(par1ItemStack, par2);
			}

			if (this.getItemInUse() != null && par1ItemStack.getItem() == GCCoreItems.bowGravity)
			{
				final int j = par1ItemStack.getMaxItemUseDuration() - this.getItemInUseCount();

				if (j >= 18)
				{
					return Items.bow.getItemIconForUseDuration(2);
				}

				if (j > 13)
				{
					return Items.bow.getItemIconForUseDuration(1);
				}

				if (j > 0)
				{
					return Items.bow.getItemIconForUseDuration(0);
				}
			}
			else
			{
				return super.getItemIcon(par1ItemStack, par2);
			}

			icon = par1ItemStack.getItem().getIcon(par1ItemStack, par2, this, this.getItemInUse(), this.getItemInUseCount());
		}

		return icon;
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
