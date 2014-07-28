package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockIce;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Random;

public class BlockIceAsteroids extends BlockBreakable
{
    public BlockIceAsteroids(String assetName)
    {
        super(assetName, Material.ice, false);
        this.slipperiness = 0.98F;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setBlockTextureName(AsteroidsModule.TEXTURE_PREFIX + assetName);
        this.setBlockName(assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return super.shouldSideBeRendered(world, x, y, z, 1 - side);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(world, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player))
        {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(meta);

            if (itemstack != null) items.add(itemstack);

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
                this.dropBlockAsItem(world, x, y, z, is);
        }
        else
        {
            if (world.provider.isHellWorld || world.provider instanceof IGalacticraftWorldProvider)
            {
                world.setBlockToAir(x, y, z);
                return;
            }

            int i1 = EnchantmentHelper.getFortuneModifier(player);
            harvesters.set(player);
            this.dropBlockAsItem(world, x, y, z, meta, i1);
            harvesters.set(null);
            Material material = world.getBlock(x, y - 1, z).getMaterial();

            if (material.blocksMovement() || material.isLiquid())
            {
                world.setBlock(x, y, z, Blocks.flowing_water);
            }
        }
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11 - this.getLightOpacity())
        {
            if (world.provider.isHellWorld || world.provider instanceof IGalacticraftWorldProvider)
            {
                world.setBlockToAir(x, y, z);
                return;
            }

            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, Blocks.water);
        }
    }

    @Override
    public int getMobilityFlag()
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(this.getTextureName());
    }
}
