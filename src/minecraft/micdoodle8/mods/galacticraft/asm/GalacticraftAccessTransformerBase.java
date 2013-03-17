package micdoodle8.mods.galacticraft.asm;

import java.io.DataInputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class GalacticraftAccessTransformerBase implements IClassTransformer
{
	private String obfuscatedName;
	private File fileLocation;
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) 
	{
        if(FMLRelauncher.side().equals("CLIENT") || FMLRelauncher.side().equals("SERVER"))
        {
			bytes = override(name, bytes, this.getObfuscatedName(), this.getLocation());
        }
        
		return bytes;
	}
	
	public static byte[] override(String name, byte[] bytes, String ObfName, File location) 
	{
		if(!name.replace('.', '/').equals(ObfName))
		{
			return bytes;
		}
		
		try
		{
			ZipFile zip = new ZipFile(location);
			ZipEntry entry = zip.getEntry(name.replace('.', '/')+".class");
			
			if(entry == null)
			{
				System.out.println(name + " not found in " + location.getName());
			}
			else
			{
				DataInputStream zin = new DataInputStream(zip.getInputStream(entry));
				bytes = new byte[(int) entry.getSize()];
				zin.readFully(bytes);
				zin.close();
				System.out.println(name + " was overriden from " + location.getName());
			}
			
			zip.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error overriding " + name + " from " + location.getName(), e);
		} 
		
		return bytes;
    }
	
	public String getObfuscatedName()
	{
		return this.obfuscatedName;
	}
	
	public void setObfuscatedName(String newName)
	{
		this.obfuscatedName = newName;
	}
	
	public File getLocation()
	{
		return this.fileLocation;
	}
	
	public void setLocation(File newLoc)
	{
		this.fileLocation = newLoc;
	}
}
