/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package micdoodle8.mods.galacticraft.core.client.fx;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import buildcraft.core.render.TextureLiquidsFX;

public class GCCoreTextureCrudeOilFX extends TextureLiquidsFX
{
	public GCCoreTextureCrudeOilFX()
	{
		super(10, 31, 10, 31, 10, 31, GCCoreBlocks.crudeOilStill.blockIndexInTexture, GCCoreBlocks.crudeOilStill.getTextureFile());
	}
}
