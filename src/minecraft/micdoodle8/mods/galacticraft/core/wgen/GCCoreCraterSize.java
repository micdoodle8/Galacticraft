package micdoodle8.mods.galacticraft.core.wgen;

public enum GCCoreCraterSize
{
	SMALL(8, 12, 14),
	MEDIUM(13, 17, 8),
	LARGE(18, 25, 2),
	EXTREME(26, 30, 1);

	public final int MIN_SIZE;
	public final int MAX_SIZE;
	private final int PROBABILITY;

	public static GCCoreCraterSize[] sizeArray;

	private GCCoreCraterSize(int min, int max, int prob)
	{
		this.MIN_SIZE = min;
		this.MAX_SIZE = max;
		this.PROBABILITY = prob;
	}

	static
	{
		int amount = 0;
		for(final GCCoreCraterSize c : GCCoreCraterSize.values())
		{
			amount += c.PROBABILITY;
		}
		GCCoreCraterSize.sizeArray = new GCCoreCraterSize[amount];
		int pointer = 0;
		for(final GCCoreCraterSize c : GCCoreCraterSize.values())
		{
			for(int i = 0; i < c.PROBABILITY; i++)
			{
				GCCoreCraterSize.sizeArray[pointer] = c;
				pointer++;
			}
		}
	}
}
