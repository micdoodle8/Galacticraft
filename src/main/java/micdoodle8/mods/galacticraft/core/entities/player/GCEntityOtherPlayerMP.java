package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.World;

import com.mojang.authlib.GameProfile;

public class GCEntityOtherPlayerMP extends EntityOtherPlayerMP
{
    //	private ResourceLocation galacticraftCape; TODO Capes
//	private ThreadDownloadImageData galacticraftCapeImageData;
//
    public GCEntityOtherPlayerMP(World par1World, GameProfile profile)
    {
        super(par1World, profile);
    }
//
//    public void func_152121_a(MinecraftProfileTexture.Type p_152121_1_, ResourceLocation p_152121_2_)
//    {
//        super.func_152121_a(p_152121_1_, p_152121_2_);
//        this.setupCustomSkin();
//    }
//
//    public ResourceLocation getLocationCape()
//    {
//        ResourceLocation location = super.getLocationCape();
//        boolean b = this.galacticraftCapeImageData != null && this.galacticraftCapeImageData.getGlTextureId() != -1;
//
//        if ((ConfigManagerCore.overrideCapes || location == null) && this.galacticraftCape != null)
//        {
//
//            return null;
//        }
//
//        return this.locationCape;
//    }
//
//	protected void setupCustomSkin()
//	{
//		if (ClientProxyCore.capeMap.containsKey(this.getGameProfile().getName()))
//		{
//			this.galacticraftCape = GCEntityOtherPlayerMP.getLocationCape2(this.getGameProfile().getName());
//			this.galacticraftCapeImageData = GCEntityOtherPlayerMP.getDownloadImage(this.galacticraftCape, EntityClientPlayerMP.getCapeURL(this.getGameProfile().getName()));
//		}
//	}
//
//	public static ResourceLocation getLocationCape2(String par0Str)
//	{
//		return new ResourceLocation("cloaksGC/" + StringUtils.stripControlCodes(par0Str));
//	}
//
//	private static ThreadDownloadImageData getDownloadImage(ResourceLocation par0ResourceLocation, String par1Str)
//	{
//		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
//		Object object = texturemanager.getTexture(par0ResourceLocation);
//
//		if (object == null)
//		{
//			object = new ThreadDownloadImageData(null, par1Str, null, null);
//			texturemanager.loadTexture(par0ResourceLocation, (ITextureObject) object);
//		}
//
//		return (ThreadDownloadImageData) object;
//	}
//
//	public static String getCapeURL(String par0Str)
//	{
//		return ClientProxyCore.capeMap.get(par0Str);
//	}
//
//	@Override
//	public ResourceLocation getLocationCape()
//	{
//		if ((ConfigManagerCore.overrideCapes || super.getLocationCape() == null) && this.galacticraftCape != null)
//		{
//			return this.galacticraftCape;
//		}
//
//		return super.getLocationCape();
//	}
//
//	@Override
//	public ThreadDownloadImageData getTextureCape()
//	{
//		if ((ConfigManagerCore.overrideCapes || !super.getTextureCape().isTextureUploaded()) && this.galacticraftCape != null)
//		{
//			return this.galacticraftCapeImageData;
//		}
//
//		return super.getTextureCape();
//	}
//
//    public boolean func_152122_n()
//    {
//        return this.galacticraftCape != null || super.func_152122_n();
//    }
}
