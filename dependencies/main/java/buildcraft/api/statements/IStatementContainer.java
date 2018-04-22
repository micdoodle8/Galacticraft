/** Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * The BuildCraft API is distributed under the terms of the MIT License. Please check the contents of the license, which
 * should be located as "LICENSE.API" in the BuildCraft source code distribution. */
package buildcraft.api.statements;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/** This is implemented by objects containing Statements, such as Gates and TileEntities. */
public interface IStatementContainer {
    TileEntity getTile();

    @Nullable
    TileEntity getNeighbourTile(EnumFacing side);
}
