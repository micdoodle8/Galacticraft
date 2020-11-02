package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import javax.annotation.Nullable;

public class ItemSchematic extends Item implements ISchematicItem, ISortable
{
    public ItemSchematic(Item.Properties builder)
    {
        super(builder);
//        super(EntityHangingSchematic.class);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setMaxStackSize(1);
//        this.setUnlocalizedName(assetName);
    }

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
//            for (int i = 0; i < 2; i++)
//            {
//                list.add(new ItemStack(this, 1, i));
//            }
//        }
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (stack.getItem() == GCItems.schematicBuggy)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("schematic.moonbuggy")));
        }
        else if (stack.getItem() == GCItems.schematicRocketT2)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("schematic.rocket_t2")));

            if (!GalacticraftCore.isPlanetsLoaded)
            {
                tooltip.add(new StringTextComponent(EnumColor.DARK_AQUA + "\"Galacticraft: Planets\" Not Installed!"));
            }
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.SCHEMATIC;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        ItemStack stack = context.getPlayer().getHeldItem(context.getHand());
        BlockPos blockpos = context.getPos().offset(context.getFace());
        Direction facing = context.getFace();

        if (facing != Direction.DOWN && facing != Direction.UP && context.getPlayer().canPlayerEdit(blockpos, facing, stack))
        {
            EntityHangingSchematic entityhanging = this.createEntity(context.getWorld(), blockpos, facing, this.getIndex(stack.getDamage()));

            if (entityhanging != null && entityhanging.onValidSurface())
            {
                if (!context.getWorld().isRemote)
                {
                    entityhanging.playPlaceSound();
                    context.getWorld().addEntity(entityhanging);
                    entityhanging.sendToClient(context.getWorld(), blockpos);
                }

                stack.shrink(1);
            }

            return ActionResultType.SUCCESS;
        }
        else
        {
            return ActionResultType.FAIL;
        }
    }

    private EntityHangingSchematic createEntity(World worldIn, BlockPos pos, Direction clickedSide, int index)
    {
        return new EntityHangingSchematic(GCEntities.HANGING_SCHEMATIC, worldIn, pos, clickedSide, index);
    }

    /**
     * Higher tiers should override - see ItemSchematicTier2 for example
     **/
    protected int getIndex(int damage)
    {
        return damage;
    }

    /**
     * Make sure the number of these will match the index values
     */
    public static void registerSchematicItems()
    {
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematicBuggy, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematicRocketT2, 1));
    }

    /**
     * Make sure the order of these will match the index values
     */
    @OnlyIn(Dist.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_buggy.png"));
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_rocket_t2.png"));
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return "item.galacticraftcore.schematic";
    }
}
