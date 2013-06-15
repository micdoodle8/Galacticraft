package micdoodle8.mods.galacticraft.core.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluidRoot extends Block
{
    public final static Map<Integer, Boolean> displacementIds = new HashMap<Integer, Boolean>();

    static
    {
        BlockFluidRoot.displacementIds.put(Block.doorWood.blockID, false);
        BlockFluidRoot.displacementIds.put(Block.doorIron.blockID, false);
        BlockFluidRoot.displacementIds.put(Block.signPost.blockID, false);
        BlockFluidRoot.displacementIds.put(Block.signWall.blockID, false);
        BlockFluidRoot.displacementIds.put(Block.reed.blockID, false);
    }

    public int quantaPerBlock = 8;
    public float quantaPerBlockFloat = 8F;
    public int density = 1;
    public int densityDir = -1;

    public int tickRate = 20;
    public int renderPass = 1;
    public int maxScaledLight = 0;

    public BlockFluidRoot(int id, Material material)
    {

        super(id, material);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setTickRandomly(true);
        this.disableStats();
    }

    public BlockFluidRoot setQuantaPerBlock(int quantaPerBlock)
    {
        if (quantaPerBlock > 16 || quantaPerBlock < 1)
        {
            quantaPerBlock = 8;
        }

        this.quantaPerBlock = quantaPerBlock;
        this.quantaPerBlockFloat = quantaPerBlock;
        return this;
    }

    public BlockFluidRoot setDensity(int density)
    {
        if (density == 0)
        {
            density = 1;
        }
        
        this.density = density;
        this.densityDir = density > 0 ? -1 : 1;
        return this;
    }

    public BlockFluidRoot setTickRate(int tickRate)
    {
        if (tickRate <= 0)
        {
            tickRate = 20;
        }
        
        this.tickRate = tickRate;
        return this;
    }

    public BlockFluidRoot setRenderPass(int renderPass)
    {
        this.renderPass = renderPass;
        return this;
    }

    public BlockFluidRoot setMaxScaledLight(int maxScaledLight)
    {
        this.maxScaledLight = maxScaledLight;
        return this;
    }

    public boolean canDisplace(IBlockAccess world, int x, int y, int z)
    {
        int bId = world.getBlockId(x, y, z);
        
        if (bId == 0)
        {
            return true;
        }
        
        if (bId == this.blockID)
        {
            return false;
        }
        
        if (BlockFluidRoot.displacementIds.containsKey(bId))
        {
            return BlockFluidRoot.displacementIds.get(bId);
        }
        
        Material material = Block.blocksList[bId].blockMaterial;
        
        if (material.blocksMovement() || material == Material.portal)
        {
            return false;
        }
        
        return true;
    }

    public boolean displaceIfPossible(World world, int x, int y, int z)
    {
        int bId = world.getBlockId(x, y, z);
        
        if (bId == 0)
        {
            return true;
        }
        
        if (bId == this.blockID)
        {
            return false;
        }
        
        if (BlockFluidRoot.displacementIds.containsKey(bId))
        {
            if (BlockFluidRoot.displacementIds.get(bId))
            {
                Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                return true;
            }
            
            return false;
        }
        
        Material material = Block.blocksList[bId].blockMaterial;
        
        if (material.blocksMovement() || material == Material.portal)
        {
            return false;
        }
        
        Block.blocksList[bId].dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        
        return true;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId)
    {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate);
    }

    @Override
    public boolean func_82506_l()
    {

        return false;
    }

    @Override
    public boolean canCollideCheck(int meta, boolean fullHit)
    {
        return fullHit && meta == this.quantaPerBlock - 1;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }

    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public int tickRate(World world)
    {
        return this.tickRate;
    }

    @Override
    public void velocityToAddToEntity(World world, int x, int y, int z, Entity entity, Vec3 vec)
    {
        if (this.densityDir > 0)
        {
            return;
        }
        
        Vec3 vec_flow = this.getFlowVector(world, x, y, z);
        vec.xCoord += vec_flow.xCoord * (this.quantaPerBlock * 4);
        vec.yCoord += vec_flow.yCoord * (this.quantaPerBlock * 4);
        vec.zCoord += vec_flow.zCoord * (this.quantaPerBlock * 4);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        if (this.maxScaledLight == 0)
        {
            return super.getLightValue(world, x, y, z);
        }
        
        int data = world.getBlockMetadata(x, y, z);
        
        return (int) (data / this.quantaPerBlockFloat * this.maxScaledLight);
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

    @SideOnly(Side.CLIENT)
    @Override
    public float getBlockBrightness(IBlockAccess world, int x, int y, int z)
    {
        float lightThis = world.getLightBrightness(x, y, z);
        float lightUp = world.getLightBrightness(x, y + 1, z);

        return lightThis > lightUp ? lightThis : lightUp;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z)
    {
        int lightThis = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int lightUp = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int lightThisBase = lightThis & 255;
        int lightUpBase = lightUp & 255;
        int lightThisExt = lightThis >> 16 & 255;
        int lightUpExt = lightUp >> 16 & 255;

        return (lightThisBase > lightUpBase ? lightThisBase : lightUpBase) | (lightThisExt > lightUpExt ? lightThisExt : lightUpExt) << 16;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return this.renderPass;
    }

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCrudeOilRenderID();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        if (world.getBlockId(x, y, z) != this.blockID)
        {
            return !world.isBlockOpaqueCube(x, y, z);
        }
        
        Material mat = world.getBlockMaterial(x, y, z);
        return mat == this.blockMaterial ? false : super.shouldSideBeRendered(world, x, y, z, side);
    }

    public static final int getDensity(IBlockAccess world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        
        if (!(block instanceof BlockFluidRoot))
        {
            return Integer.MAX_VALUE;
        }
        
        return ((BlockFluidRoot) block).density;
    }

    @SideOnly(Side.CLIENT)
    public static double getFlowDirection(IBlockAccess world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        
        if (!(Block.blocksList[world.getBlockId(x, y, z)] instanceof BlockFluidRoot))
        {
            return -1000.0;
        }
        
        Vec3 vec = ((BlockFluidRoot) block).getFlowVector(world, x, y, z);

        return vec.xCoord == 0.0D && vec.zCoord == 0.0D ? -1000.0D : Math.atan2(vec.zCoord, vec.xCoord) - Math.PI / 2D;
    }

    public final int getQuantaValue(IBlockAccess world, int x, int y, int z)
    {
        if (world.getBlockId(x, y, z) == 0)
        {
            return 0;
        }
        
        if (world.getBlockId(x, y, z) != this.blockID)
        {
            return -1;
        }
        
        int quantaRemaining = world.getBlockMetadata(x, y, z) + 1;
        
        return quantaRemaining;
    }

    public final int getQuantaValueBelow(IBlockAccess world, int x, int y, int z, int belowThis)
    {
        int quantaRemaining = this.getQuantaValue(world, x, y, z);
        
        if (quantaRemaining >= belowThis)
        {
            return -1;
        }
        
        return quantaRemaining;
    }

    public final int getQuantaValueAbove(IBlockAccess world, int x, int y, int z, int aboveThis)
    {
        int quantaRemaining = this.getQuantaValue(world, x, y, z);
        
        if (quantaRemaining <= aboveThis)
        {
            return -1;
        }
        
        return quantaRemaining;
    }

    public final float getQuantaPercentage(IBlockAccess world, int x, int y, int z)
    {
        int quantaRemaining = this.getQuantaValue(world, x, y, z);
        
        return quantaRemaining / this.quantaPerBlockFloat;
    }

    public Vec3 getFlowVector(IBlockAccess world, int x, int y, int z)
    {
        Vec3 vec = world.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
        int decay = this.quantaPerBlock - this.getQuantaValue(world, x, y, z);

        for (int side = 0; side < 4; ++side)
        {
            int x2 = x;
            int z2 = z;

            switch (side)
            {
            case 0:
                --x2;
                break;
            case 1:
                --z2;
                break;
            case 2:
                ++x2;
                break;
            case 3:
                ++z2;
                break;
            }

            int otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y, z2);
            if (otherDecay >= this.quantaPerBlock)
            {
                if (!world.getBlockMaterial(x2, y, z2).blocksMovement())
                {
                    otherDecay = this.quantaPerBlock - this.getQuantaValue(world, x2, y - 1, z2);

                    if (otherDecay >= 0)
                    {
                        int power = otherDecay - (decay - this.quantaPerBlock);
                        vec = vec.addVector((x2 - x) * power, (y - y) * power, (z2 - z) * power);
                    }
                }
            }
            else if (otherDecay >= 0)
            {
                int power = otherDecay - decay;
                vec = vec.addVector((x2 - x) * power, (y - y) * power, (z2 - z) * power);
            }
        }

        if (world.getBlockId(x, y + 1, z) == this.blockID)
        {
            boolean flag = false;

            if (this.isBlockSolid(world, x, y, z - 1, 2))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x, y, z + 1, 3))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x - 1, y, z, 4))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x + 1, y, z, 5))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x, y + 1, z - 1, 2))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x, y + 1, z + 1, 3))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x - 1, y + 1, z, 4))
            {
                flag = true;
            }
            else if (this.isBlockSolid(world, x + 1, y + 1, z, 5))
            {
                flag = true;
            }

            if (flag)
            {
                vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
            }
        }
        
        vec = vec.normalize();
        return vec;
    }

}
