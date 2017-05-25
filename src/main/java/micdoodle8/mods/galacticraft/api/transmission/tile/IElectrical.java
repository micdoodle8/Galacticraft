package micdoodle8.mods.galacticraft.api.transmission.tile;

import net.minecraft.util.EnumFacing;

/**
 * Applied to all TileEntities that can interact with electricity.
 *
 * @author Calclavia, King_Lemming
 */
public interface IElectrical extends IConnector
{
    /**
     * Adds electricity to an block. Returns the quantity of electricity that
     * was accepted. This should always return 0 if the block cannot be
     * externally charged.
     *
     * @param from         Orientation the electricity is sent in from.
     * @param receive      Maximum amount of electricity to be sent into the block.
     * @param tierProduced The tier of electricity which is being provided (must be 1 or 2)
     * @param doReceive    If false, the charge will only be simulated.
     * @return Amount of energy that was accepted by the block.
     */
    float receiveElectricity(EnumFacing from, float receive, int tierProduced, boolean doReceive);

    /**
     * Adds electricity to an block. Returns the ElectricityPack, the
     * electricity provided. This should always return null if the block cannot
     * be externally discharged.
     *
     * @param from      Orientation the electricity is requested from.
     * @param request   Maximum amount of energy to be sent into the block.
     * @param doProvide If false, the charge will only be simulated.
     * @return Amount of energy that was given out by the block.
     */
    float provideElectricity(EnumFacing from, float request, boolean doProvide);

    /**
     * @return How much energy does this TileEntity want?
     */
    float getRequest(EnumFacing direction);

    /**
     * @return How much energy does this TileEntity want to provide?
     */
    float getProvide(EnumFacing direction);

    /**
     * Gets the tier of this TileEntity.
     *
     * @return The tier, should be 1 or 2
     */
    int getTierGC();

}
