package micdoodle8.mods.galacticraft.core.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.util.*;

public class GuiCelestialSelection extends GuiScreen
{
    private static enum EnumSelectionState
    {
        PREVIEW,
        PROFILE
    }

    private static final int MAX_SPACE_STATION_NAME_LENGTH = 32;
    
    private Matrix4f mainWorldMatrix;
    private float zoom = 0.0F;
    private float planetZoom = 0.0F;
    private boolean doneZooming = false;
    private float preSelectZoom = 0.0F;
    private Vector2f preSelectPosition = new Vector2f();
    public static ResourceLocation guiMain0 = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialselection.png");
    public static ResourceLocation guiMain1 = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialselection1.png");
    private int ticksSinceSelection = 0;
    private int ticksSinceUnselection = -1;
    private int ticksSinceMenuOpen = 0;
    private Vector2f position = new Vector2f(0, 0);
    private Map<CelestialBody, Vector3f> planetPosMap = Maps.newHashMap();
    private Map<CelestialBody, Integer> celestialBodyTicks = Maps.newHashMap();
    private CelestialBody selectedBody;
    private CelestialBody lastSelectedBody;
    private static int BORDER_WIDTH = 0;
    private static int BORDER_EDGE_WIDTH = 0;
    private EnumSelectionState selectionState = EnumSelectionState.PREVIEW;
    private int selectionCount = 0;
    private int zoomTooltipPos = 0;
    private Object selectedParent = GalacticraftCore.solarSystemSol;
    private final boolean mapMode;
    public List<CelestialBody> possibleBodies;
    public Map<String, String> spaceStationNames = Maps.newHashMap();
    public Map<String, Integer> spaceStationIDs = Maps.newHashMap();
    public SmallFontRenderer smallFontRenderer;
    private String selectedStationOwner = "";
    private int spaceStationListOffset = 0;
    private boolean renamingSpaceStation;
    private String renamingString = "";
    private Vector2f translation = new Vector2f();

    public GuiCelestialSelection(boolean mapMode, List<CelestialBody> possibleBodies)
    {
    	this.translation.x = 0.0F;
    	this.translation.y = 0.0F;
        this.mapMode = mapMode;
        this.possibleBodies = possibleBodies;
        this.smallFontRenderer = new SmallFontRenderer(FMLClientHandler.instance().getClient().gameSettings, new ResourceLocation("textures/font/ascii.png"), FMLClientHandler.instance().getClient().renderEngine, false);
    }

