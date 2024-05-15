package dev.clatza.mcautofight.Monitors;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import dev.clatza.mcautofight.Controllers.EntitySearchController;
import dev.clatza.mcautofight.Controllers.MovementController;
import dev.clatza.mcautofight.Controllers.TargetController;
import dev.clatza.mcautofight.Data.MovementType;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class WalkToTargetMonitor {
    private String currentEntityName = "";
    private Vec3d PathFindingGoal = Vec3d.ZERO;
    private float lastMovingStartAt;

    public WalkToTargetMonitor() {
        BaritoneAPI.getSettings().allowSprint.value = true;
        BaritoneAPI.getSettings().allowBreak.value = false;
        BaritoneAPI.getSettings().allowInventory.value = false;
        BaritoneAPI.getSettings().allowParkourPlace.value = false;
        BaritoneAPI.getSettings().allowDiagonalDescend.value = false;
        BaritoneAPI.getSettings().allowDiagonalAscend.value = false;
        BaritoneAPI.getSettings().allowPlace.value = false;
        BaritoneAPI.getSettings().enterPortal.value = false;
        BaritoneAPI.getSettings().allowWalkOnBottomSlab.value = true;
        BaritoneAPI.getSettings().autoTool.value = false;
        BaritoneAPI.getSettings().freeLook.value = false;
        BaritoneAPI.getSettings().planningTickLookahead.value = 60;
        BaritoneAPI.getSettings().pathRenderLineWidthPixels.value = 5.0F;
        BaritoneAPI.getSettings().renderGoal.value = false;
        BaritoneAPI.getSettings().antiCheatCompatibility.value = true;
        BaritoneAPI.getSettings().jumpPenalty.value = 0.0;
        BaritoneAPI.getSettings().maxFallHeightNoWater.value = 16;

        BaritoneAPI.getSettings().primaryTimeoutMS.value = 2000L;

        lastMovingStartAt = System.currentTimeMillis();
    }

    public void tick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) return;
        if(TargetController.getTarget() == null) return;
        if (!TargetController.getTarget().isAlive()) return;
        if (TargetController.getTarget().isRemoved()) return;

        double distanceToEntity = player.getPos().distanceTo(TargetController.getTarget().getPos());
        double distanceToTarget = player.getPos().distanceTo(PathFindingGoal);;

        //We have a new target. Stop all movements and start moving to the new target
        if(!TargetController.getTarget().getEntityName().equals(currentEntityName)){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] New target found. Stopping all movements and start moving to the new target.");

            lastMovingStartAt = System.currentTimeMillis();
            currentEntityName = TargetController.getTarget().getEntityName();
            MovementController.stopAllMovements();
        }

        //The entity is more then 10 blocks away. Start moving with pathfinding.
        if(distanceToEntity >= 10 && (MovementController.getMovementType() == MovementType.STRAIGHT || MovementController.getMovementType() == MovementType.NONE)){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] The entity is more then 10 blocks away. Start moving with pathfinding.");

            lastMovingStartAt = System.currentTimeMillis();
            PathFindingGoal = TargetController.getTarget().getPos();

            MovementController.stopAllMovements();
            MovementController.startPathfinding(new GoalXZ((int)PathFindingGoal.x, (int)PathFindingGoal.z));
            return;
        }

        //Pathfinding is done but the entity is still more as 10 blocks away.
        if(distanceToTarget >= 5 && distanceToEntity >= 10 && MovementController.getMovementType() == MovementType.PATHFINDING ){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] Pathfinding is done but the entity is still more as 10 blocks away.");

            lastMovingStartAt = System.currentTimeMillis();
            PathFindingGoal = TargetController.getTarget().getPos();

            MovementController.stopAllMovements();
            MovementController.startPathfinding(new GoalXZ((int)PathFindingGoal.x, (int)PathFindingGoal.z));
            return;
        }

        //The entity is less then 10 blocks away. Start moving straight to the entity.
        if(distanceToEntity >= 10 && MovementController.getMovementType() == MovementType.PATHFINDING ){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] The entity is less then 10 blocks away. Start moving straight to the entity.");

            lastMovingStartAt = System.currentTimeMillis();
            MovementController.stopPathfinding();
            MovementController.moveToDirection(TargetController.getTarget().getPos());
            MovementController.startMovementForward();
            return;
        }

        //The entity is less as 2 blocks away. Stop all movements.
        if(distanceToEntity < 2 && MovementController.getMovementType() == MovementType.STRAIGHT ){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] The entity is less as 2 blocks away. Stop all movements.");

            lastMovingStartAt = System.currentTimeMillis();
            MovementController.stopAllMovements();
            return;
        }

        //The entity is more then 2 blocks away. Start moving straight to the entity.
        if(distanceToEntity > 2 && MovementController.getMovementType() == MovementType.NONE){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] The entity is more as 2 blocks away. Start moving straight to the entity.");

            lastMovingStartAt = System.currentTimeMillis();
            MovementController.startMovementForward();
            return;
        }

        //Failsafe. If we dont move for 30 seconds, stop all movements and get a new target.
        if(lastMovingStartAt + 30000 < System.currentTimeMillis()){
            if (GlobalData.DEBUG) System.out.println("[DEBUG][WalkToTargetMonitor] Failsafe. We dont move for 30 seconds, stop all movements and get a new target.");

            lastMovingStartAt = System.currentTimeMillis();
            MovementController.stopAllMovements();

            EntitySearchController.addToIgnoreList(TargetController.getTarget());
            TargetController.setNewTarget(EntitySearchController.getClosestEntity());
        }
    }
}
