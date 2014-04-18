package micdoodle8.mods.galacticraft.core.wrappers;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;

/**
 * BlockMetaList.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockMetaList
{
	private Block blockID;
	private List<Integer> metaList;

	public BlockMetaList(Block blockID, Integer... metadata)
	{
		this(blockID, Arrays.asList(metadata));
	}

	public BlockMetaList(Block blockID, List<Integer> metadata)
	{
		this.blockID = blockID;
		this.metaList = metadata;
	}

	public Block getBlock()
	{
		return this.blockID;
	}

	public List<Integer> getMetaList()
	{
		return this.metaList;
	}

	public void addMetadata(int meta)
	{
		this.metaList.add(meta);
	}

	public void removeMetadata(int meta)
	{
		this.metaList.remove(meta);
	}

	@Override
	public int hashCode()
	{
		return this.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof BlockMetaList)
		{
			return ((BlockMetaList) obj) == this;
		}

		return false;
	}
}
