package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockSlimelingEgg extends Block implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    private IIcon[] icons;
    public static String[] names = { "redEgg", "blueEgg", "yellowEgg" };

    public BlockSlimelingEgg()
    {
        super(Material.rock);
        this.setBlockBounds(0.17F, 0.0F, 0.11F, 0.83F, 0.70F, 0.89F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[6];
        this.icons[0] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_0");
        this.icons[1] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_0");
        this.icons[2] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_0");
        this.icons[3] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "redEgg_1");
        this.icons[4] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "blueEgg_1");
        this.icons[5] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "yellowEgg_1");
        this.blockIcon = this.icons[0];
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        Block block = par1World.getBlock(par2, par3 - 1, par4);
        return block.isSideSolid(par1World, par2, par3, par4, ForgeDirection.UP);
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();
        int l = world.getBlockMetadata(x, y, z);

        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            return world.setBlockToAir(x, y, z);
        }
        else if (player.capabilities.isCreativeMode)
        {
            return world.setBlockToAir(x, y, z);
        }
        else if (l < 3)
        {
            world.setBlockMetadataWithNotify(x, y, z, l + 3, 2);

            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof TileEntitySlimelingEgg)
            {
                ((TileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(50) + 20;
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerUUID = VersionUtil.mcVersionMatches("1.7.2") ? player.getCommandSenderName() : player.getUniqueID().toString();
                ((TileEntitySlimelingEgg) tile).lastTouchedPlayerName = player.getCommandSenderName();
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer par2EntityPlayer, int x, int y, int z, int par6)
    {
        ItemStack currentStack = par2EntityPlayer.getCurrentEquippedItem();

        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            par2EntityPlayer.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            par2EntityPlayer.addExhaustion(0.025F);
            this.dropBlockAsItem(world, x, y, z, par6 % 3, 0);
            if (currentStack.getItem() == MarsItems.deshPickaxe && EnchantmentHelper.getSilkTouchModifier(par2EntityPlayer))
            {
                ItemStack itemstack = new ItemStack(MarsItems.deshPickSlime, 1, currentStack.getItemDamage());
                if (currentStack.stackTagCompound != null)
                {
                    itemstack.stackTagCompound = (NBTTagCompound) currentStack.stackTagCompound.copy();
                }
                par2EntityPlayer.setCurrentItemOrArmor(0, itemstack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return this.icons[metadata % 6];
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftPlanets.getBlockRenderID(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int par3)
    {
        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < BlockSlimelingEgg.names.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySlimelingEgg();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata == 3)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 0);
        }
        if (metadata == 4)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 1);
        }
        if (metadata == 5)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, 2);
        }
        return super.getPickBlock(target, world, x, y, z);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
