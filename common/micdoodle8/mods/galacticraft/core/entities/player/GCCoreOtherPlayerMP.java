package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class GCCoreOtherPlayerMP extends EntityOtherPlayerMP
{
	private ResourceLocation galacticraftCape;
	private ThreadDownloadImageData galacticraftCapeImageData;

	public GCCoreOtherPlayerMP(World par1World, String par2Str)
	{
		super(par1World, par2Str);
	}

	@Override
	protected void setupCustomSkin()
	{
		super.setupCustomSkin();

		if (ClientProxyCore.capeMap.containsKey(this.username))
		{
			this.galacticraftCape = GCCoreOtherPlayerMP.getLocationCape2(this.username);
			this.galacticraftCapeImageData = GCCoreOtherPlayerMP.getDownloadImage(this.galacticraftCape, GCCorePlayerSP.getCapeURL(this.username), null, null);
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
			texturemanager.loadTexture(par0ResourceLocation, (TextureObject) object);
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
}
