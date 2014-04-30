package codechicken.obfuscator;

import org.objectweb.asm.commons.Remapper;

import codechicken.obfuscator.ObfuscationMap.ObfuscationEntry;

public class ObfRemapper extends Remapper
{
    public final ObfuscationMap obf;
    public ObfDirection dir;
    
    public ObfRemapper(ObfuscationMap obf, ObfDirection dir)
    {
        this.obf = obf;
        this.dir = dir;
    }
    
    @Override
    public String map(String name)
    {
        if(name.indexOf('$') >= 0)
            return map(name.substring(0, name.indexOf('$')))+name.substring(name.indexOf('$'));
        
        ObfuscationEntry map;
        if(dir.obfuscate)
            map = obf.lookupMcpClass(name);
        else
            map = obf.lookupObfClass(name);

        if(map != null)
            return dir.obfuscate(map).s_owner;
        
        return name;
    }
    
    @Override
    public String mapFieldName(String owner, String name, String desc)
    {
        ObfuscationEntry map;
        if(dir.obfuscate)
            map = obf.lookupMcpField(owner, name);
        else
            map = obf.lookupObfField(owner, name);
        
        if(map == null)
            map = obf.lookupSrgField(owner, name);
        
        if(map != null)
            return dir.obfuscate(map).s_name;
        
        return name;
    }
    
    @Override
    public String mapMethodName(String owner, String name, String desc)
    {
        if(owner.length() == 0 || owner.charAt(0) == '[')
            return name;
        
        ObfuscationEntry map;
        if(dir.obfuscate)
            map = obf.lookupMcpMethod(owner, name, desc);
        else
            map = obf.lookupObfMethod(owner, name, desc);
        
        if(map == null)
            map = obf.lookupSrg(name);
        
        if(map != null)
            return dir.obfuscate(map).s_name;
        
        return name;
    }

    @Override
    public Object mapValue(Object cst)
    {
        if(cst instanceof String)
        {
            if(dir.srg_cst)
            {
                ObfuscationEntry map = obf.lookupSrg((String) cst);
                if(map != null)
                    return dir.obfuscate(map).s_name;
            }
            return cst;
        }
        
        return super.mapValue(cst);
    }
}
