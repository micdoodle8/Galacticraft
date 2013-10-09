package codechicken.core.asm;

import java.io.DataInputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import codechicken.lib.asm.ObfMapping;

public class ClassOverrider
{
    public static byte[] overrideBytes(String name, byte[] bytes, ObfMapping classMapping, File location)
    {
        if(!classMapping.isClass(name))
            return bytes;
        
        try
        {
            ZipFile zip = new ZipFile(location);
            ZipEntry entry = zip.getEntry(classMapping.s_owner+".class");
            if(entry == null)
            {
                zip.close();
                
                if(ObfMapping.obfuscated)
                {
                    System.err.println(name+" not found in "+location.getName());
                }
                else//try and reverse runtime deobf
                {
                    String rev = MCPDeobfuscationTransformer.unmap(classMapping.s_owner);
                    if(rev != null && !rev.equals(classMapping.s_owner))
                    {
                        ObfMapping revmap = new ObfMapping(rev);
                        revmap.s_owner = rev;
                        byte[] nbytes = overrideBytes(rev.replace('/', '.'), bytes, revmap, location);
                        return MCPDeobfuscationTransformer.instance().transform(rev, null, nbytes);
                    }
                }
            }
            else
            {
                DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
                bytes = new byte[(int) entry.getSize()];
                zin.readFully(bytes);
                zip.close();
                System.out.println(name+" was overriden from "+location.getName());
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException("Error overriding "+name+" from "+location.getName(), e);
        }
        return bytes;
    }
}

    
