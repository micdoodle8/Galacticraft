package gregtechmod.api.interfaces;

import net.minecraft.util.Icon;

public interface IIconContainer {
	/**
	 * @return A regular Icon.
	 */
	public Icon getIcon();
	
	/**
	 * @return Icon of the Overlay (or null if there is no Icon)
	 */
	public Icon getOverlayIcon();
	
	/**
	 * @return X of the Overlay (0-15)
	 */
	public int getOverlayX();
	
	/**
	 * @return Y of the Overlay (0-15)
	 */
	public int getOverlayY();
	
	/**
	 * @return Width of the Overlay (1-16)
	 */
	public int getOverlayWidth();
	
	/**
	 * @return Height of the Overlay (1-16)
	 */
	public int getOverlayHeight();
}
