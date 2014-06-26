package micdoodle8.mods.galacticraft.core.world.gen;

public enum EnumCraterSize
{
	SMALL(8, 12, 14), MEDIUM(13, 17, 8), LARGE(18, 25, 2), EXTREME(26, 30, 1);

	public final int MIN_SIZE;
	public final int MAX_SIZE;
	private final int PROBABILITY;

	public static EnumCraterSize[] sizeArray;

	private EnumCraterSize(int min, int max, int prob)
	{
		this.MIN_SIZE = min;
		this.MAX_SIZE = max;
		this.PROBABILITY = prob;
	}

	static
	{
		int amount = 0;
		for (final EnumCraterSize c : EnumCraterSize.values())
		{
			amount += c.PROBABILITY;
		}
		EnumCraterSize.sizeArray = new EnumCraterSize[amount];
		int pointer = 0;
		for (final EnumCraterSize c : EnumCraterSize.values())
		{
			for (int i = 0; i < c.PROBABILITY; i++)
			{
				EnumCraterSize.sizeArray[pointer] = c;
				pointer++;
			}
		}
	}
}
