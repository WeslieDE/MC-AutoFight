package dev.clatza.mcautofight;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class TeleportMonitor {
    private static Vec3d lastPosition = Vec3d.ZERO;

    public static void registerGameEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(!GlobalData.isAttacking) return;
            if (MinecraftClient.getInstance().player == null) return;

            if(lastPosition == Vec3d.ZERO)
                lastPosition = MinecraftClient.getInstance().player.getPos();

            if(MinecraftClient.getInstance().player.getPos().distanceTo(lastPosition) > 20){
                System.out.println("Teleport detected");
                GlobalData.isAttacking = false;

                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBindingHelper.setKeyBindingPressed(forwardKey, false);
                client.inGameHud.getChatHud().addMessage(Text.literal("Auto Klicker is now " + (GlobalData.isAttacking ? "enabled" : "disabled")));
            }

            lastPosition = MinecraftClient.getInstance().player.getPos();

        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            MinecraftClient.getInstance().execute(() -> {
                GlobalData.isAttacking = false;

                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBindingHelper.setKeyBindingPressed(forwardKey, false);
                System.out.println("Start moving forward; distance: verbindung getrennt");
            });
        });
    }
}
