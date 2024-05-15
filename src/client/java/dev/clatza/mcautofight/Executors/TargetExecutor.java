package dev.clatza.mcautofight.Executors;

import dev.clatza.mcautofight.GlobalData;
import net.minecraft.entity.Entity;

public class TargetExecutor {
    private static Entity currentTarget = null;

    public static void setNewTarget(Entity entity) {
        if (currentTarget == entity) {
            if (GlobalData.DEBUG)
                System.out.println("[DEBUG][TargetExecutor] setNetTarget: New target == current target. Ignoring.");
        }

        currentTarget = entity;
    }

    public static Entity getTarget() {
        return currentTarget;
    }
}
