//package micdoodle8.mods.galacticraft.API;
//
//import java.io.File;
//
//import cpw.mods.fml.common.FMLLog;
//
//public class ChunkProviderFile 
//{
//	public File file;
//	private String[] readFile;
//	
//	private String pluginName;
//	private int dimensionID;
//	
//	private int topBlockID;
//	private int middleBlockID;
//	private int fillBlockID;
//	
//	public ChunkProviderFile(File file)
//	{
//		this.file = file;
//		this.readFile = new PropertyFile().readFile(file);
//		this.processFile();
//	}
//	
//	private void processFile()
//	{
//		for (int i = 0; i < this.readFile.length; i++)
//		{
//			if (this.readFile[i] != null)
//			{
//				String temp;
//				
//				if (this.readFile[i].contains("PluginName:"))
//				{
//					temp = this.readFile[i].replaceAll("PluginName:", "");
//					temp = temp.replaceAll("\"", "");
//					temp = temp.replaceAll(" ", "");
//					this.pluginName = temp;
//				}
//				else if (this.readFile[i].contains("DimensionID:"))
//				{
//					temp = this.readFile[i].replaceAll("DimensionID:", "");
//					temp = temp.replaceAll("\"", "");
//					temp = temp.replaceAll(" ", "");
//					this.dimensionID = Integer.parseInt(temp);
//				}
//				else if (this.readFile[i].contains("TopBlockID:"))
//				{
//					temp = this.readFile[i].replaceAll("TopBlockID:", "");
//					temp = temp.replaceAll("\"", "");
//					temp = temp.replaceAll(" ", "");
//					this.topBlockID = Integer.parseInt(temp);
//				}
//				else if (this.readFile[i].contains("MiddleBlockID:"))
//				{
//					temp = this.readFile[i].replaceAll("MiddleBlockID:", "");
//					temp = temp.replaceAll("\"", "");
//					temp = temp.replaceAll(" ", "");
//					this.middleBlockID = Integer.parseInt(temp);
//				}
//				else if (this.readFile[i].contains("FillBlockID:"))
//				{
//					temp = this.readFile[i].replaceAll("FillBlockID:", "");
//					temp = temp.replaceAll("\"", "");
//					temp = temp.replaceAll(" ", "");
//					this.fillBlockID = Integer.parseInt(temp);
//				}
//			}
//		}
//	}
//	
//	public int getTopBlockID()
//	{
//		return this.topBlockID;
//	}
//	
//	public int getMidBlockID()
//	{
//		return this.middleBlockID;
//	}
//	
//	public int getFillBlockID()
//	{
//		return this.fillBlockID;
//	}
//	
//	public String getPluginName()
//	{
//		return this.pluginName;
//	}
//
//	public int getDimensionID() 
//	{
//		return this.dimensionID;
//	}
//}
