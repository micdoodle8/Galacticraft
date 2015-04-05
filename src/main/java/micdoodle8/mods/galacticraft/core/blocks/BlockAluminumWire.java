package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAluminumWire extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    public static final String[] names = { "aluminumWire", "aluminumWireHeavy" };
    private static IIcon[] blockIcons;
    private boolean isHeavy = false;

    public BlockAluminumWire(String assetName, boolean heavy)
    {
        super(Material.cloth);
        this.setStepSound(Block.soundTypeCloth);
        this.setResistance(0.2F);
        this.isHeavy = heavy;
        if (heavy)
        {
	        this.setBlockBounds(0.3375F, 0.3375F, 0.3375F, 0.6625F, 0.6625F, 0.6625F);
	        this.minVector = new Vector3(0.3375, 0.3375, 0.3375);
	        this.maxVector = new Vector3(0.6625, 0.6625, 0.6625);
        }
        else
        {
	        this.setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
	        this.minVector = new Vector3(0.4, 0.4, 0.4);
	        this.maxVector = new Vector3(0.6, 0.6, 0.6);
        }       	
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
        if (isHeavy)
            return BlockAluminumWire.blockIcons[1];

        return BlockAluminumWire.blockIcons[0];
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public int damageDropped(int metadata)
    {
        return 0;
    }
    
    @Override
    public Item getItemDropped(int metadata, Random rand, int fortune)
    {
    	if (metadata > 0)
        	return Item.getItemFromBlock(GCBlocks.aluminumWireHeavy);
    	return Item.getItemFromBlock(this);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntity tile;
        if (isHeavy)
        {
            tile = new TileEntityAluminumWire(2);        	
        }
        else
        {
	    	switch (metadata)
	        {
		        case 0:
		            tile = new TileEntityAluminumWire(1);
		            break;
		        case 1:
		            tile = new TileEntityAluminumWire(2);
		            GCLog.info("Aluminum wire block with metadata is deprecated.");
		            break;
		        default:
		            return null;
	        }
        }
    	
    	return tile;
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.POWER;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        if (isHeavy)
        	return GCCoreUtil.translate("tile.aluminumWireHeavy.description");

        switch (meta)
        {
        case 0:
            return GCCoreUtil.translate("tile.aluminumWire.description");
        case 1:
            return "Aluminum wire block with metadata is deprecated.";
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
