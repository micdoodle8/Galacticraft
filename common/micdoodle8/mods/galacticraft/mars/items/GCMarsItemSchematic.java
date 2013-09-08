package micdoodle8.mods.galacticraft.mars.items;

import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSchematic;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemSchematic extends GCCoreItemSchematic implements ISchematicItem
{
    protected Icon[] icons = new Icon[1];

    public static final String[] names = { "schematic_rocketT3" }; // 15

    public GCMarsItemSchematic(int itemID)
    {
        super(itemID, "schematic");
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < GCMarsItemSchematic.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons = new Icon[1];
        this.icons[0] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "schematic_rocketT3");
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
                par3List.add(LanguageRegistry.instance().getStringLocalization("schematic.rocketT3.name"));
                par3List.add(EnumColor.DARK_AQUA + "COMING SOON");
                break;
            }
        }
    }
}
