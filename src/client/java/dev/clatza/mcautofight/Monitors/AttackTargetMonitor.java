package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.ActionController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class AttackTargetMonitor {
    public void tick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        float cooldownProgress = player.getAttackCooldownProgress(0.0f);
        boolean isAttackReady = cooldownProgress >= 1.0f;

        if (!isAttackReady) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;

        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hit).getEntity();

            if (!entity.isLiving()) return;
            if (entity instanceof AnimalEntity || entity instanceof Monster) attackEntity(entity);
        }
    }

    private static final double MAX_REACH = 3.0;

    private static void attackEntity(Entity entity) {
        PlayerEntity player = MinecraftClient.getInstance().player;
//        MinecraftClient client = MinecraftClient.getInstance();

        if (player == null) return;
        if (entity.isRemoved()) return;

        Vec3d eyePosition = player.getCameraPosVec(1.0F);
        double distance = eyePosition.distanceTo(entity.getPos());

        if (distance < MAX_REACH) {
            //if(client.interactionManager == null) return;

            //client.interactionManager.attackEntity(player, entity);
            //player.swingHand(Hand.MAIN_HAND);

            ActionController.attack();
        }
    }
}
