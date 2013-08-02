package micdoodle8.mods.galacticraft.mars.items;

import java.util.List;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemPickaxe extends ItemPickaxe
{
    public static String[] names = { "deshPick" };
    private Icon[] icons = new Icon[2];
    
    public GCMarsItemPickaxe(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        super(par1, par2EnumToolMaterial);
        this.setMaxDamage(par2EnumToolMaterial.getMaxUses() * 2);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        icons[0] = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftmars:"));
        icons[1] = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftmars:") + "_slime");
        this.itemIcon = icons[0];
    }

    @Override
    public Icon getIconFromDamage(int damage)
    {
        if (damage >= this.getMaxDamage() / 2)
        {
            return this.icons[1];
        }
        else
        {
            return this.icons[0];
        }
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, this.getMaxDamage() / 2));
    }

    @Override
    public int getDisplayDamage(ItemStack stack)
    {
        if (stack.getItemDamage() == stack.getMaxDamage())
        {
            return stack.getMaxDamage();
        }
        
        return (stack.getItemDamage() % (this.getMaxDamage() / 2)) * 2;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemstack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
        if ((double)Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D)
        {
            if (itemstack.getItemDamage() == itemstack.getMaxDamage() / 2 - 1)
            {
                par7EntityLivingBase.renderBrokenItemStack(itemstack);
                --itemstack.stackSize;

                if (par7EntityLivingBase instanceof EntityPlayer)
                {
                    EntityPlayer entityplayer = (EntityPlayer)par7EntityLivingBase;
                    entityplayer.addStat(StatList.objectBreakStats[this.itemID], 1);
                }

                if (itemstack.stackSize < 0)
                {
                    itemstack.stackSize = 0;
                }

                itemstack.setItemDamage(0);
            }
            
            itemstack.damageItem(1, par7EntityLivingBase);
        }

        return true;
    }
    
    @Override
    public boolean isDamaged(ItemStack stack)
    {
        return stack.getItemDamage() % (this.getMaxDamage() / 2) != 0 || stack.getItemDamage() == this.getMaxDamage();
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) 
    {
        if (itemstack.getItemDamage() >= this.getMaxDamage() / 2)
        {
            list.add(LanguageRegistry.instance().getStringLocalization("item.deshPickSlime.description.name"));
        }
        
        super.addInformation(itemstack, player, list, par4);
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        return "item." + GCMarsItemPickaxe.names[0];
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
}