    @Override
    public void initGui()
    {
        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            this.celestialBodyTicks.put(planet, 0);
        }

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            this.celestialBodyTicks.put(moon, 0);
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            this.celestialBodyTicks.put(satellite, 0);
        }

        GuiCelestialSelection.BORDER_WIDTH = this.width / 65;
        GuiCelestialSelection.BORDER_EDGE_WIDTH = GuiCelestialSelection.BORDER_WIDTH / 4;
    }

    private String getGrandparentName()
    {
        if (this.selectedParent instanceof Planet)
        {
            SolarSystem parentSolarSystem = ((Planet) this.selectedParent).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedParentGalaxyName();
            }
        }
        else if (this.selectedParent instanceof IChildBody)
        {
            Planet parentPlanet = ((IChildBody) this.selectedParent).getParentPlanet();

            if (parentPlanet != null)
            {
                SolarSystem parentSolarSystem = parentPlanet.getParentSolarSystem();

                if (parentSolarSystem != null)
                {
                    return parentSolarSystem.getLocalizedName();
                }
            }
        }
        else if (this.selectedParent instanceof Star)
        {
            SolarSystem parentSolarSystem = ((Star) this.selectedParent).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedParentGalaxyName();
            }
        }
        else if (this.selectedParent instanceof SolarSystem)
        {
            return ((SolarSystem) this.selectedParent).getLocalizedParentGalaxyName();
        }

        return "Null";
    }

    private String getParentName()
    {
        if (this.selectedParent instanceof Planet)
        {
            SolarSystem parentSolarSystem = ((Planet) this.selectedParent).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedName();
            }
        }
        else if (this.selectedParent instanceof IChildBody)
        {
            Planet parentPlanet = ((IChildBody) this.selectedParent).getParentPlanet();

            if (parentPlanet != null)
            {
                return parentPlanet.getLocalizedName();
            }
        }
        else if (this.selectedParent instanceof SolarSystem)
        {
            return ((SolarSystem) this.selectedParent).getLocalizedName();
        }
        else if (this.selectedParent instanceof Star)
        {
            SolarSystem parentSolarSystem = ((Star) this.selectedParent).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedName();
            }
        }

        return "Null";
    }

    private float getScale(CelestialBody celestialBody)
    {
        return 3.0F * celestialBody.getRelativeDistanceFromCenter().unScaledDistance * (celestialBody instanceof Planet ? 25.0F : 1.0F / 5.0F);
    }

    private List<CelestialBody> getSiblings(CelestialBody celestialBody)
    {
        List<CelestialBody> bodyList = Lists.newArrayList();

        if (celestialBody instanceof Planet)
        {
            SolarSystem solarSystem = ((Planet) celestialBody).getParentSolarSystem();

            for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
            {
                SolarSystem solarSystem1 = planet.getParentSolarSystem();

                if (solarSystem1 != null && solarSystem1.equals(solarSystem))
                {
                    bodyList.add(planet);
                }
            }
        }
        else if (celestialBody instanceof IChildBody)
        {
            Planet planet = ((IChildBody) celestialBody).getParentPlanet();

            for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
            {
                Planet planet1 = moon.getParentPlanet();

                if (planet1 != null && planet1.equals(planet))
                {
                    bodyList.add(moon);
                }
            }
        }

        Collections.sort(bodyList);

        return bodyList;
    }

    private List<CelestialBody> getChildren(Object object)
    {
        List<CelestialBody> bodyList = Lists.newArrayList();

        if (object instanceof Planet)
        {
            List<Moon> moons = GalaxyRegistry.getMoonsForPlanet((Planet) object);
            bodyList.addAll(moons);
        }
        else if (object instanceof SolarSystem)
        {
            List<Planet> planets = GalaxyRegistry.getPlanetsForSolarSystem((SolarSystem) object);
            bodyList.addAll(planets);
        }

        Collections.sort(bodyList);

        return bodyList;
    }

    private float lerp(float v0, float v1, float t)
    {
        return v0 + t * (v1 - v0);
    }

    private Vector2f lerpVec2(Vector2f v0, Vector2f v1, float t)
    {
        return new Vector2f(v0.x + t * (v1.x - v0.x), v0.y + t * (v1.y - v0.y));
    }

    private float getZoomAdvanced()
    {
    	if (this.ticksSinceMenuOpen < 30)
    	{
    		float scale = Math.max(0.0F, Math.min(this.ticksSinceMenuOpen / 30.0F, 1.0F));
    		float lerp = this.lerp(-0.75F, 0.0F, (float)Math.pow(scale, 0.5F));
            return lerp;
    	}
    	
        if (this.selectedBody == null)
        {
            if (!this.doneZooming)
            {
                float unselectScale = this.lerp(this.zoom, this.preSelectZoom, Math.max(0.0F, Math.min(this.ticksSinceUnselection / 100.0F, 1.0F)));
                
                if (unselectScale <= this.preSelectZoom + 0.05F)
                {
                    this.zoom = this.preSelectZoom;
                    this.preSelectZoom = 0.0F;
                    this.ticksSinceUnselection = -1;
                    this.doneZooming = true;
                }

                return unselectScale;
            }

            return this.zoom;
        }

        if (this.selectionState == EnumSelectionState.PREVIEW && this.selectionCount < 2 && !(this.lastSelectedBody instanceof Planet && this.selectedBody instanceof Planet))
        {
            return this.zoom;
        }

        if (!this.doneZooming)
        {
            float f = this.lerp(this.zoom, 12, Math.max(0.0F, Math.min((this.ticksSinceSelection - 20) / 40.0F, 1.0F)));

            if (f >= 11.95F)
            {
                this.doneZooming = true;
            }

            return f;
        }

        return 12 + this.planetZoom;
    }

    private Vector2f getTranslationAdvanced(float partialTicks)
    {
        if (this.selectedBody == null)
        {
            if (this.ticksSinceUnselection > 0)
            {
            	float f0 = Math.max(0.0F, Math.min((this.ticksSinceUnselection + partialTicks) / 100.0F, 1.0F));
            	if (f0 >= 0.999999F)
            	{
            		this.ticksSinceUnselection = 0;
            	}
                return this.lerpVec2(this.position, this.preSelectPosition, f0);
            }
            
            return new Vector2f(this.position.x + translation.x, this.position.y + translation.y);
        }

        if (this.selectionCount < 2)
        {
            if (this.selectedBody instanceof IChildBody)
            {
                Vector3f posVec = this.getCelestialBodyPosition(((IChildBody) this.selectedBody).getParentPlanet());
                return new Vector2f(posVec.x, posVec.y);
            }
            
            return new Vector2f(this.position.x + translation.x, this.position.y + translation.y);
        }

        Vector3f posVec = this.getCelestialBodyPosition(this.selectedBody);
        return this.lerpVec2(this.position, new Vector2f(posVec.x, posVec.y), Math.max(0.0F, Math.min((this.ticksSinceSelection + partialTicks - 18) / 7.5F, 1.0F)));
    }

    @Override
    protected void keyTyped(char keyChar, int keyID)
    {
        // Override and do nothing, so it isn't possible to exit the GUI
        if (this.mapMode)
        {
            super.keyTyped(keyChar, keyID);
        }

        if (keyID == 1)
        {
            if (this.selectedBody != null)
            {
                this.unselectCelestialBody();
            }
        }

        if (this.renamingSpaceStation)
        {
            if (keyID == Keyboard.KEY_BACK)
            {
                if (this.renamingString != null && this.renamingString.length() > 0)
                {
                    String toBeParsed = this.renamingString.substring(0, this.renamingString.length() - 1);

                    if (this.isValid(toBeParsed))
                    {
                        this.renamingString = toBeParsed;
//                        this.timeBackspacePressed = System.currentTimeMillis();
                    }
                    else
                    {
                        this.renamingString = "";
                    }
                }
            }
            else if (keyChar == 22)
            {
                String pastestring = GuiScreen.getClipboardString();

                if (pastestring == null)
                {
                    pastestring = "";
                }

                if (this.isValid(this.renamingString + pastestring))
                {
                    this.renamingString = this.renamingString + pastestring;
                    this.renamingString = this.renamingString.substring(0, Math.min(String.valueOf(this.renamingString).length(), MAX_SPACE_STATION_NAME_LENGTH));
                }
            }
            else if (this.isValid(this.renamingString + keyChar))
            {
                this.renamingString = this.renamingString + keyChar;
                this.renamingString = this.renamingString.substring(0, Math.min(this.renamingString.length(), MAX_SPACE_STATION_NAME_LENGTH));
            }

            return;
        }

        // Keyboard shortcut - teleport to dimension by pressing 'Enter'
        if (keyID == Keyboard.KEY_RETURN)
        {
            this.teleportToSelectedBody();
        }
    }

    public boolean isValid(String string)
    {
        if (string.length() <= 0)
        {
            return false;
        }

        return ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1));
    }

    private boolean canCreateSpaceStation()
    {
        if (ClientProxyCore.clientSpaceStationID != 0 && ClientProxyCore.clientSpaceStationID != -1)
        {
            return false;
        }

        return !this.mapMode;
    }

    private void unselectCelestialBody()
    {
        this.selectionCount = 0;
        this.ticksSinceUnselection = 0;
        this.lastSelectedBody = this.selectedBody;
        this.selectedBody = null;
        this.doneZooming = false;
        this.selectedStationOwner = "";
    }

    @Override
    public void updateScreen()
    {
        this.ticksSinceMenuOpen++;

        for (CelestialBody e : this.celestialBodyTicks.keySet())
        {
//			if (!(e instanceof Planet && e == this.selectedBody) && !(e instanceof Planet && this.selectedBody instanceof IChildBody && GalaxyRegistry.getIChildBodysForPlanet((Planet) e).contains(this.selectedBody)))
            {
                Integer i = this.celestialBodyTicks.get(e);

                if (i != null)
                {
                    i++;
                }

                this.celestialBodyTicks.put(e, i);
            }
        }

        if (this.selectedBody != null)
        {
            this.ticksSinceSelection++;
        }

        if (this.selectedBody == null && this.ticksSinceUnselection >= 0)
        {
            this.ticksSinceUnselection++;
        }
        
        if (!this.renamingSpaceStation && (this.selectedBody == null || this.selectionCount < 2))
        {
        	this.translation.x = 0.0F;
        	this.translation.y = 0.0F;
        	
            if (mc.gameSettings.isKeyDown(KeyHandlerClient.leftKey))
            {
            	translation.x += -2.0F;
            	translation.y += -2.0F;
            }
            
            if (mc.gameSettings.isKeyDown(KeyHandlerClient.rightKey))
            {
            	translation.x += 2.0F;
            	translation.y += 2.0F;
            }
            
            if (mc.gameSettings.isKeyDown(KeyHandlerClient.upKey))
            {
            	translation.x += 2.0F;
            	translation.y += -2.0F;
            }
            
            if (mc.gameSettings.isKeyDown(KeyHandlerClient.downKey))
            {
            	translation.x += -2.0F;
            	translation.y += 2.0F;
            }
        }
    }

    private boolean teleportToSelectedBody()
    {
        if (this.selectedBody != null)
        {
            if (this.selectedBody.getReachable() && this.possibleBodies != null && this.possibleBodies.contains(this.selectedBody))
            {
                try
                {
                    String dimension;

                    if (this.selectedBody == GalacticraftCore.satelliteSpaceStation)
                    {
                        if (this.spaceStationIDs == null)
                        {
                            GCLog.severe("Please report as a BUG: spaceStationIDs was null.");
                            return false;
                        }
                        Integer mapping = this.spaceStationIDs.get(this.selectedStationOwner);
                        //No need to check lowercase as selectedStationOwner is taken from keys.
                        if (mapping == null)
                        {
                            GCLog.severe("Problem matching player name in space station check: " + this.selectedStationOwner);
                            return false;
                        }
                        int spacestationID = mapping;
                        WorldProvider spacestation = WorldUtil.getProviderForDimension(spacestationID);
                        if (spacestation != null)
                        {
                            dimension = spacestation.getDimensionName();
                        }
                        else
                        {
                            GCLog.severe("Failed to find a spacestation with dimension " + spacestationID);
                            return false;
                        }
                    }
                    else
                    {
                        dimension = WorldUtil.getProviderForDimension(this.selectedBody.getDimensionID()).getDimensionName();
                    }

                    if (dimension.contains("$"))
                    {
                        this.mc.gameSettings.thirdPersonView = 0;
                    }
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_TELEPORT_ENTITY, new Object[] { dimension }));
                    //TODO   Some type of clientside "in Space" holding screen here while waiting for the server to do the teleport
                    //(Otherwise the client will be returned to the destination he was in until now, which looks weird)
                    mc.displayGuiScreen(null);
                    return true;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected void mouseClicked(int x, int y, int button)
    {
        super.mouseClicked(x, y, button);
        boolean clickHandled = false;

        if (this.selectedBody != null && x > BORDER_WIDTH + BORDER_EDGE_WIDTH && x < BORDER_WIDTH + BORDER_EDGE_WIDTH + 88 && y > BORDER_WIDTH + BORDER_EDGE_WIDTH && y < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
        {
            this.unselectCelestialBody();
            return;
        }

        if (!this.mapMode)
        {
            if (x > width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 96 && x < width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH && y > GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182 && y < GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182 + 12)
            {
                if (this.selectedBody == GalacticraftCore.planetOverworld && this.canCreateSpaceStation())
                {
                    final SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.selectedBody.getDimensionID());

                    if (recipe != null && recipe.matches(this.mc.thePlayer, false))
                    {
                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_BIND_SPACE_STATION_ID, new Object[] { this.selectedBody.getDimensionID() }));
                        //Zoom in on Overworld to show the new SpaceStation if not already zoomed
                        if (this.selectionCount < 2)
                        {
	                        this.selectionCount = 2;
	                        this.preSelectZoom = this.zoom;
	                        this.preSelectPosition = this.position;
	                        this.ticksSinceSelection = 0;
	                        this.doneZooming = false;
                        }
                    }

                    clickHandled = true;
                }
            }
        }

        if (this.mapMode)
        {
            if (x > width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88 && x < width - BORDER_WIDTH - BORDER_EDGE_WIDTH && y > BORDER_WIDTH + BORDER_EDGE_WIDTH && y < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
            {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                clickHandled = true;
            }
        }

        if (this.selectedBody != null && !this.mapMode)
        {
            if (x > width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88 && x < width - BORDER_WIDTH - BORDER_EDGE_WIDTH && y > BORDER_WIDTH + BORDER_EDGE_WIDTH && y < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
            {
                if (!(this.selectedBody instanceof Satellite) || !this.selectedStationOwner.equals(""))
                {
                    this.teleportToSelectedBody();
                }

                clickHandled = true;
            }
        }

        // Need unscaled mouse coords
        int mouseX = Mouse.getX();
        int mouseY = Mouse.getY() * -1 + Minecraft.getMinecraft().displayHeight - 1;

        if (this.selectedBody instanceof Satellite)
        {
            if (this.renamingSpaceStation)
            {
                if (x >= width / 2 - 90 && x <= width / 2 + 90 && y >= this.height / 2 - 38 && y <= this.height / 2 + 38)
                {
                    // Apply
                    if (x >= width / 2 - 90 + 17 && x <= width / 2 - 90 + 17 + 72 && y >= this.height / 2 - 38 + 59 && y <= this.height / 2 - 38 + 59 + 12)
                    {
                        String strName = this.mc.thePlayer.getGameProfile().getName();
                    	Integer spacestationID = this.spaceStationIDs.get(strName);
                    	if (spacestationID == null) spacestationID = this.spaceStationIDs.get(strName.toLowerCase());
                    	if (spacestationID != null)
                    	{
	                    	this.spaceStationNames.put(strName, this.renamingString);
	                        GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_RENAME_SPACE_STATION, new Object[] { this.renamingString, spacestationID }));
                    	}
                        this.renamingSpaceStation = false;
                    }
                    // Cancel
                    if (x >= width / 2 && x <= width / 2 + 72 && y >= this.height / 2 - 38 + 59 && y <= this.height / 2 - 38 + 59 + 12)
                    {
                        this.renamingSpaceStation = false;
                    }
                    clickHandled = true;
                }
            }
            else
            {
                this.drawTexturedModalRect(width / 2 - 47, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 94, 11, 0, 414, 188, 22, false, false);

                if (x >= width / 2 - 47 && x <= width / 2 - 47 + 94 && y >= GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH && y <= GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 11)
                {
                    if (this.selectedStationOwner.length() != 0 && this.selectedStationOwner.equalsIgnoreCase(this.mc.thePlayer.getGameProfile().getName()))
                    {
                        this.renamingSpaceStation = true;
                        this.renamingString = null;
                        clickHandled = true;
                    }
                }

                int max = Math.min((this.height / 2) / 14, this.spaceStationIDs.size());

                int xPos;
                int yPos;

                // Up button
                xPos = width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 85;
                yPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 45;

                if (x >= xPos && x <= xPos + 61 && y >= yPos && y <= yPos + 4)
                {
                    if (this.spaceStationListOffset > 0)
                    {
                        this.spaceStationListOffset--;
                    }
                    clickHandled = true;
                }

                // Down button
                xPos = width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 85;
                yPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 49 + max * 14;

                if (x >= xPos && x <= xPos + 61 && y >= yPos && y <= yPos + 4)
                {
                    if (max + spaceStationListOffset < this.spaceStationIDs.size())
                    {
                        this.spaceStationListOffset++;
                    }
                    clickHandled = true;
                }

                Iterator<Map.Entry<String, Integer>> it = this.spaceStationIDs.entrySet().iterator();
                int i = 0;
                int j = 0;
                while (it.hasNext() && i < max)
                {
                    Map.Entry<String, Integer> e = it.next();
                    if (j >= this.spaceStationListOffset)
                    {
                        int xOffset = 0;

                        if (e.getKey().equalsIgnoreCase(this.selectedStationOwner))
                        {
                            xOffset -= 5;
                        }

                        xPos = width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95 + xOffset;
                        yPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 50 + i * 14;

                        if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12)
                        {
                            this.selectedStationOwner = e.getKey();
                            clickHandled = true;
                        }
                        i++;
                    }
                    j++;
                }
            }
        }

        List<CelestialBody> children = this.getChildren(this.selectedParent);

        for (int i = 0; i < children.size(); i++)
        {
            CelestialBody child = children.get(i);
            int xOffset = 0;

            if (child.equals(this.selectedBody))
            {
                xOffset += 4;
            }

            int xPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 2 + xOffset;
            int yPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 50 + i * 14;

            if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12)
            {
                if (this.selectedBody != child || this.selectionCount < 2)
                {
                    if (this.selectedBody == null)
                    {
                        this.preSelectZoom = this.zoom;
                        this.preSelectPosition = this.position;
                    }

                    int selectionCountOld = this.selectionCount;

                    if (this.selectionCount > 0 && this.selectedBody != child)
                    {
                        this.unselectCelestialBody();
                    }

                    if (selectionCountOld == 2)
                    {
                        this.selectionCount = 1;
                    }

                    this.doneZooming = false;
                    this.planetZoom = 0.0F;

                    if (child != this.selectedBody)
                    {
                        this.lastSelectedBody = this.selectedBody;
                    }

                    this.selectedBody = child;
                    this.ticksSinceSelection = 0;
                    this.selectionCount++;
                    clickHandled = true;
                    break;
                }
            }
        }

        if (!clickHandled)
        {
            for (Map.Entry<CelestialBody, Vector3f> e : this.planetPosMap.entrySet())
            {
                CelestialBody bodyClicked = e.getKey();
            	if (this.selectedBody == null && bodyClicked instanceof IChildBody)
                {
                    continue;
                }

                float iconSize = e.getValue().z; // Z value holds size on-screen

                if (mouseX >= e.getValue().x - iconSize && mouseX <= e.getValue().x + iconSize && mouseY >= e.getValue().y - iconSize && mouseY <= e.getValue().y + iconSize)
                {
                    if (this.selectedBody != bodyClicked || this.selectionCount < 2)
                    {
                        if (this.selectionCount > 0 && this.selectedBody != bodyClicked)
                        {
                            if (!(this.selectedBody instanceof IChildBody && ((IChildBody) this.selectedBody).getParentPlanet() == bodyClicked))
                            {
                                this.unselectCelestialBody();
                            }
                            else if (this.selectionCount == 2)
                            {
                                this.selectionCount--;
                            }
                        }

                        this.doneZooming = false;
                        this.planetZoom = 0.0F;

                        if (bodyClicked != this.selectedBody)
                        {
                            this.lastSelectedBody = this.selectedBody;
                        }
                        
                        if (this.selectionCount == 1 && !(bodyClicked instanceof IChildBody))
                        {
                            this.preSelectZoom = this.zoom;
                            this.preSelectPosition = this.position;
                        }

                        this.selectedBody = bodyClicked;
                        this.ticksSinceSelection = 0;
                        this.selectionCount++;
                        
                        //Auto select if it's a spacestation and there is only a single entry
                        if (this.selectedBody instanceof Satellite && this.spaceStationIDs.entrySet().size() == 1)
                        {
                            Iterator<Map.Entry<String, Integer>> it = this.spaceStationIDs.entrySet().iterator();
                            this.selectedStationOwner = new String(it.next().getKey());
                        }
                        	
                        clickHandled = true;
                        break;
                    }
                }
            }
        }

        if (!clickHandled)
        {
            if (this.selectedBody != null)
            {
                this.unselectCelestialBody();
            }
        }

        Object selectedParent = this.selectedParent;

        if (this.selectedBody instanceof IChildBody)
        {
            selectedParent = ((IChildBody) this.selectedBody).getParentPlanet();
        }
        else if (this.selectedBody instanceof Planet)
        {
            selectedParent = ((Planet) this.selectedBody).getParentSolarSystem();
        }
        else if (this.selectedBody == null)
        {
            selectedParent = GalacticraftCore.solarSystemSol;
        }

        if (this.selectedParent != selectedParent)
        {
            this.selectedParent = selectedParent;
            this.ticksSinceMenuOpen = 0;
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mousePosX, int mousePosY, float partialTicks)
    {
        if (Mouse.hasWheel())
        {
            float wheel = Mouse.getDWheel() / (this.selectedBody == null ? 500.0F : 250.0F);

            if (wheel != 0)
            {
                if (this.selectedBody == null || (this.selectionState == EnumSelectionState.PREVIEW && this.selectionCount < 2))
                {
                	//Minimum zoom increased from 0.55F to 1F to allow zoom out to see other solar systems
                	this.zoom = Math.min(Math.max(this.zoom + wheel * ((this.zoom + 2.0F)) / 10.0F, -1.0F), 3);
                }
                else
                {
                    this.planetZoom = Math.min(Math.max(this.planetZoom + wheel, -4.9F), 5);
                }
            }
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

        Matrix4f camMatrix = new Matrix4f();
        Matrix4f.translate(new Vector3f(0.0F, 0.0F, -2000.0F), camMatrix, camMatrix); // See EntityRenderer.java:setupOverlayRendering
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.m00 = 2.0F / width;
        viewMatrix.m11 = 2.0F / -height;
        viewMatrix.m22 = -2.0F / 2000.0F;
        viewMatrix.m30 = -1.0F;
        viewMatrix.m31 = 1.0F;
        viewMatrix.m32 = -2.0F;

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
        fb.rewind();
        viewMatrix.store(fb);
        fb.flip();
        GL11.glMultMatrix(fb);
        fb.clear();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        fb.rewind();
        camMatrix.store(fb);
        fb.flip();
        fb.clear();
        GL11.glMultMatrix(fb);

        this.setBlackBackground();

        GL11.glPushMatrix();
        Matrix4f worldMatrix = this.setIsometric(partialTicks);
        mainWorldMatrix = worldMatrix;
        float gridSize = 7000F; //194.4F;
        //TODO: Add dynamic map sizing, to allow the map to be small by default and expand when more distant solar systems are added.
		this.drawGrid(gridSize, height / 3 / 3.5F);
        this.drawCircles();
        GL11.glPopMatrix();

        HashMap<CelestialBody, Matrix4f> matrixMap = this.drawCelestialBodies(worldMatrix);

        this.planetPosMap.clear();

        for (Map.Entry<CelestialBody, Matrix4f> e : matrixMap.entrySet())
        {
            Matrix4f planetMatrix = e.getValue();
            Matrix4f matrix0 = Matrix4f.mul(viewMatrix, planetMatrix, planetMatrix);
            int x = (int) Math.floor((matrix0.m30 * 0.5 + 0.5) * Minecraft.getMinecraft().displayWidth);
            int y = (int) Math.floor(Minecraft.getMinecraft().displayHeight - (matrix0.m31 * 0.5 + 0.5) * Minecraft.getMinecraft().displayHeight);
            Vector2f vec = new Vector2f(x, y);

            Matrix4f scaleVec = new Matrix4f();
            scaleVec.m00 = matrix0.m00;
            scaleVec.m11 = matrix0.m11;
            scaleVec.m22 = matrix0.m22;
            Vector4f newVec = Matrix4f.transform(scaleVec, new Vector4f(2, -2, 0, 0), null);
            float iconSize = (newVec.y * (Minecraft.getMinecraft().displayHeight / 2.0F)) * (e.getKey() instanceof Star ? 2 : 1) * (e.getKey() == this.selectedBody ? 1.5F : 1.0F);

            this.planetPosMap.put(e.getKey(), new Vector3f(vec.x, vec.y, iconSize)); // Store size on-screen in Z-value for ease
        }

        this.drawSelectionCursor(fb, worldMatrix);

        this.drawButtons(mousePosX, mousePosY);
        this.drawBorder();
        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    private void drawSelectionCursor(FloatBuffer fb, Matrix4f worldMatrix)
    {
        switch (this.selectionCount)
        {
        case 1:
            if (this.selectedBody != null)
            {
                GL11.glPushMatrix();
                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
                Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
                Matrix4f worldMatrix1 = new Matrix4f();
                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
                fb.rewind();
                worldMatrix1.store(fb);
                fb.flip();
                GL11.glMultMatrix(fb);
                fb.clear();
                GL11.glScalef(1 / 15.0F, 1 / 15.0F, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                float colMod = this.getZoomAdvanced() < 4.9F ? (float) (Math.sin(this.ticksSinceSelection / 2.0F) * 0.5F + 0.5F) : 1.0F;
                GL11.glColor4f(1.0F, 1.0F, 0.0F, 1 * colMod);
                int width = this.getWidthForCelestialBody(this.selectedBody) * 10;

                this.drawTexturedModalRect(-width, -width, width * 2, width * 2, 266, 29, 100, 100, false, false);
                GL11.glPopMatrix();
            }
            break;
        case 2:
            if (this.selectedBody != null)
            {
                GL11.glPushMatrix();
                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
                Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
                Matrix4f worldMatrix1 = new Matrix4f();
                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
                fb.rewind();
                worldMatrix1.store(fb);
                fb.flip();
                GL11.glMultMatrix(fb);
                fb.clear();
                float scale = Math.max(0.3F, 1.5F / (this.ticksSinceSelection / 5.0F));
                float scale0 = this.getZoomAdvanced() < 0 || this.getZoomAdvanced() > 1 ? scale + this.planetZoom / 75.0F - 0.45F : scale;//(0.6F / this.getZoomAdvanced()) : scale;
                GL11.glScalef(scale0, scale0, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                float colMod = this.getZoomAdvanced() < 4.9F ? (float) (Math.sin(this.ticksSinceSelection / 1.0F) * 0.5F + 0.5F) : 1.0F;
                GL11.glColor4f(0.4F, 0.8F, 1.0F, 1 * colMod);
                this.drawTexturedModalRect(-50, -50, 100, 100, 266, 29, 100, 100, false, false);
                GL11.glPopMatrix();
            }
            break;
        default:
            break;
        }
    }

    private Vector3f getCelestialBodyPosition(CelestialBody cBody)
    {
        if (cBody instanceof Star)
        {
        	if (cBody.getUnlocalizedName().equalsIgnoreCase("star.sol"))
        		 //Return zero vector for Sol, different location for other solar systems
        		return new Vector3f();
            return ((Star) cBody).getParentSolarSystem().getMapPosition().toVector3f();
        }

        int cBodyTicks = this.celestialBodyTicks.get(cBody);
        float timeScale = cBody instanceof Planet ? 200.0F : 2.0F;
        float distanceFromCenter = this.getScale(cBody);
        Vector3f cBodyPos = new Vector3f((float) Math.sin(cBodyTicks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, (float) Math.cos(cBodyTicks / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, 0);
        
        if (cBody instanceof Planet)
        {
        	Vector3f parentVec = this.getCelestialBodyPosition(((Planet) cBody).getParentSolarSystem().getMainStar());
        	return Vector3f.add(cBodyPos, parentVec, null);
        }
        
        if (cBody instanceof IChildBody)
        {
            Vector3f parentVec = this.getCelestialBodyPosition(((IChildBody) cBody).getParentPlanet());
            return Vector3f.add(cBodyPos, parentVec, null);
        }

        if (cBody instanceof Satellite)
        {
        	
            Vector3f parentVec = this.getCelestialBodyPosition(((Satellite) cBody).getParentPlanet());
            return Vector3f.add(cBodyPos, parentVec, null);
        }

        return cBodyPos;
    }

    private int getWidthForCelestialBody(CelestialBody celestialBody)
    {
        if (celestialBody != this.selectedBody || this.selectionCount != 1)
        {
            return celestialBody instanceof Star ? 8 : (celestialBody instanceof Planet ? 4 : (celestialBody instanceof IChildBody ? 4 : (celestialBody instanceof Satellite ? 4 : 2)));
        }

        return celestialBody instanceof Star ? 12 : (celestialBody instanceof Planet ? 6 : (celestialBody instanceof IChildBody ? 6 : (celestialBody instanceof Satellite ? 6 : 2)));
    }

    public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(Matrix4f worldMatrix)
    {
        GL11.glColor3f(1, 1, 1);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
        HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();

        for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
        {
            Star star = solarSystem.getMainStar();

            if (star != null && star.getBodyIcon() != null)
            {
                GL11.glPushMatrix();
                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
                
                Matrix4f.translate(this.getCelestialBodyPosition(star), worldMatrix0, worldMatrix0);

                Matrix4f worldMatrix1 = new Matrix4f();
                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);

                fb.rewind();
                worldMatrix1.store(fb);
                fb.flip();
                GL11.glMultMatrix(fb);

                float alpha = 1.0F;

                if (this.selectedBody != null && this.selectedBody != star && this.selectionCount >= 2)
                {
                    alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
                }

                if (this.selectedBody != null && this.selectionCount >= 2)
                {
                    if (star != this.selectedBody)
                    {
                        alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);

                        if (!(this.lastSelectedBody instanceof Star) && this.lastSelectedBody != null)
                        {
                            alpha = 0.0F;
                        }
                    }
                }

                if (alpha != 0)
                {
                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(star, star.getBodyIcon(), 8);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, alpha);
                    if (preEvent.celestialBodyTexture != null)
                    {
                        this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
                    }

                    if (!preEvent.isCanceled())
                    {
                        int size = this.getWidthForCelestialBody(star);
                        this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(star, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(star);
                    MinecraftForge.EVENT_BUS.post(postEvent);
                }
                
                fb.clear();
                GL11.glPopMatrix();
            }
        }

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            if (planet.getBodyIcon() != null)
            {
                GL11.glPushMatrix();
                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

                Matrix4f.translate(this.getCelestialBodyPosition(planet), worldMatrix0, worldMatrix0);

                Matrix4f worldMatrix1 = new Matrix4f();
                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);

                fb.rewind();
                worldMatrix1.store(fb);
                fb.flip();
                GL11.glMultMatrix(fb);

                float alpha = 1.0F;

                if ((this.selectedBody instanceof IChildBody && ((IChildBody) this.selectedBody).getParentPlanet() != planet) || (this.selectedBody instanceof Planet && this.selectedBody != planet && this.selectionCount >= 2))
                {
                    if (this.lastSelectedBody == null && !(this.selectedBody instanceof IChildBody))
                    {
                        alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
                    }
                    else
                    {
                        alpha = 0.0F;
                    }
                }

                if (alpha != 0)
                {
                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(planet, planet.getBodyIcon(), 12);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, alpha);
                    if (preEvent.celestialBodyTexture != null)
                    {
                        this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
                    }

                    if (!preEvent.isCanceled())
                    {
                        int size = this.getWidthForCelestialBody(planet);
                        this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(planet, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet);
                    MinecraftForge.EVENT_BUS.post(postEvent);
                }

                fb.clear();
                GL11.glPopMatrix();
            }
        }

        if (this.selectedBody != null)
        {
            Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

            for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
            {
                if ((moon == this.selectedBody || (moon.getParentPlanet() == this.selectedBody && this.selectionCount != 1)) && (this.ticksSinceSelection > 35 || this.selectedBody == moon || (this.lastSelectedBody instanceof Moon && GalaxyRegistry.getMoonsForPlanet(((Moon) this.lastSelectedBody).getParentPlanet()).contains(moon))))
                {
                    GL11.glPushMatrix();
                    Matrix4f worldMatrix1 = new Matrix4f(worldMatrix0);
                    Matrix4f.translate(this.getCelestialBodyPosition(moon), worldMatrix1, worldMatrix1);

                    Matrix4f worldMatrix2 = new Matrix4f();
                    Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix2, worldMatrix2);
                    Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix2, worldMatrix2);
                    Matrix4f.scale(new Vector3f(0.25F, 0.25F, 1.0F), worldMatrix2, worldMatrix2);
                    worldMatrix2 = Matrix4f.mul(worldMatrix1, worldMatrix2, worldMatrix2);

                    fb.rewind();
                    worldMatrix2.store(fb);
                    fb.flip();
                    GL11.glMultMatrix(fb);

                    CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(moon, moon.getBodyIcon(), 8);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    GL11.glColor4f(1, 1, 1, 1);
                    if (preEvent.celestialBodyTexture != null)
                    {
                        this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
                    }

                    if (!preEvent.isCanceled())
                    {
                        int size = this.getWidthForCelestialBody(moon);
                        this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                        matrixMap.put(moon, worldMatrix1);
                    }

                    CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(moon);
                    MinecraftForge.EVENT_BUS.post(postEvent);
                    fb.clear();
                    GL11.glPopMatrix();
                }
            }
        }

        if (this.selectedBody != null)
        {
            Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);

            for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
            {
                if (this.possibleBodies != null && this.possibleBodies.contains(satellite))
                {
                    if ((satellite == this.selectedBody || (satellite.getParentPlanet() == this.selectedBody && this.selectionCount != 1)) && (this.ticksSinceSelection > 35 || this.selectedBody == satellite || (this.lastSelectedBody instanceof Satellite && GalaxyRegistry.getSatellitesForCelestialBody(((Satellite) this.lastSelectedBody).getParentPlanet()).contains(satellite))))
                    {
                        GL11.glPushMatrix();
                        Matrix4f worldMatrix1 = new Matrix4f(worldMatrix0);
                        Matrix4f.translate(this.getCelestialBodyPosition(satellite), worldMatrix1, worldMatrix1);

                        Matrix4f worldMatrix2 = new Matrix4f();
                        Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix2, worldMatrix2);
                        Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix2, worldMatrix2);
                        Matrix4f.scale(new Vector3f(0.25F, 0.25F, 1.0F), worldMatrix2, worldMatrix2);
                        worldMatrix2 = Matrix4f.mul(worldMatrix1, worldMatrix2, worldMatrix2);

                        fb.rewind();
                        worldMatrix2.store(fb);
                        fb.flip();
                        GL11.glMultMatrix(fb);

                        CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(satellite, satellite.getBodyIcon(), 8);
                        MinecraftForge.EVENT_BUS.post(preEvent);

                        GL11.glColor4f(1, 1, 1, 1);
                        this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);

                        if (!preEvent.isCanceled())
                        {
                            int size = this.getWidthForCelestialBody(satellite);
                            this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize);
                            matrixMap.put(satellite, worldMatrix1);
                        }

                        CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(satellite);
                        MinecraftForge.EVENT_BUS.post(postEvent);
                        fb.clear();
                        GL11.glPopMatrix();
                    }
                }
            }
        }

        return matrixMap;
    }

    public void drawBorder()
    {
        Gui.drawRect(0, 0, GuiCelestialSelection.BORDER_WIDTH, height, ColorUtil.to32BitColor(255, 100, 100, 100));
        Gui.drawRect(width - GuiCelestialSelection.BORDER_WIDTH, 0, width, height, ColorUtil.to32BitColor(255, 100, 100, 100));
        Gui.drawRect(0, 0, width, GuiCelestialSelection.BORDER_WIDTH, ColorUtil.to32BitColor(255, 100, 100, 100));
        Gui.drawRect(0, height - GuiCelestialSelection.BORDER_WIDTH, width, height, ColorUtil.to32BitColor(255, 100, 100, 100));
        Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, ColorUtil.to32BitColor(255, 40, 40, 40));
        Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, ColorUtil.to32BitColor(255, 40, 40, 40));
        Gui.drawRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH, GuiCelestialSelection.BORDER_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, ColorUtil.to32BitColor(255, 80, 80, 80));
        Gui.drawRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH, width - GuiCelestialSelection.BORDER_WIDTH, height - GuiCelestialSelection.BORDER_WIDTH, ColorUtil.to32BitColor(255, 80, 80, 80));
    }

    public void drawButtons(int mousePosX, int mousePosY)
    {
        this.zLevel = 0.0F;
        boolean handledSliderPos = false;

        if (this.selectionState == EnumSelectionState.PROFILE)
        {
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.drawTexturedModalRect(width / 2 - 43, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 86, 15, 266, 0, 172, 29, false, false);
            String str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
            this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2, ColorUtil.to32BitColor(255, 255, 255, 255));

            if (this.selectedBody != null)
            {
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);

                if (mousePosX > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosX < BORDER_WIDTH + BORDER_EDGE_WIDTH + 88 && mousePosY > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosY < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
                {
                    GL11.glColor3f(3.0F, 0.0F, 0.0F);
                }
                else
                {
                    GL11.glColor3f(0.9F, 0.2F, 0.2F);
                }

                this.drawTexturedModalRect(BORDER_WIDTH + BORDER_EDGE_WIDTH, BORDER_WIDTH + BORDER_EDGE_WIDTH, 88, 13, 0, 392, 148, 22, false, false);
                str = GCCoreUtil.translate("gui.message.back.name").toUpperCase();
                this.fontRendererObj.drawString(str, BORDER_WIDTH + BORDER_EDGE_WIDTH + 45 - this.fontRendererObj.getStringWidth(str) / 2, BORDER_WIDTH + BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2 - 2, ColorUtil.to32BitColor(255, 255, 255, 255));

                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                if (mousePosX > width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88 && mousePosX < width - BORDER_WIDTH - BORDER_EDGE_WIDTH && mousePosY > BORDER_WIDTH + BORDER_EDGE_WIDTH && mousePosY < BORDER_WIDTH + BORDER_EDGE_WIDTH + 13)
                {
                    GL11.glColor3f(0.0F, 3.0F, 0.0F);
                }
                else
                {
                    GL11.glColor3f(0.2F, 0.9F, 0.2F);
                }

                this.drawTexturedModalRect(width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88, BORDER_WIDTH + BORDER_EDGE_WIDTH, 88, 13, 0, 392, 148, 22, true, false);

                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                this.drawTexturedModalRect(BORDER_WIDTH + BORDER_EDGE_WIDTH, height - BORDER_WIDTH - BORDER_EDGE_WIDTH - 13, 88, 13, 0, 392, 148, 22, false, true);
                this.drawTexturedModalRect(width - BORDER_WIDTH - BORDER_EDGE_WIDTH - 88, height - BORDER_WIDTH - BORDER_EDGE_WIDTH - 13, 88, 13, 0, 392, 148, 22, true, true);
                int menuTopLeft = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH - 115 + height / 2 - 4;
                int posX = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + Math.min(this.ticksSinceSelection * 10, 133) - 134;
                int posX2 = (int) (GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + Math.min(this.ticksSinceSelection * 1.25F, 15) - 15);
                int fontPosY = menuTopLeft + GuiCelestialSelection.BORDER_EDGE_WIDTH + this.fontRendererObj.FONT_HEIGHT / 2 - 2;
                this.drawTexturedModalRect(posX, menuTopLeft + 12, 133, 196, 0, 0, 266, 392, false, false);

//			str = this.selectedBody.getLocalizedName();
//			this.fontRendererObj.drawString(str, posX + 20, fontPosY, GCCoreUtil.to32BitColor(255, 255, 255, 255));

                str = GCCoreUtil.translate("gui.message.daynightcycle.name") + ":";
                this.fontRendererObj.drawString(str, posX + 5, fontPosY + 14, ColorUtil.to32BitColor(255, 150, 200, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.0.name");
                this.fontRendererObj.drawString(str, posX + 10, fontPosY + 25, ColorUtil.to32BitColor(255, 255, 255, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.1.name");
                if (!str.isEmpty())
                {
                    this.fontRendererObj.drawString(str, posX + 10, fontPosY + 36, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                str = GCCoreUtil.translate("gui.message.surfacegravity.name") + ":";
                this.fontRendererObj.drawString(str, posX + 5, fontPosY + 50, ColorUtil.to32BitColor(255, 150, 200, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.0.name");
                this.fontRendererObj.drawString(str, posX + 10, fontPosY + 61, ColorUtil.to32BitColor(255, 255, 255, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.1.name");
                if (!str.isEmpty())
                {
                    this.fontRendererObj.drawString(str, posX + 10, fontPosY + 72, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                str = GCCoreUtil.translate("gui.message.surfacecomposition.name") + ":";
                this.fontRendererObj.drawString(str, posX + 5, fontPosY + 88, ColorUtil.to32BitColor(255, 150, 200, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.0.name");
                this.fontRendererObj.drawString(str, posX + 10, fontPosY + 99, ColorUtil.to32BitColor(255, 255, 255, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.1.name");
                if (!str.isEmpty())
                {
                    this.fontRendererObj.drawString(str, posX + 10, fontPosY + 110, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                str = GCCoreUtil.translate("gui.message.atmosphere.name") + ":";
                this.fontRendererObj.drawString(str, posX + 5, fontPosY + 126, ColorUtil.to32BitColor(255, 150, 200, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.0.name");
                this.fontRendererObj.drawString(str, posX + 10, fontPosY + 137, ColorUtil.to32BitColor(255, 255, 255, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.1.name");
                if (!str.isEmpty())
                {
                    this.fontRendererObj.drawString(str, posX + 10, fontPosY + 148, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                str = GCCoreUtil.translate("gui.message.meansurfacetemp.name") + ":";
                this.fontRendererObj.drawString(str, posX + 5, fontPosY + 165, ColorUtil.to32BitColor(255, 150, 200, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.0.name");
                this.fontRendererObj.drawString(str, posX + 10, fontPosY + 176, ColorUtil.to32BitColor(255, 255, 255, 255));
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.1.name");
                if (!str.isEmpty())
                {
                    this.fontRendererObj.drawString(str, posX + 10, fontPosY + 187, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                this.drawTexturedModalRect(posX2, menuTopLeft + 12, 17, 199, 439, 0, 32, 399, false, false);
//			this.drawRectD(posX2 + 16.5, menuTopLeft + 13, posX + 131, menuTopLeft + 14, GCCoreUtil.to32BitColor(120, 0, (int) (0.6F * 255), 255));
            }
        }
        else
        {
            String str;
            // Catalog:
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 74, 11, 0, 392, 148, 22, false, false);
            str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
            this.fontRendererObj.drawString(str, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 40 - fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 1, ColorUtil.to32BitColor(255, 255, 255, 255));

            int scale = (int) Math.min(95, this.ticksSinceMenuOpen * 12.0F);

            // Parent frame:
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH - 95 + scale, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 12, 95, 41, 0, 436, 95, 41, false, false);
            str = this.getParentName();
            this.fontRendererObj.drawString(str, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 9 - 95 + scale, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 34, ColorUtil.to32BitColor(255, 255, 255, 255));
            GL11.glColor4f(1, 1, 0, 1);
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);

            // Grandparent frame:
            this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 2 - 95 + scale, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 14, 93, 17, 95, 436, 93, 17, false, false);
            str = this.getGrandparentName();
            this.fontRendererObj.drawString(str, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 7 - 95 + scale, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 16, ColorUtil.to32BitColor(255, 120, 120, 120));
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

            List<CelestialBody> children = this.getChildren(this.selectedParent);

            for (int i = 0; i < children.size(); i++)
            {
                CelestialBody child = children.get(i);
                int xOffset = 0;

                if (child.equals(this.selectedBody))
                {
                    xOffset += 4;
                }

                scale = (int) Math.min(95.0F, Math.max(0.0F, (this.ticksSinceMenuOpen * 25.0F) - 95 * i));

                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                if (child.getReachable())
                {
                    GL11.glColor4f(0.0F, 0.6F, 0.0F, scale / 95.0F);
                }
                else
                {
                    GL11.glColor4f(0.6F, 0.0F, 0.0F, scale / 95.0F);
                }
                this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 3 + xOffset, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 51 + i * 14, 86, 10, 0, 489, 86, 10, false, false);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, scale / 95.0F);
                this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 2 + xOffset, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 50 + i * 14, 93, 12, 95, 464, 93, 12, false, false);

                if (scale > 0)
                {
                    str = child.getLocalizedName();
                    this.fontRendererObj.drawString(str, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 7 + xOffset, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 52 + i * 14, ColorUtil.to32BitColor(255, 255, 255, 255));
                }
            }

            if (this.mapMode)
            {
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 74, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 74, 11, 0, 392, 148, 22, true, false);
                str = GCCoreUtil.translate("gui.message.exit.name").toUpperCase();
                this.fontRendererObj.drawString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 40 - fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 1, ColorUtil.to32BitColor(255, 255, 255, 255));
            }

            if (this.selectedBody != null)
            {
                // Right-hand bar (basic selection info)
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

                if (this.selectedBody instanceof Satellite)
                {
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                    int max = Math.min((this.height / 2) / 14, this.spaceStationIDs.size());
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 95, 53, this.selectedStationOwner.length() == 0 ? 95 : 0, 186, 95, 53, false, false);
                    if (this.spaceStationListOffset <= 0)
                    {
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    }
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 85, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 45, 61, 4, 0, 239, 61, 4, false, false);
                    if (max + spaceStationListOffset >= this.spaceStationIDs.size())
                    {
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    }
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 85, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 49 + max * 14, 61, 4, 0, 239, 61, 4, false, true);
                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

                    if (this.spaceStationIDs.get(this.selectedStationOwner) == null)
                    {
                        str = GCCoreUtil.translate("gui.message.selectSS.name");
                        this.drawSplitString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 47, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 20, 91, ColorUtil.to32BitColor(255, 255, 255, 255));
                    }
                    else
                    {
                        str = GCCoreUtil.translate("gui.message.ssOwner.name");
                        this.fontRendererObj.drawString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 85, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 18, ColorUtil.to32BitColor(255, 255, 255, 255));
                        str = this.selectedStationOwner;
                        this.smallFontRenderer.drawString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 47 - this.smallFontRenderer.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 30, ColorUtil.to32BitColor(255, 255, 255, 255));
                    }

                    Iterator<Map.Entry<String, Integer>> it = this.spaceStationIDs.entrySet().iterator();
                    int i = 0;
                    int j = 0;
                    while (it.hasNext() && i < max)
                    {
                        Map.Entry<String, Integer> e = it.next();

                        if (j >= this.spaceStationListOffset)
                        {
                            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                            int xOffset = 0;

                            if (e.getKey().equalsIgnoreCase(this.selectedStationOwner))
                            {
                                xOffset -= 5;
                            }

                            this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95 + xOffset, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 50 + i * 14, 93, 12, 95, 464, 93, 12, true, false);
                            str = "";
                            String str0 = this.spaceStationNames.get(e.getKey());
                            int point = 0;
                            while (this.smallFontRenderer.getStringWidth(str) < 80 && point < str0.length())
                            {
                                str = str + str0.substring(point, point + 1);
                                point++;
                            }
                            if (this.smallFontRenderer.getStringWidth(str) >= 80)
                            {
                                str = str.substring(0, str.length() - 3);
                                str = str + "...";
                            }
                            this.smallFontRenderer.drawString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 88 + xOffset, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 51 + i * 14, ColorUtil.to32BitColor(255, 255, 255, 255));
                            i++;
                        }
                        j++;
                    }
                }
                else
                {
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 96, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 96, 139, 63, 0, 96, 139, false, false);
                }

                if (this.canCreateSpaceStation() && (!(this.selectedBody instanceof Satellite)))
                {
                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 134, 93, 47, 159, 102, 93, 47, false, false);
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 79, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 129, 61, 4, 0, 170, 61, 4, false, false);

                    if (this.selectedBody == GalacticraftCore.planetOverworld)
                    {
                        GL11.glColor4f(0.0F, 1.0F, 0.1F, 1);
                        SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.selectedBody.getDimensionID());
                        boolean validInputMaterials = true;

                        int i = 0;
                        for (Map.Entry<Object, Integer> e : recipe.getInput().entrySet())
                        {
                            Object next = e.getKey();
                            int xPos = (int)(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95 + i * 93 / (double)recipe.getInput().size() + 5);
                            int yPos = GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 154;

                            if (next instanceof ItemStack)
                            {
                                int amount = getAmountInInventory((ItemStack) next);
                                RenderHelper.enableGUIStandardItemLighting();
                                GuiCelestialSelection.itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, ((ItemStack) next).copy(), xPos, yPos);
                                RenderHelper.disableStandardItemLighting();
                                GL11.glEnable(GL11.GL_BLEND);

                                if (mousePosX >= xPos && mousePosX <= xPos + 16 && mousePosY >= yPos && mousePosY <= yPos + 16)
                                {
                                    GL11.glDepthMask(true);
                                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                                    GL11.glPushMatrix();
                                    GL11.glTranslatef(0, 0, 300);
                                    int k = this.smallFontRenderer.getStringWidth(((ItemStack) next).getDisplayName());
                                    int j2 = mousePosX - k / 2;
                                    int k2 = mousePosY - 12;
                                    int i1 = 8;

                                    if (j2 + k > this.width)
                                    {
                                        j2 -= (j2 - this.width + k);
                                    }

                                    if (k2 + i1 + 6 > this.height)
                                    {
                                        k2 = this.height - i1 - 6;
                                    }

                                    int j1 = ColorUtil.to32BitColor(190, 0, 153, 255);
                                    this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
                                    this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
                                    this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
                                    this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
                                    this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
                                    int k1 = ColorUtil.to32BitColor(170, 0, 153, 255);
                                    int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
                                    this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
                                    this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
                                    this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
                                    this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

                                    this.smallFontRenderer.drawString(((ItemStack) next).getDisplayName(), j2, k2, ColorUtil.to32BitColor(255, 255, 255, 255));

                                    GL11.glPopMatrix();
                                }

                                str = "" + amount + "/" + e.getValue();
                                boolean valid = amount >= e.getValue();
                                if (!valid && validInputMaterials)
                                {
                                    validInputMaterials = false;
                                }
                                int color = valid ? ColorUtil.to32BitColor(255, 0, 255, 0) : ColorUtil.to32BitColor(255, 255, 0, 0);
                                this.smallFontRenderer.drawString(str, xPos + 8 - this.smallFontRenderer.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 170, color);
                            }
                            else if (next instanceof ArrayList)
                            {
                                ArrayList<ItemStack> items = (ArrayList<ItemStack>) next;

                                int amount = 0;

                                for (ItemStack stack : items)
                                {
                                    amount += getAmountInInventory(stack);
                                }

                                RenderHelper.enableGUIStandardItemLighting();
                                ItemStack stack = items.get((this.ticksSinceMenuOpen / 20) % items.size()).copy();
                                GuiCelestialSelection.itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.renderEngine, stack, xPos, yPos);
                                RenderHelper.disableStandardItemLighting();
                                GL11.glEnable(GL11.GL_BLEND);

                                if (mousePosX >= xPos && mousePosX <= xPos + 16 && mousePosY >= yPos && mousePosY <= yPos + 16)
                                {
                                    GL11.glDepthMask(true);
                                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                                    GL11.glPushMatrix();
                                    GL11.glTranslatef(0, 0, 300);
                                    int k = this.smallFontRenderer.getStringWidth(stack.getDisplayName());
                                    int j2 = mousePosX - k / 2;
                                    int k2 = mousePosY - 12;
                                    int i1 = 8;

                                    if (j2 + k > this.width)
                                    {
                                        j2 -= (j2 - this.width + k);
                                    }

                                    if (k2 + i1 + 6 > this.height)
                                    {
                                        k2 = this.height - i1 - 6;
                                    }

                                    int j1 = ColorUtil.to32BitColor(190, 0, 153, 255);
                                    this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
                                    this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
                                    this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
                                    this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
                                    this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
                                    int k1 = ColorUtil.to32BitColor(170, 0, 153, 255);
                                    int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
                                    this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
                                    this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
                                    this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
                                    this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

                                    this.smallFontRenderer.drawString(stack.getDisplayName(), j2, k2, ColorUtil.to32BitColor(255, 255, 255, 255));

                                    GL11.glPopMatrix();
                                }

                                str = "" + amount + "/" + e.getValue();
                                boolean valid = amount >= e.getValue();
                                if (!valid && validInputMaterials)
                                {
                                    validInputMaterials = false;
                                }
                                int color = valid ? ColorUtil.to32BitColor(255, 0, 255, 0) : ColorUtil.to32BitColor(255, 255, 0, 0);
                                this.smallFontRenderer.drawString(str, xPos + 8 - this.smallFontRenderer.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 170, color);
                            }

                            i++;
                        }

                        if (validInputMaterials)
                        {
                            GL11.glColor4f(0.0F, 1.0F, 0.1F, 1);
                        }
                        else
                        {
                            GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                        }

                        this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);

                        if (!this.mapMode)
                        {
                            if (mousePosX >= width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95 && mousePosX <= width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH && mousePosY >= GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182 && mousePosY <= GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182 + 12)
                            {
                                this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182, 93, 12, 0, 174, 93, 12, false, false);
                            }
                        }

                        this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 95, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 182, 93, 12, 0, 174, 93, 12, false, false);

                        int color = (int)((Math.sin(this.ticksSinceMenuOpen / 5.0) * 0.5 + 0.5) * 255);
                        this.drawSplitString(GCCoreUtil.translate("gui.message.canCreateSpaceStation.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 48, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 137, 91, ColorUtil.to32BitColor(255, color, 255, color), true);

                        if (!mapMode)
                        {
                            this.drawSplitString(GCCoreUtil.translate("gui.message.createSS.name").toUpperCase(), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 48, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 185, 91, ColorUtil.to32BitColor(255, 255, 255, 255));
                        }
                    }
                    else
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.cannotCreateSpaceStation.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 48, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 138, 91, ColorUtil.to32BitColor(255, 255, 255, 255));
                    }
                }

                // Catalog overlay
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F - Math.min(0.3F, this.ticksSinceSelection / 50.0F));
                this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 74, 11, 0, 392, 148, 22, false, false);
                str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
                this.fontRendererObj.drawString(str, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 40 - fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 1, ColorUtil.to32BitColor(255, 255, 255, 255));

                // Top bar title:
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                if (this.selectedBody instanceof Satellite)
                {
                    if (this.selectedStationOwner.length() == 0 || !this.selectedStationOwner.equalsIgnoreCase(this.mc.thePlayer.getGameProfile().getName()))
                    {
                        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1);
                    }
                    this.drawTexturedModalRect(width / 2 - 47, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 94, 11, 0, 414, 188, 22, false, false);
                }
                else
                {
                    this.drawTexturedModalRect(width / 2 - 47, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 94, 11, 0, 414, 188, 22, false, false);
                }
                if (this.selectedBody.getTierRequirement() >= 0 && (!(this.selectedBody instanceof Satellite)))
                {
                    boolean canReach;
                    if (!this.selectedBody.getReachable() || (this.possibleBodies != null && !this.possibleBodies.contains(this.selectedBody)))
                    {
                        canReach = false;
                        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                    }
                    else
                    {
                        canReach = true;
                        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1);
                    }
                    this.drawTexturedModalRect(width / 2 - 30, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 11, 30, 11, 0, 414, 60, 22, false, false);
                    this.drawTexturedModalRect(width / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 11, 30, 11, 128, 414, 60, 22, false, false);
                    str = GCCoreUtil.translateWithFormat("gui.message.tier.name", this.selectedBody.getTierRequirement() == 0 ? "?" : this.selectedBody.getTierRequirement());
                    this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 13, canReach ? ColorUtil.to32BitColor(255, 140, 140, 140) : ColorUtil.to32BitColor(255, 255, 100, 100));
                }

                str = this.selectedBody.getLocalizedName();

                if (this.selectedBody instanceof Satellite)
                {
                    str = GCCoreUtil.translate("gui.message.rename.name").toUpperCase();
                }

                this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 2, ColorUtil.to32BitColor(255, 255, 255, 255));

                // Catalog wedge:
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                this.drawTexturedModalRect(GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 4, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 83, 12, 0, 477, 83, 12, false, false);

                if (!this.mapMode)
                {
                    if (!this.selectedBody.getReachable() || (this.possibleBodies != null && !this.possibleBodies.contains(this.selectedBody)) || (this.selectedBody instanceof Satellite && this.selectedStationOwner.equals("")))
                    {
                        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1);
                    }

                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 74, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH, 74, 11, 0, 392, 148, 22, true, false);
                    str = GCCoreUtil.translate("gui.message.launch.name").toUpperCase();
                    this.fontRendererObj.drawString(str, width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 40 - fontRendererObj.getStringWidth(str) / 2, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 1, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

                if (this.selectionCount == 1 && !(this.selectedBody instanceof Satellite))
                {
                    handledSliderPos = true;

                    int sliderPos = this.zoomTooltipPos;
                    if (zoomTooltipPos != 38)
                    {
                        sliderPos = Math.min(this.ticksSinceSelection * 2, 38);
                        this.zoomTooltipPos = sliderPos;
                    }

                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                    this.drawTexturedModalRect(width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 182, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH - 38 + sliderPos, 83, 38, 512 - 166, 512 - 76, 166, 76, true, false);

                    boolean flag0 = GalaxyRegistry.getSatellitesForCelestialBody(this.selectedBody).size() > 0;
                    boolean flag1 = this.selectedBody instanceof Planet ? GalaxyRegistry.getMoonsForPlanet((Planet) this.selectedBody).size() > 0 : false;
                    if (flag0 && flag1)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.clickAgain.0.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 182 + 41, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 1 - 38 + sliderPos, 79, ColorUtil.to32BitColor(255, 150, 150, 150));
                    }
                    else if (!flag0 && flag1)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.clickAgain.1.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 182 + 41, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 5 - 38 + sliderPos, 79, ColorUtil.to32BitColor(255, 150, 150, 150));
                    }
                    else if (flag0)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.clickAgain.2.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 182 + 41, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 5 - 38 + sliderPos, 79, ColorUtil.to32BitColor(255, 150, 150, 150));
                    }
                    else
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.clickAgain.3.name"), width - GuiCelestialSelection.BORDER_WIDTH - GuiCelestialSelection.BORDER_EDGE_WIDTH - 182 + 41, GuiCelestialSelection.BORDER_WIDTH + GuiCelestialSelection.BORDER_EDGE_WIDTH + 10 - 38 + sliderPos, 79, ColorUtil.to32BitColor(255, 150, 150, 150));
                    }

                }

                if (this.selectedBody instanceof Satellite && renamingSpaceStation)
                {
                    this.drawDefaultBackground();
                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                    this.drawTexturedModalRect(width / 2 - 90, this.height / 2 - 38, 179, 67, 159, 0, 179, 67, false, false);
                    this.drawTexturedModalRect(width / 2 - 90 + 4, this.height / 2 - 38 + 2, 171, 10, 159, 92, 171, 10, false, false);
                    this.drawTexturedModalRect(width / 2 - 90 + 8, this.height / 2 - 38 + 18, 161, 13, 159, 67, 161, 13, false, false);
                    this.drawTexturedModalRect(width / 2 - 90 + 17, this.height / 2 - 38 + 59, 72, 12, 159, 80, 72, 12, true, false);
                    this.drawTexturedModalRect(width / 2, this.height / 2 - 38 + 59, 72, 12, 159, 80, 72, 12, false, false);
                    str = GCCoreUtil.translate("gui.message.assignName.name");
                    this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, this.height / 2 - 35, ColorUtil.to32BitColor(255, 255, 255, 255));
                    str = GCCoreUtil.translate("gui.message.apply.name");
                    this.fontRendererObj.drawString(str, width / 2 - this.fontRendererObj.getStringWidth(str) / 2 - 36, this.height / 2 + 23, ColorUtil.to32BitColor(255, 255, 255, 255));
                    str = GCCoreUtil.translate("gui.message.cancel.name");
                    this.fontRendererObj.drawString(str, width / 2 + 36 - this.fontRendererObj.getStringWidth(str) / 2, this.height / 2 + 23, ColorUtil.to32BitColor(255, 255, 255, 255));

                    if (this.renamingString == null)
                    {
                        this.renamingString = this.spaceStationNames.get(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName());
                        if (this.renamingString == null) this.renamingString = this.spaceStationNames.get(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName().toLowerCase());
                        if (this.renamingString == null) this.renamingString = "";
                    }

                    str = this.renamingString;
                    String str0 = this.renamingString;

                    if ((this.ticksSinceMenuOpen / 10) % 2 == 0)
                    {
                        str0 += "_";
                    }

                    this.fontRendererObj.drawString(str0, width / 2 - this.fontRendererObj.getStringWidth(str) / 2, this.height / 2 - 17, ColorUtil.to32BitColor(255, 255, 255, 255));
                }

