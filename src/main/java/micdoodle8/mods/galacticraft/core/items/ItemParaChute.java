package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import static net.minecraft.item.EnumDyeColor.*;

public class ItemParaChute extends Item implements ISortableItem, IClickableItem
{
    public static final String[] names = { "plain", // 0
            "black", // 1
            "blue", // 2
            "lime", // 3
            "brown", // 4
            "darkblue", // 5
            "darkgray", // 6
            "darkgreen", // 7
            "gray", // 8
            "magenta", // 9
            "orange", // 10
            "pink", // 11
            "purple", // 12
            "red", // 13
            "teal", // 14
            "yellow" }; // 15

//    protected IIcon[] icons;

    public ItemParaChute(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            for (int i = 0; i < ItemParaChute.names.length; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        int i = 0;
        this.icons = new IIcon[ItemParaChute.names.length];

        for (String name : ItemParaChute.names)
        {
            this.icons[i++] = iconRegister.registerIcon(this.getIconString() + "_" + name);
        }
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName() + "_" + ItemParaChute.names[itemStack.getItemDamage()];
    }

    /*@Override
    public IIcon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }*/

    // @Override
    // @SideOnly(Side.CLIENT)
    // public int getIconFromDamage(int par1)
    // {
    // switch (par1)
    // {
    // case 0: // plain
    // return 49;
    // case 1: // black
    // return 34;
    // case 2: // blue
    // return 46;
    // case 3: // green
    // return 44;
    // case 4: // brown
    // return 37;
    // case 5: // dark blue
    // return 38;
    // case 6: // dark gray
    // return 42;
    // case 7: // dark green
    // return 36;
    // case 8: // gray
    // return 41;
    // case 9: // magenta
    // return 47;
    // case 10: // orange
    // return 48;
    // case 11: // pink
    // return 43;
    // case 12: // purple
    // return 39;
    // case 13: // red
    // return 35;
    // case 14: // teal
    // return 40;
    // case 15: // yellow
    // return 45;
    // }
    //
    // return 0;
    // }

    public static EnumDyeColor getDyeEnumFromParachuteDamage(int damage)
    {
        switch (damage)
        {
        case 1:
            return BLACK;
        case 13:
            return RED;
        case 7:
            return GREEN;
        case 4:
            return BROWN;
        case 5:
            return BLUE;
        case 12:
            return PURPLE;
        case 14:
            return CYAN;
        case 8:
            return SILVER;
        case 6:
            return GRAY;
        case 11:
            return PINK;
        case 3:
            return LIME;
        case 15:
            return YELLOW;
        case 2:
            return LIGHT_BLUE;
        case 9:
            return MAGENTA;
        case 10:
            return ORANGE;
        case 0:
            return WHITE;
        }

        return WHITE;
    }

    public static int getParachuteDamageValueFromDyeEnum(EnumDyeColor color)
    {
        switch (color)
        {
        case BLACK:
            return 1;
        case RED:
            return 13;
        case GREEN:
            return 7;
        case BROWN:
            return 4;
        case BLUE:
            return 5;
        case PURPLE:
            return 12;
        case CYAN:
            return 14;
        case SILVER:
            return 8;
        case GRAY:
            return 6;
        case PINK:
            return 11;
        case LIME:
            return 3;
        case YELLOW:
            return 15;
        case LIGHT_BLUE:
            return 2;
        case MAGENTA:
            return 9;
        case ORANGE:
            return 10;
        case WHITE:
            return 0;
        }

        return -1;
    }

    public static int getParachuteDamageValueFromDye(int meta)
    {
        return getParachuteDamageValueFromDyeEnum(EnumDyeColor.byDyeDamage(meta));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player instanceof EntityPlayerMP)
        {
            if (itemStack.getItem() instanceof IClickableItem)
            {
                itemStack = ((IClickableItem)itemStack.getItem()).onItemRightClick(itemStack, worldIn, player);
            }

            if (itemStack.isEmpty())
            {
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
    
    public ItemStack onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        ItemStack gear = stats.getExtendedInventory().getStackInSlot(4);

        if (gear.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(4, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }
        
        return itemStack;
    }
}