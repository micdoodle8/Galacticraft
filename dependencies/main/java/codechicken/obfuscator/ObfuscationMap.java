package codechicken.obfuscator;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;

import codechicken.lib.asm.ObfMapping;

public class ObfuscationMap
{
    public class ObfuscationEntry
    {
        public final ObfMapping obf;
        public final ObfMapping srg;
        public final ObfMapping mcp;
        
        public ObfuscationEntry(ObfMapping obf, ObfMapping srg, ObfMapping mcp)
        {
            this.obf = obf;
            this.srg = srg;
            this.mcp = mcp;
        }
    }
    
    private class ClassEntry extends ObfuscationEntry
    {
        public Map<String, ObfuscationEntry> mcpMap = new HashMap<String, ObfuscationEntry>();
        public Map<String, ObfuscationEntry> srgMap = new HashMap<String, ObfuscationEntry>();
        public Map<String, ObfuscationEntry> obfMap = new HashMap<String, ObfuscationEntry>();
        
        public ClassEntry(String obf, String srg)
        {
            super(new ObfMapping(obf, "", ""), 
                    new ObfMapping(srg, "", ""), 
                    new ObfMapping(srg, "", ""));
        }
        
        public ObfuscationEntry addEntry(ObfMapping obf_desc, ObfMapping srg_desc)
        {
            ObfuscationEntry entry = new ObfuscationEntry(obf_desc, srg_desc, srg_desc.copy());
            obfMap.put(obf_desc.s_name.concat(obf_desc.s_desc), entry);
            srgMap.put(srg_desc.s_name, entry);
            
            if(srg_desc.s_name.startsWith("field_") || srg_desc.s_name.startsWith("func_"))
                srgMemberMap.put(srg_desc.s_name, entry);
            
            return entry;
        }

        public void inheritFrom(ClassEntry p)
        {
            inherit(obfMap, p.obfMap);
            inherit(srgMap, p.srgMap);
            inherit(mcpMap, p.mcpMap);
        }
        
        private void inherit(Map<String, ObfuscationEntry> child, Map<String, ObfuscationEntry> parent)
        {
            for(Entry<String, ObfuscationEntry> e : parent.entrySet())
                if(!child.containsKey(e.getKey()))
                    child.put(e.getKey(), e.getValue());
        }
    }
    
    private Map<String, ClassEntry> srgMap = new HashMap<String, ClassEntry>();
    private Map<String, ClassEntry> obfMap = new HashMap<String, ClassEntry>();
    private ArrayListMultimap<String, ObfuscationEntry> srgMemberMap = ArrayListMultimap.create();
    
    private IHeirachyEvaluator heirachyEvaluator;
    private HashSet<String> mappedClasses = new HashSet<String>();
    public ILogStreams log = SystemLogStreams.inst;
    
    public ObfuscationMap setHeirachyEvaluator(IHeirachyEvaluator eval)
    {
        heirachyEvaluator = eval;
        return this;
    }
    
    public ObfuscationMap setLog(ILogStreams log)
    {
        this.log = log;
        return this;
    }
    
    public ObfuscationEntry addClass(String obf, String srg)
    {
        return addEntry(new ObfMapping(obf, "", ""), new ObfMapping(srg, "", ""));
    }
    
    public ObfuscationEntry addField(String obf_owner, String obf_name, String srg_owner, String srg_name)
    {
        return addEntry(new ObfMapping(obf_owner, obf_name, ""), 
                new ObfMapping(srg_owner, srg_name, ""));
    }
    
    public ObfuscationEntry addMethod(String obf_owner, String obf_name, String obf_desc, String srg_owner, String srg_name, String srg_desc)
    {
        return addEntry(new ObfMapping(obf_owner, obf_name, obf_desc), 
                new ObfMapping(srg_owner, srg_name, srg_desc));
    }
    
    public ObfuscationEntry addEntry(ObfMapping obf_desc, ObfMapping srg_desc)
    {
        ClassEntry e = srgMap.get(srg_desc.s_owner);
        if(e == null)
        {
            e = new ClassEntry(obf_desc.s_owner, srg_desc.s_owner);
            obfMap.put(obf_desc.s_owner, e);
            srgMap.put(srg_desc.s_owner, e);
        }
        if(obf_desc.s_name.length() > 0)
            return e.addEntry(obf_desc, srg_desc);
        
        return e;
    }
    
    public void addMcpName(String srg_name, String mcp_name)
    {
        List<ObfuscationEntry> entries = srgMemberMap.get(srg_name);
        if(entries.isEmpty())
        {
            log.err().println("Tried to add mcp name ("+mcp_name+") for unknown srg key ("+srg_name+")");
            return;
        }
        for(ObfuscationEntry entry : entries)
        {
            entry.mcp.s_name = mcp_name;
            srgMap.get(entry.srg.s_owner).mcpMap.put(entry.mcp.s_name.concat(entry.mcp.s_desc), entry);
        }
    }
    
    public ObfuscationEntry lookupSrg(String srg_key)
    {
        List<ObfuscationEntry> list = srgMemberMap.get(srg_key);
        return list.isEmpty() ? null : list.get(0);
    }
    
    public ObfuscationEntry lookupMcpClass(String name)
    {
        return srgMap.get(name);
    }

    public ObfuscationEntry lookupObfClass(String name)
    {
        return obfMap.get(name);
    }

    public ObfuscationEntry lookupMcpField(String owner, String name)
    {
        return lookupMcpMethod(owner, name, "");
    }

