package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.StateController;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class TeleportMonitor {
    private static Vec3d lastPosition = Vec3d.ZERO;

    public void tick() {
        if (MinecraftClient.getInstance().player == null) return;

        if(lastPosition == Vec3d.ZERO)
            lastPosition = MinecraftClient.getInstance().player.getPos();

        if(MinecraftClient.getInstance().player.getPos().distanceTo(lastPosition) > 80){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][TeleportMonitor] Teleport detected!");

            StateController.setAutoFight(false);
        }

        lastPosition = MinecraftClient.getInstance().player.getPos();
    }
}