//                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
//                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            }
        }

        if (!handledSliderPos)
        {
            this.zoomTooltipPos = 0;
        }
    }

    private int getAmountInInventory(ItemStack stack)
    {
        int amountInInv = 0;

        for (int x = 0; x < FMLClientHandler.instance().getClientPlayerEntity().inventory.getSizeInventory(); x++)
        {
            final ItemStack slot = FMLClientHandler.instance().getClientPlayerEntity().inventory.getStackInSlot(x);

            if (slot != null)
            {
                if (SpaceStationRecipe.checkItemEquals(stack, slot))
                {
                    amountInInv += slot.stackSize;
                }
            }
        }

        return amountInInv;
    }

    public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5)
    {
        this.drawSplitString(par1Str, par2, par3, par4, par5, false);
    }

    public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5, boolean small)
    {
        this.renderSplitString(par1Str, par2, par3, par4, false, par5, small);
    }

    private void renderSplitString(String par1Str, int par2, int par3, int par4, boolean par5, int par6, boolean small)
    {
        if (small)
        {
            List list = this.smallFontRenderer.listFormattedStringToWidth(par1Str, par4);

            for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += this.smallFontRenderer.FONT_HEIGHT)
            {
                String s1 = (String) iterator.next();
                this.renderStringAligned(s1, par2, par3, par4, par6, par5, small);
            }
        }
        else
        {
            List list = this.fontRendererObj.listFormattedStringToWidth(par1Str, par4);

            for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += this.fontRendererObj.FONT_HEIGHT)
            {
                String s1 = (String) iterator.next();
                this.renderStringAligned(s1, par2, par3, par4, par6, par5, small);
            }
        }
    }

    private int renderStringAligned(String par1Str, int par2, int par3, int par4, int par5, boolean par6, boolean small)
    {
        if (small)
        {
            if (this.smallFontRenderer.getBidiFlag())
            {
                int i1 = this.smallFontRenderer.getStringWidth(this.bidiReorder(par1Str));
                par2 = par2 + par4 - i1;
            }

            return this.smallFontRenderer.drawString(par1Str, par2 - this.smallFontRenderer.getStringWidth(par1Str) / 2, par3, par5, par6);
        }
        else
        {
            if (this.fontRendererObj.getBidiFlag())
            {
                int i1 = this.fontRendererObj.getStringWidth(this.bidiReorder(par1Str));
                par2 = par2 + par4 - i1;
            }

            return this.fontRendererObj.drawString(par1Str, par2 - this.fontRendererObj.getStringWidth(par1Str) / 2, par3, par5, par6);
        }
    }

    private String bidiReorder(String p_147647_1_)
    {
        try
        {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException arabicshapingexception)
        {
            return p_147647_1_;
        }
    }

    public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invertX, boolean invertY)
    {
        this.drawTexturedModalRect(x, y, width, height, u, v, uWidth, vHeight, invertX, invertY, 512);
    }

    public void drawTexturedModalRect(int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, boolean invertX, boolean invertY, int texSize)
    {
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float texMod = 1 / (float) texSize;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        int height0 = invertY ? 0 : vHeight;
        int height1 = invertY ? vHeight : 0;
        int width0 = invertX ? uWidth : 0;
        int width1 = invertX ? 0 : uWidth;
        tessellator.addVertexWithUV(x, y + height, this.zLevel, (u + width0) * texMod, (v + height0) * texMod);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (u + width1) * texMod, (v + height0) * texMod);
        tessellator.addVertexWithUV(x + width, y, this.zLevel, (u + width1) * texMod, (v + height1) * texMod);
        tessellator.addVertexWithUV(x, y, this.zLevel, (u + width0) * texMod, (v + height1) * texMod);
        tessellator.draw();
    }

    public void setBlackBackground()
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator var3 = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        var3.startDrawingQuads();
        var3.addVertex(0.0D, height, -90.0D);
        var3.addVertex(width, height, -90.0D);
        var3.addVertex(width, 0.0D, -90.0D);
        var3.addVertex(0.0D, 0.0D, -90.0D);
        var3.draw();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public Matrix4f setIsometric(float partialTicks)
    {
        Matrix4f mat0 = new Matrix4f();
        Matrix4f.translate(new Vector3f(width / 2.0F + translation.x, height / 2 + translation.y, 0), mat0, mat0);
        Matrix4f.rotate((float) Math.toRadians(55), new Vector3f(1, 0, 0), mat0, mat0);
        Matrix4f.rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1), mat0, mat0);
        float zoomLocal = this.getZoomAdvanced();
        this.zoom = this.getZoomAdvanced();
        Matrix4f.scale(new Vector3f(1.1f + zoomLocal, 1.1F + zoomLocal, 1.1F + zoomLocal), mat0, mat0);
        Vector2f cBodyPos = this.getTranslationAdvanced(partialTicks);
        this.position = this.getTranslationAdvanced(partialTicks);
        Matrix4f.translate(new Vector3f(-cBodyPos.x, -cBodyPos.y, 0), mat0, mat0);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        fb.rewind();
        mat0.store(fb);
        fb.flip();
        GL11.glMultMatrix(fb);
        return mat0;
    }

    public void drawGrid(float gridSize, float gridScale)
    {
        GL11.glColor4f(0.0F, 0.2F, 0.5F, 0.55F);

        GL11.glBegin(GL11.GL_LINES);

        gridSize += gridScale / 2;
        for (float v = -gridSize; v <= gridSize; v += gridScale)
        {
            GL11.glVertex3f(v, -gridSize, -0.0F);
            GL11.glVertex3f(v, gridSize, -0.0F);
            GL11.glVertex3f(-gridSize, v, -0.0F);
            GL11.glVertex3f(gridSize, v, -0.0F);
        }

        GL11.glEnd();
    }

    public void drawCircles()
    {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glLineWidth(3);
        int count = 0;

        final float theta = (float) (2 * Math.PI / 90);
        final float cos = (float) Math.cos(theta);
        final float sin = (float) Math.sin(theta);

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {	
            if (planet.getParentSolarSystem() != null)
            {
            	Vector3f systemOffset = this.getCelestialBodyPosition(planet.getParentSolarSystem().getMainStar());
            	
            	float x = this.getScale(planet);
                float y = 0;

                float alpha = 1.0F;

                if ((this.selectedBody instanceof IChildBody && ((IChildBody) this.selectedBody).getParentPlanet() != planet) || (this.selectedBody instanceof Planet && this.selectedBody != planet && this.selectionCount >= 2))
                {
                    if (this.lastSelectedBody == null && !(this.selectedBody instanceof IChildBody) && !(this.selectedBody instanceof Satellite))
                    {
                        alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
                    }
                    else
                    {
                        alpha = 0.0F;
                    }
                }

                if (alpha != 0)
                {
                    switch (count % 2)
                    {
                    case 0:
                        GL11.glColor4f(0.0F / 1.4F, 0.6F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
                        break;
                    case 1:
                        GL11.glColor4f(0.4F / 1.4F, 0.9F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
                        break;
                    }

                    CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(planet);
                    MinecraftForge.EVENT_BUS.post(preEvent);

                    if (!preEvent.isCanceled())
                    {
                    	GL11.glTranslatef(systemOffset.x, systemOffset.y, systemOffset.z);
                    	
                        GL11.glBegin(GL11.GL_LINE_LOOP);

                        float temp;
                        for (int i = 0; i < 90; i++)
                        {
                            GL11.glVertex2f(x, y);

                            temp = x;
                            x = cos * x - sin * y;
                            y = sin * temp + cos * y;
                        }
                        
                        GL11.glEnd();
                        
                    	GL11.glTranslatef(-systemOffset.x, -systemOffset.y, -systemOffset.z);
                        
                        count++;
                    }

                    CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(planet);
                    MinecraftForge.EVENT_BUS.post(postEvent);
                }
            }
        }

        count = 0;

        if (this.selectedBody != null)
        {
            Vector3f planetPos = this.getCelestialBodyPosition(this.selectedBody);

            if (this.selectedBody instanceof IChildBody)
            {
                planetPos = this.getCelestialBodyPosition(((IChildBody) this.selectedBody).getParentPlanet());
            }
            else if (this.selectedBody instanceof Satellite)
            {
                planetPos = this.getCelestialBodyPosition(((Satellite) this.selectedBody).getParentPlanet());
            }

            GL11.glTranslatef(planetPos.x, planetPos.y, 0);

            for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
            {
                if ((moon.getParentPlanet() == this.selectedBody && this.selectionCount != 1) && this.ticksSinceSelection > 24 || moon == this.selectedBody || this.lastSelectedBody instanceof IChildBody)
                {
                    float x = this.getScale(moon);
                    float y = 0;

                    float alpha = 1;

                    if (this.selectionCount >= 2)
                    {
                        alpha = this.selectedBody instanceof IChildBody ? 1.0F : Math.min(Math.max((this.ticksSinceSelection - 30) / 15.0F, 0.0F), 1.0F);

                        if (this.lastSelectedBody instanceof Moon)
                        {
                            if (GalaxyRegistry.getMoonsForPlanet(((Moon) this.lastSelectedBody).getParentPlanet()).contains(moon))
                            {
                                alpha = 1.0F;
                            }
                        }
                    }

                    if (alpha != 0)
                    {
                        switch (count % 2)
                        {
                        case 0:
                            GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
                            break;
                        case 1:
                            GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
                            break;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(moon);
                        MinecraftForge.EVENT_BUS.post(preEvent);

                        if (!preEvent.isCanceled())
                        {
                            GL11.glBegin(GL11.GL_LINE_LOOP);

                            float temp;
                            for (int i = 0; i < 90; i++)
                            {
                                GL11.glVertex2f(x, y);

                                temp = x;
                                x = cos * x - sin * y;
                                y = sin * temp + cos * y;
                            }

                            GL11.glEnd();

                            count++;
                        }

                        CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(moon);
                        MinecraftForge.EVENT_BUS.post(postEvent);
                    }
                }
            }

            for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
            {
                if (this.possibleBodies != null && this.possibleBodies.contains(satellite))
                {
                    if ((satellite.getParentPlanet() == this.selectedBody && this.selectionCount != 1) && this.ticksSinceSelection > 24 || satellite == this.selectedBody || this.lastSelectedBody instanceof IChildBody)
                    {
                        float x = this.getScale(satellite);
                        float y = 0;

                        float alpha = 1;

                        if (this.selectionCount >= 2)
                        {
                            alpha = this.selectedBody instanceof IChildBody ? 1.0F : Math.min(Math.max((this.ticksSinceSelection - 30) / 15.0F, 0.0F), 1.0F);

                            if (this.lastSelectedBody instanceof Satellite)
                            {
                                if (GalaxyRegistry.getSatellitesForCelestialBody(((Satellite) this.lastSelectedBody).getParentPlanet()).contains(satellite))
                                {
                                    alpha = 1.0F;
                                }
                            }
                        }

                        if (alpha != 0)
                        {
                            switch (count % 2)
                            {
                            case 0:
                                GL11.glColor4f(0.0F, 0.6F, 1.0F, alpha);
                                break;
                            case 1:
                                GL11.glColor4f(0.4F, 0.9F, 1.0F, alpha);
                                break;
                            }

                            CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(satellite);
                            MinecraftForge.EVENT_BUS.post(preEvent);

                            if (!preEvent.isCanceled())
                            {
                                GL11.glBegin(GL11.GL_LINE_LOOP);

                                float temp;
                                for (int i = 0; i < 90; i++)
                                {
                                    GL11.glVertex2f(x, y);

                                    temp = x;
                                    x = cos * x - sin * y;
                                    y = sin * temp + cos * y;
                                }

                                GL11.glEnd();

                                count++;
                            }

                            CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(satellite);
                            MinecraftForge.EVENT_BUS.post(postEvent);
                        }
                    }
                }
            }
        }

        GL11.glLineWidth(1);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
        default:
            break;
        }
    }
}
