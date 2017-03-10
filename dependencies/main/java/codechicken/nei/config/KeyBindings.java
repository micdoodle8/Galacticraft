package codechicken.nei.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyBindings {
	private static final String categoryNameGui = "NEI (Not Enough Items) Gui";
	private static final String categoryNameWorld = "NEI (Not Enough Items) World";
	private static Map<String, KeyBinding> keyBindings = new HashMap<String, KeyBinding>();
	private static List<KeyBinding> keyBindingsToRegister = new ArrayList<KeyBinding>();

	static {
//		setDefaultKeyBinding("nei.options.keys.gui.recipe", JEIIntegrationManager.getShowRecipes());
//		setDefaultKeyBinding("nei.options.keys.gui.usage", JEIIntegrationManager.getShowUses());
//		setDefaultKeyBinding("nei.options.keys.gui.back", JEIIntegrationManager.getRecipeBack());
		setDefaultKeyBinding("nei.options.keys.gui.enchant", KeyConflictContext.GUI, KeyModifier.NONE, Keyboard.KEY_X, categoryNameGui);
		setDefaultKeyBinding("nei.options.keys.gui.potion", KeyConflictContext.GUI, KeyModifier.NONE, Keyboard.KEY_P, categoryNameGui);
		setDefaultKeyBinding("nei.options.keys.gui.prev", KeyConflictContext.GUI, KeyModifier.NONE, Keyboard.KEY_PRIOR, categoryNameGui);
		setDefaultKeyBinding("nei.options.keys.gui.next", KeyConflictContext.GUI, KeyModifier.NONE, Keyboard.KEY_NEXT, categoryNameGui);
//		setDefaultKeyBinding("nei.options.keys.gui.hide", JEIIntegrationManager.getToggleOverlay());
//		setDefaultKeyBinding("nei.options.keys.gui.search", JEIIntegrationManager.getFocusSearch());

		setDefaultKeyBinding("nei.options.keys.world.chunkoverlay", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_F9, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.moboverlay", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_F7, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.dawn", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.noon", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.dusk", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.midnight", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.rain", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.heal", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
		setDefaultKeyBinding("nei.options.keys.world.creative", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_NONE, categoryNameWorld);
	}

	public static KeyBinding get(String string) {
		KeyBinding keyBinding = keyBindings.get(string);
		if (keyBinding == null) {
			throw new IllegalArgumentException("There is no key binding for " + string);
		}
		return keyBinding;
	}

	@Deprecated
	public static void setDefaultKeyBinding(String string, int key) {
		KeyBinding keyBinding = new KeyBinding(string, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, key, "misc");
		keyBindings.put(string, keyBinding);
		keyBindingsToRegister.add(keyBinding);
	}

	/**
	 * Add a new keybinding to get registered
	 */
	public static void setDefaultKeyBinding(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode, String category) {
		KeyBinding keyBinding = new KeyBinding(description, keyConflictContext, keyModifier, keyCode, category);
		keyBindings.put(description, keyBinding);
		keyBindingsToRegister.add(keyBinding);
	}

	/**
	 * Add a keybinding that is already being registered somewhere else
	 */
	public static void setDefaultKeyBinding(String description, KeyBinding keyBinding) {
		keyBindings.put(description, keyBinding);
	}

	public static void register() {
		for (KeyBinding keyBinding : keyBindingsToRegister) {
			ClientRegistry.registerKeyBinding(keyBinding);
		}
	}
}
