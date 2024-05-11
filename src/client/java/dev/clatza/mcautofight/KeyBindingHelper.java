package dev.clatza.mcautofight;

import baritone.api.BaritoneAPI;
import dev.clatza.mcautofight.Utils.TimedRemovalList;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHelper {
    public static KeyBinding toggleVariableKeybind;
    public static long keyPressTimeout = 0;

    public static void registerGameEvents() {
        toggleVariableKeybind = net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Toggle Auto klicker",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F6,
                "Auto klicker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KeyBindingHelper.toggleVariableKeybind.wasPressed()) {
                if(keyPressTimeout + 200 < System.currentTimeMillis()){
                    keyPressTimeout = System.currentTimeMillis();
                    setGlobalAttackMode(!GlobalData.isAttacking);
                }
            }
        });
    }

    public static void setGlobalAttackMode(boolean newStatus){
        GlobalData.isAttacking = newStatus;

        GlobalData.entityIgnoreList = new TimedRemovalList();
        GlobalData.currentTargetEntity = null;

        if(newStatus){
            if(MinecraftClient.getInstance().player == null) return;
            TeleportMonitor.detectPortals(MinecraftClient.getInstance().player.getWorld(), MinecraftClient.getInstance().player.getPos());
            GlobalData.killCounter = 0;
        }

        if(!newStatus) BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);

        //KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
        //setKeyBindingPressed(forwardKey, GlobalData.isAttacking);

        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("Auto Klicker is now " + (GlobalData.isAttacking ? "enabled" : "disabled")));
    }

    public static void setKeyBindingPressed(KeyBinding keyBinding, boolean pressed) {
        keyBinding.setPressed(pressed);
    }
}