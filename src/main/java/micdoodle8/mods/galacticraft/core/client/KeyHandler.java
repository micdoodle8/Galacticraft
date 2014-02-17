package micdoodle8.mods.galacticraft.core.client;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;

public abstract class KeyHandler
{
	public KeyBinding[] keyBindings;
	public KeyBinding[] vKeyBindings;
	public boolean[] keyDown;
	public boolean[] repeatings;
	public boolean[] vRepeatings;
	public boolean isDummy;

	public KeyHandler(KeyBinding[] keyBindings, boolean[] repeatings, KeyBinding[] vanillaKeys, boolean[] vanillaRepeatings)
	{
		assert keyBindings.length == repeatings.length : "You need to pass two arrays of identical length";
		assert vanillaKeys.length == vanillaRepeatings.length : "You need to pass two arrays of identical length";
		this.keyBindings = keyBindings;
		this.repeatings = repeatings;
		this.vKeyBindings = vanillaKeys;
		this.vRepeatings = vanillaRepeatings;
		this.keyDown = new boolean[keyBindings.length + vanillaKeys.length];
	}

	public KeyHandler(KeyBinding[] keyBindings)
	{
		this.keyBindings = keyBindings;
		this.isDummy = true;
	}

	public KeyBinding[] getKeyBindings()
	{
		return this.keyBindings;
	}

	@SubscribeEvent
	public void onTick(ClientTickEvent event)
	{
		if (event.side == Side.CLIENT)
		{
			if (event.phase == Phase.START)
			{
				this.keyTick(event.type, false);
			}
			else if (event.phase == Phase.END)
			{
				this.keyTick(event.type, true);
			}
		}

	}

	public void keyTick(Type type, boolean tickEnd)
	{
		for (int i = 0; i < this.keyBindings.length; i++)
		{
			KeyBinding keyBinding = this.keyBindings[i];
			int keyCode = keyBinding.getKeyCode();
			boolean state = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
			if (state != this.keyDown[i] || state && this.repeatings[i])
			{
				if (state)
				{
					this.keyDown(type, keyBinding, tickEnd, state != this.keyDown[i]);
				}
				else
				{
					this.keyUp(type, keyBinding, tickEnd);
				}
				if (tickEnd)
				{
					this.keyDown[i] = state;
				}
			}
		}
		for (int i = 0; i < this.vKeyBindings.length; i++)
		{
			KeyBinding keyBinding = this.vKeyBindings[i];
			int keyCode = keyBinding.getKeyCode();
			boolean state = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
			if (state != this.keyDown[i + this.keyBindings.length] || state && this.vRepeatings[i])
			{
				if (state)
				{
					this.keyDown(type, keyBinding, tickEnd, state != this.keyDown[i + this.keyBindings.length]);
				}
				else
				{
					this.keyUp(type, keyBinding, tickEnd);
				}
				if (tickEnd)
				{
					this.keyDown[i + this.keyBindings.length] = state;
				}
			}
		}
	}

	public abstract void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat);

	public abstract void keyUp(Type types, KeyBinding kb, boolean tickEnd);

}