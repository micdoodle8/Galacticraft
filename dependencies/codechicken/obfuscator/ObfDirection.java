package codechicken.obfuscator;

import codechicken.lib.asm.ObfMapping;
import codechicken.obfuscator.ObfuscationMap.ObfuscationEntry;

public class ObfDirection
{
    public boolean obfuscate;
    public boolean srg;
    public boolean srg_cst;
    
    public ObfDirection setObfuscate(boolean obfuscate)
    {
        this.obfuscate = obfuscate;
        return this;
    }
    
    public ObfDirection setSearge(boolean srg)
    {
        this.srg = srg;
        return this;
    }

    public ObfDirection setSeargeConstants(boolean srg_cst)
    {
        this.srg_cst = srg_cst;
        return this;
    }

    public ObfMapping obfuscate(ObfuscationEntry map)
    {
        return srg ? map.srg : obfuscate ? map.obf : map.mcp;
    }
}
