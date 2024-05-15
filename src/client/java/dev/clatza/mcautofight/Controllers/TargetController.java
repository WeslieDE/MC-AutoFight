package dev.clatza.mcautofight.Controllers;

import dev.clatza.mcautofight.Executors.TargetExecutor;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.entity.Entity;


public class TargetController {
    public static void setNewTarget(Entity entity) {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][TargetController] setNetTarget: " + entity.toString());

        MovementController.stopAllMovements();
        TargetExecutor.setNewTarget(entity);
    }

    public static Entity getTarget() {
        return TargetExecutor.getTarget();
    }
}
