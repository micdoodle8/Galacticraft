package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

public class GCEntityOtherPlayerMP extends EntityOtherPlayerMP
{
	private ResourceLocation galacticraftCape;
	private ThreadDownloadImageData galacticraftCapeImageData;

	public GCEntityOtherPlayerMP(World par1World, GameProfile profile)
	{
		super(par1World, profile);
	}

	@Override
	protected void setupCustomSkin()
	{
		super.setupCustomSkin();

		if (ClientProxyCore.capeMap.containsKey(this.getGameProfile().getName()))
		{
			this.galacticraftCape = GCEntityOtherPlayerMP.getLocationCape2(this.getGameProfile().getName());
			this.galacticraftCapeImageData = GCEntityOtherPlayerMP.getDownloadImage(this.galacticraftCape, GCEntityClientPlayerMP.getCapeURL(this.getGameProfile().getName()), null, null);
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
            texturemanager.loadTexture(par0ResourceLocation, (ITextureObject)object);
        }

        return (ThreadDownloadImageData)object;
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
}
