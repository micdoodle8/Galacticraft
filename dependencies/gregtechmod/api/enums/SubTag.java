package gregtechmod.api.enums;

import java.util.ArrayList;

/**
 * Just a simple Class to be able to add special Tags for Materials.
 * 
 * The Tags should be added in preload to the Materials.
 * In order to make yourself a new SubTag, just create one new instance of SubTag
 * and use that one instance on all Materials you want to add those Tags to.
 */
public final class SubTag {
	private static long sSubtagID = 0;
	
	public static final ArrayList<SubTag> sSubTags = new ArrayList<SubTag>();
	
	/**
	 * Add this to your Material if you want to have its Ore Calcite heated in a Blast Furnace for more output. Already listed are:
	 * Iron, Pyrite, PigIron, DeepIron, ShadowIron, WroughtIron and MeteoricIron.
	 */
	public static final SubTag BLASTFURNACE_CALCITE_DOUBLE = new SubTag("BLASTFURNACE_CALCITE_DOUBLE"), BLASTFURNACE_CALCITE_TRIPLE = new SubTag("BLASTFURNACE_CALCITE_TRIPLE");
	
	/**
	 * Materials which are outputting less in an Induction Smelter. Already listed are:
	 * Pyrite
	 */
	public static final SubTag INDUCTIONSMELTING_LOW_OUTPUT = new SubTag("INDUCTIONSMELTING_LOW_OUTPUT");
	
	/**
	 * Add this to your Material if you want to have its Ore Sodium Persulfate washed. Already listed are:
	 * Zinc, Nickel, Copper, Cobalt, Cobaltite and Tetrahedrite.
	 */
	public static final SubTag WASHING_SODIUMPERSULFATE	= new SubTag("WASHING_SODIUMPERSULFATE");
	
	/**
	 * Add this to your Material if you want to have its Ore Mercury washed. Already listed are:
	 * Gold, Silver, Osmium, Mithril, Platinum, Midasium, Cooperite and AstralSilver.
	 */
	public static final SubTag WASHING_MERCURY			= new SubTag("WASHING_MERCURY");
	
	/**
	 * Add this to your Material if you want to have its Ore giving Cinnabar Crystals on Pulverization. Already listed are:
	 * Redstone
	 */
	public static final SubTag PULVERIZING_CINNABAR		= new SubTag("PULVERIZING_CINNABAR");
	
	public final long mSubtagID;
	public final String mName;
	
	public SubTag(String aName) {
		mSubtagID = sSubtagID++;
		mName = aName;
		sSubTags.add(this);
	}
}