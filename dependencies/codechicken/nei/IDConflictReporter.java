package codechicken.nei;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;


import com.google.common.collect.HashMultimap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;

public class IDConflictReporter
{
    private static class IDConflict implements Comparable<IDConflict>
    {
        public int id;
        public String msg;
        
        public IDConflict(int id, Collection<?> values, Object[] base)
        {
            this.id = id;
            msg = id+": "+identify(base[id]);
            for(Object o : values)
                msg+=" - "+identify(o);
        }
        
        @Override
        public int compareTo(IDConflict o)
        {
            return o.id == id ? 0 : id > o.id ? 1 : -1;
        }
        
        public static String identify(Object o)
        {
            return name(o)+" from "+getContainerID(o);
        }
        
        public static String getContainerID(Object o)
        {
            ModContainer mc = containers.get(o);
            if(mc != null)
                return mc.getModId();
            return "Unknown";
        }

        public static String name(Object o)
        {
            if(o instanceof Block)
                return blockName((Block)o);
            return o.toString();
        }

        public static String blockName(Block block)
        {
            String name = block.getUnlocalizedName();
            if(name != null)
                if(name.startsWith("tile."))
                    name = name.substring(5);
            
            if(name == null || name.length() == 0)
                name = block.getClass().getName();
            
            return name;
        }
    }
    
    private static HashMultimap<Integer, Block> blockConflicts = HashMultimap.create();
    private static HashMap<Object, ModContainer> containers = new HashMap<Object, ModContainer>();
    
    public static void blockConstructed(Block block, int id)
    {
        if(Block.blocksList[id] != null)
            blockConflicts.put(id, block);
        
        bindModContainer(block);
    }
    
    private static void bindModContainer(Block block)
    {
        containers.put(block, Loader.instance().activeModContainer());
    }

    private static boolean hasConflicts()
    {
        return !blockConflicts.isEmpty();
    }
    
    public static void postInit()
    {
        if(!hasConflicts())
            return;
        
        try
        {
            File outputFile = new File("IDConflicts.txt");
            if(!outputFile.exists())
                outputFile.createNewFile();

            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            printConflicts(writer, blockConflicts, "Blocks", Block.blocksList);
            
            writer.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        
        NEIServerUtils.throwCME("A list of ID conflicts has been written to\nthe file 'IDConflicts.txt' in your minecraft directory");
    }

    private static void printConflicts(PrintWriter writer, HashMultimap<Integer, ?> conflictMap, String header, Object[] array)
    {
        if(!conflictMap.isEmpty())
        {
            writer.println("#"+header);
            
            int conflictCount = 0;
            ArrayList<IDConflict> conflicts = new ArrayList<IDConflict>(conflictMap.size());
            for(int id : conflictMap.keySet())
            {
                Collection<?> values = conflictMap.get(id);
                conflicts.add(new IDConflict(id, values, array));
                conflictCount+=values.size();
            }
            Collections.sort(conflicts);
            for(IDConflict c : conflicts)
                writer.println(c.msg);
            
            printSuggestions(writer, conflictCount, array);
        }
    }

    private static void printSuggestions(PrintWriter writer, int conflictCount, Object[] array)
    {
        ArrayList<int[]> freeRanges = new ArrayList<int[]>();
        int start = -1;
        for(int i = 0; i < array.length; i++)
        {
            boolean isFree = array[i] == null;
            if(isFree)
            {
                if(start == -1)
                    start = i;
            }
            else
            {
                if(start >= 0)
                {
                    freeRanges.add(new int[]{start, i-1});
                    start = -1;
                }
            }
        }
        if(start >= 0)
            freeRanges.add(new int[]{start, array.length-1});
        
        Collections.sort(freeRanges, new Comparator<int[]>()
        {
            @Override
            public int compare(int[] o1, int[] o2)
            {
                int a = o1[1]-o1[0];
                int b = o2[1]-o2[0];
                return a == b ? 0 : a > b ? -1 : 1;
            }
        });
        
        ArrayList<int[]> topFree = new ArrayList<int[]>();
        int nfree = 0;
        for(int i = 0; i < freeRanges.size() && (nfree < conflictCount || topFree.size() < 3); i++)
        {
            int[] range = freeRanges.get(i);
            topFree.add(range);
            nfree+=range[1]-range[0];
        }
        
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        for(int[] range : topFree)
        {
            sb.append(prefix);
            if(range[0] == range[1])
                sb.append(range[0]);
            else
                sb.append(range[0]+"-"+range[1]+" ("+(range[1]-+range[0]+1)+" IDs)");
            prefix = ", ";
        }
        writer.println("Suggested Ranges: "+sb.toString());
        if(nfree < conflictCount)
            writer.println("You don't have enough blockIDs for all these mods, try uninstalling "+modWithMostIDs(array));
    }

    private static String modWithMostIDs(Object[] array)
    {
        HashMap<ModContainer, Integer> idCount = new HashMap<ModContainer, Integer>();
        for(Object o : array)
            if(o != null)
            {
                ModContainer mc = containers.get(o);
                if(mc != null)
                {
                    Integer v = idCount.get(mc);
                    idCount.put(mc, v == null ? 1 : v+1);
                }
            }
        
        int largestCount = 0;
        ModContainer largestMod = null;
        for(Entry<ModContainer, Integer> e : idCount.entrySet())
            if(e.getValue() > largestCount)
            {
                largestCount = e.getValue();
                largestMod = e.getKey();
            }
        
        return largestMod.getModId();
    }
}
