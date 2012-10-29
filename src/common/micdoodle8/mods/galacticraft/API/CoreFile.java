package micdoodle8.mods.galacticraft.API;

import java.io.File;

import cpw.mods.fml.common.FMLLog;

public class CoreFile 
{
	public File file;
	private String pluginName;
	private int pluginID;
	private int dimensionID;
	private String[] readFile;
	
	public CoreFile(File file)
	{
		this.file = file;
		this.readFile = new PropertyFile().readFile(file);
		this.processFile();
	}
	
	private void processFile()
	{
		for (int i = 0; i < this.readFile.length; i++)
		{
			if (this.readFile[i] != null)
			{
				String temp;
				
				if (this.readFile[i].contains("PluginName:"))
				{
					temp = this.readFile[i].replaceAll("PluginName:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.pluginName = temp;
				}
				else if (this.readFile[i].contains("PluginID:"))
				{
					temp = this.readFile[i].replaceAll("PluginID:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.pluginID = Integer.valueOf(temp);
				}
				else if (this.readFile[i].contains("DimensionID:"))
				{
					temp = this.readFile[i].replaceAll("DimensionID:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.dimensionID = Integer.parseInt(temp);
				}
			}
		}
	}
	
	public String getPluginName()
	{
		return this.pluginName;
	}
	
	public int getDimensionID()
	{
		return this.dimensionID;
	}
	
	public int getPluginID()
	{
		return this.pluginID;
	}
}
