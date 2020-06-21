package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class KeyHandler
{
    private final KeyBinding[] keyBindings;
    private KeyBinding[] vKeyBindings;
    private boolean[] keyDown;
    private boolean[] repeatings;
    private boolean[] vRepeatings;
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            this.keyTick(event.type, false);
        }
        else if (event.phase == TickEvent.Phase.END)
        {
            this.keyTick(event.type, true);
        }
    }

    public void keyTick(TickEvent.Type type, boolean tickEnd)
    {
        boolean inChat = Minecraft.getInstance().currentScreen instanceof ChatScreen;

        for (int i = 0; i < this.keyBindings.length; i++)
        {
            KeyBinding keyBinding = this.keyBindings[i];
//            int keyCode = keyBinding.getKeyCode();
//            if (keyCode == Keyboard.KEY_NONE) continue;
            boolean state = keyBinding.isKeyDown();

//            try
//            {
//                if (!inChat)
//                {
//                    if (keyCode < 0)
//                    {
//                        keyCode += 100;
//                        state = Mouse.isButtonDown(keyCode);
//                    }
//                    else
//                    {
//                        state = Keyboard.isKeyDown(keyCode);
//                    }
//                }
//            }
//            catch (IndexOutOfBoundsException e)
//            {
//                GCLog.severe("Invalid keybinding! " + keyBinding.getKeyDescription());
//                continue;
//            }

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
//            int keyCode = keyBinding.getKeyCode();
//            if (keyCode == Keyboard.KEY_NONE) continue;
//            boolean state = keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
            boolean state = keyBinding.isKeyDown();
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

    public abstract void keyDown(TickEvent.Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat);

    public abstract void keyUp(TickEvent.Type types, KeyBinding kb, boolean tickEnd);

}