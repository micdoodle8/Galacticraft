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

    public synchronized static int[] getIntCache(int p_76445_0_)
    {
        return a(p_76445_0_);
    }

    //Obfuscated method name, because this won't be processed by deobfuscators
    public synchronized static int[] a(int p_76445_0_)
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            return getIntCacheGC(p_76445_0_);
        }
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

    public synchronized static void resetIntCache()
    {
        a();
    }

    //Obfuscated method name, because this won't be processed by deobfuscators
    public synchronized static void a()
    {
        if (MapUtil.backgroundMapping(Thread.currentThread()))
        {
            resetIntCacheGC();
            return;
        }
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

    public synchronized static String getCacheSizes()
    {
        return b();
    }

    //Obfuscated method name, because this won't be processed by deobfuscators
    public synchronized static String b()
    {
        return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
    }
}