package micdoodle8.mods.galacticraft.planets.mars.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GasLiquefierRecipeHandler extends TemplateRecipeHandler
{
    private static final ResourceLocation liquefierGuiTexture = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/gui/gasLiquefier.png");
    private static final ResourceLocation liquefierGasesTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/gui/gasesMethaneOxygenNitrogen.png");
    int ticksPassed;
    int extra = 0;
    int inputGas = 0;  //0 is methane   1 is oxygen   2 is atmosphere or nitrogen
    int outputGas = 0;  //0 is fuel   1 is oxygen   2 is nitrogen
    boolean fillAtmos = false;
    protected FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;

    public String getRecipeId()
    {
        return "galacticraft.liquefier";
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    public Set<Entry<PositionedStack, PositionedStack>> getRecipes()
    {
        return NEIGalacticraftMarsConfig.getLiquefierRecipes();
    }

    @Override
    public void drawBackground(int i)
    {
        int progress = this.ticksPassed % 144;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGuiTexture);
        GuiDraw.drawTexturedModalRect(-2, 0, 3, 4, 168, 64);

        if (progress <= 40)
        {
            if (this.fillAtmos)
            {
                int yoffset = progress / 3;
                GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGasesTexture);
                GuiDraw.drawTexturedModalRect(2, 62 - yoffset, 1 + this.inputGas * 17, 26 - yoffset, 16, yoffset);
            }
            else if (this.inputGas > 0)
            {
                GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGasesTexture);
                GuiDraw.drawTexturedModalRect(2, 62 - 10, 1 + this.inputGas * 17, 26 - 10, 16, 10);
            }
        }
        else if (progress < 104)
        {
            int level = (progress - 41) / 3;

            int yoffset = 20 - level;
            if (this.fillAtmos)
            {
                yoffset = 13 + level / 3;
            }
            GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGasesTexture);
            GuiDraw.drawTexturedModalRect(2, 62 - yoffset, 1 + this.inputGas * 17, 26 - yoffset, 16, yoffset);

            if (this.outputGas == 0)
            {
                GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGuiTexture);
                GuiDraw.drawTexturedModalRect(127, 62 - level, 176 + 16, 26 - level, 16, level);
            }
            else
            {
                GuiDraw.drawTexturedModalRect(127 + ((this.outputGas == 3) ? 21 : 0), 62 - level, 1 + this.outputGas * 17, 26 - level, 16, level);
                GuiDraw.changeTexture(GasLiquefierRecipeHandler.liquefierGuiTexture);
            }

            //Offsets from GUI: x - 5,  y - 4
            int powerlevel = 53 - (progress - 41) / 6;
            GuiDraw.drawTexturedModalRect(37, 13, 176, 38, powerlevel, 7);
            GuiDraw.drawTexturedModalRect(23, 12, 208, 0, 11, 10);
        }

        if (this.fillAtmos)
        {
            String gasname = this.outputGas == 3 ? GCCoreUtil.translate("gas.oxygen.name") : GCCoreUtil.translate("gas.nitrogen.name");
            String text1 = " * " + GCCoreUtil.translate("gui.message.withAtmosphere0.name");
            String text2 = GCCoreUtil.lowerCaseNoun(gasname) + " " + GCCoreUtil.translate("gui.message.withAtmosphere1.name");
            this.fontRendererObj.drawString(text1, 4, 83, 4210752);
            this.fontRendererObj.drawString(text2, 4, 93, 4210752);
        }
    }

    @Override
    public void onUpdate()
    {
        this.ticksPassed += 1 + this.extra;
        //this.extra = 1 - this.extra;
        super.onUpdate();
    }

    @Override
    public void loadTransferRects()
    {
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals(this.getRecipeId()))
        {
            for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
            {
                this.arecipes.add(new CachedLiquefierRecipe(irecipe));
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getValue().item, result))
            {
                this.arecipes.add(new CachedLiquefierRecipe(irecipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        for (final Map.Entry<PositionedStack, PositionedStack> irecipe : this.getRecipes())
        {
            if (NEIServerUtils.areStacksSameTypeCrafting(ingredient, irecipe.getKey().item))
            {
                this.arecipes.add(new CachedLiquefierRecipe(irecipe));
                break;
            }
        }
    }

    @Override
    public ArrayList<PositionedStack> getIngredientStacks(int recipe)
    {
        PositionedStack input = this.arecipes.get(recipe).getIngredients().get(0);
        Item inputItem = input.item.getItem();

        this.inputGas = 2;
        this.fillAtmos = false;
        if (inputItem == AsteroidsItems.methaneCanister)
        {
            this.inputGas = 0;
        }
        else if (inputItem == AsteroidsItems.canisterLOX)
        {
            this.inputGas = 1;
        }
        else if (inputItem == AsteroidsItems.atmosphericValve)
        {
            this.fillAtmos = true;
        }

        if (this.ticksPassed % 144 > 40)
        {
            ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
            stacks.add(new PositionedStack(new ItemStack(inputItem, 1, inputItem.getMaxDamage()), input.relx, input.rely));
            return stacks;
        }
        else
        {
            return (ArrayList<PositionedStack>) this.arecipes.get(recipe).getIngredients();
        }
    }

    @Override
    public PositionedStack getResultStack(int recipe)
    {
        PositionedStack output = this.arecipes.get(recipe).getResult();
        Item outputItem = output.item.getItem();

        if (outputItem == GCItems.fuelCanister)
        {
            this.outputGas = 0;
        }
        else if (outputItem == AsteroidsItems.canisterLOX)
        {
            this.outputGas = 3;
        }
        else
        {
            this.outputGas = 4;
        }

        if (this.ticksPassed % 144 < 104)
        {
            return new PositionedStack(new ItemStack(outputItem, 1, outputItem.getMaxDamage()), output.relx, output.rely);
        }
        else
        {
            return this.arecipes.get(recipe).getResult();
        }
    }

    public class CachedLiquefierRecipe extends TemplateRecipeHandler.CachedRecipe
    {
        public PositionedStack input;
        public PositionedStack output;

        @Override
        public PositionedStack getIngredient()
        {
            return this.input;
        }

        @Override
        public PositionedStack getResult()
        {
            return this.output;
        }

        public CachedLiquefierRecipe(PositionedStack pstack1, PositionedStack pstack2)
        {
            super();
            this.input = pstack1;
            this.output = pstack2;
        }

        public CachedLiquefierRecipe(Map.Entry<PositionedStack, PositionedStack> recipe)
        {
            this(recipe.getKey(), recipe.getValue());
        }
    }

    @Override
    public String getRecipeName()
    {
        return "Gas Liquefier";
    }

    @Override
    public String getGuiTexture()
    {
        return MarsModule.ASSET_PREFIX + "textures/gui/gasLiquefier.png";
    }

    @Override
    public void drawForeground(int recipe)
    {
    }
}
