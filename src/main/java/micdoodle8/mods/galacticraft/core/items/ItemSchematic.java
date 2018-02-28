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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSchematic extends ItemHangingEntity implements ISchematicItem, ISortableItem
{
    public ItemSchematic(String assetName)
    {
        super(EntityHangingSchematic.class);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < 2; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (par2EntityPlayer.worldObj.isRemote)
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
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.SCHEMATIC;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else if (side == EnumFacing.UP)
        {
            return false;
        }
        else
        {
            BlockPos blockpos = pos.offset(side);

            if (!playerIn.canPlayerEdit(blockpos, side, stack))
            {
                return false;
            }
            else
            {
                EntityHangingSchematic entityhanging = this.createEntity(worldIn, blockpos, side, this.getIndex(stack.getItemDamage()));

                if (entityhanging != null && entityhanging.onValidSurface())
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.spawnEntityInWorld(entityhanging);
                        entityhanging.sendToClient(worldIn, blockpos);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }
    }

    private EntityHangingSchematic createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide, int index)
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
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.ASSET_PREFIX, "textures/items/schematic_buggy.png"));
        SchematicRegistry.registerTexture(new ResourceLocation(Constants.ASSET_PREFIX, "textures/items/schematic_rocket_t2.png"));
    }
}
