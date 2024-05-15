package dev.clatza.mcautofight.Executors;

import dev.clatza.mcautofight.Controllers.KeyBindingController;
import dev.clatza.mcautofight.Controllers.TargetController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class ActionExecutor {
    private static final int WAIT_TIME = 150;

    public static void doAttack() throws InterruptedException {
        attackEntity(TargetController.getTarget());
    }

    public static void doJump() throws InterruptedException {
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.jumpKey, true);
        Thread.sleep(WAIT_TIME);
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.jumpKey, false);
    }

    public static void doSprint() throws InterruptedException {
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.sprintKey, true);
        Thread.sleep(WAIT_TIME);
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.sprintKey, false);
    }

    public static void doScreenshot() throws InterruptedException {
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.screenshotKey, true);
        Thread.sleep(WAIT_TIME);
        KeyBindingController.setKeyBindingPressed(MinecraftClient.getInstance().options.sprintKey, false);
    }

    private static final double MAX_REACH = 3.0;
    private static void attackEntity(Entity entity) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) return;
        if(entity.isRemoved()) return;

        MinecraftClient client = MinecraftClient.getInstance();

        Vec3d eyePosition = player.getCameraPosVec(1.0F);
        double distance = eyePosition.distanceTo(entity.getPos());

        if (distance < MAX_REACH) {
            if(client.interactionManager == null) return;

            client.interactionManager.attackEntity(player, entity);
            player.swingHand(Hand.MAIN_HAND);
        }
    }
}
