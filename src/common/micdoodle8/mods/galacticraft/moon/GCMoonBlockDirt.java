package micdoodle8.mods.galacticraft.moon;

import micdoodle8.mods.galacticraft.moon.client.GCMoonColorizerGrass;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCMoonBlockDirt extends GCMoonBlock
{
	public GCMoonBlockDirt(int i, int j)
	{
		super(i, j, Material.ground);
	}

	@Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant)
    {
        int plantID = plant.getPlantID(world, x, y + 1, z);
        
        if (plant instanceof BlockFlower)
        {
            return true;
        }
        
        return false;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double var1 = 0.5D;
        double var3 = 1.0D;
        return ColorizerGrass.getGrassColor(var1, var3);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int par1)
    {
        return this.getBlockColor();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;

        for (int var8 = -1; var8 <= 1; ++var8)
        {
            for (int var9 = -1; var9 <= 1; ++var9)
            {
            	int var10 = getGrassColorAtYCoord(par3);
                var5 += (var10 & 255);
                var6 += (var10 & 255);
                var7 += var10 & 255;
            }
        }

        return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
    }
    
    private int getGrassColorAtYCoord(int y)
    {
    	return GCMoonColorizerGrass.getGrassColor((y + 100) / 1.7D, (y + 100) / 1.7D);
    }
}
