package micdoodle8.mods.galacticraft.core.world.gen;

import com.google.common.collect.Lists;

import java.util.List;

import micdoodle8.mods.galacticraft.core.util.MapUtil;

public class IntCache
{
    private static int intCacheSize = 256;
    private static List<int[]> freeSmallArrays = Lists.<int[]>newArrayList();
    private static List<int[]> inUseSmallArrays = Lists.<int[]>newArrayList();
    private static List<int[]> freeLargeArrays = Lists.<int[]>newArrayList();
    private static List<int[]> inUseLargeArrays = Lists.<int[]>newArrayList();
    private static int intCacheSize2 = 256;
    private static List<int[]> freeSmallArrays2 = Lists.<int[]>newArrayList();
    private static List<int[]> inUseSmallArrays2 = Lists.<int[]>newArrayList();
    private static List<int[]> freeLargeArrays2 = Lists.<int[]>newArrayList();
    private static List<int[]> inUseLargeArrays2 = Lists.<int[]>newArrayList();

    public static int[] getIntCache(int p_76445_0_)
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            return getIntCacheGC(p_76445_0_);
        }
        return getIntCacheVanilla(p_76445_0_);
    }

    //Obfuscated method name for use by mods, because this won't be processed by the SRG renamer
    public static int[] func_76445_a(int p_76445_0_)
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            return getIntCacheGC(p_76445_0_);
        }
        return getIntCacheVanilla(p_76445_0_);
    }
    
    //Obfuscated method name for use by vanilla, because this won't be processed by the deobfuscators
    public static int[] a(int p_76445_0_)
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            return getIntCacheGC(p_76445_0_);
        }
        return getIntCacheVanilla(p_76445_0_);
    }
        
    //Vanilla method needs to be synchronized
    public synchronized static int[] getIntCacheVanilla(int p_76445_0_)
    {
        if (p_76445_0_ <= 256)
        {
            if (freeSmallArrays.isEmpty())
            {
                int[] aint4 = new int[256];
                inUseSmallArrays.add(aint4);
                return aint4;
            }
            else
            {
                int[] aint3 = (int[])freeSmallArrays.remove(freeSmallArrays.size() - 1);
                inUseSmallArrays.add(aint3);
                return aint3;
            }
        }
        else if (p_76445_0_ > intCacheSize)
        {
            intCacheSize = p_76445_0_;
            freeLargeArrays.clear();
            inUseLargeArrays.clear();
            int[] aint2 = new int[intCacheSize];
            inUseLargeArrays.add(aint2);
            return aint2;
        }
        else if (freeLargeArrays.isEmpty())
        {
            int[] aint1 = new int[intCacheSize];
            inUseLargeArrays.add(aint1);
            return aint1;
        }
        else
        {
            int[] aint = (int[])freeLargeArrays.remove(freeLargeArrays.size() - 1);
            inUseLargeArrays.add(aint);
            return aint;
        }
    }

    //GC version does not need to be synchonised because we've already tested that it is called from
    //our own background mapping thread, and those are tightly controlled so only one can be running
    public static int[] getIntCacheGC(int size)
    {
        if (size <= 256)
        {
            if (freeSmallArrays2.isEmpty())
            {
                int[] aint4 = new int[256];
                inUseSmallArrays2.add(aint4);
                return aint4;
            }
            else
            {
                int[] aint3 = (int[])freeSmallArrays2.remove(freeSmallArrays2.size() - 1);
                inUseSmallArrays2.add(aint3);
                return aint3;
            }
        }
        else if (size > intCacheSize2)
        {
            intCacheSize2 = size;
            freeLargeArrays2.clear();
            inUseLargeArrays2.clear();
            int[] aint2 = new int[intCacheSize2];
            inUseLargeArrays2.add(aint2);
            return aint2;
        }
        else if (freeLargeArrays2.isEmpty())
        {
            int[] aint1 = new int[intCacheSize2];
            inUseLargeArrays2.add(aint1);
            return aint1;
        }
        else
        {
            int[] aint = (int[])freeLargeArrays2.remove(freeLargeArrays2.size() - 1);
            inUseLargeArrays2.add(aint);
            return aint;
        }
    }

    public static void resetIntCache()
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            resetIntCacheGC();
            return;
        }
        resetIntCacheVanilla();
    }

    //Obfuscated method name for use by mods, because this won't be processed by the SRG renamer
    public static void func_76446_a()
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            resetIntCacheGC();
            return;
        }
        resetIntCacheVanilla();
    }

    //Obfuscated method name for use by vanilla, because this won't be processed by the deobfuscators
    public static void a()
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            resetIntCacheGC();
            return;
        }
        resetIntCacheVanilla();
    }
    
    //Vanilla method needs to be synchronized
    public synchronized static void resetIntCacheVanilla()
    {
        if (!freeLargeArrays.isEmpty())
        {
            freeLargeArrays.remove(freeLargeArrays.size() - 1);
        }

        if (!freeSmallArrays.isEmpty())
        {
            freeSmallArrays.remove(freeSmallArrays.size() - 1);
        }

        freeLargeArrays.addAll(inUseLargeArrays);
        freeSmallArrays.addAll(inUseSmallArrays);
        inUseLargeArrays.clear();
        inUseSmallArrays.clear();
    }

    //GC version does not need to be synchonised because we've already tested that it is called from
    //our own background mapping thread, and those are tightly controlled so only one can be running
    public static void resetIntCacheGC()
    {
        if (!freeLargeArrays2.isEmpty())
        {
            freeLargeArrays2.remove(freeLargeArrays2.size() - 1);
        }

        if (!freeSmallArrays2.isEmpty())
        {
            freeSmallArrays2.remove(freeSmallArrays2.size() - 1);
        }

        freeLargeArrays2.addAll(inUseLargeArrays2);
        freeSmallArrays2.addAll(inUseSmallArrays2);
        inUseLargeArrays2.clear();
        inUseSmallArrays2.clear();
    }

    public static String func_85144_b()
    {
        return getCacheSizes();
    }

    public static String b()
    {
        return getCacheSizes();
    }

    //No GC version of this, because we don't call it - note that vanilla CrashReport calls this (but will be redirected here only in a deobf dev environment, for reasons... basically CrashReport gets called in game startup before Forge and Forge mods have even loaded)
    public synchronized static String getCacheSizes()
    {
        return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
    }
}