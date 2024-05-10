package dev.clatza.mcautofight;

import dev.clatza.mcautofight.Utils.TimedRemovalList;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHelper {
    // Die Variable toggleVariableKeybind ist jetzt als static öffentlich verfügbar
    public static KeyBinding toggleVariableKeybind;

    public static void registerGameEvents() {
        toggleVariableKeybind = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toogle Autoklicker", // Übersetzungsschlüssel für die Beschreibung
                InputUtil.Type.KEYSYM, // Typ der Eingabe (KEYSYM für Tastatur)
                GLFW.GLFW_KEY_F6, // Standardtaste ist F6
                "Autoklicker" // Übersetzungsschlüssel für die Kategorie
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBindingHelper.toggleVariableKeybind.wasPressed()) {
                setGlobalAttackMode(!GlobalData.isAttacking);
            }
        });
    }

    public static void setGlobalAttackMode(boolean newStatus){
        GlobalData.isAttacking = !GlobalData.isAttacking;

        GlobalData.entityIgnoreList = new TimedRemovalList();
        GlobalData.currentTargetEntety = null;
        TeleportMonitor.detectPortals(MinecraftClient.getInstance().player.getWorld(), MinecraftClient.getInstance().player.getPos());

        KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
        setKeyBindingPressed(forwardKey, GlobalData.isAttacking);
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Auto Klicker is now " + (GlobalData.isAttacking ? "enabled" : "disabled")));

    }

    public static void setKeyBindingPressed(KeyBinding keyBinding, boolean pressed) {
        keyBinding.setPressed(pressed);
    }
}