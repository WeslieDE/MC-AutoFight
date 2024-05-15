package dev.clatza.mcautofight.Controllers;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import dev.clatza.mcautofight.Data.MovementType;
import dev.clatza.mcautofight.Executors.KeyBindingExecutor;
import dev.clatza.mcautofight.Executors.MovementExecutor;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class MovementController {
    private static MovementType currentMovementType = MovementType.NONE;

    public static void startPathfinding(GoalXZ goal) {
        if(currentMovementType != MovementType.NONE) return;
        if (GlobalData.DEBUG) System.out.println("[DEBUG][MovementController] Starting pathfinding to: " + goal.toString());

        currentMovementType = MovementType.PATHFINDING;
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(goal);
    }

    public static void stopPathfinding() {
        if(currentMovementType != MovementType.PATHFINDING)return;
        if (GlobalData.DEBUG) System.out.println("[DEBUG][MovementController] Stopping pathfinding");

        currentMovementType = MovementType.NONE;
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);
    }

    public static void startMovementForward() {
        if(currentMovementType != MovementType.NONE) return;
        if (GlobalData.DEBUG) System.out.println("[DEBUG][MovementController] Starting movement forward");

        currentMovementType = MovementType.STRAIGHT;
        KeyBindingExecutor.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, true);
    }

    public static void stopMovementForward() {
        if(currentMovementType != MovementType.STRAIGHT) return;
        if (GlobalData.DEBUG) System.out.println("[DEBUG][MovementController] Stopping movement forward");

        currentMovementType = MovementType.NONE;
        KeyBindingExecutor.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, false);
    }

    public static MovementType getMovementType() {
        return currentMovementType;
    }

//    public static void setStartPosition(Vec3d pos){
//        //TODO: Implement saveStartPosition
//    }
//
//    public static void returnToStartPosition(){
//        //TODO: Implement returnToStartPosition
//    }

    public static void moveToDirection(Vec3d cord) {
        //if(GlobalData.DEBUG)System.out.println("[DEBUG][MovementController] moveToDirection: " + cord.toString());
        if(currentMovementType != MovementType.STRAIGHT) return;

        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        MovementExecutor.changeDirection(player, cord);
    }

    public static void stopAllMovements() {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][MovementController] Stop all movements");
        System.out.println("[DEBUG][MovementController] state: " + currentMovementType.toString());
        stopPathfinding();
        stopMovementForward();
    }
}
