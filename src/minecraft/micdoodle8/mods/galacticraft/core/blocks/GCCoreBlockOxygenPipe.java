package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Arrays;

import mekanism.api.GasTransmission;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.API.IColorable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenPipe extends BlockContainer
{
	private Icon[] pipeIcons = new Icon[16];
	
	private final float oxygenPipeMin = 0.4F;
	private final float oxygenPipeMax = 0.6F;

	public GCCoreBlockOxygenPipe(int i)
	{
		super(i, Material.glass);
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
    {
		Vector3 thisVec = new Vector3(x, y, z);
		thisVec = thisVec.modifyPositionFromSide(ForgeDirection.getOrientation(par5));
		int idAtSide = thisVec.getBlockID(par1IBlockAccess);
		
        GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1IBlockAccess.getBlockTileEntity(x, y, z);
		
		if (idAtSide == GCCoreBlocks.oxygenPipe.blockID && ((GCCoreTileEntityOxygenPipe)thisVec.getTileEntity(par1IBlockAccess)).getColor() == tileEntity.getColor())
		{
			return this.pipeIcons[15];
		}
		
        return this.pipeIcons[tileEntity.getColor()];
    }

	@Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1World.getBlockTileEntity(x, y, z);
        
        if (!par1World.isRemote)
        {
        	ItemStack stack = par5EntityPlayer.inventory.getCurrentItem();
        	
            if (stack != null)
            {
                if (stack.getItem() instanceof ItemDye)
                {
                    int dyeColor = par5EntityPlayer.inventory.getCurrentItem().getItemDamageForDisplay();
                    
                    byte colorBefore = tileEntity.getColor();
                    
                    tileEntity.setColor((byte) dyeColor);
                    
                    if (colorBefore != (byte) dyeColor && !par5EntityPlayer.capabilities.isCreativeMode && --par5EntityPlayer.inventory.getCurrentItem().stackSize == 0)
                    {
                    	par5EntityPlayer.inventory.mainInventory[par5EntityPlayer.inventory.currentItem] = null;
                    }
                    
                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
                    {
                        float f = 0.7F;
                        double d0 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                        double d1 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.2D + 0.6D;
                        double d2 = (double)(par1World.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                        EntityItem entityitem = new EntityItem(par1World, (double)x + d0, (double)y + d1, (double)z + d2, new ItemStack(Item.dyePowder, 1, colorBefore));
                        entityitem.delayBeforeCanPickup = 10;
                        par1World.spawnEntityInWorld(entityitem);
                    }
                    
                    PacketManager.sendPacketToClients(PacketManager.getPacket(BasicComponents.CHANNEL, tileEntity, tileEntity.getColor(), -1));
                    
            		for (ForgeDirection dir : ForgeDirection.values())
            		{
            			Vector3 vec = new Vector3(tileEntity);
            			vec = vec.modifyPositionFromSide(dir);
            			TileEntity tileAt = vec.getTileEntity(tileEntity.worldObj);
            			
            			if (tileAt != null && tileAt instanceof IColorable)
            			{
            				((IColorable) tileAt).onAdjacentColorChanged(new Vector3(tileAt), new Vector3(tileEntity));
            			}
            		}
            		
                    return true;
                }
                
            }
            
        }
        
        return false;
        
    }
    
	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCOxygenPipeRenderID();
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
    	this.pipeIcons = new Icon[16];
    	
    	for (int count = 0; count < ItemDye.dyeColorNames.length; count++)
    	{
    		this.pipeIcons[count] = par1IconRegister.registerIcon("galacticraftcore:pipe_oxygen_" + ItemDye.dyeColorNames[count]);
    	}
    	
    	this.blockIcon = pipeIcons[15];
    }

	@Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
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
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenPipe();
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		float minX = 0.3F;
		float minY = 0.3F;
		float minZ = 0.3F;
		float maxX = 0.7F;
		float maxY = 0.7F;
		float maxZ = 0.7F;
		
		if(tileEntity != null)
		{
			boolean[] connectable = new boolean[] {false, false, false, false, false, false};
			ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);
			
			for(ITubeConnection connection : connections)
			{
				if(connection !=  null)
				{
					int side = Arrays.asList(connections).indexOf(connection);
					
					if(connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
					{
						connectable[side] = true;
					}
				}
			}
			
			if(connectable[2])
			{
				minZ = 0.0F;
			}
			if(connectable[3])
			{
				maxZ = 1.0F;
			}
			
			if(connectable[4])
			{
				minX = 0.0F;
			}
			
			if(connectable[5])
			{
				maxX = 1.0F;
			}
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec3d, Vec3 vec3d1)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		float minX = 0.0F;
		float minY = 0.0F;
		float minZ = 0.0F;
		float maxX = 1.0F;
		float maxY = 1.0F;
		float maxZ = 1.0F;
		
		if(tileEntity != null)
		{
			boolean[] connectable = new boolean[] {false, false, false, false, false, false};
			ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);
			
			for(ITubeConnection connection : connections)
			{
				if(connection !=  null)
				{
					int side = Arrays.asList(connections).indexOf(connection);
					
					if(connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
					{
						connectable[side] = true;
					}
				}
			}
			
			if(connectable[2])
			{
				minZ = 0.0F;
			}
			
			if(connectable[3])
			{
				maxZ = 1.0F;
			}
			
			if(connectable[4])
			{
				minX = 0.0F;
			}
			
			if(connectable[5])
			{
				maxX = 1.0F;
			}
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}

		final MovingObjectPosition r = super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);

		return super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);
	}
}
