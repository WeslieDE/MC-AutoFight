package dev.clatza.mcautofight.Monitors;

import dev.clatza.mcautofight.Controllers.MovementController;
import dev.clatza.mcautofight.Controllers.TargetController;
import dev.clatza.mcautofight.Data.MovementType;
import dev.clatza.mcautofight.GlobalData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;

public class LookAtTargetMonitor {
    private float lastTimeSet = 0;

    public void tick() {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null) return;

        if(TargetController.getTarget() == null) return;
        if (TargetController.getTarget().isRemoved()) return;

        if(lastTimeSet + 100 < System.currentTimeMillis()) return;
        lastTimeSet = System.currentTimeMillis();

        if (GlobalData.DEBUG) System.out.println("[DEBUG][LookAtTargetMonitor] 1");


        double distanceToEntity = player.getPos().distanceTo(TargetController.getTarget().getPos());
        if(distanceToEntity < 10 && MovementController.getMovementType() == MovementType.STRAIGHT){
            MovementController.moveToDirection(TargetController.getTarget().getPos());
        }

        player.getWorld().addParticle(ParticleTypes.SMOKE, TargetController.getTarget().getPos().x, TargetController.getTarget().getPos().y, TargetController.getTarget().getPos().z, 0, 0, 0);
    }
}
