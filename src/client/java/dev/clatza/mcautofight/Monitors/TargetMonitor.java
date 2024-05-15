package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.EntitySearchController;
import dev.clatza.mcautofight.Controllers.TargetController;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.entity.Entity;

public class TargetMonitor {
    public void tick() {
        if(TargetController.getTarget() == null) {setNewTarget("Null"); return;}
        if(TargetController.getTarget().isRemoved()) {setNewTarget("isRemoved"); }
    }

    private void setNewTarget(String reason){
        if (GlobalData.DEBUG) System.out.println("[DEBUG][TargetMonitor] Seartch for new target: " + reason);

        Entity newTarget = EntitySearchController.getClosestEntity();
        TargetController.setNewTarget(newTarget);
    }
}
