package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockAluminumWire extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    public static final String[] names = { "aluminumWire", "aluminumWireHeavy" };
    private static IIcon[] blockIcons;

    public BlockAluminumWire(String assetName)
    {
        super(Material.cloth);
        this.setStepSound(Block.soundTypeCloth);
        this.setResistance(0.2F);
        this.setBlockBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.65F, 0.65F);
        this.minVector = new Vector3(0.35, 0.35, 0.35);
        this.maxVector = new Vector3(0.65, 0.65, 0.65);
        this.setHardness(0.075F);
        this.setBlockName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        BlockAluminumWire.blockIcons = new IIcon[BlockAluminumWire.names.length];

        for (int i = 0; i < BlockAluminumWire.names.length; i++)
        {
            BlockAluminumWire.blockIcons[i] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + BlockAluminumWire.names[i]);
        }
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 0:
            return BlockAluminumWire.blockIcons[0];
        case 1:
            return BlockAluminumWire.blockIcons[1];
        default:
            return BlockAluminumWire.blockIcons[0];
        }
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntity tile;
    	switch (metadata)
        {
        case 0:
            tile = new TileEntityAluminumWire(1);
            break;
        case 1:
            tile = new TileEntityAluminumWire(2);
            break;
        default:
            return null;
        }
    	
    	return tile;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.POWER;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case 0:
            return GCCoreUtil.translate("tile.aluminumWire.description");
        case 1:
            return GCCoreUtil.translate("tile.aluminumWireHeavy.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
