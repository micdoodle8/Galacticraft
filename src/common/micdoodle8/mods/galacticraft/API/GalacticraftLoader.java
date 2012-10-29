package micdoodle8.mods.galacticraft.API;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class GalacticraftLoader 
{
	public File baseDirectory;
	public File clientDirectory;
	public int numberOfPlugins;
	public List<ProviderFile> providerFiles = new ArrayList<ProviderFile>();
	public List<ChunkProviderFile> chunkProviderFiles = new ArrayList<ChunkProviderFile>();
	public List<BlockFile> blockFiles = new ArrayList<BlockFile>();
	public List<String> spriteSheets = new ArrayList<String>();

	public Hashtable<Integer, CoreFile> coreFiles = new Hashtable<Integer, CoreFile>();
    public Hashtable<Integer, ProviderFile> dimensions = new Hashtable<Integer, ProviderFile>();
    public Hashtable<Integer, ChunkProviderFile> chunkProviders = new Hashtable<Integer, ChunkProviderFile>();
    
	public List<File> listOfPlugins = new ArrayList<File>();
	
	public void load(File file)
	{
		this.baseDirectory = new File(file, "/Galacticraft/");
		
		if (baseDirectory.exists())
		{
			FMLLog.info("Galacticraft directory found, attempting to load...");
		}
		else
		{
			FMLLog.info("Galacticraft directory not found, creating an empty directory...");
			baseDirectory.mkdirs();
			baseDirectory.mkdir();
		}
		
		File coreDirectory;
		
		coreDirectory = new File(this.baseDirectory, "/plugins/");

		if (coreDirectory.exists())
		{
			for (int i = 0; i < coreDirectory.listFiles().length; i++)
			{
				if (!coreDirectory.listFiles()[i].isDirectory())
				{
					this.numberOfPlugins++;
					this.listOfPlugins.add(coreDirectory.listFiles()[i]);
				}
			}
		}
		else
		{
			coreDirectory.mkdirs();
			coreDirectory.mkdir();
		}
		
		for (File files : this.listOfPlugins)
		{
			if (coreDirectory.listFiles() != null)
			{
				for (int i = 0; i < coreDirectory.listFiles().length; i++)
				{
					if (!coreDirectory.listFiles()[i].isDirectory())
					{
						try
						{
							CoreFile coreFile = new CoreFile(coreDirectory.listFiles()[i]);
							
							if (coreFile.getPluginID() <= 20 && coreFile.getPluginID() >= 1)
							{
								if (!this.coreFiles.containsKey(coreFile.getPluginID()))
								{
									coreFiles.put(coreFile.getPluginID(), coreFile);
									FMLLog.info("	Successfully loaded plugin with name " + coreFile.getPluginName() + " and ID " + coreFile.getPluginID());
								}
								else
								{
									FMLLog.severe("    Could not load plugin with name " + coreFile.getPluginName() + " and ID " + coreFile.getPluginID());
									FMLLog.severe("    Plugin ID is already taken!");
								}
							}
							else
							{
								FMLLog.severe("    Could not load plugin with name " + coreFile.getPluginName() + " and ID " + coreFile.getPluginID());
								FMLLog.severe("    Not enough sprite sheets to load plugin!");
							}
						}
						catch (Exception e)
						{
							FMLLog.info("		Failed to load plugin core file with name " + coreDirectory.listFiles()[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				FMLLog.info("	No plugins found! You should add some plugins to extend your intergalactic experience!");
			}
		}
		
		File providerDirectory;
		
		providerDirectory = new File(this.baseDirectory, "/providers/");

		if (providerDirectory.exists())
		{
		}
		else
		{
			providerDirectory.mkdirs();
			providerDirectory.mkdir();
		}
		
		for (File files : this.listOfPlugins)
		{
			if (providerDirectory.listFiles() != null)
			{
				for (int i = 0; i < providerDirectory.listFiles().length; i++)
				{
					if (!providerDirectory.listFiles()[i].isDirectory())
					{
						try
						{
							ProviderFile providerFile = new ProviderFile(providerDirectory.listFiles()[i]);
							this.providerFiles.add(providerFile);
							this.dimensions.put(providerFile.getDimensionID(), providerFile);
							DimensionManager.registerProviderType(providerFile.getDimensionID(), ProviderFile.class, false);
							DimensionManager.registerDimension(providerFile.getDimensionID(), providerFile.getDimensionID());
						}
						catch (Exception e)
						{
							FMLLog.info("		Failed to load plugin provider file with name " + providerDirectory.listFiles()[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		File chunkProviderDirectory = new File(this.baseDirectory, "/chunkproviders/");

		if (chunkProviderDirectory.exists())
		{
		}
		else
		{
			chunkProviderDirectory.mkdirs();
			chunkProviderDirectory.mkdir();
		}
		
		for (File files : this.listOfPlugins)
		{
			if (chunkProviderDirectory.listFiles() != null)
			{
				for (int i = 0; i < chunkProviderDirectory.listFiles().length; i++)
				{
					if (!chunkProviderDirectory.listFiles()[i].isDirectory())
					{
						try
						{
							ChunkProviderFile providerFile = new ChunkProviderFile(chunkProviderDirectory.listFiles()[i]);
							this.chunkProviderFiles.add(providerFile);
							this.chunkProviders.put(providerFile.getDimensionID(), providerFile);
						}
						catch (Exception e)
						{
							FMLLog.info("		Failed to load plugin chunk provider file with name " + chunkProviderDirectory.listFiles()[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		File blocksDirectory = new File(this.baseDirectory, "/blocks/");

		if (blocksDirectory.exists())
		{
		}
		else
		{
			blocksDirectory.mkdirs();
			blocksDirectory.mkdir();
		}
		
		for (File files : this.listOfPlugins)
		{
			if (blocksDirectory.listFiles() != null)
			{
				for (int i = 0; i < blocksDirectory.listFiles().length; i++)
				{
					if (!blocksDirectory.listFiles()[i].isDirectory())
					{
						try
						{
							BlockFile blockFile = new BlockFile(blocksDirectory.listFiles()[i]);
							this.blockFiles.add(blockFile);
						}
						catch (Exception e)
						{
							FMLLog.info("		Failed to load plugin block file with name " + blocksDirectory.listFiles()[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		for (BlockFile files : this.blockFiles)
		{
			Block block = new BlockBase(files);
			
			GameRegistry.registerBlock(block);
			
			LanguageRegistry.addName(block, files.getBlockName());
			
			// TODO harvest levels
		}
		
		File spritesDirectory = new File(this.baseDirectory, "/spritesheets/");

		if (spritesDirectory.exists())
		{
		}
		else
		{
			spritesDirectory.mkdirs();
			spritesDirectory.mkdir();
		}
		
		for (File files : this.listOfPlugins)
		{
			if (spritesDirectory.listFiles() != null)
			{
				for (int i = 0; i < spritesDirectory.listFiles().length; i++)
				{
					if (!spritesDirectory.listFiles()[i].isDirectory())
					{
						try
						{
							FMLLog.info(spritesDirectory.listFiles()[i].getPath());
							this.spriteSheets.add(spritesDirectory.listFiles()[i].getAbsolutePath());
						}
						catch (Exception e)
						{
							FMLLog.info("		Failed to load plugin block file with name " + spritesDirectory.listFiles()[i].getName());
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
