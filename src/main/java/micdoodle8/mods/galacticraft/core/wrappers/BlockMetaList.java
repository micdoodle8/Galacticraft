package micdoodle8.mods.galacticraft.core.wrappers;

import java.util.Arrays;
import java.util.List;

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
	private Integer blockID;
	private List<Integer> metaList;

	public BlockMetaList(int blockID, Integer... metadata)
	{
		this(blockID, Arrays.asList(metadata));
	}

	public BlockMetaList(int blockID, List<Integer> metadata)
	{
		this.blockID = blockID;
		this.metaList = metadata;
	}

	public int getBlockID()
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
		return this.blockID.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof BlockMetaList)
		{
			return ((BlockMetaList) obj).blockID == this.blockID;
		}

		return false;
	}
}
