package micdoodle8.mods.galacticraft.core.blocks;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.BlockMulti;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockMulti extends BlockMulti
{
	// 0: Normal, 1: Space Station Teleport, 2: Rocket Launch Pad, 3: Rocket Crafting Table
	Icon[] fakeIcons;
	
	public GCCoreBlockMulti(int id) 
	{
		super(id);
	}

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	fakeIcons = new Icon[2];
    	fakeIcons[0] = par1IconRegister.func_94245_a("galacticraftcore:launch_pad");
    	fakeIcons[1] = par1IconRegister.func_94245_a("galacticraftcore:workbench_nasa_top");
    }

    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
    	switch (par2)
    	{
    	case 2:
    		return this.fakeIcons[0];
    	case 3:
    		return this.fakeIcons[1];
    	default:
    		return this.fakeIcons[0];
    	}
    }

	@Override
	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock)
	{
		this.makeFakeBlock(worldObj, position, mainBlock, 0);
	}

	public void makeFakeBlock(World worldObj, Vector3 position, Vector3 mainBlock, int meta)
	{
		worldObj.setBlockAndMetadataWithNotify(position.intX(), position.intY(), position.intZ(), this.blockID, meta, 3);
		((TileEntityMulti) worldObj.getBlockTileEntity(position.intX(), position.intY(), position.intZ())).setMainBlock(mainBlock);
	}
	
    public float getBlockHardness(World par1World, int par2, int par3, int par4)
    {
        return par1World.getBlockMetadata(par2, par3, par4) == 0 ? this.blockHardness : -1.0F;
    }
}
