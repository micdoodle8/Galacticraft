package micdoodle8.mods.galacticraft.core.util;

public class JavaUtil extends SecurityManager
{
    public static JavaUtil instance = new JavaUtil();

    public Class<?> getCaller()
    {
        return getClassContext()[2];
    }

    public boolean isCalledBySpecific(Class<?> clazz)
    {
        Class<?>[] context = getClassContext();
        if (context.length < 4) return false;
        return (clazz == context[2] && !clazz.isAssignableFrom(context[3]));
    }
}
