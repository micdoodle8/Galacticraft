package micdoodle8.mods.galacticraft.core.items;

import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.API.ISchematicItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemSchematic extends Item implements ISchematicItem
{
    protected Icon[] icons = new Icon[1];

    public static final String[] names = { "schematic_buggy", "schematic_rocketT2" }; // 15

    public GCCoreItemSchematic(int itemID)
    {
        super(itemID);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < GCCoreItemSchematic.names.length; i++)
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
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons = new Icon[2];
        this.icons[0] = iconRegister.registerIcon("galacticraftcore:schematic_buggy" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[1] = iconRegister.registerIcon("galacticraftcore:schematic_rocketT2" + GalacticraftCore.TEXTURE_SUFFIX);
    }

    @Override
    public Icon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par2EntityPlayer.worldObj.isRemote)
        {
            switch (par1ItemStack.getItemDamage())
            {
            case 0:
                par3List.add(LanguageRegistry.instance().getStringLocalization("schematic.moonbuggy.name"));
                break;
            case 1:
                par3List.add(LanguageRegistry.instance().getStringLocalization("schematic.rocketT2.name"));
                par3List.add(EnumColor.DARK_AQUA + "COMING SOON");
                break;
            }
        }
    }
}
