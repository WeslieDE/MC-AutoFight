package dev.clatza.mcautofight.Controllers;

import dev.clatza.mcautofight.Executors.EntitySearchExecutor;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class EntitySearchController {
    public static Entity getClosestEntity() {
        if (GlobalData.DEBUG) System.out.println("[DEBUG][EntitySearchController] Get new entity.");

        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return null;

        return EntitySearchExecutor.findEntity(player, false);
    }

//    public static Entity getRandomEntity() {
//        if (GlobalData.DEBUG) System.out.println("[DEBUG][EntitySearchController] Get new random entity.");
//
//        PlayerEntity player = MinecraftClient.getInstance().player;
//        if (player == null) return null;
//
//        return EntitySearchExecutor.findEntity(player, true);
//    }

    public static void addToIgnoreList(Entity entity) {
        if (GlobalData.DEBUG)
            System.out.println("[DEBUG][EntitySearchController] addToIgnoreList: " + entity.toString());

        EntitySearchExecutor.addToIgnoreList(entity);
    }

//    public static void clearIgnoreList(Entity entity) {
//        if (GlobalData.DEBUG)
//            System.out.println("[DEBUG][EntitySearchController] addToIgnoreList: " + entity.toString());
//
//        EntitySearchExecutor.clearIgnoreList(entity);
//    }
}
