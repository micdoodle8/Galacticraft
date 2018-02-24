package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMeteorChunk extends Item implements ISortableItem
{
    public static final String[] names = { "meteor_chunk", "meteor_chunk_hot" };

    public static final int METEOR_BURN_TIME = 45 * 20;

    public ItemMeteorChunk(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabMaterials);
        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5)
    {
        if (itemstack.getItemDamage() == 1 && !world.isRemote)
        {
            if (itemstack.hasTagCompound())
            {
                float meteorBurnTime = itemstack.getTagCompound().getFloat("MeteorBurnTimeF");

                if (meteorBurnTime >= 0.5F)
                {
                    meteorBurnTime -= 0.5F;
                    itemstack.getTagCompound().setFloat("MeteorBurnTimeF", meteorBurnTime);
                }
                else
                {
                    itemstack.setItemDamage(0);
                    itemstack.setTagCompound(null);
                }
            }
            else
            {
                itemstack.setTagCompound(new NBTTagCompound());
                itemstack.getTagCompound().setFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
            }
        }
    }

    @Override
    public void onCreated(ItemStack itemstack, World world, EntityPlayer entityPlayer)
    {
        super.onCreated(itemstack, world, entityPlayer);

        if (itemstack.getItemDamage() == 1)
        {
            if (!itemstack.hasTagCompound())
            {
                itemstack.setTagCompound(new NBTTagCompound());
            }

            itemstack.getTagCompound().setFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (itemstack.getItemDamage() > 0)
        {
            float burnTime = 0.0F;

            if (itemstack.hasTagCompound())
            {
                float meteorBurnTime = itemstack.getTagCompound().getFloat("MeteorBurnTimeF");
                burnTime = Math.round(meteorBurnTime / 10.0F) / 2.0F;
            }
            else
            {
                burnTime = 45.0F;
            }

            tooltip.add(GCCoreUtil.translate("item.hot_description.name") + " " + burnTime + GCCoreUtil.translate("gui.seconds"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "item." + ItemMeteorChunk.names[itemStack.getItemDamage()];
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
            --itemStack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 1.0F, 0.0001F / (Item.itemRand.nextFloat() * 0.1F));

        if (!world.isRemote)
        {
            EntityMeteorChunk meteor = new EntityMeteorChunk(world, player, 1.0F);

            if (itemStack.getItemDamage() > 0)
            {
                meteor.setFire(20);
                meteor.isHot = true;
            }
            meteor.canBePickedUp = player.capabilities.isCreativeMode ? 2 : 1;
            world.spawnEntityInWorld(meteor);
        }

        player.swingItem();

        return itemStack;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
