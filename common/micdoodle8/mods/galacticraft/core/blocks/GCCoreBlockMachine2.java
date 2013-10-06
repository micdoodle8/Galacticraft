package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCoalGenerator;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityElectricIngotCompressor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.block.BlockTile;

public class GCCoreBlockMachine2 extends BlockTile
{
    public static final int ELECTRIC_COMPRESSOR_METADATA = 0;
    public static final int CIRCUIT_FABRICATOR_METADATA = 4;

    private Icon iconMachineSide;
    private Icon iconOutput;

    private Icon iconElectricCompressor;
    private Icon iconCircuitFabricator;

    public GCCoreBlockMachine2(int id, String assetName)
    {
        super(id, UniversalElectricity.machine);
        this.setUnlocalizedName("basicMachine");
        this.setHardness(1.0F);
        this.setStepSound(Block.soundMetalFootstep);
        this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine");
        this.iconOutput = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_output");

        this.iconMachineSide = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "machine_side");
        this.iconElectricCompressor = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "electric_compressor");
        this.iconCircuitFabricator = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "circuit_fabricator");
    }

    @Override
    public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
    {
        TileEntity tile = par1World.getBlockTileEntity(x, y, z);

        if (tile instanceof GCCoreTileEntityCoalGenerator)
        {
            GCCoreTileEntityCoalGenerator tileEntity = (GCCoreTileEntityCoalGenerator) tile;
            if (tileEntity.generateWatts > 0)
            {
                int metadata = par1World.getBlockMetadata(x, y, z);
                float var7 = x + 0.5F;
                float var8 = y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
                float var9 = z + 0.5F;
                float var10 = 0.52F;
                float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

                if (metadata == 3)
                {
                    par1World.spawnParticle("smoke", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                    par1World.spawnParticle("flame", var7 - var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 2)
                {
                    par1World.spawnParticle("smoke", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                    par1World.spawnParticle("flame", var7 + var10, var8, var9 + var11, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 1)
                {
                    par1World.spawnParticle("smoke", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
                    par1World.spawnParticle("flame", var7 + var11, var8, var9 - var10, 0.0D, 0.0D, 0.0D);
                }
                else if (metadata == 0)
                {
                    par1World.spawnParticle("smoke", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
                    par1World.spawnParticle("flame", var7 + var11, var8, var9 + var10, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public Icon getIcon(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.blockIcon;
        }

        if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            metadata -= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA;

            if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
            {
                return this.iconCircuitFabricator;
            }
            
            if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
            {
                return this.iconOutput;
            }
        }
        else if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            metadata -= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA;

            if (metadata == 0 && side == 4 || metadata == 1 && side == 5 || metadata == 2 && side == 3 || metadata == 3 && side == 2)
            {
                return this.iconElectricCompressor;
            }
            
            if (side == ForgeDirection.getOrientation(metadata + 2).ordinal())
            {
                return this.iconOutput;
            }
        }

        return this.iconMachineSide;
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int metadata = world.getBlockMetadata(x, y, z);

        int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 1;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 0;
            break;
        }

        if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            world.setBlockMetadataWithNotify(x, y, z, GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA + change, 3);
        }
        else if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            world.setBlockMetadataWithNotify(x, y, z, GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA + change, 3);
        }
    }

    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);
        int original = metadata;

        int change = 0;

        if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            original -= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA;
        }
        else if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            original -= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
        }

        // Re-orient the block
        switch (original)
        {
        case 0:
            change = 3;
            break;
        case 3:
            change = 1;
            break;
        case 1:
            change = 2;
            break;
        case 2:
            change = 0;
            break;
        }

        if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            change += GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA;
        }
        else if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            change += GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
        }

        par1World.setBlockMetadataWithNotify(x, y, z, change, 3);
        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
            {
                par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
                return true;
            }
            else if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
            {
                par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
                return true;
            }
            else
            {
                par5EntityPlayer.openGui(GalacticraftCore.instance, -1, par1World, x, y, z);
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            return new GCCoreTileEntityCircuitFabricator();
        }
        else if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            return new GCCoreTileEntityElectricIngotCompressor();
        }
        else
        {
            return null;
        }
    }

    public ItemStack getElectricCompressor()
    {
        return new ItemStack(this.blockID, 1, GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA);
    }

    public ItemStack getCircuitFabricator()
    {
        return new ItemStack(this.blockID, 1, GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(this.getElectricCompressor());
        par3List.add(this.getCircuitFabricator());
    }

    @Override
    public int damageDropped(int metadata)
    {
        if (metadata >= GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA)
        {
            return GCCoreBlockMachine2.ELECTRIC_COMPRESSOR_METADATA;
        }
        else if (metadata >= GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA)
        {
            return GCCoreBlockMachine2.CIRCUIT_FABRICATOR_METADATA;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        int id = this.idPicked(world, x, y, z);

        if (id == 0)
        {
            return null;
        }

        Item item = Item.itemsList[id];

        if (item == null)
        {
            return null;
        }

        int metadata = this.getDamageValue(world, x, y, z);

        return new ItemStack(id, 1, metadata);
    }
}
