package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockBasicMars extends Block implements IDetectableResource, IPlantableBlock, ITileEntityProvider, ITerraformableBlock
{
    @SideOnly(Side.CLIENT)
    private IIcon[] marsBlockIcons;

    //Metadata values:
    //0 copper ore, 1 tin ore, 2 desh ore, 3 iron ore
    //4 cobblestone, 5 top (surface rock), 6 middle, 7 dungeon brick
    //8 desh decoration block
    //9 Mars stone
    //10 dungeon spawner (invisible)
    
    public MapColor getMapColor(int meta)
    {
        switch (meta)
        {
        case 7:
            return MapColor.greenColor;
        case 5:
            return MapColor.dirtColor;
        default:
            return MapColor.redColor;
        }
    }

    public BlockBasicMars()
    {
        super(Material.rock);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 10)
        {
            return null;
        }

        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 10)
        {
            return AxisAlignedBB.getBoundingBox(x + 0.0D, y + 0.0D, z + 0.0D, x + 0.0D, y + 0.0D, z + 0.0D);
        }

        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        if (metadata == 10)
        {
            return 10000.0F;
        }

        if (metadata == 7)
        {
            return 40.0F;
        }

        return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.marsBlockIcons = new IIcon[11];
        this.marsBlockIcons[0] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "cobblestone");
        this.marsBlockIcons[1] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "decoration_desh");
        this.marsBlockIcons[2] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "middle");
        this.marsBlockIcons[3] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "brick");
        this.marsBlockIcons[4] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "top");
        this.marsBlockIcons[5] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "copper");
        this.marsBlockIcons[6] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "desh");
        this.marsBlockIcons[7] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "tin");
        this.marsBlockIcons[8] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "bottom");
        this.marsBlockIcons[9] = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "iron");
        this.marsBlockIcons[10] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "blank");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        final int meta = par1World.getBlockMetadata(par2, par3, par4);

        if (meta == 7)
        {
            return 4.0F;
        }

        if (meta == 10)
        {
            return -1.0F;
        }

        return this.blockHardness;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata == 10)
        {
            return new TileEntityDungeonSpawnerMars();
        }

        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return null;
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        if (meta == 10)
        {
            return false;
        }

        return super.canHarvestBlock(player, meta);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 0:
            return this.marsBlockIcons[5];
        case 1:
            return this.marsBlockIcons[7];
        case 2:
            return this.marsBlockIcons[6];
        case 3:
            return this.marsBlockIcons[9];
        case 4:
            return this.marsBlockIcons[0];
        case 5:
            return this.marsBlockIcons[4];
        case 6:
            return this.marsBlockIcons[2];
        case 7:
            return this.marsBlockIcons[3];
        case 8:
            return this.marsBlockIcons[1];
        case 9:
            return this.marsBlockIcons[8];
        case 10:
            return this.marsBlockIcons[10];
        }

        return this.marsBlockIcons[1];
    }

    @Override
    public Item getItemDropped(int meta, Random random, int par3)
    {
        if (meta == 2)
        {
            return MarsItems.marsItemBasic;
        }
        else if (meta == 10)
        {
            return Item.getItemFromBlock(Blocks.air);
        }

        return Item.getItemFromBlock(this);
    }

    @Override
    public int damageDropped(int meta)
    {
        if (meta == 9)
        {
            return 4;
        }
        else if (meta == 2)
        {
            return 0;
        }
        else
        {
            return meta;
        }
    }

    @Override
    public int quantityDropped(int meta, int fortune, Random random)
    {
        if (meta == 10)
        {
            return 0;
        }
        else if (meta == 2 && fortune >= 1)
        {
            return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
        }

        return 1;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        int var4;

        for (var4 = 0; var4 < 11; ++var4)
        {
            if (var4 != 10)
            {
                par3List.add(new ItemStack(par1, 1, var4));
            }
        }
    }

    @Override
    public boolean isValueable(int metadata)
    {
        switch (metadata)
        {
        case 0:
            return true;
        case 1:
            return true;
        case 2:
            return true;
        case 3:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable)
    {
        return false;
    }

    @Override
    public int requiredLiquidBlocksNearby()
    {
        return 4;
    }

    @Override
    public boolean isPlantable(int metadata)
    {
        return false;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (rand.nextInt(10) == 0)
        {
            int metadata = world.getBlockMetadata(x, y, z);

            if (metadata == 7)
            {
                GalacticraftPlanets.spawnParticle("sludgeDrip", new Vector3(x + rand.nextDouble(), y, z + rand.nextDouble()), new Vector3(0, 0, 0));

                if (rand.nextInt(100) == 0)
                {
                    world.playSound(x, y, z, GalacticraftCore.TEXTURE_PREFIX + "ambience.singledrip", 1, 0.8F + rand.nextFloat() / 5.0F, false);
                }
            }
        }
    }

    @Override
    public boolean isTerraformable(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) == 5 && world.getBlock(x, y + 1, z) instanceof BlockAir;
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
        return metadata < 10;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 2)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }
        if (metadata == 9)
        {
            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
        }
        if (metadata == 10)
        {
            return null;
        }

        return super.getPickBlock(target, world, x, y, z);
    }
    
    @Override
    public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target)
    {
        if (target != Blocks.stone) return false;
    	int meta = world.getBlockMetadata(x, y, z);
    	return (meta == 6 || meta == 9);
    }
}
