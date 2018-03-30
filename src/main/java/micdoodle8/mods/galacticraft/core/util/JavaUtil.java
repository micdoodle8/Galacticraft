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
        
        int imax = Math.min(context.length, 6);
        for (int i = 2; i < imax; i++)
        {
            if (clazz == context[i])
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCalledBy(Class<?> clazz1, Class<?> clazz2)
    {
        Class<?>[] context = getClassContext();
        
        int imax = Math.min(context.length, 7);
        for (int i = 2; i < imax; i++)
        {
            Class<?> test = context[i]; 
            if (test == clazz1 || test == clazz2)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCalledBy(Class<?> clazz1, Class<?> clazz2, Class<?> clazz3)
    {
        Class<?>[] context = getClassContext();
        
        int imax = Math.min(context.length, 6);
        for (int i = 2; i < imax; i++)
        {
            Class<?> test = context[i]; 
            if (test == clazz1 || test == clazz2 || test == clazz3)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCalledBy(String name)
    {
        Class<?>[] context = getClassContext();
        
        int imax = Math.min(context.length, 6);
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

    public int isCalledBySecond(Class<?> clazz1, Class<?> clazz2, Class<?> clazz3)
    {
        Class<?>[] context = getClassContext();
        if (context.length < 4) return 0;
        Class<?> test = context[3];
        if (test == clazz1) return 1;
        if (test == clazz2) return 2;
        if (test == clazz3) return 3;
        return 0;
    }

    public boolean isCalledByThird(Class<?> clazz)
    {
        Class<?>[] context = getClassContext();
        if (context.length < 5) return false;
        return (context[4] == clazz);
    }
}
