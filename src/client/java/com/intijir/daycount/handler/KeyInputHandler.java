package com.intijir.daycount.handler;

import com.intijir.daycount.DayCount;
import com.intijir.daycount.config.DayCountConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_DAY_COUNT = "day_count_keybind";
    public static final String KEY_DAY_COUNT = "key.daycount.day_count";

    public static KeyBinding dayCountKey;
    private static int debounce = 5;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (dayCountKey.isPressed() && (debounce == 5)) {
                debounce--;
                DayCountConfig.INSTANCE.dayCountEnabled = !DayCountConfig.INSTANCE.dayCountEnabled;
            }
            if (debounce < 5) {
                debounce--;
            }
            if (debounce == 0) {
                debounce = 5;
            }
        });
    }

    public static void register(){
        dayCountKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(KEY_DAY_COUNT, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, new KeyBinding.Category(Identifier.of(KEY_CATEGORY_DAY_COUNT))));
        registerKeyInputs();
    }
}
