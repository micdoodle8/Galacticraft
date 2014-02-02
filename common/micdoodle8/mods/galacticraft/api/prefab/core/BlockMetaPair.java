package micdoodle8.mods.galacticraft.api.prefab.core;

/**
 * Do not include this prefab class in your released mod download.
 */
public class BlockMetaPair
{
	private final short blockID;
	private final byte metadata;

	public BlockMetaPair(short blockID, byte metadata)
	{
		this.blockID = blockID;
		this.metadata = metadata;
	}

	public short getBlockID()
	{
		return this.blockID;
	}

	public byte getMetadata()
	{
		return this.metadata;
	}
}
