package micdoodle8.mods.galacticraft.core.util;

public class JavaUtil extends SecurityManager
{
    public static JavaUtil instance = new JavaUtil();

    public Class<?> getCaller()
    {
        return getClassContext()[2];
    }

    public boolean isCalledBy(Class<?> clazz)
    {
        Class<?>[] context = getClassContext();
        
        int imax = Math.max(context.length, 6);
        for (int i = 2; i < imax; i++)
        {
            if (clazz == context[i])
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCalledBy(String name)
    {
        Class<?>[] context = getClassContext();
        
        int imax = Math.max(context.length, 6);
        for (int i = 2; i < imax; i++)
        {
            if (context[i].getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCalledBySpecific(Class<?> clazz)
    {
        Class<?>[] context = getClassContext();
        if (context.length < 6) return false;
        return (clazz == context[3] && !clazz.isAssignableFrom(context[4]));
    }
}
