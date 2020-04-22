package micdoodle8.mods.galacticraft.core.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.api.world.SpaceStationType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.*;

public class GuiCelestialSelection extends GuiScreen
{
    protected enum EnumView
    {
        PREVIEW,
        PROFILE
    }

    protected enum EnumSelection
    {
        UNSELECTED,
        SELECTED,
        ZOOMED
    }

    protected static final int MAX_SPACE_STATION_NAME_LENGTH = 32;

    protected float zoom = 0.0F;
    protected float planetZoom = 0.0F;
    protected boolean doneZooming = false;
    protected float preSelectZoom = 0.0F;
    protected Vector2f preSelectPosition = new Vector2f();
    protected static ResourceLocation guiMain0 = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialselection.png");
    protected static ResourceLocation guiMain1 = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialselection1.png");
    protected float ticksSinceSelectionF = 0;
    protected float ticksSinceUnselectionF = -1;
    protected float ticksSinceMenuOpenF = 0;
    protected float ticksTotalF = 0;
    @Deprecated
    protected int ticksSinceSelection = 0;
    @Deprecated
    protected int ticksSinceUnselection = -1;
    @Deprecated
    protected int ticksSinceMenuOpen = 0;
    @Deprecated
    protected int ticksTotal = 0;
    protected int animateGrandchildren = 0;
    protected Vector2f position = new Vector2f(0, 0);
    protected Map<CelestialBody, Vector3f> planetPosMap = Maps.newHashMap();
    @Deprecated
    protected Map<CelestialBody, Integer> celestialBodyTicks = Maps.newHashMap();
    protected CelestialBody selectedBody;
    protected CelestialBody lastSelectedBody;
    protected static int BORDER_SIZE = 0;
    protected static int BORDER_EDGE_SIZE = 0;
    protected int canCreateOffset = 24;
    protected EnumView viewState = EnumView.PREVIEW;
    protected EnumSelection selectionState = EnumSelection.UNSELECTED;
    protected int zoomTooltipPos = 0;
    protected Object selectedParent = GalacticraftCore.solarSystemSol;
    protected final boolean mapMode;
    public List<CelestialBody> possibleBodies;

    // Each home planet has a map of owner's names linked with their station data:
    public Map<Integer, Map<String, StationDataGUI>> spaceStationMap = Maps.newHashMap();

    protected String selectedStationOwner = "";
    protected int spaceStationListOffset = 0;
    protected boolean renamingSpaceStation;
    protected String renamingString = "";
    protected Vector2f translation = new Vector2f(0.0F, 0.0F);
    protected boolean mouseDragging = false;
    protected int lastMovePosX = -1;
    protected int lastMovePosY = -1;
    protected boolean errorLogged = false;
    public boolean canCreateStations = false;
    protected List<CelestialBody> bodiesToRender = Lists.newArrayList();

    // String colours
    protected static final int WHITE = ColorUtil.to32BitColor(255, 255, 255, 255);
    protected static final int GREY5 = ColorUtil.to32BitColor(255, 150, 150, 150);
    protected static final int GREY4 = ColorUtil.to32BitColor(255, 140, 140, 140);
    protected static final int GREY3 = ColorUtil.to32BitColor(255, 120, 120, 120);
    protected static final int GREY2 = ColorUtil.to32BitColor(255, 100, 100, 100);
    protected static final int GREY1 = ColorUtil.to32BitColor(255, 80, 80, 80);
    protected static final int GREY0 = ColorUtil.to32BitColor(255, 40, 40, 40);
    protected static final int GREEN = ColorUtil.to32BitColor(255, 0, 255, 0);
    protected static final int RED = ColorUtil.to32BitColor(255, 255, 0, 0);
    protected static final int RED3 = ColorUtil.to32BitColor(255, 255, 100, 100);
    protected static final int CYAN = ColorUtil.to32BitColor(255, 150, 200, 255);

    public GuiCelestialSelection(boolean mapMode, List<CelestialBody> possibleBodies, boolean canCreateStations)
    {
        this.mapMode = mapMode;
        this.possibleBodies = possibleBodies;
        this.canCreateStations = canCreateStations;
    }

    @Override
    public void initGui()
    {
        GuiCelestialSelection.BORDER_SIZE = this.width / 65;
        GuiCelestialSelection.BORDER_EDGE_SIZE = GuiCelestialSelection.BORDER_SIZE / 4;

        for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
        {
            bodiesToRender.add(solarSystem.getMainStar());
        }
        bodiesToRender.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        bodiesToRender.addAll(GalaxyRegistry.getRegisteredMoons().values());
        bodiesToRender.addAll(GalaxyRegistry.getRegisteredSatellites().values());
    }

    protected String getGrandparentName()
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

    protected int getSatelliteParentID(Satellite satellite)
    {
        return satellite.getParentPlanet().getDimensionID();
    }

    protected String getParentName()
    {
        if (this.selectedBody instanceof Planet)
        {
            SolarSystem parentSolarSystem = ((Planet) this.selectedBody).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedName();
            }
        }
        else if (this.selectedBody instanceof IChildBody)
        {
            Planet parentPlanet = ((IChildBody) this.selectedBody).getParentPlanet();

            if (parentPlanet != null)
            {
                return parentPlanet.getLocalizedName();
            }
        }
        else if (this.selectedBody instanceof Star)
        {
            SolarSystem parentSolarSystem = ((Star) this.selectedBody).getParentSolarSystem();

            if (parentSolarSystem != null)
            {
                return parentSolarSystem.getLocalizedName();
            }
        }
        else if (this.selectedParent != null)
        {
            if (this.selectedParent instanceof SolarSystem)
            {
                return ((SolarSystem) this.selectedParent).getLocalizedName();
            }
        }

