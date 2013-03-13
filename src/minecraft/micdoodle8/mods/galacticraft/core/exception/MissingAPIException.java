package micdoodle8.mods.galacticraft.core.exception;

import java.util.ArrayList;

import cpw.mods.fml.common.LoaderException;

public class MissingAPIException extends LoaderException 
{
	public ArrayList<String> missingAPIs;

	public MissingAPIException(ArrayList<String> missingAPIs) 
	{
		this.missingAPIs = missingAPIs;
	}
}
