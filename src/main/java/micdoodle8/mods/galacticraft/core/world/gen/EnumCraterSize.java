package micdoodle8.mods.galacticraft.core.world.gen;

public enum EnumCraterSize
{
    SMALL(8, 12, 5, 3),
    MEDIUM(13, 17, 4, 4),
    LARGE(18, 25, 3, 6),
    EXTREME(26, 30, 2, 8),
    DINOSAUR_KILLER(45, 80, 1, 10);

    public final int MIN_SIZE;
    public final int MAX_SIZE;
    private final int PROBABILITY;
    public final int DEPTH;

    public static EnumCraterSize[] sizeArray;

    EnumCraterSize(int min, int max, int prob, int depth)
    {
        this.MIN_SIZE = min;
        this.MAX_SIZE = max;
        this.PROBABILITY = prob;
        this.DEPTH = depth;
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
