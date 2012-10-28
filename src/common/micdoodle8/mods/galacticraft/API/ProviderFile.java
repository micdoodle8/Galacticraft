package micdoodle8.mods.galacticraft.API;

import java.io.File;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GCMarsChunkProvider;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.Vec3;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldProviderEnd;
import net.minecraft.src.WorldProviderHell;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ProviderFile extends GalacticraftWorldProvider
{
	public File file;
	private String[] readFile;
	
	private String pluginName;
	private String dimensionName;
	private int dimensionID;
	private boolean sunset;
	private boolean fog;
	private float fogRed;
	private float fogGreen;
	private float fogBlue;
	private float sunAngleMultiplier;
	private boolean skyColored;
	private String joinMessage;
	private String leaveMessage;
	
	public ProviderFile()
	{
		this.dimensionId = this.dimensionID;
	}
	
	public ProviderFile(File file)
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
				else if (this.readFile[i].contains("DimensionName:"))
				{
					temp = this.readFile[i].replaceAll("DimensionName:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					FMLLog.info(temp);
					this.dimensionName = temp;
				}
				else if (this.readFile[i].contains("DimensionID:"))
				{
					temp = this.readFile[i].replaceAll("DimensionID:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.dimensionID = Integer.parseInt(temp);
				}
				else if (this.readFile[i].contains("Sunset:"))
				{
					temp = this.readFile[i].replaceAll("Sunset:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.sunset = Boolean.parseBoolean(temp);
				}
				else if (this.readFile[i].contains("Fog:"))
				{
					temp = this.readFile[i].replaceAll("Fog:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.fog = Boolean.parseBoolean(temp);
				}
				else if (this.readFile[i].contains("FogColorRed:"))
				{
					temp = this.readFile[i].replaceAll("FogColorRed:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.fogRed = Float.parseFloat(temp);
				}
				else if (this.readFile[i].contains("FogColorGreen:"))
				{
					temp = this.readFile[i].replaceAll("FogColorGreen:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.fogGreen = Float.parseFloat(temp);
				}
				else if (this.readFile[i].contains("FogColorBlue:"))
				{
					temp = this.readFile[i].replaceAll("FogColorBlue:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.fogBlue = Float.parseFloat(temp);
				}
				else if (this.readFile[i].contains("DaySpeed:"))
				{
					temp = this.readFile[i].replaceAll("DaySpeed:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.sunAngleMultiplier = Float.parseFloat(temp);
				}
				else if (this.readFile[i].contains("IsSkyColored:"))
				{
					temp = this.readFile[i].replaceAll("IsSkyColored:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.skyColored = Boolean.parseBoolean(temp);
				}
				else if (this.readFile[i].contains("JoinMessage:"))
				{
					temp = this.readFile[i].replaceAll("JoinMessage:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.joinMessage = String.valueOf(temp);
				}
				else if (this.readFile[i].contains("LeaveMessage:"))
				{
					temp = this.readFile[i].replaceAll("LeaveMessage:", "");
					temp = temp.replaceAll("\"", "");
					temp = temp.replaceAll(" ", "");
					this.leaveMessage = String.valueOf(temp);
				}
			}
		}
	}

	@Override
    public float[] calcSunriseSunsetColors(float var1, float var2)
    {
		if (GalacticraftCore.loader.dimensions.get(this.dimensionId).sunset)
		{
			return super.calcSunriseSunsetColors(var1, var2);
		}
		else
		{
			return null;
		}
    }
    
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int par1, int par2)
    {
		if (GalacticraftCore.loader.dimensions.get(this.dimensionId).sunset)
		{
			return true;
		}
		else
		{
			return false;
		}
    }

	@SideOnly(Side.CLIENT)
	@Override
    public Vec3 getFogColor(float var1, float var2)
    {
        return this.worldObj.func_82732_R().getVecFromPool((double)GalacticraftCore.loader.dimensions.get(this.dimensionId).fogRed / 255F, (double)GalacticraftCore.loader.dimensions.get(this.dimensionId).fogGreen / 255F, (double)GalacticraftCore.loader.dimensions.get(this.dimensionId).fogBlue / 255F);
    }
	
	@Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        int var4 = (int)(par1 % 24000L);
        float var5 = ((float)var4 + par3) / 24000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5 * GalacticraftCore.loader.dimensions.get(this.dimensionId).sunAngleMultiplier;
    }

	@Override
    public IChunkProvider getChunkProvider()
    {
		ChunkProviderFile chunkFile = GalacticraftCore.loader.chunkProviders.get(this.dimensionId);
		
		if (String.valueOf(chunkFile.getPluginName()).equals(String.valueOf(GalacticraftCore.loader.dimensions.get(this.dimensionId).getPluginName())))
		{
			return new ChunkProviderBase(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled(), chunkFile);
		}
		else
		{
			FMLLog.severe("Chunk provider file didn't match with world provider's plugin name");
			return null;
		}
    }
    
    @Override
    public String getSaveFolder()
    {
    	return "DIM" + GalacticraftCore.loader.dimensions.get(this.dimensionId).dimensionID;
    }

    @Override
    public String getWelcomeMessage()
    {
        return GalacticraftCore.loader.dimensions.get(this.dimensionId).joinMessage;
    }

    @Override
    public String getDepartMessage()
    {
        return GalacticraftCore.loader.dimensions.get(this.dimensionId).leaveMessage;
    }

	@Override
    public boolean isSkyColored()
    {
        return GalacticraftCore.loader.dimensions.get(this.dimensionId).skyColored;
    }
	
	public String getPluginName()
	{
		return this.pluginName;
	}

	public int getDimensionID() 
	{
		return this.dimensionID;
	}

	@Override
	public String getDimensionName() 
	{
		return GalacticraftCore.loader.dimensions.get(this.dimensionId).dimensionName;
	}
}
