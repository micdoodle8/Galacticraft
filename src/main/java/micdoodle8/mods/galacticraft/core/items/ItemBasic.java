package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBasic extends Item implements ISortableItem
{
    public static final String[] names = { "solar_module_0", "solar_module_1", "raw_silicon", "ingot_copper", "ingot_tin", "ingot_aluminum", "compressed_copper", "compressed_tin", "compressed_aluminum", "compressed_steel", "compressed_bronze", "compressed_iron", "wafer_solar", "wafer_basic", "wafer_advanced", "dehydrated_apple", "dehydrated_carrot", "dehydrated_melon", "dehydrated_potato", "frequency_module", "ambient_thermal_controller" };
    public static final int WAFER_BASIC = 13;
    public static final int WAFER_ADVANCED = 14;

//    protected IIcon[] icons = new IIcon[ItemBasic.names.length];

    public ItemBasic(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(assetName);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        int i = 0;

        for (final String name : ItemBasic.names)
        {
            this.icons[i++] = iconRegister.registerIcon(this.getIconString() + "." + name);
        }
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() > 14 && itemStack.getItemDamage() < 19)
        {
            return this.getUnlocalizedName() + ".canned_food";
        }

        return this.getUnlocalizedName() + "." + ItemBasic.names[itemStack.getItemDamage()];
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < ItemBasic.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
        {
            tooltip.add(EnumColor.BRIGHT_GREEN + GCCoreUtil.translate(this.getUnlocalizedName() + "." + ItemBasic.names[par1ItemStack.getItemDamage()] + ".name"));
        }
        else if (par1ItemStack.getItemDamage() == 19)
        {
            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate("gui.frequency_module.desc.0"));
            tooltip.add(EnumColor.AQUA + GCCoreUtil.translate("gui.frequency_module.desc.1"));
        }
    }

    public int getHealAmount(ItemStack par1ItemStack)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 15:
            return 8;
        case 16:
            return 8;
        case 17:
            return 4;
        case 18:
            return 2;
        default:
            return 0;
        }
    }

    public float getSaturationModifier(ItemStack par1ItemStack)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 15:
            return 0.3F;
        case 16:
            return 0.6F;
        case 17:
            return 0.3F;
        case 18:
            return 0.3F;
        default:
            return 0.0F;
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        if (stack.getItemDamage() > 14 && stack.getItemDamage() < 19)
        {
            --stack.stackSize;
            playerIn.getFoodStats().addStats(this.getHealAmount(stack), this.getSaturationModifier(stack));
            worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            if (!worldIn.isRemote)
            {
                playerIn.entityDropItem(new ItemStack(GCItems.canister, 1, 0), 0.0F);
            }
            return stack;
        }

        return super.onItemUseFinish(stack, worldIn, playerIn);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
        {
            return 32;
        }

        return super.getMaxItemUseDuration(par1ItemStack);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19)
        {
            return EnumAction.EAT;
        }

        return super.getItemUseAction(par1ItemStack);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.getItemDamage() > 14 && par1ItemStack.getItemDamage() < 19 && par3EntityPlayer.canEat(false))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }
        if (par1ItemStack.getItemDamage() == 19)
        {
            if (par3EntityPlayer instanceof EntityPlayerMP)
            {
                GCPlayerStats stats = GCPlayerStats.get(par3EntityPlayer);
                ItemStack gear = stats.getExtendedInventory().getStackInSlot(5);

                if (gear == null && par1ItemStack.getTagCompound() == null)
                {
                    stats.getExtendedInventory().setInventorySlotContents(5, par1ItemStack.copy());
                    par1ItemStack.stackSize = 0;
                }
            }
        }
        return par1ItemStack;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity)
    {
        if (itemStack.getItemDamage() != 19)
        {
            return false;
        }

        //Frequency module
        if (!player.worldObj.isRemote && entity != null && !(entity instanceof EntityPlayer))
        {
            if (itemStack.getTagCompound() == null)
            {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            itemStack.getTagCompound().setLong("linkedUUIDMost", entity.getUniqueID().getMostSignificantBits());
            itemStack.getTagCompound().setLong("linkedUUIDLeast", entity.getUniqueID().getLeastSignificantBits());

            player.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.tracking.message")));
            return true;
        }
        return false;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        switch (meta)
        {
        case 3:
        case 4:
        case 5:
            return EnumSortCategoryItem.INGOT;
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            return EnumSortCategoryItem.PLATE;
        }
        return EnumSortCategoryItem.GENERAL;
    }
    
    @Override
    public float getSmeltingExperience(ItemStack item)
    {
        switch (item.getItemDamage())
        {
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            return 1F;
        }
        return -1F;
    }
}
