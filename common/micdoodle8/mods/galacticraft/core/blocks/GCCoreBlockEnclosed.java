package micdoodle8.mods.galacticraft.core.blocks;

import java.lang.reflect.Constructor;
import java.util.List;
import micdoodle8.mods.galacticraft.API.IPartialSealedBlock;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import universalelectricity.core.block.IConductor;
import basiccomponents.common.BasicComponents;
import basiccomponents.common.tileentity.TileEntityCopperWire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockEnclosed extends BlockContainer implements IPartialSealedBlock
{
    private Icon[] enclosedIcons;

    public GCCoreBlockEnclosed(int id)
    {
        super(id, Material.cloth);
        this.setStepSound(Block.soundClothFootstep);
        this.setResistance(0.2F);
        this.setHardness(0.4f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        if (BasicComponents.blockCopperWire != null)
        {
            par3List.add(new ItemStack(par1, 1, 0));
        }
        
        par3List.add(new ItemStack(par1, 1, 1));
        
        if (GCCoreCompatibilityManager.isIc2Loaded())
        {
            par3List.add(new ItemStack(par1, 1, 2));
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        switch (par2)
        {
        case 0:
            return this.enclosedIcons[0];
        case 1:
            return this.enclosedIcons[1];
        case 2:
            return this.enclosedIcons[2];
        }

        return this.blockIcon;
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.enclosedIcons = new Icon[3];
        this.enclosedIcons[0] = par1IconRegister.registerIcon("galacticraftcore:enclosed_copper_wire");
        this.enclosedIcons[1] = par1IconRegister.registerIcon("galacticraftcore:enclosed_oxygen_pipe");
        this.enclosedIcons[2] = par1IconRegister.registerIcon("galacticraftcore:enclosed_copper_cable");
        this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:enclosed_copper_wire");
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof IConductor)
        {
            ((IConductor) tileEntity).updateAdjacentConnections();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity instanceof IConductor)
        {
            ((IConductor) tileEntity).updateAdjacentConnections();
        }
    }

    @Override
    public int getRenderType()
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
        case 0:
            return new TileEntityCopperWire();
        case 1:
            return new GCCoreTileEntityOxygenPipe();
        case 2:
            try
            {
                Class clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
                Constructor constructor = clazz.getConstructor(new Class[] { Short.class });
                Object o = constructor.newInstance(new Object[] {0});
                return (TileEntity) o;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        }

        return null;
    }

    @Override
    public boolean isSealed(World world, int x, int y, int z)
    {
        return true;
    }
}
