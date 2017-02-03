package micdoodle8.mods.galacticraft.core.proxy;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.player.IPlayerServer;
import micdoodle8.mods.galacticraft.core.entities.player.PlayerServer;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.wrappers.PartialCanister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxyCore
{
    public IPlayerServer player = new PlayerServer();

    public void preInit(FMLPreInitializationEvent event)
    {
    }

    public void registerVariants()
    {

    }

    public void init(FMLInitializationEvent event)
    {
    }

    public void postInit(FMLPostInitializationEvent event)
    {
    }

    public void registerCanister(PartialCanister container)
    {
    }

    public void registerFluidTexture(Fluid fluid, ResourceLocation submergedTexture)
    {
    }

    public World getClientWorld()
    {
        return null;
    }

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo)
    {
    }

    public World getWorldForID(int dimensionID)
    {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer == null)
        {
            return null;
        }
        return theServer.worldServerForDimension(dimensionID);
    }

    public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
    {
        if (handler instanceof NetHandlerPlayServer)
        {
            return ((NetHandlerPlayServer) handler).playerEntity;
        }
        else
        {
            return null;
        }
    }

    public void postRegisterItem(Item item)
    {
    }

    public void unregisterNetwork(FluidNetwork fluidNetwork)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            TickHandlerServer.removeFluidNetwork(fluidNetwork);
        }
    }

    public void registerNetwork(FluidNetwork fluidNetwork)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            TickHandlerServer.addFluidNetwork(fluidNetwork);
        }
    }

    public boolean isPaused()
    {
        return false;
    }
}
