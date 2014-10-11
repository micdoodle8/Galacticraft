package micdoodle8.mods.galacticraft.core.blocks;

import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockTelemetry extends BlockAdvancedTile implements ItemBlockDesc.IBlockShiftDesc
{
    private IIcon iconFront;
    private IIcon iconSide;
	
	//Metadata: 0-3 = orientation;  bits 2,3 = reserved for future use
	protected BlockTelemetry(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockTextureName("iron_block");
        this.setBlockName(assetName);
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.iconFront = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "cargo_pad");
        this.iconSide = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "cargo_pad");
    }
    
    @Override
    public IIcon getIcon(int side, int metadata)
    {
        if (side == (metadata & 7))
        {
            return this.iconSide;
        }

        return this.iconFront;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        int metadata = 0;

        int angle = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = 0;

        switch (angle)
        {
        case 0:
            change = 3;
            break;
        case 1:
            change = 4;
            break;
        case 2:
            change = 2;
            break;
        case 3:
            change = 5;
            break;
        }

        world.setBlockMetadataWithNotify(x, y, z, change, 3);
    }

    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        final int metadata = world.getBlockMetadata(x, y, z);
        final int facing = metadata & 3;
        int change = 0;
        
        switch (facing)
        {
        	case 0:
        		change = 1;
        		break;
        	case 1:
        		change = 3;
        		break;
        	case 2:
        		change = 5;
        		break;
        	case 3:
        		change = 4;
        		break;
        	case 4:
        		change = 2;
        		break;
        	case 5:
        		change = 0;       		
        }
        change += (12 & metadata);
        world.setBlockMetadataWithNotify(x, y, z, change, 2);

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityTelemetry();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (!world.isRemote)
        {
        	TileEntity tile = world.getTileEntity(x, y, z);
        	if (tile instanceof TileEntityTelemetry)
        	{
        		ItemStack held = player.inventory.getCurrentItem();
        		//Look for Frequency Module
        		if (held != null && held.getItem() == GCItems.basicItem && held.getItemDamage() == 19)
        		{
        			NBTTagCompound fmData = held.stackTagCompound;
        			if (fmData != null && fmData.hasKey("linkedUUIDMost") && fmData.hasKey("linkedUUIDLeast"))
        			{
        				UUID uuid = new UUID(fmData.getLong("linkedUUIDMost"), fmData.getLong("linkedUUIDLeast"));
        				((TileEntityTelemetry) tile).addTrackedEntity(uuid);
        			}
        			else if (fmData == null)
        			{
        				fmData = new NBTTagCompound();
        				held.setTagCompound(fmData);
        			}
        			fmData.setInteger("teCoordX", x);
        			fmData.setInteger("teCoordY", y);
        			fmData.setInteger("teCoordZ", z);
        			fmData.setInteger("teDim", world.provider.dimensionId);
        			return true;
        		}
        	}
        }
    	return false;
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
