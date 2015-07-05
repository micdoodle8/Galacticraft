package micdoodle8.mods.galacticraft.core.client.objload;

/**
 * Thrown if there is a problem parsing the model
 *
 * @author cpw
 */
public class ModelFormatException extends RuntimeException {

    private static final long serialVersionUID = 2023547503969671835L;

    public ModelFormatException() {
        super();
    }

    public ModelFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelFormatException(String message) {
        super(message);
    }

    public ModelFormatException(Throwable cause) {
        super(cause);
    }

}