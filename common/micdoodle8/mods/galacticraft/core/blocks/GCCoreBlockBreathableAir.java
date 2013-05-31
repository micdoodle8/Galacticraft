package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenPressureProtocol;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GCCoreBlockBreathableAir extends Block
{
    public GCCoreBlockBreathableAir(int var1)
    {
        super(var1, Material.air);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isAirBlock(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4)
    {
        return null;
    }

    @Override
    public boolean isBlockReplaceable(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    @Override
    public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4)
    {
        return true;
    }

    @Override
    public boolean canCollideCheck(int var1, boolean var2)
    {
        return false;
    }

    @Override
    public int getRenderBlockPass()
    {
        return GCCoreConfigManager.transparentBreathableAir ? 1 : 0;
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("galacticraftcore:breathable_air");
    }

    @Override
    public int getMobilityFlag()
    {
        return 1;
    }

    @Override
    public int idDropped(int var1, Random var2, int var3)
    {
        return -1;
    }

    @Override
    public void onBlockAdded(World var1, int var2, int var3, int var4)
    {
        if (var1.getBlockMetadata(var2, var3, var4) == 5)
        {
            this.spread(var1, var2, var3, var4);
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par1IBlockAccess.getBlockId(par2, par3, par4) == this.blockID)
        {
            return false;
        } else
        {
            final int i = par1IBlockAccess.getBlockId(par2, par3, par4);
            boolean var6 = false;

            if (Block.blocksList[i] != null)
            {
                var6 = !Block.blocksList[i].isOpaqueCube();
            }

            final boolean var7 = i == 0;

            if ((var6 || var7) && par5 == 3 && !var6)
            {
                return true;
            } else if ((var6 || var7) && par5 == 4 && !var6)
            {
                return true;
            } else if ((var6 || var7) && par5 == 5 && !var6)
            {
                return true;
            } else if ((var6 || var7) && par5 == 2 && !var6)
            {
                return true;
            } else if ((var6 || var7) && par5 == 0 && !var6)
            {
                return true;
            } else if ((var6 || var7) && par5 == 1 && !var6)
            {
                return true;
            } else
            {
                return false;
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5)
    {
        if (var5 != 0 && var5 != GCCoreBlocks.breatheableAir.blockID)
        {
            this.spread(var1, var2, var3, var4);
        }
    }

    private void spread(World var1, int var2, int var3, int var4)
    {
        final OxygenPressureProtocol var5 = new OxygenPressureProtocol();
        var5.unSeal(var1, var2, var3, var4);
    }
}
