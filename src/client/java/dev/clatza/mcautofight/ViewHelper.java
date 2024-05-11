package dev.clatza.mcautofight;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ViewHelper {
    private static final float SMOOTH_FACTOR = 0.05F;

    public static void registerGameEvents(){
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!GlobalData.isAttacking) return;

            if (GlobalData.currentTargetEntity == null){
                GlobalData.currentTargetEntity = findAnimal(MinecraftClient.getInstance().player, false);

                if(GlobalData.currentTargetEntity != null) System.out.println("New target Enemy: " + GlobalData.currentTargetEntity.getEntityName());
            }

            if (GlobalData.currentTargetEntity == null) return;
            if (!GlobalData.currentTargetEntity.isLiving()) {GlobalData.currentTargetEntity = null; return; }
            if (GlobalData.currentTargetEntity.isRemoved()) {GlobalData.currentTargetEntity = null; return; }

            changeLookDirection(MinecraftClient.getInstance().player, GlobalData.currentTargetEntity.getPos());

            if(GlobalData.lastEnemyFoundAt + 10000 < System.currentTimeMillis()){
                GlobalData.entityIgnoreList.add(GlobalData.currentTargetEntity.getId());
                GlobalData.currentTargetEntity = findAnimal(MinecraftClient.getInstance().player, true);
            }
        });
    }

    public static void changeLookDirection(PlayerEntity player, Vec3d targetPos) {
        if (player == null) return;

        player.getWorld().addParticle(ParticleTypes.SMOKE, targetPos.x, targetPos.y, targetPos.z, 0, 0, 0);

        Vec3d eyePos = player.getCameraPosVec(1.0F);
        Vec3d toTarget = targetPos.subtract(eyePos).normalize();

        float targetYaw = (float) Math.toDegrees(MathHelper.atan2(toTarget.z, toTarget.x)) - 90.0F;
        float targetPitch = (float) Math.toDegrees(-MathHelper.atan2(toTarget.y, Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z)));

        float newYaw = player.getYaw() + getShortestAngle(player.getYaw(), targetYaw) * SMOOTH_FACTOR;
        float newPitch = MathHelper.lerp(SMOOTH_FACTOR, player.getPitch(), targetPitch);

        player.setYaw(newYaw % 360.0F);
        player.setPitch(newPitch);
    }

    private static Entity findAnimal(PlayerEntity player, boolean random) {
        if(player == null) return null;

        Box searchBox = new Box(player.getBlockPos()).expand(60);
        List<AnimalEntity> animals = player.getWorld().getEntitiesByClass(AnimalEntity.class, searchBox,
                animal -> Math.abs(animal.getY() - player.getY()) <= 6 && !GlobalData.entityIgnoreList.contains(animal.getId()) && TeleportMonitor.isInSafeZone(animal.getPos()));

        if (animals.isEmpty()) return null;

        GlobalData.lastEnemyFoundAt = System.currentTimeMillis();

        if (random) {
            Random rand = new Random();
            return animals.get(rand.nextInt(animals.size()));
        } else {
            return animals.stream()
                    .min(Comparator.comparingDouble(a -> a.squaredDistanceTo(player)))
                    .orElse(null);
        }
    }

    public static float getShortestAngle(float current, float target) {
        float difference = target - current;
        if (difference < -180.0F) {
            difference += 360.0F;
        } else if (difference > 180.0F) {
            difference -= 360.0F;
        }
        return difference;
    }
}
