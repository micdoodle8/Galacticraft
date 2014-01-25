package gregtechmod.api.enums;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IIconContainer;
import gregtechmod.api.util.GT_Config;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class GT_ItemTextures implements Runnable {
	public GT_ItemTextures() {
		GregTech_API.sGTItemIconload.add(this);
	}
	
	@Override
	public void run() {
		for (MetaIcons tIcon : MetaIcons.values()) tIcon.mIcon = ((IconRegister)GregTech_API.sItemIcons).registerIcon(GregTech_API.TEXTURE_PATH_ITEM + (GT_Config.system?"troll":"colorized/" + tIcon));
	}
	
	public enum MetaIcons implements IIconContainer {
		  INGOT_METALLIC, INGOT_SHINY, INGOT_DULL, INGOT_LEAF
		, INGOT_HOT
		, INGOT_DOUBLE_METALLIC, INGOT_DOUBLE_SHINY, INGOT_DOUBLE_DULL, INGOT_DOUBLE_LEAF
		, INGOT_TRIPLE_METALLIC, INGOT_TRIPLE_SHINY, INGOT_TRIPLE_DULL, INGOT_TRIPLE_LEAF
		, INGOT_QUADRUPLE_METALLIC, INGOT_QUADRUPLE_SHINY, INGOT_QUADRUPLE_DULL, INGOT_QUADRUPLE_LEAF
		, INGOT_QUINTUPLE_METALLIC, INGOT_QUINTUPLE_SHINY, INGOT_QUINTUPLE_DULL, INGOT_QUINTUPLE_LEAF
		, PLATE_METALLIC, PLATE_SHINY, PLATE_DULL, PLATE_LEAF
		, PLATE_DOUBLE_METALLIC, PLATE_DOUBLE_SHINY, PLATE_DOUBLE_DULL, PLATE_DOUBLE_LEAF
		, PLATE_TRIPLE_METALLIC, PLATE_TRIPLE_SHINY, PLATE_TRIPLE_DULL, PLATE_TRIPLE_LEAF
		, PLATE_QUADRUPLE_METALLIC, PLATE_QUADRUPLE_SHINY, PLATE_QUADRUPLE_DULL, PLATE_QUADRUPLE_LEAF
		, PLATE_QUINTUPLE_METALLIC, PLATE_QUINTUPLE_SHINY, PLATE_QUINTUPLE_DULL, PLATE_QUINTUPLE_LEAF
		, PLATE_DENSE_METALLIC, PLATE_DENSE_SHINY, PLATE_DENSE_DULL, PLATE_DENSE_LEAF
		, ROD_METALLIC, ROD_SHINY, ROD_DULL
		, NUGGET_METALLIC, NUGGET_SHINY, NUGGET_DULL, NUGGET_LEAF
		, GEM_METALLIC, GEM_FLINT, GEM_RUBY, GEM_SMALL, GEM_ROUND, GEM_SQUARE, GEM_OCTAGON, GEM_QUARTZ, GEM_LAPIS, GEM_NETHERSTAR, GEM_DIAMOND, GEM_EMERALD, GEM_RECTANGLE_VERTICAL, GEM_RECTANGLE_HORIZONTAL
		, DUST_METALLIC, DUST_SHINY, DUST_ROUGH, DUST_FINE, DUST_DULL
		, DUST_IMPURE_METALLIC, DUST_IMPURE_SHINY, DUST_IMPURE_ROUGH, DUST_IMPURE_FINE, DUST_IMPURE_DULL
		, DUST_SMALL_METALLIC, DUST_SMALL_SHINY, DUST_SMALL_ROUGH, DUST_SMALL_FINE, DUST_SMALL_DULL
		, DUST_TINY_METALLIC, DUST_TINY_SHINY, DUST_TINY_ROUGH, DUST_TINY_FINE, DUST_TINY_DULL
		, CRUSHED
		, PURIFIED
		, CENTRIFUGED
		, LENS
		, ROUND
		, BOLT
		, SCREW
		, RING
		, CELL, CELL_EMPTY, CELL_PLASMA
		;
		
		protected Icon mIcon;
		
		@Override public Icon getIcon() {return mIcon;}
		@Override public Icon getOverlayIcon() {return null;}
		@Override public int getOverlayX() {return 0;}
		@Override public int getOverlayY() {return 0;}
		@Override public int getOverlayWidth() {return 16;}
		@Override public int getOverlayHeight() {return 16;}
	}

	public static final IIconContainer[] SET_METALLIC = new IIconContainer[] {
		MetaIcons.DUST_TINY_METALLIC,
		MetaIcons.DUST_SMALL_METALLIC,
		MetaIcons.DUST_METALLIC,
		MetaIcons.DUST_IMPURE_METALLIC,
		MetaIcons.DUST_METALLIC,
		MetaIcons.CRUSHED,
		MetaIcons.PURIFIED,
		MetaIcons.CENTRIFUGED,
		MetaIcons.GEM_METALLIC,
		MetaIcons.NUGGET_METALLIC,
		null,
		MetaIcons.INGOT_METALLIC,
		MetaIcons.INGOT_HOT,
		MetaIcons.INGOT_DOUBLE_METALLIC,
		MetaIcons.INGOT_TRIPLE_METALLIC,
		MetaIcons.INGOT_QUADRUPLE_METALLIC,
		MetaIcons.INGOT_QUINTUPLE_METALLIC,
		MetaIcons.PLATE_METALLIC,
		MetaIcons.PLATE_DOUBLE_METALLIC,
		MetaIcons.PLATE_TRIPLE_METALLIC,
		MetaIcons.PLATE_QUADRUPLE_METALLIC,
		MetaIcons.PLATE_QUINTUPLE_METALLIC,
		MetaIcons.PLATE_DENSE_METALLIC,
		MetaIcons.ROD_METALLIC,
		MetaIcons.LENS,
		MetaIcons.ROUND,
		MetaIcons.BOLT,
		MetaIcons.SCREW,
		MetaIcons.RING,
		null,
		MetaIcons.CELL,
		MetaIcons.CELL_PLASMA
	};

	public static final IIconContainer[] SET_SHINY = new IIconContainer[] {
		MetaIcons.DUST_TINY_SHINY,
		MetaIcons.DUST_SMALL_SHINY,
		MetaIcons.DUST_SHINY,
		MetaIcons.DUST_IMPURE_SHINY,
		MetaIcons.DUST_SHINY,
		MetaIcons.CRUSHED,
		MetaIcons.PURIFIED,
		MetaIcons.CENTRIFUGED,
		MetaIcons.GEM_METALLIC,
		MetaIcons.NUGGET_SHINY,
		null,
		MetaIcons.INGOT_SHINY,
		MetaIcons.INGOT_HOT,
		MetaIcons.INGOT_DOUBLE_SHINY,
		MetaIcons.INGOT_TRIPLE_SHINY,
		MetaIcons.INGOT_QUADRUPLE_SHINY,
		MetaIcons.INGOT_QUINTUPLE_SHINY,
		MetaIcons.PLATE_SHINY,
		MetaIcons.PLATE_DOUBLE_SHINY,
		MetaIcons.PLATE_TRIPLE_SHINY,
		MetaIcons.PLATE_QUADRUPLE_SHINY,
		MetaIcons.PLATE_QUINTUPLE_SHINY,
		MetaIcons.PLATE_DENSE_SHINY,
		MetaIcons.ROD_SHINY,
		MetaIcons.LENS,
		MetaIcons.ROUND,
		MetaIcons.BOLT,
		MetaIcons.SCREW,
		MetaIcons.RING,
		null,
		MetaIcons.CELL,
		MetaIcons.CELL_PLASMA
	};
	
	public static final IIconContainer[] SET_DULL = new IIconContainer[] {
		MetaIcons.DUST_TINY_DULL,
		MetaIcons.DUST_SMALL_DULL,
		MetaIcons.DUST_DULL,
		MetaIcons.DUST_IMPURE_DULL,
		MetaIcons.DUST_DULL,
		MetaIcons.CRUSHED,
		MetaIcons.PURIFIED,
		MetaIcons.CENTRIFUGED,
		MetaIcons.GEM_METALLIC,
		MetaIcons.NUGGET_DULL,
		null,
		MetaIcons.INGOT_DULL,
		MetaIcons.INGOT_HOT,
		MetaIcons.INGOT_DOUBLE_DULL,
		MetaIcons.INGOT_TRIPLE_DULL,
		MetaIcons.INGOT_QUADRUPLE_DULL,
		MetaIcons.INGOT_QUINTUPLE_DULL,
		MetaIcons.PLATE_DULL,
		MetaIcons.PLATE_DOUBLE_DULL,
		MetaIcons.PLATE_TRIPLE_DULL,
		MetaIcons.PLATE_QUADRUPLE_DULL,
		MetaIcons.PLATE_QUINTUPLE_DULL,
		MetaIcons.PLATE_DENSE_DULL,
		MetaIcons.ROD_DULL,
		MetaIcons.LENS,
		MetaIcons.ROUND,
		MetaIcons.BOLT,
		MetaIcons.SCREW,
		MetaIcons.RING,
		null,
		MetaIcons.CELL,
		MetaIcons.CELL_PLASMA
	};
	
	public static final IIconContainer[] SET_LEAF = new IIconContainer[] {
		MetaIcons.DUST_TINY_FINE,
		MetaIcons.DUST_SMALL_FINE,
		MetaIcons.DUST_FINE,
		MetaIcons.DUST_IMPURE_FINE,
		MetaIcons.DUST_FINE,
		MetaIcons.CRUSHED,
		MetaIcons.PURIFIED,
		MetaIcons.CENTRIFUGED,
		MetaIcons.GEM_METALLIC,
		MetaIcons.NUGGET_LEAF,
		null,
		MetaIcons.INGOT_LEAF,
		MetaIcons.INGOT_HOT,
		MetaIcons.INGOT_DOUBLE_LEAF,
		MetaIcons.INGOT_TRIPLE_LEAF,
		MetaIcons.INGOT_QUADRUPLE_LEAF,
		MetaIcons.INGOT_QUINTUPLE_LEAF,
		MetaIcons.PLATE_LEAF,
		MetaIcons.PLATE_DOUBLE_LEAF,
		MetaIcons.PLATE_TRIPLE_LEAF,
		MetaIcons.PLATE_QUADRUPLE_LEAF,
		MetaIcons.PLATE_QUINTUPLE_LEAF,
		MetaIcons.PLATE_DENSE_LEAF,
		MetaIcons.ROD_DULL,
		MetaIcons.LENS,
		MetaIcons.ROUND,
		MetaIcons.BOLT,
		MetaIcons.SCREW,
		MetaIcons.RING,
		null,
		MetaIcons.CELL,
		MetaIcons.CELL_PLASMA
	};
	
	public static final IIconContainer[] SET_EMPTY = new IIconContainer[] {
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		MetaIcons.CELL_EMPTY,
		MetaIcons.CELL_EMPTY
	};

	public static final IIconContainer[] SET_NONE = SET_DULL;
	public static final IIconContainer[] SET_FLUID = SET_DULL;
	public static final IIconContainer[] SET_ROOT = SET_LEAF;
	public static final IIconContainer[] SET_LAPIS = SET_DULL;
	public static final IIconContainer[] SET_QUARTZ = SET_DULL;
}