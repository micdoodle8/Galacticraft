//package micdoodle8.mods.galacticraft.API;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import net.minecraft.src.Block;
//import net.minecraft.src.Material;
//import cpw.mods.fml.common.FMLLog;
//
//public class BlockBase extends Block
//{
//	BlockFile file;
//	
//	public BlockBase(BlockFile file) 
//	{
//		super(file.getBlockID(), file.getTextureIndex(), Material.rock/*TODO*/);
//		this.file = file;
//		this.setCreativeTab(GalacticraftCore.galacticraftTab);
//		this.setHardness(file.getHardness());
//		this.setResistance(file.getResistance());
//		this.setBlockName(file.getBlockName().toLowerCase());
//		this.setTextureFile(file.getTextureName());
//	}
//}
