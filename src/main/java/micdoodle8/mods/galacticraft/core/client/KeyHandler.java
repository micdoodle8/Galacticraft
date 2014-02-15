package micdoodle8.mods.galacticraft.core.client;

import java.util.EnumSet;

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
    public void onTick (ClientTickEvent event)
    {
//        if (event.side == Side.CLIENT)
//        {
//            if (event.phase == Phase.START)
//                keyTick(event.type, false);
//            else if (event.phase == Phase.END)
//                keyTick(event.type, true);
//        }

    }

    public void keyTick(Type type, boolean tickEnd)
    {
        for (int i = 0; i < keyBindings.length; i++)
        {
            KeyBinding keyBinding = keyBindings[i];
            int keyCode = keyBinding.getKeyCode();
            boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
            if (state != keyDown[i] || (state && repeatings[i]))
            {
                if (state)
                {
                    keyDown(type, keyBinding, tickEnd, state != keyDown[i]);
                }
                else
                {
                    keyUp(type, keyBinding, tickEnd);
                }
                if (tickEnd)
                {
                    keyDown[i] = state;
                }
            }
        }
        for (int i = 0; i < vKeyBindings.length; i++)
        {
            KeyBinding keyBinding = vKeyBindings[i];
            int keyCode = keyBinding.getKeyCode();
            boolean state = (keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode));
            if (state != keyDown[i + keyBindings.length] || (state && vRepeatings[i]))
            {
                if (state)
                {
                    keyDown(type, keyBinding, tickEnd, state != keyDown[i + keyBindings.length]);
                }
                else
                {
                    keyUp(type, keyBinding, tickEnd);
                }
                if (tickEnd)
                {
                    keyDown[i + keyBindings.length] = state;
                }
            }
        }
    }
    
    public abstract void keyDown (Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat);

    public abstract void keyUp (Type types, KeyBinding kb, boolean tickEnd);

}