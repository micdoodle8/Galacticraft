package codechicken.nei;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import codechicken.core.ClientUtils;
import codechicken.lib.lang.LangUtil;
import codechicken.lib.packet.PacketCustom;
import codechicken.nei.api.API;
import codechicken.nei.api.ItemInfo;
import cpw.mods.fml.client.CustomModLoadingErrorDisplayException;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.World;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientHandler implements ITickHandler
{
    public static LangUtil lang = LangUtil.loadLangDir("nei");
    
    private static ClientHandler instance;
    
    private ArrayList<EntityItem> SMPmagneticItems = new ArrayList<EntityItem>();
    private World lastworld;
    private boolean firstTick = true;
    
    public void addSMPMagneticItem(int i, World world)
    {
        WorldClient cworld = (WorldClient)world;
        Entity e = cworld.getEntityByID(i);
        if(e == null || !(e instanceof EntityItem))
        {
            return;
        }
        SMPmagneticItems.add((EntityItem)e);
    }
    
    private void updateMagnetMode(World world, EntityPlayerSP player)
    {
        if(!NEIClientConfig.getMagnetMode())return;
        
        float distancexz = 16;
        float distancey = 8;
        double maxspeedxz = 0.5;
        double maxspeedy = 0.5;
        double speedxz = 0.05;
        double speedy = 0.07;
        
        List<EntityItem> items;
        if(world.isRemote)
        {
            items = SMPmagneticItems;
        }
        else
        {
            items = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(distancexz, distancey, distancexz));
        }
        for(Iterator<EntityItem> iterator = items.iterator(); iterator.hasNext();)
        {
            EntityItem item = iterator.next();
            
            if(item.delayBeforeCanPickup > 0)continue;
            if(item.isDead && world.isRemote)iterator.remove();
            
            if(!NEIClientUtils.canItemFitInInventory(player, item.getEntityItem()))continue;
            
            double dx = player.posX - item.posX;
            double dy = player.posY + player.getEyeHeight() - item.posY;
            double dz = player.posZ - item.posZ;
            double absxz = Math.sqrt(dx*dx+dz*dz);
            double absy = Math.abs(dy);
            if(absxz > distancexz)
            {
                continue;
            }
            
            if(absxz > 1)
            {
                dx /= absxz;
                dz /= absxz;
            }
            
            if(absy > 1)
            {
                dy /= absy;
            }

            double vx = item.motionX + speedxz*dx;
            double vy = item.motionY + speedy*dy;
            double vz = item.motionZ + speedxz*dz;
            
            double absvxz = Math.sqrt(vx*vx+vz*vz);
            double absvy = Math.abs(vy);
            
            double rationspeedxz = absvxz / maxspeedxz;
            if(rationspeedxz > 1)
            {
                vx/=rationspeedxz;
                vz/=rationspeedxz;
            }
            
            double rationspeedy = absvy / maxspeedy;
            if(rationspeedy > 1)
            {
                vy/=rationspeedy;
            }
            
            if(absvxz < 0.2 && absxz < 0.2 && world.isRemote)
            {
                item.setDead();
            }
            
            item.setVelocity(vx, vy, vz);
        }
    }

    public static void load() 
    {
        try
        {
            TMIUninstaller.deleteTMIUninstaller();
            if(TMIUninstaller.TMIInstalled())
            {
                TMIUninstaller.runTMIUninstaller();
                NEIClientUtils.mc().shutdownMinecraftApplet();
            }
        }
        catch(Exception e)
        {
            System.err.println("Error with TMI Uninstaller");
            e.printStackTrace();
        }
        
        instance = new ClientHandler();        

        PacketCustom.assignHandler(NEICPH.channel, 0, 255, new NEICPH());    
        TickRegistry.registerTickHandler(instance, Side.CLIENT);
        MinecraftForge.EVENT_BUS.register(new WorldOverlayRenderer());
        
        LanguageRegistry.instance().addStringLocalization("entity.SnowMan.name", "Snow Golem");
        API.registerHighlightHandler(new DefaultHighlightHandler(), ItemInfo.Layout.HEADER);
        HUDRenderer.load();
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) 
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        if(type.contains(TickType.CLIENT) && mc.theWorld != null)
        {
            loadWorld(mc.theWorld, false);
            
            if(!NEIClientConfig.isEnabled())
                return;
            
            KeyManager.tickKeyStates();
            
            NEIController.updateUnlimitedItems(mc.thePlayer.inventory);
            if(mc.currentScreen == null)
                NEIController.processCreativeCycling(mc.thePlayer.inventory);
            
            updateMagnetMode(mc.theWorld, mc.thePlayer);
        }
        
        if(type.contains(TickType.CLIENT))
        {
            GuiScreen gui = mc.currentScreen;
            if(gui instanceof GuiMainMenu)
            {
                if(firstTick)
                {
                    firstTick = false;
                    onMainMenuInit();
                }
                
                if(lastworld != null)
                    lastworld = null;
            }
        }
    }
    
    public void loadWorld(World world, boolean remote)
    {
        if(world != lastworld)
        {
            SMPmagneticItems.clear();
            WorldOverlayRenderer.reset();
            
            if(!remote)
            {
                NEIClientConfig.setHasSMPCounterPart(false);
                NEIClientConfig.setInternalEnabled(false);
                
                if(ClientUtils.isLocal())//wait for server to initiate
                    return;
                
                NEIClientConfig.loadWorld("remote/"+ClientUtils.getServerIP().replace(':', '~'));
            }
            
            lastworld = world;
        }
    }
    
    public void worldLoaded(World world)
    {
        SMPmagneticItems.clear();
        WorldOverlayRenderer.reset();
    }

    private void onMainMenuInit()
    {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) 
    {
        if(type.contains(TickType.RENDER) && NEIClientConfig.isEnabled())
        {
            HUDRenderer.renderOverlay();
        }
    }

    @Override
    public EnumSet<TickType> ticks() 
    {
        return EnumSet.of(TickType.CLIENT, TickType.CLIENT, TickType.RENDER);
    }

    @Override
    public String getLabel() 
    {
        return "NEI Client";
    }

    public static ClientHandler instance() 
    {
        return instance;
    }
    
    public static RuntimeException throwCME(final String message)
    {
        final GuiScreen errorGui = new GuiErrorScreen(null, null)
        {
            @Override
            public void handleMouseInput()
            {}
            @Override
            public void handleKeyboardInput()
            {}
            @Override
            public void drawScreen(int par1, int par2, float par3)
            {
                drawDefaultBackground();
                String[] s_msg = message.split("\n");
                for(int i = 0; i < s_msg.length; ++i)
                    drawCenteredString(fontRenderer, s_msg[i], width / 2, height / 3 + 12 * i, 0xFFFFFFFF);
            }
        };
        
        @SuppressWarnings("serial")
        CustomModLoadingErrorDisplayException e = (new CustomModLoadingErrorDisplayException()
        {
            @Override
            public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer)
            {
                Minecraft.getMinecraft().displayGuiScreen(errorGui);
            }
            
            @Override
            public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime)
            {}
        });
        throw e;
    }
}