    public ObfuscationEntry lookupSrgField(String owner, String name)
    {
        if(name.startsWith("field_"))
        {
            ObfuscationEntry e = lookupSrg(name);
            if(e != null)
                return e;
        }

        evaluateHeirachy(owner);
        ClassEntry e = srgMap.get(owner);
        return e == null ? null : e.srgMap.get(name);
    }

    public ObfuscationEntry lookupObfField(String owner, String name)
    {
        return lookupObfMethod(owner, name, "");
    }

    public ObfuscationEntry lookupMcpMethod(String owner, String name, String desc)
    {
        evaluateHeirachy(owner);
        ClassEntry e = srgMap.get(owner);
        return e == null ? null : e.mcpMap.get(name.concat(desc));
    }

    public ObfuscationEntry lookupObfMethod(String owner, String name, String desc)
    {
        evaluateHeirachy(owner);
        ClassEntry e = obfMap.get(owner);
        return e == null ? null : e.obfMap.get(name.concat(desc));
    }
    
    private boolean isMapped(ObfuscationEntry desc)
    {
        return mappedClasses.contains(desc.srg.s_owner);
    }
    
    private ObfuscationEntry getOrCreateClassEntry(String name)
    {
        ObfuscationEntry e = lookupObfClass(name);
        if(e == null)
            e = lookupMcpClass(name);
        if(e == null)
            e = addClass(name, name);//if the class isn't in obf or srg maps, it must be a custom mod class with no name change.
        return e;
    }

    public ObfuscationEntry evaluateHeirachy(String name)
    {
        ObfuscationEntry desc = getOrCreateClassEntry(name);
        if(isMapped(desc))
            return desc;

        mappedClasses.add(desc.srg.s_owner);
        if(heirachyEvaluator == null)
            throw new IllegalArgumentException("Cannot call method/field mappings if a heirachy evaluator is not set.");
        
        if(!heirachyEvaluator.isLibClass(desc))
        {
            List<String> parents = heirachyEvaluator.getParents(desc);
            if(parents == null)
                log.err().println("Could not find class: "+desc.srg.s_owner);
            else
                for(String parent : parents)
                    inherit(desc, evaluateHeirachy(parent));
        }
        
        return desc;
    }

    public void inherit(ObfuscationEntry desc, ObfuscationEntry p_desc)
    {
        inherit(desc.srg.s_owner, p_desc.srg.s_owner);
    }

    public void inherit(String name, String parent)
    {
        ClassEntry e = srgMap.get(name);
        if(e == null)
            throw new IllegalStateException("Tried to inerit to an undefined class: "+name+" extends "+parent);
        ClassEntry p = srgMap.get(parent);
        if(p == null)
            throw new IllegalStateException("Tried to inerit from undefired parent: "+name+" extends "+parent);
        e.inheritFrom(p);
    }
    
    public void parseMappings(File[] mappings)
    {
        parseSRGS(mappings[0]);
        parseCSV(mappings[1]);
        parseCSV(mappings[2]);
    }
    
    public static String[] splitLast(String s, char c)
    {
        int i = s.lastIndexOf(c);
        return new String[]{s.substring(0, i), s.substring(i+1)};
    }
    
    public static String[] split4(String s, char c)
    {
        String[] split = new String[4];
        int i2 = s.indexOf(c);
        split[0] = s.substring(0, i2);
        int i = i2+1;
        i2 = s.indexOf(c, i);
        split[1] = s.substring(i, i2);
        i = i2+1;
        i2 = s.indexOf(c, i);
        split[2] = s.substring(i, i2);
        i = i2+1;
        i2 = s.indexOf(c, i);
        split[3] = s.substring(i);
        return split;
    }

    private void parseSRGS(File srgs)
    {
        log.out().println("Parsing "+srgs.getName());
        
        Function<String, Void> function = new Function<String, Void>()
        {
            @Override
            public Void apply(String line)
            {
                int hpos = line.indexOf('#');
                if(hpos > 0)
                    line = line.substring(0, hpos).trim();
                if(line.startsWith("CL: "))
                {
                    String[] params = splitLast(line.substring(4), ' ');
                    addClass(params[0], params[1]);
                }
                else if(line.startsWith("FD: "))
                {
                    String[] params = splitLast(line.substring(4), ' ');
                    String[] p1 = splitLast(params[0], '/');
                    String[] p2 = splitLast(params[1], '/');
                    addField(p1[0], p1[1], 
                            p2[0], p2[1]);
                    return null;
                }
                else if(line.startsWith("MD: "))
                {
                    String[] params = split4(line.substring(4), ' ');
                    String[] p1 = splitLast(params[0], '/');
                    String[] p2 = splitLast(params[2], '/');
                    addMethod(p1[0], p1[1], params[1], 
                            p2[0], p2[1], params[3]);
                    return null;
                }
                return null;
            }
        };
        
        ObfuscationRun.processLines(srgs, function);
    }

    private void parseCSV(File csv)
    {
        log.out().println("Parsing "+csv.getName());
        
        Function<String, Void> function = new Function<String, Void>()
        {
            @Override
            public Void apply(String line)
            {
                if(line.startsWith("func_") || line.startsWith("field_"))
                {
                    int i = line.indexOf(',');
                    String srg = line.substring(0, i);
                    int i2 = i+1;
                    i = line.indexOf(',', i2);
                    String mcp = line.substring(i2, i);
                    
                    addMcpName(srg, mcp);
                }
                return null;
            }
        };
        
        ObfuscationRun.processLines(csv, function);
    }
}
