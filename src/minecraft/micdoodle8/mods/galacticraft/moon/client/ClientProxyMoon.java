package micdoodle8.mods.galacticraft.moon.client;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.moon.CommonProxyMoon;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.dimension.GCMoonWorldProvider;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.World;
import net.minecraft.src.WorldClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;

public class ClientProxyMoon extends CommonProxyMoon implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang;
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/moon/client");
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		GalacticraftCore.registerClientSubMod(this);
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
        GCMoonColorizerGrass.setGrassBiomeColorizer(FMLClientHandler.instance().getClient().renderEngine.getTextureContents("/micdoodle8/mods/galacticraft/moon/client/blocks/moonfoliagecolor.png"));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		GCMoonBlocks.addNames();
		GCMoonItems.addNames();
	}

	@Override
	public void registerRenderInformation()
	{
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/moon/client/blocks/moon.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/moon/client/items/moon.png");
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b) {}
	
    public static class TickHandlerClient implements ITickHandler
    {
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            WorldClient world = minecraft.theWorld;
            
            EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (world != null && world.provider instanceof GCMoonWorldProvider)
    			{
    				if (world.provider.getSkyProvider() == null)
                    {
    					world.provider.setSkyProvider(new GCMoonSkyProvider());
                    }
    			}
            }
        }

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData) 
    	{
    	}
    	
        @Override
		public String getLabel()
        {
            return "Galacticraft Moon Client";
        }

    	@Override
    	public EnumSet<TickType> ticks() 
    	{
    		return EnumSet.of(TickType.CLIENT);
    	}
    }

	@Override
	public String getDimensionName() 
	{
		return "Moon";
	}

	@Override
	public GCCoreLocalization getLanguageFile() 
	{
		return this.lang;
	}

	@Override
	public String getPlanetSpriteDirectory() 
	{
		return "/micdoodle8/mods/galacticraft/moon/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer() 
	{
		return new GCMoonSlotRenderer();
	}

	@Override
	public float getPlanetSize() 
	{
		return 0;
	}

	@Override
	public float getDistanceFromCenter() 
	{
		return 0;
	}

	@Override
	public boolean isMoon() 
	{
		return true;
	}

	@Override
	public float getPhaseShift() 
	{
		return 0;
	}

	@Override
	public float getStretchValue() 
	{
		return 0;
	}
}
