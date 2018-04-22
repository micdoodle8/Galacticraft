//package micdoodle8.mods.galacticraft.core.nei;
//
//import codechicken.lib.gui.GuiDraw;
//import codechicken.nei.api.stack.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import codechicken.nei.util.NEIServerUtils;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//public class CircuitFabricatorRecipeHandler extends TemplateRecipeHandler
//{
//    private static final ResourceLocation circuitFabricatorTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/circuit_fabricator.png");
//    int ticksPassed;
//
//    public String getRecipeId()
//    {
//        return "galacticraft.circuits";
//    }
//
//    @Override
//    public int recipiesPerPage()
//    {
//        return 1;
//    }
//
//    public Set<Map.Entry<ArrayList<PositionedStack>, PositionedStack>> getRecipes()
//    {
//        HashMap<ArrayList<PositionedStack>, PositionedStack> recipes = new HashMap<ArrayList<PositionedStack>, PositionedStack>();
//
//        for (Map.Entry<HashMap<Integer, PositionedStack>, PositionedStack> stack : NEIGalacticraftConfig.getCircuitFabricatorRecipes())
//        {
//            ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>();
//
//            for (Map.Entry<Integer, PositionedStack> input : stack.getKey().entrySet())
//            {
//                inputStacks.add(input.getValue());
//            }
//
//            recipes.put(inputStacks, stack.getValue());
//        }
//
//        return recipes.entrySet();
//    }
//
//    @Override
//    public void drawBackground(int recipe)
//    {
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GuiDraw.changeTexture(CircuitFabricatorRecipeHandler.circuitFabricatorTexture);
//        GuiDraw.drawTexturedModalRect(-2, 9, 3, 4, 168, 64);
//        GuiDraw.drawTexturedModalRect(68, 73, 73, 68, 96, 35);
//        GuiDraw.drawTexturedModalRect(83, 25, 176, 17 + 10 * (Math.min(this.ticksPassed % 70, 51) / 3 % 3), Math.min(this.ticksPassed % 70, 51), 10);
//    }
//
//    @Override
//    public void onUpdate()
//    {
//        this.ticksPassed += 1;
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
//            for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//            {
//                this.arecipes.add(new CachedCircuitRecipe(irecipe));
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
//        for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//        {
//            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
//            {
//                this.arecipes.add(new CachedCircuitRecipe(irecipe));
//            }
//        }
//    }
//
//    @Override
//    public void loadUsageRecipes(ItemStack ingredient)
//    {
//        for (final Map.Entry<ArrayList<PositionedStack>, PositionedStack> irecipe : this.getRecipes())
//        {
//            for (final PositionedStack pstack : irecipe.getKey())
//            {
//                if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, pstack.item))
//                {
//                    this.arecipes.add(new CachedCircuitRecipe(irecipe));
//                    break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public ArrayList<PositionedStack> getIngredientStacks(int recipe)
//    {
//        return (ArrayList<PositionedStack>) this.arecipes.get(recipe).getIngredients();
//    }
//
//    @Override
//    public PositionedStack getResultStack(int recipe)
//    {
//        if (this.ticksPassed % 70 >= 51)
//        {
//            return this.arecipes.get(recipe).getResult();
//        }
//
//        return null;
//    }
//
//    public class CachedCircuitRecipe extends TemplateRecipeHandler.CachedRecipe
//    {
//        public ArrayList<PositionedStack> input;
//        public PositionedStack output;
//
//        @Override
//        public ArrayList<PositionedStack> getIngredients()
//        {
//            return (ArrayList<PositionedStack>) getCycledIngredients(cycleticks / 20, this.input);
//        }
//
//        @Override
//        public PositionedStack getResult()
//        {
//            return this.output;
//        }
//
//        public CachedCircuitRecipe(ArrayList<PositionedStack> pstack1, PositionedStack pstack2)
//        {
//            super();
//            this.input = pstack1;
//            this.output = pstack2;
//        }
//
//        public CachedCircuitRecipe(Map.Entry<ArrayList<PositionedStack>, PositionedStack> recipe)
//        {
//            this(recipe.getKey(), recipe.getValue());
//        }
//    }
//
//    @Override
//    public String getRecipeName()
//    {
//        return GCCoreUtil.translate("tile.machine2.5.name");
//    }
//
//    @Override
//    public String getGuiTexture()
//    {
//        return Constants.TEXTURE_PREFIX + "textures/gui/circuit_fabricator.png";
//    }
//
//    @Override
//    public void drawForeground(int recipe)
//    {
//    }
//}
