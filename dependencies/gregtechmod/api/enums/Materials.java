package gregtechmod.api.enums;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.interfaces.IIconContainer;
import gregtechmod.api.util.GT_Config;
import gregtechmod.api.util.GT_ModHandler;
import gregtechmod.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * This List contains every Material I know about, and is used to determine Recipes for the 
 */
public enum Materials {
	_NULL				(  -1, GT_ItemTextures.SET_EMPTY			, 0                                     , 255, 255, 255,   0,	"NULL"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			, Element._NULL		),
	
	/**
	 * Direct Elements
	 */
	Aluminium			(  19, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Aluminium"						,    0,       0,          0,          0,       1700, 1700,  true, false,   3,   1,   1, Dyes.dyeLightBlue	, Element.Al		),
	Antimony			(  58, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Antimony"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeLightGray	, Element.Sb		),
	Arsenic				(  39, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Arsenic"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeOrange		, Element.As		),
	Barium				(  63, GT_ItemTextures.SET_METALLIC			, 1    |8|16                            , 255, 255, 255,   0,	"Barium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			, Element.Ba		),
	Beryllium			(   8, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Beryllium"						,    0,       0,          0,          0,          0,    0, false, false,   6,   1,   1, Dyes.dyeGreen		, Element.Be		),
	Calcium				(  26, GT_ItemTextures.SET_METALLIC			, 1      |16                            , 255, 255, 255,   0,	"Calcium"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyePink		, Element.Ca		),
	Carbon				(  10, GT_ItemTextures.SET_DULL				, 1      |16                            , 255, 255, 255,   0,	"Carbon"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeBlack		, Element.C			),
	Cadmium				(  55, GT_ItemTextures.SET_SHINY			, 1    |8|16                            , 255, 255, 255,   0,	"Cadmium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGray		, Element.Cd		),
	Cerium				(  65, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Cerium"						,    0,       0,          0,          0,       1068, 1068, true , false,   4,   1,   1, Dyes._NULL			, Element.Ce		),
	Chlorine			(  23, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Chlorine"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeCyan		, Element.Cl		),
	Chrome				(  30, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Chrome"						,    0,       0,          0,          0,       1700, 1700,  true, false,   5,   1,   1, Dyes.dyePink		, Element.Cr		),
	Cobalt				(  33, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Cobalt"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		, Element.Co		),
	Copper				(  35, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Copper"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeOrange		, Element.Cu		),
	Deuterium			(   2, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Deuterium"						,    0,       0,          0,          0,          0,    0, false,  true,  10,   1,   1, Dyes.dyeYellow		, Element.D			),
	Dysprosium			(  73, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Dysprosium"					,    0,       0,          0,          0,       1680, 1680, true , false,   4,   1,   1, Dyes._NULL			, Element.Dy		),
	Empty				(   0, GT_ItemTextures.SET_EMPTY			,         16                            , 255, 255, 255,   0,	"Empty"							,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes._NULL			, Element._NULL		),
	Erbium				(  75, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Erbium"						,    0,       0,          0,          0,       1802, 1802, true , false,   4,   1,   1, Dyes._NULL			, Element.Er		),
	Europium			(  70, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Europium"						,    0,       0,          0,          0,       1099, 1099, true , false,   4,   1,   1, Dyes._NULL			, Element.Eu		),
	Fluorine			(  14, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Fluorine"						,    0,       0,          0,          0,          0,    0, false,  true,   2,   1,   1, Dyes.dyeGreen		, Element.F			),
	Gadolinium			(  71, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Gadolinium"					,    0,       0,          0,          0,       1585, 1585, true , false,   4,   1,   1, Dyes._NULL			, Element.Gd		),
	Gold				(  86, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Gold"							,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeYellow		, Element.Au		),
	Holmium				(  74, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Holmium"						,    0,       0,          0,          0,       1734, 1734, true , false,   4,   1,   1, Dyes._NULL			, Element.Ho		),
	Hydrogen			(   1, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Hydrogen"						,    1,      15,          0,          0,          0,    0, false,  true,   2,   1,   1, Dyes.dyeBlue		, Element.H			),
	Helium				(   4, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Helium"						,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeYellow		, Element.He		),
	Helium_3			(   5, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Helium-3"						,    0,       0,          0,          0,          0,    0, false,  true,  10,   1,   1, Dyes.dyeYellow		, Element.He_3		),
	Indium				(  56, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Indium"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeGray		, Element.In		),
	Iridium				(  84, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Iridium"						,    0,       0,          0,          0,          0,    0, false, false,  10,   1,   1, Dyes.dyeWhite		, Element.Ir		),
	Iron				(  32, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Iron"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	, Element.Fe		),
	Lanthanum			(  64, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Lanthanum"						,    0,       0,          0,          0,       1193, 1193, true , false,   4,   1,   1, Dyes._NULL			, Element.La		),
	Lead				(  89, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Lead"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePurple		, Element.Pb		),
	Lithium				(   6, GT_ItemTextures.SET_DULL				, 1    |8|16                            , 255, 255, 255,   0,	"Lithium"						,    3,      60,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeLightBlue	, Element.Li		),
	Lutetium			(  78, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Lutetium"						,    0,       0,          0,          0,       1925, 1925, true , false,   4,   1,   1, Dyes._NULL			, Element.Lu		),
	Magic				(-128, GT_ItemTextures.SET_SHINY			, 1|2|4|8|16                            , 255, 255, 255,   0,	"Magic"							,    5,      32,          0,          0,          0,    0, false, false,   7,   1,   1, Dyes.dyePurple		, Element.Ma		),
	Magnesium			(  18, GT_ItemTextures.SET_METALLIC			, 1    |8|16                            , 255, 255, 255,   0,	"Magnesium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePink		, Element.Mg		),
	Manganese			(  31, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Manganese"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		, Element.Mn		),
	Mercury				(  87, GT_ItemTextures.SET_SHINY			,         16                            , 255, 255, 255,   0,	"Mercury"						,    5,       8,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	, Element.Hg		),
	Molybdenum			(  48, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Molybdenum"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			, Element.Mo		),
	Neodymium			(  67, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Neodymium"						,    0,       0,          0,          0,       1297, 1297, true , false,   4,   1,   1, Dyes._NULL			, Element.Nd		),
	Neutronium			( 129, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Neutronium"					,    0,       0,          0,          0,          0,    0, false, false,  20,   1,   1, Dyes.dyeWhite		, Element.Nt		),
	Nickel				(  34, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Nickel"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeLightBlue	, Element.Ni		),
	Nitrogen			(  12, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Nitrogen"						,    0,       0,          0,          0,          0,    0, false,  true,   2,   1,   1, Dyes.dyeCyan		, Element.N			),
	Osmium				(  83, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Osmium"						,    0,       0,          0,          0,          0,    0, false, false,  10,   1,   1, Dyes.dyeBlue		, Element.Os		),
	Oxygen				(  13, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Oxygen"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeWhite		, Element.O			),
	Palladium			(  52, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Palladium"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeGray		, Element.Pd		),
	Phosphor			(  21, GT_ItemTextures.SET_DULL				, 1    |8|16                            , 255, 255, 255,   0,	"Phosphor"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeYellow		, Element.P			),
	Platinum			(  85, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Platinum"						,    0,       0,          0,          0,          0,    0, false, false,   6,   1,   1, Dyes.dyeOrange		, Element.Pt		),
	Plutonium			( 100, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Plutonium"						,    0,       0,    2000000,          0,          0,    0, false, false,   6,   1,   1, Dyes.dyeLime		, Element.Pu		),
	Potassium			(  25, GT_ItemTextures.SET_METALLIC			,         16                            , 255, 255, 255,   0,	"Potassium"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeWhite		, Element.K			),
	Praseodymium		(  66, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Praseodymium"					,    0,       0,          0,          0,       1208, 1208, true , false,   4,   1,   1, Dyes._NULL			, Element.Pr		),
	Promethium			(  68, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Promethium"					,    0,       0,          0,          0,       1315, 1315, true , false,   4,   1,   1, Dyes._NULL			, Element.Pm		),
	Samarium			(  69, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Samarium"						,    0,       0,          0,          0,       1345, 1345, true , false,   4,   1,   1, Dyes._NULL			, Element.Sm		),
	Scandium			(  27, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Scandium"						,    0,       0,          0,          0,       1814, 1814, true , false,   2,   1,   1, Dyes.dyeYellow		, Element.Sc		),
	Silicon				(  20, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Silicon"						,    0,       0,          0,          0,       1500, 1500, true , false,   1,   1,   1, Dyes.dyeBlack		, Element.Si		),
	Silver				(  54, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Silver"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	, Element.Ag		),
	Sodium				(  17, GT_ItemTextures.SET_METALLIC			,         16                            , 255, 255, 255,   0,	"Sodium"						,    3,      30,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlue		, Element.Na		),
	Sulfur				(  22, GT_ItemTextures.SET_DULL				, 1    |8|16                            , 255, 255, 255,   0,	"Sulfur"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeYellow		, Element.S			),
	Tellurium			(  59, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Tellurium"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeGray		, Element.Te		),
	Terbium				(  72, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Terbium"						,    0,       0,          0,          0,       1629, 1629, true , false,   4,   1,   1, Dyes._NULL			, Element.Tb		),
	Thorium				(  96, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Thorium"						,    0,       0,     500000,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeBlack		, Element.Th		),
	Thulium				(  76, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Thulium"						,    0,       0,          0,          0,       1818, 1818, true , false,   4,   1,   1, Dyes._NULL			, Element.Tm		),
	Tin					(  57, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Tin"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		, Element.Sn		),
	Titanium			(  28, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Titanium"						,    0,       0,          0,          0,       1500, 1500, true , false,   5,   1,   1, Dyes.dyePurple		, Element.Ti		),
	Tritium				(   3, GT_ItemTextures.SET_METALLIC			,         16                            , 255, 255, 255,   0,	"Tritium"						,    0,       0,          0,          0,          0,    0, false,  true,  10,   1,   1, Dyes.dyeRed			, Element.T			),
	Tungsten			(  81, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Tungsten"						,    0,       0,          0,          0,       2500, 2500, true , false,   4,   1,   1, Dyes.dyeBlack		, Element.W			),
	Uranium				(  98, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Uranium"						,    0,       0,    1000000,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeGreen		, Element.U			),
	Ytterbium			(  77, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Ytterbium"						,    0,       0,          0,          0,       1097, 1097, true , false,   4,   1,   1, Dyes._NULL			, Element.Yb		),
	Yttrium				(  45, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Yttrium"						,    0,       0,          0,          0,       1799, 1799, true , false,   4,   1,   1, Dyes._NULL			, Element.Y			),
	Zinc				(  36, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Zinc"							,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeWhite		, Element.Zn		),
	
	/**
	 * The "Random Material" ones.
	 */
	Organic				(  -1, GT_ItemTextures.SET_LEAF				, false),
	Crystal				(  -1, GT_ItemTextures.SET_SHINY			, false),
	Quartz				(  -1, GT_ItemTextures.SET_QUARTZ			, false),
	Metal				(  -1, GT_ItemTextures.SET_METALLIC			, false),
	Stone				(  -1, GT_ItemTextures.SET_DULL				, false),
	Cobblestone			(  -1, GT_ItemTextures.SET_DULL				, false),
	
	/**
	 * Unknown Material Components. Dead End Section.
	 */
	BasalticMineralSand	(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Basaltic Mineral Sand"			,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	CassiteriteSand		(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Cassiterite Sand"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	GarnetSand			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Garnet Sand"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	GraniticMineralSand	(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Granitic Mineral Sand"			,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	QuartzSand			(  -1, GT_ItemTextures.SET_QUARTZ			, 1    |8                               , 255, 255, 255,   0,	"Quartz Sand"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	VolcanicAsh			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Volcanic Ashes"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Borax				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Borax"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Molybdenite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Molybdenite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Pyrolusite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Pyrolusite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	RockSalt			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Rock Salt"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Stibnite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Stibnite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Asbestos			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Asbestos"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Chrysotile			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Chrysotile"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Diatomite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Diatomite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Glauconite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Glauconite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Gypsum				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Gypsum"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Mirabilite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Mirabilite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Mica				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Mica"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Talc				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Talc"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Trona				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Trona"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Barite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Barite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Bastnasite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Bastnasite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Garnierite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Garnierite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Lepidolite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Lepidolite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Magnesite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Magnesite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Pentlandite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Pentlandite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Scheelite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Scheelite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Alunite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Alunite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Strontium			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Strontium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Celestine			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Celestine"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Dolomite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Dolomite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Wollastonite		(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Wollastonite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Zeolite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Zeolite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	BandedIron			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Banded Iron"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Ilmenite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Ilmenite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Pollucite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Pollucite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Spodumene			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Spodumene"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Tantalite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Tantalite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Uraninite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Uraninite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	VanadiumMagnetite	(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Vanadium Magnetite"			,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Kyanite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Kyanite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Perlite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Perlite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Pumice				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Pumice"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Bentonite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Bentonite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	FullersEarth		(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"FullersEarth"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Kaolinite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Kaolinite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	BrownLimonite		(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Brown Limonite"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	YellowLimonite		(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Yellow Limonite"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Vermiculite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Vermiculite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Oilsands			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Oilsands"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Petroleum			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Petroleum"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	
	// #TooLazyToSort
	
	Adamant				( 319, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Adamantium"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Adamite				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Adamite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Adluorite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Adluorite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Agate				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Agate"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Alduorite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Alduorite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Amber				( 514, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Amber"							,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeOrange		),
	Ammonium			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Ammonium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Amordrine			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Amordrine"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Andesite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Andesite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Angmallen			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Angmallen"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Ardite				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Ardite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Aredrite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Aredrite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Atlarus				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Atlarus"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Bitumen				(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Bitumen"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Black				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Black"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlack		),
	Blizz				( 851, GT_ItemTextures.SET_SHINY			, 1                                     , 255, 255, 255,   0,	"Blizz"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Blueschist			( 852, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Blueschist"					,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeLightBlue	),
	Bluestone			( 813, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Bluestone"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlue		),
	BlueTopaz			( 513, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"BlueTopaz"						,    0,       0,          0,          0,          0,    0, false, true ,   3,   1,   1, Dyes.dyeBlue		),
	Bloodstone			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Bloodstone"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			),
	Blutonium			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Blutonium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		),
	Carmot				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Carmot"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Celenegil			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Celenegil"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	CertusQuartz		( 516, GT_ItemTextures.SET_QUARTZ			, 1  |4|8                               , 255, 255, 255,   0,	"Certus Quartz"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	),
	Ceruclase			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Ceruclase"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Citrine				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Citrine"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	CobaltHexahydrate	( 853, GT_ItemTextures.SET_METALLIC			, 1      |16                            , 255, 255, 255,   0,	"Cobalt Hexahydrate"			,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlue		),
	ConstructionFoam	( 854, GT_ItemTextures.SET_DULL				, 1      |16                            , 255, 255, 255,   0,	"Construction Foam"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Chalcopyrite		( 855, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Chalcopyrite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Chalk				( 856, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Chalk"							,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeWhite		),
	Chert				( 857, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Chert"							,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes._NULL			),
	Chimerite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Chimerite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Coral				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Coral"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	CrudeOil			( 858, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Crude Oil"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		),
	Chrysocolla			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Chrysocolla"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	CrystalFlux			( 517, GT_ItemTextures.SET_SHINY			, 1  |4                                 , 255, 255, 255,   0,	"Flux Crystal"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Cyanite				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Cyanite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeCyan		),
	Dacite				( 859, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Dacite"						,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeLightGray	),
	DarkIron			( 342, GT_ItemTextures.SET_DULL				, 1|2  |8                               , 255, 255, 255,   0,	"Dark Iron"						,    0,       0,          0,          0,          0,    0, false, false,   5,   1,   1, Dyes.dyePurple		),
	DarkStone			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Dark Stone"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlack		),
	Demonite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Demonite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			),
	Desh				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Desh"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Desichalkos			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Desichalkos"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Dilithium			( 515, GT_ItemTextures.SET_DULL				, 1  |4|8|16                            , 255, 255, 255,   0,	"Dilithium"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeWhite		),
	Draconic			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Draconic"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			),
	Duranium			( 328, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Duranium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Eclogite			( 860, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Eclogite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	ElectrumFlux		( 320, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Fluxed Electrum"				,    0,       0,          0,          0,       3000, 3000, true , false,   1,   1,   1, Dyes.dyeYellow		),
	Emery				( 861, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Emery"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Enderium			( 321, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Enderium"						,    0,       0,          0,          0,       3000, 3000, true , false,   1,   1,   1, Dyes.dyeGreen		),
	Energized			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Energized"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Epidote				( 862, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Epidote"						,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes._NULL			),
	Eximite				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Eximite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	FieryBlood			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Fiery Blood"					,    5,     100,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			),
	Firestone			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Firestone"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeOrange		),
	Fluorite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Fluorite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	FoolsRuby			( 512, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Ruby"							,    0,       0,          0,          0,          0,    0, false,  true,   3,   1,   1, Dyes.dyeRed			),
	Force				( 521, GT_ItemTextures.SET_SHINY			, 1|2|4|8                               , 255, 255, 255,   0,	"Force"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	Forcicium			( 518, GT_ItemTextures.SET_SHINY			, 1  |4|8|16                            , 255, 255, 255,   0,	"Forcicium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	Forcillium			( 519, GT_ItemTextures.SET_SHINY			, 1  |4|8|16                            , 255, 255, 255,   0,	"Forcillium"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	Gabbro				( 863, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Gabbro"						,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes._NULL			),
	Glowstone			( 811, GT_ItemTextures.SET_SHINY			, 1      |16                            , 255, 255, 255,   0,	"Glowstone"						,    0,       0,      25000,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Gneiss				( 864, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Gneiss"						,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes._NULL			),
	Graphite			( 865, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Graphite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGray		),
	Greenschist			( 866, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Green Schist"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGreen		),
	Greenstone			( 867, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Greenstone"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGreen		),
	Greywacke			( 868, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Greywacke"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Haderoth			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Haderoth"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Hematite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Hematite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Hepatizon			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Hepatizon"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	HSLA				( 322, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"HSLA Steel"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Ignatius			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Ignatius"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Infernal			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infernal"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Infuscolium			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Infuscolium"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	InfusedGold			( 323, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Infused Gold"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	InfusedAir			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Air"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	InfusedFire			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Fire"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			),
	InfusedEarth		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Earth"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	InfusedWater		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Water"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		),
	InfusedVis			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Vis"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePurple		),
	InfusedDull			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Dull"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	),
	InfusedEntropy		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Entropy"				,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	InfusedOrder		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infused Order"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Inolashite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Inolashite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Invisium			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Invisium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Jade				( 537, GT_ItemTextures.SET_SHINY			, 1    |8                               , 255, 255, 255,   0,	"Jade"							,    0,       0,          0,          0,          0,    0, false, false,   5,   1,   1, Dyes.dyeGreen		),
	Jasper				( 511, GT_ItemTextures.SET_DULL				, 1  |4|8                               , 255, 255, 255,   0,	"Jasper"						,    0,       0,          0,          0,          0,    0, false,  true,   3,   1,   1, Dyes.dyeRed			),
	Kalendrite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Kalendrite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Komatiite			( 869, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Komatiite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Lava				( 700, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Lava"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		),
	Lemurite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Lemurite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Limestone			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Limestone"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Lodestone			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Lodestone"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Luminite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Luminite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		),
	Magnetite			( 870, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Magnetite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Magma				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Magma"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		),
	Malachite			( 871, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Malachite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Mawsitsit			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Mawsitsit"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Mercassium			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Mercassium"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	MeteoricIron		( 340, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Meteoric Iron"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	MeteoricSteel		( 341, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Meteoric Steel"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Meteorite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Meteorite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		),
	Meutoite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Meutoite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Migmatite			( 872, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Migmatite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Monazite			( 520, GT_ItemTextures.SET_METALLIC			, 1  |4|8                               , 255, 255, 255,   0,	"Monazite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	Moonstone			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Moonstone"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		),
	Naquadah			( 324, GT_ItemTextures.SET_METALLIC			, 1|2  |8|16                            , 255, 255, 255,   0,	"Naquadah"						,    0,       0,          0,          0,          0,    0, false, false,  10,   1,   1, Dyes.dyeBlack		),
	NaquadahAlloy		( 325, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Naquadah Alloy"				,    0,       0,          0,          0,          0,    0, false, false,  10,   1,   1, Dyes.dyeBlack		),
	Nether				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Nether"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	NetherBrick			( 814, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Nether Brick"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			),
	NetherQuartz		( 522, GT_ItemTextures.SET_QUARTZ			, 1  |4|8                               , 255, 255, 255,   0,	"Nether Quartz"					,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeWhite		),
	NetherStar			( 506, GT_ItemTextures.SET_SHINY			, 1  |4                                 , 255, 255, 255,   0,	"Nether Star"					,    5,   50000,          0,          0,          0,    0, false, false,  15,   1,   1, Dyes.dyeWhite		),
	Nikolite			( 812, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Nikolite"						,    0,       0,       5000,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeCyan		),
	Onyx				(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Onyx"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Opal				( 510, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Opal"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Orichalcum			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Orichalcum"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Osmonium			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Osmonium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		),
	Oureclase			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Oureclase"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Painite				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Painite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Peanutwood			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Peanut Wood"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Pewter				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Pewter"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Pitchblende			( 873, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Pitchblende"					,    0,       0,          0,          0,          0,    0, false, false,   5,   1,   1, Dyes.dyeYellow		),
	Phoenixite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Phoenixite"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Potash				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Potash"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Plastic				( 874, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Plastic"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		),
	Prometheum			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Prometheum"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Quartzite			( 523, GT_ItemTextures.SET_QUARTZ			, 1  |4|8                               , 255, 255, 255,   0,	"Quartzite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		),
	Quicklime			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Quicklime"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Randomite			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Randomite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	RefinedGlowstone	( 326, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Refined Glowstone"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	RefinedObsidian		( 327, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Refined Obsidian"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		),
	Rhyolite			( 875, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Rhyolite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Rubracium			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Rubracium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	RyuDragonRyder		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Ryu Dragon Ryder"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Sand				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Sand"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Sanguinite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Sanguinite"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Siltstone			( 876, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Siltstone"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Soapstone			( 877, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Soapstone"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Spinel				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Spinel"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Starconium			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Starconium"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Sugilite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Sugilite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Sunstone			(  -1, GT_ItemTextures.SET_NONE				, 1    |8                               , 255, 255, 255,   0,	"Sunstone"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	Tar					(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Tar"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		),
	Tartarite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Tartarite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Tapazite			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Tapazite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		),
	Thyrium				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Thyrium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Tourmaline			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Tourmaline"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	Tritanium			( 329, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Tritanium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		),
	Turquoise			(  -1, GT_ItemTextures.SET_NONE				, 1                                     , 255, 255, 255,   0,	"Turquoise"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes._NULL			),
	UUMatter			( 703, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"UUMatter"						,    0,       0,          0,          0,          0,    0, false, false,  10,   1,   1, Dyes.dyePink		),
	Void				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Void"							,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes._NULL			),
	Voidstone			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Voidstone"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes._NULL			),
	Vulcanite			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Vulcanite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Vyroxeres			(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Vyroxeres"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes._NULL			),
	Yellorite			(  -1, GT_ItemTextures.SET_NONE				,       8                               , 255, 255, 255,   0,	"Yellorite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	Yellorium			(  -1, GT_ItemTextures.SET_NONE				, 1|2                                   , 255, 255, 255,   0,	"Yellorium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		),
	Zectium				(  -1, GT_ItemTextures.SET_NONE				, 1|2  |8                               , 255, 255, 255,   0,	"Zectium"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlack		),
	
	/**
	 * Not possible to determine exact Components
	 */
	Advanced			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Advanced"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Basic				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Basic"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Antimatter			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Antimatter"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		),
	BioFuel				( 705, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Biofuel"						,    0,       6,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		),
	Biomass				( 704, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Biomass"						,    3,       8,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGreen		),
	Cluster				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Cluster"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeWhite		),
	CoalFuel			( 710, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Coalfuel"						,    0,      16,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		),
	Creosote			( 712, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Creosote"						,    3,       3,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		),
	Data				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Data"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Elite				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Elite"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Ethanol				( 706, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Ethanol"						,    0,     128,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		),
	Fuel				( 708, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Diesel"						,    0,     128,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Good				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Good"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Gunpowder			( 800, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Gunpowder"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Infinite			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Infinite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	LimePure			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Pure Lime"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLime		),
	Master				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Master"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Meat				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Meat"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		),
	MeatRaw				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Raw Meat"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		),
	MeatCooked			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Cooked Meat"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		),
	Milk				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Milk"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		),
	Mud					(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Mud"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		),
	Oil					( 707, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Oil"							,    3,      16,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		),
	Paper				( 879, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Paper"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		),
	Peat				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Peat"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		),
	Primitive			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Primitive"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Red					(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Red"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			),
	Reinforced			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Reinforced"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		),
	Rubber				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Rubber"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		),
	SeedOil				( 713, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Seed Oil"						,    3,       2,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLime		),
	TNT					(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"TNT"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			),
	Ultimate			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Ultimate"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	),
	Unstable			(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Unstable"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeWhite		),
	Unstableingot		(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Unstable"						,    0,       0,          0,          0,          0,    0, false,  true,   1,   1,   1, Dyes.dyeWhite		),
	Wheat				(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Wheat"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Wood				( 809, GT_ItemTextures.SET_ROOT				, 1                                     , 255, 255, 255,   0,	"Wood"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		),
	
	/**
	 * TODO: This
	 */
	AluminiumBrass		(  -1, GT_ItemTextures.SET_NONE				, 1|2                                   , 255, 255, 255,   0,	"Aluminium Brass"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Osmiridium			( 317, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Osmiridium"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue	),
	Sunnarium			( 318, GT_ItemTextures.SET_SHINY			, 1|2                                   , 255, 255, 255,   0,	"Sunnarium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		),
	Endstone			( 808, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Endstone"						,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeYellow		),
	Netherrack			( 807, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Netherrack"					,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeRed			),
	
	/**
	 * First Degree Compounds
	 */
	Adamantine			( 878, GT_ItemTextures.SET_SHINY			, 1    |8                               , 255, 255, 255,   0,	"Adamantine"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	, 2, Arrays.asList(new MaterialStack(Adamant, 1))),
	Almandine			( 820, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Almandine"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Iron, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Amethyst			( 509, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Amethyst"						,    0,       0,          0,          0,          0,    0, false,  true,   3,   1,   1, Dyes.dyePink		, 1, Arrays.asList(new MaterialStack(Silicon, 4), new MaterialStack(Oxygen, 8), new MaterialStack(Iron, 1))),
	Andradite			( 821, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Andradite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		, 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Iron, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Ash					( 815, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Ashes"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	, 2, Arrays.asList(new MaterialStack(Carbon, 1))),
	BatteryAlloy		( 315, GT_ItemTextures.SET_DULL				, 1|2                                   , 255, 255, 255,   0,	"Battery Alloy"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		, 2, Arrays.asList(new MaterialStack(Lead, 4), new MaterialStack(Antimony, 1))),
	Bauxite				( 822, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Bauxite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBrown		, 1, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Aluminium, 16), new MaterialStack(Hydrogen, 10), new MaterialStack(Oxygen, 12))),
	Bone				( 806, GT_ItemTextures.SET_DULL				, 0                                     , 255, 255, 255,   0,	"Bone"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		, 0, Arrays.asList(new MaterialStack(Calcium, 1))),
	Brass				( 301, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Brass"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Copper, 3))),
	Bronze				( 300, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Bronze"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 2, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Copper, 3))),
	Calcite				( 823, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Calcite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3))),
	Cassiterite			( 824, GT_ItemTextures.SET_METALLIC			,       8                               , 255, 255, 255,   0,	"Cassiterite"					,    0,       0,          0,          0,          0,    0, false, false,   4,   3,   1, Dyes.dyeWhite		, 0, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Oxygen, 2))),
	Charcoal			( 536, GT_ItemTextures.SET_DULL				, 1  |4                                 , 255, 255, 255,   0,	"Charcoal"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		, 1, Arrays.asList(new MaterialStack(Carbon, 1))),
	Chromite			( 825, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Chromite"						,    0,       0,          0,          0,       1700, 1700,  true, false,   6,   1,   1, Dyes.dyePink		, 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Chrome, 2), new MaterialStack(Oxygen, 4))),
	Cinnabar			( 826, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Cinnabar"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBrown		, 2, Arrays.asList(new MaterialStack(Mercury, 1), new MaterialStack(Sulfur, 1))),
	Clay				( 805, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Clay"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightBlue	, 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Lithium, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 2))),
	Coal				( 535, GT_ItemTextures.SET_DULL				, 1  |4                                 , 255, 255, 255,   0,	"Coal"							,    0,       0,          0,          0,          0,    0, false, false,   2,   2,   1, Dyes.dyeBlack		, 1, Arrays.asList(new MaterialStack(Carbon, 1))),
	Cobaltite			( 827, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Cobaltite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		, 1, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Arsenic, 1), new MaterialStack(Sulfur, 1))),
	Cooperite			( 828, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Cooperite"						,    0,       0,          0,          0,          0,    0, false, false,   5,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Platinum, 3), new MaterialStack(Nickel, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Palladium, 1))),
	Cupronickel			( 310, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Cupronickel"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Nickel, 1))),
	DarkAsh				( 816, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Dark Ashes"					,    0,       0,          0,          0,          0,    0, false, false,   1,   2,   1, Dyes.dyeGray		, 1, Arrays.asList(new MaterialStack(Carbon, 1))),
	DeepIron			( 829, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Deep Iron"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePink		, 2, Arrays.asList(new MaterialStack(Iron, 1))),
	Diamond				( 500, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Diamond"						,    0,       0,          0,          0,          0,    0, false,  true,   5, 128,   1, Dyes.dyeWhite		, 1, Arrays.asList(new MaterialStack(Carbon, 1))),
	Electrum			( 303, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Electrum"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Gold, 1))),
	Emerald				( 501, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Emerald"						,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeGreen		, 1, Arrays.asList(new MaterialStack(Beryllium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 6), new MaterialStack(Oxygen, 18))),
	Galena				( 830, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Galena"						,    0,       0,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyePurple		, 1, Arrays.asList(new MaterialStack(Lead, 3), new MaterialStack(Silver, 3), new MaterialStack(Sulfur, 2))),
	Glyceryl			( 714, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Glyceryl"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeCyan		, 1, Arrays.asList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Nitrogen, 3), new MaterialStack(Oxygen, 9))),
	GreenSapphire		( 504, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Green Sapphire"				,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeCyan		, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3))),
	Grossular			( 831, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Grossular"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Invar				( 302, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Invar"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBrown		, 2, Arrays.asList(new MaterialStack(Iron, 2), new MaterialStack(Nickel, 1))),
	Kanthal				( 312, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Kanthal"						,    0,       0,          0,          0,       2500, 2500,  true, false,   1,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Chrome, 1))),
	Lazurite			( 524, GT_ItemTextures.SET_LAPIS			, 1  |4|8                               , 255, 255, 255,   0,	"Lazurite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		, 1, Arrays.asList(new MaterialStack(Aluminium, 6), new MaterialStack(Silicon, 6), new MaterialStack(Calcium, 8), new MaterialStack(Sodium, 8))),
	Liveroot			( 832, GT_ItemTextures.SET_ROOT				, 1                                     , 255, 255, 255,   0,	"Liveroot"						,    0,       0,          0,          0,          0,    0, false, false,   2,   4,   3, Dyes.dyeBrown		, 2, Arrays.asList(new MaterialStack(Wood, 3), new MaterialStack(Magic, 1))),
	Magnalium			( 313, GT_ItemTextures.SET_DULL				, 1|2                                   , 255, 255, 255,   0,	"Magnalium"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue	, 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Aluminium, 2))),
	Methane				( 715, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Methane"						,    1,      45,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeMagenta		, 1, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 4))),
	Nichrome			( 311, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Nichrome"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Nickel, 4), new MaterialStack(Chrome, 1))),
	NitroCarbon			( 716, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Nitro-Carbon"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeCyan		, 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Carbon, 1))),
	NitrogenDioxide		( 717, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Nitrogen Dioxide"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeCyan		, 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 2))),
	Obsidian			( 804, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Obsidian"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		, 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Iron, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 8))),
	Olivine				( 505, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Olivine"						,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeLime		, 1, Arrays.asList(new MaterialStack(Magnesium, 2), new MaterialStack(Iron, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 4))),
	Phosphate			( 833, GT_ItemTextures.SET_DULL				, 1    |8|16                            , 255, 255, 255,   0,	"Phosphate"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeYellow		, 1, Arrays.asList(new MaterialStack(Phosphor, 1), new MaterialStack(Oxygen, 4))),
	PigIron				( 307, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Pig Iron"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePink		, 2, Arrays.asList(new MaterialStack(Iron, 1))),
	Pyrite				( 834, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Pyrite"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Sulfur, 2))),
	Pyrope				( 835, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Pyrope"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyePurple		, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Ruby				( 502, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Ruby"							,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeRed			, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3), new MaterialStack(Chrome, 1))),
	Salt				( 817, GT_ItemTextures.SET_METALLIC			, 1                                     , 255, 255, 255,   0,	"Salt"							,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeWhite		, 1, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Chlorine, 1))),
	Saltpeter			( 836, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Saltpeter"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeWhite		, 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3))),
	Sapphire			( 503, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Sapphire"						,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeBlue		, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3))),
	SiliconDioxide		( 837, GT_ItemTextures.SET_QUARTZ			, 1      |16                            , 255, 255, 255,   0,	"Silicon Dioxide"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLightGray	, 1, Arrays.asList(new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 2))),
	Sodalite			( 525, GT_ItemTextures.SET_LAPIS			, 1  |4|8                               , 255, 255, 255,   0,	"Sodalite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		, 1, Arrays.asList(new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Sodium, 4), new MaterialStack(Chlorine, 1))),
	SodiumPersulfate	( 718, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Sodium Persulfate"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4))),
	SodiumSulfide		( 719, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Sodium Sulfide"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Sulfur, 1))),
	SolderingAlloy		( 314, GT_ItemTextures.SET_DULL				, 1|2                                   , 255, 255, 255,   0,	"Soldering Alloy"				,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		, 2, Arrays.asList(new MaterialStack(Tin, 9), new MaterialStack(Antimony, 1))),
	Spessartine			( 838, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Spessartine"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Manganese, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Sphalerite			( 839, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Sphalerite"					,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeYellow		, 1, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Sulfur, 1))),
	StainlessSteel		( 306, GT_ItemTextures.SET_SHINY			, 1|2                                   , 255, 255, 255,   0,	"Stainless Steel"				,    0,       0,          0,          0,       2000, 2000,  true, false,   1,   1,   1, Dyes.dyeWhite		, 1, Arrays.asList(new MaterialStack(Iron, 6), new MaterialStack(Chrome, 1), new MaterialStack(Manganese, 1), new MaterialStack(Nickel, 1))),
	Steel				( 305, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Steel"							,    0,       0,          0,          0,       1000, 1000,  true, false,   4,  51,  50, Dyes.dyeGray		, 1, Arrays.asList(new MaterialStack(Iron, 50), new MaterialStack(Carbon, 1))),
	SulfuricAcid		( 720, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Sulfuric Acid"					,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4))),
	Tanzanite			( 508, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Tanzanite"						,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyePurple		, 1, Arrays.asList(new MaterialStack(Calcium, 2), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 13))),
	Tetrahedrite		( 840, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Tetrahedrite"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Copper, 3), new MaterialStack(Antimony, 1), new MaterialStack(Sulfur, 3), new MaterialStack(Iron, 1))), //Cu3SbS3 + x(Fe,Zn)6Sb2S9
	Topaz				( 507, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Topaz"							,    0,       0,          0,          0,          0,    0, false,  true,   5,   1,   1, Dyes.dyeOrange		, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 6))),
	Tungstate			( 841, GT_ItemTextures.SET_DULL				, 1    |8                               , 255, 255, 255,   0,	"Tungstate"						,    0,       0,          0,          0,       2500, 2500,  true, false,   4,   1,   1, Dyes.dyeBlack		, 1, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Lithium, 2), new MaterialStack(Oxygen, 4))),
	Uvarovite			( 842, GT_ItemTextures.SET_SHINY			, 1    |8                               , 255, 255, 255,   0,	"Uvarovite"						,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeGreen		, 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Chrome, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12))),
	Water				( 701, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Water"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlue		, 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1))),
	Ice					( 702, GT_ItemTextures.SET_SHINY			, 1|      16                            , 255, 255, 255,   0,	"Ice"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlue		, 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1))),
	WroughtIron			( 304, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Wrought Iron"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeLightGray	, 2, Arrays.asList(new MaterialStack(Iron, 1))),
	
	/**
	 * Second Degree Compounds
	 */
	Redstone			( 810, GT_ItemTextures.SET_METALLIC			, 1    |8                               , 255, 255, 255,   0,	"Redstone"						,    0,       0,       5000,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Silicon, 1), new MaterialStack(Pyrite, 5), new MaterialStack(Ruby, 1), new MaterialStack(Mercury, 3))),
	Lapis				( 526, GT_ItemTextures.SET_LAPIS			, 1  |4|8                               , 255, 255, 255,   0,	"Lapis"							,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeBlue		, 2, Arrays.asList(new MaterialStack(Lazurite, 12), new MaterialStack(Sodalite, 2), new MaterialStack(Pyrite, 1), new MaterialStack(Calcite, 1))),
	Blaze				( 801, GT_ItemTextures.SET_SHINY			, 1                                     , 255, 255, 255,   0,	"Blaze"							,    0,       0,          0,          0,          0,    0, false, false,   2,   3,   2, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(DarkAsh, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Magic, 1))),
	EnderPearl			( 532, GT_ItemTextures.SET_SHINY			, 1  |4  |16                            , 255, 255, 255,   0,	"Enderpearl"					,    0,       0,      25000,          0,          0,    0, false, false,   1,  16,  10, Dyes.dyeGreen		, 1, Arrays.asList(new MaterialStack(Beryllium, 1), new MaterialStack(Potassium, 4), new MaterialStack(Nitrogen, 5), new MaterialStack(Magic, 6))),
	EnderEye			( 533, GT_ItemTextures.SET_SHINY			, 1  |4                                 , 255, 255, 255,   0,	"Endereye"						,    5,      10,      50000,          0,          0,    0, false, false,   1,   2,   1, Dyes.dyeGreen		, 2, Arrays.asList(new MaterialStack(EnderPearl, 1), new MaterialStack(Blaze, 1))),
	Flint				( 802, GT_ItemTextures.SET_METALLIC			, 1                                     , 255, 255, 255,   0,	"Flint"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		, 2, Arrays.asList(new MaterialStack(SiliconDioxide, 1))),
	Niter				( 531, GT_ItemTextures.SET_DULL				, 1  |4|8                               , 255, 255, 255,   0,	"Niter"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		, 2, Arrays.asList(new MaterialStack(Saltpeter, 1))),
	Pyrotheum			( 843, GT_ItemTextures.SET_SHINY			, 1                                     , 255, 255, 255,   0,	"Pyrotheum"						,    2,      62,          0,          0,          0,    0, false, false,   2,   3,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Coal, 1), new MaterialStack(Redstone, 1), new MaterialStack(Blaze, 1))),
	HydratedCoal		( 818, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Hydrated Coal"					,    0,       0,          0,          0,          0,    0, false, false,   1,   9,   8, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(Coal, 8), new MaterialStack(Water, 1))),
	Apatite				( 530, GT_ItemTextures.SET_DULL				, 1  |4|8                               , 255, 255, 255,   0,	"Apatite"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeCyan		, 1, Arrays.asList(new MaterialStack(Calcium, 5), new MaterialStack(Phosphate, 3), new MaterialStack(Chlorine, 1))),
	Alumite				(  -1, GT_ItemTextures.SET_NONE				, 1|2                                   , 255, 255, 255,   0,	"Alumite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		, 2, Arrays.asList(new MaterialStack(Aluminium, 5), new MaterialStack(Iron, 2), new MaterialStack(Obsidian, 2))),
	Manyullyn			(  -1, GT_ItemTextures.SET_NONE				, 1|2                                   , 255, 255, 255,   0,	"Manyullyn"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		, 2, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Aredrite, 1))),
	IronWood			( 338, GT_ItemTextures.SET_ROOT				, 1|2                                   , 255, 255, 255,   0,	"Ironwood"						,    0,       0,          0,          0,          0,    0, false, false,   2,  19,  18, Dyes.dyeBrown		, 2, Arrays.asList(new MaterialStack(Iron, 9), new MaterialStack(Liveroot, 9), new MaterialStack(Gold, 1))),
	ShadowIron			( 336, GT_ItemTextures.SET_METALLIC			, 1|2  |8                               , 255, 255, 255,   0,	"Shadowiron"					,    0,       0,          0,          0,          0,    0, false, false,   3,   4,   3, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(Iron, 3), new MaterialStack(Magic, 1))),
	ShadowSteel			( 337, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Shadowsteel"					,    0,       0,          0,          0,       1700, 1700,  true, false,   4,   4,   3, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(Steel, 3), new MaterialStack(Magic, 1))),
	SteelLeaf			( 339, GT_ItemTextures.SET_LEAF				, 1|2                                   , 255, 255, 255,   0,	"Steelleaf"						,    0,       0,          0,          0,          0,    0, false, false,   4,   2,   1, Dyes.dyeGreen		, 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Magic, 1))),
	BlackSteel			( 334, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Black Steel"					,    0,       0,          0,          0,       1200, 1200,  true, false,   4,   1,   1, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(Steel, 1))),
	DamascusSteel		( 335, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Damascus Steel"				,    0,       0,          0,          0,       1500, 1500,  true, false,   4,   1,   1, Dyes.dyeGray		, 2, Arrays.asList(new MaterialStack(Steel, 1))),
	TungstenSteel		( 316, GT_ItemTextures.SET_METALLIC			, 1|2                                   , 255, 255, 255,   0,	"Tungstensteel"					,    0,       0,          0,          0,       3000, 3000,  true, false,   4,   1,   1, Dyes.dyeBlue		, 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Tungsten, 1))),
	NitroCoalFuel		( 711, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Nitro-Coalfuel"				,    0,      48,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeBlack		, 0, Arrays.asList(new MaterialStack(Glyceryl, 1), new MaterialStack(CoalFuel, 4))),
	NitroFuel			( 709, GT_ItemTextures.SET_FLUID			,         16                            , 255, 255, 255,   0,	"Nitro-Diesel"					,    0,     384,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeLime		, 0, Arrays.asList(new MaterialStack(Glyceryl, 1), new MaterialStack(Fuel, 4))),
	AstralSilver		( 333, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Astral Silver"					,    0,       0,          0,          0,          0,    0, false, false,   4,   3,   2, Dyes.dyeWhite		, 2, Arrays.asList(new MaterialStack(Silver, 2), new MaterialStack(Magic, 1))),
	Midasium			( 332, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Midasium"						,    0,       0,          0,          0,          0,    0, false, false,   4,   3,   2, Dyes.dyeOrange		, 2, Arrays.asList(new MaterialStack(Gold, 2), new MaterialStack(Magic, 1))),
	Mithril				( 331, GT_ItemTextures.SET_SHINY			, 1|2  |8                               , 255, 255, 255,   0,	"Mithril"						,    0,       0,          0,          0,          0,    0, false, false,   4,   3,   2, Dyes.dyeLightBlue	, 2, Arrays.asList(new MaterialStack(Platinum, 2), new MaterialStack(Magic, 1))),
	BlueAlloy			( 309, GT_ItemTextures.SET_DULL				, 1|2                                   , 255, 255, 255,   0,	"Blue Alloy"					,    0,       0,          0,          0,          0,    0, false, false,   3,   5,   1, Dyes.dyeLightBlue	, 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Nikolite, 4))),
	RedAlloy			( 308, GT_ItemTextures.SET_DULL				, 1|2                                   , 255, 255, 255,   0,	"Red Alloy"						,    0,       0,          0,          0,          0,    0, false, false,   3,   5,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Metal, 1), new MaterialStack(Redstone, 4))),
	Phosphorus			( 534, GT_ItemTextures.SET_DULL				, 1  |4|8|16                            , 255, 255, 255,   0,	"Phosphorus"					,    0,       0,          0,          0,          0,    0, false, false,   3,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Phosphate, 2))),
	Basalt				( 844, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Basalt"						,    0,       0,          0,          0,          0,    0, false, false,   2,   1,   1, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(Olivine, 1), new MaterialStack(Calcite, 3), new MaterialStack(Flint, 8), new MaterialStack(DarkAsh, 4))),
	GarnetRed			( 527, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Red Garnet"					,    0,       0,          0,          0,          0,    0, false,  true,   4,   1,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Pyrope, 3), new MaterialStack(Almandine, 5), new MaterialStack(Spessartine, 8))),
	GarnetYellow		( 528, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Yellow Garnet"					,    0,       0,          0,          0,          0,    0, false,  true,   4,   1,   1, Dyes.dyeYellow		, 2, Arrays.asList(new MaterialStack(Andradite, 5), new MaterialStack(Grossular, 8), new MaterialStack(Uvarovite, 3))),
	Marble				( 845, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Marble"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		, 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Calcite, 7))),
	Sugar				( 803, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Sugar"							,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeWhite		, 1, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Water, 5), new MaterialStack(Oxygen, 25))),
	Thaumium			( 330, GT_ItemTextures.SET_SHINY			, 1|2                                   , 255, 255, 255,   0,	"Thaumium"						,    0,       0,          0,          0,          0,    0, false, false,   5,   2,   1, Dyes.dyePurple		, 0, Arrays.asList(new MaterialStack(Metal, 1), new MaterialStack(Magic, 1))),
	Vinteum				( 529, GT_ItemTextures.SET_SHINY			, 1  |4|8                               , 255, 255, 255,   0,	"Vinteum"						,    5,      32,          0,          0,          0,    0, false, false,   4,   1,   1, Dyes.dyeLightBlue	, 2, Arrays.asList(new MaterialStack(Magic, 1))),
	Vis					(  -1, GT_ItemTextures.SET_NONE				, 0                                     , 255, 255, 255,   0,	"Vis"							,    5,      32,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePurple		, 2, Arrays.asList(new MaterialStack(Magic, 1))),
	Redrock				( 846, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Redrock"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeRed			, 2, Arrays.asList(new MaterialStack(Calcite, 2), new MaterialStack(Flint, 1), new MaterialStack(Clay, 1))),
	PotassiumFeldspar	( 847, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Potassium Feldspar"			,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyePink		, 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 8))),
	Biotite				( 848, GT_ItemTextures.SET_METALLIC			, 1                                     , 255, 255, 255,   0,	"Biotite"						,    0,       0,          0,          0,          0,    0, false, false,   1,   1,   1, Dyes.dyeGray		, 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 3), new MaterialStack(Aluminium, 3), new MaterialStack(Fluorine, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 10))),
	GraniteBlack		( 849, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Black Granite"					,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeBlack		, 2, Arrays.asList(new MaterialStack(SiliconDioxide, 4), new MaterialStack(Biotite, 1))),
	GraniteRed			( 850, GT_ItemTextures.SET_DULL				, 1                                     , 255, 255, 255,   0,	"Red Granite"					,    0,       0,          0,          0,          0,    0, false, false,   0,   1,   1, Dyes.dyeMagenta		, 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3), new MaterialStack(PotassiumFeldspar, 1))),
	
	/**
	 * Materials which are renamed automatically
	 */
	@Deprecated FzDarkIron			(DarkIron),
	@Deprecated Ashes				(Ash),
	@Deprecated DarkAshes			(DarkAsh),
	@Deprecated Abyssal				(Basalt),
	@Deprecated Adamantium			(Adamant),
	@Deprecated AluminumBrass		(AluminiumBrass),
	@Deprecated Aluminum			(Aluminium),
	@Deprecated NaturalAluminum		(Aluminium),
	@Deprecated NaturalAluminium	(Aluminium),
	@Deprecated Beryl				(Emerald),
	@Deprecated BlackGranite		(GraniteBlack),
	@Deprecated CalciumCarbonate	(Calcite),
	@Deprecated CreosoteOil			(Creosote),
	@Deprecated Chromium			(Chrome),
	@Deprecated Diesel				(Fuel),
	@Deprecated Enderpearl			(EnderPearl),
	@Deprecated Endereye			(EnderEye),
	@Deprecated EyeOfEnder			(EnderEye),
	@Deprecated Eyeofender			(EnderEye),
	@Deprecated Flour				(Wheat),
	@Deprecated Garnet				(GarnetRed),
	@Deprecated Granite				(GraniteBlack),
	@Deprecated Kalium				(Potassium),
	@Deprecated Lapislazuli			(Lapis),
	@Deprecated LapisLazuli			(Lapis),
	@Deprecated Monazit				(Monazite),
	@Deprecated Natrium				(Sodium),
	@Deprecated NitroDiesel			(NitroFuel),
	@Deprecated Obby				(Obsidian),
	@Deprecated Peridot				(Olivine),
	@Deprecated Phosphorite			(Phosphorus),
	@Deprecated Quarried			(Marble),
	@Deprecated Quicksilver			(Mercury),
	@Deprecated QuickSilver			(Mercury),
	@Deprecated RedRock				(Redrock),
	@Deprecated RefinedIron			(Iron),
	@Deprecated RedGranite			(GraniteRed),
	@Deprecated Sheldonite			(Cooperite),
	@Deprecated SilverLead			(Galena),
	@Deprecated Titan				(Titanium),
	@Deprecated Uran				(Uranium),
	@Deprecated Wolframite			(Tungstate),
	@Deprecated Wolframium			(Tungsten),
	@Deprecated Wolfram				(Tungsten),
	@Deprecated WrougtIron			(WroughtIron);
	
	static {
		Redstone		.add(SubTag.PULVERIZING_CINNABAR);
		
		Pyrite			.add(SubTag.BLASTFURNACE_CALCITE_DOUBLE);
		
		Iron			.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		PigIron			.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		DeepIron		.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		ShadowIron		.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		WroughtIron		.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		MeteoricIron	.add(SubTag.BLASTFURNACE_CALCITE_TRIPLE);
		
		Gold			.add(SubTag.WASHING_MERCURY);
		Silver			.add(SubTag.WASHING_MERCURY);
		Osmium			.add(SubTag.WASHING_MERCURY);
		Mithril			.add(SubTag.WASHING_MERCURY);
		Platinum		.add(SubTag.WASHING_MERCURY);
		Midasium		.add(SubTag.WASHING_MERCURY);
		Cooperite		.add(SubTag.WASHING_MERCURY);
		AstralSilver	.add(SubTag.WASHING_MERCURY);
		
		Zinc			.add(SubTag.WASHING_SODIUMPERSULFATE);
		Nickel			.add(SubTag.WASHING_SODIUMPERSULFATE);
		Copper			.add(SubTag.WASHING_SODIUMPERSULFATE);
		Cobalt			.add(SubTag.WASHING_SODIUMPERSULFATE);
		Cobaltite		.add(SubTag.WASHING_SODIUMPERSULFATE);
		Tetrahedrite	.add(SubTag.WASHING_SODIUMPERSULFATE);
		
		Salt			.setOreMultiplier( 2).setSmeltingMultiplier( 2);
		Cassiterite		.setOreMultiplier( 2).setSmeltingMultiplier( 2);
		NetherQuartz	.setOreMultiplier( 2).setSmeltingMultiplier( 2);
		Phosphorus		.setOreMultiplier( 3).setSmeltingMultiplier( 3);
		Sulfur			.setOreMultiplier( 4).setSmeltingMultiplier( 4);
		Saltpeter		.setOreMultiplier( 4).setSmeltingMultiplier( 4);
		Apatite			.setOreMultiplier( 4).setSmeltingMultiplier( 4).setByProductMultiplier(2);
		Bauxite			.setOreMultiplier( 4).setSmeltingMultiplier( 4).setByProductMultiplier(4);
		Nikolite		.setOreMultiplier( 5).setSmeltingMultiplier( 5);
		Redstone		.setOreMultiplier( 5).setSmeltingMultiplier( 5);
		Glowstone		.setOreMultiplier( 5).setSmeltingMultiplier( 5);
		Lapis			.setOreMultiplier( 6).setSmeltingMultiplier( 6).setByProductMultiplier(4);
		Sodalite		.setOreMultiplier( 6).setSmeltingMultiplier( 6).setByProductMultiplier(4);
		Lazurite		.setOreMultiplier( 6).setSmeltingMultiplier( 6).setByProductMultiplier(4);
		Monazite		.setOreMultiplier( 8).setSmeltingMultiplier( 8).setByProductMultiplier(2);
		
		Pyrite			.setDirectSmelting(Iron			).add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
		Tetrahedrite	.setDirectSmelting(Copper		).add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
		Sphalerite		.setDirectSmelting(Zinc			).add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
		Cinnabar		.setDirectSmelting(Mercury		).add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT).setGemSmelting();
		Chromite		.setOreReplacement(Chrome		).setDirectSmelting(Chrome			);
		Cobaltite		.setOreReplacement(Cobalt		).setDirectSmelting(Cobalt			);
		Cooperite		.setOreReplacement(Platinum		).setDirectSmelting(Platinum		);
		Tungstate		.setOreReplacement(Tungsten		).setDirectSmelting(Tungsten		);
		Adamantine		.setOreReplacement(Adamant		).setDirectSmelting(Adamant			);
		Cassiterite		.setOreReplacement(Tin			).setDirectSmelting(Tin				);
		
		Galena			.addOreByProduct(Sulfur			).addOreByProduct(Silver			).addOreByProduct(Lead			);
		Lapis			.addOreByProduct(Lazurite		).addOreByProduct(Sodalite			).addOreByProduct(Pyrite		);
		Pyrite			.addOreByProduct(Sulfur			).addOreByProduct(Phosphorus		).addOreByProduct(Iron			);
		Tungstate		.addOreByProduct(Manganese		).addOreByProduct(Silver			).addOreByProduct(Lithium			);
		Sphalerite		.addOreByProduct(Zinc			).addOreByProduct(GarnetYellow		);
		Iron			.addOreByProduct(Nickel			).addOreByProduct(Tin				);
		Gold			.addOreByProduct(Copper			).addOreByProduct(Nickel			);
		Copper			.addOreByProduct(Gold			).addOreByProduct(Nickel			);
		Tin				.addOreByProduct(Iron			).addOreByProduct(Zinc				);
		Antimony		.addOreByProduct(Zinc			).addOreByProduct(Iron				);
		Nickel			.addOreByProduct(Iron			).addOreByProduct(Platinum			);
		Silver			.addOreByProduct(Lead			).addOreByProduct(Sulfur			);
		Lead			.addOreByProduct(Silver			).addOreByProduct(Sulfur			);
		Cinnabar		.addOreByProduct(Redstone		).addOreByProduct(Glowstone			);
		Uranium			.addOreByProduct(Plutonium		).addOreByProduct(Lead				);
		Thorium			.addOreByProduct(Uranium		).addOreByProduct(Lead				);
		Plutonium		.addOreByProduct(Uranium		).addOreByProduct(Lead				);
		Electrum		.addOreByProduct(Gold			).addOreByProduct(Silver			);
		Bronze			.addOreByProduct(Copper			).addOreByProduct(Tin				);
		Brass			.addOreByProduct(Copper			).addOreByProduct(Zinc				);
		Coal			.addOreByProduct(Coal			).addOreByProduct(Thorium			);
		Redstone		.addOreByProduct(Cinnabar		).addOreByProduct(Glowstone			);
		Glowstone		.addOreByProduct(Redstone		).addOreByProduct(Gold				);
		Bauxite			.addOreByProduct(Grossular		).addOreByProduct(Titanium			);
		Manganese		.addOreByProduct(Chrome			).addOreByProduct(Iron				);
		Sapphire		.addOreByProduct(Aluminium		).addOreByProduct(GreenSapphire		);
		GreenSapphire	.addOreByProduct(Aluminium		).addOreByProduct(Sapphire			);
		GarnetRed		.addOreByProduct(Spessartine	).addOreByProduct(Ruby				);
		GarnetYellow	.addOreByProduct(Uvarovite		).addOreByProduct(Sphalerite		);
		Platinum		.addOreByProduct(Iridium		).addOreByProduct(Nickel			);
		Emerald			.addOreByProduct(Beryllium		).addOreByProduct(Aluminium			);
		Olivine			.addOreByProduct(Pyrope			).addOreByProduct(Magnesium			);
		Cooperite		.addOreByProduct(Palladium		).addOreByProduct(Iridium			);
		Chrome			.addOreByProduct(Iron			).addOreByProduct(Magnesium			);
		Tetrahedrite	.addOreByProduct(Antimony		).addOreByProduct(Zinc				);
		Basalt			.addOreByProduct(Olivine		).addOreByProduct(DarkAsh			);
		Lazurite		.addOreByProduct(Sodalite		).addOreByProduct(Lapis				);
		Sodalite		.addOreByProduct(Lazurite		).addOreByProduct(Lapis				);
		Ruby			.addOreByProduct(Chrome			).addOreByProduct(GarnetRed			);
		PigIron			.addOreByProduct(Iron			);
		Calcite			.addOreByProduct(Andradite		);
		Apatite			.addOreByProduct(Phosphorus		);
		Zinc			.addOreByProduct(Tin			);
		Nikolite		.addOreByProduct(Diamond		);
		Monazite		.addOreByProduct(Thorium		);
		NetherQuartz	.addOreByProduct(Netherrack		);
		DeepIron		.addOreByProduct(Iron			);
		ShadowIron		.addOreByProduct(Iron			);
		Midasium		.addOreByProduct(Gold			);
		AstralSilver	.addOreByProduct(Silver			);
		Steel			.addOreByProduct(Iron			);
		Phosphorus		.addOreByProduct(Apatite		);
		Netherrack		.addOreByProduct(Sulfur			);
		Flint			.addOreByProduct(Obsidian		);
		Sulfur			.addOreByProduct(Sulfur			);
		Saltpeter		.addOreByProduct(Saltpeter		);
		Endstone		.addOreByProduct(Helium_3		);
		Pyrope			.addOreByProduct(GarnetRed		);
		Almandine		.addOreByProduct(GarnetRed		);
		Spessartine		.addOreByProduct(GarnetRed		);
		Andradite		.addOreByProduct(GarnetYellow	);
		Grossular		.addOreByProduct(GarnetYellow	);
		Uvarovite		.addOreByProduct(GarnetYellow	);
		Diamond			.addOreByProduct(Coal			);
		Osmium			.addOreByProduct(Iridium		);
		Iridium			.addOreByProduct(Platinum		).addOreByProduct(Osmium			);
		Magnesium		.addOreByProduct(Olivine		);
		Aluminium		.addOreByProduct(Bauxite		);
		Titanium		.addOreByProduct(Almandine		);
		Chromite		.addOreByProduct(Iron			);
		Tungsten		.addOreByProduct(Manganese		);
		Obsidian		.addOreByProduct(Olivine		);
		Ash				.addOreByProduct(Carbon			);
		DarkAsh			.addOreByProduct(Carbon			);
		Redrock			.addOreByProduct(Clay			);
		Marble			.addOreByProduct(Calcite		);
		Clay			.addOreByProduct(Clay			);
		GraniteBlack	.addOreByProduct(Biotite		);
		GraniteRed		.addOreByProduct(PotassiumFeldspar);
	}
	
	public static Materials get(String aMaterialName) {
		Object tObject = GT_Utility.getFieldContent(Materials.class, aMaterialName, false, false);
		if (tObject != null && tObject instanceof Materials) return (Materials)tObject;
		return _NULL;
	}
	
	public static Materials getRealMaterial(String aMaterialName) {
		return get(aMaterialName).mMaterialInto;
	}
	
	public static void init(GT_Config aConfiguration) {
		for (Materials tMaterial : values()) {
			String tString = tMaterial.toString().toLowerCase();
			if (tMaterial.mBlastFurnaceRequired) tMaterial.mBlastFurnaceRequired = aConfiguration.add(GT_ConfigCategories.Materials.blastfurnacerequirements, tString, true);
			if (tMaterial.mAmplificationValue > 0) tMaterial.mAmplificationValue = aConfiguration.add(GT_ConfigCategories.Materials.UUM_MaterialCost, tString, tMaterial.mAmplificationValue);
			if (tMaterial.mUUMEnergy > 0) tMaterial.mUUMEnergy = aConfiguration.add(GT_ConfigCategories.Materials.UUM_EnergyCost, tString, tMaterial.mUUMEnergy);
			if (tMaterial.mBlastFurnaceRequired && aConfiguration.add(GT_ConfigCategories.Materials.blastinductionsmelter, tString, tMaterial.mBlastFurnaceTemp < 1500)) GT_ModHandler.ThermalExpansion.addSmelterBlastOre(tMaterial);
		}
	}
	
	public boolean isRadioactive() {
		if (mElement != null) return mElement.mHalfLifeSeconds >= 0;
		for (MaterialStack tMaterial : mMaterialList) if (tMaterial.mMaterial.isRadioactive()) return true;
		return false;
	}
	
	public int getProtons() {
		if (mElement != null) return mElement.getProtons();
		if (mMaterialList.size() <= 0) return Element.Tc.getProtons();
		int rAmount = 0, tAmount = 0;
		for (MaterialStack tMaterial : mMaterialList) {
			tAmount += tMaterial.mAmount;
			rAmount += tMaterial.mAmount * tMaterial.mMaterial.getProtons();
		}
		return (int)((getDensity() * rAmount) / (tAmount * GregTech_API.MATERIAL_UNIT));
	}
	
	public int getNeutrons() {
		if (mElement != null) return mElement.getNeutrons();
		if (mMaterialList.size() <= 0) return Element.Tc.getNeutrons();
		int rAmount = 0, tAmount = 0;
		for (MaterialStack tMaterial : mMaterialList) {
			tAmount += tMaterial.mAmount;
			rAmount += tMaterial.mAmount * tMaterial.mMaterial.getNeutrons();
		}
		return (int)((getDensity() * rAmount) / (tAmount * GregTech_API.MATERIAL_UNIT));
	}
	
	public int getMass() {
		if (mElement != null) return mElement.getMass();
		if (mMaterialList.size() <= 0) return Element.Tc.getMass();
		int rAmount = 0, tAmount = 0;
		for (MaterialStack tMaterial : mMaterialList) {
			tAmount += tMaterial.mAmount;
			rAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
		}
		return (int)((getDensity() * rAmount) / (tAmount * GregTech_API.MATERIAL_UNIT));
	}
	
	public long getDensity() {
		return mDensity;
	}
	
	public String getToolTip() {
		return getToolTip(1);
	}
	
	public String getToolTip(long aMultiplier) {
		if (getDensity() * aMultiplier >= GregTech_API.MATERIAL_UNIT * 2) {
			if (mElement != null || mMaterialList.size() < 2) return mChemicalFormula + ((getDensity() * aMultiplier) / GregTech_API.MATERIAL_UNIT);
			return "(" + mChemicalFormula + ")" + ((getDensity() * aMultiplier) / GregTech_API.MATERIAL_UNIT);
		}
		return mChemicalFormula;
	}
	
	private final ArrayList<ItemStack> mMaterialItems = new ArrayList<ItemStack>();
	
	/**
	 * Adds an ItemStack to this Material.
	 */
	public Materials add(ItemStack aStack) {
		if (aStack != null && !contains(aStack)) mMaterialItems.add(aStack);
		return this;
	}
	
	/**
	 * This is used to determine if any of the ItemStacks belongs to this Material.
	 */
	public boolean contains(ItemStack... aStacks) {
		if (aStacks == null || aStacks.length <= 0) return false;
		for (ItemStack tStack : mMaterialItems) for (ItemStack aStack : aStacks) if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) return true;
		return false;
	}
	
	/**
	 * This is used to determine if an ItemStack belongs to this Material.
	 */
	public boolean remove(ItemStack aStack) {
		if (aStack == null) return false;
		boolean temp = false;
		for (int i = 0; i < mMaterialItems.size(); i++) if (GT_Utility.areStacksEqual(aStack, mMaterialItems.get(i))) {
			mMaterialItems.remove(i--);
			temp = true;
		}
		return temp;
	}
	
	private final List<SubTag> mSubTags = new ArrayList<SubTag>();
	
	/**
	 * Adds a SubTag to this Material
	 */
	public Materials add(SubTag aTag) {
		if (aTag != null && !contains(aTag)) mSubTags.add(aTag);
		return this;
	}
	
	/**
	 * If this Material has this exact SubTag
	 */
	public boolean contains(SubTag aTag) {
		return mSubTags.contains(aTag);
	}
	
	/**
	 * Removes a SubTag from this Material
	 */
	public boolean remove(SubTag aTag) {
		return mSubTags.remove(aTag);
	}
	
	/**
	 * Adds a Material to the List of Byproducts when grinding this Ore.
	 * Is used for more precise Ore grinding, so that it is possible to choose between certain kinds of Materials.
	 */
	public Materials addOreByProduct(Materials aMaterial) {
		if (!mOreByProducts.contains(aMaterial)) mOreByProducts.add(aMaterial);
		return this;
	}
	
	/**
	 * If this Ore gives multiple drops of its Main Material.
	 * Lapis Ore for example gives about 6 drops.
	 */
	public Materials setOreMultiplier(int aOreMultiplier) {
		if (aOreMultiplier > 0) mOreMultiplier = aOreMultiplier;
		return this;
	}
	
	/**
	 * If this Ore gives multiple drops of its Byproduct Material.
	 */
	public Materials setByProductMultiplier(int aByProductMultiplier) {
		if (aByProductMultiplier > 0) mByProductMultiplier = aByProductMultiplier;
		return this;
	}
	
	/**
	 * If this Ore gives multiple drops of its Main Material.
	 * Lapis Ore for example gives about 6 drops.
	 */
	public Materials setSmeltingMultiplier(int aSmeltingMultiplier) {
		if (aSmeltingMultiplier > 0) mSmeltingMultiplier = aSmeltingMultiplier;
		return this;
	}
	
	/**
	 * This Ore should be smolten directly into an Ingot of this Material instead of an Ingot of itself.
	 */
	public Materials setDirectSmelting(Materials aMaterial) {
		if (aMaterial != null) mDirectSmelting = aMaterial;
		return this;
	}
	
	/**
	 * This Ore should be smolten directly into a Gem of this Material, if the Ingot is missing.
	 */
	public Materials setGemSmelting() {
		mOreSmeltsToGem = true;
		return this;
	}
	
	/**
	 * This Material should be the Main Material this Ore gets ground into.
	 * Example, Chromite giving Chrome or Tungstate giving Tungsten.
	 */
	public Materials setOreReplacement(Materials aMaterial) {
		if (aMaterial != null) mOreReplacement = aMaterial;
		return this;
	}
	
	public final IIconContainer[] mIconSet;
	public boolean mBlastFurnaceRequired = false, mTransparent = false, mOreSmeltsToGem = false;
	public String mChemicalFormula = "?", mDefaultLocalName;
	public Dyes mColor = Dyes._NULL;
	public short mMeltingPoint = 0, mBlastFurnaceTemp = 0;
	public int mAmplificationValue = 0, mUUMEnergy = 0, mFuelPower = 0, mFuelType = 0, mExtraData = 0, mOreValue = 0, mOreMultiplier = 1, mByProductMultiplier = 1, mSmeltingMultiplier = 1;
	public long mDensity = GregTech_API.MATERIAL_UNIT;
	public Element mElement = null;
	public Materials mDirectSmelting = this, mOreReplacement = this;
	public final int mMetaItemSubID;
	public final boolean mUnificatable;
	public final Materials mMaterialInto;
	public final List<MaterialStack> mMaterialList = new ArrayList<MaterialStack>();
	public final List<Materials> mOreByProducts = new ArrayList<Materials>();
	public FluidStack mSolid = null, mFluid = null, mGas = null, mPlasma = null;
	
	private Materials(int aMetaItemSubID, IIconContainer[] aIconSet, boolean aUnificatable) {
		mUnificatable = aUnificatable;
		mMaterialInto = this;
		mMetaItemSubID = aMetaItemSubID;
		mIconSet = Arrays.copyOf(aIconSet, 32);
		if (aMetaItemSubID >= 0) {
			if (GregTech_API.sGeneratedMaterials[aMetaItemSubID] == null) {
				GregTech_API.sGeneratedMaterials[aMetaItemSubID] = this;
			} else {
				throw new IllegalArgumentException("The Index " + aMetaItemSubID + " is already used!");
			}
		}
	}
	
	private Materials(Materials aMaterialInto) {
		mUnificatable = false;
		mDefaultLocalName = aMaterialInto.mDefaultLocalName;
		mMaterialInto = aMaterialInto.mMaterialInto;
		mChemicalFormula = aMaterialInto.mChemicalFormula;
		mMetaItemSubID = aMaterialInto.mMetaItemSubID;
		mIconSet = GT_ItemTextures.SET_NONE;
	}
	
	/**
	 * @param aMetaItemSubID the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
	 * @param aTypes which kind of Items should be generated. Bitmask as follows:
	 *      1 = Dusts of all kinds.
	 *      2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other Metal specific things.
	 *      4 = Dusts, Gems, Plates, Lenses (if transparent).
	 *      8 = Dusts, Impure Dusts, crushed Ores, purified Ores, centrifuged Ores etc.
	 *     16 = Cells and Plasma Cells.
	 * @param aR, aG, aB Color of the Material 0-255 each.
	 * @param aA transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
	 * @param aLocalName The Name used as Default for localization.
	 * @param aFuelType Type of Generator to get Energy from this Material.
	 * @param aFuelPower EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for Gems.
	 * @param aAmplificationValue Amount of UUM amplifier gotten from this.
	 * @param aUUMEnergy Amount of EU needed to shape the UUM into this Material.
	 * @param aMeltingPoint Used to determine the smelting Costs in Furnii.
	 * @param aBlastFurnaceTemp Used to determine the needed Heat capactiy Costs in Blast Furnii.
	 * @param aBlastFurnaceRequired If this requires a Blast Furnace.
	 * @param aColor Vanilla MC Wool Color which comes the closest to this.
	 */
	private Materials(int aMetaItemSubID, IIconContainer[] aIconSet, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aAmplificationValue, int aUUMEnergy, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor) {
		this(aMetaItemSubID, aIconSet, true);
		mDefaultLocalName = aLocalName;
		mMeltingPoint = (short)aMeltingPoint;
		mBlastFurnaceTemp = (short)aBlastFurnaceTemp;
		mBlastFurnaceRequired = aBlastFurnaceRequired;
		mTransparent = aTransparent;
		mAmplificationValue = aAmplificationValue;
		mUUMEnergy = aUUMEnergy;
		mFuelPower = aFuelPower;
		mFuelType = aFuelType;
		mOreValue = aOreValue;
		mDensity = (GregTech_API.MATERIAL_UNIT * aDensityMultiplier) / aDensityDivider;
		mColor = aColor==null?Dyes._NULL:aColor;
	}
	
	/**
	 * @param aElement The Element Enum represented by this Material
	 */
	private Materials(int aMetaItemSubID, IIconContainer[] aIconSet, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aAmplificationValue, int aUUMEnergy, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, Element aElement) {
		this(aMetaItemSubID, aIconSet, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aAmplificationValue, aUUMEnergy, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
		mElement = aElement;
		mElement.mLinkedMaterials.add(this);
		mChemicalFormula = aElement.toString();
		mChemicalFormula = mChemicalFormula.replaceAll("_", "-");
	}
	
	private Materials(int aMetaItemSubID, IIconContainer[] aIconSet, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aAmplificationValue, int aUUMEnergy, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList) {
		this(aMetaItemSubID, aIconSet, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aAmplificationValue, aUUMEnergy, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
		mExtraData = aExtraData;
		mMaterialList.addAll(aMaterialList);
		mChemicalFormula = "";
		for (MaterialStack tMaterial : mMaterialList) mChemicalFormula += tMaterial.toString();
		mChemicalFormula = mChemicalFormula.replaceAll("_", "-");
	}
	
	public static volatile int VERSION = 407;
}