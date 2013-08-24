package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntitySlimelingEgg;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsBlockSlimelingEgg extends Block implements ITileEntityProvider
{
    private Icon[] icons;
    public static String[] names = { "redEgg", "blueEgg", "yellowEgg" };

    public GCMarsBlockSlimelingEgg(int i)
    {
        super(i, Material.rock);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons = new Icon[6];
        this.icons[0] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "redEgg_0");
        this.icons[1] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "blueEgg_0");
        this.icons[2] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "yellowEgg_0");
        this.icons[3] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "redEgg_1");
        this.icons[4] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "blueEgg_1");
        this.icons[5] = iconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "yellowEgg_1");
        this.blockIcon = this.icons[0];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        ItemStack stack = player.getCurrentEquippedItem();

        if (stack != null && stack.itemID == Item.slimeBall.itemID && world.getBlockMetadata(x, y, z) < 3)
        {
        }
        else if (world.getBlockMetadata(x, y, z) >= 3)
        {
            int l = world.getBlockMetadata(x, y, z) - 3;
            world.setBlockMetadataWithNotify(x, y, z, l, 2);

            if (!world.isRemote)
            {
                float f = world.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityitem;

                entityitem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(Item.slimeBall));
                entityitem.motionX = (float) world.rand.nextGaussian() * 0.05F;
                entityitem.motionY = (float) world.rand.nextGaussian() * 0.05F + 0.2F;
                entityitem.motionZ = (float) world.rand.nextGaussian() * 0.05F;

                world.spawnEntityInWorld(entityitem);

                TileEntity tile = world.getBlockTileEntity(x, y, z);

                if (tile instanceof GCMarsTileEntitySlimelingEgg)
                {
                    ((GCMarsTileEntitySlimelingEgg) tile).timeToHatch = -1;
                    ((GCMarsTileEntitySlimelingEgg) tile).lastTouchedPlayer = "NoPlayer";
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z)
    {
        ItemStack currentStack = player.getCurrentEquippedItem();

        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            return world.setBlockToAir(x, y, z);
        }
        else
        {
            int l = world.getBlockMetadata(x, y, z) + 3;
            world.setBlockMetadataWithNotify(x, y, z, l, 2);

            TileEntity tile = world.getBlockTileEntity(x, y, z);

            if (tile instanceof GCMarsTileEntitySlimelingEgg)
            {
                ((GCMarsTileEntitySlimelingEgg) tile).timeToHatch = world.rand.nextInt(500) + 100;
                ((GCMarsTileEntitySlimelingEgg) tile).lastTouchedPlayer = player.username;
            }

            return false;
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer par2EntityPlayer, int x, int y, int z, int par6)
    {
        ItemStack currentStack = par2EntityPlayer.getCurrentEquippedItem();

        if (currentStack != null && currentStack.getItem() instanceof ItemPickaxe)
        {
            par2EntityPlayer.addStat(StatList.mineBlockStatArray[this.blockID], 1);
            par2EntityPlayer.addExhaustion(0.025F);
            this.dropBlockAsItem(world, x, y, z, par6 % 3, 0);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int metadata)
    {
        return this.icons[metadata % 6];
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftMars.proxy.getEggRenderID();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int idDropped(int meta, Random random, int par3)
    {
        return this.blockID;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < GCMarsBlockSlimelingEgg.names.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new GCMarsTileEntitySlimelingEgg();
    }
}
