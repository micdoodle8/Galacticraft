package codechicken.core.featurehack;

public class GameDataManipulator
{
    private static boolean override;
    
    public static boolean override()
    {
        return override;
    }
    
    public static void createHiddenItem(Runnable function)
    {
        override = true;
        function.run();
        override = false;
    }
}
