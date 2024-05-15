package dev.clatza.mcautofight.Executors;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MovementExecutor {
    private static final float SMOOTH_FACTOR = 0.05F;

    public static void changeDirection(PlayerEntity player, Vec3d targetPos) {
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

    private static float getShortestAngle(float current, float target) {
        float difference = target - current;
        if (difference < -180.0F) {
            difference += 360.0F;
        } else if (difference > 180.0F) {
            difference -= 360.0F;
        }
        return difference;
    }
}
