package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Arrays;
import mekanism.api.gas.GasTransmission;
import mekanism.api.gas.ITubeConnection;
import mekanism.api.transmitters.ITransmitter;
import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
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
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenPipe extends BlockContainer implements ITileEntityProvider
{
    private Icon[] pipeIcons = new Icon[16];

    public GCCoreBlockOxygenPipe(int id, String assetName)
    {
        super(id, Material.glass);
        this.setHardness(0.3F);
        this.setStepSound(Block.soundGlassFootstep);
        this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        final GCCoreTileEntityOxygenPipe tile = (GCCoreTileEntityOxygenPipe) par1World.getBlockTileEntity(par2, par3, par4);

        if (tile != null && tile.getColor() != 15)
        {
            final float f = 0.7F;
            final double d0 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            final double d1 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
            final double d2 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            final EntityItem entityitem = new EntityItem(par1World, par2 + d0, par3 + d1, par4 + d2, new ItemStack(Item.dyePowder, 1, tile.getColor()));
            entityitem.delayBeforeCanPickup = 10;
            par1World.spawnEntityInWorld(entityitem);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!world.isRemote && tileEntity instanceof ITransmitter<?>)
        {
            ((ITransmitter<?>) tileEntity).refreshTransmitterNetwork();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (!world.isRemote && tileEntity instanceof ITransmitter<?>)
        {
            ((ITransmitter<?>) tileEntity).refreshTransmitterNetwork();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
    {
        Vector3 thisVec = new Vector3(x, y, z);
        thisVec = thisVec.modifyPositionFromSide(ForgeDirection.getOrientation(par5));
        final int idAtSide = thisVec.getBlockID(par1IBlockAccess);

        final GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1IBlockAccess.getBlockTileEntity(x, y, z);

        if (idAtSide == GCCoreBlocks.oxygenPipe.blockID && ((GCCoreTileEntityOxygenPipe) thisVec.getTileEntity(par1IBlockAccess)).getColor() == tileEntity.getColor())
        {
            return this.pipeIcons[15];
        }

        return this.pipeIcons[tileEntity.getColor()];
    }

    @Override
    public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        final GCCoreTileEntityOxygenPipe tileEntity = (GCCoreTileEntityOxygenPipe) par1World.getBlockTileEntity(x, y, z);

        if (!par1World.isRemote)
        {
            final ItemStack stack = par5EntityPlayer.inventory.getCurrentItem();

            if (stack != null)
            {
                if (stack.getItem() instanceof ItemDye)
                {
                    final int dyeColor = par5EntityPlayer.inventory.getCurrentItem().getItemDamageForDisplay();

                    final byte colorBefore = tileEntity.getColor();

                    tileEntity.setColor((byte) dyeColor);

                    if (colorBefore != (byte) dyeColor && !par5EntityPlayer.capabilities.isCreativeMode && --par5EntityPlayer.inventory.getCurrentItem().stackSize == 0)
                    {
                        par5EntityPlayer.inventory.mainInventory[par5EntityPlayer.inventory.currentItem] = null;
                    }

                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
                    {
                        final float f = 0.7F;
                        final double d0 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        final double d1 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
                        final double d2 = par1World.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        final EntityItem entityitem = new EntityItem(par1World, x + d0, y + d1, z + d2, new ItemStack(Item.dyePowder, 1, colorBefore));
                        entityitem.delayBeforeCanPickup = 10;
                        par1World.spawnEntityInWorld(entityitem);
                    }

                    PacketManager.sendPacketToClients(PacketManager.getPacket(GalacticraftCore.CHANNEL, tileEntity, tileEntity.getColor(), -1));

                    for (final ForgeDirection dir : ForgeDirection.values())
                    {
                        Vector3 vec = new Vector3(tileEntity);
                        vec = vec.modifyPositionFromSide(dir);
                        final TileEntity tileAt = vec.getTileEntity(tileEntity.worldObj);

                        if (tileAt != null && tileAt instanceof IColorable)
                        {
                            ((IColorable) tileAt).onAdjacentColorChanged(dir);
                        }

                        if (!par1World.isRemote && tileAt instanceof ITransmitter<?>)
                        {
                            ((ITransmitter<?>) tileAt).refreshTransmitterNetwork();
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
            this.pipeIcons[count] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "pipe_oxygen_" + ItemDye.dyeColorNames[count]);
        }

        this.blockIcon = this.pipeIcons[15];
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
        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        float minX = 0.3F;
        final float minY = 0.3F;
        float minZ = 0.3F;
        float maxX = 0.7F;
        final float maxY = 0.7F;
        float maxZ = 0.7F;

        if (tileEntity != null)
        {
            final boolean[] connectable = new boolean[] { false, false, false, false, false, false };
            final ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);

            for (final ITubeConnection connection : connections)
            {
                if (connection != null)
                {
                    final int side = Arrays.asList(connections).indexOf(connection);

                    if (connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
                    {
                        connectable[side] = true;
                    }
                }
            }

            if (connectable[2])
            {
                minZ = 0.0F;
            }
            if (connectable[3])
            {
                maxZ = 1.0F;
            }

            if (connectable[4])
            {
                minX = 0.0F;
            }

            if (connectable[5])
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
        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        float minX = 0.0F;
        final float minY = 0.0F;
        float minZ = 0.0F;
        float maxX = 1.0F;
        final float maxY = 1.0F;
        float maxZ = 1.0F;

        if (tileEntity != null)
        {
            final boolean[] connectable = new boolean[] { false, false, false, false, false, false };
            final ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);

            for (final ITubeConnection connection : connections)
            {
                if (connection != null)
                {
                    final int side = Arrays.asList(connections).indexOf(connection);

                    if (connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
                    {
                        connectable[side] = true;
                    }
                }
            }

            if (connectable[2])
            {
                minZ = 0.0F;
            }

            if (connectable[3])
            {
                maxZ = 1.0F;
            }

            if (connectable[4])
            {
                minX = 0.0F;
            }

            if (connectable[5])
            {
                maxX = 1.0F;
            }

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        float minX = 0.35F;
        final float minY = 0.35F;
        float minZ = 0.35F;
        float maxX = 0.65F;
        final float maxY = 0.65F;
        float maxZ = 0.65F;

        if (tileEntity != null)
        {
            final boolean[] connectable = new boolean[] { false, false, false, false, false, false };
            final ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);

            for (final ITubeConnection connection : connections)
            {
                if (connection != null)
                {
                    final int side = Arrays.asList(connections).indexOf(connection);

                    if (connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
                    {
                        connectable[side] = true;
                    }
                }
            }

            if (connectable[2])
            {
                minZ = 0.0F;
            }
            if (connectable[3])
            {
                maxZ = 1.0F;
            }

            if (connectable[4])
            {
                minX = 0.0F;
            }

            if (connectable[5])
            {
                maxX = 1.0F;
            }

            this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }

        return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
    }
}
