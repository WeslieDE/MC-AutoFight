package dev.clatza.mcautofight;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class MovementHelper {
    private static final double MAX_REACH = 3.0;

    public static void registerGameEvents(){
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!GlobalData.isAttacking) return;

            PlayerEntity player = MinecraftClient.getInstance().player;
            float cooldownProgress = player.getAttackCooldownProgress(0.0f);
            boolean isAttrackReady = cooldownProgress >= 1.0f;

            if (!isAttrackReady) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            HitResult hit = client.crosshairTarget;

            if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
                GlobalData.currentTargetEntity = ((EntityHitResult) hit).getEntity();
                if (!GlobalData.currentTargetEntity.isLiving()) return;
                if (GlobalData.currentTargetEntity instanceof AnimalEntity || GlobalData.currentTargetEntity instanceof Monster) attackEntity(GlobalData.currentTargetEntity);
                GlobalData.currentTargetEntity = null;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = MinecraftClient.getInstance().player;

            if (!GlobalData.isAttacking) return;
            if (GlobalData.currentTargetEntity == null) return;
            if (player == null) return;

            //if(GlobalData.currentTargetEntety == null){
            //KeyBinding forwardKey = client.options.forwardKey;
            //KeyBindingHelper.setKeyBindingPressed(forwardKey, false);
            //System.out.println("Stop moving forward; No target");
            //return;
            //}

            double distance = player.distanceTo(GlobalData.currentTargetEntity);

            if (distance > 1.0) {
                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBindingHelper.setKeyBindingPressed(forwardKey, true);
                //System.out.println("Start moving forward; distance: " + distance);
                return;
            }else{
                KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
                KeyBindingHelper.setKeyBindingPressed(forwardKey, false);
                //System.out.println("Stop moving forward: distance: " + distance);
                return;
            }
        });
    }

    public static void attackEntity(Entity entity) {
        if(entity.isRemoved()) return;

        PlayerEntity player = MinecraftClient.getInstance().player;
        MinecraftClient client = MinecraftClient.getInstance();

        Vec3d eyePosition = player.getCameraPosVec(1.0F);
        double distance = eyePosition.distanceTo(entity.getPos());

        if (distance < MAX_REACH) {
            client.interactionManager.attackEntity(player, entity);
            player.swingHand(Hand.MAIN_HAND);
            GlobalData.killCounter++;
        }
    }
}
