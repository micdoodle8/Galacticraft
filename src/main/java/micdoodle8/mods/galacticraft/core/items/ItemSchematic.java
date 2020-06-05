package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemSchematic extends HangingEntityItem implements ISchematicItem, ISortableItem
{
    public ItemSchematic(Item.Properties builder)
    {
        super(EntityHangingSchematic.class);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
        {
            for (int i = 0; i < 2; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 0:
            tooltip.add(GCCoreUtil.translate("schematic.moonbuggy.name"));
            break;
        case 1:
            tooltip.add(GCCoreUtil.translate("schematic.rocket_t2.name"));

            if (!GalacticraftCore.isPlanetsLoaded)
            {
                tooltip.add(EnumColor.DARK_AQUA + "\"Galacticraft: Planets\" Not Installed!");
            }
            break;
        }
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.SCHEMATIC;
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity playerIn, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = playerIn.getHeldItem(hand);
        BlockPos blockpos = pos.offset(facing);

        if (facing != Direction.DOWN && facing != Direction.UP && playerIn.canPlayerEdit(blockpos, facing, stack))
        {
            EntityHangingSchematic entityhanging = this.createEntity(worldIn, blockpos, facing, this.getIndex(stack.getItemDamage()));

            if (entityhanging != null && entityhanging.onValidSurface())
            {
                if (!worldIn.isRemote)
                {
                    entityhanging.playPlaceSound();
                    worldIn.addEntity(entityhanging);
                    entityhanging.sendToClient(worldIn, blockpos);
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
        return new EntityHangingSchematic(worldIn, pos, clickedSide, index);
    }
    
    /**
    *  Higher tiers should override - see ItemSchematicTier2 for example
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
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematic, 1, 0));
        SchematicRegistry.registerSchematicItem(new ItemStack(GCItems.schematic, 1, 1));
    }
    
    /**
     * Make sure the order of these will match the index values
     */
    @SideOnly(value=Side.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_buggy.png"));
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/items/schematic_rocket_t2.png"));
    }
}
