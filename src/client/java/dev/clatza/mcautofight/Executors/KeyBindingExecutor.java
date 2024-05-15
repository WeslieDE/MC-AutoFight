package dev.clatza.mcautofight.Executors;

import dev.clatza.mcautofight.Controllers.StateController;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingExecutor {
    private static KeyBinding toggleVariableKeybind;
    private static long keyPressTimeout = 0;
    private static boolean isGlobalAttackMode = false;

    public static void registerKeyBindings() {
        toggleVariableKeybind = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Auto klicker",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                "Auto klicker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleVariableKeybind.wasPressed()) {
                if (keyPressTimeout + 2000 < System.currentTimeMillis()) {
                    keyPressTimeout = System.currentTimeMillis();
                    isGlobalAttackMode = !isGlobalAttackMode;

                    StateController.setAutoFight(isGlobalAttackMode);
                }
            }
        });
    }

    public static void setKeyBindingPressed(KeyBinding keyBinding, boolean pressed) {
        keyBinding.setPressed(pressed);
    }
}
