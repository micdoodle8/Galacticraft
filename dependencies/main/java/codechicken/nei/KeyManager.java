package codechicken.nei;

import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * Good old down/held/up keystate tracker
 */
public class KeyManager
{
    public static interface IKeyStateTracker
    {
        public void tickKeyStates();
    }
    
    public static class KeyState
    {
        public boolean down;
        public boolean held;
        public boolean up;
    }
    
    public static HashMap<String, KeyState> keyStates = new HashMap<String, KeyState>();
    public static LinkedList<IKeyStateTracker> trackers = new LinkedList<IKeyStateTracker>();
    
    public static void tickKeyStates()
    {
        for(Entry<String, KeyState> entry : keyStates.entrySet())
        {
            int keyCode = NEIClientConfig.getKeyBinding(entry.getKey());
            boolean down = keyCode != 0 && Keyboard.isKeyDown(keyCode);
            KeyState state = entry.getValue();
            if(down)
            {
                state.down = !state.held;
                state.up = false;
            }
            else
            {
                state.up = state.held;
                state.down = false;
            }
            state.held = down;
        }
        
        for(IKeyStateTracker tracker : trackers)
            tracker.tickKeyStates();
    }
}
