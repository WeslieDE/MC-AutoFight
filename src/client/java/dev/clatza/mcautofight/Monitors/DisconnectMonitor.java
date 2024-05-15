package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.StateController;
import dev.clatza.mcautofight.GlobalData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

public class DisconnectMonitor {
    public static Boolean listen = false;

    public DisconnectMonitor() {
        listen = true;
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> MinecraftClient.getInstance().execute(this::stop));
    }

    private void stop() {
        StateController.setAutoFight(false);
        if (GlobalData.DEBUG) System.out.println("[DEBUG][DisconnectMonitor] Disconnected from server. AutoFight stopped.");
    }

    public void tick() {
    }
}
