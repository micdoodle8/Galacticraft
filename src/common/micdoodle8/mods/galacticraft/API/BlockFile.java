package micdoodle8.mods.galacticraft.API;

import java.io.File;

public class BlockFile 
{
	public File file;
	private String[] readFile;
	
	private String pluginName;
	private int blockID;
	private String blockName;
	private String textureName;
	private int textureIndex;
	private float hardness;
	private float resistance;
	
	public BlockFile(File file)
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
				else if (this.readFile[i].contains("BlockID:"))
				{
					temp = this.readFile[i].replaceAll("BlockID:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.blockID = Integer.parseInt(temp);
				}
				else if (this.readFile[i].contains("BlockName:"))
				{
					temp = this.readFile[i].replaceAll("BlockName:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.blockName = temp;
				}
				else if (this.readFile[i].contains("TextureName:"))
				{
					temp = this.readFile[i].replaceAll("TextureName:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.textureName = temp;
				}
				else if (this.readFile[i].contains("IndexInTexture:"))
				{
					temp = this.readFile[i].replaceAll("IndexInTexture:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.textureIndex = Integer.valueOf(temp);
				}
				else if (this.readFile[i].contains("Hardness:"))
				{
					temp = this.readFile[i].replaceAll("Hardness:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.hardness = Float.valueOf(temp);
				}
				else if (this.readFile[i].contains("Resistance:"))
				{
					temp = this.readFile[i].replaceAll("Resistance:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.resistance = Float.valueOf(temp);
				}
			}
		}
	}
	
	public String getPluginName()
	{
		return this.pluginName;
	}
	
	public int getBlockID()
	{
		return this.blockID;
	}
	
	public String getBlockName()
	{
		return this.blockName;
	}
	
	public String getTextureName()
	{
		return this.textureName;
	}
	
	public int getTextureIndex()
	{
		return this.textureIndex;
	}
	
	public float getHardness()
	{
		return this.hardness;
	}
	
	public float getResistance()
	{
		return this.resistance;
	}
}
