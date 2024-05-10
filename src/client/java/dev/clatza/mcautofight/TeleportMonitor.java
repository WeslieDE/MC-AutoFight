package dev.clatza.mcautofight;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TeleportMonitor {
    private static Vec3d lastPosition = Vec3d.ZERO;

    private static final int SEARCH_RADIUS = 50;
    private static final int BUFFER_ZONE = 15;
    private static final List<Box> portalSafeZones = new ArrayList<>();

    private static boolean lastState = false;

    public static void registerGameEvents(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(!GlobalData.isAttacking) return;
            if (MinecraftClient.getInstance().player == null) return;

            if(lastPosition == Vec3d.ZERO)
                lastPosition = MinecraftClient.getInstance().player.getPos();

            if(MinecraftClient.getInstance().player.getPos().distanceTo(lastPosition) > 20){
                System.out.println("Teleport detected");
                KeyBindingHelper.setGlobalAttackMode(false);
             }

            if(GlobalData.isAttacking != lastState){
                lastState = GlobalData.isAttacking;
                detectPortals(MinecraftClient.getInstance().player.getWorld(), MinecraftClient.getInstance().player.getPos());
            }

            lastPosition = MinecraftClient.getInstance().player.getPos();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> MinecraftClient.getInstance().execute(() -> KeyBindingHelper.setGlobalAttackMode(false)));
    }

    public static void detectPortals(World world, Vec3d playerPos) {
        if(world == null) return;

        BlockPos.Mutable searchPos = new BlockPos.Mutable();
        for (int x = -SEARCH_RADIUS; x <= SEARCH_RADIUS; x++) {
            for (int y = -SEARCH_RADIUS; y <= SEARCH_RADIUS; y++) {
                for (int z = -SEARCH_RADIUS; z <= SEARCH_RADIUS; z++) {
                    searchPos.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                    if (world.getBlockState(searchPos).getBlock() == Blocks.NETHER_PORTAL) {
                        Box portalBox = new Box(searchPos).expand(BUFFER_ZONE);
                        portalSafeZones.add(portalBox);
                    }
                }
            }
        }
    }

    public static boolean isInSafeZone(Vec3d position) {
        for (Box safeZone : portalSafeZones) {
            if (safeZone.contains(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5)) {
                return false;
            }
        }
        return true;
    }
}
