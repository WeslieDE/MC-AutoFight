package dev.clatza.mcautofight;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalXZ;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
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
    private static String currentEntityTarget = "";
    private static Vec3d currentPosTarget = Vec3d.ZERO;

    public static void registerGameEvents(){
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

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!GlobalData.isAttacking) return;

            PlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null) return;

            float cooldownProgress = player.getAttackCooldownProgress(0.0f);
            boolean isAttackReady = cooldownProgress >= 1.0f;

            if (!isAttackReady) {
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

        WorldRenderEvents.AFTER_ENTITIES.register(MovementHelper::afterEntities);
    }

    public static void attackEntity(Entity entity) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        MinecraftClient client = MinecraftClient.getInstance();

        if(player == null) return;
        if(entity.isRemoved()) return;

        Vec3d eyePosition = player.getCameraPosVec(1.0F);
        double distance = eyePosition.distanceTo(entity.getPos());

        if (distance < MAX_REACH) {
            if(client.interactionManager == null) return;

            client.interactionManager.attackEntity(player, entity);
            player.swingHand(Hand.MAIN_HAND);
            GlobalData.killCounter++;
        }
    }

    private static void afterEntities(WorldRenderContext context) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (!GlobalData.isAttacking) return;
        if (GlobalData.currentTargetEntity == null) return;
        if (player == null) return;

        Entity entity = GlobalData.currentTargetEntity;
        double distanceToTarget = player.getPos().distanceTo(currentPosTarget);
        double distanceToEntity = player.getPos().distanceTo(entity.getPos());

        if(GlobalData.DEBUG)System.out.println("Distance To Entity: " + distanceToEntity);
        if(GlobalData.DEBUG)System.out.println("Distance To Target: " + distanceToTarget);

        if (distanceToTarget >= 5 && distanceToEntity >= 5) {
            KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, false);

            if (!GlobalData.currentTargetEntity.getEntityName().equals(currentEntityTarget)) {
                if(GlobalData.DEBUG)System.out.println("Start new Pathfinding");
                currentPosTarget = GlobalData.currentTargetEntity.getPos();
                currentEntityTarget = GlobalData.currentTargetEntity.getEntityName();
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalXZ((int) GlobalData.currentTargetEntity.getPos().x, (int) GlobalData.currentTargetEntity.getPos().z));
                return;
            }

            return;
        }

        if (distanceToTarget <= 5 && distanceToEntity >= 5) {
            if(GlobalData.DEBUG)System.out.println("Target reached, but entity not reached yet");
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);
            currentPosTarget = GlobalData.currentTargetEntity.getPos();
            currentEntityTarget = "";
            return;
        }

        if (distanceToTarget >= 500 && distanceToEntity <= 25) {
            if(GlobalData.DEBUG)System.out.println("Invalid Target, too far away");
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);
            currentPosTarget = GlobalData.currentTargetEntity.getPos();
            currentEntityTarget = "";
            return;
        }

        if (distanceToTarget <= 5 && distanceToEntity <= 5) {
            currentPosTarget = Vec3d.ZERO;
            currentEntityTarget = "";

            //KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, true);
            BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);

            if (distanceToEntity >= 1) {
                if (GlobalData.currentTargetEntity.getY() > player.getY() + 1.5) {
                    if(GlobalData.DEBUG)System.out.println("Entity is above player");
                    GlobalData.entityIgnoreList.add(GlobalData.currentTargetEntity.getId());
                    GlobalData.currentTargetEntity = null;
                    currentPosTarget = Vec3d.ZERO;
                    KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, false);
                }else{
                    KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, true);
                }
            }else{
                if(GlobalData.DEBUG)System.out.println("Reached target entity");
                KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, false);
            }
        }
    }
}
