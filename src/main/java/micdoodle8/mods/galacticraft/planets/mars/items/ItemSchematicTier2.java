package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSchematicTier2 extends ItemSchematic implements ISchematicItem, ISortableItem
{
    private static int indexOffset = 0;
    
    public ItemSchematicTier2()
    {
        super("schematic");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (int i = 0; i < 3; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
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
                tooltip.add(GCCoreUtil.translate("schematic.rocket_t3.name"));
                break;
            case 1:
                tooltip.add(GCCoreUtil.translate("schematic.cargo_rocket.name"));
                break;
            case 2:
                tooltip.add(GCCoreUtil.translate("schematic.astro_miner.name"));
                break;
            }
        }
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.SCHEMATIC;
    }
    
    /** 
     *  Higher tiers should use this form and make sure they have set up the
     *  indexOffset correctly in registerSchematicItems()
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
        indexOffset = SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematic, 1, 0));
        SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematic, 1, 1));
        SchematicRegistry.registerSchematicItem(new ItemStack(MarsItems.schematic, 1, 2));
    }

    /**
     * Make sure the order of these will match the index values
     */
    @SideOnly(value=Side.CLIENT)
    public static void registerTextures()
    {
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_rocket_t3.png"));
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_rocket_cargo.png"));
        SchematicRegistry.registerTexture(new ResourceLocation("galacticraftplanets", "textures/items/schematic_astro_miner.png"));
    }
}
