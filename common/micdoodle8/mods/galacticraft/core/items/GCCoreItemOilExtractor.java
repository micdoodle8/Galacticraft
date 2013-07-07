package micdoodle8.mods.galacticraft.core.items;

import java.lang.reflect.Field;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemOilExtractor extends Item
{
    protected Icon[] icons = new Icon[5];

    public static final String[] names = { "extactor_1", // 0
    "extactor_2", // 1
    "extactor_3", // 2
    "extactor_4", // 3
    "extactor_5" }; // 4

    public GCCoreItemOilExtractor(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (this.getNearestOilBlock(par3EntityPlayer) != null)
        {
            if (this.openCanister(par3EntityPlayer) != null)
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
            }
        }

        return par1ItemStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons = new Icon[5];
        this.icons[0] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "extractor_1" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[1] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "extractor_2" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[2] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "extractor_3" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[3] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "extractor_4" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[4] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "extractor_5" + GalacticraftCore.TEXTURE_SUFFIX);

        this.itemIcon = this.icons[0];
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "item.oilExtractor";
    }

    @Override
    public void onUsingItemTick(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, int count)
    {
        Vector3 blockHit = null;

        if ((blockHit = this.getNearestOilBlock(par3EntityPlayer)) != null)
        {
            final int x = MathHelper.floor_double(blockHit.x), y = MathHelper.floor_double(blockHit.y), z = MathHelper.floor_double(blockHit.z);

            if (this.isOilBlock(par3EntityPlayer, par3EntityPlayer.worldObj, x, y, z))
            {

                par3EntityPlayer.worldObj.setBlock(x, y, z, 0);

                if (this.openCanister(par3EntityPlayer) != null)
                {
                    final ItemStack canister = this.openCanister(par3EntityPlayer);

                    if (canister != null && count % 5 == 0 && canister.getItemDamage() > 25)
                    {
                        canister.setItemDamage(canister.getItemDamage() - 25);
                    }
                }
            }
        }
    }

    private ItemStack openCanister(EntityPlayer player)
    {
        for (final ItemStack stack : player.inventory.mainInventory)
        {
            if (stack != null && stack.getItem() instanceof GCCoreItemOilCanister)
            {
                if (stack.getMaxDamage() - stack.getItemDamage() >= 0 && stack.getMaxDamage() - stack.getItemDamage() < GCCoreItems.oilCanister.getMaxDamage() - 1)
                {
                    return stack;
                }
            }
        }

        return null;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        final int count2 = useRemaining / 2;

        switch (count2 % 5)
        {
        case 0:
            if (useRemaining == 0)
            {
                return this.icons[0];
            }
            return this.icons[4];
        case 1:
            return this.icons[3];
        case 2:
            return this.icons[2];
        case 3:
            return this.icons[1];
        case 4:
            return this.icons[0];
        }

        return this.icons[0];
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        if (par2World.isRemote)
        {
            this.itemIcon = this.icons[0];
        }
    }

    private boolean isOilBlock(EntityPlayer player, World world, int x, int y, int z)
    {
        Class buildCraftClass = null;

        try
        {
            if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
            {
                for (final Field f : buildCraftClass.getFields())
                {
                    if (f.getName().equals("oilMoving") || f.getName().equals("oilStill"))
                    {
                        final Block block = (Block) f.get(null);

                        if (world.getBlockId(x, y, z) == block.blockID && world.getBlockMetadata(x, y, z) == 0)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        catch (final Throwable cnfe)
        {
        }

        if (world.getBlockId(x, y, z) == GCCoreBlocks.crudeOilStill.blockID && world.getBlockMetadata(x, y, z) == 0)
        {
            return true;
        }

        return false;
    }

    private Vector3 getNearestOilBlock(EntityPlayer par1EntityPlayer)
    {
        final float var4 = 1.0F;
        final float var5 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * var4;
        final float var6 = par1EntityPlayer.prevRotationYaw + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * var4;
        final double var7 = par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * var4;
        final double var9 = par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * var4 + 1.62D - par1EntityPlayer.yOffset;
        final double var11 = par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * var4;
        final Vector3 var13 = new Vector3(var7, var9, var11);
        final float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        final float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        final float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        final float var17 = MathHelper.sin(-var5 * 0.017453292F);
        final float var18 = var15 * var16;
        final float var20 = var14 * var16;
        double var21 = 5.0D;

        if (par1EntityPlayer instanceof EntityPlayerMP)
        {
            var21 = ((EntityPlayerMP) par1EntityPlayer).theItemInWorldManager.getBlockReachDistance();
        }

        for (double dist = 0.0; dist <= var21; dist += 1D)
        {
            final Vector3 var23 = var13.add(new Vector3(var18 * dist, var17 * dist, var20 * dist));

            if (this.isOilBlock(par1EntityPlayer, par1EntityPlayer.worldObj, MathHelper.floor_double(var23.x), MathHelper.floor_double(var23.y), MathHelper.floor_double(var23.z)))
            {
                return var23;
            }
        }

        return null;
    }
}
