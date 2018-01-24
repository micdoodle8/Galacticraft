package mekanism.api.gas;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GasRegistry
{
	private static ArrayList<Gas> registeredGasses = new ArrayList<>();

	private static Logger LOG = LogManager.getLogger("Mekanism GasRegistry");

	/**
	 * Register a new gas into GasRegistry. Call this BEFORE post-init.
	 * @param gas - Gas to register
	 * @return the gas that has been registered, pulled right out of GasRegistry
	 */
	public static Gas register(Gas gas)
	{
		if(gas == null)
		{
			return null;
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
			if (hasAlreadyStitched()){
				gas.updateIcon(Minecraft.getMinecraft().getTextureMapBlocks());
				if (gas.getSpriteRaw() == null){
					LOG.error("Gas {} registered post texture stitch without valid sprite!", gas.getName());
				}
			}
		}

		registeredGasses.add(gas);

		return getGas(gas.getName());
	}

	/**
	 * Gets the gas associated with the defined ID.
	 * @param id - ID to check
	 * @return gas associated with defined ID
	 */
	public static Gas getGas(int id)
	{
		if(id == -1)
		{
			return null;
		}

		return registeredGasses.get(id);
	}

	/**
	 * Gets the gas associated with the defined fluid.
	 * @param f - fluid to check
	 * @return the gas associated with the fluid
	 */
	public static Gas getGas(Fluid f)
	{
		for(Gas gas : getRegisteredGasses())
		{
			if(gas.hasFluid() && gas.getFluid() == f)
			{
				return gas;
			}
		}

		return null;
	}

	/**
	 * Whether or not GasRegistry contains a gas with the specified name
	 * @param name - name to check
	 * @return if GasRegistry contains a gas with the defined name
	 */
	public static boolean containsGas(String name)
	{
		return getGas(name) != null;
	}

	/**
	 * Gets the list of all gasses registered in GasRegistry.
	 * @return a cloned list of all registered gasses
	 */
	public static List<Gas> getRegisteredGasses()
	{
		return new ArrayList<>(registeredGasses);
	}

	/**
	 * Gets the gas associated with the specified name.
	 * @param name - name of the gas to get
	 * @return gas associated with the name
	 */
	public static Gas getGas(String name)
	{
		for(Gas gas : registeredGasses)
		{
			if(gas.getName().toLowerCase().equals(name.toLowerCase()))
			{
				return gas;
			}
		}

		return null;
	}

	/**
	 * Gets the gas ID of a specified gas.
	 * @param gas - gas to get the ID from
	 * @return gas ID
	 */
	public static int getGasID(Gas gas)
	{
		if(gas == null || !containsGas(gas.getName()))
		{
			return -1;
		}

		return registeredGasses.indexOf(gas);
	}

	private static boolean hasAlreadyStitched(){
		return Loader.instance().getLoaderState().ordinal() > LoaderState.PREINITIALIZATION.ordinal();
	}
}