        return "Null";
    }

    protected float getScale(CelestialBody celestialBody)
    {
        return 3.0F * celestialBody.getRelativeDistanceFromCenter().unScaledDistance * (celestialBody instanceof Planet ? 25.0F : 1.0F / 5.0F);
    }

    protected List<CelestialBody> getSiblings(CelestialBody celestialBody)
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

            for (Satellite sat : GalaxyRegistry.getRegisteredSatellites().values())
            {
                Planet planet1 = sat.getParentPlanet();

                if (planet1 != null && planet1.equals(planet))
                {
                    bodyList.add(sat);
                }
            }
        }

        Collections.sort(bodyList);

        return bodyList;
    }

    protected List<CelestialBody> getChildren(Object object)
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

    protected float lerp(float v0, float v1, float t)
    {
        return v0 + t * (v1 - v0);
    }

    protected Vector2f lerpVec2(Vector2f v0, Vector2f v1, float t)
    {
        return new Vector2f(v0.x + t * (v1.x - v0.x), v0.y + t * (v1.y - v0.y));
    }

    protected float getZoomAdvanced()
    {
        if (this.ticksTotalF < 30)
        {
            float scale = Math.max(0.0F, Math.min(this.ticksTotalF / 30.0F, 1.0F));
            return this.lerp(-0.75F, 0.0F, (float) Math.pow(scale, 0.5F));
        }

        if (this.selectedBody == null || this.selectionState != EnumSelection.ZOOMED)
        {
            if (!this.doneZooming)
            {
                float unselectScale = this.lerp(this.zoom, this.preSelectZoom, Math.max(0.0F, Math.min(this.ticksSinceUnselectionF / 100.0F, 1.0F)));

                if (unselectScale <= this.preSelectZoom + 0.05F)
                {
                    this.zoom = this.preSelectZoom;
//                    this.preSelectZoom = 0.0F;
                    this.ticksSinceUnselectionF = -1;
                    this.ticksSinceUnselection = -1;
                    this.doneZooming = true;
                }

                return unselectScale;
            }

            return this.zoom;
        }

        if (!this.doneZooming)
        {
            float f = this.lerp(this.zoom, 12, Math.max(0.0F, Math.min((this.ticksSinceSelectionF - 20) / 40.0F, 1.0F)));

            if (f >= 11.95F)
            {
                this.doneZooming = true;
            }

            return f;
        }

        return 12 + this.planetZoom;
    }

    protected Vector2f getTranslationAdvanced(float partialTicks)
    {
        if (this.selectedBody == null)
        {
            if (this.ticksSinceUnselectionF > 0)
            {
                float f0 = Math.max(0.0F, Math.min(this.ticksSinceUnselectionF / 100.0F, 1.0F));
                if (f0 >= 0.999999F)
                {
                    this.ticksSinceUnselectionF = 0;
                    this.ticksSinceUnselection = 0;
                }
                return this.lerpVec2(this.position, this.preSelectPosition, f0);
            }

            return new Vector2f(this.position.x + translation.x, this.position.y + translation.y);
        }

        if (!this.isZoomed())
        {
            if (this.selectedBody instanceof IChildBody)
            {
                Vector3f posVec = this.getCelestialBodyPosition(((IChildBody) this.selectedBody).getParentPlanet());
                return new Vector2f(posVec.x, posVec.y);
            }

            return new Vector2f(this.position.x + translation.x, this.position.y + translation.y);
        }

//        if (this.selectedBody instanceof Planet && this.lastSelectedBody instanceof IChildBody && ((IChildBody) this.lastSelectedBody).getParentPlanet() == this.selectedBody)
//        {
//            Vector3f posVec = this.getCelestialBodyPosition(this.selectedBody);
//            return new Vector2f(posVec.x, posVec.y);
//        }

        Vector2f pos = this.position;

        if (this.lastSelectedBody != null)
        {
            Vector3f pos3 = this.getCelestialBodyPosition(this.lastSelectedBody);
            pos.x = pos3.x;
            pos.y = pos3.y;
        }

        Vector3f posVec = this.getCelestialBodyPosition(this.selectedBody);
        return this.lerpVec2(pos, new Vector2f(posVec.x, posVec.y), Math.max(0.0F, Math.min((this.ticksSinceSelectionF - 18) / 7.5F, 1.0F)));
    }

    @Override
    protected void keyTyped(char keyChar, int keyID) throws IOException
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
        return string.length() > 0 && ChatAllowedCharacters.isAllowedCharacter(string.charAt(string.length() - 1));

    }

    protected boolean canCreateSpaceStation(CelestialBody atBody)
    {
        if (this.mapMode || ConfigManagerCore.disableSpaceStationCreation || !this.canCreateStations)
        {
            return false;
        }

        if (!atBody.getReachable() || (this.possibleBodies != null && !this.possibleBodies.contains(atBody)))
        {
            // If parent body is unreachable, the satellite is also unreachable
            return false;
        }

        boolean foundRecipe = false;
        for (SpaceStationType type : GalacticraftRegistry.getSpaceStationData())
        {
            if (type.getWorldToOrbitID() == atBody.getDimensionID())
            {
                foundRecipe = true;
            }
        }

        if (!foundRecipe)
        {
            return false;
        }

        if (!ClientProxyCore.clientSpaceStationID.containsKey(atBody.getDimensionID()))
        {
            return true;
        }

        int resultID = ClientProxyCore.clientSpaceStationID.get(atBody.getDimensionID());

        return !(resultID != 0 && resultID != -1);
    }

    protected void unselectCelestialBody()
    {
        this.selectionState = EnumSelection.UNSELECTED;
        this.ticksSinceUnselectionF = 0;
        this.ticksSinceUnselection = 0;
        this.lastSelectedBody = this.selectedBody;
        this.selectedBody = null;
        this.doneZooming = false;
        this.selectedStationOwner = "";
        this.animateGrandchildren = 0;
    }

    @Override
    public void updateScreen()
    {
        this.ticksSinceMenuOpen++;
        this.ticksTotal++;

        if (this.ticksSinceMenuOpen < 20)
        {
            Mouse.setGrabbed(false);
        }

        if (this.selectedBody != null)
        {
            this.ticksSinceSelection++;
        }

        if (this.selectedBody == null && this.ticksSinceUnselection >= 0)
        {
            this.ticksSinceUnselection++;
        }

        if (!this.renamingSpaceStation && (this.selectedBody == null || !this.isZoomed()))
        {
            if (GameSettings.isKeyDown(KeyHandlerClient.leftKey))
            {
                translation.x += -2.0F;
                translation.y += -2.0F;
            }

            if (GameSettings.isKeyDown(KeyHandlerClient.rightKey))
            {
                translation.x += 2.0F;
                translation.y += 2.0F;
            }

            if (GameSettings.isKeyDown(KeyHandlerClient.upKey))
            {
                translation.x += 2.0F;
                translation.y += -2.0F;
            }

            if (GameSettings.isKeyDown(KeyHandlerClient.downKey))
            {
                translation.x += -2.0F;
                translation.y += 2.0F;
            }
        }
    }

    protected void teleportToSelectedBody()
    {
        if (this.selectedBody != null)
        {
            if (this.selectedBody.getReachable() && this.possibleBodies != null && this.possibleBodies.contains(this.selectedBody))
            {
                try
                {
                    String dimension;
                    int dimensionID;

                    if (this.selectedBody instanceof Satellite)
                    {
                        if (this.spaceStationMap == null)
                        {
                            GCLog.severe("Please report as a BUG: spaceStationIDs was null.");
                            return;
                        }
                        Satellite selectedSatellite = (Satellite) this.selectedBody;
                        Integer mapping = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(this.selectedStationOwner).getStationDimensionID();
                        //No need to check lowercase as selectedStationOwner is taken from keys.
                        if (mapping == null)
                        {
                            GCLog.severe("Problem matching player name in space station check: " + this.selectedStationOwner);
                            return;
                        }
                        dimensionID = mapping;
                        WorldProvider spacestation = WorldUtil.getProviderForDimensionClient(dimensionID);
                        if (spacestation != null)
                        {
                            dimension = "Space Station " + mapping;
                        }
                        else
                        {
                            GCLog.severe("Failed to find a spacestation with dimension " + dimensionID);
                            return;
                        }
                    }
                    else
                    {
                        dimensionID = this.selectedBody.getDimensionID();
                        dimension = WorldUtil.getDimensionName(WorldUtil.getProviderForDimensionClient(dimensionID));
                    }

                    if (dimension.contains("$"))
                    {
                        this.mc.gameSettings.thirdPersonView = 0;
                    }
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_TELEPORT_ENTITY, GCCoreUtil.getDimensionID(mc.world), new Object[] { dimensionID }));
                    mc.displayGuiScreen(new GuiTeleporting(dimensionID));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleInput() throws IOException
    {
        this.translation.x = 0.0F;
        this.translation.y = 0.0F;
        super.handleInput();
    }

    @Override
    protected void mouseClickMove(int x, int y, int lastButtonClicked, long timeSinceMouseClick)
    {
        super.mouseClickMove(x, y, lastButtonClicked, timeSinceMouseClick);

        if (mouseDragging && lastMovePosX != -1 && lastButtonClicked == 0)
        {
            int deltaX = x - lastMovePosX;
            int deltaY = y - lastMovePosY;
            float scollMultiplier = -Math.abs(this.zoom);

            if (this.zoom == -1.0F)
            {
                scollMultiplier = -1.5F;
            }

            if (this.zoom >= -0.25F && this.zoom <= 0.15F)
            {
                scollMultiplier = -0.2F;
            }

            if (this.zoom >= 0.15F)
            {
                scollMultiplier = -0.15F;
            }

            translation.x += (deltaX - deltaY) * scollMultiplier * (ConfigManagerCore.invertMapMouseScroll ? -1.0F : 1.0F) * ConfigManagerCore.mapMouseScrollSensitivity * 0.2F;
            translation.y += (deltaY + deltaX) * scollMultiplier * (ConfigManagerCore.invertMapMouseScroll ? -1.0F : 1.0F) * ConfigManagerCore.mapMouseScrollSensitivity * 0.2F;
        }

        lastMovePosX = x;
        lastMovePosY = y;
    }

    @Override
    protected void mouseReleased(int x, int y, int button)
    {
        super.mouseReleased(x, y, button);

        mouseDragging = false;
        lastMovePosX = -1;
        lastMovePosY = -1;
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException
    {
        super.mouseClicked(x, y, button);
        boolean clickHandled = false;
        
        final int LHS = GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE;
        final int RHS = width - LHS;
        final int TOP = LHS;

        if (this.selectedBody != null && x > LHS && x < LHS + 88 && y > TOP && y < TOP + 13)
        {
            this.unselectCelestialBody();
            return;
        }

        if (!this.mapMode)
        {
            if (x >= RHS - 95 && x < RHS && y > TOP + 181 + canCreateOffset && y < TOP + 182 + 12 + canCreateOffset)
            {
                if (this.selectedBody != null)
                {
                    SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.selectedBody.getDimensionID());
                    if (recipe != null && this.canCreateSpaceStation(this.selectedBody))
                    {
                        if (recipe.matches(this.mc.player, false) || this.mc.player.capabilities.isCreativeMode)
                        {
                            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_BIND_SPACE_STATION_ID, GCCoreUtil.getDimensionID(this.mc.world), new Object[] { this.selectedBody.getDimensionID() }));
                            //Zoom in on Overworld to show the new SpaceStation if not already zoomed
                            if (!this.isZoomed())
                            {
                                this.selectionState = EnumSelection.ZOOMED;
                                this.preSelectZoom = this.zoom;
                                this.preSelectPosition = this.position;
                                this.ticksSinceSelectionF = 0;
                                this.ticksSinceSelection = 0;
                                this.doneZooming = false;
                            }
                            return;
                        }

                        clickHandled = true;
                    }
                }
            }
        }

        if (this.mapMode)
        {
            if (x > RHS - 88 && x < RHS && y > TOP && y < TOP + 13)
            {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                clickHandled = true;
            }
        }

        if (this.selectedBody != null && !this.mapMode)
        {
            if (x > RHS - 88 && x < RHS && y > TOP && y < TOP + 13)
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
                        String strName = PlayerUtil.getName(this.mc.player);
//                        Integer spacestationID = this.spaceStationIDs.get(strName);
//                        if (spacestationID == null) spacestationID = this.spaceStationIDs.get(strName.toLowerCase());
                        Satellite selectedSatellite = (Satellite) this.selectedBody;
                        Integer spacestationID = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(strName).getStationDimensionID();
                        if (spacestationID == null)
                        {
                            spacestationID = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(strName.toLowerCase()).getStationDimensionID();
                        }
                        if (spacestationID != null)
                        {
                            this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(strName).setStationName(this.renamingString);
//	                    	this.spaceStationNames.put(strName, this.renamingString);
                            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_RENAME_SPACE_STATION, GCCoreUtil.getDimensionID(this.mc.world), new Object[] { this.renamingString, spacestationID }));
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
                this.drawTexturedModalRect(width / 2 - 47, TOP, 94, 11, 0, 414, 188, 22, false, false);

                if (x >= width / 2 - 47 && x <= width / 2 - 47 + 94 && y >= TOP && y <= TOP + 11)
                {
                    if (this.selectedStationOwner.length() != 0 && this.selectedStationOwner.equalsIgnoreCase(PlayerUtil.getName(this.mc.player)))
                    {
                        this.renamingSpaceStation = true;
                        this.renamingString = null;
                        clickHandled = true;
                    }
                }

                Satellite selectedSatellite = (Satellite) this.selectedBody;
                int stationListSize = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).size();
                int max = Math.min((this.height / 2) / 14, stationListSize);

                int xPos;
                int yPos;

                // Up button
                xPos = RHS - 85;
                yPos = TOP + 45;

                if (x >= xPos && x <= xPos + 61 && y >= yPos && y <= yPos + 4)
                {
                    if (this.spaceStationListOffset > 0)
                    {
                        this.spaceStationListOffset--;
                    }
                    clickHandled = true;
                }

                // Down button
                xPos = RHS - 85;
                yPos = TOP + 49 + max * 14;

                if (x >= xPos && x <= xPos + 61 && y >= yPos && y <= yPos + 4)
                {
                    if (max + spaceStationListOffset < stationListSize)
                    {
                        this.spaceStationListOffset++;
                    }
                    clickHandled = true;
                }

                Iterator<Map.Entry<String, StationDataGUI>> it = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).entrySet().iterator();
                int i = 0;
                int j = 0;
                while (it.hasNext() && i < max)
                {
                    Map.Entry<String, StationDataGUI> e = it.next();
                    if (j >= this.spaceStationListOffset)
                    {
                        int xOffset = 0;

                        if (e.getKey().equalsIgnoreCase(this.selectedStationOwner))
                        {
                            xOffset -= 5;
                        }

                        xPos = RHS - 95 + xOffset;
                        yPos = TOP + 50 + i * 14;

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

        int xPos = LHS + 2;
        int yPos = TOP + 10;

        boolean planetZoomedMoon = this.isZoomed() && this.selectedParent instanceof Planet;

        // Top yellow button e.g. Sol
        if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12 && this.selectedParent instanceof CelestialBody)
        {
            if (this.selectedBody == null)
            {
                this.preSelectZoom = this.zoom;
                this.preSelectPosition = this.position;
            }

            EnumSelection selectionCountOld = this.selectionState;

            if (this.isSelected())
            {
                this.unselectCelestialBody();
            }

            if (selectionCountOld == EnumSelection.ZOOMED)
            {
                this.selectionState = EnumSelection.SELECTED;
            }

            this.selectedBody = (CelestialBody) this.selectedParent;
            this.ticksSinceSelectionF = 0;
            this.ticksSinceSelection = 0;
            this.selectionState = EnumSelection.values()[this.selectionState.ordinal() + 1];
            if (this.isZoomed() && !planetZoomedMoon)
            {
                this.ticksSinceMenuOpenF = 0;
                this.ticksSinceMenuOpen = 0;
            }
            clickHandled = true;
        }

        yPos += 22;

        // First blue button - normally the Selected Body (but it's the parent planet if this is a moon)
        if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12)
        {
            if (planetZoomedMoon)
            {
                if (this.selectedBody == null)
                {
                    this.preSelectZoom = this.zoom;
                    this.preSelectPosition = this.position;
                }

                EnumSelection selectionCountOld = this.selectionState;
                if (this.isSelected())
                {
                    this.unselectCelestialBody();
                }
                if (selectionCountOld == EnumSelection.ZOOMED)
                {
                    this.selectionState = EnumSelection.SELECTED;
                }

                this.selectedBody = (CelestialBody) this.selectedParent;
                this.ticksSinceSelectionF = 0;
                this.ticksSinceSelection = 0;
                this.selectionState = EnumSelection.values()[this.selectionState.ordinal() + 1];
            }
            clickHandled = true;
        }

        if (!clickHandled)
        {
            List<CelestialBody> children = this.getChildren(this.isZoomed() && !(this.selectedParent instanceof Planet) ? this.selectedBody : this.selectedParent);
    
            yPos = TOP + 50;
            for (CelestialBody child : children)
            {
                clickHandled = this.testClicked(child, child.equals(this.selectedBody) ? 5 : 0, yPos, x, y, false);
                yPos += 14;
    
                if (!clickHandled && !this.isZoomed() && child.equals(this.selectedBody))
                {
                    List<CelestialBody> grandchildren = this.getChildren(child);
                    int gOffset = 0;
                    for (CelestialBody grandchild : grandchildren)
                    {
                        if (gOffset + 14 > this.animateGrandchildren)
                        {
                            break;
                        }
                        clickHandled = this.testClicked(grandchild, 10, yPos, x, y, true);
                        yPos += 14;
                        gOffset += 14;
                        if (clickHandled)
                            break;
                    }
                    yPos += this.animateGrandchildren - gOffset; 
                }
    
                if (clickHandled)
                    break;
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
                    if (this.selectedBody != bodyClicked || !this.isZoomed())
                    {
                        if (this.isSelected() && this.selectedBody != bodyClicked)
                        {
                            /*if (!(this.selectedBody instanceof IChildBody) || ((IChildBody) this.selectedBody).getParentPlanet() != bodyClicked)
                            {
//                                this.unselectCelestialBody();
                            }
                            else */if (this.isZoomed())
                            {
                                this.selectionState = EnumSelection.SELECTED;
                            }
                        }

                        if (bodyClicked != this.selectedBody)
                        {
                            this.lastSelectedBody = this.selectedBody;
                            this.animateGrandchildren = 0;
                            if (!(this.selectedBody instanceof IChildBody) || ((IChildBody) this.selectedBody).getParentPlanet() != bodyClicked)
                            {
                                // Only unzoom if the new selected body is not the child of the previously selected body
                                this.selectionState = EnumSelection.UNSELECTED;
                            }
                        }
                        else
                        {
                            this.doneZooming = false;
                            this.planetZoom = 0.0F;
                        }

                        this.selectedBody = bodyClicked;
                        this.ticksSinceSelectionF = 0;
                        this.ticksSinceSelection = 0;
                        this.selectionState = EnumSelection.values()[this.selectionState.ordinal() + 1];

                        if (bodyClicked instanceof IChildBody)
                        {
                            this.selectionState = EnumSelection.ZOOMED;
                        }

                        if (this.isZoomed())
                        {
                            this.ticksSinceMenuOpenF = 0;
                            this.ticksSinceMenuOpen = 0;
                        }

                        //Auto select if it's a spacestation and there is only a single entry
                        if (this.selectedBody instanceof Satellite && this.spaceStationMap.get(this.getSatelliteParentID((Satellite) this.selectedBody)).size() == 1)
                        {
                            Iterator<Map.Entry<String, StationDataGUI>> it = this.spaceStationMap.get(this.getSatelliteParentID((Satellite) this.selectedBody)).entrySet().iterator();
                            this.selectedStationOwner = it.next().getKey();
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
                this.planetZoom = 0.0F;
            }

            mouseDragging = true;
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
        }
    }

    protected boolean testClicked(CelestialBody body, int xOffset, int yPos, int x, int y, boolean grandchild)
    {
        int xPos = GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE + 2 + xOffset;
        if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12)
        {
            if (this.selectedBody != body || !this.isZoomed())
            {
                if (this.selectedBody == null)
                {
                    this.preSelectZoom = this.zoom;
                    this.preSelectPosition = this.position;
                }

                EnumSelection selectionCountOld = this.selectionState;

                if (selectionCountOld == EnumSelection.ZOOMED)
                {
                    this.selectionState = EnumSelection.SELECTED;
                }

                this.doneZooming = false;
                this.planetZoom = 0.0F;

                if (body != this.selectedBody)
                {
                    // Selecting a different body
                    this.lastSelectedBody = this.selectedBody;
                    this.selectionState = EnumSelection.SELECTED;
                }
                else
                {
                    // Selecting the same body e.g. double-clicking
                    this.selectionState = EnumSelection.values()[this.selectionState.ordinal() + 1];
                }

                this.selectedBody = body;
                this.ticksSinceSelectionF = 0;
                this.ticksSinceSelection = 0;
                if (grandchild) this.selectionState = EnumSelection.ZOOMED;
                if (this.isZoomed())
                {
                    this.ticksSinceMenuOpenF = 0;
                    this.ticksSinceMenuOpen = 0;
                }
                this.animateGrandchildren = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mousePosX, int mousePosY, float partialTicks)
    {
        this.ticksSinceMenuOpenF += partialTicks;
        this.ticksTotalF += partialTicks;

        if (this.selectedBody != null)
        {
            this.ticksSinceSelectionF += partialTicks;
        }

        if (this.selectedBody == null && this.ticksSinceUnselectionF >= 0)
        {
            this.ticksSinceUnselectionF += partialTicks;
        }

        if (Mouse.hasWheel())
        {
            float wheel = Mouse.getDWheel() / (this.selectedBody == null ? 500.0F : 250.0F);

            if (wheel != 0)
            {
                if (this.selectedBody == null || (this.viewState == EnumView.PREVIEW && !this.isZoomed()))
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
        Matrix4f.translate(new Vector3f(0.0F, 0.0F, -9000.0F), camMatrix, camMatrix); // See EntityRenderer.java:setupOverlayRendering
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.m00 = 2.0F / width;
        viewMatrix.m11 = 2.0F / -height;
        viewMatrix.m22 = -2.0F / 9000.0F;
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

        try {
            this.drawButtons(mousePosX, mousePosY);
        } catch (Exception e)
        {
            if (!this.errorLogged)
            {
                this.errorLogged = true;
                GCLog.severe("Problem identifying planet or dimension in an add on for Galacticraft!");
                GCLog.severe("(The problem is likely caused by a dimension ID conflict.  Check configs for dimension clashes.  You can also try disabling Mars space station in configs.)");
                e.printStackTrace();
            }
        }

        this.drawBorder();
        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    protected void drawSelectionCursor(FloatBuffer fb, Matrix4f worldMatrix)
    {
        GL11.glPushMatrix();
        switch (this.selectionState)
        {
        case SELECTED:
            if (this.selectedBody != null)
            {
//                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
//                Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
//                Matrix4f worldMatrix1 = new Matrix4f();
//                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
//                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
//                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
//                fb.rewind();
//                worldMatrix1.store(fb);
//                fb.flip();
//                GL11.glMultMatrix(fb);
                setupMatrix(this.selectedBody, worldMatrix, fb);
                fb.clear();
                GL11.glScalef(1 / 15.0F, 1 / 15.0F, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                float colMod = this.getZoomAdvanced() < 4.9F ? (float) (Math.sin(this.ticksSinceSelectionF / 2.0F) * 0.5F + 0.5F) : 1.0F;
                GL11.glColor4f(1.0F, 1.0F, 0.0F, 1 * colMod);
                int width = (int)Math.floor((getWidthForCelestialBody(this.selectedBody) / 2.0) * (this.selectedBody instanceof IChildBody ? 9.0 : 30.0));

                this.drawTexturedModalRect(-width, -width, width * 2, width * 2, 266, 29, 100, 100, false, false);
            }
            break;
        case ZOOMED:
            if (this.selectedBody != null)
            {
//                Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
//                Matrix4f.translate(this.getCelestialBodyPosition(this.selectedBody), worldMatrix0, worldMatrix0);
//                Matrix4f worldMatrix1 = new Matrix4f();
//                Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
//                Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
//                worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
//                fb.rewind();
//                worldMatrix1.store(fb);
//                fb.flip();
//                GL11.glMultMatrix(fb);
                setupMatrix(this.selectedBody, worldMatrix, fb);
                fb.clear();
                float div = (this.zoom + 1.0F - this.planetZoom);
                float scale = Math.max(0.3F, 1.5F / (this.ticksSinceSelectionF / 5.0F)) * 2.0F / div;
                GL11.glScalef(scale, scale, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                float colMod = this.getZoomAdvanced() < 4.9F ? (float) (Math.sin(this.ticksSinceSelectionF / 1.0F) * 0.5F + 0.5F) : 1.0F;
                GL11.glColor4f(0.4F, 0.8F, 1.0F, 1 * colMod);
                int width = getWidthForCelestialBody(this.selectedBody) * 13;
                this.drawTexturedModalRect(-width, -width, width * 2, width * 2, 266, 29, 100, 100, false, false);
            }
            break;
        default:
            break;
        }
        GL11.glPopMatrix();
    }

    protected Vector3f getCelestialBodyPosition(CelestialBody cBody)
    {
        if (cBody == null) return new Vector3f(0.0F, 0.0F, 0.0F);
        if (cBody instanceof Star)
        {
            if (cBody.getUnlocalizedName().equalsIgnoreCase("star.sol"))
            //Return zero vector for Sol, different location for other solar systems
            {
                return new Vector3f();
            }
            return ((Star) cBody).getParentSolarSystem().getMapPosition().toVector3f();
        }

        float timeScale = cBody instanceof Planet ? 200.0F : 2.0F;
        float distanceFromCenter = this.getScale(cBody);
        Vector3f cBodyPos = new Vector3f((float) Math.sin(ticksTotalF / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, (float) Math.cos(ticksTotalF / (timeScale * cBody.getRelativeOrbitTime()) + cBody.getPhaseShift()) * distanceFromCenter, 0);

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

        return cBodyPos;
    }

    public int getWidthForCelestialBody(CelestialBody celestialBody)
    {
        boolean zoomed = celestialBody == this.selectedBody && this.selectionState == EnumSelection.SELECTED;
        return celestialBody instanceof Star ? (zoomed ? 12 : 8) :
                celestialBody instanceof Planet ? (zoomed ? 6 : 4) :
                        celestialBody instanceof IChildBody ? (zoomed ? 6 : 4) : 2;
    }

    public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(Matrix4f worldMatrix)
    {
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
        HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();

        for (CelestialBody body : bodiesToRender)
        {
            boolean hasParent = body instanceof IChildBody;

            float alpha = getAlpha(body);

            if (alpha > 0.0F)
            {
                GlStateManager.pushMatrix();
                Matrix4f worldMatrixLocal = setupMatrix(body, worldMatrix, fb, hasParent ? 0.25F : 1.0F);
                CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(body, body.getBodyIcon(), 16);
                MinecraftForge.EVENT_BUS.post(preEvent);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
                if (preEvent.celestialBodyTexture != null)
                {
                    this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
                }

                if (!preEvent.isCanceled())
                {
                    int size = getWidthForCelestialBody(body);
                    this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize, preEvent.textureSize);
                    matrixMap.put(body, worldMatrixLocal);
                }

                CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(body);
                MinecraftForge.EVENT_BUS.post(postEvent);
                GlStateManager.popMatrix();
            }
        }

        return matrixMap;
    }

    /**
     * Draws gray border around outside of gui
     */
    public void drawBorder()
    {
        Gui.drawRect(0, 0, GuiCelestialSelection.BORDER_SIZE, height, GREY2);
        Gui.drawRect(width - GuiCelestialSelection.BORDER_SIZE, 0, width, height, GREY2);
        Gui.drawRect(0, 0, width, GuiCelestialSelection.BORDER_SIZE, GREY2);
        Gui.drawRect(0, height - GuiCelestialSelection.BORDER_SIZE, width, height, GREY2);
        Gui.drawRect(GuiCelestialSelection.BORDER_SIZE, GuiCelestialSelection.BORDER_SIZE, GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE, height - GuiCelestialSelection.BORDER_SIZE, GREY0);
        Gui.drawRect(GuiCelestialSelection.BORDER_SIZE, GuiCelestialSelection.BORDER_SIZE, width - GuiCelestialSelection.BORDER_SIZE, GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE, GREY0);
        Gui.drawRect(width - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE, GuiCelestialSelection.BORDER_SIZE, width - GuiCelestialSelection.BORDER_SIZE, height - GuiCelestialSelection.BORDER_SIZE, GREY1);
        Gui.drawRect(GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE, width - GuiCelestialSelection.BORDER_SIZE, height - GuiCelestialSelection.BORDER_SIZE, GREY1);
    }

    public void drawButtons(int mousePosX, int mousePosY)
    {
        this.zLevel = 0.0F;
        boolean handledSliderPos = false;

        final int LHS = GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE;
        final int RHS = width - LHS;
        final int TOP = LHS;
        final int BOT = height - LHS;

        if (this.viewState == EnumView.PROFILE)
        {
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.drawTexturedModalRect(width / 2 - 43, TOP, 86, 15, 266, 0, 172, 29, false, false);
            String str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
            this.fontRenderer.drawString(str, width / 2 - this.fontRenderer.getStringWidth(str) / 2, TOP + this.fontRenderer.FONT_HEIGHT / 2, WHITE);

            if (this.selectedBody != null)
            {
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);

                if (mousePosX > LHS && mousePosX < LHS + 88 && mousePosY > TOP && mousePosY < TOP + 13)
                {
                    GL11.glColor3f(3.0F, 0.0F, 0.0F);
                }
                else
                {
                    GL11.glColor3f(0.9F, 0.2F, 0.2F);
                }

                this.drawTexturedModalRect(LHS, TOP, 88, 13, 0, 392, 148, 22, false, false);
                str = GCCoreUtil.translate("gui.message.back.name").toUpperCase();
                this.fontRenderer.drawString(str, LHS + 45 - this.fontRenderer.getStringWidth(str) / 2, TOP + this.fontRenderer.FONT_HEIGHT / 2 - 2, WHITE);

                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                if (mousePosX > RHS - 88 && mousePosX < RHS && mousePosY > TOP && mousePosY < TOP + 13)
                {
                    GL11.glColor3f(0.0F, 3.0F, 0.0F);
                }
                else
                {
                    GL11.glColor3f(0.2F, 0.9F, 0.2F);
                }

                this.drawTexturedModalRect(RHS - 88, TOP, 88, 13, 0, 392, 148, 22, true, false);

                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                this.drawTexturedModalRect(LHS, BOT - 13, 88, 13, 0, 392, 148, 22, false, true);
                this.drawTexturedModalRect(RHS - 88, BOT - 13, 88, 13, 0, 392, 148, 22, true, true);
                int menuTopLeft = TOP - 115 + height / 2 - 4;
                int posX = LHS + Math.min((int)this.ticksSinceSelectionF * 10, 133) - 134;
                int posX2 = (int) (LHS + Math.min(this.ticksSinceSelectionF * 1.25F, 15) - 15);
                int fontPosY = menuTopLeft + GuiCelestialSelection.BORDER_EDGE_SIZE + this.fontRenderer.FONT_HEIGHT / 2 - 2;
                this.drawTexturedModalRect(posX, menuTopLeft + 12, 133, 196, 0, 0, 266, 392, false, false);

//			str = this.selectedBody.getLocalizedName();
//			this.fontRenderer.drawString(str, posX + 20, fontPosY, GCCoreUtil.to32BitColor(255, 255, 255, 255));

                str = GCCoreUtil.translate("gui.message.daynightcycle.name") + ":";
                this.fontRenderer.drawString(str, posX + 5, fontPosY + 14, CYAN);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.0.name");
                this.fontRenderer.drawString(str, posX + 10, fontPosY + 25, WHITE);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".daynightcycle.1.name");
                if (!str.isEmpty())
                {
                    this.fontRenderer.drawString(str, posX + 10, fontPosY + 36, WHITE);
                }

                str = GCCoreUtil.translate("gui.message.surfacegravity.name") + ":";
                this.fontRenderer.drawString(str, posX + 5, fontPosY + 50, CYAN);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.0.name");
                this.fontRenderer.drawString(str, posX + 10, fontPosY + 61, WHITE);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacegravity.1.name");
                if (!str.isEmpty())
                {
                    this.fontRenderer.drawString(str, posX + 10, fontPosY + 72, WHITE);
                }

                str = GCCoreUtil.translate("gui.message.surfacecomposition.name") + ":";
                this.fontRenderer.drawString(str, posX + 5, fontPosY + 88, CYAN);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.0.name");
                this.fontRenderer.drawString(str, posX + 10, fontPosY + 99, WHITE);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".surfacecomposition.1.name");
                if (!str.isEmpty())
                {
                    this.fontRenderer.drawString(str, posX + 10, fontPosY + 110, WHITE);
                }

                str = GCCoreUtil.translate("gui.message.atmosphere.name") + ":";
                this.fontRenderer.drawString(str, posX + 5, fontPosY + 126, CYAN);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.0.name");
                this.fontRenderer.drawString(str, posX + 10, fontPosY + 137, WHITE);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".atmosphere.1.name");
                if (!str.isEmpty())
                {
                    this.fontRenderer.drawString(str, posX + 10, fontPosY + 148, WHITE);
                }

                str = GCCoreUtil.translate("gui.message.meansurfacetemp.name") + ":";
                this.fontRenderer.drawString(str, posX + 5, fontPosY + 165, CYAN);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.0.name");
                this.fontRenderer.drawString(str, posX + 10, fontPosY + 176, WHITE);
                str = GCCoreUtil.translate("gui.message." + this.selectedBody.getName() + ".meansurfacetemp.1.name");
                if (!str.isEmpty())
                {
                    this.fontRenderer.drawString(str, posX + 10, fontPosY + 187, WHITE);
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
            this.drawTexturedModalRect(LHS, TOP, 74, 11, 0, 392, 148, 22, false, false);
            str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.fontRenderer.drawString(str, LHS + 40 - fontRenderer.getStringWidth(str) / 2, TOP + 1, WHITE);

            int scale = (int) Math.min(95, this.ticksSinceMenuOpenF * 12.0F);
            boolean planetZoomedNotMoon = this.isZoomed() && !(this.selectedParent instanceof Planet); 

            // Parent frame:
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            this.drawTexturedModalRect(LHS - 95 + scale, TOP + 12, 95, 41, 0, 436, 95, 41, false, false);
            str = planetZoomedNotMoon ? this.selectedBody.getLocalizedName() : this.getParentName();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.fontRenderer.drawString(str, LHS + 9 - 95 + scale, TOP + 34, WHITE);
            GL11.glColor4f(1, 1, 0, 1);
            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);

            // Grandparent frame:
            this.drawTexturedModalRect(LHS + 2 - 95 + scale, TOP + 14, 93, 17, 95, 436, 93, 17, false, false);
            str = planetZoomedNotMoon ? this.getParentName() : this.getGrandparentName();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.fontRenderer.drawString(str, LHS + 7 - 95 + scale, TOP + 16, GREY3);
            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

            List<CelestialBody> children = this.getChildren(planetZoomedNotMoon ? this.selectedBody : this.selectedParent);
            drawChildren(children, 0, 0, true);

            if (this.mapMode)
            {
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                this.drawTexturedModalRect(RHS - 74, TOP, 74, 11, 0, 392, 148, 22, true, false);
                str = GCCoreUtil.translate("gui.message.exit.name").toUpperCase();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.fontRenderer.drawString(str, RHS - 40 - fontRenderer.getStringWidth(str) / 2, TOP + 1, WHITE);
            }

            if (this.selectedBody != null)
            {
                // Right-hand bar (basic selectionState info)
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

                if (this.selectedBody instanceof Satellite)
                {
                    Satellite selectedSatellite = (Satellite) this.selectedBody;
                    int stationListSize = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).size();

                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                    int max = Math.min((this.height / 2) / 14, stationListSize);
                    this.drawTexturedModalRect(RHS - 95, TOP, 95, 53, this.selectedStationOwner.length() == 0 ? 95 : 0, 186, 95, 53, false, false);
                    if (this.spaceStationListOffset <= 0)
                    {
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    }
                    this.drawTexturedModalRect(RHS - 85, TOP + 45, 61, 4, 0, 239, 61, 4, false, false);
                    if (max + spaceStationListOffset >= stationListSize)
                    {
                        GL11.glColor4f(0.65F, 0.65F, 0.65F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    }
                    this.drawTexturedModalRect(RHS - 85, TOP + 49 + max * 14, 61, 4, 0, 239, 61, 4, false, true);
                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);

                    if (this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(this.selectedStationOwner) == null)
                    {
                        str = GCCoreUtil.translate("gui.message.select_ss.name");
                        this.drawSplitString(str, RHS - 47, TOP + 20, 91, WHITE, false, false);
                    }
                    else
                    {
                        str = GCCoreUtil.translate("gui.message.ss_owner.name");
                        this.fontRenderer.drawString(str, RHS - 85, TOP + 18, WHITE);
                        str = this.selectedStationOwner;
                        this.fontRenderer.drawString(str, RHS - 47 - this.fontRenderer.getStringWidth(str) / 2, TOP + 30, WHITE);
                    }

                    Iterator<Map.Entry<String, StationDataGUI>> it = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).entrySet().iterator();
                    int i = 0;
                    int j = 0;
                    while (it.hasNext() && i < max)
                    {
                        Map.Entry<String, StationDataGUI> e = it.next();

                        if (j >= this.spaceStationListOffset)
                        {
                            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                            GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                            int xOffset = 0;

                            if (e.getKey().equalsIgnoreCase(this.selectedStationOwner))
                            {
                                xOffset -= 5;
                            }

                            this.drawTexturedModalRect(RHS - 95 + xOffset, TOP + 50 + i * 14, 93, 12, 95, 464, 93, 12, true, false);
                            str = "";
                            String str0 = e.getValue().getStationName();
                            int point = 0;
                            while (this.fontRenderer.getStringWidth(str) < 80 && point < str0.length())
                            {
                                str = str + str0.substring(point, point + 1);
                                point++;
                            }
                            if (this.fontRenderer.getStringWidth(str) >= 80)
                            {
                                str = str.substring(0, str.length() - 3);
                                str = str + "...";
                            }
                            this.fontRenderer.drawString(str, RHS - 88 + xOffset, TOP + 52 + i * 14, WHITE);
                            i++;
                        }
                        j++;
                    }
                }
                else
                {
                    this.drawTexturedModalRect(RHS - 96, TOP, 96, 139, 63, 0, 96, 139, false, false);
                }

                if (this.canCreateSpaceStation(this.selectedBody) && (!(this.selectedBody instanceof Satellite)))
                {
                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain1);
                    int canCreateLength = Math.max(0, this.drawSplitString(GCCoreUtil.translate("gui.message.can_create_space_station.name"), 0, 0, 91, 0, true, true) - 2);
                    canCreateOffset = canCreateLength * this.fontRenderer.FONT_HEIGHT;

                    this.drawTexturedModalRect(RHS - 95, TOP + 134, 93, 4, 159, 102, 93, 4, false, false);
                    for (int barY = 0; barY < canCreateLength; ++barY)
                    {
                        this.drawTexturedModalRect(RHS - 95, TOP + 138 + barY * this.fontRenderer.FONT_HEIGHT, 93, this.fontRenderer.FONT_HEIGHT, 159, 106, 93, this.fontRenderer.FONT_HEIGHT, false, false);
                    }
                    this.drawTexturedModalRect(RHS - 95, TOP + 138 + canCreateOffset, 93, 43, 159, 106, 93, 43, false, false);
                    this.drawTexturedModalRect(RHS - 79, TOP + 129, 61, 4, 0, 170, 61, 4, false, false);

                    SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.selectedBody.getDimensionID());
                    if (recipe != null)
                    {
                        GL11.glColor4f(0.0F, 1.0F, 0.1F, 1);
                        boolean validInputMaterials = true;

                        int i = 0;
                        for (Map.Entry<Object, Integer> e : recipe.getInput().entrySet())
                        {
                            Object next = e.getKey();
                            int xPos = (int) (RHS - 95 + i * 93 / (double) recipe.getInput().size() + 5);
                            int yPos = TOP + 154 + canCreateOffset;

                            if (next instanceof ItemStack)
                            {
                                int amount = getAmountInInventory((ItemStack) next);
                                RenderHelper.enableGUIStandardItemLighting();
                                ItemStack toRender = ((ItemStack) next).copy();
                                this.itemRender.renderItemAndEffectIntoGUI(toRender, xPos, yPos);
                                this.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, toRender, xPos, yPos, null);
                                RenderHelper.disableStandardItemLighting();
                                GL11.glEnable(GL11.GL_BLEND);

                                if (mousePosX >= xPos && mousePosX <= xPos + 16 && mousePosY >= yPos && mousePosY <= yPos + 16)
                                {
                                    GL11.glDepthMask(true);
                                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                                    GL11.glPushMatrix();
                                    GL11.glTranslatef(0, 0, 300);
                                    int k = this.fontRenderer.getStringWidth(((ItemStack) next).getDisplayName());
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

                                    this.fontRenderer.drawString(((ItemStack) next).getDisplayName(), j2, k2, WHITE);

                                    GL11.glPopMatrix();
                                }

                                str = "" + e.getValue();
                                boolean valid = amount >= e.getValue();
                                if (!valid && validInputMaterials)
                                {
                                    validInputMaterials = false;
                                }
                                int color = valid | this.mc.player.capabilities.isCreativeMode ? GREEN : RED;
                                this.fontRenderer.drawString(str, xPos + 8 - this.fontRenderer.getStringWidth(str) / 2, TOP + 170 + canCreateOffset, color);
                            }
                            else if (next instanceof Collection)
                            {
                                Collection<ItemStack> items = (Collection<ItemStack>) next;

                                int amount = 0;

                                for (ItemStack stack : items)
                                {
                                    amount += getAmountInInventory(stack);
                                }

                                RenderHelper.enableGUIStandardItemLighting();

                                Iterator<ItemStack> it = items.iterator();
                                int count = 0;
                                int toRenderIndex = ((int)this.ticksSinceMenuOpenF / 20) % items.size();
                                ItemStack toRender = null;
                                while (it.hasNext())
                                {
                                    ItemStack stack = it.next();
                                    if (count == toRenderIndex)
                                    {
                                        toRender = stack;
                                        break;
                                    }
                                    count++;
                                }

                                if (toRender == null)
                                {
                                    continue;
                                }

                                this.itemRender.renderItemAndEffectIntoGUI(toRender, xPos, yPos);
                                this.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, toRender, xPos, yPos, null);
                                RenderHelper.disableStandardItemLighting();
                                GL11.glEnable(GL11.GL_BLEND);

                                if (mousePosX >= xPos && mousePosX <= xPos + 16 && mousePosY >= yPos && mousePosY <= yPos + 16)
                                {
                                    GL11.glDepthMask(true);
                                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                                    GL11.glPushMatrix();
                                    GL11.glTranslatef(0, 0, 300);
                                    int k = this.fontRenderer.getStringWidth(toRender.getDisplayName());
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

                                    this.fontRenderer.drawString(toRender.getDisplayName(), j2, k2, WHITE);

                                    GL11.glPopMatrix();
                                }

                                str = "" + e.getValue();
                                boolean valid = amount >= e.getValue();
                                if (!valid && validInputMaterials)
                                {
                                    validInputMaterials = false;
                                }
                                int color = valid | this.mc.player.capabilities.isCreativeMode ? GREEN : RED;
                                this.fontRenderer.drawString(str, xPos + 8 - this.fontRenderer.getStringWidth(str) / 2, TOP + 170 + canCreateOffset, color);
                            }

                            i++;
                        }

                        if (validInputMaterials || this.mc.player.capabilities.isCreativeMode)
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
                            if (mousePosX >= RHS - 95 && mousePosX <= RHS && mousePosY >= TOP + 182 + canCreateOffset && mousePosY <= TOP + 182 + 12 + canCreateOffset)
                            {
                                this.drawTexturedModalRect(RHS - 95, TOP + 182 + canCreateOffset, 93, 12, 0, 174, 93, 12, false, false);
                            }
                        }

                        this.drawTexturedModalRect(RHS - 95, TOP + 182 + canCreateOffset, 93, 12, 0, 174, 93, 12, false, false);

                        int color = (int) ((Math.sin(this.ticksSinceMenuOpenF / 5.0) * 0.5 + 0.5) * 255);
                        this.drawSplitString(GCCoreUtil.translate("gui.message.can_create_space_station.name"), RHS - 48, TOP + 137, 91, ColorUtil.to32BitColor(255, color, 255, color), true, false);

                        if (!mapMode)
                        {
                            this.drawSplitString(GCCoreUtil.translate("gui.message.create_ss.name").toUpperCase(), RHS - 48, TOP + 185 + canCreateOffset, 91, WHITE, false, false);
                        }
                    }
                    else
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.cannot_create_space_station.name"), RHS - 48, TOP + 138, 91, WHITE, true, false);
                    }
                }

                // Catalog overlay
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F - Math.min(0.3F, this.ticksSinceSelectionF / 50.0F));
                this.drawTexturedModalRect(LHS, TOP, 74, 11, 0, 392, 148, 22, false, false);
                str = GCCoreUtil.translate("gui.message.catalog.name").toUpperCase();
                this.fontRenderer.drawString(str, LHS + 40 - fontRenderer.getStringWidth(str) / 2, TOP + 1, WHITE);

                // Top bar title:
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                if (this.selectedBody instanceof Satellite)
                {
                    if (this.selectedStationOwner.length() == 0 || !this.selectedStationOwner.equalsIgnoreCase(PlayerUtil.getName(this.mc.player)))
                    {
                        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1);
                    }
                    else
                    {
                        GL11.glColor4f(0.0F, 1.0F, 0.0F, 1);
                    }
                    this.drawTexturedModalRect(width / 2 - 47, TOP, 94, 11, 0, 414, 188, 22, false, false);
                }
                else
                {
                    this.drawTexturedModalRect(width / 2 - 47, TOP, 94, 11, 0, 414, 188, 22, false, false);
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
                    this.drawTexturedModalRect(width / 2 - 30, TOP + 11, 30, 11, 0, 414, 60, 22, false, false);
                    this.drawTexturedModalRect(width / 2, TOP + 11, 30, 11, 128, 414, 60, 22, false, false);
                    str = GCCoreUtil.translateWithFormat("gui.message.tier.name", this.selectedBody.getTierRequirement() == 0 ? "?" : this.selectedBody.getTierRequirement());
                    this.fontRenderer.drawString(str, width / 2 - this.fontRenderer.getStringWidth(str) / 2, TOP + 13, canReach ? GREY4 : RED3);
                }

                str = this.selectedBody.getLocalizedName();

                if (this.selectedBody instanceof Satellite)
                {
                    str = GCCoreUtil.translate("gui.message.rename.name").toUpperCase();
                }

                this.fontRenderer.drawString(str, width / 2 - this.fontRenderer.getStringWidth(str) / 2, TOP + 2, WHITE);

                // Catalog wedge:
                this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                this.drawTexturedModalRect(LHS + 4, TOP, 83, 12, 0, 477, 83, 12, false, false);

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
                    this.drawTexturedModalRect(RHS - 74, TOP, 74, 11, 0, 392, 148, 22, true, false);
                    str = GCCoreUtil.translate("gui.message.launch.name").toUpperCase();
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    this.fontRenderer.drawString(str, RHS - 40 - fontRenderer.getStringWidth(str) / 2, TOP + 2, WHITE);
                }

                if (this.selectionState == EnumSelection.SELECTED && !(this.selectedBody instanceof Satellite))
                {
                    handledSliderPos = true;

                    int sliderPos = this.zoomTooltipPos;
                    if (zoomTooltipPos != 38)
                    {
                        sliderPos = Math.min((int)this.ticksSinceSelectionF * 2, 38);
                        this.zoomTooltipPos = sliderPos;
                    }

                    GL11.glColor4f(0.0F, 0.6F, 1.0F, 1);
                    this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
                    this.drawTexturedModalRect(RHS - 182, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE - sliderPos, 83, 38, 512 - 166, 512 - 76, 166, 76, true, false);

                    boolean flag0 = GalaxyRegistry.getSatellitesForCelestialBody(this.selectedBody).size() > 0;
                    boolean flag1 = this.selectedBody instanceof Planet && GalaxyRegistry.getMoonsForPlanet((Planet) this.selectedBody).size() > 0;
                    if (flag0 && flag1)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.click_again.0.name"), RHS - 182 + 41, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE + 2 - sliderPos, 79, GREY5, false, false);
                    }
                    else if (!flag0 && flag1)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.click_again.1.name"), RHS - 182 + 41, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE + 6 - sliderPos, 79, GREY5, false, false);
                    }
                    else if (flag0)
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.click_again.2.name"), RHS - 182 + 41, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE + 6 - sliderPos, 79, GREY5, false, false);
                    }
                    else
                    {
                        this.drawSplitString(GCCoreUtil.translate("gui.message.click_again.3.name"), RHS - 182 + 41, height - GuiCelestialSelection.BORDER_SIZE - GuiCelestialSelection.BORDER_EDGE_SIZE + 11 - sliderPos, 79, GREY5, false, false);
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
                    str = GCCoreUtil.translate("gui.message.assign_name.name");
                    this.fontRenderer.drawString(str, width / 2 - this.fontRenderer.getStringWidth(str) / 2, this.height / 2 - 35, WHITE);
                    str = GCCoreUtil.translate("gui.message.apply.name");
                    this.fontRenderer.drawString(str, width / 2 - this.fontRenderer.getStringWidth(str) / 2 - 36, this.height / 2 + 23, WHITE);
                    str = GCCoreUtil.translate("gui.message.cancel.name");
                    this.fontRenderer.drawString(str, width / 2 + 36 - this.fontRenderer.getStringWidth(str) / 2, this.height / 2 + 23, WHITE);

                    if (this.renamingString == null)
                    {
                        Satellite selectedSatellite = (Satellite) this.selectedBody;
                        String playerName = PlayerUtil.getName(this.mc.player);
                        this.renamingString = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(playerName).getStationName();
                        if (this.renamingString == null)
                        {
                            this.renamingString = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(playerName.toLowerCase()).getStationName();
                        }
                        if (this.renamingString == null)
                        {
                            this.renamingString = "";
                        }
                    }

                    str = this.renamingString;
                    String str0 = this.renamingString;

                    if ((this.ticksSinceMenuOpenF / 10) % 2 == 0)
                    {
                        str0 += "_";
                    }

                    this.fontRenderer.drawString(str0, width / 2 - this.fontRenderer.getStringWidth(str) / 2, this.height / 2 - 17, WHITE);
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

    /**
     * Draws child bodies (when appropriate) on the left-hand interface
     */
    protected int drawChildren(List<CelestialBody> children, int xOffsetBase, int yOffsetPrior, boolean recursive)
    {
        xOffsetBase += GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE;
        final int yOffsetBase = GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE + 50 + yOffsetPrior;
        int yOffset = 0;
        for (int i = 0; i < children.size(); i++)
        {
            CelestialBody child = children.get(i);
            int xOffset = xOffsetBase + (child.equals(this.selectedBody) ? 5 : 0);  
            final int scale = (int) Math.min(95.0F, Math.max(0.0F, (this.ticksSinceMenuOpenF * 25.0F) - 95 * i));

            this.mc.renderEngine.bindTexture(GuiCelestialSelection.guiMain0);
            float brightness = child.equals(this.selectedBody) ? 0.2F : 0.0F;
            if (child.getReachable())
            {
                GL11.glColor4f(0.0F, 0.6F + brightness, 0.0F, scale / 95.0F);
            }
            else
            {
                GL11.glColor4f(0.6F + brightness, 0.0F, 0.0F, scale / 95.0F);
            }
            this.drawTexturedModalRect(3 + xOffset, yOffsetBase + yOffset + 1, 86, 10, 0, 489, 86, 10, false, false);
//            GL11.glColor4f(5 * brightness, 0.6F + 2 * brightness, 1.0F - 4 * brightness, scale / 95.0F);
            GL11.glColor4f(3 * brightness, 0.6F + 2 * brightness, 1.0F, scale / 95.0F);
            this.drawTexturedModalRect(2 + xOffset, yOffsetBase + yOffset, 93, 12, 95, 464, 93, 12, false, false);

            if (scale > 0)
            {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                int color = 14737632;
                this.fontRenderer.drawString(child.getLocalizedName(), 7 + xOffset, yOffsetBase + yOffset + 2, color);
            }
            
            yOffset += 14;
            if (recursive && child.equals(this.selectedBody))
            {
                List<CelestialBody> grandchildren = this.getChildren(child);
                if (grandchildren.size() > 0)
                {
                    if (this.animateGrandchildren == 14 * grandchildren.size())
                    {
                        yOffset += drawChildren(grandchildren, 10, yOffset, false);
                    }
                    else
                    {
                        if (this.animateGrandchildren >= 14)
                        {
                            List<CelestialBody> partial = new LinkedList<>();
                            for (int j = 0; j < this.animateGrandchildren / 14; j++)
                            {
                                partial.add(grandchildren.get(j));
                            }
                            drawChildren(partial, 10, yOffset, false);
                        }
                        yOffset += this.animateGrandchildren;
                        this.animateGrandchildren += 2;
                    }
                }
            }
        }
        return yOffset;
    }

    protected int getAmountInInventory(ItemStack stack)
    {
        int amountInInv = 0;

        for (int x = 0; x < FMLClientHandler.instance().getClientPlayerEntity().inventory.getSizeInventory(); x++)
        {
            final ItemStack slot = FMLClientHandler.instance().getClientPlayerEntity().inventory.getStackInSlot(x);

            if (slot != null)
            {
                if (SpaceStationRecipe.checkItemEquals(stack, slot))
                {
                    amountInInv += slot.getCount();
                }
            }
        }

        return amountInInv;
    }

    public int drawSplitString(String par1Str, int par2, int par3, int par4, int par5, boolean small, boolean simulate)
    {
        return this.renderSplitString(par1Str, par2, par3, par4, par5, small, simulate);
    }

    protected int renderSplitString(String par1Str, int par2, int par3, int par4, int par6, boolean small, boolean simulate)
    {
        if (small)
        {
            List list = this.fontRenderer.listFormattedStringToWidth(par1Str, par4);

            for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += this.fontRenderer.FONT_HEIGHT)
            {
                String s1 = (String) iterator.next();
                if (!simulate)
                {
                    this.renderStringAligned(s1, par2, par3, par4, par6);
                }
            }

            return list.size();
        }
        else
        {
            List list = this.fontRenderer.listFormattedStringToWidth(par1Str, par4);

            for (Iterator iterator = list.iterator(); iterator.hasNext(); par3 += this.fontRenderer.FONT_HEIGHT)
            {
                String s1 = (String) iterator.next();
                if (!simulate)
                {
                    this.renderStringAligned(s1, par2, par3, par4, par6);
                }
            }

            return list.size();
        }
    }

    protected void renderStringAligned(String par1Str, int par2, int par3, int par4, int par5)
    {
        if (this.fontRenderer.getBidiFlag())
        {
            int i1 = this.fontRenderer.getStringWidth(this.bidiReorder(par1Str));
            par2 = par2 + par4 - i1;
        }

        this.fontRenderer.drawString(par1Str, par2 - this.fontRenderer.getStringWidth(par1Str) / 2, par3, par5, false);
    }

    protected String bidiReorder(String p_147647_1_)
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
        this.drawTexturedModalRect(x, y, width, height, u, v, uWidth, vHeight, invertX, invertY, 512, 512);
    }

    public void drawTexturedModalRect(float x, float y, float width, float height, float u, float v, float uWidth, float vHeight, boolean invertX, boolean invertY, float texSizeX, float texSizeY)
    {
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        float texModX = 1F / texSizeX;
        float texModY = 1F / texSizeY;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float height0 = invertY ? 0 : vHeight;
        float height1 = invertY ? vHeight : 0;
        float width0 = invertX ? uWidth : 0;
        float width1 = invertX ? 0 : uWidth;
        worldRenderer.pos(x, y + height, this.zLevel).tex((u + width0) * texModX, (v + height0) * texModY).endVertex();
        worldRenderer.pos(x + width, y + height, this.zLevel).tex((u + width1) * texModX, (v + height0) * texModY).endVertex();
        worldRenderer.pos(x + width, y, this.zLevel).tex((u + width1) * texModX, (v + height1) * texModY).endVertex();
        worldRenderer.pos(x, y, this.zLevel).tex((u + width0) * texModX, (v + height1) * texModY).endVertex();
        tessellator.draw();
    }

    public void setBlackBackground()
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(0.0D, height, -90.0D).endVertex();
        worldRenderer.pos(width, height, -90.0D).endVertex();
        worldRenderer.pos(width, 0.0D, -90.0D).endVertex();
        worldRenderer.pos(0.0D, 0.0D, -90.0D).endVertex();
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Rotates/translates/scales to appropriate values before drawing celestial bodies
     */
    public Matrix4f setIsometric(float partialTicks)
    {
        Matrix4f mat0 = new Matrix4f();
        Matrix4f.translate(new Vector3f(width / 2.0F, height / 2, 0), mat0, mat0);
        Matrix4f.rotate((float) Math.toRadians(55), new Vector3f(1, 0, 0), mat0, mat0);
        Matrix4f.rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1), mat0, mat0);
        float zoomLocal = this.getZoomAdvanced();
        this.zoom = zoomLocal;
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

    /**
     * Draw background grid
     */
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

    /**
     * Draw orbit circles on gui
     */
    public void drawCircles()
    {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glLineWidth(3);
        int count = 0;

        final float theta = (float) (2 * Math.PI / 90);
        final float cos = (float) Math.cos(theta);
        final float sin = (float) Math.sin(theta);

        for (CelestialBody body : bodiesToRender)
        {
            Vector3f systemOffset = new Vector3f(0.0F, 0.0F, 0.0F);
            if (body instanceof IChildBody)
            {
                systemOffset = this.getCelestialBodyPosition(((IChildBody) body).getParentPlanet());
            }
            else if (body instanceof Planet)
            {
                systemOffset = this.getCelestialBodyPosition(((Planet) body).getParentSolarSystem().getMainStar());
            }

            float x = this.getScale(body);
            float y = 0;

            float alpha = getAlpha(body);

            if (alpha > 0.0F)
            {
                switch (count % 2)
                {
                case 0:
                    GL11.glColor4f(0.0F / 1.4F, 0.6F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
                    break;
                case 1:
                    GL11.glColor4f(0.3F / 1.4F, 0.8F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
                    break;
                }

                CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(body, systemOffset);
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

                CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(body);
                MinecraftForge.EVENT_BUS.post(postEvent);
            }
        }

        GL11.glLineWidth(1);
    }

    /**
     * Returns the transparency of the selected body.
     *
     * Hidden bodies will return 0.0, opaque bodies will return 1.0, and ones fading in/out will pass between those two values
     */
    public float getAlpha(CelestialBody body)
    {
        float alpha = 1.0F;

        if (body instanceof IChildBody)
        {
            boolean selected = body == this.selectedBody || (((IChildBody) body).getParentPlanet() == this.selectedBody && this.selectionState != EnumSelection.SELECTED);
            boolean ready = this.lastSelectedBody != null || this.ticksSinceSelectionF > 35;
            boolean isSibling = getSiblings(this.selectedBody).contains(body);
            boolean isPossible = !(body instanceof Satellite) || (this.possibleBodies != null && this.possibleBodies.contains(body));
            if ((!selected && !isSibling) || !isPossible)
            {
                alpha = 0.0F;
            }
            else if (this.isZoomed() && ((!selected || !ready) && !isSibling))
            {
                alpha = Math.min(Math.max((this.ticksSinceSelectionF - 30) / 15.0F, 0.0F), 1.0F);
            }
        }
        else
        {
            boolean isSelected = this.selectedBody == body;
            boolean isChildSelected = this.selectedBody instanceof IChildBody;
            boolean isOwnChildSelected = isChildSelected && ((IChildBody) this.selectedBody).getParentPlanet() == body;

            if (!isSelected && !isOwnChildSelected && (this.isZoomed() || isChildSelected))
            {
                if (this.lastSelectedBody != null || this.selectedBody instanceof IChildBody)
                {
                    alpha = 0.0F;
                }
                else
                {
                    alpha = 1.0F - Math.min(this.ticksSinceSelectionF / 25.0F, 1.0F);
                }
            }
        }

        return alpha;
    }

    public static class StationDataGUI
    {
        private String stationName;
        private Integer stationDimensionID;

        public StationDataGUI(String stationName, Integer stationDimensionID)
        {
            this.stationName = stationName;
            this.stationDimensionID = stationDimensionID;
        }

        public String getStationName()
        {
            return stationName;
        }

        public void setStationName(String stationName)
        {
            this.stationName = stationName;
        }

        public Integer getStationDimensionID()
        {
            return stationDimensionID;
        }
    }

    protected boolean isZoomed()
    {
        return this.selectionState == EnumSelection.ZOOMED;
    }

    protected boolean isSelected()
    {
        return this.selectionState != EnumSelection.UNSELECTED;
    }

    protected Matrix4f setupMatrix(CelestialBody body, Matrix4f worldMatrix, FloatBuffer fb)
    {
        return setupMatrix(body, worldMatrix, fb, 1.0F);
    }

    protected Matrix4f setupMatrix(CelestialBody body, Matrix4f worldMatrix, FloatBuffer fb, float scaleXZ)
    {
        Matrix4f worldMatrix0 = new Matrix4f(worldMatrix);
        Matrix4f.translate(this.getCelestialBodyPosition(body), worldMatrix0, worldMatrix0);
        Matrix4f worldMatrix1 = new Matrix4f();
        Matrix4f.rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1), worldMatrix1, worldMatrix1);
        Matrix4f.rotate((float) Math.toRadians(-55), new Vector3f(1, 0, 0), worldMatrix1, worldMatrix1);
        if (scaleXZ != 1.0F)
        {
            Matrix4f.scale(new Vector3f(scaleXZ, scaleXZ, 1.0F), worldMatrix1, worldMatrix1);
        }
        worldMatrix1 = Matrix4f.mul(worldMatrix0, worldMatrix1, worldMatrix1);
        fb.rewind();
        worldMatrix1.store(fb);
        fb.flip();
        GL11.glMultMatrix(fb);

        return worldMatrix1;
    }
}
