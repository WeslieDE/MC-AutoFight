package dev.clatza.mcautofight;

import baritone.api.BaritoneAPI;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
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
    private static Vec3d LastKnownPosition = Vec3d.ZERO;

    public static void registerGameEvents(){
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (!GlobalData.isAttacking) return;

            PlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null) return;

            if (GlobalData.currentTargetEntity == null){
                GlobalData.currentTargetEntity = findAnimal(MinecraftClient.getInstance().player, false);

                //if(GlobalData.currentTargetEntity != null) System.out.println("New target Enemy: " + GlobalData.currentTargetEntity.getEntityName());
            }

            if (GlobalData.currentTargetEntity == null) return;
            if (!GlobalData.currentTargetEntity.isLiving()) {GlobalData.currentTargetEntity = null; return; }
            if (GlobalData.currentTargetEntity.isRemoved()) {GlobalData.currentTargetEntity = null; return; }

            Entity entity = GlobalData.currentTargetEntity;
            double distance = player.getPos().distanceTo(entity.getPos());
            double distanceToLastPos = player.getPos().distanceTo(LastKnownPosition);
            if(distance < 6) changeLookDirection(MinecraftClient.getInstance().player, GlobalData.currentTargetEntity.getPos());

            if((GlobalData.lastEnemyFoundAt + 3000 < System.currentTimeMillis() && distanceToLastPos < 2) || GlobalData.lastEnemyFoundAt + 30000 < System.currentTimeMillis()){
            if(GlobalData.lastEnemyFoundAt + 30000 < System.currentTimeMillis()){
                if(GlobalData.DEBUG)System.out.println("Dont move; Running failsafe");
                KeyBindingHelper.setKeyBindingPressed(MinecraftClient.getInstance().options.forwardKey, false);
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(null);
                GlobalData.entityIgnoreList.add(GlobalData.currentTargetEntity.getId());
                GlobalData.currentTargetEntity = findAnimal(MinecraftClient.getInstance().player, true);
            }

            LastKnownPosition = GlobalData.currentTargetEntity.getPos();
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

        Box searchBox = new Box(player.getBlockPos()).expand(40);
        List<AnimalEntity> animals = player.getWorld().getEntitiesByClass(AnimalEntity.class, searchBox,
                animal -> !GlobalData.entityIgnoreList.contains(animal.getId()));

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
