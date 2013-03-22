package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.LanServerList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ThreadLanServerFind;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.WorldProvider;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import universalelectricity.components.common.BasicComponents;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreGuiChoosePlanet extends GuiScreen
{
    private int spaceTimer = 0;

    public static RenderItem drawItems = new RenderItem();
    
    private GCCoreGuiChoosePlanetSlot planetSlots;

    private static final Random rand = new Random();

    private final float updateCounter = 0.0F;

    public int selectedSlot;

    private WorldProvider oldProvider;

    private String[] destinations;

    public EntityPlayer playerToSend;

    private boolean initialized;

    public GuiSmallButton sendButton;

    public GuiSmallButton createSpaceStationButton;
    private boolean field_74024_A;

    private static final String[] titlePanoramaPaths = new String[] {"/micdoodle8/mods/galacticraft/core/client/backgrounds/bg3.png"};

    public GCCoreGuiChoosePlanet(EntityPlayer player, String[] listOfDestinations)
    {
    	this.playerToSend = player;
    	this.destinations = listOfDestinations;
    }
    
    public void updateDimensionList(String[] listOfDestinations)
    {
    	this.destinations = listOfDestinations;
    }

    // Override keyTyped so you don't accidently hit Escape and fall to your death!
    @Override
	protected void keyTyped(char par1, int par2) {}

    @Override
	public void initGui()
    {
    	if (!(this.planetSlots == null))
            this.planetSlots.func_77207_a(2, 10, 10, 10);

        this.planetSlots = new GCCoreGuiChoosePlanetSlot(this);

        StringTranslate.getInstance();
        this.buttonList.clear();
        this.buttonList.add(new GCCoreGuiTexturedButton(0, this.width - 28, 5, 22, 22, "/micdoodle8/mods/galacticraft/core/client/gui/button1.png", 22, 22));
        this.buttonList.add(this.sendButton = new GuiSmallButton(1, this.width - 110, this.height - 26, 105, 20, "Send To Dimension"));
        
        if (this.createSpaceStationButton == null)
        {
        	this.buttonList.add(this.createSpaceStationButton = new GuiSmallButton(2, (this.width / 2) - 60, 4, 120, 20, "Create Space Station"));
        	this.createSpaceStationButton.enabled = true;
        }
        else
        {
        	this.buttonList.add(this.createSpaceStationButton);
        }
        
        this.planetSlots.registerScrollButtons(this.buttonList, 2, 3);
    }

    @Override
	public void updateScreen()
    {
        this.spaceTimer += 2;
    }

    @Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void drawPanorama2(float par1)
    {
        final Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        final byte var5 = 1;


        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 128.0F;
            final float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 128.0F;
            final float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9 + 0.5F);
            GL11.glScalef(7F, 7F, 7F);

            for (int var10 = 0; var10 < 9; ++var10)
            {
                GL11.glPushMatrix();

                if (var10 == 1)
                {
                	GL11.glTranslatef(1.96F, 0.0F, 0.0F);
                }

                if (var10 == 2)
                {
                	GL11.glTranslatef(-1.96F, 0.0F, 0.0F);
                }

                if (var10 == 3)
                {
                	GL11.glTranslatef(0.0F, 1.96F, 0.0F);
                }

                if (var10 == 4)
                {
                	GL11.glTranslatef(0.0F, -1.96F, 0.0F);
                }

                if (var10 == 5)
                {
                	GL11.glTranslatef(-1.96F, -1.96F, 0.0F);
                }

                if (var10 == 6)
                {
                	GL11.glTranslatef(-1.96F, 1.96F, 0.0F);
                }

                if (var10 == 7)
                {
                	GL11.glTranslatef(1.96F, -1.96F, 0.0F);
                }

                if (var10 == 8)
                {
                	GL11.glTranslatef(1.96F, 1.96F, 0.0F);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(GCCoreGuiChoosePlanet.titlePanoramaPaths[var10 - var10]));
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawPanorama(float par1)
    {
        final Tessellator var4 = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        final byte var5 = 1;


        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
            final float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
            final float var9 = 0.0F;
            GL11.glTranslatef(var7, var8, var9);
            GL11.glRotatef(MathHelper.sin((this.spaceTimer + par1) / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-(this.spaceTimer + par1) * 0.005F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(41, 0, 0, 1);

            for (int var10 = 0; var10 < 6; ++var10)
            {
                GL11.glPushMatrix();


                if (var10 == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var10 == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(GCCoreGuiChoosePlanet.titlePanoramaPaths[0]));
                var4.startDrawingQuads();
                var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
                var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
                var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
                var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
                var4.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        var4.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture(GCCoreGuiChoosePlanet.titlePanoramaPaths[0]));
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    public void renderSkybox(float par1)
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 0.0F, 1.0F);
        this.drawPanorama(par1);
        this.drawPanorama2(par1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.rotateAndBlurSkybox();
        final Tessellator var4 = Tessellator.instance;
        var4.startDrawingQuads();
        final float var5 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
        final float var6 = this.height * var5 / 256.0F;
        final float var7 = this.width * var5 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        final int var8 = this.width;
        final int var9 = this.height;
        var4.addVertexWithUV(0.0D, var9, this.zLevel, 0.5F - var6, 0.5F + var7);
        var4.addVertexWithUV(var8, var9, this.zLevel, 0.5F - var6, 0.5F - var7);
        var4.addVertexWithUV(var8, 0.0D, this.zLevel, 0.5F + var6, 0.5F - var7);
        var4.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + var6, 0.5F + var7);
        var4.draw();
        GL11.glPopMatrix();
        GL11.glColorMask(true, true, true, true);
        this.mc.renderEngine.func_98185_a();
    }
    
    protected void drawItemStackTooltip(List<String> strings, List<ItemStack> items, List<Boolean> correctAmount, int par2, int par3)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        if (!strings.isEmpty())
        {
            int k = 0;
            int l;
            int i1;

            for (l = 0; l < strings.size(); ++l)
            {
                i1 = this.fontRenderer.getStringWidth((String)strings.get(l));

                if (i1 > k)
                {
                    k = i1 + (items.size() == 0 ? 0 : 34);
                }
            }

            l = par2 + 12;
            i1 = par3 - 12;
            int j1 = 14;

            if (strings.size() > 1)
            {
                j1 += 2 + (strings.size() - 1) * (items.size() == 0 ? 10 : 16);
            }

            if (((this.height - 500) / 2) + i1 + j1 + 6 > this.height)
            {
                i1 = this.height - j1 - ((this.height - 500) / 2) - 6;
            }

            this.zLevel = 300.0F;
            this.drawItems.zLevel = 300.0F;
            int k1 = -267386864;
            
            this.drawGradientRect(l - 3, i1 - 4, l + k + 3, i1 - 3, k1, k1);
            this.drawGradientRect(l - 3, i1 + j1 + 3, l + k + 3, i1 + j1 + 4, k1, k1);
            this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 + j1 + 3, k1, k1);
            this.drawGradientRect(l - 4, i1 - 3, l - 3, i1 + j1 + 3, k1, k1);
            this.drawGradientRect(l + k + 3, i1 - 3, l + k + 4, i1 + j1 + 3, k1, k1);
            int l1 = 1347420415;
            int i2 = (l1 & 16711422) >> 1 | l1 & -16777216;
            this.drawGradientRect(l - 3, i1 - 3 + 1, l - 3 + 1, i1 + j1 + 3 - 1, l1, i2);
            this.drawGradientRect(l + k + 2, i1 - 3 + 1, l + k + 3, i1 + j1 + 3 - 1, l1, i2);
            this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 - 3 + 1, l1, l1);
            this.drawGradientRect(l - 3, i1 + j1 + 2, l + k + 3, i1 + j1 + 3, i2, i2);

            int stringY = i1 + (items.size() == 0 ? 0 : 5);
            
            for (int j2 = 0; j2 < strings.size(); ++j2)
            {
                String s = (String)strings.get(j2);
                Boolean b = correctAmount.get(j2);
                
                s = "\u00a7" + Integer.toHexString(b ? 10 : 4) + s;

                this.fontRenderer.drawStringWithShadow(s, l + (items.size() == 0 ? 0 : 19), stringY, -1);

                stringY += items.size() > 0 ? 16 : 14;
            }

            int itemY = i1;
            
            for (ItemStack stack : items)
            {
            	this.drawItems.renderItemAndEffectIntoGUI(fontRenderer, this.mc.renderEngine, stack, l, itemY);
            	
            	itemY += 16;
            }

            this.zLevel = 0.0F;
            this.drawItems.zLevel = 0.0F;
        }
    }

    @Override
	public void drawScreen(int par1, int par2, float par3)
    {
    	String str = null;
    	
    	if (this.createSpaceStationButton != null)
    	{
        	if (this.createSpaceStationButton.enabled == false && this.canCreateSpaceStation(this.selectedSlot))
        	{
        		this.createSpaceStationButton.enabled = true;
        	}
        	else if (this.createSpaceStationButton.enabled == true && !this.canCreateSpaceStation(this.selectedSlot))
        	{
        		this.createSpaceStationButton.enabled = false;
        	}
    	}

    	if (this.initialized)
    	{
            this.planetSlots.drawScreen(par1, par2, par3);
            super.drawScreen(par1, par2, par3);
    	}

		for (final IGalacticraftSubModClient mod : GalacticraftCore.clientSubMods)
		{
    		String dest = this.destinations[this.selectedSlot].toLowerCase();

    		if (dest.contains("*"))
    		{
    			dest = dest.replace("*", "");
    		}

    		if (mod.getDimensionName().toLowerCase().equals(dest))
    		{
				str = LanguageRegistry.instance().getStringLocalization("gui.choosePlanet.desc." + dest);
    		}
    	}

    	if (this.destinations[this.selectedSlot].toLowerCase().equals("overworld"))
    	{
    		str = LanguageRegistry.instance().getStringLocalization("gui.choosePlanet.desc.overworld");
    	}

    	if (str != null)
    	{
	    	final String[] strArray = str.split("#");

	    	final int j = 260 / strArray.length + 1;

	    	for (int i = 0; i < strArray.length; i++)
	    	{
	    		if (strArray[i].contains("*"))
	    		{
	    			strArray[i] = strArray[i].replace("*", "");
		            this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16716305);
	    		}
	    		else
	    		{
		            this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16777215);
	    		}
	    	}
    	}
    	
    	if (createSpaceStationButton != null)
    	{
        	GCCorePlayerBaseClient clientPlayer = PlayerUtil.getPlayerBaseClientFromPlayer(playerToSend);
        	
    		if (par1 >= createSpaceStationButton.xPosition && par2 >= createSpaceStationButton.yPosition && par1 < createSpaceStationButton.xPosition + 120 && par2 < createSpaceStationButton.yPosition + 20)
    		{
    			if (this.playerAlreadyCreatedDimension(clientPlayer))
    			{
    				List<String> strings = new ArrayList();
    				List<ItemStack> items = new ArrayList();
    				List<Boolean> hasEnough = new ArrayList();
    				strings.add("Space Station Already");
    				strings.add("        Created!");
    				hasEnough.add(false);
    				hasEnough.add(false);
    				this.drawItemStackTooltip(strings, items, hasEnough, createSpaceStationButton.xPosition + 115, createSpaceStationButton.yPosition + 15);
    			}
    			else if (this.canCreateSpaceStation(this.selectedSlot))
    			{
    				List<String> strings = new ArrayList();
    				List<ItemStack> items = new ArrayList();
    				List<Boolean> hasEnough = new ArrayList();
    				ItemStack stack = null;
    				stack = RecipeUtil.getStandardSpaceStationRequirements().get(0);
    				strings.add("Tin: " + this.getItemCountInPlayerInventory(this.mc.thePlayer, stack) + " / " + this.getNumberRequired(stack));
    				hasEnough.add(this.hasCorrectAmount(this.mc.thePlayer, stack));
    				stack = RecipeUtil.getStandardSpaceStationRequirements().get(1);
    				strings.add("Steel: " + this.getItemCountInPlayerInventory(this.mc.thePlayer, stack) + " / " + this.getNumberRequired(stack));
    				hasEnough.add(this.hasCorrectAmount(this.mc.thePlayer, stack));
    				stack = RecipeUtil.getStandardSpaceStationRequirements().get(2);
    				strings.add("Iron: " + this.getItemCountInPlayerInventory(this.mc.thePlayer, stack) + " / " + this.getNumberRequired(stack));
    				hasEnough.add(this.hasCorrectAmount(this.mc.thePlayer, stack));
    				items.add(new ItemStack(BasicComponents.itemIngot, 1, 1));
    				items.add(new ItemStack(BasicComponents.itemIngot, 1, 3));
    				items.add(new ItemStack(Item.ingotIron, 1, 0));
    				this.drawItemStackTooltip(strings, items, hasEnough, createSpaceStationButton.xPosition + 115, createSpaceStationButton.yPosition + 15);
    			}
    			else
    			{
    				List<String> strings = new ArrayList();
    				List<ItemStack> items = new ArrayList();
    				List<Boolean> hasEnough = new ArrayList();
    				strings.add("Cannot create Space");
    				strings.add("     Station here!");
    				hasEnough.add(false);
    				hasEnough.add(false);
    				this.drawItemStackTooltip(strings, items, hasEnough, createSpaceStationButton.xPosition + 115, createSpaceStationButton.yPosition + 15);
    			}
    		}
    	}
    }
    
    private static int getItemCountInPlayerInventory(EntityPlayer player, ItemStack itemToFind)
    {
    	int count = 0;
    	
    	if (player != null)
    	{
    		for (ItemStack stack : player.inventory.mainInventory)
    		{
    			if (stack != null && itemToFind != null && stack.itemID == itemToFind.itemID && stack.getItemDamage() == itemToFind.getItemDamage())
    			{
    				count += stack.stackSize;
    			}
    		}
    	}
    	
    	return count;
    }
    
    public static boolean hasCorrectMaterials(EntityPlayer player, List<ItemStack> stacks)
    {
    	boolean flag = true;
    	
    	for (ItemStack stack : stacks)
    	{
    		if (!hasCorrectAmount(player, stack))
    		{
    			flag = false;
    		}
    	}
    	
    	return flag;
    }
    
    public static boolean hasCorrectAmount(EntityPlayer player, ItemStack stack)
    {
    	return getItemCountInPlayerInventory(player, stack) >= getNumberRequired(stack);
    }
    
    public static int getNumberRequired(ItemStack stack)
    {
    	if (stack.itemID == BasicComponents.itemIngot.itemID && stack.getItemDamage() == 1) // tin
    	{
    		return 16;
    	}
    	else if (stack.itemID == BasicComponents.itemIngot.itemID && stack.getItemDamage() == 3) // steel
    	{
    		return 8;
    	}
    	else if (stack.itemID == Item.ingotIron.itemID && stack.getItemDamage() == 0) // iron
    	{
    		return 12;
    	}
    	
    	return 0;
    }

    public void drawBlackBackground()
    {
        final ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        final int var6 = var5.getScaledWidth();
        final int var7 = var5.getScaledHeight();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.renderEngine.getTexture("/micdoodle8/mods/galacticraft/core/client/backgrounds/black.png"));
        final Tessellator var3 = Tessellator.instance;
        var3.startDrawingQuads();
        var3.addVertexWithUV(0.0D, var7, -90.0D, 0.0D, 1.0D);
        var3.addVertexWithUV(var6, var7, -90.0D, 1.0D, 1.0D);
        var3.addVertexWithUV(var6, 0.0D, -90.0D, 1.0D, 0.0D);
        var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        var3.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
	protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            Object[] toSend2 = {this.mc.thePlayer.username};
        	FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiGalaxyMap(this.playerToSend, this.destinations));
        	break;
        case 1:
        	if (par1GuiButton.enabled)
        	{
        		String dimension = this.destinations[this.selectedSlot];
                final Object[] toSend = {dimension};
                if (dimension.contains("$"))
                {
                    this.mc.gameSettings.thirdPersonView = 0;
                }
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 2, toSend));
                return;
        	}
        	else
        	{
        		FMLLog.severe("Severe problem when trying to teleport " + this.playerToSend.username);
        	}
        	break;
        case 2:
        	if (initialized && par1GuiButton.enabled && this.hasCorrectMaterials(this.mc.thePlayer, RecipeUtil.getStandardSpaceStationRequirements()))
        	{
                final Object[] toSend = {this.destinations[this.selectedSlot]};
                PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 15, toSend));
                par1GuiButton.enabled = false;
                return;
        	}
        	break;
        }
    }

    public boolean isValidDestination(int i)
    {
    	final String str = this.destinations[i];

    	if (str.contains("*"))
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }

    public boolean canCreateSpaceStation(int i)
    {
    	final String str = this.destinations[i];
    	
    	GCCorePlayerBaseClient clientPlayer = PlayerUtil.getPlayerBaseClientFromPlayer(playerToSend);

    	if (clientPlayer != null && str.toLowerCase().equals("overworld"))
    	{
    		initialized = true;
    		
    		if (clientPlayer.spaceStationDimensionIDClient == 0)
    		{
    			return true;
    		}
    		else if (clientPlayer.spaceStationDimensionIDClient == -1)
    		{
    			return true;
    		}
    		else
    		{
    			return false;
    		}
    	}
		else
		{
			return false;
		}
    }
    
    public boolean playerAlreadyCreatedDimension(GCCorePlayerBaseClient clientPlayer)
    {
    	if (clientPlayer != null && clientPlayer.spaceStationDimensionIDClient != 0 && clientPlayer.spaceStationDimensionIDClient != -1)
    	{
    		return true;
    	}
    	
    	return false;
    }

    public boolean hasSpacestation(int i)
    {
    	final String str = this.destinations[i];

    	if (str.contains("$"))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    static EntityPlayer getPlayerToSend(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.playerToSend;
    }

    static String[] getDestinations(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.destinations;
    }

    static GuiSmallButton getSendButton(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.sendButton;
    }

    static GuiSmallButton getCreateSpaceStationButton(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
    	return par0GuiLanguage.createSpaceStationButton;
    }

    static int setSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage, int par1)
    {
        return par0GuiLanguage.selectedSlot = par1;
    }

    static int getSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage)
    {
        return par0GuiLanguage.selectedSlot;
    }
}
