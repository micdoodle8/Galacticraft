package micdoodle8.mods.galacticraft.core.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemSchematic extends Item implements ISchematicItem
{
    public static final String[] names = { "buggy", "rocketT2" }; // 15

//    protected IIcon[] icons = new IIcon[ItemSchematic.names.length];

    public ItemSchematic(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
//        //this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < ItemSchematic.names.length; i++)
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

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + ItemSchematic.names[i]);
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }*/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par2EntityPlayer.worldObj.isRemote)
        {
            switch (par1ItemStack.getItemDamage())
            {
            case 0:
                par3List.add(GCCoreUtil.translate("schematic.moonbuggy.name"));
                break;
            case 1:
                par3List.add(GCCoreUtil.translate("schematic.rocketT2.name"));

                if (!GalacticraftCore.isPlanetsLoaded)
                {
                    par3List.add(EnumColor.DARK_AQUA + "\"Galacticraft: Planets\" Not Installed!");
                }
                break;
            }
        }
    }
}
