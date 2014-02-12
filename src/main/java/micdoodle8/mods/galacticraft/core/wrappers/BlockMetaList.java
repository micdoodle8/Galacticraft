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
	private Block block;
	private List<Integer> metaList;

	public BlockMetaList(Block block, Integer... metadata)
	{
		this(block, Arrays.asList(metadata));
	}

	public BlockMetaList(Block block, List<Integer> metadata)
	{
		this.block = block;
		this.metaList = metadata;
	}

	public Block getBlock()
	{
		return this.block;
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
		return this.block.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof BlockMetaList)
		{
			return ((BlockMetaList) obj).block == this.block;
		}

		return false;
	}
}
