package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSchematicTier2 extends ItemSchematic implements ISchematicItem, ISortable
{
    private static int indexOffset = 0;

    public ItemSchematicTier2(Item.Properties properties)
    {
        super(properties);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            for (int i = 0; i < 3; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (this == MarsItems.schematicRocketT3)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("schematic.rocket_t3")));
        }
        else if (this == MarsItems.schematicAstroMiner)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("schematic.astro_miner")));
        }
        else
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("schematic.cargo_rocket")));
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.SCHEMATIC;
    }

    /**
     * Higher tiers should use this form and make sure they have set up the
     * indexOffset correctly in registerSchematicItems()
     */
    @Override
    protected int getIndex(int damage)
    {
        return damage + indexOffset;
    }

    /**
     * Make sure the number of these will match the index values
     */
    public static void registerSchematicItems()
    {
        indexOffset = SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematicAstroMiner, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematicRocketT3, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematicCargoRocket, 1));
    }

    /**
     * Make sure the order of these will match the index values
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_rocket_t3.png"));
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_rocket_cargo.png"));
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_astro_miner.png"));
    }
}
