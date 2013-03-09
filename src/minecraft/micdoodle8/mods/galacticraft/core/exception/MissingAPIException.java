package micdoodle8.mods.galacticraft.core.exception;

import java.io.File;
import java.util.ArrayList;

import com.google.common.collect.SetMultimap;

import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.ModContainer;

public class MissingAPIException extends LoaderException 
{
	public ArrayList<String> missingAPIs;

	public MissingAPIException(ArrayList<String> missingAPIs) 
	{
		this.missingAPIs = missingAPIs;
	}
}
