package gregtechmod.api.interfaces;

/**
 * You are allowed to include this File in your Download, as i will not change it.
 * Simple Interface for Machines, which need my Machine Blocks for MultiBlockStructures.
 * 
 * Every Machine implementing this Interface will conduct Machine updates.
 */
public interface IMachineBlockUpdateable {
	/**
	 * The Machine Update, which is called when the Machine needs an Update of its Parts.
	 * I suggest to wait 1-5 seconds before actually checking the Machine Parts.
	 * RP-Frames could for example cause Problems when you instacheck the Machine Parts.
	 */
	public void onMachineBlockUpdate();
}
