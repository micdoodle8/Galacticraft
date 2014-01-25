package gregtechmod.api.enums;

import gregtechmod.api.util.GT_Utility;

public enum Dyes {
	dyeBlack		( 0, "Black"),
	dyeRed			( 1, "Red"),
	dyeGreen		( 2, "Green"),
	dyeBrown		( 3, "Brown"),
	dyeBlue			( 4, "Blue"),
	dyePurple		( 5, "Purple"),
	dyeCyan			( 6, "Cyan"),
	dyeLightGray	( 7, "Light Gray"),
	dyeGray			( 8, "Gray"),
	dyePink			( 9, "Pink"),
	dyeLime			(10, "Lime"),
	dyeYellow		(11, "Yellow"),
	dyeLightBlue	(12, "Light Blue"),
	dyeMagenta		(13, "Magenta"),
	dyeOrange		(14, "Orange"),
	dyeWhite		(15, "White"),
	_NULL			(-1, "");
	
	public final byte mColor;
	public final String mName;
	
	private Dyes(int aColor, String aName) {
		mColor = (byte)aColor;
		mName = aName;
	}
	
	public static Dyes get(int aColor) {
		if (aColor >= 0 && aColor < 16) return values()[aColor];
		return _NULL;
	}
	
	public static Dyes get(String aColor) {
		Object tObject = GT_Utility.getFieldContent(Dyes.class, aColor, false, false);
		if (tObject != null && tObject instanceof Dyes) return (Dyes)tObject;
		return _NULL;
	}
}