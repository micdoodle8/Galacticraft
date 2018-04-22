//package micdoodle8.mods.galacticraft.planets.mars.client.nei;
//
//import codechicken.lib.gui.GuiDraw;
//import codechicken.nei.api.stack.PositionedStack;
//import codechicken.nei.recipe.GuiRecipe;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import codechicken.nei.util.NEIServerUtils;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
//import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import static codechicken.lib.gui.GuiDraw.getMousePosition;
//
//public class MethaneSynthesizerRecipeHandler extends TemplateRecipeHandler
//{
//    private static final ResourceLocation synthesizerGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/methane_synthesizer.png");
//    private static final ResourceLocation synthesizerGasesTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/gases_methane_oxygen_nitrogen.png");
//    int ticksPassed;
//    int extra = 0;
//
//    boolean fillAtmos = false;
//    protected FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
//
//    public String getRecipeId()
//    {
//        return "galacticraft.synthesizer";
//    }
//
//    @Override
//    public int recipiesPerPage()
//    {
//        return 1;
//    }
//
//    public Set<Entry<PositionedStack, PositionedStack>> getRecipes()
//    {
//        return NEIGalacticraftMarsConfig.getSynthesizerRecipes();
//    }
//
//    @Override
//    public void drawBackground(int recipe)
//    {
//        int progress = this.ticksPassed % 144;
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        GuiDraw.changeTexture(MethaneSynthesizerRecipeHandler.synthesizerGuiTexture);
//        GuiDraw.drawTexturedModalRect(-2, 0, 3, 4, 168, 66);
//
//        if (progress <= 40)
//        {
//            int level = progress * 38 / 40;
//            GuiDraw.changeTexture(MethaneSynthesizerRecipeHandler.synthesizerGasesTexture);
//            GuiDraw.drawTexturedModalRect(2, 62 - level, 35, 38 - level, 16, level);
//
//            if (this.fillAtmos)
//            {
//                int yoffset = progress / 2;
//                GuiDraw.drawTexturedModalRect(23, 44 - yoffset, 35, 26 - yoffset, 16, yoffset);
//            }
//        }
//        else if (progress < 104)
//        {
//            int level = (progress - 41) / 3;
//
//            int yoffset = 20 - level;
//            GuiDraw.changeTexture(MethaneSynthesizerRecipeHandler.synthesizerGasesTexture);
//            GuiDraw.drawTexturedModalRect(2, 62 - yoffset, 35, 26 - yoffset, 16, yoffset);
//            if (this.fillAtmos)
//            {
//                GuiDraw.drawTexturedModalRect(23, 44 - yoffset, 35, 26 - yoffset, 16, yoffset);
//            }
//            GuiDraw.drawTexturedModalRect(148, 62 - level, 1, 26 - level, 16, level);
//
//            //Offsets from GUI: x - 5,  y - 4
//            int powerlevel = 53 - (progress - 41) / 6;
//            GuiDraw.changeTexture(MethaneSynthesizerRecipeHandler.synthesizerGuiTexture);
//            GuiDraw.drawTexturedModalRect(61, 13, 176, 38, powerlevel, 7);
//            GuiDraw.drawTexturedModalRect(47, 12, 208, 0, 11, 10);
//        }
//
//        if (this.fillAtmos)
//        {
//            String gasname = GCCoreUtil.translate("gas.carbondioxide.name");
//            String text1 = " * " + GCCoreUtil.translate("gui.message.with_atmosphere0.name");
//            String text2 = " " + GCCoreUtil.lowerCaseNoun(gasname);
//            String text3 = GCCoreUtil.translate("gui.message.with_atmosphere1.name");
//            this.fontRenderer.drawString(text1, 4, 85, 4210752);
//            this.fontRenderer.drawString(text2, 18, 95, 4210752);
//            this.fontRenderer.drawString(text3, 18, 105, 4210752);
//        }
//    }
//
//    @Override
//    public void onUpdate()
//    {
//        this.ticksPassed += 1 + this.extra;
//        //this.extra = 1 - this.extra;
//        super.onUpdate();
//    }
//
//    @Override
//    public void loadTransferRects()
//    {
//    }
//
//    @Override
//    public void loadCraftingRecipes(String outputId, Object... results)
//    {
//        if (outputId.equals(this.getRecipeId()))
//        {
//            for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
//            {
//                this.arecipes.add(new CachedSynthesizerRecipe(irecipe));
//            }
//        }
//        else
//        {
//            super.loadCraftingRecipes(outputId, results);
//        }
//    }
//
//    @Override
//    public void loadCraftingRecipes(ItemStack result)
//    {
//        for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
//        {
//            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
//            {
//                this.arecipes.add(new CachedSynthesizerRecipe(irecipe));
//            }
//        }
//    }
//
//    @Override
//    public void loadUsageRecipes(ItemStack ingredient)
//    {
//        for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
//        {
//            if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, irecipe.getKey().item))
//            {
//                this.arecipes.add(new CachedSynthesizerRecipe(irecipe));
//                break;
//            }
//        }
//    }
//
//    @Override
//    public ArrayList<PositionedStack> getIngredientStacks(int recipe)
//    {
//        PositionedStack input = this.arecipes.get(recipe).getIngredients().get(0);
//        Item inputItem = input.item.getItem();
//
//        if (inputItem == AsteroidsItems.atmosphericValve)
//        {
//            this.fillAtmos = true;
//        }
//        else
//        {
//            this.fillAtmos = false;
//        }
//
//        if (this.ticksPassed % 144 > 40)
//        {
//            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//            if (inputItem != MarsItems.carbonFragments)
//            {
//                stacks.add(new PositionedStack(new ItemStack(inputItem, 1, inputItem.getMaxDamage()), input.relx, input.rely));
//            }
//            else if ((this.ticksPassed % 144) < 104)
//            {
//                int number = 24 - ((this.ticksPassed % 144) - 40) * 3 / 8;
//                stacks.add(new PositionedStack(new ItemStack(inputItem, number, 0), input.relx, input.rely));
//            }
//            return stacks;
//        }
//        else
//        {
//            return (ArrayList<PositionedStack>) this.arecipes.get(recipe).getIngredients();
//        }
//    }
//
//    @Override
//    public PositionedStack getResultStack(int recipe)
//    {
//        if (this.ticksPassed % 144 < 104)
//        {
//            PositionedStack output = this.arecipes.get(recipe).getResult();
//            Item outputItem = output.item.getItem();
//            return new PositionedStack(new ItemStack(outputItem, 1, outputItem.getMaxDamage()), output.relx, output.rely);
//        }
//        else
//        {
//            return this.arecipes.get(recipe).getResult();
//        }
//    }
//
//    public class CachedSynthesizerRecipe extends TemplateRecipeHandler.CachedRecipe
//    {
//        public PositionedStack input;
//        public PositionedStack output;
//
//        @Override
//        public PositionedStack getIngredient()
//        {
//            return this.input;
//        }
//
//        @Override
//        public PositionedStack getResult()
//        {
//            return this.output;
//        }
//
//        public CachedSynthesizerRecipe(PositionedStack pstack1, PositionedStack pstack2)
//        {
//            super();
//            this.input = pstack1;
//            this.output = pstack2;
//        }
//
//        public CachedSynthesizerRecipe(Map.Entry<PositionedStack, PositionedStack> recipe)
//        {
//            this(recipe.getKey(), recipe.getValue());
//        }
//    }
//
//    @Override
//    public String getRecipeName()
//    {
//        return GCCoreUtil.translate("tile.mars_machine.5.name");
//    }
//
//    @Override
//    public String getGuiTexture()
//    {
//        return GalacticraftPlanets.ASSET_PREFIX + "/textures/gui/methaneSynthesizer.png";
//    }
//
//    @Override
//    public void drawForeground(int recipe)
//    {
//    }
//
//    @Override
//    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
//    {
//        Point mousePos = getMousePosition();
//        try
//        {
//            Class<GuiContainer> clazz = GuiContainer.class;
//            mousePos.x -= (Integer) clazz.getField("field_147003_i").get(gui);
//            mousePos.y -= (Integer) clazz.getField("field_147009_r").get(gui);
//        }
//        catch (Exception ee)
//        {
//            ee.printStackTrace();
//        }
//
//        if (mousePos.x < 23 && mousePos.x > 6 && mousePos.y < 78 && mousePos.y > 39)
//        {
//            currenttip.add(GCCoreUtil.translate("fluid.hydrogen"));
//        }
//        else if (mousePos.x < 44 && mousePos.x > 27 && mousePos.y < 60 && mousePos.y > 39)
//        {
//            currenttip.add(GCCoreUtil.translate("gas.carbondioxide.name"));
//        }
//
//        return currenttip;
//    }
//}
