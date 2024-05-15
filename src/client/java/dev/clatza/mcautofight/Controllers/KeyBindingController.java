package dev.clatza.mcautofight.Controllers;

import dev.clatza.mcautofight.Executors.KeyBindingExecutor;
import net.minecraft.client.option.KeyBinding;

public class KeyBindingController {
    public static void registerKeyBindings() {
        KeyBindingExecutor.registerKeyBindings();
    }

//    public static void setKeyBindingPressed(KeyBinding keyBinding, boolean pressed) {
//        KeyBindingExecutor.setKeyBindingPressed(keyBinding, pressed);
//    }
}
